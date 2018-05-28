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
	  
	// Log modes enum
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
}
