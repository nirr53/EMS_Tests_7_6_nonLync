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
import org.openqa.selenium.*;import EMS_Tests.enumsClass.*;

/**
* ----------------
* This test tests the timing values between actions on multiple-devices-changes menu
* ----------------
* Tests:
* 	- Enter Manage multiple devices changes menu.
* 	- Create several users using POST query.
* 	 1. send message with 5 seconds between each action
* 	 2. send message with 30 seconds between each action
* 	 3. send message with 300 seconds between each action
* 	 4. Delete the users.
* 
* Results:
*	 1-3. Actions should be made successfully.
* 	 4. Users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test124__multiple_devices_actions_timing {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test124__multiple_devices_actions_timing(browserTypes browser) {
	  
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
  public void Manage_multiple_devices_timimg_tests() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String usersNumber   = "4";
	String dispPrefix    = "tmngDvcs" + testFuncs.getId();
	int usStartIdx 		 = 1;
    Map<String, String> map = new HashMap<String, String>();

    // Create several users using POST query
	testFuncs.myDebugPrinting("Step 1 - Create several users using POST query");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUsers(testVars.getIp()		  	 	,
						  testVars.getPort() 	 	 	,
						  Integer.valueOf(usersNumber)	,	
						  dispPrefix  	  		     	,			 
						  testVars.getDomain()	     	,					
						  "registered"		  	     	,						
						  testVars.getDefPhoneModel()	,
						  testVars.getDefTenant()    	,												
						  testVars.getDefLocation());
	testFuncs.verifyPostUsersCreate(driver,  dispPrefix,  dispPrefix, true, Integer.valueOf(usersNumber));	

    // Step 1 - send message with 5 seconds between each action
  	testFuncs.myDebugPrinting("Step 1 - send message with 5 seconds between each action");  	
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("action"      , "Send Message");
    map.put("message"     , "myMessage");
    map.put("timeoutValue", "5");
    testFuncs.setMultipleDevicesAction(driver, map);
    verifyStr(dispPrefix, Integer.valueOf(usersNumber));
   
    // Step 2 - send message with 30 seconds between each action
   	testFuncs.myDebugPrinting("Step 2 - send message with 30 seconds between each action");  	
 	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("timeoutValue", "30");
    testFuncs.setMultipleDevicesAction(driver, map);
    verifyStr(dispPrefix, Integer.valueOf(usersNumber));

    // Step 3 - send message with 300 seconds between each action
   	testFuncs.myDebugPrinting("Step 3 - send message with 300 seconds between each action");  	
 	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("timeoutValue", "300");
    testFuncs.setMultipleDevicesAction(driver, map);
    verifyStr(dispPrefix, Integer.valueOf(usersNumber));
     
    // Step 4 - Delete the created users
  	testFuncs.myDebugPrinting("Step 4 - Delete the created users");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("usersPrefix"	  , dispPrefix + "_");
    map.put("usersNumber"	  , usersNumber); 
    map.put("startIdx"   	  , String.valueOf(usStartIdx));
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    dispPrefix = dispPrefix.toLowerCase();
    testFuncs.searchStr(driver, dispPrefix + "_1@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, dispPrefix + "_2@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, dispPrefix + "_3@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, dispPrefix + "_4@" + testVars.getDomain() + " Finished");
  }
  
  // Verify string
  private void verifyStr(String dispPrefix, Integer valueOf) {
	  
	  for (int i = 1; i <= valueOf; ++i) {
		  
		  testFuncs.searchStr(driver, dispPrefix + "_" + i + "@" + testVars.getDomain() + " " + testFuncs.readFile("mac_" + i + ".txt"));  
	  }
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
