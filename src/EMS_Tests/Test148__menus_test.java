package EMS_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;
import EMS_Tests.enumsClass.*;

/**
* ----------------
* This test tests that all menus are accessible without stuck of the system
* ----------------
* Tests:
* 	 - Login and enter the Setup Wizard menu 
* 	 1. Enter all the menus of the system and verify that the system is not get stuck 
* 		when it try to open each of them.
* 
* Results:
* 	 1. As described.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test148__menus_test {
	
  private browserTypes  usedBrowser;
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test148__menus_test(browserTypes browser) {
	  
	  System.out.println("Browser - "  + browser);
	  this.usedBrowser = browser;
  }
  
  //Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameters(name="{0}")
  public static Collection data() {
	  
	GlobalVars testVars2  = new GlobalVars();
    return Arrays.asList(testVars2.getBrowsers());
  }
  
  @BeforeClass 
  public static void setting_SystemProperties() {
	  
      System.out.println("System Properties seting Key value.");
  }  
  
  @Before
  public void setUp() throws Exception {
	  	
	testVars  = new GlobalVars();
    testFuncs = new GlobalFuncs(); 
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Menus_test() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	int menuIdx = 0;
	String menuNames[][] = {{"Dashboard_Alarms"								,"Export"},  	
							{"Setup_Wizard"									,"System Properties"},
							{"Setup_Manage_users"							,"New User"},
							{"Setup_Manage_multiple_users"					,"Manage Multiple Users"},
							{"Setup_Manage_multiple_devices"				,"Manage Multiple Devices"},
							{"Setup_Phone_conf_section"						,"Phones Configuration"},
							{"Setup_Phone_conf_templates"					,"IP Phones Configuration Templates"},     
							{"Templates_mapping"							,"Zero Touch Templates Mapping"},        
							{"Setup_Phone_conf_system_settings"			    ,"System Settings"},   
							{"Setup_Phone_conf_dhcp_options_configuration"  ,"DHCP Options Configuration"},
							{"Setup_Phone_conf_system_settings_sbc_conf"    ,"Proxy DHCP Options Configuration"},	
							{"Setup_Phone_conf_system_settings_ldap"		,"LDAP Configuration"},	
							{"Setup_Phone_conf_templates_placeholders"		,"Template Placeholders"},                     
							{"Tenant_configuration"							,"Tenant Configuration"},   
							{"Site_configuration"							,"Site Configuration"},     
							{"Setup_user_configuration"						,"Manage Multiple Users - User Configuration"},   
							{"Setup_Phone_conf_phone_device_placeholders"	,"Manage Devices Placeholders"},          
							{"Setup_Phone_conf_phone_configuration_files"	,"Manage Configuration Files"},
							{"Setup_Phone_conf_phone_firmware_files"		,"Phone firmware files"},
							{"Setup_Import_export_configuration_import"		,"To Import Phone Configuration Files"},
							{"Setup_Import_export_configuration_export"		,"To export phone configuration files"},
							{"Setup_Import_export_users_devices_import"		,"Import Users and Devices information"},
							{"Setup_Import_export_users_devices_export"		,"Export Users and Devices information"},          
							{"Setup_System_section"							,"System"},         
							{"Setup_System_view_tenants"					,"Tenant List"},
							{"Setup_System_view_sites"						,"View Sites"},
							{"Setup_System_license"							,"License Properties"},
							{"Monitor_device_status"						,"Devices Status"},
							{"Troubleshoot_system_diagnostics"				,"System Logs"}};

	// Login the system and enter Setup-Wizard menu
	testFuncs.myDebugPrinting("Login the system and enter Setup-Wizard menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Wizard", "System Properties");

	// Step 1 - Move between all menus
	testFuncs.myDebugPrinting("Step 1 - Move between all menus");
	for (String[] menuData : menuNames) {
		
		testFuncs.pressHomeButton(driver);
		testFuncs.myDebugPrinting(menuIdx + ".Enter menu - "      + menuData[0], enumsClass.logModes.NORMAL);
		testFuncs.myDebugPrinting(menuIdx + ".Seek for header - " + menuData[1], enumsClass.logModes.MINOR);
		testFuncs.enterMenu(driver, menuData[0], menuData[1]);
		menuIdx++;
	}
  }

  @After
  public void tearDown() throws Exception {
	  
//    driver.quit();
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
      testFuncs.myFail(verificationErrorString);
    }
  }
}
