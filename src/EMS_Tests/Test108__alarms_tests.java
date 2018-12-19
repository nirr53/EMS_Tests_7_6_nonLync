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
* This test tests alarms from different types
* ----------------
* Tests:
* 	- Create a registered user via POST query
* 	 1. Create an alarm that sent from an unknown MAC address
* 	 2. Create an alarm with special characters
* 	 3. Delete all the created Alarms
* 	 4. Delete the created user 
* 
* Results:
* 	1. Alert should not be displayed
* 	2. Alerts should be created for all characters
*   3. All the alerts should be created successfully.
*   4. User should be deleted.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test108__alarms_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test108__alarms_tests(browserTypes browser) {
	  
	  System.out.println("Browser - "  + browser);
	  this.usedBrowser = browser;
  }
  
  // Define each browser as a parameter
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
  public void Alarms_tests() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	  
	// Set variables and login
	String Id 		= testFuncs.getId();
	String username = ("rgAlrt" + Id).toLowerCase(); 
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	
    // Create a registered user using POST method
	testFuncs.myDebugPrinting("Create a registered user using POST method");
	testFuncs.createUsers(testVars.getIp()		     ,
						  testVars.getPort() 	 	 ,
						  1				 			 ,	
						  username  	  		 	 ,			 
						  testVars.getDomain()	     ,					
						  "registered"		  	     ,						
						  testVars.getDefPhoneModel(),						
						  testVars.getDefTenant()    ,					
						  testVars.getDefLocation());
    testFuncs.verifyPostUserCreate(driver, username, username, true);
	String mac1 = testFuncs.readFile("mac_1.txt");

	// Step 1 - Create an alarm that sent from an unknown MAC address
	testFuncs.myDebugPrinting("Step 1 - Create an alarm that sent from an unknown MAC address");
	String alertPrefix = "unknownMac";
	String []alertsForSearch = {alertPrefix + "_" + Id};
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_DASHBOARD_ALARMS, "Export");
	testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(), testVars.getIp()  							 ,
															  testVars.getPort()							 ,
															  "00908f123456"								 ,
															  "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE",
															  alertsForSearch[0]				    		 ,
															  "2017-07-217T12:24:18"						 ,
															  "info2"	 									 ,
															  "info1"	 									 ,
															  "minor");
		
	// Search the alerts according to their description
	testFuncs.myDebugPrinting("Search the alerts according to their description", enumsClass.logModes.MINOR);
	testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.DESCRPTION, alertPrefix  , alertsForSearch);
	
	// Step 2 - Create an alarm with different special characters
	testFuncs.myDebugPrinting("Step 2 - Create an alarm with different special characterss");	
	String alertPrefix2 = "specialCharcaters_";
	String []alertsForSearch2 = {alertPrefix2 + "!#$/" + "_"  + Id,
								 alertPrefix2 + "=?^`" + "_"  + Id,
								 alertPrefix2 + "{|}~" + "_"  + Id,
								 alertPrefix2 + ";*+"  + "_" + Id};
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_DASHBOARD_ALARMS, "Export");
	
	// Create alarms with different sets of characters
	testFuncs.myDebugPrinting("Create alarms with different sets of characters", enumsClass.logModes.MINOR);
	for (int i = 0; i < 4; ++i) {
		
		testFuncs.myDebugPrinting("alertsForSearch2[i] - " + alertsForSearch2[i], enumsClass.logModes.MINOR);
		testFuncs.createAlarmViaPost(testVars.getAlarmsBatName()					,
									 testVars.getIp()  								,						
									 testVars.getPort()								,
									 mac1								 			,
									 "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE",
									 alertsForSearch2[i]				    		,
									 "2017-07-217T12:24:18"						 	,
									 "info2"	 									,
									 "info1"	 									,
				  					 "minor");
		
		// Search the created alert
		String[] searchedAlerts = {alertsForSearch2[i]};
		testFuncs.myDebugPrinting("Search the created alert", enumsClass.logModes.MINOR);
		testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.DESCRPTION, alertPrefix2  , searchedAlerts);
	}
	
	// Step 3 - Delete the created alarms
	testFuncs.myDebugPrinting("Step 3 - Delete the created alarms");
	for (int i = 0; i < 4; ++i) {
		
		testFuncs.myDebugPrinting("Delete alert " +  alertsForSearch2[i], enumsClass.logModes.MINOR);
		testFuncs.deleteAlarm(driver, alertsForSearch2[i]);	
	}
	
	// Step 4 - Delete the created and user
	testFuncs.myDebugPrinting("Step 4 - Delete the created user");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");    
	testFuncs.selectMultipleUsers(driver, username, "1");
	Map<String, String> map = new HashMap<String, String>();
	map.put("usersPrefix"	  , username);  
	map.put("usersNumber"	  , "1"); 
	map.put("startIdx"   	  , String.valueOf("1"));	  
	map.put("srcUsername"	  , "Finished");	  
	map.put("action"	 	  , "Delete Users");	  
	map.put("skipVerifyDelete", "true");	  
	testFuncs.setMultipleUsersAction(driver, map);
	username = username.toLowerCase();
	testFuncs.searchStr(driver, username + "@" + testVars.getDomain() + " Finished");	  
  }

  @After
  public void tearDown() throws Exception {
	  
    driver.quit();
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
    	testFuncs.myFail(verificationErrorString);
    }
  }
}
