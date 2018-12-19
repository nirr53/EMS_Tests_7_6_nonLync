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
* This test tests a receive of alarms from all types
* ----------------
* Tests:
* 	- Create a registered user via POST query
* 	1. Create an alarm and check its date and time stamp.
* 	2. After 2 minutes, refresh the Alarm and check that it updated respectively.
*   3. Delete the created alarms
* 	4. Delete the created user 
* 
* Results:
* 	1. Alert create-time should be created successfully.
* 	2. Alert create-time should be updated successfully.
*   3. All the alerts should be deleted successfully.
*   4. User should be deleted.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test159__alarms_date_time {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test159__alarms_date_time(browserTypes browser) {
	  
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
	
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Alarms_date_time() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	  
	// Set variables and login
	String Id 		 = testFuncs.getId();
	String username  = ("alrmsDateUsr" + Id).toLowerCase();
	String alarmName = "IPPHONE REGISTRATION FAILURE";
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	
    // Create a registered user using POST method
	testFuncs.myDebugPrinting("Create a registered user using POST method");
	testFuncs.createUsers(testVars.getIp()		  	 	,
						  testVars.getPort() 	 	 	,
						  Integer.valueOf(1)			,	
						  username  	  		     	,			 
						  testVars.getDomain()	     	,					
						  "registered"		  	     	,						
						  testVars.getDefPhoneModel()	,
						  testVars.getDefTenant()    	,												
						  testVars.getDefLocation());
    testFuncs.verifyPostUserCreate(driver, username, username, true);
	String mac1 = testFuncs.readFile("mac_1.txt");

	// Step 1 - Create an alarm and test its date
	testFuncs.myDebugPrinting("Step 1 - Create an alarm and test its date");
	String alertPrefix = "dateTime";
	String []alarmsForSearch = {alertPrefix + "_" + Id};
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_DASHBOARD_ALARMS, "Export");
	testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(),
								 testVars.getIp()  			,
								 testVars.getPort()			,
								 mac1						,
								 alarmName					,
								 alarmsForSearch[0]			,
								 "2017-07-217T12:24:18"		,
								 Id	 						,
								 Id	 						,
								 "minor");
		
	// Verify the Alarm create
	testFuncs.myDebugPrinting("Verify the Alarm create");	
	testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.DESCRPTION, alarmsForSearch[0], alarmsForSearch);
	String currDispTime = driver.findElement(By.xpath("//*[@id='table']/tbody[1]/tr/td[11]")).getText();
	testFuncs.myDebugPrinting("currDispTime - " + currDispTime, enumsClass.logModes.MINOR);	
	testFuncs.myWait(120000);
	
	// Step 2 - After 2 minutes, refresh the Alarm and check that it updated respectively.
	testFuncs.myDebugPrinting("Step 2 - After 2 minutes, refresh the Alarm and check that it updated respectively.");
	testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(),
								 testVars.getIp()  			,
								 testVars.getPort()			,
								 mac1						,
								 alarmName					,
								 alarmsForSearch[0]			,
								 "2017-07-217T12:24:18"		,
								 Id	 						,
								 Id	 						,
								 "minor");
	
	// Verify the Alarm refresh
	testFuncs.myDebugPrinting("Verify the Alarm refresh");	
	testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.DESCRPTION, alarmsForSearch[0], alarmsForSearch);
	String currDispTime2 = driver.findElement(By.xpath("//*[@id='table']/tbody[1]/tr/td[11]")).getText();
	testFuncs.myDebugPrinting("currDispTime2 - " + currDispTime2, enumsClass.logModes.MINOR);

	// Compare dates
	testFuncs.myDebugPrinting("Compare dates", enumsClass.logModes.NORMAL);			
	String[] dateParts = currDispTime2.split(" ");
	String[] dateParts2 = currDispTime2.split(" ");
	testFuncs.myAssertTrue("Dates are not match !!", dateParts2[0].contains(dateParts[0]));

	// Compare time
	testFuncs.myDebugPrinting("Compare time", enumsClass.logModes.NORMAL);			
	String[] timeParts  = dateParts[1].split(":");
	String[] timeParts2 = dateParts2[1].split(":");
	testFuncs.myAssertTrue("Hours are not match !! (timeParts[0] - " + timeParts[0] + ", timeParts2[0] - " + timeParts2[0], timeParts[0].contains(timeParts2[0]));
	int minutes1 = Integer.valueOf(timeParts[1]);
	int minutes2 = Integer.valueOf(timeParts2[1]);
	testFuncs.myDebugPrinting("minutes1 - " + minutes1, enumsClass.logModes.MINOR);			
	testFuncs.myDebugPrinting("minutes2 - " + minutes2, enumsClass.logModes.MINOR);
	testFuncs.myAssertTrue("Times were not updated !!", (minutes1 	    == minutes2) ||
														((minutes1 + 1) == minutes2) ||
														((minutes1 + 2) == minutes2));
	
	// Step 3 - Delete the created alarms
	testFuncs.myDebugPrinting("Step 3 - Delete the created alarm");			
	testFuncs.deleteAlarmNoVerify(driver, alarmsForSearch[0]);	
	testFuncs.deleteAlarmNoVerify(driver, alarmsForSearch[0]);
	
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
