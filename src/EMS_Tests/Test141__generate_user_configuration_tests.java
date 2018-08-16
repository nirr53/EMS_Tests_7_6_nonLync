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
* This test tests the Configuration file of ZT created user
* ----------------
* Tests:
* 	 - Create a registered user with registered device.
* 	 1. Verify that the configuration file of the user appears at Configuration-files menu while ZT create
* 	 2. Create a user-configuration value (Add+Save)
* 	 3. Verify that Before-Generate the value does not exists at the configuration value.
*  	 4. Generate the user configuration
* 	 5. Verify that After-Generate the value does exists at the configuration value.
*	 6. Delete the created user
* 
* Results:
* 	 1.   File should be displayed
*    2-5. Only after generate the value should be displayed.
*    6.   User should be deleted.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test141__generate_user_configuration_tests {
	
  private browserTypes  usedBrowser;
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test141__generate_user_configuration_tests(browserTypes browser) {
	  
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
  public void Generate_user_configuration_tests() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String usersNumber		= "1";
	String bodyText 		= "",  mac = "", currUrl = "";
	String Id 				= testFuncs.getId();
	String regPrefix		= "genconftst" + Id;
	String confName 		= "confName"   + Id;
	String confValue 		= "confValue"  + Id;
	String usersFullName[] = {regPrefix + "@" + testVars.getDomain()};
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  usersNumber); 
    map.put("startIdx"   ,  usersNumber);

	// Login and create a registered user
	testFuncs.myDebugPrinting("Login and create a registered user");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
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
 	testFuncs.myDebugPrinting("device mac - " + mac, enumsClass.logModes.MINOR);
	
 	// Step 1 - Verify that the configuration file of the user appears at Configuration-files menu while ZT create
 	testFuncs.myDebugPrinting("Step 1 - Verify that the configuration file of the user appears at Configuration-files menu while ZT create");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_CONF_FILES, "Manage Configuration Files");
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='searchInput']"), mac  , 7000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='fbody']/tr[1]/td[3]/a[2]", mac);
	
    // Step 2 - Create a user-configuration value (Add+Save)
	testFuncs.myDebugPrinting("Step 2 - Create a user-configuration value (Add+Save)");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_USER_CONFIGURATION, "Manage Multiple Users - User Configuration");
	testFuncs.selectMultipleUsers(driver, regPrefix, usersNumber);
	testFuncs.createNewConfValue(driver, confName, confValue);
	testFuncs.saveConfValues(driver, usersFullName);
	
 	// Step 3 -  Verify that Before-Generate the value does not exists at the configuration value
 	testFuncs.myDebugPrinting("Step 3 -  Verify that Before-Generate the value does not exists at the configuration value");
 	currUrl  = driver.getCurrentUrl();
 	testFuncs.myDebugPrinting("currUrl - " + currUrl, enumsClass.logModes.MINOR);
    driver.get("https://" + testVars.getIp() + "//configfiles//" + mac + ".cfg");
 	testFuncs.myWait(5000);
	bodyText = driver.findElement(By.tagName("body")).getText();
	testFuncs.myAssertTrue("Congiuration value was added before generate !!", !bodyText.contains(confName));
    driver.get(currUrl);
    
	// Step 4 - Generate the user configuration
 	testFuncs.myDebugPrinting("Step 4 - Generate the user configuration");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, "Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, regPrefix, usersNumber);
    map.put("action"  , "Generate IP Phones Configuration Files");
    testFuncs.setMultipleUsersAction(driver, map);
	
 	// Step 5 - Verify that After-Generate the value does exists at the configuration value
 	testFuncs.myDebugPrinting("Step 5 - Verify that After-Generate the value does exists at the configuration value");
 	currUrl  = driver.getCurrentUrl();
 	testFuncs.myDebugPrinting("currUrl - " + currUrl, enumsClass.logModes.MINOR);
    driver.get("https://" + testVars.getIp() + "//configfiles//" + mac + ".cfg");
 	testFuncs.myWait(5000);
	bodyText     = driver.findElement(By.tagName("body")).getText();
	testFuncs.myAssertTrue("Congiuration value was not added successfully !!", bodyText.contains(confName + " = " + confValue));
    driver.get(currUrl);
 	
	// Step 6 - Delete the created user
 	testFuncs.myDebugPrinting("Step 6 - Delete the created user");
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
