package EMS_Tests;

import EMS_Tests.enumsClass.menuNames;

public class MenuPaths {
	
	// Setup section
	private String SETUP_SECTION 						 = "//*[@id='navbar-collapse']/ul[1]/li[1]/a";
	private String SETUP_WIZARD_SECTION 			 	 = "//*[@id='left-nav']/ul/li[1]/a/span";
	private String SETUP_WIZARD 			 			 = "//*[@id='left-nav']/ul/li[1]/ul/li/a/span[1]";
	private String SETUP_USERS_DEVICES_MANAGE_MULTI_USRS = "//*[@id='left-nav']/ul/li[2]/ul/li[2]/a/span[1]";
	private String SETUP_USERS_DEVICES_MANAGE_MULTI_DVCS = "//*[@id='left-nav']/ul/li[2]/ul/li[3]/a/span[1]";
	private String SETUP_PHONE_CONF_SECTION 			 = "//*[@id='left-nav']/ul/li[3]/a";
	private String SETUP_PHONE_CONF_TEMPLATES 			 = "//*[@id='left-nav']/ul/li[3]/ul/li[1]/a";
	private String SETUP_PHONE_CONF_TEMPLATES_MAPPING 	 = "//*[@id='left-nav']/ul/li[3]/ul/li[2]/a";
	private String SETUP_PHONE_CONF_SYS_SETTINGS	 	 = "//*[@id='left-nav']/ul/li[3]/ul/li[3]/a";
	private String SETUP_PHONE_CONF_DHCP_OPTIONS_CONFIG	 = "//*[@id='left-nav']/ul/li[3]/ul/li[4]/a";
	private String SETUP_PHONE_CONF_LDAP_SETTINGS	 	 = "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div/a[1]";	
	private String SETUP_PHONE_CONF_SBC_SETTINGS	 	 = "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div/a[3]";	
	private String SETUP_PHONE_CONF_TEMPLATES_PHS 		 = "//*[@id='left-nav']/ul/li[3]/ul/li[5]/a";
	private String SETUP_PHONE_CONF_TENANT_CONFIGURATION = "//*[@id='left-nav']/ul/li[3]/ul/li[6]/a";
	private String SETUP_PHONE_CONF_SITE_CONFIGURATION   = "//*[@id='left-nav']/ul/li[3]/ul/li[7]/a";
	private String SETUP_PHONE_CONF_USER_CONFIGURATION   = "//*[@id='left-nav']/ul/li[3]/ul/li[8]/a";
	private String SETUP_PHONE_CONF_DEVICE_PLACEHOLDERS  = "//*[@id='left-nav']/ul/li[3]/ul/li[9]/a";	
	private String SETUP_PHONE_CONF_PHONE_CONF_FILES 	 = "//*[@id='left-nav']/ul/li[3]/ul/li[10]/a";	
	private String SETUP_PHONE_CONF_PHONE_FIRMWARE_FILES = "//*[@id='left-nav']/ul/li[3]/ul/li[11]/a";
	private String SETUP_IMPORT_EXPORT_SECTION 			 = "//*[@id='left-nav']/ul/li[4]/a/span";	
	private String SETUP_IMPORT_EXPORT_CONF_IMPORT	 	 = "//*[@id='left-nav']/ul/li[4]/ul/li[1]/a";
	private String SETUP_IMPORT_EXPORT_CONF_EXPORT 		 = "//*[@id='left-nav']/ul/li[4]/ul/li[2]/a";
	private String SETUP_IMPORT_EXPORT_USRS_DVCS_IMPORT	 = "//*[@id='left-nav']/ul/li[4]/ul/li[3]/a";
	private String SETUP_IMPORT_EXPORT_USRS_DVCS_EXPORT  = "//*[@id='left-nav']/ul/li[4]/ul/li[4]/a";
	private String SETUP_SYSTEM_SECTION 				 = "//*[@id='left-nav']/ul/li[5]/a";
	private String SETUP_SYSTEM_VIEW_TENATS 			 = "//*[@id='left-nav']/ul/li[5]/ul/li[1]/a";
	private String SETUP_SYSTEM_VIEW_SITES 			 	 = "//*[@id='left-nav']/ul/li[5]/ul/li[2]/a";
	private String SETUP_SYSTEM_LICENSE 				 = "//*[@id='left-nav']/ul/li[5]/ul/li[3]/a";
		
