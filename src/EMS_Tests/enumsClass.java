package EMS_Tests;

public class enumsClass {
	
		
	// Select types
	public enum selectTypes {
	         
		INDEX, NAME, GIVEN_TEXT; 
	}
	
	// Browser types
	public enum browserTypes {
	         
		CHROME, FF, IE; 
	}
	  
	// Log modes
	public enum logModes {
		 	 
		MAJOR(""), NORMAL(" "), MINOR("  "), DEBUG("   ");  		 
		private String level = "";
		
		private logModes(String level) {		 
			this.level = level;   	 
		}
		public String getLevel() {   		 
			return level;    
		}
	 }
	
	// Menu names
	public enum menuNames {
		 	 
		MAINPAGE_DASHBOARD_ALARMS,
		MAINPAGE_GEN_INFOR_LOGOUT,
		MAINPAGE_MONITOR_GEN_INFOR_LOGOUT,
		MAINPAGE_MONITOR_GEN_INFOR_LOGOUT2;
	}
	
	
	
   	
//case "General_Informatiom_logout":
//	paths[0] = MAINPAGE_USER_DETAILS;
//	paths[1] = MAINPAGE_USER_LOGOUT_BUTTON;
//	break;          	
//case "Monitoring_General_Informatiom_logout":
//	paths[0] = MAINPAGE_USER_DETAILS;
//	paths[1] = MONITOR_MAINPAGE_USER_LOGOUT_BUTTON;
//	break;
//case "Monitoring_General_Informatiom_logout2":
//	paths[0] = MAINPAGE_USER_DETAILS;
//	paths[1] = MONITOR_MAINPAGE_USER_LOGOUT_BUTTON2;
//	break;
	
	
	
}
