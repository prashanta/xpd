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
import com.mysql.jdbc.PreparedStatement;

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
	

	public void run() throws FileNotFoundException, UnsupportedEncodingException{
		ArrayList<String> data = export();
		saveToFile(data);
		importTable(data);
	}

	private ArrayList<String> export(){
		ArrayList<String> data = new ArrayList<String>();

		Connection conn = null;
		long startTime = System.nanoTime();
		try{			
			// Get DB connection
			conn = (Connection) exportDataSource.getConnection();
			String exportQuery = profile.getExportQuery();
			log.info(">>>> Exporting table : " + profile.getExportTableName());			
			log.debug("SQL: " + exportQuery);
			Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = s.executeQuery(exportQuery);

			// Get number of records 
			rs.last();
			int size = rs.getRow();
			log.info("Retrived ["+ size +"] records in : "+ (System.nanoTime() - startTime)/1000000000 + " sec");
			rs.beforeFirst();			

			// Get result set meta data - for debug perposes
			ResultSetMetaData meta = rs.getMetaData();		
			int NCols = meta.getColumnCount();   // # of attributes in result
			log.debug("Name \t\t\t Type \t DispSz \t Scale \t Precis \t ClassName"); 
			log.debug("-----------------------------------------------");
			for (int i = 1; i <= NCols; i++)
				log.debug(meta.getColumnName(i) + "\t\t\t" + meta.getColumnTypeName(i) + "\t" + meta.getColumnDisplaySize(i) + "\t" + meta.getScale(i) + "\t" + meta.getPrecision(i) + "\t" + meta.getColumnClassName(i) + "\t");

			startTime = System.nanoTime();

			// Generate import SQL and write to file
			data = profile.getImportQuey(meta, rs, size);

			log.info("Generated import query in : "+ (System.nanoTime() - startTime)/1000000000 + " sec");
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

	private void saveToFile(ArrayList<String> data) throws FileNotFoundException, UnsupportedEncodingException{
		long startTime = System.nanoTime();

		PrintWriter writer = new PrintWriter(profile.getExportTableName() + ".sql", "UTF-8");
		writer.println(data);
		writer.close();

		log.info("Wrote SQL to file in : "+ (System.nanoTime() - startTime)/1000000000 + " sec");
	}

	private void importTable(ArrayList<String> data){
		Connection conn = null;
		try{			
			// Get DB connection
			conn = (Connection) importDataSource.getConnection();			
			Statement s = conn.createStatement();
			log.info(">>>> Exporting table : " + profile.getImportTableName());
			s.executeUpdate("DELETE FROM `"+ profile.getImportTableName() + "` WHERE 1");
			
			long startTime = System.nanoTime();
			String queryPrefix = "INSERT INTO `" + profile.getImportTableName() + "` (";
			int c = profile.getImportColumns().length - 1;
			for(String column:profile.getImportColumns()){
				queryPrefix += "`"+column+"`";
				queryPrefix += (c > 0)? ", " : "";
				c--;
			}
			queryPrefix += ") VALUES ";
			ProgressBar bar = new ProgressBar(data.size());
			for(String row : data){
				String query = queryPrefix + row;	
				s.executeUpdate(query);
				bar.update();
			}
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
	}
	
	private void importTableBatch(ArrayList<String> data){
		Connection conn = null;
		try{	
			// Get DB connection
			conn = (Connection) importDataSource.getConnection();
			Statement s = conn.createStatement();
             
		    log.info(">>>> Exporting table : " + profile.getImportTableName());
			s.executeUpdate("DELETE FROM `"+ profile.getImportTableName() + "` WHERE 1");
			
			long startTime = System.nanoTime();
			String queryPrefix = "INSERT INTO `" + profile.getImportTableName() + "` ("; 
			int c = profile.getImportColumns().length - 1;
			for(String column:profile.getImportColumns()){
				queryPrefix += "`"+column+"`";
				queryPrefix += (c > 0)? ", " : "";
				c--;
			}
			queryPrefix += ") VALUES ";
			ProgressBar bar = new ProgressBar(data.size());
			int i=0;
			for(String row : data){
				String query = queryPrefix + row;
				s.addBatch(query);
                if(i%100 ==0) {
                	s.executeBatch();
                	bar.update(i);
                }
                i++;
			}	
			s.executeBatch();
			bar.update(i);
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
