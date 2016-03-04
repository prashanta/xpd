package com.gemcity.xpd;



import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gemcity.xpd.beans.ExportProfile;
import com.gemcity.xpd.dao.TableExportDAO;

public class App
{
    
    public static void main( String[] args )
    {   
    	// App expects table names in arguments
    	if(args.length < 1)
    		System.out.println("Usage: xpd [<Table1> <Table2> ...]");
    	else{
    		
    		// Create Spring Context
    		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        	
    		// Get the TableExportDAO bean
    		TableExportDAO dao = (TableExportDAO)context.getBean("tableExportDAO");
    		
    		try {
    			
    			// Get ExportProfile bean for "part" table
    			ExportProfile partTableProfile = (ExportProfile)context.getBean("partTableExportProfile");
    			ExportProfile partMtlTableProfile = (ExportProfile)context.getBean("partMtlTableExportProfile");
    			
    			// Export "part" table
    			//dao.run(partTableProfile);
    			dao.run(partMtlTableProfile);
    			
    			
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    
		
    }
}
