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

/**
* ----------------
* This test tests the alarms icon at main page
* ----------------
* Tests:
* 	- Create a registered user via POST query
* 	 1. Check the link to the Alarms menu via the Alarm icon when there are no alarms
* 	 2. Create an alarm and check that its status updated
* 	 3. Create an another alarm and check that the Alarms status is updated
* 	 4. Delete all the created Alarms and check that its status updated
* 	 5. Delete the created user 
* 
* Results:
* 	1. Link should work. Zero alarms should be reported at the link
* 	2. Link should work. One alarm should be reported at the link message-box
* 	3. Link should work. Two alarms should be reported at the link message-box
* 	4. Link should work. Zero alarms should be reported at the link message-box
*   5. User should be deleted.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test104__alarms_icons {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test104__alarms_icons(String browser) {
	  
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
  public void Alarms_icon_tests() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	  
	// Set variables and login
	String Id 				 = testFuncs.getId();
	String username 		 = ("alIcnTst" + Id).toLowerCase(); 
	String []alertsForSearch = {"alert1" + "_" + Id, "alert2" + "_" + Id};	
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	
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

	// Step 1 - Check the link to the Alarms menu via the Alarm icon when there are no alarms
	testFuncs.myDebugPrinting("Step 1 - Check the link to the Alarms menu via the Alarm icon when there are no alarms");
	checkAlarmsViaIcon("0");
	
	// Step 2 - Create an alarm and check that its status updated
	testFuncs.myDebugPrinting("Step 2 - Create an alarm and check that its status updated");
	testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(), testVars.getIp()  							 ,
															  testVars.getPort()							 ,
															  mac1								 ,
															  "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE",
															  alertsForSearch[0]				    		 ,
															  "2017-07-217T12:24:18"						 ,
															  "info2"	 									 ,
															  "info1"	 									 ,
															  "Major");
	// Nir 7.4.2048 14\2\18 - VI 151096
	checkAlarmsViaIcon("1"); 
	
	// Step 3 - Create an alarm and check that its status not updated
	testFuncs.myDebugPrinting("Step 3 - Create an alarm and check that its status updated");
	testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(), testVars.getIp()  							 ,
															  testVars.getPort()							 ,
															  mac1								 ,
															  "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE",
															  alertsForSearch[1]				    		 ,
															  "2017-07-217T12:24:18"						 ,
															  "info2"	 									 ,
															  "info1"	 									 ,
															  "minor");
	// Nir 7.4.2048 14\2\18 - VI 151096
	checkAlarmsViaIcon("1"); 
	
	// Step 3 - Delete all the created Alarms and check that its status updated
	testFuncs.myDebugPrinting("Step 3 - Delete all the created Alarms and check that its status updated");
	testFuncs.deleteAlarm(driver, alertsForSearch[0]);	
	testFuncs.deleteAlarm(driver, alertsForSearch[1]);
	// Nir 7.4.2048 14\2\18 - VI 151096
	checkAlarmsViaIcon("0"); 
	
	// Step 4 - Delete the created and user
	testFuncs.myDebugPrinting("Step 4 - Delete the created user");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");    
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

  // Check Alarms icon
  private void checkAlarmsViaIcon(String numOfSrcAlerts) {
	  	
	  // Check Alarms-Icon message-box
	  testFuncs.myDebugPrinting("Check Alarms-Icon message-box", testVars.logerVars.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='navbar-collapse']/ul[3]/li[2]/a"), 2000);
	  testFuncs.searchStr(driver, "ALARMS");
	  testFuncs.searchStr(driver, "Total: " + numOfSrcAlerts + " alarm(s)");
	  	  
	  // Check View-All link
	  testFuncs.myDebugPrinting("Check View-All link", testVars.logerVars.MINOR);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[4]/span[2]/a"), 10000);	  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='trunkTBL']/div/div[1]/h3", "Alarms");
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
