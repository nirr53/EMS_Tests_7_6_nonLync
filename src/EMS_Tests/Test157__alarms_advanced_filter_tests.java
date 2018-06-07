package EMS_Tests;

import java.net.InetAddress;
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
* This test tests more filter criteria of alarms
* ----------------
* Tests:
* 	- Create a registered user via POST query
* 	 1. Create an alarm with special info and verify that it filtered correctly.
* 	 2. Filter the alarm by its Remote host and verify that it filtered correctly.
* 	 3. Filter the alarm by its Source and verify that it filtered correctly.
* 	 4. Filter the alarm by its Name and verify that it filtered correctly.
* 	 5. Delete all the created Alarms
* 	 6. Delete the created user 
* 
* Results:
* 	1+4. Alert should be filtered correctly.
*     5. All the alerts should be deleted successfully.
*     6. User should be deleted.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test157__alarms_advanced_filter_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test157__alarms_advanced_filter_tests(browserTypes browser) {
	  
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
  public void Alarms_advanced_filter_tests() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	  
	// Set variables and login
	String Id 		= testFuncs.getId();
	String username = ("rgAlrt" + Id).toLowerCase(); 
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	
    // Create a registered user using POST method
	testFuncs.myDebugPrinting("Create a registered user using POST method");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()       	,
			 												 testVars.getPort()     	,
			 												 "1"				    	,
			 												 username 					,
			 												 testVars.getDomain()       ,
			 												 "registered"               ,
			 												 testVars.getDefPhoneModel(),
			 												 testVars.getDefTenant()    ,
			 												 "myLocation");
    testFuncs.verifyPostUserCreate(driver, username, username, true);
	String mac1 = testFuncs.readFile("mac_1.txt");

	// Step 1 - Create an Alarm with special info and verify that it filtered correctly
	testFuncs.myDebugPrinting("Step 1 - Create an Alarm with special info and verify that it filtered correctly");
	String alarmPrefix = "infoAlarm";
	String info1       = "info1" + Id;
	String info2       = "info2" + Id;
	String []alarmsForSearch = {alarmPrefix + "_" + Id};
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_DASHBOARD_ALARMS, "Export");
	testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(), testVars.getIp()  							 ,
															  testVars.getPort()							 ,
															  mac1								 			 ,
															  "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE",
															  alarmsForSearch[0]				    		 ,
															  "2017-07-217T12:24:18"						 ,
															  info1	 									 	 ,
															  info2	 									 	 ,
															  "minor");
		
	// Search the alerts according to their Info
	testFuncs.myDebugPrinting("Search the alerts according to their Info", enumsClass.logModes.MINOR);
	testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.INFO, info1  , alarmsForSearch);
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/h3/div/a[3]"), 3000);
	testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.INFO, info2  , alarmsForSearch);
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/h3/div/a[3]"), 3000);
	
	// Step 2 - Filter the alarm by its Remote-host and verify that it filtered correctly
	testFuncs.myDebugPrinting("Step 2 - Filter the alarm by its Remote-host and verify that it filtered correctly");	
	testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.REMOTE_HOST, InetAddress.getLocalHost().getHostAddress() , alarmsForSearch);
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/h3/div/a[3]"), 3000);

	// Step 3 - Filter the alarm by its Source and verify that it filtered correctly
	testFuncs.myDebugPrinting("Step 3 - Filter the alarm by its Source and verify that it filtered correctly");	
	testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.SOURCE, "IPPhone/" + mac1 , alarmsForSearch);
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/h3/div/a[3]"), 3000);

	// Step 4- Filter the alarm by its Name and verify that it filtered correctly
	testFuncs.myDebugPrinting("Step 4 - Filter the alarm by its Name and verify that it filtered correctly");	
	testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.NAME, "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE" , alarmsForSearch);
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/h3/div/a[3]"), 3000);
	
	// Step 5 - Delete the created alarms
	testFuncs.myDebugPrinting("Step 5 - Delete the created alarm");			
	testFuncs.deleteAlarm(driver, alarmsForSearch[0]);	
	
	// Step 6 - Delete the created and user
	testFuncs.myDebugPrinting("Step 6 - Delete the created user");
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
