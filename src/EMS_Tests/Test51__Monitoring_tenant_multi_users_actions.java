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
* This test tests the multiple user actions via a Monitoring user (tenant)
* ----------------
* Tests:
* 	 - Login via Administrator, create a user with a POST query and logout
* 	 1. Verify that Change-tenant action is deactivated.
* 	 2. Verify that Reset-password action is deactivated.
* 	 3. Verify that Delete-users action is deactivated.
* 	 4. Verify that Restart-devices action is deactivated.
* 	 5. Verify that Generate-configuration-users action is deactivated.
* 	 6. Verify that Update-configuration-users action is deactivated.
*  	 7. Verify that Send-message action is enabled.
*    8. Verify that Add user-configuration is not enabled.
*    9. Verify that Add user-configuration is not enabled.
*   10. Logout. login as Administrator and delete the created user
* 
* Results:
*	 1-9. As described.
*	  10. User should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test51__Monitoring_tenant_multi_users_actions {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test51__Monitoring_tenant_multi_users_actions(browserTypes browser) {
	  
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
  public void Monitoring_system_multi_users_actions() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String srcUserName      = "monitMultiUsrs" + testFuncs.getId();
	Map<String, String> map = new HashMap<String, String>();
	map.put("startIdx"   ,  String.valueOf(1));
	map.put("usersNumber",  "1"); 
	  
	// Login via Administrator, create a user with a POST query and logout
	testFuncs.myDebugPrinting("Login via Administrator, create a user with a POST query and add a device-PH to it");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUsers(testVars.getIp(),
			  testVars.getPort() 	 	  ,
			  1							  ,	
			  srcUserName  		 		  ,
			  testVars.getDomain()		  ,
			  "registered"		  		  ,
			  testVars.getDefPhoneModel() ,
			  testVars.getDefTenant()     ,
			  testVars.getDefLocation());
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	 
    // Login via a Monitoring user (tenant) and enter Multiple-user-changes menu
	testFuncs.myDebugPrinting("Login via a Monitoring user (tenant) and enter Multiple-user-changes menu");
	testFuncs.login(driver, testVars.getMonitTenLoginData(enumsClass.loginData.USERNAME), testVars.getMonitTenLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
    
    // Step 1 - Verify that Change-tenant action is deactivated
  	testFuncs.myDebugPrinting("Step 1 - Verify that Change-tenant action is deactivated");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("resetTenant");
	testFuncs.myWait(2000);
	new Select(driver.findElement(By.xpath("//*[@id='branch']"))).selectByVisibleText(testVars.getDefTenant());
	testFuncs.myWait(2000);
	String changeTenantButton = driver.findElement(By.xpath("//*[@id='resetTenantTR']/td[1]/div/a[2]")).getAttribute("class");	
	testFuncs.myAssertTrue("Change Tenant button is activated !!", changeTenantButton.contains("not-active"));
	
    // Step 2 - Verify that Reset-password action is deactivated
  	testFuncs.myDebugPrinting("Step 2 - Verify that Reset-password action is deactivated");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("resetPasswords");
	testFuncs.myWait(2000);
	String resetPassButton = driver.findElement(By.xpath("//*[@id='resetPasswordsTR']/td[1]/div[1]/table/tbody/tr/td[4]/a")).getAttribute("class");	
	testFuncs.myAssertTrue("Reset password button is activated !!", resetPassButton.contains("not-active"));

    // Step 3 - Verify that Delete-users action is deactivated
  	testFuncs.myDebugPrinting("Step 3 - Verify that Delete-users action is deactivated");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("deleteUsers");
	testFuncs.myWait(2000);
	String deletePassButton = driver.findElement(By.xpath("//*[@id='deleteUsersTR']/td[1]/div/a")).getAttribute("class");	
	testFuncs.myAssertTrue("Delete users button is activated !!", deletePassButton.contains("not-active"));

    // Step 4 - Verify that Restart-devices button is deactivated.
   	testFuncs.myDebugPrinting("Step 4 - Verify that Restart-devices button is deactivated.");
   	String resModes[] = {"Graceful", "Force", "Scheduled"};
   	for (String resMode : resModes) {
   		
   		testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
   	    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
   		new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("resetIpPhones");
   		testFuncs.myWait(2000);
   		new Select(driver.findElement(By.xpath("//*[@id='resetIpPhonesTR']/td/div[1]/select"))).selectByValue(resMode);
   		testFuncs.myWait(2000);		
   		String resetDevicesButton = driver.findElement(By.xpath("//*[@id='resetIpPhonesTR']/td[1]/div[1]/a")).getAttribute("class");	
   		testFuncs.myAssertTrue("Reset devices button is activated !!", resetDevicesButton.contains("not-active"));
   	}
	
    // Step 5 - Verify that Generate-configuration-users action is deactivated
  	testFuncs.myDebugPrinting("Step 5 - Verify that Generate-configuration-users action is deactivated");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("setIpPhones");
	testFuncs.myWait(2000);
	String generateConfButton = driver.findElement(By.xpath("//*[@id='setIpPhonesTR']/td[1]/div/div/a")).getAttribute("class");	
   	testFuncs.myAssertTrue("Generate configuration button is activated !!", generateConfButton.contains("not-active"));
	
	// Step 6 - Verify that Update-configuration-users action is deactivated
  	testFuncs.myDebugPrinting("Step 6 - Verify that Update-configuration-users action is deactivated");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("updateConfigFiles");
	testFuncs.myWait(2000);
	String updateConfButton = driver.findElement(By.xpath("//*[@id='updateConfigFilesTR']/td[1]/div/a")).getAttribute("class");	
   	testFuncs.myAssertTrue("Update configuration button is activated !!", updateConfButton.contains("not-active"));
	
    // Step 7 - Verify that Send-message action is enabled
  	testFuncs.myDebugPrinting("Step 7 - Verify that Send-message action is enabled");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("sendMessage");
	testFuncs.myWait(2000);
	String sendMessageButton = driver.findElement(By.xpath("//*[@id='sendMessageTR']/td[1]/div/a")).getAttribute("class");	
   	testFuncs.myAssertTrue("Send message button is activated !!", sendMessageButton.contains("not-active"));
    
    // Step 8 - Verify that Add user-configuration is not enabled
  	testFuncs.myDebugPrinting("Step 8 - Verify that Add user-configuration is not enabled");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("personalInfo");
	testFuncs.myWait(2000);
	String addConfButton = driver.findElement(By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[3]/a")).getAttribute("class");	
   	testFuncs.myAssertTrue("Add configuratione button is activated !!", addConfButton.contains("not-active"));
	String featuresButton = driver.findElement(By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[1]/button")).getAttribute("class");	
   	testFuncs.myAssertTrue("Features button is activated !!", featuresButton.contains("not-active"));
	String actionsButton = driver.findElement(By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[2]/button")).getAttribute("class");	
   	testFuncs.myAssertTrue("Actions button is activated !!", actionsButton.contains("not-active")); 
	
    // Step 9 - Verify that Delete user-configuration is not enabled
  	testFuncs.myDebugPrinting("Step 9 - Verify that Delete user-configuration is not enabled");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("deletePersonalInfo");
	testFuncs.myWait(2000);
	String deleteConfButton = driver.findElement(By.xpath("//*[@id='deletePersonalInfoTR']/td[1]/div/a")).getAttribute("class");	
   	testFuncs.myAssertTrue("Delete configuration button is activated !!", deleteConfButton.contains("not-active")); 
  
 	// Step 10 - Logout. login as Administrator and delete the created user
 	testFuncs.myDebugPrinting("Step 10 - Logout. login as Administrator and delete the created user");
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
	testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	map.put("action"	      ,  "Delete Users");
	map.put("srcUsername"     ,  "Finished");
	map.put("usersPrefix"	  , srcUserName);
	map.put("skipVerifyDelete", "true");    
	map.put("startIdx"        ,  String.valueOf(1));
	map.put("usersNumber"     ,  "1");
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
