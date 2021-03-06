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
* This test tests the max permitted length of Location field during a user create.
* ----------------
* Tests:
*  	 - Login the system
*  	 1. Create a user with Location field in 2048 characters.
*  	 2. Create a user with Location field in 2049 characters. 
*  	 3. Delete the users.
*  
* Results:
*    1. A user with 2048 characters long Location field should be created successfully.
*    2. A user with 2049 characters long Location field should be created unsuccessfully.
*    3. The users should be deleted successfully.
*  
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test76__long_location_users {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test76__long_location_users(browserTypes browser) {
	  
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
  public void User_location() throws Exception {
	  
		Log.startTestCase(this.getClass().getName());

		// Set variables + login
		String usersPrefix      = "location";
		testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
		testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
		Map<String, String> map = new HashMap<String, String>();
	    map.put("usersNumber",  "2"); 
	    map.put("startIdx"   ,  String.valueOf(1));
	
	  	// Step 1 - Create a user with Location field in 2048 characters
		testFuncs.myDebugPrinting("Step 1 - Create a user with Location field in 2048 characters");
		testFuncs.createUsers(testVars.getIp()		  	  ,
							  testVars.getPort() 	 	  ,
							  1							  ,	
							  usersPrefix + "2048"  	  ,
							  testVars.getDomain()		  ,
							  "registered"		  		  ,
							  "430HD"					  ,
							  testVars.getDefTenant()     ,
							  testVars.getDefLocation());
	    testFuncs.verifyPostUserCreate(driver, usersPrefix + "2048" + "@" + testVars.getDomain(), usersPrefix + "2048", true);
	    
	  	// Step 2 - Create a user with Location field in 2049 characters
		testFuncs.myDebugPrinting("Step 2 - Create a user with Location field in 2049 characters");
		testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");		
		testFuncs.createUsers(testVars.getIp()		  	  ,
							  testVars.getPort() 	 	  ,
							  1							  ,	
							  usersPrefix + "2049"  	  ,
							  testVars.getDomain()		  ,
							  "registered"		  		  ,
							  "430HD"					  ,
							  testVars.getDefTenant()     ,
							  testVars.getDefLocation());
	    testFuncs.verifyPostUserCreate(driver, usersPrefix + "2049" + "@" + testVars.getDomain(), usersPrefix + "2049", true);

	    // Step 3 - Delete the users
	  	testFuncs.myDebugPrinting("Step 3 - Delete the created users");
		testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
	    testFuncs.selectMultipleUsers(driver, usersPrefix, "2");
	    map.put("usersPrefix"	  , usersPrefix);
	    map.put("usersNumber"	  , "2"); 
	    map.put("startIdx"   	  , "2");
	    map.put("srcUsername"	  , "Finished");
	    map.put("action"	 	  , "Delete Users");
	    map.put("skipVerifyDelete", "true");
	    testFuncs.setMultipleUsersAction(driver, map);
	    testFuncs.searchStr(driver, usersPrefix + "2048" + "@" + testVars.getDomain() + " Finished");
	    testFuncs.searchStr(driver, usersPrefix + "2049" + "@" + testVars.getDomain() + " Finished");
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
