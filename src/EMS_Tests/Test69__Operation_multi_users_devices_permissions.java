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
* This test tests the display of users and devices at multiple users/devices menus according to different permissions
* ----------------
* Tests:
*    - Login via Administrator, create a user of default-Tenant with a POST query
*    -                        , create another user of non-default-Tenant with a POST query
* 	 - 	Logout, re-login via an Operation user (system)
* 	 1. Enter Manage multiple devices changes menu and verify that both devices are displayed
* 	 2. Enter Manage multiple users changes menu and verify that both users are displayed
* 	 - 	Re-login via an Operation user (which associate with default tenant)
* 	 3. Enter Manage multiple devices changes menu and verify that only device of default-Tenant is displayed
* 	 4. Enter Manage multiple users changes menu and verify that only user of default-Tenant is displayed
*    5. Logout, re-login as Administrator and delete the created users
*    
* Results:
*	 1-5. As described.
* 
* @author Nir Klieman
* @version 1.00
*/

/**
 * @author nirk
 *
 */
@RunWith(Parameterized.class)
public class Test69__Operation_multi_users_devices_permissions {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test69__Operation_multi_users_devices_permissions(browserTypes browser) {
	  
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
  public void Operation_multi_users_devices_permissions() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String prefix			= "op";
	String nonDefTenant		= testVars.getNonDefTenant(0);
	String Id 				= testFuncs.getId();
	String defTenantUser    = prefix + "deftenant"    + Id;
	String nonDefTenantUser = prefix + "nondeftenant" + Id;
	
	// Login via Administrator, create a user of default Tenant with a POST query
	testFuncs.myDebugPrinting("Login via Administrator, create a user of default Tenant with a POST query");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(),
			  					testVars.getIp()           ,
			  					testVars.getPort()         ,
			  					"1"		        		   ,
			  					defTenantUser  		       ,
			  					testVars.getDomain()       ,
			  					"registered"               ,
			  					testVars.getDefPhoneModel(),
			  					testVars.getDefTenant()    ,
				 				"myLocation");
	
	// Create another user with non-default Tenant with a POST query
	testFuncs.myDebugPrinting("Create another user with non-default Tenant with a POST query");
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(),
			  					testVars.getIp()           ,
			  					testVars.getPort()         ,
			  					"1"		        		   ,
			  					nonDefTenantUser  		   ,
			  					testVars.getDomain()       ,
			  					"registered"               ,
			  					testVars.getDefPhoneModel(),
			  					nonDefTenant    		   ,
				 				"myLocation");	
		 
    // Logout, re-login via an Operation user (system)
	testFuncs.myDebugPrinting("Logout, re-login via an Operation user (system)");
	testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getOperUsername(), testVars.getOperPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
   
    // Step 1 - Enter Manage multiple devices changes menu and verify that both devices are displayed
  	testFuncs.myDebugPrinting("Enter Manage multiple devices changes menu and verify that both devices are displayed");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_devices", "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, defTenantUser	  , "1");
    testFuncs.selectMultipleUsers(driver, nonDefTenantUser, "1");
  	
	// Step 2 - Enter Manage multiple users changes menu and verify that both users are displayed
	testFuncs.myDebugPrinting("Step 2 - Enter Manage multiple users changes menu and verify that both users are displayed");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, defTenantUser	  , "1");
    testFuncs.selectMultipleUsers(driver, nonDefTenantUser, "1");
    
    // Logout, re-login via an Operation user (tenant)
   	testFuncs.myDebugPrinting("Logout, re-login via an Operation user (tenant)");
   	testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
   	testFuncs.login(driver, testVars.getOperTenUsername(), testVars.getOperTenPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
    
    // Step 3 - Enter Manage multiple devices changes menu and verify that only device of Tenant A is displayed
  	testFuncs.myDebugPrinting("Step 3 - Enter Manage multiple devices changes menu and verify that only device of Tenant A is displayed");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_devices", "Manage Multiple Devices");
  	testFuncs.selectMultipleDevices(driver, defTenantUser   , "1");
  	testFuncs.selectMultipleDevices(driver, nonDefTenantUser, "0");
  	
    // Step 4 - Enter Manage multiple users changes menu and verify that only device of default-Tenant is displayed
  	testFuncs.myDebugPrinting("Step 4 - Enter Manage multiple users changes menu and verify that only device of default-Tenant is displayed");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
  	testFuncs.selectMultipleUsers(driver, defTenantUser   , "1");
  	testFuncs.selectMultipleUsers(driver, nonDefTenantUser, "0");
  	
    // Step 5 - Logout, re-login as Administrator and delete the created users
  	testFuncs.myDebugPrinting("Step 5 - Logout, re-login as Administrator and delete the created users");
   	testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
  	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
  	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, Id, "2");
    Map<String, String> map = new HashMap<String, String>();
    map.put("usersPrefix"	  , prefix);
    map.put("usersNumber"	  , "2"); 
    map.put("startIdx"   	  , String.valueOf("2"));
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    testFuncs.searchStr(driver, defTenantUser + "@" + testVars.getDomain()    + " Finished"); 
    testFuncs.searchStr(driver, nonDefTenantUser + "@" + testVars.getDomain() + " Finished"); 	   
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
