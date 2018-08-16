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
* This test tests the create of users with different USBHeadsetType statuses.
* ----------------
* Tests:
* 	 - Enter Manage multiple users changes menu.
* 	 1. Create a user using POST query with randomize USBHeadsetType value
* 	 2. Create a user using POST query with "unknown" USBHeadsetType value
* 	 3. Create a user using POST query without "USBHeadsetType" value
* 	 4. Create a user using POST query with randomize "USBHeadsetType" value 129 characters long
* 	 5. Delete all the created users.
* 
* Results:
* 	 1-4. User should be created successfully with the given status.
* 	   5. Users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test152__different_USBHeadsetType_status {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test152__different_USBHeadsetType_status(browserTypes browser) {
	  
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
  public void Different_USBHeadsetType_fields() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables login
	String Id                 = testFuncs.getId();
	String prefixName         = "usbHdstTypeuser";
	String usersNumber	      = "4";
	String usbHdstTypeStts    = prefixName + "status"  + Id;
	String usbHdstTypeUnknown = prefixName + "unknown" + Id;
	String usbHdstTypeWithout = prefixName + "empty" + Id;
	String usbHdstTypeLong 	  = prefixName + "long"    + Id;

	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	
    // Step 1 - Create a user using POST query with randomize USBHeadsetType status
	testFuncs.myDebugPrinting("Step 1 - Create a user using POST query with randomize USBHeadsetType status");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(),
								testVars.getIp()           ,
								testVars.getPort()    	   ,
								"1"				   		   ,
								usbHdstTypeStts		   	   ,
								testVars.getDomain()  	   ,
								"registered"          	   ,
								testVars.getDefPhoneModel(),
								testVars.getDefTenant()    ,
			 					"myLocation");
	testFuncs.verifyPostUserCreate(driver, usbHdstTypeStts, usbHdstTypeStts, true);
	String tempUSBHeadsetType = testFuncs.readFile("ip_1.txt");
	testFuncs.myDebugPrinting("tempUSBHeadsetType - " + tempUSBHeadsetType, enumsClass.logModes.MINOR);
	testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[23]", tempUSBHeadsetType); 
	
    // Step 2 - Create a user using POST query with unknown USBHeadsetType status
	testFuncs.myDebugPrinting("Step 2 - Create a user using POST query with unknown USBHeadsetType status");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(),
								testVars.getIp()           ,
								testVars.getPort()    	   ,
								"1"				   		   ,
								usbHdstTypeUnknown		   ,
								testVars.getDomain()  	   ,
								"registered"          	   ,
								testVars.getDefPhoneModel(),
								testVars.getDefTenant()    ,
								"myLocation");
	testFuncs.verifyPostUserCreate(driver, usbHdstTypeUnknown, usbHdstTypeUnknown, true);
	testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[23]", "unknown"); 
	
    // Step 3 - Create a user using POST query without USBHeadsetType status
	testFuncs.myDebugPrinting("Step 3 - Create a user using POST query without USBHeadsetType status");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(),
			testVars.getIp()           ,
			testVars.getPort()    	   ,
			"1"				   		   ,
			usbHdstTypeWithout		   ,
			testVars.getDomain()  	   ,
			"registered"          	   ,
			testVars.getDefPhoneModel(),
			testVars.getDefTenant()    ,
			"myLocation");
	testFuncs.verifyPostUserCreate(driver, usbHdstTypeWithout, usbHdstTypeWithout, true);
	testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[23]", "");

    // Step 4 - Create a user using POST query with randomize USBHeadsetType value 129 characters long
	testFuncs.myDebugPrinting("Step 4 - Create a user using POST query with randomize USBHeadsetType value 129 characters long");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(),
								testVars.getIp()           ,
								testVars.getPort()    	   ,
								"1"				   		   ,
								usbHdstTypeLong		   ,
								testVars.getDomain()  	   ,
								"registered"          	   ,
								testVars.getDefPhoneModel(),
								testVars.getDefTenant()    ,
								"myLocation");
	testFuncs.verifyPostUserCreate(driver, usbHdstTypeLong, usbHdstTypeLong, true);
	String tempLongUSBHeadsetType = testFuncs.readFile("ip_1.txt");
	testFuncs.myDebugPrinting("tempLongUSBHeadsetType - " + tempLongUSBHeadsetType, enumsClass.logModes.MINOR);
	testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[23]", tempLongUSBHeadsetType); 
   
   // Step 5 - Delete the users
  	testFuncs.myDebugPrinting("Step 5 - Delete the users");
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
    testFuncs.searchStr(driver, usbHdstTypeStts.toLowerCase()    + "@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, usbHdstTypeUnknown.toLowerCase() + "@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, usbHdstTypeWithout.toLowerCase() + "@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, usbHdstTypeLong.toLowerCase()    + "@" + testVars.getDomain() + " Finished");
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
