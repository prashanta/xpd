package com.gemcity.xpd.utility;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.log4j.Logger;


public class ExportUtil {
	
	static Logger log = Logger.getLogger(ExportUtil.class);
	
	/**
	 * Generate Export Query
	 * 
	 * @param tableName		Name of table
	 * @param columns		List of columns
	 * @param whereClause	Where close to be used in the query
	 * @return
	 */
	public static String getExportQuery(String tableName, String[] columns, String whereClause){
		String query  = "SELECT ";
		int count = columns.length-1;
		for(String column:columns){	
			query +=  tableName+ "." + column;
			if(count>0)
				query += ", ";
			count--;
		}
		query += " FROM " + tableName + " " + whereClause;	
		return query;
	}
	
	/**
	 * Clean string.
	 * @param s
	 * @return
	 */
	public static String cleanString(String s){
		String clean = s.replaceAll("'", "''").replaceAll("\\\\", "\\\\\\\\").replaceAll("////", "////////");
		return clean;
	}
	
	public static void printColumnInfo(ResultSetMetaData meta){
		try {
			int cols = meta.getColumnCount();
			log.debug("Name \t\t\t Type \t DispSz \t Scale \t Precis \t ClassName"); 
			log.debug("-----------------------------------------------");
			for (int i = 1; i <= cols; i++)
				log.debug(meta.getColumnName(i) + "\t\t\t" + meta.getColumnTypeName(i) + "\t" + meta.getColumnDisplaySize(i) + "\t" + meta.getScale(i) + "\t" + meta.getPrecision(i) + "\t" + meta.getColumnClassName(i) + "\t");
		} catch (SQLException e) {
			e.printStackTrace();
		}			
	}
}