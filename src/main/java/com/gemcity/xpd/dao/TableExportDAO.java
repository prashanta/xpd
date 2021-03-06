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
import com.gemcity.xpd.utility.ExportUtil;
import com.gemcity.xpd.utility.ProgressBar;

import java.sql.Connection;

public class TableExportDAO {

	// Log stuffs
	static Logger log = Logger.getLogger(TableExportDAO.class);

	public static final boolean DEBUG = false;
	
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
	public void run(boolean isFileOnly, boolean isSilentMode, String dir) throws FileNotFoundException, UnsupportedEncodingException{
		ArrayList<String> data = export(isSilentMode);
		if(data.size()>0){
			saveToFile(data, dir);
			if(!isFileOnly)
				importTableBatch(data, isSilentMode);
			log.info("Completed importing : " + this.profile.getExportTableName());
		}
		else{
			log.error("No data retrived for table : " + this.profile.getExportTableName());
		}	
	}

	/*
	 * Get export data from Progress database.
	 */
	private ArrayList<String> export(boolean isSilentMode){
		
		ArrayList<String> data = new ArrayList<String>();
		
		Connection conn = null;
		long startTime = System.nanoTime();
		try{
			
			if(!isSilentMode){
				System.out.println("\n-----------------------------------------");
				log.info("Exporting table - " + profile.getExportTableName());
			}
			
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
			if(!isSilentMode)
				log.info("Retrived ["+ size +"] records in : "+ (System.nanoTime() - startTime)/1000000000 + " sec");

			// Get result set meta data - for debug purposes
			ResultSetMetaData meta = rs.getMetaData();
			
			if(DEBUG)	
				ExportUtil.printColumnInfo(meta);
			
			startTime = System.nanoTime();

			// Generate import SQL and write to file
			data = profile.getImportQuey(meta, rs, size, isSilentMode);
			
			if(!isSilentMode)
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

	/*
	 * Save exported data to a file
	 */
	private void saveToFile(ArrayList<String> data, String dir) throws FileNotFoundException, UnsupportedEncodingException{
		String path = dir==null? "" : dir + "\\"; 
		PrintWriter writer = new PrintWriter(path + profile.getExportTableName() + ".sql", "UTF-8");
		for(String row : data)
			writer.println(row);
		writer.close();
	}

	
	/*
	 * Bulk import data to MySQL database
	 */
	private void importTableBatch(ArrayList<String> data, boolean isSilentMode){
		Connection conn = null;
		try{	
			// Get DB connection
			conn = (Connection) importDataSource.getConnection();
			Statement s = conn.createStatement();             
			
			// First clean up the table and truncate
			s.executeUpdate("DELETE FROM `"+ profile.getImportTableName() + "` WHERE 1");
			s.executeUpdate("TRUNCATE TABLE " + profile.getImportTableName());
			
			s.executeUpdate("SET foreign_key_checks = 0");
			s.executeUpdate("SET UNIQUE_CHECKS = 0");
			s.executeUpdate("SET AUTOCOMMIT = 0;");
			
			s.executeUpdate("DELETE FROM `"+ profile.getImportTableName() + "` WHERE 1");
			s.executeUpdate("DELETE FROM `"+ profile.getImportTableName() + "` WHERE 1");
			long startTime = System.nanoTime();
			ProgressBar bar = new ProgressBar("Populating table " + profile.getImportTableName(), data.size());
			int i=0;
			for(String row : data){
				s.addBatch(row);
                if(i%100 ==0) {
                	s.executeBatch();
                	if(!isSilentMode)
                		bar.update(i);
                }
                i++;
			}			
			s.executeBatch();
			
			s.executeUpdate("SET foreign_key_checks = 1");
			s.executeUpdate("SET UNIQUE_CHECKS = 1");
			s.executeUpdate("SET AUTOCOMMIT = 1;");
			
			if(!isSilentMode){
				bar.update(i);
				log.info("Imported in : "+ (System.nanoTime() - startTime)/1000000000 + " sec");
			}
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
}
