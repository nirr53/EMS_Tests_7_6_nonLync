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
* This test tests the create of users with different HRSSpeakerFW statuses.
* ----------------
* Tests:
* 	 - Enter Manage multiple users changes menu.
* 	 1. Create a user using POST query with randomize HRSSpeakerFW value
* 	 2. Create a user using POST query with empty HRSSpeakerFW value
* 	 3. Create a user using POST query with HRSSpeakerFW value that has special characters
* 	 4. Delete all the created users.
* 
* Results:
* 	 1-3. User should be created successfully with the given status.
* 	   4. Users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test154__different_HRSSpeakerFW_status {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test154__different_HRSSpeakerFW_status(browserTypes browser) {
	  
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
  public void Different_HRSSpeakerFW_fields() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String Id                = testFuncs.getId();
	String prefixName        = "hrsSpeakerFw";
	String usersNumber	     = "3";
	String tempHRSSpeakerFW  = "";
	String hrsSpeakerFwStts  = prefixName + "Status" + Id;
	String hrsSpeakerFwEmpty = prefixName + "Empty"  + Id;
	String hrsSpeakerFwSpec  = prefixName + "Spec"   + Id;
	
	// Login and check the HRS Speaker FW check-box
	testFuncs.myDebugPrinting("Login and check the HRS Speaker FW check-box");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.MONITOR_DEVICE_STATUS, "Devices Status");
	testFuncs.selectColumn(driver, "//*[@id='HRS_SPEAKER_FW']", true);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	
    // Step 1 - Create a user using POST query with randomize HRSSpeakerFW value
	testFuncs.myDebugPrinting("Step 1 - Create a user using POST query with randomize HRSSpeakerFW value");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(),
								testVars.getIp()           ,
								testVars.getPort()    	   ,
								"1"				   		   ,
								hrsSpeakerFwStts		   ,
								testVars.getDomain()  	   ,
								"registered"          	   ,
								testVars.getDefPhoneModel(),
								testVars.getDefTenant()    ,
			 					"myLocation");
	testFuncs.verifyPostUserCreate(driver, hrsSpeakerFwStts, hrsSpeakerFwStts, true);
	tempHRSSpeakerFW = testFuncs.readFile("ip_1.txt");
	testFuncs.myDebugPrinting("tempHRSSpeakerFW - " + tempHRSSpeakerFW, enumsClass.logModes.MINOR);
	testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[25]", tempHRSSpeakerFW); 	
	
    // Step 2 - Create a user using POST query with empty HRSSpeakerFW value
	testFuncs.myDebugPrinting("Step 2 - Create a user using POST query with empty HRSSpeakerFW value");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(),
								testVars.getIp()           ,
								testVars.getPort()    	   ,
								"1"				   		   ,
								hrsSpeakerFwEmpty	   ,
								testVars.getDomain()  	   ,
								"registered"          	   ,
								testVars.getDefPhoneModel(),
								testVars.getDefTenant()    ,
						"myLocation");
	testFuncs.verifyPostUserCreate(driver, hrsSpeakerFwEmpty, hrsSpeakerFwEmpty, true);
	testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[25]", "");

    // Step 3 - Create a user using POST query with HRSSpeakerFW value that has special characters
	testFuncs.myDebugPrinting("Step 3 - Create a user using POST query with HRSSpeakerFW value that has special characters");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(),
								testVars.getIp()           ,
								testVars.getPort()    	   ,
								"1"				   		   ,
								hrsSpeakerFwSpec		   	   ,
								testVars.getDomain()  	   ,
								"registered"          	   ,
								testVars.getDefPhoneModel(),
								testVars.getDefTenant()    ,
								"myLocation");
	testFuncs.verifyPostUserCreate(driver, hrsSpeakerFwSpec, hrsSpeakerFwSpec, true);
	tempHRSSpeakerFW = testFuncs.readFile("ip_1.txt");
	testFuncs.myDebugPrinting("tempHRSSpeakerFW - " + tempHRSSpeakerFW, enumsClass.logModes.MINOR);
	testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[25]", tempHRSSpeakerFW); 
   
    // Step 4 - Delete the users
  	testFuncs.myDebugPrinting("Step 4 - Delete the users");
    Map<String, String> map = new HashMap<String, String>();
    map.put("action"	      , "Delete Users");
    map.put("srcUsername"     , "Finished");
    map.put("skipVerifyDelete", "true");
    map.put("usersPrefix"     , prefixName);
    map.put("usersNumber"	  , usersNumber); 
    map.put("startIdx"   	  , String.valueOf(1));  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, prefixName, usersNumber);   
    testFuncs.setMultipleUsersAction(driver, map);  
    testFuncs.searchStr(driver, hrsSpeakerFwStts.toLowerCase()  + "@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, hrsSpeakerFwEmpty.toLowerCase() + "@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, hrsSpeakerFwSpec.toLowerCase()  + "@" + testVars.getDomain() + " Finished");
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
