package com.gemcity.xpd;



import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gemcity.xpd.dao.TableExportDAO;

public class App
{
    
    public static void main( String[] args )
    {   
    	// Application expects table names in arguments
    	if(args.length < 1){    		
    		System.out.println("Usage: xpd [<Table1> <Table2> ...] \n");
    		System.out.println("where <Table*> is: Part, PartMtl, PartRev, PartPlant AprvVend, PlantWhse, Vendor, PartOpr");    		
    	}
    	else{
    		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    		
    		try {
	    		
	    		for(String table : args){
	    			if(table.equals("Part"))
	    				((TableExportDAO)context.getBean("partTableExportDAO")).run();	    	    		    				
	    			else if(table.equals("PartMtl"))
	    				((TableExportDAO)context.getBean("partMtlTableExportDAO")).run();
	    			else if(table.equals("PartRev"))
	    				((TableExportDAO)context.getBean("partRevTableExportDAO")).run();
	    			else if(table.equals("PartPlant"))
	    				((TableExportDAO)context.getBean("partPlantTableExportDAO")).run();
	    			else if(table.equals("AprvVend"))
	    				((TableExportDAO)context.getBean("aprvVendTableExportDAO")).run();
	    			else if(table.equals("PlantWhse"))
	    				((TableExportDAO)context.getBean("plantWhseTableExportDAO")).run();
	    			else if(table.equals("Vendor"))
	    				((TableExportDAO)context.getBean("vendorTableExportDAO")).run();
	    			else if(table.equals("Vendor"))
	    				((TableExportDAO)context.getBean("vendorTableExportDAO")).run();
	    			else if(table.equals("PartOpr"))
	    				((TableExportDAO)context.getBean("partOprTableExportDAO")).run();
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
