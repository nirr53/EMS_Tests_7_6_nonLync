package EMS_Tests;

import java.util.Arrays;
import java.util.Collection;
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
* This test tests the multiple user actions via an Operation user
* ----------------
* Tests:
*    - Login via Administrator, create a user with a POST query and logout
* 	 - Re-login via an Operation user (system) and enter Manage multiple users changes menu.
* 	 1. Verify that Change-region action is active.
* 	 2. Verify that Reset-password action is active.
* 	 3. Verify that Restart-devices action is active.
* 	 4. Verify that Generate-configuration-users action is active.
* 	 5. Verify that Update-configuration-users action is active.
*  	 6. Verify that Send-message action is active.
*  	 7. Verify that Delete-devices action is active.
* 
* Results:
*	 1-7. In all actions, in all modes, the selected button should be active.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test60__Operation_system_multi_users_actions {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test60__Operation_system_multi_users_actions(browserTypes browser) {
	  
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
  public void Opeartion_system_multi_users_actions() throws Exception {
	  	
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String srcUserName      = "oprsysmlusr" + testFuncs.getId();
	
	// Login via Administrator, create a user with a POST query and logout
	testFuncs.myDebugPrinting("Login via Administrator, create a user with a POST query and add a device-PH to it");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(),
			  					testVars.getIp()           ,
			  					testVars.getPort()         ,
			  					"1"		        		   ,
			  					srcUserName  		       ,
			  					testVars.getDomain()       ,
			  					"registered"               ,
			  					testVars.getDefPhoneModel(),
			  					testVars.getDefTenant()    ,
				 				"myLocation");
	testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
		 
    // Re-login via an Operation user (system) and enter Manage multiple devices changes menu.
	testFuncs.myDebugPrinting("Re-login via an Operation user (system) and enter Manage multiple devices changes menu.");
	testFuncs.login(driver, testVars.getOperUsername(), testVars.getOperPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");

    // Step 1 - Verify that Change-tenant action is active.
  	testFuncs.myDebugPrinting("Step 1 - Verify that Change-tenant action is active.");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
  	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("resetTenant");
    testFuncs.myWait(2000);
  	new Select(driver.findElement(By.xpath("//*[@id='branch']"))).selectByVisibleText(testVars.getDefTenant());
    testFuncs.myWait(2000);  
    testFuncs.myClick(driver, By.xpath("//*[@id='resetTenantTR']/td/div/a[2]"), 5000);
   	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Set Users Tenant");
   	testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to change the tenant to selected user(s) ?");
   	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
    testFuncs.searchStr(driver, srcUserName + "@" + testVars.getDomain());
    
    // Step 2 - Verify that Reset-password action is active.
  	testFuncs.myDebugPrinting("Step 2 - Verify that Reset-password action is active.");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
  	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("resetPasswords");
    testFuncs.myWait(2000);
    testFuncs.myClick(driver, By.xpath("//*[@id='resetPasswordsTR']/td/div[1]/table/tbody/tr/td[4]/a"), 5000);
   	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Reset Users Passwords");
   	testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to change the password to selected user(s) ?");
   	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
    testFuncs.searchStr(driver, srcUserName + "@" + testVars.getDomain());
    
    // Step 3 - Verify that Restart-devices action is active.
 	testFuncs.myDebugPrinting("Step 3 - Verify that Restart-devices action is active."); 	
 	String resModes[] = {"Graceful", "Force", "Scheduled"};
 	for (String resMode : resModes) {
 		
 	 	testFuncs.myDebugPrinting("resMode - " + resMode, enumsClass.logModes.NORMAL);
	 	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
	    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	  	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("resetIpPhones");
	    testFuncs.myWait(2000);
	  	new Select(driver.findElement(By.xpath("//*[@id='resetIpPhonesTR']/td/div[1]/select"))).selectByVisibleText(resMode);
	    testFuncs.myWait(2000);    
	    testFuncs.myClick(driver, By.xpath("//*[@id='resetIpPhonesTR']/td/div[1]/a"), 5000);
		testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Restart Devices");
		testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Note: restart command will work only on supported IP Phones.\nAre you sure you want to restart the selected IP Phones?");
		testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
	    testFuncs.searchStr(driver, srcUserName + "@" + testVars.getDomain());
 	}
 	
    // Step 4 - Verify that Generate-configuration-users action is active.
   	testFuncs.myDebugPrinting("Step 4 - Verify that Generate-configuration-users action is active.");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
  	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("setIpPhones");
    testFuncs.myWait(2000);
    testFuncs.myClick(driver, By.xpath("//*[@id='setIpPhonesTR']/td/div/div/a"), 5000);
   	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Generate IP Phones Configuration Files");
   	testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "The configuration files will be generated to the location defined in the template (destinationDir)..\nDo you want to continue?");
   	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
    testFuncs.searchStr(driver, srcUserName + "@" + testVars.getDomain());
 	
    // Step 5 - Verify that Update-configuration-users action is active.
   	testFuncs.myDebugPrinting("Step 5 - Verify that Update-configuration-users action is active.");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
  	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("updateConfigFiles");
    testFuncs.myWait(2000);
    testFuncs.myClick(driver, By.xpath("//*[@id='updateConfigFilesTR']/td/div/a"), 5000);
   	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update Configuration Files");
   	testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Note: update configuration command will work only on supported IP Phones.\nAre you sure you want to update the selected IP Phones files?");
   	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
    testFuncs.searchStr(driver, srcUserName + "@" + testVars.getDomain());
 
    // Step 6 - Verify that Send-message action is active.
	testFuncs.myDebugPrinting("Step 6 - Verify that Send-message action is active.");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
  	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("sendMessage");
    testFuncs.myWait(2000);  
    testFuncs.mySendKeys(driver, By.xpath("//*[@id='msgText']"), "message", 3000);
    testFuncs.myClick(driver, By.xpath("//*[@id='sendMessageTR']/td/div/a"), 5000);
   	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Send Message");
   	testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to send the message to the selected user(s) ?");
   	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
    testFuncs.searchStr(driver, srcUserName + "@" + testVars.getDomain());

    // Step 7 - Verify that Delete-users action is active.
  	testFuncs.myDebugPrinting("Step 7 - Verify that Delete-users action is active.");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
  	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("deleteUsers");
    testFuncs.myWait(2000);  
    testFuncs.myClick(driver, By.xpath("//*[@id='deleteUsersTR']/td/div/a"), 5000);
   	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete Users");
   	testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the selected users?");
   	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
    testFuncs.searchStr(driver, srcUserName + "@" + testVars.getDomain());
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