	// Mainpage section
	private String MAINPAGE_ALERTS_BUTTON 				 = "//*[@id='left-nav']/ul/li/ul/li[3]/a";
	private String MAINPAGE_USER_DETAILS 				 = "//*[@id='navbar-collapse']/ul[3]/li[3]/a";
	private String MAINPAGE_USER_LOGOUT_BUTTON 			 = "/html/body/div[2]/span[2]/a/button";
	private String MONITOR_MAINPAGE_USER_LOGOUT_BUTTON 	 = "/html/body/div[3]/span[2]/a/button";
	private String MONITOR_MAINPAGE_USER_LOGOUT_BUTTON2  = "/html/body/div[4]/span[2]/a/button";

	// Monitor section
	private String MONITOR_SECTION 						 = "//*[@id='navbar-collapse']/ul[1]/li[2]/a";
	private String MONITOR_DEVICE_STATUS_MENU 			 = "//*[@id='left-nav']/ul/li/ul/li[2]/a/span[1]";
	
	// Troubleshoot section
	private String TROUBLESHOOT_SECTION 				 = "//*[@id='navbar-collapse']/ul[1]/li[3]/a";
	private String TROUBLESHOOT_WEB_ADMIN_LOGS_MENU		 = "//*[@id='left-nav']/ul/li/ul/li[1]/a/span[1]";

