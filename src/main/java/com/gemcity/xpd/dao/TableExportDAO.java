package com.gemcity.xpd.dao;


import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.gemcity.xpd.beans.ExportProfile;
import java.sql.Connection;

public class TableExportDAO {

	private DataSource exportDataSource;

	static Logger log = Logger.getLogger(TableExportDAO.class);

	public void setExportDataSource(DataSource exportDataSource) {
		this.exportDataSource = exportDataSource;
	}

	public void export(ExportProfile profile){
		Connection conn = null;
		long startTime = System.nanoTime();
		try{			
			// Get DB connection
			conn = (Connection) exportDataSource.getConnection();
			log.info(">>>> Exporting table : " + profile.getExportTableName());
			String exportSQL = profile.getExportQuery();
			log.debug("SQL: " + exportSQL);
			Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = s.executeQuery(exportSQL);
			
			// Get number of records 
			rs.last();
			log.info("Retrived ["+ rs.getRow() +"] records in : "+ (System.nanoTime() - startTime)/1000000000 + " sec");
			rs.beforeFirst();			
			
			// Get result set meta data
			ResultSetMetaData meta = rs.getMetaData();		
			int NCols = meta.getColumnCount();   // # of attributes in result
			log.debug("Name \t\t\t Type \t DispSz \t Scale \t Precis \t ClassName"); 
			log.debug("-----------------------------------------------");
			for (int i = 1; i <= NCols; i++)
				log.debug(meta.getColumnName(i) + "\t\t\t" + meta.getColumnTypeName(i) + "\t" + meta.getColumnDisplaySize(i) + "\t" + meta.getScale(i) + "\t" + meta.getPrecision(i) + "\t" + meta.getColumnClassName(i) + "\t");
			
			startTime = System.nanoTime();
			
			// Generate import SQL and write to file
			String sql = profile.getImportQuey(meta, rs);
			PrintWriter writer = new PrintWriter(profile.getExportTableName() + ".sql", "UTF-8");
			writer.println(sql);
			writer.close();						
			log.info("Wrote SQL to file in : "+ (System.nanoTime() - startTime)/1000000000 + " sec");

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
