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
* This test tests the create of users with different HRSSpeakerModel statuses.
* ----------------
* Tests:
* 	 - Enter Manage multiple users changes menu.
* 	 1. Create a user using POST query with randomize HRSSpeakerModel value
* 	 2. Create a user using POST query with empty HRSSpeakerModel value
* 	 3. Create a user using POST query with HRSSpeakerModel value that has special characters
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
public class Test153__different_HRSSpeakerModel_status {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test153__different_HRSSpeakerModel_status(browserTypes browser) {
	  
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
  public void Different_HRSSpeakerModel_fields() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String Id                     = testFuncs.getId();
	String prefixName             = "hrsSpeakerModel";
	String usersNumber	          = "3";
	String tempHRSSpeakerModel 	  = "";
	String hrsSpeakerModelStts    = prefixName + "Status" + Id;
	String hrsSpeakerModelEmpty   = prefixName + "Empty"  + Id;
	String hrsSpeakerModelSpec 	  = prefixName + "Spec"   + Id;
    Map<String, String> extraData = new HashMap<String, String>();
	
	// Login and check the HRS Speaker Model check-box
	testFuncs.myDebugPrinting("Login and check the HRS Speaker Model check-box");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.MONITOR_DEVICE_STATUS, "Devices Status");
	testFuncs.selectColumn(driver, "//*[@id='HRS_SPEAKER_MODEL']", true);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	
    // Step 1 - Create a user using POST query with randomize HRSSpeakerModel value
	testFuncs.myDebugPrinting("Step 1 - Create a user using POST query with randomize HRSSpeakerModel value");	
    extraData.put("HRSSpeakerModel"		 , Id);
	testFuncs.writeFile("ip_1.txt"		 , Id);
	testFuncs.createUsers(testVars.getIp()				,
						  testVars.getPort() 	 	 	,
						  Integer.valueOf(1)			,	
						  hrsSpeakerModelStts  	  		,			 
						  testVars.getDomain()	     	,					
						  "registered"		  	     	,						
						  testVars.getDefPhoneModel()	,
						  testVars.getDefTenant()    	,												
						  testVars.getDefLocation()		,
						  extraData);
	testFuncs.verifyPostUserCreate(driver, hrsSpeakerModelStts, hrsSpeakerModelStts, true);
	tempHRSSpeakerModel = testFuncs.readFile("ip_1.txt");
	testFuncs.myDebugPrinting("tempHRSSpeakerModel - " + tempHRSSpeakerModel, enumsClass.logModes.MINOR);
	testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[24]", tempHRSSpeakerModel); 
	
    // Step 2 - Create a user using POST query with empty HRSSpeakerModel value
	testFuncs.myDebugPrinting("Step 2 - Create a user using POST query with empty HRSSpeakerModel value");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
    extraData.put("HRSSpeakerModel"		 , "");
	testFuncs.createUsers(testVars.getIp()				,
						  testVars.getPort() 	 	 	,
						  Integer.valueOf(1)			,	
						  hrsSpeakerModelEmpty  	  	,			 
						  testVars.getDomain()	     	,					
						  "registered"		  	     	,						
						  testVars.getDefPhoneModel()	,
						  testVars.getDefTenant()    	,												
						  testVars.getDefLocation()		,
						  extraData);
	testFuncs.verifyPostUserCreate(driver, hrsSpeakerModelEmpty, hrsSpeakerModelEmpty, true);
	testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[24]", "");

    // Step 3 - Create a user using POST query with HRSSpeakerModel value that has special characters
	testFuncs.myDebugPrinting("Step 3 - Create a user using POST query with HRSSpeakerModel value that has special characters");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	String spChars	=  "~!@#$%^&*()+<>,'_";
    testFuncs.deleteFilesByPrefix(System.getProperty("user.dir"), "ip_1.txt");
    extraData.put("HRSSpeakerModel"		 , spChars);
	testFuncs.writeFile("ip_1.txt"		 , spChars);
	testFuncs.createUsers(testVars.getIp()				,
						  testVars.getPort() 	 	 	,
						  Integer.valueOf(1)			,	
						  hrsSpeakerModelSpec  	  		,			 
						  testVars.getDomain()	     	,					
						  "registered"		  	     	,						
						  testVars.getDefPhoneModel()	,
						  testVars.getDefTenant()    	,												
						  testVars.getDefLocation()		,
						  extraData);
	testFuncs.verifyPostUserCreate(driver, hrsSpeakerModelSpec, hrsSpeakerModelSpec, true);
	tempHRSSpeakerModel = testFuncs.readFile("ip_1.txt");
	testFuncs.myDebugPrinting("tempHRSSpeakerModel - " + tempHRSSpeakerModel, enumsClass.logModes.MINOR);
	testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[24]", tempHRSSpeakerModel); 
   
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
    testFuncs.searchStr(driver, hrsSpeakerModelStts.toLowerCase()  + "@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, hrsSpeakerModelEmpty.toLowerCase() + "@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, hrsSpeakerModelSpec.toLowerCase()  + "@" + testVars.getDomain() + " Finished");
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
