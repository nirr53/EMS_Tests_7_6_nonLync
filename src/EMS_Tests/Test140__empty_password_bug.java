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
* This test tests the Empty password bug
* ----------------
* Tests:
* 	 - Create a registered user with registered device.
* 	 - Enter the Device-Status menu and generate it.
* 	 1. Verify that the generated file appears at Configuration-files menu
* 	 2. Verify that generated file does not contains password with long tab
* 	 3. Delete the created user
* 
* Results:
* 	 1. Generated file should be displayed
*    2. Only empty password should be displayed.
*    3. User should be deleted.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test140__empty_password_bug {
	
  private browserTypes  usedBrowser;
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test140__empty_password_bug(browserTypes browser) {
	  
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
  public void Empty_password_bug() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String usersNumber		= "1";
	String mac				= "";
	String regPrefix		= "empPssTst" + testFuncs.getId();
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  usersNumber); 

	// Login and create a registered user
	testFuncs.myDebugPrinting("Login and create a registered user");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()       	,
			 												 testVars.getPort()     	,
			 												 usersNumber				    ,
			 												 regPrefix					,
			 												 testVars.getDomain()       ,
			 												 "registered"               ,
			 												 testVars.getDefPhoneModel(),
			 												 testVars.getDefTenant()    ,
			 												 "myLocation");
    testFuncs.verifyPostUserCreate(driver, regPrefix, regPrefix, true);
    mac = testFuncs.readFile("mac_1.txt");
 	testFuncs.myDebugPrinting("device mac - " + mac, enumsClass.logModes.MINOR);
    
    // Generate the Device configuration file
 	testFuncs.myDebugPrinting("Generate the Device configuration file");
	searchAndSelectDevice(driver, regPrefix);
	generateDevice(regPrefix);
	
 	// Step 1 -  Verify that the generated file appears at Configuration-files menu
 	testFuncs.myDebugPrinting("Step 1 - Verify that the generated file appears at Configuration-files menu");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_CONF_FILES, "Manage Configuration Files");
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='searchInput']"), mac, 7000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='fbody']/tr[1]/td[3]/a[2]", mac);
	
 	// Step 2 - Verify that generated file does not contains password with long tab
 	testFuncs.myDebugPrinting("Step 2 - Verify that generated file does not contains password with long tab");
 	String currUrl  = driver.getCurrentUrl();
 	testFuncs.myDebugPrinting("currUrl - " + currUrl, enumsClass.logModes.MINOR);
    driver.get("https://" + testVars.getIp() + "//configfiles//" + mac + ".cfg");
 	testFuncs.myWait(5000);
	String bodyText     = driver.findElement(By.tagName("body")).getText();
	testFuncs.myAssertTrue("Password is still empty !!", !bodyText.contains("ems_server/user_password= "));
    driver.get(currUrl);
 	
	// Step 3 - Delete all created users
 	testFuncs.myDebugPrinting("Step 3 - Delete all created users");
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
  
  // Generate device configuration
  private void generateDevice(String deviceName) {
	  
	  // Generate device configuration
	  testFuncs.myDebugPrinting("Generate device configuration", enumsClass.logModes.NORMAL); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/a")			, 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[7]/a"), 7000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Generate IPP IP-Phones Configuration");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to generate the configuration files of the selected IP phones?");  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 4000); 
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Generate IPP IP-Phones Configuration");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Update devices now");  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[4]/div/button[1]"), 20000);  
	  
	  // Verify change
	  testFuncs.myDebugPrinting("Verify change", enumsClass.logModes.MINOR); 
	  testFuncs.searchStr(driver, deviceName + " (" + testFuncs.readFile("mac_1.txt") + ")");  
  }
  
  // Search for a device and select it via Select-All checkbox
  private void searchAndSelectDevice(WebDriver driver, String userName) {
	  
	  // Search device
	  testFuncs.myDebugPrinting("Search device", enumsClass.logModes.NORMAL);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input"), "user:" + userName.trim(), 5000);
	  driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).sendKeys(Keys.ENTER);	  
	  testFuncs.myWait(5000);
	  testFuncs.searchStr(driver, userName.trim()); 	    
	  testFuncs.myWait(5000);
	  
	  // Select the searched device via check Select-All check-box
	  testFuncs.myDebugPrinting("Select the searched device via check Select-All check-box", enumsClass.logModes.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='selectall']"), 3000);	  
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
