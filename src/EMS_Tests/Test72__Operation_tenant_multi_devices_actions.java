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
* This test tests the multiple device actions via an Operation user (tenant)
* ----------------
* Tests:
*    - Login via Administrator, create a user with a POST query and logout
* 	 - Re-login via an Operation user (tenant) and enter Manage multiple devices changes menu.
*    1.   Verify that Change-phone-type button is active.
*    2.   Verify that Change-language action is active.
* 	 3.   Verify that Restart-devices action is active.
* 	 4.   Verify that Generate-configuration-users button is active.
* 	 5.   Verify that Update-configuration-users button is active.
*    6.   Verify that Send-message action is active.
*    7.   Verify that Change-firmware action is active.
*    8.   Verify that Change-VLAN-mode action is active.
*    9.   Verify that Delete-devices button is active.
*    10.  Logout, re-login as Administrator and delete the created user
*
* Results:
*	 1-9.  In all actions, in all modes, the selected button should be active.
*	  10.  Logout, re-login as Administrator and delete the created user
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test72__Operation_tenant_multi_devices_actions {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test72__Operation_tenant_multi_devices_actions(browserTypes browser) {
	  
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
  public void Operation_tenant_multi_devices_actions() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String srcUserName      = "opTnMltDv" + testFuncs.getId();
	String tempName			=  "NirTemplate420";      // name of default template which associated with the current Operation user
	
	// Login via Administrator, create a user with a POST query and logout
	testFuncs.myDebugPrinting("Login via Administrator, create a user with a POST query and add a device-PH to it");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
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
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
		 
    // Re-login via an Operation user (tenant) and enter Manage multiple devices changes menu.
	testFuncs.myDebugPrinting("Re-login via an Operation user (system) and enter Manage multiple devices changes menu.");
	testFuncs.login(driver, testVars.getOperTenLoginData(enumsClass.loginData.USERNAME), testVars.getOperTenLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
   
    // Step 1 - Verify that Change-IP-phone-type action is active.
  	testFuncs.myDebugPrinting("Step 1 - Verify that Change-IP-phone-type action is active.");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("changeType");
    testFuncs.myWait(2000);
	new Select(driver.findElement(By.xpath("//*[@id='ipptype']"))).selectByVisibleText(tempName);
    testFuncs.myWait(2000); 
    testFuncs.myClick(driver, By.xpath("//*[@id='changeTypeTR']/td/div[1]/a[2]"), 5000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Change Template");
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to change the Template of the selected devices?");
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
    testFuncs.searchStr(driver, srcUserName + "@" + testVars.getDomain() + " " + testFuncs.readFile("mac_1.txt"));
	
	// Step 2 - Verify that Change-language action is active.
	testFuncs.myDebugPrinting("Step 2 - Verify that Change-language action is active.");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("changeLanguage");
    testFuncs.myWait(2000);
	new Select(driver.findElement(By.xpath("//*[@id='deviceLanguage']"))).selectByVisibleText("English");
    testFuncs.myWait(2000);  
    testFuncs.myClick(driver, By.xpath("//*[@id='changeLanguageTR']/td/div[1]/a[2]"), 5000);
   	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Change Language");
   	testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to change the device's language?");
   	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
    testFuncs.searchStr(driver, srcUserName + "@" + testVars.getDomain() + " " + testFuncs.readFile("mac_1.txt"));
	
    // Step 3 - Verify that Restart-devices action is active.
   	testFuncs.myDebugPrinting("Step 3 - Verify that Restart-devices action is active");
	testFuncs.myWait(2000);
	String resModes[] = {"Graceful", "Force", "Scheduled"};
	for (String resMode : resModes) {
		
		testFuncs.myDebugPrinting("resMode - " + resMode, enumsClass.logModes.NORMAL);
		testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
	    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
		new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("resetIpPhones");
	    testFuncs.myWait(2000);
		new Select(driver.findElement(By.xpath("//*[@id='resetIpPhonesTR']/td/div[1]/select"))).selectByVisibleText(resMode);
	    testFuncs.myWait(2000);
	    testFuncs.myClick(driver, By.xpath("//*[@id='resetIpPhonesTR']/td/div[1]/a"), 5000);
	   	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Restart Devices");
	   	testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Note: restart command will work only on supported IP Phones.\nAre you sure you want to restart the selected IP Phones?");
	   	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
	    testFuncs.searchStr(driver, srcUserName + "@" + testVars.getDomain() + " " + testFuncs.readFile("mac_1.txt"));
	}
   	
    // Step 4 - Verify that Generate-configuration-users action is active.
  	testFuncs.myDebugPrinting("Step 4 - Verify that Generate-configuration-users action is active.");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("setIpPhones");
    testFuncs.myWait(2000);
    testFuncs.myClick(driver, By.xpath("//*[@id='setIpPhonesTR']/td/div/div/a"), 5000);
   	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Generate IP Phones Configuration Files");
   	testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "The configuration files will be generated to the location defined in the template (destinationDir).\nDo you want to continue?");
   	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
    testFuncs.searchStr(driver, srcUserName + "@" + testVars.getDomain() + " " + testFuncs.readFile("mac_1.txt"));

    // Step 5 - Verify that Update-configuration-users action is active.
   	testFuncs.myDebugPrinting("Step 5 - Verify that Update-configuration-users action is active.");
 	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
 	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("updateConfigFiles");
    testFuncs.myWait(2000);
    testFuncs.myClick(driver, By.xpath("//*[@id='updateConfigFilesTR']/td/div/a"), 5000);
    testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update Configuration file");
    testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Note: update configuration command will work only on supported IP Phones.\nAre you sure you want to update the selected IP Phones files?");
    testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
    testFuncs.searchStr(driver, srcUserName + "@" + testVars.getDomain() + " " + testFuncs.readFile("mac_1.txt"));
  
    // Step 6 - Verify that Send-message action is active.
  	testFuncs.myDebugPrinting("Step 6 - Verify that Send-message action is active.");
 	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
 	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("sendMessage");
    testFuncs.myWait(2000);
    testFuncs.mySendKeys(driver, By.xpath("//*[@id='sendMessageTR']/td/div/input"), "message", 3000);
    testFuncs.myClick(driver, By.xpath("//*[@id='sendMessageTR']/td/div/a"), 5000);
    testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Send Message");
    testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to send message to the selected devices?");
    testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
    testFuncs.searchStr(driver, srcUserName + "@" + testVars.getDomain() + " " + testFuncs.readFile("mac_1.txt"));

	// Step 7 - Verify that Change-firmware action is active.
	testFuncs.myDebugPrinting("Step 7 - Verify that Change-firmware action is active.");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
 	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("updateFirmware");
    testFuncs.myWait(2000);
 	new Select(driver.findElement(By.xpath("//*[@id='firmware_id']"))).selectByVisibleText("430HD");
    testFuncs.myWait(2000);
    testFuncs.myClick(driver, By.xpath("//*[@id='updateFirmwareTR']/td/div[1]/a[2]"), 5000);
    testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Change Firmware");
    testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to change the device's firmware?");
    testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
    testFuncs.searchStr(driver, srcUserName + "@" + testVars.getDomain() + " " + testFuncs.readFile("mac_1.txt"));

	// Step 8 - Verify that Change-VLAN action is active.
	testFuncs.myDebugPrinting("Step 8 - Verify that Change-VLAN action is active");
	String vlanModes[] = {"Disabled", "Manual Configuration", "Automatic - CDP", "Automatic - LLDP", "Automatic - CDP LLDP"};
	for (String vlanMode : vlanModes) {

		testFuncs.myDebugPrinting("vlanMode - " + vlanMode, enumsClass.logModes.NORMAL);
		testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
	    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
	 	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("changeVlan");
	    testFuncs.myWait(2000);
	 	new Select(driver.findElement(By.xpath("//*[@id='changeVlanTR']/td/div[1]/select"))).selectByVisibleText(vlanMode);
	    testFuncs.myWait(2000);
	    testFuncs.myClick(driver, By.xpath("//*[@id='changeVlanTR']/td/div[1]/a[2]"), 5000);
	    testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Change VLAN Discovery Mode");
	    testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to change the device's VLAN discovery mode?");
	    testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
	    testFuncs.searchStr(driver, srcUserName + "@" + testVars.getDomain() + " " + testFuncs.readFile("mac_1.txt"));
	}
	
	// Step 9 - Verify that Delete-devices action is active.
	testFuncs.myDebugPrinting("Step 9 - Verify that Delete-devices action is active.");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_DEVICES, "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
 	new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByValue("deleteDevices");
    testFuncs.myWait(2000);
    testFuncs.myClick(driver, By.xpath("//*[@id='deleteDevicesTR']/td/div/a"), 5000);
    testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete Devices");
    testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the selected devices?");
    testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);
    testFuncs.searchStr(driver, srcUserName + "@" + testVars.getDomain() + " " + testFuncs.readFile("mac_1.txt"));
  
    // Step 10 - Logout, re-login as Administrator and delete the created user
  	testFuncs.myDebugPrinting("Step 10 - Logout, re-login as Administrator and delete the created user");
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
  	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
  	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, srcUserName, "1");
    Map<String, String> map = new HashMap<String, String>();
    map.put("usersPrefix"	  , srcUserName + "_");
    map.put("usersNumber"	  , "1"); 
    map.put("startIdx"   	  , String.valueOf("1"));
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    srcUserName = srcUserName.toLowerCase();
    testFuncs.searchStr(driver, srcUserName + "@" + testVars.getDomain() + " Finished");    
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
