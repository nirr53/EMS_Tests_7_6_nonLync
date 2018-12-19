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
* This test tests the Events & Alarms filter at Alarms-filter
* ----------------
* @category Tests:
* 	- Create a registered user via POST query
* 	- Create an Alarm associate with the user
* 	- Create an Event associate with the user
*   1. Filter the Alarms by 'Alarms only'
*   2. Filter the Alarms by 'Events & Alarms'
*   3. Delete the created Event & Alarm
*   4. Delete the created user
*   
*  @category Results:
* 	 1. Only Alarms should be filtered
* 	 2. All the Alarms should be filtered
* 	 3. The created Event & Alarm should be deleted successfully.
* 	 4. The user should be created successfully. 
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test169__alarms_events_filter {
	
  private browserTypes  usedBrowser;
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test169__alarms_events_filter(browserTypes browser) {
	  
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
	
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Alarms_events_filter() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables and login
	String Id 		  		= testFuncs.getId();
	String bodyText			= "";
	String testPrefix 		= "evntAlFilUsr";
	String username   		= testPrefix + Id; 
	String userNumber		= "2";
	String [] filterOptions = {"Alarms", "Alarms And Events"};
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
		
	// Create a registered user using POST method
	testFuncs.myDebugPrinting("Create a registered user using POST method");
	testFuncs.createUsers(testVars.getIp(),
			  testVars.getPort() 	 	  ,
			  Integer.valueOf(userNumber)							  ,	
			  username  		 	  	  ,
			  testVars.getDomain()		  ,
			  "registered"		  		  ,
			  testVars.getDefPhoneModel() ,
			  testVars.getDefTenant()     ,
			  testVars.getDefLocation());
	    
	testFuncs.verifyPostUserCreate(driver, username, username, true);
	String [] macs = {testFuncs.readFile("mac_1.txt"), testFuncs.readFile("mac_2.txt")};
		
	// Create an Alarm & Event
	testFuncs.myDebugPrinting("Create an Alarm & Event");
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_DASHBOARD_ALARMS, "Export");
	String [][]alarmsData = {{"IPPhone General Local Event", "This Event provides information about IPP internal operation" 					 + Id},
							 {"IPPhone Lync Login Failure" , "This Alarm is activated when failing to connect to the Lync server during sign in" + Id}};	
	for (int i = 0; i < alarmsData[0].length; ++i) {
		
		testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(),
									 testVars.getIp()  			,
									 testVars.getPort()			,
									 macs[i]					,
									 alarmsData[i][0]			,
									 alarmsData[i][1]			,
									 "2017-07-217T12:24:18"		,
									 "info1" + Id	 			,
									 "info2" + Id	 			,
									 "minor");
	}
	
	// Step 1 - Filter the Alarms by 'Alarms only'
	testFuncs.myDebugPrinting("Step 1 - Filter the Alarms by 'Alarms only'", enumsClass.logModes.MAJOR);
	testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.ALARMS_ONLY, filterOptions[0], alarmsData[1]);
	bodyText     = driver.findElement(By.tagName("body")).getText();
	testFuncs.myAssertTrue("Event is displayed !!\nbodyText - " + bodyText, !bodyText.contains(alarmsData[0][0]));
	
	// Step 2 - Filter the Alarms by 'Events & Alarms'
	testFuncs.myDebugPrinting("Step 2 - Filter the Alarms by 'Events & Alarms'", enumsClass.logModes.MAJOR);
	testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.ALARMS_AND_EVENTS, filterOptions[1], alarmsData[0]);
	bodyText  = driver.findElement(By.tagName("body")).getText();
	testFuncs.myAssertTrue("Event is not displayed !!\nbodyText - " + bodyText, bodyText.contains(alarmsData[1][0]));		
	
	// Step 3 - Delete the created Alarm & Events
	testFuncs.myDebugPrinting("Step 3 - Delete the created Alarm & Events");
	
	// Nir 7.6.132 6/18 - bug - you cannot filter by more than one criteria, and since Alarms select is always reset this section should rewritten
	//	testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.REMOTE_HOST, "IPPhone/" + macs[1], alarmsData[1]);
	testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.ALARMS_ONLY, filterOptions[0], alarmsData[1]);
	testFuncs.deleteAlarmWithoutSearch(driver);
	//	testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.REMOTE_HOST, "IPPhone/" + macs[0], alarmsData[0]);
	testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.ALARMS_AND_EVENTS, filterOptions[1], alarmsData[0]);
	testFuncs.deleteAlarmWithoutSearch(driver);
	
	// Step 4 - Delete the created and user
	testFuncs.myDebugPrinting("Step 4 - Delete the created user");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");    
	testFuncs.selectMultipleUsers(driver, username, userNumber);
	Map<String, String> map = new HashMap<String, String>();
	map.put("usersPrefix"	  , username);  
	map.put("usersNumber"	  , userNumber); 
	map.put("startIdx"   	  , String.valueOf("1"));	  
	map.put("srcUsername"	  , "Finished");	  
	map.put("action"	 	  , "Delete Users");	  
	map.put("skipVerifyDelete", "true");	  
	testFuncs.setMultipleUsersAction(driver, map);
	username = username.toLowerCase();
	testFuncs.searchStr(driver, username + "_1@" + testVars.getDomain() + " Finished");
	testFuncs.searchStr(driver, username + "_2@" + testVars.getDomain() + " Finished");
  }

  @After
  public void tearDown() throws Exception { 
	    
//	driver.quit();
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
      testFuncs.myFail(verificationErrorString);
    }
  }
}
