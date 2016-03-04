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
import java.sql.Connection;

public class TableExportDAO {

	// Log stuffs
	static Logger log = Logger.getLogger(TableExportDAO.class);


	private DataSource exportDataSource;
	private DataSource importDataSource;

	public void setExportDataSource(DataSource exportDataSource) {
		this.exportDataSource = exportDataSource;
	}

	public void setImportDataSource(DataSource importDataSource) {
		this.importDataSource = importDataSource;
	}

	public void run(ExportProfile profile) throws FileNotFoundException, UnsupportedEncodingException{
		ArrayList<String> data = export(profile);
		saveToFile(data, profile.getExportTableName());
		importTable(data, profile);
	}

	private ArrayList<String> export(ExportProfile profile){
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
			log.info("Retrived ["+ rs.getRow() +"] records in : "+ (System.nanoTime() - startTime)/1000000000 + " sec");
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
			data = profile.getImportQuey(meta, rs);

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

	private void saveToFile(ArrayList<String> data, String fileName) throws FileNotFoundException, UnsupportedEncodingException{
		long startTime = System.nanoTime();

		PrintWriter writer = new PrintWriter(fileName + ".sql", "UTF-8");
		writer.println(data);
		writer.close();

		log.info("Wrote SQL to file in : "+ (System.nanoTime() - startTime)/1000000000 + " sec");
	}

	private void importTable(ArrayList<String> data, ExportProfile profile){
		Connection conn = null;
		try{			
			// Get DB connection
			conn = (Connection) importDataSource.getConnection();
			Statement s = conn.createStatement();
			log.info(">>>> Exporting table : " + profile.getImportTableName());
			s.executeUpdate("DELETE FROM `"+ profile.getImportTableName() + "` WHERE 1");

			String queryPrefix = "INSERT INTO `" + profile.getImportTableName() + "` (";
			int c = profile.getImportColumns().length - 1;
			for(String column:profile.getImportColumns()){
				queryPrefix += "`"+column+"`";
				queryPrefix += (c > 0)? ", " : "";
				c--;
			}
			queryPrefix += ") VALUES ";

			for(String row : data){
				String query = queryPrefix + row;	
				s.executeUpdate(query);
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
