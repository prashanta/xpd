package com.gemcity.xpd.beans;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.gemcity.xpd.utility.ExportUtil;
import com.gemcity.xpd.utility.ProgressBar;

public class ExportProfile {
	
	static Logger log = Logger.getLogger(ExportProfile.class);
	
	private String exportTableName;
	private String exportColumns[];	
	private String whereClause;
	

	private String importTableName;
	private String importColumns[];	
	
	private String referenceTableName;
	private String referenceColumnName;

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
	

	public String getReferenceTableName() {
		return referenceTableName;
	}

	public void setReferenceTableName(String referenceTableName) {
		this.referenceTableName = referenceTableName;
	}

	public String getReferenceColumnName() {
		return referenceColumnName;
	}

	public void setReferenceColumnName(String referenceColumnName) {
		this.referenceColumnName = referenceColumnName;
	}

	public String getExportQuery(){
		String query  = "";
		int c = exportColumns.length - 1;		
		for(String column:exportColumns){	
			query +=  exportTableName+ "." + column + ((c > 0)? ", " : "");
			c--;
		}
		query = "SELECT " + query + " FROM pub." + exportTableName + " " + whereClause;
		return query;
	}

	public ArrayList<String> getImportQuey(ResultSetMetaData meta, ResultSet rs, int size){
		ArrayList<String> query= new ArrayList<String>();
		try{
			String queryPrefix = "INSERT INTO `" + this.getImportTableName() + "` (";
			int b = this.getImportColumns().length - 1;
			for(String column:this.getImportColumns()){
				queryPrefix += "`"+column+"`" + (b > 0? ", " : "");
				b--;
			}
			
			int nCols = meta.getColumnCount();			
			ProgressBar bar = new ProgressBar("Generating import query", size);
			while(rs.next()) {
				int c = importColumns.length - 1;
				String value = "(";
				for(int i=1; i <= nCols; i++){	
					String colType = meta.getColumnClassName(i);
					// String
					if(colType == "java.lang.String"){
						String val = rs.getString(i);
						value += val!=null? ("'" + ExportUtil.cleanString(rs.getString(i)) + "'") : "''";
					}		
					// Integer
					else if(colType == "java.lang.Integer")
						value += rs.getInt(i);
					// Decimal
					else if(colType == "java.math.BigDecimal")
						value += rs.getDouble(i);
					// Date
					else if(colType == "java.sql.Date"){
						Date temp = rs.getDate(i);
						value += temp != null? ("'" + temp.toString() + "'") : "null";
					}
					// Boolean
					else if(colType == "java.lang.Boolean")
						value += "" + rs.getBoolean(i);
					else{
						throw new Exception("xpd: Nothing matching : " + colType);
					}
					value += (c > 0)? ", " : "";
					c--;				
				}					
				query.add(queryPrefix + ") VALUES" + value + ");");
				bar.update();
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return query;
	}
}
