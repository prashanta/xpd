package com.gemcity.xpd.beans;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class ExportProfile {
	
	private String exportTableName;
	private String exportColumns[];	
	private String whereClause;

	private String importTableName;
	private String importColumns[];	

	public String getExportTableName() {
		return exportTableName;
	}

	public void setExportTableName(String exportTableName) {
		this.exportTableName = exportTableName;
	}

	public String[] getExportColumns() {
		return exportColumns;
	}

	public void setExportColumns(String[] exportColumns) {
		this.exportColumns = exportColumns;
	}

	public String getWhereClause() {
		if(whereClause == null)
			return "";
		else
			return whereClause;
	}

	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	public String getImportTableName() {
		return importTableName;
	}

	public void setImportTableName(String importTableName) {
		this.importTableName = importTableName;
	}

	public String[] getImportColumns() {
		return importColumns;
	}

	public void setImportColumns(String[] importColumns) {
		this.importColumns = importColumns;
	}

	public String getExportQuery(){
		String query  = "SELECT ";
		int count = exportColumns.length-1;
		for(String column:exportColumns){	
			query +=  exportTableName+ "." + column;
			if(count>0)
				query += ", ";
			count--;
		}
		query += " FROM pub." + exportTableName + " " + whereClause;	
		return query;
	}

	public String getImportQuey(ResultSetMetaData meta, ResultSet rs){
		String query = "";
		String queryPrefix = "INSERT INTO `" + importTableName + "` (";
		
		try{
			int count = importColumns.length - 1;
			for(String column:importColumns){
				queryPrefix += "`"+column+"`";
				
				
				if(count>0)
					queryPrefix += ", ";
				count--;
			}
			queryPrefix += ") VALUES (";

			int nCols = meta.getColumnCount();

			while(rs.next()) {
				count = importColumns.length - 1;
				String value = "";
				for(int i=1; i <= nCols; i++){	
					String colType = meta.getColumnClassName(i); 
					if(colType == "java.lang.String")
						value += "'" + rs.getString(i).replaceAll("'", "''") + "'"; 
					else if(colType == "java.lang.Boolean")
						value += "" + rs.getBoolean(i);
					else
						System.out.println("Nothing matching : " + colType);
					if(count>0)
						value += ", ";
					count--;
				}	
				query += queryPrefix + value + "); \n";
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return query;
	}
}
