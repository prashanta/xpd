package com.gemcity.xpd;



import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gemcity.xpd.dao.TableExportDAO;

public class App
{
    
    public static void main( String[] args )
    {   
    	// App expects table names in arguments
    	if(args.length < 1)
    		System.out.println("Usage: xpd [<Table1> <Table2> ...]");
    	else{
    		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    		
    		try {
	    		
	    		for(String table : args){
	    			if(table.equals("Part")){
	    				TableExportDAO part = (TableExportDAO)context.getBean("partTableExportDAO");
	    	    		part.run();    				
	    			}
	    			else if(table.equals("PartMtl")){
	    				TableExportDAO partMtl = (TableExportDAO)context.getBean("partMtlTableExportDAO");
	    				partMtl.run();
	    			}
	    			else if(table.equals("PartRev")){
	    				TableExportDAO partRev = (TableExportDAO)context.getBean("partRevTableExportDAO");
	    				partRev.run();
	    			}
	    			else{
	    				System.out.println("ERROR: Unrecognized table:" + table);
	    				break;
	    			}	    				
	    		}
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }
}
