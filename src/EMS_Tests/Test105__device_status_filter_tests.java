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
import org.openqa.selenium.support.ui.Select;
import EMS_Tests.enumsClass.*;

/**
* ----------------
* This test tests a filter of devices
* ----------------
* Tests:
* 	 - Create a user+device on default tenant using POST query
* 	 - Create user+device on non-default tenant using POST query
* 	 1. Filter the devices by Site and Tenant
* 	 2. Filter the devices by Tenant
* 	 3. Clear filter On Device-Status menu
* 	 4. Search for non-existing devices
* 	 5. Delete the users.
* 
* Results:
* 	 1. All Devices should be detected
*	 2. Only the Tenant that belongs to the filtered tenant should be detected.
* 	 3. All Devices should be detected
*	 4. None of the device should be detected.
* 	 5. All users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test105__device_status_filter_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test105__device_status_filter_tests(browserTypes browser) {
	  
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
  public void Filter_Tests() throws Exception {

	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	int usStartIdx 		 	= 1;
	int usersNumber			= 1;
	String usersNumberStr   = String.valueOf(usersNumber);
	String dispPrefix   	= "__dfTnUsr" + testFuncs.getId();
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  usersNumberStr); 
    map.put("startIdx"   ,  String.valueOf(usStartIdx));
    map.put("srcUsername",  "Finished");
	
    // Create a user+device on default tenant using POST query
	testFuncs.myDebugPrinting("Create a user+device on default tenant using POST query");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()           ,
			 testVars.getPort()         ,
			 usersNumberStr		        ,
			 dispPrefix  		        ,
			 testVars.getDomain()       ,
			 "registered"               ,
			 testVars.getDefPhoneModel(),
			 testVars.getDefTenant()    ,
			 "myLocation");
	testFuncs.verifyPostUserCreate(driver,  dispPrefix,  dispPrefix, true); 
	testFuncs.myWait(10000);
	
    // Create a user+device on non-default tenant using POST query
	testFuncs.myDebugPrinting("Create a user+device on non-default tenant using POST query");
	String dispPrefix2 = "__nnDfTnUsr" + testFuncs.getId();
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()           ,
			 testVars.getPort()         ,
			 usersNumberStr		        ,
			 dispPrefix2  		        ,
			 testVars.getDomain()       ,
			 "registered"               ,
			 testVars.getDefPhoneModel(),
			 testVars.getNonDefTenant(0),
			 "myLocation");
	testFuncs.verifyPostUserCreate(driver,  dispPrefix2,  dispPrefix2, true); 
	   
	// Step 1 - Filter by Site and Tenant
	testFuncs.myDebugPrinting("Step 1 - Filter by Site and Tenant");
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/a")			 , 7000);
	testFuncs.myClick(driver, By.xpath("//*[@id='searchForm']/div[15]/div/button[2]"), 7000);
	new Select(driver.findElement(By.name("inputTenant[]"))).selectByVisibleText(testVars.getDefTenant().toLowerCase());
	testFuncs.myWait(2000);
	new Select(driver.findElement(By.name("inputSite[]"))).selectByVisibleText(testVars.getDefSite().toLowerCase());
	testFuncs.myWait(2000);
	testFuncs.myClick(driver, By.xpath("//*[@id='searchForm']/div[15]/div/button[1]"), 7000);
	testFuncs.searchStr(driver, dispPrefix);
	testFuncs.myAssertTrue("User with other tenant was detected !!", !driver.findElement(By.tagName("body")).getText().contains(dispPrefix2));
	
	// Step 2 - Filter by Tenant
	testFuncs.myDebugPrinting("Step 2 - Filter by Tenant");
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/a")			 , 7000);
	testFuncs.myClick(driver, By.xpath("//*[@id='searchForm']/div[15]/div/button[2]"), 7000);
	new Select(driver.findElement(By.name("inputTenant[]"))).selectByVisibleText(testVars.getDefTenant().toLowerCase());
	testFuncs.myWait(2000);
	testFuncs.myClick(driver, By.xpath("//*[@id='searchForm']/div[15]/div/button[1]"), 7000);
	testFuncs.searchStr(driver, dispPrefix);
	testFuncs.myAssertTrue("User with other tenant was detected !!", !driver.findElement(By.tagName("body")).getText().contains(dispPrefix2));

	// Step 3 - Clear filter On Device-Status menu
	testFuncs.myDebugPrinting("Step 3 - Clear filter On filter menu");
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/h3/div/a[4]"), 3000);
	testFuncs.searchStr(driver, dispPrefix);
	testFuncs.searchStr(driver, dispPrefix2);
	
	// Step 4 - Search for non-existing devices
	testFuncs.myDebugPrinting("Step 4 - Search for non-existing devices");
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/a")			 , 7000);
	testFuncs.myClick(driver, By.xpath("//*[@id='searchForm']/div[15]/div/button[2]"), 7000);
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='inputUser']"), "abcdefghyhjdhaj", 2000);
	testFuncs.myClick(driver, By.xpath("//*[@id='searchForm']/div[15]/div/button[1]"), 7000);
	testFuncs.searchStr(driver, "There are no devices that fit this search criteria");
	
    // Step 5 - Delete the created users
  	testFuncs.myDebugPrinting("Step 5 - Delete the created users");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumberStr);
    map.put("usersNumber"	  , usersNumberStr); 
    map.put("usersPrefix"	  , dispPrefix);
    map.put("startIdx"   	  , String.valueOf(usStartIdx));
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
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