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
* This test tests the Configuration file of ZT created user while edit exist configuration values
* ----------------
* Tests:
* 	 - Create a registered user with registered device.
* 	 - Create a user-configuration value (Add+Save)
* 	 - Generate the user configuration
* 	 1. Edit the created configuration value from the user without generate.
* 	 2. Verify that value did not change at the configuration value.
* 	 3. Generate the user configuration
* 	 4. Verify that value did change at the configuration value.
* 	 5. Delete the created user
* 
* Results:
* 	 1+2. The value should not be edited on the configuration file
* 	 3+4. The value should be edited on the configuration file
*      5. User should be deleted.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test143__generate_exists_user_configuration_tests {
	
  private browserTypes  usedBrowser;
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test143__generate_exists_user_configuration_tests(browserTypes browser) {
	  
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
  public void Generate_user_configuration_edit_tests() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String usersNumber		= "1";
	String bodyText 		= "",  mac = "", currUrl = "";
	String Id 				= testFuncs.getId();
	String regPrefix		= "genconftst" + Id;
	String confName 		= "confname"   + Id;
	String confValue 		= "confvalue"  + Id;
	String usersFullName[]  = {regPrefix + "@" + testVars.getDomain()};
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  usersNumber); 
    map.put("startIdx"   ,  usersNumber);

	// Login, create a registered user and user-configuration value (Add+Save)
	testFuncs.myDebugPrinting("Login, create a registered user and user-configuration value (Add+Save)");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUsers(testVars.getIp(),
			  testVars.getPort() 	 	  ,
			  Integer.valueOf(usersNumber),	
			  regPrefix  		 		  ,
			  testVars.getDomain()		  ,
			  "registered"		  		  ,
			  testVars.getDefPhoneModel() ,
			  testVars.getDefTenant()     ,
			  testVars.getDefLocation());
    testFuncs.verifyPostUserCreate(driver, regPrefix, regPrefix, true);
    mac = testFuncs.readFile("mac_1.txt");
 	testFuncs.myDebugPrinting("device mac - " + mac, enumsClass.logModes.MINOR);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_USER_CONFIGURATION, "Manage Multiple Users - User Configuration");
	testFuncs.selectMultipleUsers(driver, regPrefix, usersNumber);
	testFuncs.createNewConfValue(driver, confName, confValue);
	testFuncs.saveConfValues(driver, usersFullName);
    
	// Generate the user configuration
 	testFuncs.myDebugPrinting("Generate the user configuration");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, "Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, regPrefix, usersNumber);
    map.put("action"  , "Generate IP Phones Configuration Files");
    testFuncs.setMultipleUsersAction(driver, map);
	
 	// Step 1 - Edit the created configuration value from the user without generate
 	testFuncs.myDebugPrinting("Step 1 - Edit the created configuration value from the user without generate");
 	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
 	testFuncs.searchUser(driver, regPrefix);  
 	String newConfValue = "new" + confValue;
 	editUserConfigurationValue(driver, regPrefix + "@" + testVars.getDomain(), confName, confValue, newConfValue);

 	// Step 2 - Verify that old value (before the edit) still exists at the configuration file
 	testFuncs.myDebugPrinting("Step 2 - Verify that old value (before the edit) still exists at the configuration file");
 	currUrl  = driver.getCurrentUrl();
 	testFuncs.myDebugPrinting("currUrl - " + currUrl, enumsClass.logModes.MINOR);
    driver.get("https://" + testVars.getIp() + "//configfiles//" + mac + ".cfg");
 	testFuncs.myWait(5000);
	bodyText     = driver.findElement(By.tagName("body")).getText();
	testFuncs.myAssertTrue("Congiuration value was edited without generate !!", bodyText.contains(confName + " = " + confValue));
    driver.get(currUrl);
 	testFuncs.myWait(5000);
    
	// Step 3 - Generate the user configuration
 	testFuncs.myDebugPrinting("Step 3 - Generate the user configuration");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, "Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, regPrefix, usersNumber);
    map.put("action"  , "Generate IP Phones Configuration Files");
    testFuncs.setMultipleUsersAction(driver, map);
    
 	// Step 4 - Verify that new value (after the edit) exists at the configuration file
 	testFuncs.myDebugPrinting("Step 4 - Verify that new value (after the edit) exists at the configuration file");
 	currUrl  = driver.getCurrentUrl();
 	testFuncs.myDebugPrinting("currUrl - " + currUrl, enumsClass.logModes.MINOR);
    driver.get("https://" + testVars.getIp() + "//configfiles//" + mac + ".cfg");
 	testFuncs.myWait(5000);
	bodyText     = driver.findElement(By.tagName("body")).getText();
	testFuncs.myAssertTrue("Congiuration value was edited without generate !!", bodyText.contains(confName + " = " + newConfValue));
    driver.get(currUrl);
 	testFuncs.myWait(5000);

	// Step 5 - Delete the created user
 	testFuncs.myDebugPrinting("Step 5 - Delete the created user");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
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
  
  /**
  *  Edit user configuration value
  *  @param driver       - given driver
  *  @param userName     - given user-name (with domain) of the edited configuration value
  *  @param confName  	 - configuration value name for edit
  *  @param confValue	 - configuration value name for edit
  *  @param newConfValue - new configuration value
  */
  public void editUserConfigurationValue(WebDriver driver, String userName, String confName, String confValue, String newConfValue) {
		 
	  // Enter the configuration-value menu of the user
	  testFuncs.myDebugPrinting("Enter the configuration-value menu of the user", enumsClass.logModes.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[1]/td[4]/a"), 4000);	  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div[2]/div/div[1]", "User configuration (" + userName + ")");			  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='table_keys']/tbody/tr/td[1]", confName);			  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='table_keys']/tbody/tr/td[2]", confValue);			  

	  // Edit the user
	  testFuncs.myDebugPrinting("Edit the user", enumsClass.logModes.MINOR);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_name']") , confName    , 2000);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_value']"), newConfValue, 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div[2]/div/div[2]/div[1]/div[3]/a/span"), 5000);	  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Add Setting");			  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "This setting name is already in use.\nAre you sure you want to replace " + confValue + " to " + newConfValue); 
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save Configuration ( " + userName + " )");			  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "User configuration was saved successfully."); 
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);
	  
	  // Verify the edit
	  testFuncs.myDebugPrinting("Verify the edit", enumsClass.logModes.MINOR);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='table_keys']/tbody/tr/td[1]", confName);			  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='table_keys']/tbody/tr/td[2]", newConfValue);
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
