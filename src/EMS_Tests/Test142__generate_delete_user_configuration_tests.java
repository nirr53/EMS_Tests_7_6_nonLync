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
* This test tests the Configuration file of ZT created user while delete
* ----------------
* Tests:
* 	 - Create a registered user with registered device.
* 	 - Create a user-configuration value (Add+Save)
* 	 - Generate the user configuration
* 	 1. Delete the configuration value from the user without generate.
* 	 2. Verify that value does exists at the configuration value.
* 	 3. Generate the user configuration
* 	 4. Verify that value not exists at the configuration value.
* 	 5. Delete the created user
* 
* Results:
* 	 1+2. The value should still be found on the configuration file
* 	 3+4. The value should not be found on the configuration file
*      5. User should be deleted.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test142__generate_delete_user_configuration_tests {
	
  private String        usedBrowser = "";
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test142__generate_delete_user_configuration_tests(String browser) {
	  
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
  public void Generate_user_configuration_delete_tests() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String usersNumber		= "1";
	String bodyText 		= "",  mac = "", currUrl = "";
	String Id 				= testFuncs.getId();
	String regPrefix		= "genconftst" + Id;
	String confName 		= "confName"   + Id;
	String confValue 		= "confValue"  + Id;
	String confNames[] =  {confName};
	String confValues[] = {confValue};
	String usersFullName[] = {regPrefix + "@" + testVars.getDomain()};
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  usersNumber); 
    map.put("startIdx"   ,  usersNumber);

	// Login, create a registered user and user-configuration value (Add+Save)
	testFuncs.myDebugPrinting("Login, create a registered user and user-configuration value (Add+Save)");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()       	,
			 												 testVars.getPort()     	,
			 												 usersNumber				    ,
			 												 regPrefix					,
			 												 testVars.getDomain()       ,
			 												 "registered"               ,
			 												 testVars.getDefPhoneModel(),
			 												 testVars.getDefTenant()    ,
			 												 "myLocation");
    testFuncs.verifyPostUserCreate(driver, regPrefix, regPrefix, true);
    mac = testFuncs.readFile("mac_1.txt");
 	testFuncs.myDebugPrinting("device mac - " + mac, testVars.logerVars.MINOR);
	testFuncs.enterMenu(driver, "Setup_user_configuration", "Manage Multiple Users - User Configuration");
	testFuncs.selectMultipleUsers(driver, regPrefix, usersNumber);
	testFuncs.createNewConfValue(driver, confName, confValue);
	testFuncs.saveConfValues(driver, usersFullName);
    
	// Generate the user configuration
 	testFuncs.myDebugPrinting("Generate the user configuration");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", "Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, regPrefix, usersNumber);
    map.put("action"  , "Generate IP Phones Configuration Files");
    testFuncs.setMultipleUsersAction(driver, map);
	
 	// Step 1 - Delete the configuration value from the user without generate
 	testFuncs.myDebugPrinting("Step 1 - Delete the configuration value from the user without generate");
	testFuncs.enterMenu(driver, "Setup_user_configuration", "Manage Multiple Users - User Configuration");
	testFuncs.selectMultipleUsers(driver, regPrefix, usersNumber);
	testFuncs.deleteConfValue(driver, usersFullName, confNames, confValues);
	
 	// Step 2 - Verify that value still exists at the configuration file
 	testFuncs.myDebugPrinting("Step 2 - Verify that value still exists at the configuration file");
 	currUrl  = driver.getCurrentUrl();
 	testFuncs.myDebugPrinting("currUrl - " + currUrl, testVars.logerVars.MINOR);
    driver.get("https://" + testVars.getIp() + "//configfiles//" + mac + ".cfg");
 	testFuncs.myWait(5000);
	bodyText     = driver.findElement(By.tagName("body")).getText();
	testFuncs.myAssertTrue("Congiuration value was deleted without generate !!", bodyText.contains(confName + " = " + confValue));
    driver.get(currUrl);
    
	// Step 3 - Generate the user configuration
 	testFuncs.myDebugPrinting("Step 3 - Generate the user configuration");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", "Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, regPrefix, usersNumber);
    map.put("action"  , "Generate IP Phones Configuration Files");
    testFuncs.setMultipleUsersAction(driver, map);
    
 	// Step 4 - Verify that value not exists at the configuration value
 	testFuncs.myDebugPrinting("Step 4 - Verify that value not exists at the configuration value");
 	currUrl  = driver.getCurrentUrl();
 	testFuncs.myDebugPrinting("currUrl - " + currUrl, testVars.logerVars.MINOR);
    driver.get("https://" + testVars.getIp() + "//configfiles//" + mac + ".cfg");
 	testFuncs.myWait(5000);
	bodyText     = driver.findElement(By.tagName("body")).getText();
	testFuncs.myAssertTrue("Congiuration value was not deleted after generate !!", !bodyText.contains(confName + " = " + confValue));
    driver.get(currUrl);
    
	// Step 5 - Delete the created user
 	testFuncs.myDebugPrinting("Step 5 - Delete the created user");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, regPrefix, usersNumber);
    map.put("usersPrefix"	  , regPrefix);
    map.put("usersNumber"	  , usersNumber); 
    map.put("startIdx"   	  , String.valueOf("1"));
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    regPrefix = regPrefix.toLowerCase();
    testFuncs.searchStr(driver, regPrefix + "@" + testVars.getDomain() + " Finished");
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
