package EMS_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;

/**
* ----------------
* This test tests the create of users with very long user-names (100 chars)
* ----------------
* Tests:
* 	 1. Add a new user with 100-characters long name manually.
* 	 2. Add a new registered user with 100-characters long name by POST query.
* 	 3. Delete the created users
* 
* Results:
* 	 1-2. Users should be added successfully.
* 	   3. Users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test139__username_length {
	
  private String        usedBrowser = "";
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test139__username_length(String browser) {
	  
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
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Long_user_name_tests() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String prefix		   = "lngUserName__" + testFuncs.getId();
	int    maxLength	   = 100;
	Map<String, String>map = new HashMap<String, String>();
    map.put("usersNumber",  "1"); 
    map.put("startIdx"   ,  String.valueOf(1));

	// login
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	
    // Step 1 - Add a new user with 100-characters long name manually
	testFuncs.myDebugPrinting("Step 1 - Add a new user with 100-characters long name manually");
	String userNameMan = getUserName(prefix, maxLength);
	testFuncs.addUser(driver, userNameMan, "1q2w3e$r", userNameMan, testVars.getDefTenant());
	testFuncs.myWait(2000);

    // Step 2 - Create a registered user using POST method
	testFuncs.myDebugPrinting("Step 2 - Create a registered user using POST method");
	String userNamePost = getUserName(prefix, 46);
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()       	,
			 												 testVars.getPort()     	,
			 												 "1"				    	,
			 												 userNamePost 		,
			 												 testVars.getDomain()       ,
			 												 "registered"               ,
			 												 testVars.getDefPhoneModel(),
			 												 testVars.getDefTenant()    ,
			 												 "myLocation");
    testFuncs.verifyPostUserCreate(driver, userNamePost, userNamePost, true);
    testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
    
    // Step 3 - Create an unregistered user using POST method
	testFuncs.myDebugPrinting("Step 3 - Create an unregistered user using POST method");  
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");  
	testFuncs.selectMultipleUsers(driver, prefix, "2"); 
	map.put("usersPrefix"	  , prefix);	  
	map.put("srcUsername"	  , "Finished");	  
	map.put("action"	 	  , "Delete Users");  
	map.put("skipVerifyDelete", "true");	  
	testFuncs.setMultipleUsersAction(driver, map);  
	testFuncs.searchStr(driver, userNameMan.toLowerCase()  + " Finished");
	testFuncs.searchStr(driver, userNamePost.toLowerCase() + "@" + testVars.getDomain() + " Finished");
  }
  
  // Get user-name with given prefix and wanted length
  private String getUserName(String prefix, int maxLength) {

	  // Set variables
	  String wantedRandPartStr = "";	 
	  int wantedRandPartLen    = -1;
	  Random rand 			   = new Random();
			  
	  // Create random string as suffix
	  wantedRandPartLen = maxLength - prefix.length();
	  testFuncs.myDebugPrinting("wantedRandPartLen - " + wantedRandPartLen, testVars.logerVars.MINOR);
	  for (int i = 0 ; i < wantedRandPartLen; ++i) {
		  
		  wantedRandPartStr += String.valueOf(rand.nextInt(9) + 1);
	  }

	  // Merge prefix and the random string we created
	  String wantedStr = prefix + wantedRandPartStr;
	  testFuncs.myDebugPrinting("wantedStr - " + wantedStr, testVars.logerVars.MINOR);
	  return (wantedStr);
}

  @After
  public void tearDown() throws Exception {
    
	  // Close session
//	  driver.quit();
	  System.clearProperty("webdriver.chrome.driver");
	  System.clearProperty("webdriver.ie.driver");
	  String verificationErrorString = verificationErrors.toString(); 
	  if (!"".equals(verificationErrorString)) {
    	   
		  testFuncs.myFail(verificationErrorString);
    
	  }	  
  }
}
