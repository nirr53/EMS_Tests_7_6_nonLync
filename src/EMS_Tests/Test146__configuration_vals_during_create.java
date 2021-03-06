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
* This test tests the add of configuration values to Configuration file of ZT created user
* ----------------
* Tests:
* 	 - Create a Site configuration value
* 	 - Create a Tenant configuration value
* 	 - Create a registered user with registered device 
* 	 1. Verify the configuration-file of the new user holds the Tenant and Site configuration values.
* 	 2. Delete the created tenant and site-configuration-values
* 	 3. Delete the created user
* 
* Results:
* 	   1. As described.
* 	 2-3. Delete actions should succeed.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test146__configuration_vals_during_create {
	
  private browserTypes  usedBrowser;
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test146__configuration_vals_during_create(browserTypes browser) {
	  
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
  public void Tenant_site_configuration_during_create_tests() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String usersNumber		 = "1";
	String bodyText 		 = "",  mac = "", currUrl = "";
	String Id 				 = testFuncs.getId();
	String regPrefix		 = "gnsitetencfgtst"  + Id;	
	String siteCfgKeyName    = "siteCfgName1_"    + Id;
	String siteCfgKeyValue   = "siteCfgValue1_"   + Id;
	String tenantCfgKeyName  = "tenantCfgName1_"  + Id;
	String tenantCfgKeyValue = "tenantCfgValue1_" + Id;
	String tenant 		     = testVars.getDefTenant();
	String site	  		     = testVars.getDefSite() + " [" + testVars.getDefSite() + "] / " + testVars.getDefTenant();
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  usersNumber); 
    map.put("startIdx"   ,  usersNumber);

	// Login the system
	testFuncs.myDebugPrinting("Login the system");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  

	// Create Site and Tenant configuration values
	testFuncs.myDebugPrinting("Create Site and Tenant configuration values");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SITE_CONFIGURATION, "Site Configuration");
	testFuncs.selectSite(driver, site);
    testFuncs.addNewSiteCfgKey(driver, siteCfgKeyName, siteCfgKeyValue, tenant, site);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_TENANT_CONFIGURATION, "Tenant Configuration");
	testFuncs.addNewCfgKey(driver, tenantCfgKeyName, tenantCfgKeyValue, tenant);

    // Create a registered user
	testFuncs.myDebugPrinting("Create a registered user");
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
      
 	// Step 1 - Verify the configuration-file of the new user holds the Tenant-A and Site-A values
 	testFuncs.myDebugPrinting("Step 1 - Verify the configuration-file of the new user holds the Tenant-A and Site-A values");
 	currUrl  = driver.getCurrentUrl();
 	testFuncs.myDebugPrinting("currUrl - " + currUrl, enumsClass.logModes.MINOR);
    driver.get("https://" + testVars.getIp() + "//configfiles//" + mac + ".cfg");
 	testFuncs.myWait(5000);
	bodyText = driver.findElement(By.tagName("body")).getText();
	String siteCfgSearch = siteCfgKeyName   + " = " + siteCfgKeyValue;
	String tenCfgSearch  = tenantCfgKeyName + " = " + tenantCfgKeyValue;
	testFuncs.myAssertTrue("Site-configuration-value was not added during create !! <"  + siteCfgSearch + ">", bodyText.contains(siteCfgSearch));
	testFuncs.myAssertTrue("Tenant-configuration-value was not added during create !! <"+ tenCfgSearch  + ">", bodyText.contains(tenCfgSearch));
	driver.get(currUrl);
 	testFuncs.myWait(5000);
    
	// Step 2 - Delete Site and Tenant configuration value
	testFuncs.myDebugPrinting("Step 2 - Delete Site and Tenant configuration value");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SITE_CONFIGURATION, "Site Configuration");
	testFuncs.selectSite(driver, site);
	testFuncs.deleteSiteCfgKey(driver, siteCfgKeyName, siteCfgKeyValue, tenant, site, testVars.getDefSite());
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_TENANT_CONFIGURATION, "Tenant Configuration");
    testFuncs.deleteAllConfValues(driver , tenantCfgKeyName, tenant);
	
	// Step 3 - Delete the created user
 	testFuncs.myDebugPrinting("Step 3 - Delete the created user");
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
