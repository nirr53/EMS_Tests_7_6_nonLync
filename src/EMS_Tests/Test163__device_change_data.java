package EMS_Tests;

import java.util.ArrayList;
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
* This test tests an update of the device-status fields
* ----------------
* Tests:
* 	 - Create a registered user via POST query.
* 	 1. Change the device name
* 	    Change the device model
* 		Change the device status
* 		Change the device location
* 		Change the device phone number
* 		Change the device version
*    2. Delete the created user
* 
* Results:
* 	 1. Device data should be updated as excepted.
*    2. Delete should end successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test163__device_change_data {
	
  private browserTypes  usedBrowser;
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test163__device_change_data(browserTypes browser) {
	  
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
  public void Change_device_data() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables and login
	testFuncs.myDebugPrinting("// Set variables and login");
	String chngSttsUsername = "cStts" + testFuncs.getId();
	String location 		= "myLocation";
	String phoneNumber		= "+97239764713";
	String currDispTime		= "";
	ArrayList<String> times;
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	
    // Create a registered user
	testFuncs.myDebugPrinting("Create a registered user");
	times = testFuncs.getCurrHours();
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()           ,
			 												 testVars.getPort()         ,
			 												 "1"				        ,
			 												 chngSttsUsername           ,
			 												 testVars.getDomain()       ,
			 												 "registered"          	    ,
			 												 testVars.getDefPhoneModel(),
			 												 testVars.getDefTenant()    ,
			 												 location);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
    testFuncs.verifyPostUserCreate(driver, chngSttsUsername, chngSttsUsername, true); 
	currDispTime = driver.findElement(By.xpath("//*[@id='table']/tbody[1]/tr/td[10]")).getText();
	testFuncs.myDebugPrinting("currDispTime - " + currDispTime, enumsClass.logModes.MINOR);  
//	testFuncs.myAssertTrue("Time is not displayed !! <" + currDispTime + ">", currDispTime.contains(times.get(0)) ||
//																			  currDispTime.contains(times.get(1)) ||
//																			  currDispTime.contains(times.get(2)) ||
//																			  currDispTime.contains(times.get(3)) ||
//																			  currDispTime.contains(times.get(4)));

    // Step 1 -  Change the user's device data
 	testFuncs.myDebugPrinting("Step 1 -  Change the user's device data");
 	String newUsername = "new_" + chngSttsUsername;
 	String newPhoneNumber = "new" + phoneNumber;
	String newModel 		= "420HD";
	String newVserion 		= "UC_3.1.0.478";
	String newLocation = "new" + location;
	testFuncs.sendKeepAlivePacket(testVars.getKpAlveBatName()	 ,
								  testVars.getIp()               ,
								  testVars.getPort()           	 ,
								  testFuncs.readFile("mac_1.txt"),
								  newUsername	 	 			 ,
								  newModel	 					 ,
								  testVars.getDomain()        	 ,
								  "offline"						 ,
								  newLocation		 	         ,
								  newPhoneNumber			     ,
								  newVserion);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MONITOR_DEVICE_STATUS, "Devices Status");
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input"), "user:" + chngSttsUsername.trim(), 5000);
    driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).sendKeys(Keys.ENTER);	    
//	currDispTime = driver.findElement(By.xpath("//*[@id='table']/tbody[1]/tr/td[10]")).getText();

    // Verify new device data
  	testFuncs.myDebugPrinting("Verify new device data");
  	testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[8]" , newUsername);  
  	testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[9]" , newPhoneNumber);  
  	testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[13]", newModel); 
  	testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[14]", newVserion);  
  	testFuncs.verifyStrByXpath(driver, "//*[@id='table']/tbody[1]/tr/td[19]", newLocation);  
    String attClass = driver.findElement(By.xpath("//*[@id='table']/tbody[1]/tr/td[7]/i")).getAttribute("class");
    testFuncs.myAssertTrue("Offline Icon was not detected !! <" + attClass + ">", attClass.contains("fa-times-circle"));  
	times = testFuncs.getCurrHours();
//	testFuncs.myAssertTrue("Time is not displayed !! <" + currDispTime + ">", currDispTime.contains(times.get(0)) ||
//																			  currDispTime.contains(times.get(1)) ||
//																			  currDispTime.contains(times.get(2)) ||
//																			  currDispTime.contains(times.get(3)) ||
//																			  currDispTime.contains(times.get(4)));
// 
    // Step 2 - Delete the created user
  	testFuncs.myDebugPrinting("Step 2 - Delete the created user");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, chngSttsUsername, "1");
	Map<String, String> map = new HashMap<String, String>();
    map.put("startIdx"   	  ,  String.valueOf(1));
    map.put("usersNumber"	  ,  "1");
    map.put("usersPrefix"	  ,  chngSttsUsername);
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    testFuncs.searchStr(driver, chngSttsUsername.toLowerCase() + "@" + testVars.getDomain() + " Finished");
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
