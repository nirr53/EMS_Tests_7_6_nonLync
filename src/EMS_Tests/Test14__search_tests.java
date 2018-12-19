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
* This test tests the search of users in the manage-multiple-users menu
* ----------------
* Tests:
* 	 - Enter the Import users menu.
* 	 1. Create several users using POST query.
* 	 2. Search the users by their name.
*    3. Search the user by its tenant
*    4. Search the user by its MAC
* 	 5. Delete the users.
* 
* Results:
* 	 1.   Users should be created successfully.
* 	 2-4. The users should be detected successfully.
* 	   5. Users should be deleted successfully.
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test14__search_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test14__search_tests(browserTypes browser) {
	  
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
  public void Manage_multiple_users_search_users() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String srcUserPrefix = "srcUser";
	String usersNumber   = "3";
	int lim 			 = Integer.parseInt(usersNumber) + 1;
	String dispPrefix    = srcUserPrefix + testFuncs.getId();
	String bodyText		 = "";
	int usStartIdx 		 = 1;

    // Step 1 - Create several users using POST query
	testFuncs.myDebugPrinting("Step 1 - Create several users using POST query");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUsers(testVars.getIp()			  ,
						  testVars.getPort() 	 	  ,
						  Integer.valueOf(usersNumber),	
						  dispPrefix  		 		  ,
						  testVars.getDomain()		  ,
						  "registered"		  		  ,
						  testVars.getDefPhoneModel() ,
						  testVars.getDefTenant()     ,
						  testVars.getDefLocation());
	testFuncs.verifyPostUsersCreate(driver,  dispPrefix,  dispPrefix, true, Integer.valueOf(usersNumber));	
    String macs[] = {testFuncs.readFile("mac_1.txt"),
    				 testFuncs.readFile("mac_2.txt"),
    				 testFuncs.readFile("mac_3.txt")};

    // Step 2 - Search the user by its name
	testFuncs.myDebugPrinting("Step 2 - Search the user by its name");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='filterinput']"), dispPrefix, 2000);
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/form/div/div[2]/div/table/tbody/tr[2]/td/div/div/a/span"), 7000);
     bodyText = driver.findElement(By.tagName("body")).getText();
	for (int i = 1; i < lim ; ++i) {
		
		String currUser = dispPrefix + "_" + i;
		testFuncs.myDebugPrinting("Search for user: " + currUser, enumsClass.logModes.MINOR);
		testFuncs.myAssertTrue("User <" + currUser + "> was not detected via search !!", bodyText.contains(currUser));
	}
	  
	// Step 3 - Search the user by its tenant
	testFuncs.myDebugPrinting("Step 3 - Search the user by its tenant");
	Select tenValues = new Select(driver.findElement(By.xpath("//*[@id='branch_search']")));
	tenValues.selectByVisibleText(testVars.getDefTenant());
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/form/div/div[2]/div/table/tbody/tr[2]/td/div/div/a/span"), 7000);
    bodyText = driver.findElement(By.tagName("body")).getText();
	for (int i = 1; i < lim ; ++i) {
		
		String currUser = dispPrefix + "_" + i;
		testFuncs.myDebugPrinting("Search for user: " + currUser, enumsClass.logModes.MINOR);
		testFuncs.myAssertTrue("User <" + currUser + "> was not detected via search !!", bodyText.contains(currUser));
	}
	
    // Step 4 - Search the user by its MAC
	testFuncs.myDebugPrinting("Step 4 - Search the user by its MAC");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
	for (int i = 1; i < lim ; ++i) {
		
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='filterinput']"), macs[i-1], 2000);
		testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/form/div/div[2]/div/table/tbody/tr[2]/td/div/div/a/span"), 7000);
	    bodyText = driver.findElement(By.tagName("body")).getText();
		String currUser = dispPrefix + "_" + i;
		testFuncs.myDebugPrinting("Search for MAC: " + macs[i-1], enumsClass.logModes.MINOR);
		testFuncs.myAssertTrue("User <" + currUser + "> was not detected via search !!", bodyText.contains(currUser));
	}
	
    // Step 5 - Delete the created users
  	testFuncs.myDebugPrinting("Step 5 - Delete the created users");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    Map<String, String> map = new HashMap<String, String>();
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
