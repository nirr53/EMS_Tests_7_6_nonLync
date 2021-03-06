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
* This test tests the Configuration file of ZT created user while we create a tenant configuration
* ----------------
* Tests:
* 	 - Create a registered user with registered device.
* 	 - Create a tenant configuration value
* 	 1. Verify that created value is not displayed at the user configuration file.
* 	 2. Generate the user configuration
* 	 3. Verify that created value displayed at the user configuration file.
* 	 4. Delete the created configuration-value
* 	 5. Delete the created user
* 
* Results:
* 	   1. The value should not be displayed on the configuration file
* 	   3. The value should be displayed on the configuration file
*    4-5. Delete actions should succeed.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test144__tenant_configuration_generate {
	
  private browserTypes  usedBrowser;
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test144__tenant_configuration_generate(browserTypes browser) {
	  
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
  public void Generate_tenant_configuration_tests() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String usersNumber		= "1";
	String bodyText 		= "",  mac = "", currUrl = "";
	String Id 				= testFuncs.getId();
	String regPrefix		= "gentencfgtst" + Id;	
	String cfgKeyName  		= "user_name" + Id;
	String cfgKeyValue 		= "userValue" + Id;	
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  usersNumber); 
    map.put("startIdx"   ,  usersNumber);

	// Login, create a registered user and tenant-configuration value
	testFuncs.myDebugPrinting("Login, create a registered user and tenant-configuration value");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUsers(testVars.getIp()		  	 	,
						  testVars.getPort() 	 	 	,
						  Integer.valueOf(usersNumber)	,	
						  regPrefix  	  		     	,			 
						  testVars.getDomain()	     	,					
						  "registered"		  	     	,						
						  testVars.getDefPhoneModel()	,
						  testVars.getDefTenant()    	,												
						  testVars.getDefLocation());
    testFuncs.verifyPostUserCreate(driver, regPrefix, regPrefix, true);
    mac = testFuncs.readFile("mac_1.txt");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_TENANT_CONFIGURATION, "Tenant Configuration");
	testFuncs.addNewCfgKey(driver, cfgKeyName, cfgKeyValue, testVars.getDefTenant());
	
 	// Step 1 -  Verify that created Tenant-configuration-value is not displayed at the user configuration file
 	testFuncs.myDebugPrinting("Step 1 -  Verify that created Tenant-configuration-value is not displayed at the user configuration file");
 	currUrl  = driver.getCurrentUrl();
 	testFuncs.myDebugPrinting("currUrl - " + currUrl, enumsClass.logModes.MINOR);
    driver.get("https://" + testVars.getIp() + "//configfiles//" + mac + ".cfg");
 	testFuncs.myWait(5000);
	bodyText = driver.findElement(By.tagName("body")).getText();
	testFuncs.myAssertTrue("Tenant-configuration-value was added before generate !!", !bodyText.contains(cfgKeyName));
    driver.get(currUrl);
 	testFuncs.myWait(5000);
    
	// Step 2 - Generate the user configuration
 	testFuncs.myDebugPrinting("Step 2 - Generate the user configuration");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, "Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, regPrefix, usersNumber);
    map.put("action"  , "Generate IP Phones Configuration Files");
    testFuncs.setMultipleUsersAction(driver, map);
    
 	// Step 3 -  Verify that created Tenant-configuration-value is displayed at the user configuration file
 	testFuncs.myDebugPrinting("Step 3 -  Verify that created Tenant-configuration-value is displayed at the user configuration file");
 	currUrl  = driver.getCurrentUrl();
 	testFuncs.myDebugPrinting("currUrl - " + currUrl, enumsClass.logModes.MINOR);
    driver.get("https://" + testVars.getIp() + "//configfiles//" + mac + ".cfg");
 	testFuncs.myWait(5000);
	bodyText = driver.findElement(By.tagName("body")).getText();
	testFuncs.myAssertTrue("Tenant-configuration-value was added before generate !!", bodyText.contains(cfgKeyName + " = " + cfgKeyValue));
    driver.get(currUrl);
 	testFuncs.myWait(5000);
    
	// Step 4 - Delete Tenant configuration value
	testFuncs.myDebugPrinting("Step 4 - Delete Tenant configuration value");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_TENANT_CONFIGURATION, "Tenant Configuration");
    testFuncs.deleteAllConfValues(driver , cfgKeyName, testVars.getDefTenant());
    
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
