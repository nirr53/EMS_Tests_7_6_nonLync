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
* This test tests a create of users with long names (username up to 20 characters) via Manual and POST create
* ----------------
* Tests:
* 	 1. Add a new user with long name manually.
* 	 2. Add a new user with long name via POST query.
* 	 3. Delete the created users.
* 
* Results:
* 	 1-2. Both create tests should succeed.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test64__long_name_users {
	
  private browserTypes  usedBrowser;
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  Map<String, String> 	map;
  String 				longPrefix, manLongName, postLongName;
  
  // Default constructor for print the name of the used browser 
  public Test64__long_name_users(browserTypes browser) {
	  
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
    map 	  = new HashMap<String, String>();
    String Id 	 =  testFuncs.getId();
	longPrefix   = "longName" + Id + "____";
	manLongName  = longPrefix + "tests__1";
	postLongName = longPrefix + "tests__2";
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Create_users_with_long_names() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables and login 
    map.put("startIdx"   ,  String.valueOf(1));
    map.put("usersNumber",  "2");
    map.put("usersPrefix",  longPrefix);   
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	
    // Step 1 - Create a user with a long name manually
	testFuncs.myDebugPrinting("Step 1 - Create a user with a long name manually");
	testFuncs.addUser(driver, manLongName, "1q2w3e$r", manLongName, testVars.getDefTenant());
	testFuncs.myWait(2000);

    // Step 2 - Create a registered user with a long name using POST method
	testFuncs.myDebugPrinting("Step 2 - Create a registered user with a long name using POST method");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()           ,
			 												 testVars.getPort()         ,
			 												 "1"				        ,
			 												 postLongName               ,
			 												 testVars.getDomain()       ,
			 												 "registered"          	    ,
			 												 testVars.getDefPhoneModel(),
			 												 testVars.getDefTenant()    ,
			 												 "myLocation");
    testFuncs.verifyPostUserCreate(driver, postLongName, postLongName, true);
  }

  @After
  public void tearDown() throws Exception {
	  
	// Step 3 - Delete the created users
	testFuncs.myDebugPrinting("Step 3 - Delete the created users");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
	testFuncs.selectMultipleUsers(driver, longPrefix, "2");
	map.put("action"	 	  , "Delete Users");
	map.put("skipVerifyDelete", "true");
	testFuncs.setMultipleUsersAction(driver, map);
	manLongName  = manLongName.toLowerCase();
	postLongName = postLongName.toLowerCase();
	testFuncs.searchStr(driver, manLongName  + " Finished");
	testFuncs.searchStr(driver, postLongName + "@" + testVars.getDomain() + " Finished");
	    
	  // Close the session
    driver.quit();
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
      testFuncs.myFail(verificationErrorString);
    }
  }
}