	public String[] getPaths(menuNames menuName) {
		
		String[] paths = {"", "", "", ""};
		switch (menuName) {
		
			// -----------------
			//
			// Mainpage section
			//
			// -----------------
			case MAINPAGE_DASHBOARD_ALARMS:
            	paths[0] = MAINPAGE_ALERTS_BUTTON;
            	break;
			case MAINPAGE_GEN_INFOR_LOGOUT:
            	paths[0] = MAINPAGE_USER_DETAILS;
            	paths[1] = MAINPAGE_USER_LOGOUT_BUTTON;
				break;
			case MAINPAGE_MONITOR_GEN_INFOR_LOGOUT:
            	paths[0] = MAINPAGE_USER_DETAILS;
            	paths[1] = MONITOR_MAINPAGE_USER_LOGOUT_BUTTON;
				break;
			case MAINPAGE_MONITOR_GEN_INFOR_LOGOUT2:
            	paths[0] = MAINPAGE_USER_DETAILS;
            	paths[1] = MONITOR_MAINPAGE_USER_LOGOUT_BUTTON2;
				break;			
	    		
			// -----------------
	        //
	        // Setup section
	        //
	    	// -----------------	
			case SETUP_SETUP_WIZARD:	          
				paths[0] = SETUP_SECTION;
				paths[1] = SETUP_WIZARD_SECTION;   
				paths[2] = SETUP_WIZARD;
				break;	            	
			case SETUP_MANAGE_USERS:
				paths[0] = SETUP_SECTION;        
				break;
				
	        // Users Devices menu
			case SETUP_MANAGE_MULTIPE_USERS:  
				paths[0] = SETUP_SECTION;	            
				paths[1] = SETUP_USERS_DEVICES_MANAGE_MULTI_USRS;		            
				break;			
			case SETUP_MANAGE_MULTIPE_DEVICES:	            
				paths[0] = SETUP_SECTION;	            
				paths[1] = SETUP_USERS_DEVICES_MANAGE_MULTI_DVCS;	            
				break;		
		        
			// Phone Configuration menus
			case SETUP_PHONE_CONF_SECTION: 
				paths[0] = SETUP_SECTION;		            
				paths[1] = SETUP_PHONE_CONF_SECTION;		           
				break;			
			case SETUP_PHONE_CONF_TEMPLATES:		            
				paths[0] = SETUP_SECTION;		            
				paths[1] = SETUP_PHONE_CONF_SECTION;		            
				paths[2] = SETUP_PHONE_CONF_TEMPLATES;		            
				break;
			case SETUP_TEMPLATES_MAPPING:		            
				paths[0] = SETUP_SECTION;		            
				paths[1] = SETUP_PHONE_CONF_SECTION;		            
				paths[2] = SETUP_PHONE_CONF_TEMPLATES_MAPPING;		            
				break;
			case SETUP_PHONE_CONF_SYSTEM_SETTINGS:
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_SYS_SETTINGS;
	            break;    
			case SETUP_PHONE_CONF_DHCP_OPTIONS_CONFIGURATION:
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_DHCP_OPTIONS_CONFIG;
	            break;	
			case SETUP_PHONE_CONF_SYSTEM_SETTINGS_SBC_CONF:
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_SYS_SETTINGS;
	            paths[3] = SETUP_PHONE_CONF_SBC_SETTINGS;            
	            break;            
			case SETUP_PHONE_CONF_SYSTEM_SETTINGS_SBC_LDAP:
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_SYS_SETTINGS;
	            paths[3] = SETUP_PHONE_CONF_LDAP_SETTINGS;            
	            break;	
			case SETUP_PHONE_CONF_TEMPLATES_PLACEHOLDERS:
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_TEMPLATES_PHS;
	            break;                      
			case SETUP_TENANT_CONFIGURATION:
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_TENANT_CONFIGURATION;
	            break;    
			case SETUP_SITE_CONFIGURATION:
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_SITE_CONFIGURATION;
	            break;       
			case SETUP_USER_CONFIGURATION:
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_USER_CONFIGURATION;
	            break;    
			case SETUP_PHONE_CONFIGURATION_PHONE_DEVICE_PHS:
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_DEVICE_PLACEHOLDERS;
	            break;            
			case SETUP_PHONE_CONFIGURATION_PHONE_CONF_FILES:
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_PHONE_CONF_FILES;
	            break;
			case SETUP_PHONE_CONFIGURATION_PHONE_FIRM_FILES:
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_PHONE_CONF_SECTION;
	            paths[2] = SETUP_PHONE_CONF_PHONE_FIRMWARE_FILES;
	            break;
	            
		    // Import export menus
			case SETUP_IMPORT_EXPORT_CONFIGURATION_IMPORT:
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_IMPORT_EXPORT_SECTION;
	            paths[2] = SETUP_IMPORT_EXPORT_CONF_IMPORT;
	            break; 
			case SETUP_IMPORT_EXPORT_CONFIGURATION_EXPORT:
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_IMPORT_EXPORT_SECTION;
	            paths[2] = SETUP_IMPORT_EXPORT_CONF_EXPORT;
	            break; 
			case SETUP_IMPORT_EXPORT_USRS_DEVICES_IMPORT:
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_IMPORT_EXPORT_SECTION;
	            paths[2] = SETUP_IMPORT_EXPORT_USRS_DVCS_IMPORT;
	            break; 
			case SETUP_IMPORT_EXPORT_USRS_DEVICES_EXPORT:
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_IMPORT_EXPORT_SECTION;
	            paths[2] = SETUP_IMPORT_EXPORT_USRS_DVCS_EXPORT;
	            break;    
	                
	        // System menus            
	        case SETUP_SYSTEM_SECTION:
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_SYSTEM_SECTION;
	            break;             
	        case SETUP_SYSTEM_VIEW_TENANTS:
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_SYSTEM_SECTION;
	            paths[2] = SETUP_SYSTEM_VIEW_TENATS;
	            break; 
	        case SETUP_SYSTEM_VIEW_SITES:
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_SYSTEM_SECTION;
	            paths[2] = SETUP_SYSTEM_VIEW_SITES;
	            break;
	        case SETUP_SYSTEM_LICENSE:
	            paths[0] = SETUP_SECTION;
	            paths[1] = SETUP_SYSTEM_SECTION;
	            paths[2] = SETUP_SYSTEM_LICENSE;
	            break;	        
	            
		    // -----------------
		    //
		    // Monitor section
		    //
		   	// -----------------
			case MONITOR_DEVICE_STATUS:
			    paths[0] = MONITOR_SECTION;
		        paths[1] = MONITOR_DEVICE_STATUS_MENU;
		        break;
			            
			// -----------------
			//
			// Troubleshoot section
			//				// -----------------
		    case TROUBLESHOOT_SYSTEM_DIAGNOSTICS:		            
		    	paths[0] = TROUBLESHOOT_SECTION;				            	
		    	break;	    				    
		    case TROUBLESHOOT_WEB_ADMIN_LOGS_MENU:		            
			    paths[0] = TROUBLESHOOT_SECTION;				            
			   	paths[1] = TROUBLESHOOT_WEB_ADMIN_LOGS_MENU;				            
			   	break;
		}		
		
		return paths;
	}
}
