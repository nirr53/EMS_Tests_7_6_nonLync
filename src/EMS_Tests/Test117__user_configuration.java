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
* This test tests the actions of user configuration menu.
* ----------------
*
* Tests:
* 	 - Login, create several users using POST query and enter the Generate-Configuration menu
* 	 1. Try to create a new configuration key without pressing the Save button
* 	 2. Try to create a new configuration key with pressing the Save button
*    3. Delete configuration key
*    4. Delete the created users
* 
* Results:
* 	 1. Configuration key should not be added successfully.
* 	 2. Configuration key should be added successfully.
* 	 3. Configuration key should be deleted successfully.
* 	 4. Users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test117__user_configuration {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test117__user_configuration(String browser) {
	  
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
  public void User_configuration_actions() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String usersNumber   	= "3";
	int usStartIdx 		 	= 1;
	String Id 				= testFuncs.getId();
	String dispPrefix   	= ("userConfActions" + Id).toLowerCase();
	String confPrefix		= "confName";
	String confName 		= confPrefix        + Id;
	String confValue 		= "confValue"       + Id;
    Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  usersNumber); 
    map.put("startIdx"   ,  String.valueOf(usStartIdx));
    map.put("srcUsername",  "Finished");  
	
    // Login, create several users using POST query and enter the Generate-Configuration menu
	testFuncs.myDebugPrinting("Login, create several users using POST query and enter the Generate-Configuration menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()           ,
			 testVars.getPort()         ,
			 usersNumber		        ,
			 dispPrefix  		        ,
			 testVars.getDomain()       ,
			 "registered"               ,
			 testVars.getDefPhoneModel(),
			 testVars.getDefTenant()    ,
			 "myLocation");
	testFuncs.verifyPostUsersCreate(driver,  dispPrefix,  dispPrefix, true, Integer.valueOf(usersNumber));	
	testFuncs.enterMenu(driver, "Setup_user_configuration", "Manage Multiple Users - User Configuration");

    // Step 1 - Try to create a new configuration key without pressing the Save button
	testFuncs.myDebugPrinting("Step 1 - Try to create a new configuration key without pressing the Save button");
	testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
	testFuncs.createNewConfValue(driver, confName, confValue);
	String usersFullNames[] = {dispPrefix + "_1@" + testVars.getDomain(),
							   dispPrefix + "_2@" + testVars.getDomain(),
							   dispPrefix + "_3@" + testVars.getDomain()};
	String confNames[] =  {confName,  confName , confName};
	String confValues[] = {confValue, confValue, confValue};
	testFuncs.verifyConfValues(driver, usersFullNames, confNames, confValues, false);
	
    // Step 2 - Try to create a new configuration key with pressing the Save button
	testFuncs.myDebugPrinting("Step 2 - Try to create a new configuration key with pressing the Save button");
	testFuncs.enterMenu(driver, "Setup_user_configuration", "Manage Multiple Users - User Configuration");
	testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
	testFuncs.createNewConfValue(driver, confName, confValue);
	testFuncs.saveConfValues(driver, usersFullNames);
	testFuncs.verifyConfValues(driver, usersFullNames, confNames, confValues, true);
	
    // Step 3 - Delete configuration key
	testFuncs.myDebugPrinting("Step 3 - Delete configuration key");
	testFuncs.enterMenu(driver, "Setup_user_configuration", "Manage Multiple Users - User Configuration");
	testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
	testFuncs.deleteConfValue(driver, usersFullNames, confNames, confValues);
	
    // Step 4 - Delete the created users
  	testFuncs.myDebugPrinting("Step 4 - Delete the created users");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("usersPrefix"	  , dispPrefix + "_");
    map.put("usersNumber"	  , usersNumber); 
    map.put("startIdx"   	  , String.valueOf(usStartIdx));
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
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
