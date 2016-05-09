package com.gemcity.xpd.dao;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.gemcity.xpd.beans.ExportProfile;
import com.gemcity.xpd.utility.ProgressBar;

import java.sql.Connection;

public class TableExportDAO {

	// Log stuffs
	static Logger log = Logger.getLogger(TableExportDAO.class);


	private DataSource exportDataSource;
	private DataSource importDataSource;
	private ExportProfile profile;

	public void setExportDataSource(DataSource exportDataSource) {
		this.exportDataSource = exportDataSource;
	}

	public void setImportDataSource(DataSource importDataSource) {
		this.importDataSource = importDataSource;
	}
	
	public void setProfile(ExportProfile profile){
		this.profile = profile;
	}
	
	/*
	 * Run the exporter.
	 */
	public void run(boolean fileOnly) throws FileNotFoundException, UnsupportedEncodingException{
		ArrayList<String> data = export();
		saveToFile(data);
		if(!fileOnly)
			importTableBatch(data);
	}

	/*
	 * Get export data from Progress database.
	 */
	private ArrayList<String> export(){
		ArrayList<String> data = new ArrayList<String>();
		
		
		Connection conn = null;
		long startTime = System.nanoTime();
		try{
			
			System.out.println("\n##########################");
			System.out.println("EXPORTING TABLE : " + profile.getExportTableName());
			
			// Get DB connection
			conn = (Connection) exportDataSource.getConnection();
			
			// Get export query
			String exportQuery = profile.getExportQuery();
			log.debug("SQL: " + exportQuery);

			// Execute query
			Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = s.executeQuery(exportQuery);

			// Get number of records 
			rs.last();
			int size = rs.getRow();
			rs.beforeFirst();			
			System.out.println("Retrived ["+ size +"] records in : "+ (System.nanoTime() - startTime)/1000000000 + " sec");

			// Get result set meta data - for debug purposes
			ResultSetMetaData meta = rs.getMetaData();		
			int NCols = meta.getColumnCount();   // # of attributes in result
			
			// START debug stuff
			log.debug("Name \t\t\t Type \t DispSz \t Scale \t Precis \t ClassName"); 
			log.debug("-----------------------------------------------");
			for (int i = 1; i <= NCols; i++)
				log.debug(meta.getColumnName(i) + "\t\t\t" + meta.getColumnTypeName(i) + "\t" + meta.getColumnDisplaySize(i) + "\t" + meta.getScale(i) + "\t" + meta.getPrecision(i) + "\t" + meta.getColumnClassName(i) + "\t");
			// END debug stuff
			
			startTime = System.nanoTime();

			// Generate import SQL and write to file
			data = profile.getImportQuey(meta, rs, size);

			System.out.println("Generated import query in : "+ (System.nanoTime() - startTime)/1000000000 + " sec");
		}		
		catch(Exception ex){
			ex.printStackTrace();			
		}	
		finally {
			if (conn != null) {
				try {
					conn.close();					
				} 
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return data;
	}

	/*
	 * Save exported data to a file
	 */
	private void saveToFile(ArrayList<String> data) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter(profile.getExportTableName() + ".sql", "UTF-8");
		for(String row : data)
			writer.println(row);
		writer.close();
	}

	/*
	 * Import data to MySQL database
	 */
	private void importTable(ArrayList<String> data){
		Connection conn = null;
		try{			
			// Get DB connection
			conn = (Connection) importDataSource.getConnection();			
			Statement s = conn.createStatement();
			System.out.println("Importing table : " + profile.getImportTableName());
			
			// First clean up the table and truncate
			s.executeUpdate("DELETE FROM `"+ profile.getImportTableName() + "` WHERE 1;" +
					" TRUNCATE TABLE " + profile.getImportTableName() + ";");
			
			long startTime = System.nanoTime();
			ProgressBar bar = new ProgressBar(data.size());
			for(String row : data){
				String query = row;	
				s.executeUpdate(query);
				bar.update();
			}
			System.out.println("Imported table in : "+ (System.nanoTime() - startTime)/1000000000 + " sec");
		}		
		catch(Exception ex){
			ex.printStackTrace();			
		}	
		finally {
			if (conn != null) {
				try {
					conn.close();					
				} 
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	/*
	 * Bulk import data to MySQL database
	 */
	private void importTableBatch(ArrayList<String> data){
		Connection conn = null;
		try{	
			// Get DB connection
			conn = (Connection) importDataSource.getConnection();
			Statement s = conn.createStatement();             
			System.out.println("Populating table : " + profile.getImportTableName());
			
			// First clean up the table and truncate
			s.executeUpdate("DELETE FROM `"+ profile.getImportTableName() + "` WHERE 1");
			s.executeUpdate("TRUNCATE TABLE " + profile.getImportTableName());
			
			long startTime = System.nanoTime();
			ProgressBar bar = new ProgressBar(data.size());
			int i=0;
			for(String row : data){
				s.addBatch(row);
                if(i%100 ==0) {
                	s.executeBatch();
                	bar.update(i);
                }
                i++;
			}	
			s.executeBatch();
			bar.update(i);
			System.out.println("Imported in : "+ (System.nanoTime() - startTime)/1000000000 + " sec");
		}		
		catch(Exception ex){
			ex.printStackTrace();			
		}	
		finally {
			if (conn != null) {
				try {
					conn.close();					
				} 
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	/*private void checkImportTable(){
		Connection conn = null;
		try{			
			// Get DB connection
			conn = (Connection) importDataSource.getConnection();
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SHOW TABLES LIKE '"+ profile.getImportTableName() +"'");
			if(rs.next()){
				log.info("Table exists!");
			}
			else 
				log.error("No table found");					
		}		
		catch(Exception ex){
			ex.printStackTrace();			
		}	
		finally {
			if (conn != null) {
				try {
					conn.close();					
				} 
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}*/
}
