package com.gemcity.xpd.beans;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.gemcity.xpd.dao.TableExportDAO;

public class ExportProfile {
	
	static Logger log = Logger.getLogger(ExportProfile.class);
	
	private String exportTableName;
	private String exportColumns[];	
	private String whereClause;

	private String importTableName;
	private String importColumns[];	
	
	public void init() throws Exception{
		if(exportColumns.length != importColumns.length)
			throw new Exception("xpd: Export and Import columns are not equal for ExportProfile with export table name: " + getExportTableName());
	}

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
		int c = exportColumns.length-1;		
		for(String column:exportColumns){	
			query +=  exportTableName+ "." + column;
			query += (c > 0)? ", " : "";
			c--;
		}
		query += " FROM pub." + exportTableName + " " + whereClause;	
		return query;
	}

	public ArrayList<String> getImportQuey(ResultSetMetaData meta, ResultSet rs){
		ArrayList<String> query= new ArrayList<String>();
		
		try{

			int nCols = meta.getColumnCount();

			while(rs.next()) {
				int c = importColumns.length - 1;
				String value = "(";
				for(int i=1; i <= nCols; i++){	
					String colType = meta.getColumnClassName(i); 
					if(colType == "java.lang.String"){
						String val = rs.getString(i);
						value += val!=null? ("'" + rs.getString(i).replaceAll("'", "''") + "'") : "''";
					}						
					else if(colType == "java.lang.Integer")
						value += rs.getInt(i);
					else if(colType == "java.math.BigDecimal")
						value += rs.getDouble(i);
					else if(colType == "java.lang.Boolean")
						value += "" + rs.getBoolean(i);
					else
						log.error("xpd: Nothing matching : " + colType);
					value += (c > 0)? ", " : "";
					c--;
				}	
				query.add(value + ");");				
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return query;
	}
}
