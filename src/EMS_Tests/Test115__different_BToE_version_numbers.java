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
* This test tests the create of users with different BToE statuses with different BToE Version versions.
* ----------------
* Tests:
* 	 - Enter Manage multiple users changes menu.
* 	 1. Create a user using POST query with "BToE disabled" status with version.
* 	 2. Create a user using POST query with "auto paired"   status with characters version.
* 	 3. Delete the users.
* 
* Results:
* 	 1-2. User should be created successfully with the given status and given BToE Version.
* 	   3. Users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test115__different_BToE_version_numbers {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test115__different_BToE_version_numbers(browserTypes browser) {
	  
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
  public void Different_BToE_version_numbers() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables and login
	String Id             		  = testFuncs.getId();
	String prefixName     		  = "BToE_user_version";
	String usersNumber	  		  = "2";
	String version1		  		  = "UC_2.0.13.1234";
	String btoeDisabled   		  = prefixName + "_dis_"    + Id;
	String btoeAutoPaired 		  = prefixName + "_auto_"   + Id;
    Map<String, String> extraData = new HashMap<String, String>();
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	
    // Step 1 - Create a user using POST query with "BToE disabled" status with version
	testFuncs.myDebugPrinting("Step 1 - Create a user using POST query with \"BToE disabled\" status with version.");
    extraData.put("BToEpairingstatus", "BToE disabled");
    extraData.put("BToEversion"		 , version1);
	testFuncs.createUsers(testVars.getIp()				,
						  testVars.getPort() 	 	 	,
						  Integer.valueOf(1)			,	
						  btoeDisabled  	  		    ,			 
						  testVars.getDomain()	     	,					
						  "registered"		  	     	,						
						  testVars.getDefPhoneModel()	,
						  testVars.getDefTenant()    	,												
						  testVars.getDefLocation()		,
						  extraData);
	testFuncs.verifyPostUserCreate(driver, btoeDisabled, btoeDisabled, true);
	checkBToE("disabled", version1);  
	
    // Step 2 - Create a user using POST query with "auto paired" status with characters version
	testFuncs.myDebugPrinting("Step 2 - Create a user using POST query with \"auto paired\" status with characters version");
	String version2 = "UC_abcdef";
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
    extraData.put("BToEpairingstatus", "auto paired");
    extraData.put("BToEversion"		 , version2);
	testFuncs.createUsers(testVars.getIp()				,
						  testVars.getPort() 	 	 	,
						  Integer.valueOf(1)			,	
						  btoeAutoPaired  	  		    ,			 
						  testVars.getDomain()	     	,					
						  "registered"		  	     	,						
						  testVars.getDefPhoneModel()	,
						  testVars.getDefTenant()    	,												
						  testVars.getDefLocation()		,
						  extraData);
	testFuncs.verifyPostUserCreate(driver, btoeAutoPaired, btoeAutoPaired, true);
	checkBToE("automatic", version2);  
	
    // Step 3 - Delete the users
  	testFuncs.myDebugPrinting("Step 3 - Delete the users");
    Map<String, String> map = new HashMap<String, String>();
    map.put("action"	      , "Delete Users");
    map.put("srcUsername"     , "Finished");
    map.put("skipVerifyDelete", "true");
    map.put("usersPrefix"     , "btoe");
    map.put("usersNumber"	  , usersNumber); 
    map.put("startIdx"   	  , String.valueOf(1));  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, "btoe", usersNumber);   
    testFuncs.setMultipleUsersAction(driver, map);
    testFuncs.searchStr(driver, btoeDisabled.toLowerCase()   + "@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, btoeAutoPaired.toLowerCase() + "@" + testVars.getDomain() + " Finished");
  }
  
  private void checkBToE(String state, String version) {
	      
	  // Check BToE
	  testFuncs.myDebugPrinting("Check that BToE icon and BToE version were detected !", enumsClass.logModes.NORMAL);  
	  String title = driver.findElement(By.xpath("//*[@id='table']/tbody[1]/tr/td[5]/i")).getAttribute("title");
	  String style = driver.findElement(By.xpath("//*[@id='table']/tbody[1]/tr/td[5]/i")).getAttribute("style");  
	  testFuncs.myDebugPrinting("state - " + state, enumsClass.logModes.MINOR);  
	  switch (state) {
	  
	  	case "disabled":
	  		
	  		testFuncs.myAssertTrue("State was not detected !! (title - " + title + ")", title.contains("BToE disabled"));
	  		testFuncs.myAssertTrue("color was not detected !! (style - " + style + ")", style.contains("color: red"));
	  		break;	  		
	  	case "automatic":
	  		
	  		testFuncs.myAssertTrue("State was not detected !! (title - " + title + ")", title.contains("auto paired"));
	  		testFuncs.myAssertTrue("color was not detected !! (style - " + style + ")", style.contains("color: green"));
	  		break;
	  	case "manual":
	  		
	  		testFuncs.myAssertTrue("State was not detected !! (title - " + title + ")", title.contains("manual paired"));
	  		testFuncs.myAssertTrue("color was not detected !! (style - " + style + ")", style.contains("color: blue"));
	  		break;
	  	case "notPaired":
	  		
	  		testFuncs.myAssertTrue("State was not detected !! (title - " + title + ")", title.contains("not paired"));
	  		testFuncs.myAssertTrue("color was not detected !! (style - " + style + ")", style.contains("color: orange"));
	  		break;
	  }; 
	  
	  // Check version
	  testFuncs.myDebugPrinting("Check version", enumsClass.logModes.NORMAL); 
	  testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[22]", version);	  
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
