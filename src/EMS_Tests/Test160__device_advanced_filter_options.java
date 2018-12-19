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
* 	 1. Search device by User
* 	 2. Search device by phone number
* 	 3. Search device by MAC address.
* 	 4. Search device by IP address.
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
public class Test160__device_advanced_filter_options {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test160__device_advanced_filter_options(browserTypes browser) {
	  
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
	String userName     = "dvFilter" + testFuncs.getId();
    Map<String, String> map = new HashMap<String, String>();

    // Login and create user using POST query
	testFuncs.myDebugPrinting("Login and create user using POST query");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
    testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUsers(testVars.getIp()		  	 	,
						  testVars.getPort() 	 	 	,
						  Integer.valueOf(1)			,	
						  userName  	  		     	,			 
						  testVars.getDomain()	     	,					
						  "registered"		  	     	,						
						  testVars.getDefPhoneModel()	,
						  testVars.getDefTenant()    	,												
						  testVars.getDefLocation());
	testFuncs.enterMenu(driver, enumsClass.menuNames.MONITOR_DEVICE_STATUS, "Devices Status");

    // Step 1 - Search device by User
	testFuncs.myDebugPrinting("Step 1 - Search device by User");
	testFuncs.deviceFilter(driver, enumsClass.deviceFilter.USER, new String[]{userName}, userName);
	   
	// Step 2 - Search device by Phone number
	testFuncs.myDebugPrinting("Step 2 - Search device by Phone number");
	String phoneNumber = driver.findElement(By.xpath("//*[@id='table']/tbody[1]/tr/td[9]")).getText();
	testFuncs.myDebugPrinting("phoneNumber - " + phoneNumber, enumsClass.logModes.MINOR);			
	testFuncs.deviceFilter(driver, enumsClass.deviceFilter.PHONE_NUMBER, new String[]{phoneNumber}, userName);

	// Step 3 - Search device by MAC address
	testFuncs.myDebugPrinting("Step 3 - Search device by MAC address");
	String macAddr = driver.findElement(By.xpath("//*[@id='table']/tbody[1]/tr/td[11]")).getText();
	testFuncs.myDebugPrinting("macAddr - " + macAddr, enumsClass.logModes.MINOR);			
	testFuncs.deviceFilter(driver, enumsClass.deviceFilter.MAC_ADDRESS, new String[]{macAddr}, userName);
	
	// Step 4 - Search device by IP address
	testFuncs.myDebugPrinting("Step 4 - Search device by IP address");
	String ipAddr = testFuncs.readFile("ip_1.txt");
	testFuncs.myDebugPrinting("IP addr - " + ipAddr, enumsClass.logModes.MINOR);			
	testFuncs.deviceFilter(driver, enumsClass.deviceFilter.IP_ADDRESS, new String[]{ipAddr}, userName);
	
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
	  
    driver.quit();
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
    	testFuncs.myFail(verificationErrorString);
    }
  }
}
