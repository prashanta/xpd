package com.gemcity.xpd.utility;

public class ExportUtil {
	
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
	
	public static String cleanString(String s){
		String clean = s.replaceAll("'", "''").replaceAll("\\\\", "\\\\\\\\").replaceAll("////", "////////");
		return clean;
	}

	
}