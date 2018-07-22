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
* This test tests the different methods of devices filter
* ----------------
* Tests:
* 	 - Login and create user using POST query
* 	 1. Search device by Tenant
* 	 2. Search device by Version
* 	 3. Search device by Template.
* 	 4. Search device by Site.
* 	 5. Delete the users.
* 
* Results:
* 	 1-4. Device should be detected.
* 	   5. Users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test161__device_advanced_filter_options2 {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test161__device_advanced_filter_options2(browserTypes browser) {
	  
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
  public void Devices_advanced_filter() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());	
	
	// Set variables
	String userName     = "dvFilter2" + testFuncs.getId();
	String newTenant	= testVars.getNonDefTenant(0);
	String newVersion	= "UC_3.1.0.478";
	String newModel[]	= {"405", "audiocodes_405_lync"};


    Map<String, String> map = new HashMap<String, String>();

    // Login and create user using POST query
	testFuncs.myDebugPrinting("Login and create user using POST query");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(),
								testVars.getIp()           ,
								testVars.getPort()         ,
								"1"		        		   ,
								userName  		           ,
								testVars.getDomain()       ,
								"registered"               ,
								newModel[0]				   ,
								newTenant	   			   ,
								"myLocation");
	testFuncs.enterMenu(driver, enumsClass.menuNames.MONITOR_DEVICE_STATUS, "Devices Status");

    // Step 1 - Search device by Tenant
	testFuncs.myDebugPrinting("Step 1 - Search device by Tenant");
	testFuncs.deviceFilter(driver, enumsClass.deviceFilter.TENANT, new String[]{newTenant.toLowerCase()}, userName);
	
	// Step 2 - Search device by Version
	testFuncs.myDebugPrinting("Step 2 - Search device by Version");
	testFuncs.deviceFilter(driver, enumsClass.deviceFilter.VERSION, new String[]{newVersion}, userName);

	// Step 3 - Search device by Template
	testFuncs.myDebugPrinting("Step 3 - Search device by Template");
	testFuncs.deviceFilter(driver, enumsClass.deviceFilter.TEMPLATE, new String[]{newModel[1]}, userName);
	
	// Step 4 - Search device by Site
	testFuncs.myDebugPrinting("Step 4 - Search device by Site");
	testFuncs.deviceFilter(driver, enumsClass.deviceFilter.SITE, new String[]{testVars.getDefSite().toLowerCase()}, userName);
	
    // Step 5 - Delete the created users
  	testFuncs.myDebugPrinting("Step 5 - Delete the created users");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, userName, "1");
    map.put("usersPrefix"	  , userName);
    map.put("usersNumber"	  , "1"); 
    map.put("startIdx"   	  , String.valueOf("1"));
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    testFuncs.searchStr(driver, userName.toLowerCase() + "@" + testVars.getDomain() + " Finished");
  }
  
  @After
  public void tearDown() throws Exception {
	  
//    driver.quit();
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
    	testFuncs.myFail(verificationErrorString);
    }
  }
}
