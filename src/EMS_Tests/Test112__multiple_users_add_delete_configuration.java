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
* This test tests the a create and delete of configuration key of multiple-users-changes menu
* ----------------
* Tests:
* 	 - Enter Manage multiple users changes menu.
* 	 1. Create several users using POST query.
* 	 2. Add a configuration key.
* 	 3. Delete the added configuration key.
* 	 4. Delete the users.
* 
* Results:
*	 1. Users should be created successfully.
* 	 2. The users configuration key should be added successfully.
* 	 3. The users configuration key should be deleted successfully.
* 	 4. Users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test112__multiple_users_add_delete_configuration {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test112__multiple_users_add_delete_configuration(browserTypes browser) {
	  
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
  public void Manage_multiple_users_add_delete_configuration_key() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String usersNumber   	= "3";
	String Id				= testFuncs.getId();
	int usStartIdx 		 	= 1;
	String dispPrefix   	= ("addDelConfkeyUser" + Id).toLowerCase();
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  usersNumber); 
    map.put("startIdx"   ,  String.valueOf(usStartIdx));

    // Step 1 - Create several users using POST query
	testFuncs.myDebugPrinting("Step 1 - Create several users using POST query");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUsers(testVars.getIp()			  ,
						  testVars.getPort() 	 	  ,
						  Integer.valueOf(usersNumber),	
						  dispPrefix  	  		 	  ,			 
						  testVars.getDomain()	      ,					
						  "registered"		  	      ,						
						  testVars.getDefPhoneModel() ,						
						  testVars.getDefTenant() 	  ,					
						  testVars.getDefLocation())  ;
	testFuncs.verifyPostUsersCreate(driver,  dispPrefix,  dispPrefix, true, Integer.valueOf(usersNumber));	
    
    // Step 2 - Add configuration-key
  	testFuncs.myDebugPrinting("Step 2 - Update configuration");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("action"    , "User configuration");
    map.put("confKey"   , "confKey"   + Id);
    map.put("confValue" , "confValue" + Id);
    map.put("dispPrefix", dispPrefix);
    testFuncs.setMultipleUsersAction(driver, map);
        
    // Step 3 - Delete configuration key
  	testFuncs.myDebugPrinting("Step 3 - Delete configuration key");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, "Manage Multiple Users");
	testFuncs.myWait(3000);
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("action"    , "Delete User configuration");
    testFuncs.setMultipleUsersAction(driver, map);    
    
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
