package com.gemcity.xpd;



import java.util.ArrayList;

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
    		System.out.println("where <Table*> is: Part, PartMtl, PartRev, PartPlant, AprvVend, PlantWhse, Vendor, PartOpr, PartBin");
    		System.out.println("--all : Export all tables");
    		System.out.println("--file-only : Save to SQL file only");
    	}
    	else{
    		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    		boolean all = false;
    		boolean fileOnly = false;   
    		ArrayList<String> tables = new ArrayList<String>();
    		try {    			
    			for(String arg : args){
    				if(arg.equals("--all"))
    					all = true;
    				if(arg.equals("--file-only"))
    					fileOnly = true;
    				else
    					tables.add(arg);
    			}
    			
    			if(all){
    				tables = new ArrayList<String>();
    				tables.add("Part");
    				tables.add("PartMtl");
    				tables.add("PartRev");
    				tables.add("PartPlant");
    				tables.add("AprvVend");
    				tables.add("PlantWhse");
    				tables.add("Vendor");
    				tables.add("PartOpr");
    				tables.add("PartBin");    				
    			}
    			long startTime = System.nanoTime();
	    		for(String table : tables){	    			
	    			if(table.equals("Part"))
	    				((TableExportDAO)context.getBean("partTableExportDAO")).run(fileOnly);	    	    		    				
	    			else if(table.equals("PartMtl"))
	    				((TableExportDAO)context.getBean("partMtlTableExportDAO")).run(fileOnly);
	    			else if(table.equals("PartRev"))
	    				((TableExportDAO)context.getBean("partRevTableExportDAO")).run(fileOnly);
	    			else if(table.equals("PartPlant"))
	    				((TableExportDAO)context.getBean("partPlantTableExportDAO")).run(fileOnly);
	    			else if(table.equals("AprvVend"))
	    				((TableExportDAO)context.getBean("aprvVendTableExportDAO")).run(fileOnly);
	    			else if(table.equals("PlantWhse"))
	    				((TableExportDAO)context.getBean("plantWhseTableExportDAO")).run(fileOnly);
	    			else if(table.equals("Vendor"))
	    				((TableExportDAO)context.getBean("vendorTableExportDAO")).run(fileOnly);
	    			else if(table.equals("Vendor"))
	    				((TableExportDAO)context.getBean("vendorTableExportDAO")).run(fileOnly);
	    			else if(table.equals("PartOpr"))
	    				((TableExportDAO)context.getBean("partOprTableExportDAO")).run(fileOnly);
	    			else if(table.equals("PartBin"))
	    				((TableExportDAO)context.getBean("partBinTableExportDAO")).run(fileOnly);
	    			else{
	    				System.out.println("ERROR - TABLE NOT FOUND : " + table);
	    				break;
	    			}	    				
	    		}
	    		long d = (System.nanoTime() - startTime) / 1000000000; // in seconds
	    		String time = "";
	    		if(d>60){
	    			if(d/60 > 60){
	    				if(d/(60*60) > 24)
	    					time = String.valueOf(d/(60*60*24)) + " days";
	    				else
	    					time = String.valueOf(d/(60*60)) + " min";
	    			}
	    			else
	    				time = String.valueOf(d/60) + " min";	    			
	    		}
	    		else
	    			time = String.valueOf(d) + " sec";
	    			
	    		System.out.println("\nCOMPLETED IN: "+ time);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }
}
