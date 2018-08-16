package EMS_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
    Map<menuNames, String> menusMap 	= new HashMap<menuNames, String>();
    menusMap.put(enumsClass.menuNames.MAINPAGE_DASHBOARD_ALARMS					 , "Export");
    menusMap.put(enumsClass.menuNames.SETUP_SETUP_WIZARD						 , "System Properties");
    menusMap.put(enumsClass.menuNames.SETUP_MANAGE_USERS						 , "New User" );
	menusMap.put(enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS				 , "Manage Multiple Users" );
	menusMap.put(enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES				 , "Manage Multiple Devices" );
	menusMap.put(enumsClass.menuNames.SETUP_PHONE_CONF_SECTION	   				 , "Phones Configuration" );
	menusMap.put(enumsClass.menuNames.SETUP_PHONE_CONF_TEMPLATES				 , "IP Phones Configuration Templates" );     
	menusMap.put(enumsClass.menuNames.SETUP_TEMPLATES_MAPPING					 , "Zero Touch Templates Mapping" );        
	menusMap.put(enumsClass.menuNames.SETUP_PHONE_CONF_SYSTEM_SETTINGS		     , "System Settings" );   
	menusMap.put(enumsClass.menuNames.SETUP_PHONE_CONF_DHCP_OPTIONS_CONFIGURATION, "DHCP Options Configuration" );
	menusMap.put(enumsClass.menuNames.SETUP_PHONE_CONF_SYSTEM_SETTINGS_SBC_CONF  , "Proxy DHCP Options Configuration" );	
	menusMap.put(enumsClass.menuNames.SETUP_PHONE_CONF_SYSTEM_SETTINGS_SBC_LDAP  , "LDAP Configuration" );	
	menusMap.put(enumsClass.menuNames.SETUP_PHONE_CONF_TEMPLATES_PLACEHOLDERS    , "Template Placeholders" );                     
	menusMap.put(enumsClass.menuNames.SETUP_TENANT_CONFIGURATION				 , "Tenant Configuration" );   
	menusMap.put(enumsClass.menuNames.SETUP_SITE_CONFIGURATION				     , "Site Configuration" );     
	menusMap.put(enumsClass.menuNames.SETUP_USER_CONFIGURATION				     , "Manage Multiple Users - User Configuration" );   
	menusMap.put(enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_DEVICE_PHS , "Manage Devices Placeholders" );          
	menusMap.put(enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_CONF_FILES , "Manage Configuration Files" );
	menusMap.put(enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_FIRM_FILES , "Phone firmware files" );
	menusMap.put(enumsClass.menuNames.SETUP_IMPORT_EXPORT_CONFIGURATION_IMPORT	 , "To Import Phone Configuration Files" );
	menusMap.put(enumsClass.menuNames.SETUP_IMPORT_EXPORT_CONFIGURATION_EXPORT	 , "To export phone configuration files" );
	menusMap.put(enumsClass.menuNames.SETUP_IMPORT_EXPORT_USRS_DEVICES_IMPORT	 , "Import Users and Devices information" );
	menusMap.put(enumsClass.menuNames.SETUP_IMPORT_EXPORT_USRS_DEVICES_EXPORT	 , "Export Users and Devices information" );          
	menusMap.put(enumsClass.menuNames.SETUP_SYSTEM_SECTION						 , "System" );         
	menusMap.put(enumsClass.menuNames.SETUP_SYSTEM_VIEW_TENANTS				     , "Tenant List" );
	menusMap.put(enumsClass.menuNames.SETUP_SYSTEM_VIEW_SITES					 , "View Sites" );
	menusMap.put(enumsClass.menuNames.SETUP_SYSTEM_LICENSE						 , "License Properties" );
	menusMap.put(enumsClass.menuNames.MONITOR_DEVICE_STATUS					     , "Devices Status" );		   
	menusMap.put(enumsClass.menuNames.TROUBLESHOOT_SYSTEM_DIAGNOSTICS			 , "System Logs");

	// Login the system and enter Setup-Wizard menu
	testFuncs.myDebugPrinting("Login the system and enter Setup-Wizard menu");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SETUP_WIZARD, "System Properties");

	// Step 1 - Move between all menus
	testFuncs.myDebugPrinting("Step 1 - Move between all menus");
	int mapSize = menusMap.size();
    for (menuNames key : menusMap.keySet()) {
    	
		testFuncs.pressHomeButton(driver);
		String tempHeader = menusMap.get(key);
		testFuncs.myDebugPrinting("<" + menuIdx + "--" + mapSize + ">", enumsClass.logModes.NORMAL);
		testFuncs.myDebugPrinting(menuIdx + ".Enter menu - "      + key.toString(), enumsClass.logModes.NORMAL);
		testFuncs.myDebugPrinting(menuIdx + ".Seek for header - " + tempHeader, enumsClass.logModes.MINOR);
		testFuncs.enterMenu(driver, key, tempHeader);
		++menuIdx;
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
