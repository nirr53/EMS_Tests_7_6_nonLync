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
* This test tests the create-user options (manually and via POST query)
* ----------------
* Tests:
* 	 1. Add new user manually.
* 	 2. Add a registered user.
* 	 3. Add an unregistered user
* 	 4. Verify that configuration file is not created for unregistered user
* 	 5. Delete the created users
* 
* Results:
* 	 1. User should be added successfully.
* 	 2. User should be added successfully and appear 'registered' in DEvice-status menu.
* 	 3. User should be added unsuccessfully.
* 	 4. Configuration file is not created for unregistered user.
* 	 5. Users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test5__add_user_tests {
	
  private browserTypes  usedBrowser;
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  Map<String, String> 	map;
  String 				Id, prefix, rePrefix, unregPrefix, manPrefix;
  
  // Default constructor for print the name of the used browser 
  public Test5__add_user_tests(browserTypes browser) {
	  
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
	map 	  = new HashMap<String, String>();
	Id 		  = testFuncs.getId();
	prefix	  = "pstUsrPrfx";
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void User_create() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
    map.put("usersNumber",  "1"); 
    map.put("startIdx"   ,  String.valueOf(1));
	String mac				= "";
	manPrefix 				= prefix + "0_" + Id;
	rePrefix 				= prefix + "1_" + Id;
	unregPrefix 			= prefix + "2_" + Id;
	
	// login
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	
    // Step 1 - Create user manually
	testFuncs.myDebugPrinting("Step 1 - Create user manually");
	testFuncs.addUser(driver, manPrefix, "1q2w3e$r", manPrefix, testVars.getDefTenant());
	testFuncs.myWait(2000);

    // Step 2 - Create a registered user using POST method
	testFuncs.myDebugPrinting("Step 2 - Create a registered user using POST method");
	testFuncs.createUsers(testVars.getIp()	  		 ,
						  testVars.getPort() 	 	 ,
						  1					  		 ,
						  rePrefix  		 		 ,
						  testVars.getDomain()		 ,
						  "registered"		  		 ,
						  testVars.getDefPhoneModel(),
						  testVars.getDefTenant()    ,
						  testVars.getDefLocation());
    testFuncs.verifyPostUserCreate(driver,rePrefix, rePrefix, true);
    testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
    
    // Step 3 - Create an unregistered user using POST method
	testFuncs.myDebugPrinting("Step 3 - Create an unregistered user using POST method");  
	testFuncs.createUsers(testVars.getIp()	  		 ,
						  testVars.getPort() 	 	 ,
						  1					  		 ,
						  unregPrefix 		 		 ,
						  testVars.getDomain()		 ,
						  "unregistered"		  	 ,
						  testVars.getDefPhoneModel(),
						  testVars.getDefTenant()    ,
						  testVars.getDefLocation());
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "Manage Users");
    testFuncs.verifyPostUserCreate(driver, unregPrefix, unregPrefix, false);
    
    // Step 4 - Verify that configuration file is not created for unregistered user
 	testFuncs.myDebugPrinting("Step 4 - Verify that configuration file is not created for unregistered user");  
    mac = testFuncs.readFile("mac_1.txt");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_CONF_FILES, "Manage Configuration Files");
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='searchInput']"), mac  , 7000);
	testFuncs.myAssertTrue("Configuration file was detected !!", !driver.findElement(By.tagName("body")).getText().contains(mac));
  }

  @After
  public void tearDown() throws Exception {  
	  
	  //  Delete the users	  
	  testFuncs.myDebugPrinting("Delete the created users");		
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");  
	  testFuncs.selectMultipleUsers(driver, prefix, "2"); 
	  map.put("usersPrefix"	  , prefix);	  
	  map.put("srcUsername"	  , "Finished");	  
	  map.put("action"	 	  , "Delete Users");  
	  map.put("skipVerifyDelete", "true");	  
	  testFuncs.setMultipleUsersAction(driver, map);  
	  prefix = prefix.toLowerCase();  
	  testFuncs.searchStr(driver, manPrefix.toLowerCase() + " Finished");
	  testFuncs.searchStr(driver, rePrefix.toLowerCase()  + "@" + testVars.getDomain() + " Finished");
    
	  // Close session
	  driver.quit();
	  System.clearProperty("webdriver.chrome.driver");
	  System.clearProperty("webdriver.ie.driver");
	  String verificationErrorString = verificationErrors.toString(); 
	  if (!"".equals(verificationErrorString)) {
    	   
		  testFuncs.myFail(verificationErrorString);
    
	  }	  
  }
}
