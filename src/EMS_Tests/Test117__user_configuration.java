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

/**
* ----------------
* This test tests the actions of user configuration menu.
* ----------------
*
* Tests:
* 	 - Login, create several users using POST query and enter the Generate-Configuration menu
* 	 1. Try to create a new configuration key without pressing the Save button
* 	 2. Try to create a new configuration key with pressing the Save button
*    3. Delete configuration key
*    4. Delete the created users
* 
* Results:
* 	 1. Configuration key should not be added successfully.
* 	 2. Configuration key should be added successfully.
* 	 3. Configuration key should be deleted successfully.
* 	 4. Users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test117__user_configuration {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test117__user_configuration(String browser) {
	  
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
  public void User_configuration_actions() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set vars
	String usersNumber   	= "3";
	int usStartIdx 		 	= 1;
	String Id 				= testFuncs.getId();
	String dispPrefix   	= ("userConfActions" + Id).toLowerCase();
	String confPrefix		= "confName";
	String confName 		= confPrefix        + Id;
	String confValue 		= "confValue"       + Id;
    Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  usersNumber); 
    map.put("startIdx"   ,  String.valueOf(usStartIdx));
    map.put("srcUsername",  "Finished");  
	
    // Login, create several users using POST query and enter the Generate-Configuration menu
	testFuncs.myDebugPrinting("Login, create several users using POST query and enter the Generate-Configuration menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()           ,
			 testVars.getPort()         ,
			 usersNumber		        ,
			 dispPrefix  		        ,
			 testVars.getDomain()       ,
			 "registered"               ,
			 testVars.getDefPhoneModel(),
			 testVars.getDefTenant()    ,
			 "myLocation");
	testFuncs.verifyPostUsersCreate(driver,  dispPrefix,  dispPrefix, true, Integer.valueOf(usersNumber));	
	testFuncs.enterMenu(driver, "Setup_user_configuration", "Manage Multiple Users - User Configuration");

    // Step 1 - Try to create a new configuration key without pressing the Save button
	testFuncs.myDebugPrinting("Step 1 - Try to create a new configuration key without pressing the Save button");
	testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
	createNewConfValue(driver, confName, confValue);
	String usersFullNames[] = {dispPrefix + "_1@" + testVars.getDomain(),
							   dispPrefix + "_2@" + testVars.getDomain(),
							   dispPrefix + "_3@" + testVars.getDomain()};
	String confNames[] =  {confName,  confName , confName};
	String confValues[] = {confValue, confValue, confValue};
	verifyConfValues(driver, usersFullNames, confNames, confValues, false);
	
    // Step 2 - Try to create a new configuration key with pressing the Save button
	testFuncs.myDebugPrinting("Step 2 - Try to create a new configuration key with pressing the Save button");
	testFuncs.enterMenu(driver, "Setup_user_configuration", "Manage Multiple Users - User Configuration");
	testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
	createNewConfValue(driver, confName, confValue);
	saveConfValues(driver, usersFullNames);
	verifyConfValues(driver, usersFullNames, confNames, confValues, true);
	
    // Step 3 - Delete configuration key
	testFuncs.myDebugPrinting("Step 3 - Delete configuration key");
	testFuncs.enterMenu(driver, "Setup_user_configuration", "Manage Multiple Users - User Configuration");
	testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
	deleteConfValue(driver, usersFullNames, confNames, confValues);
	
    // Step 4 - Delete the created users
  	testFuncs.myDebugPrinting("Step 4 - Delete the created users");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("usersPrefix"	  , dispPrefix + "_");
    map.put("usersNumber"	  , usersNumber); 
    map.put("startIdx"   	  , String.valueOf(usStartIdx));
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    testFuncs.searchStr(driver, dispPrefix + "_1@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, dispPrefix + "_2@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, dispPrefix + "_3@" + testVars.getDomain() + " Finished");
  }

  // Verify the create of configuration values
  private void verifyConfValues(WebDriver driver, String[] usersFullNames, String[] confNames, String[] confValues, Boolean isValuesExist) {
	    
	  int usersNUmber = usersFullNames.length;
	  
	  // Loop on all users and verify their create one after another
	  testFuncs.myDebugPrinting("Loop on all users and verify their create one after another", testVars.logerVars.MINOR);	
	  for (int i = 0; i < usersNUmber; ++i) {
		  
		  testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
		  testFuncs.searchUser(driver, usersFullNames[i]);  
		  
		  if (isValuesExist) {
			  
			  testFuncs.myDebugPrinting("isValuesExist - TRUE", testVars.logerVars.MINOR);
			  testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[1]/td[4]/a/i"), 3000);		  		    
		  } else {
			  
			  testFuncs.myDebugPrinting("isValuesExist - FALSE", testVars.logerVars.MINOR);
			  testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[1]/td[9]/a[2]")			 , 3000);	  
			  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[1]/a"), 3000);	
		  }
		  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div[2]/div/div[1]", "User configuration (" + usersFullNames[i] + ")");
		  
		  // Verify delete
		  testFuncs.myDebugPrinting("Verify delete", testVars.logerVars.MINOR);
		  if (isValuesExist) {
			  
			  testFuncs.myDebugPrinting("isValuesExist - TRUE", testVars.logerVars.MINOR);	  
			  testFuncs.verifyStrByXpath(driver, "//*[@id='table_keys']/tbody/tr/td[1]", confNames[i]);
			  testFuncs.verifyStrByXpath(driver, "//*[@id='table_keys']/tbody/tr/td[2]", confValues[i]);
			  
		  } else {
		  
			  testFuncs.myDebugPrinting("isValuesExist - FALSE", testVars.logerVars.MINOR);
			  String txt = driver.findElement(By.tagName("body")).getText();
			  testFuncs.myAssertTrue("Values was not detcted !!", !txt.contains(confNames[i]) && !txt.contains(confValues[i]));  
		  }		  
		  testFuncs.myDebugPrinting("Delete succeded !!", testVars.logerVars.MINOR);
	  }

  }

  // Delete an existing configuration value according to given data
  private void deleteConfValue(WebDriver driver, String[] usersFullNames, String[] confNames, String[] confValues) {
	  	  
	  new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByVisibleText("Delete User configuration");
	  testFuncs.myWait(3000);	
	  testFuncs.myClick(driver, By.xpath("//*[@id='deletePersonalInfoTR']/td[1]/div/a"), 5000);		  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete User configuration");			  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete User configuration to selected user(s) ?"); 
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);		  
			  
	  // Empty confNames and confValues
	  verifyConfValues(driver, usersFullNames, confNames, confValues, false);
  }
 
  // Save configuration values
  private void saveConfValues(WebDriver driver, String [] usersFullNames) {
	
	  // Save configuration
	  testFuncs.myDebugPrinting("Save configuration", testVars.logerVars.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[4]/a"), 7000);
	  
	  // Check Confirm-Box
	  testFuncs.myDebugPrinting("Check Confirm-Box", testVars.logerVars.MINOR);
	  int usersNum = usersFullNames.length;
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"							 , "Save Configuration");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/thead/tr/th[1]", "Name");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/thead/tr/th[2]", "Result");
	  for (int i = 1; i <= usersNum; ++i) {
		  
		  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/tbody/tr[" + i + "]/td[1]", usersFullNames[i-1]);
		  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/tbody/tr[" + i + "]/td[2]", "User configuration was saved successfully");
	  }
	
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);	  
  }

  // Add new configuration key according to given data
  private void createNewConfValue(WebDriver driver, String confValName, String confValValue) {
	
	  // Select 'generate configuration' action, set data and submit	
	  testFuncs.myDebugPrinting("Select 'generate configuration' action, set data and submit", testVars.logerVars.MINOR);
	  new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByVisibleText("User configuration");
	  testFuncs.myWait(3000);  
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_name']") , confValName , 2000);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ini_value']"), confValValue, 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[3]/a/span"), 3000);
	
	  // Verify create
	  testFuncs.myDebugPrinting("Verify create", testVars.logerVars.MINOR);
	  testFuncs.searchStr(driver, confValName);
	  testFuncs.searchStr(driver, confValValue);  
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
