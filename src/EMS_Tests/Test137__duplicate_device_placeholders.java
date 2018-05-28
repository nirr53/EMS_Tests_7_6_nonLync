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
import EMS_Tests.enumsClass.browserTypes;

/**
* ----------------
* This test tests a create of duplicate device placeholder
* ----------------
* Tests:
* 	 - Create a registered user and enter Device placeholders menu
* 	 - Add a Device placeholder.
* 	 1. Try to create another device-ph with the same name.
* 	 2. Delete the already been created Device placeholder.
* 	 3. Delete the user that we created via the POST query.
* 
* Results:
* 	 1. Device placeholder should not be added successfully.
* 	 2. Device placeholder should be deleted successfully.
*	 3. User should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test137__duplicate_device_placeholders {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test137__duplicate_device_placeholders(browserTypes browser) {
	  
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
  public void Duplicate_device_placeholder() throws Exception {
	  
	  Log.startTestCase(this.getClass().getName());
	  
	  // Set variables
	  String srcUserName      =   "dvPH" + testFuncs.getId();
	  Map<String, String> map = new HashMap<String, String>();
	  map.put("startIdx"   ,  String.valueOf(1));
	  map.put("usersNumber",  "1"); 
	  
	  // Login, create a user with a POST query and add a device placeholder to it
	  testFuncs.myDebugPrinting("Login, create a user with a POST query and add a device placeholder to it");
	  testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	  testFuncs.createUserViaPost(testVars.getCrUserBatName(),
			  					  testVars.getIp()           ,
			  					  testVars.getPort()         ,
			  					  "1"		        		 ,
			  					  srcUserName  		         ,
			  					  testVars.getDomain()       ,
			  					  "registered"               ,
			  					  testVars.getDefPhoneModel(),
			  					  testVars.getDefTenant()    ,
				 				  "myLocation");
	  testFuncs.verifyPostUserCreate(driver, srcUserName + "@" + testVars.getDomain(), srcUserName, true);
	  testFuncs.enterMenu(driver, "Setup_Phone_conf_phone_device_placeholders", "Manage Devices Placeholders");
	  String phName  = "DayLightActivate";
	  String phValue = "18";
	  testFuncs.addDevicePlaceholder(driver, srcUserName, phName, phValue);
	   
	  // Step 1 - Add another device placeholder with the same name
	  testFuncs.myDebugPrinting("Step 1 - Add another device placeholder with the same name");
	  testFuncs.addDevicePlaceholder(driver, srcUserName, phName);
	  
	  // Step 2 - Delete the created device placeholder
	  testFuncs.myDebugPrinting("Step 2 - Delete the created device placeholder");
	  testFuncs.enterMenu(driver, "Setup_Phone_conf_phone_device_placeholders", "Manage Devices Placeholders");
	  deleteDevicePlaceholder(driver, srcUserName, phName, phValue);
	  
	  // Step 3 - Delete the created user
	  testFuncs.myDebugPrinting("Step 3 - Delete the created user");
	  testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
	  testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	  map.put("action"	 ,  "Delete Users");
	  map.put("srcUsername",  "Finished");
	  map.put("usersPrefix"	  , srcUserName);
	  map.put("skipVerifyDelete", "true");
	  testFuncs.setMultipleUsersAction(driver, map);
	  testFuncs.searchStr(driver, srcUserName.toLowerCase() + "@" + testVars.getDomain() + " Finished");
  }
  
  /**
  *  Delete an existing device placeholder
  *  @param driver     - A given driver
  *  @param userName   - A pre-create registered user
  *  @param phName     - An existing placeholder name for edit
  *  @param phNewValue - A new value for the placeholder
  */
  private void deleteDevicePlaceholder(WebDriver driver, String userName, String phName, String phNewValue) {
	  
	  // Delete the device Placeholder
	  testFuncs.myDebugPrinting("Delete the device Placeholder", enumsClass.logModes.MINOR);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[2]/form/div/input"), userName, 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[2]/form/div/span/button"), 5000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='placeholders_body']/tr/td[8]/a"), 5000);  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete item: " + phName);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete this value?");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);  
	
	  // Verify delete
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[2]/form/div/input"), userName, 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[2]/form/div/span/button"), 5000);
	  testFuncs.searchStr(driver, "There are no placeholders at present");
	  String bodyText = driver.findElement(By.tagName("body")).getText();
	  testFuncs.myAssertTrue("PH name is still detecetd !!\nbodyText - "  + bodyText, !bodyText.contains(phName));
	  testFuncs.myAssertTrue("PH value is still detected !!\nbodyText - " + bodyText, !bodyText.contains(phNewValue));  
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
