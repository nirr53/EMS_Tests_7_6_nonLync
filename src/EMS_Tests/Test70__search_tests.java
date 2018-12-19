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
* This test tests the search methods in the Device-status menu
* ----------------
* Tests:
* 	 - Create some registered users.
* 	 - Enter the Device-status menu.
*  	 1. Search for device according to part of its name.
*  	 2. Search for device according to MAC address.
*  	 3. Delete the registered users.
*  
*  Results:
*   1-2. Devices should be found in all cases.
*     3. User should be deleted successfully.
*   
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test70__search_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test70__search_tests(browserTypes browser) {
	  
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
  public void User_search() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String srcUserPrefix 	= "searchTestUser";
	String usersNumber   	= "3";
	int usStartIdx 		 	= 1;
	String dispPrefix   	= srcUserPrefix + testFuncs.getId();
	Map<String, String> map = new HashMap<String, String>();
	map.put("usersNumber",  usersNumber); 
	map.put("startIdx"   ,  String.valueOf(usStartIdx));
	map.put("srcUsername",  "Finished");

	// Step 1 - Create several users using POST query
	testFuncs.myDebugPrinting("Step 1 - Create several users using POST query");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUsers(testVars.getIp()			  ,
						  testVars.getPort() 	 	  ,
						  Integer.valueOf(usersNumber),	
						  dispPrefix				  ,
						  testVars.getDomain()		  ,
						  "registered"		  		  ,
						  "430HD" 					  ,
						  testVars.getDefTenant()     ,
						  testVars.getDefLocation());
	testFuncs.verifyPostUserCreate(driver,  dispPrefix,  dispPrefix, true); 
	       
	// Step 1 - Search for device according to part of its name
	testFuncs.myDebugPrinting("Step 1 - Search for device according to part of its name");
	testFuncs.enterMenu(driver, enumsClass.menuNames.MONITOR_DEVICE_STATUS, "Devices Status");
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input"), "user:" + srcUserPrefix, 5000);
    driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).sendKeys(Keys.ENTER);	    
    testFuncs.myWait(15000);
    String bodyText = driver.findElement(By.tagName("body")).getText();
    for (int i = 1; i < Integer.valueOf(usersNumber); i++) {
    	
    	testFuncs.myAssertTrue("User " + i + " was not detected!!", bodyText.contains(dispPrefix + "_" + i));
    }

	// Step 2 - Search for device according to MAC address
	testFuncs.myDebugPrinting("Step 2 - Search for device according to MAC address");
	int lim = Integer.valueOf(usersNumber) + 1;
	String tempMac;
	for (int i = 1; i < lim; i++) {

			tempMac = testFuncs.readFile("mac_" + i + ".txt");
		  	testFuncs.myDebugPrinting("tempMac - " +  tempMac, enumsClass.logModes.MINOR);		    
			testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input"), "mac:" + tempMac, 5000);
		    driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).sendKeys(Keys.ENTER);	    
		    testFuncs.myWait(7000);
			bodyText = driver.findElement(By.tagName("body")).getText();
			testFuncs.myAssertTrue("Mac " + i + " was not detected!!", bodyText.contains(tempMac));
	}
		
    // Step 3 - Delete the created users
  	testFuncs.myDebugPrinting("Step 3 - Delete the created users");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("usersPrefix"	  , dispPrefix + "_");
    map.put("usersNumber"	  , usersNumber); 
    map.put("startIdx"   	  , String.valueOf(usStartIdx));
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    dispPrefix = dispPrefix.toLowerCase();
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
