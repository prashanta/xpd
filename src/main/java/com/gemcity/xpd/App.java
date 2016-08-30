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
    		System.out.println("where <Table*> is: Part, PartMtl, PartRev, PartPlant, AprvVend, PlantWhse, Vendor, PartOpr, PartBin, PartDtl, POHeader, PODetail, JobHead, RcvDtl, JobMtl, JobAsmbl");
    		System.out.println("--all : Export all tables");
    		System.out.println("--output <dir>: Directory to put output sql files.");
    		System.out.println("--silent : Silent mode");
    		System.out.println("--file-only : Save to SQL file only, skip database import");
    	}
    	else{
    		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    		boolean all = false;
    		boolean isFileOnly = false;   
    		boolean isSilentMode = false;
    		String dir = null;
    		ArrayList<String> tables = new ArrayList<String>();
    		boolean skipNext = false;
    		int index = 0;
    		try {    			
    			for(String arg : args){
    				if(skipNext){
    					skipNext = false;
    					continue;
    				}    					
    				if(arg.equals("--all"))
    					all = true;
    				else if(arg.equals("--file-only"))
    					isFileOnly = true;
    				else if(arg.equals("--silent"))
    					isSilentMode = true;
    				else if(arg.equals("--output")){
    					dir = args[index+1];
    					skipNext = true;
    				}
    				else
    					tables.add(arg);
    				index++;
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
    				tables.add("PartDtl");    				
    				tables.add("POHeader");    				
    				tables.add("PODetail");    				
    				tables.add("JobHead");    				
    				tables.add("RcvDtl");    				
    				tables.add("JobMtl");    				
    				tables.add("JobAsmbl");    				
    			}
    			long startTime = System.nanoTime();
	    		for(String table : tables){	    			
	    			if(table.equals("Part"))
	    				((TableExportDAO)context.getBean("partTableExportDAO")).run(isFileOnly, isSilentMode, dir);	    	    		    				
	    			else if(table.equals("PartMtl"))
	    				((TableExportDAO)context.getBean("partMtlTableExportDAO")).run(isFileOnly, isSilentMode, dir);
	    			else if(table.equals("PartRev"))
	    				((TableExportDAO)context.getBean("partRevTableExportDAO")).run(isFileOnly, isSilentMode, dir);
	    			else if(table.equals("PartPlant"))
	    				((TableExportDAO)context.getBean("partPlantTableExportDAO")).run(isFileOnly, isSilentMode, dir);
	    			else if(table.equals("AprvVend"))
	    				((TableExportDAO)context.getBean("aprvVendTableExportDAO")).run(isFileOnly, isSilentMode, dir);
	    			else if(table.equals("PlantWhse"))
	    				((TableExportDAO)context.getBean("plantWhseTableExportDAO")).run(isFileOnly, isSilentMode, dir);
	    			else if(table.equals("Vendor"))
	    				((TableExportDAO)context.getBean("vendorTableExportDAO")).run(isFileOnly, isSilentMode, dir);
	    			else if(table.equals("Vendor"))
	    				((TableExportDAO)context.getBean("vendorTableExportDAO")).run(isFileOnly, isSilentMode, dir);
	    			else if(table.equals("PartOpr"))
	    				((TableExportDAO)context.getBean("partOprTableExportDAO")).run(isFileOnly, isSilentMode, dir);
	    			else if(table.equals("PartBin"))
	    				((TableExportDAO)context.getBean("partBinTableExportDAO")).run(isFileOnly, isSilentMode, dir);
	    			else if(table.equals("PartDtl"))
	    				((TableExportDAO)context.getBean("partDtlTableExportDAO")).run(isFileOnly, isSilentMode, dir);
	    			else if(table.equals("POHeader"))
	    				((TableExportDAO)context.getBean("POHeaderTableExportDAO")).run(isFileOnly, isSilentMode, dir);
	    			else if(table.equals("PODetail"))
	    				((TableExportDAO)context.getBean("PODetailTableExportDAO")).run(isFileOnly, isSilentMode, dir);
	    			else if(table.equals("JobHead"))
	    				((TableExportDAO)context.getBean("jobHeadTableExportDAO")).run(isFileOnly, isSilentMode, dir);	    			
	    			else if(table.equals("RcvDtl"))
	    				((TableExportDAO)context.getBean("rcvDtlTableExportDAO")).run(isFileOnly, isSilentMode, dir);	    			
	    			else if(table.equals("JobMtl"))
	    				((TableExportDAO)context.getBean("jobMtlTableExportDAO")).run(isFileOnly, isSilentMode, dir);	    			
	    			else if(table.equals("JobAsmbl"))
	    				((TableExportDAO)context.getBean("jobAsmblTableExportDAO")).run(isFileOnly, isSilentMode, dir);	    			
	    			else{
	    				if(!isSilentMode)
	    					System.out.println("ERROR - INVALID TABLE NAME : " + table);
	    				else
	    					System.out.println(-2);
	    				break;	    				
	    			}	    				
	    		}
	    		
	    		if(!isSilentMode){
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
	    		}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(-1);
			}
    	}
    }
}
