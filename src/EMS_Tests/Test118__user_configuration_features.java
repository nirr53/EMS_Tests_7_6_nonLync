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
* This test tests the features of user configuration menu.
* ----------------
*
* Tests:
* 	 - Login, create several users using POST query and enter the Generate-Configuration menu
* 	 1. Add Day Light saving value
*    2. Add Telnet value
*    3. Add PIN Access value
*    4. Add CAP profile value
*    5  Add & delete VOCA value
*    6. Delete all values
* 
* Results:
* 	 1+5. Values should be added successfully.
* 	   6. Configuration values should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test118__user_configuration_features {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test118__user_configuration_features(browserTypes browser) {
	  
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
	
	// Set variables
	String usersNumber   	= "1";
	int usStartIdx 		 	= 1;
	String Id 				= testFuncs.getId();
	String dispPrefix   	= ("userFeatures" + Id).toLowerCase();
    Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  usersNumber); 
    map.put("startIdx"   ,  String.valueOf(usStartIdx));
    map.put("srcUsername",  "Finished");  
	
    // Login, create several users using POST query and enter the Generate-Configuration menu
	testFuncs.myDebugPrinting("Login, create several users using POST query and enter the Generate-Configuration menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()           ,
			 testVars.getPort()         ,
			 usersNumber		        ,
			 dispPrefix  		        ,
			 testVars.getDomain()       ,
			 "registered"               ,
			 testVars.getDefPhoneModel(),
			 testVars.getDefTenant()    ,
			 "myLocation");
	testFuncs.verifyPostUserCreate(driver,  dispPrefix,  dispPrefix, true); 
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_USER_CONFIGURATION, "Manage Multiple Users - User Configuration");
	
    // Step 1 - Add Day Light saving value
	testFuncs.myDebugPrinting("Step 1 - Add Day Light saving value");
	addDaylightValues(driver);
	
    // Step 2 - Add Telnet value
	testFuncs.myDebugPrinting("Step 2 - Add Telnet value");	
	addTelnetValue(driver, true);
	deleteAllValues(driver);
	addTelnetValue(driver, false);
	
    // Step 3 - Add PIN Access value
	testFuncs.myDebugPrinting("Step 3 - Add PIN Access value");
	addPINAccessValue(driver, true);
	deleteAllValues(driver);
	addPINAccessValue(driver, false);
	deleteAllValues(driver);
	
    // Step 4 - Add CAP profile value
	testFuncs.myDebugPrinting("Step 4 - Add CAP profile value");
	addCAPProfile(driver, true);
	deleteAllValues(driver);
	addCAPProfile(driver, false);	
	
    // Step 5 - Add VOCA value
	// Nir 10/6/18 - VI 542
	testFuncs.myDebugPrinting("Step 5 - Add VOCA value");
	String vocaLabel  = "label" + Id;
	String vocaNumber = Id;
	String vocaIp     = testFuncs.getRandomIp();
	String vocaPort   = testFuncs.getRandomPort();
	addVOCA(vocaLabel, vocaNumber, enumsClass.vocaModes.TCP, true,  vocaIp, vocaPort);
	addVOCA(vocaLabel, vocaNumber, enumsClass.vocaModes.TLS, false, vocaIp, vocaPort);
	addVOCA(vocaLabel, vocaNumber, enumsClass.vocaModes.UDP, true,  vocaIp, vocaPort);
	deleteVOCA(vocaLabel);	
	
    // Step 6 - Delete all values
	testFuncs.myDebugPrinting("Step 6 - Delete all values");
	deleteAllValues(driver);
  }
 
  // Delete a VOCA values
  private void deleteVOCA(String vocaLabel) {
	  
	  testFuncs.myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[1]/button"), 3000);		
	  testFuncs.myClick(driver, By.xpath("//*[@id='voca']")											    , 3000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']", "VOCA");	  

	  // Delete VOCA data
	  testFuncs.myDebugPrinting("Delete VOCA data", enumsClass.logModes.MINOR);			
	  testFuncs.myClick(driver, By.xpath("//*[@id='modalContentId']/div/label[2]/input"), 3000);	  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "VOCA");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete?");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	  
	  // Verify delete
	  testFuncs.myDebugPrinting("Verify delete", enumsClass.logModes.MINOR);
	  String txt = driver.findElement(By.tagName("body")).getText();
	  testFuncs.myAssertTrue("VOCA value still detected !! <" + txt + ">", !txt.contains(vocaLabel));  
  }

  // Create a VOCA values by given data 
  private void addVOCA(String vocaLabel, String vocaNumber, vocaModes mode, Boolean isEnableVocserver, String vocaIp, String vocaPort) {
	  
	  testFuncs.myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[1]/button"), 3000);		
	  testFuncs.myClick(driver, By.xpath("//*[@id='voca']")											    , 3000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']", "VOCA");	  
	  
	  // Fill VOCA data
	  testFuncs.myDebugPrinting("Fill VOCA data", enumsClass.logModes.MINOR);			
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='voip_services_vocanom_label']") , vocaLabel , 3000);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='voip_services_vocanom_number']"), vocaNumber, 3000);
	  testFuncs.mySelect(driver,
					   	 By.xpath("//*[@id='voip_services_vocanom_transport_mode']"),
					     enumsClass.selectTypes.GIVEN_TEXT,
					     mode.toString(),
					     3000);
	  
	  if (( driver.findElement(By.xpath("//*[@id='voip_services_vocanom_server_enabled']")).isSelected() && !isEnableVocserver) ||
		  (!driver.findElement(By.xpath("//*[@id='voip_services_vocanom_server_enabled']")).isSelected() &&  isEnableVocserver)) {
		  
		  testFuncs.myClick(driver, By.xpath("//*[@id='voip_services_vocanom_server_enabled']"), 3000);
	  }
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='voip_services_vocanom_server_ip_address']"), vocaIp , 3000);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='voip_services_vocanom_server_port']")      , vocaPort, 3000);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]")	   			         , 10000);
	  
	  // Verify create
	  testFuncs.myDebugPrinting("Verify create", enumsClass.logModes.MINOR);
	  String isEnableVocserverStr = (isEnableVocserver) ? "1" : "0";
	  testFuncs.searchStr(driver, "voip_services_vocanom_label "  			 + vocaLabel);
	  testFuncs.searchStr(driver, "voip_services_vocanom_number " 			 + vocaNumber);	
	  testFuncs.searchStr(driver, "voip_services_vocanom_transport_mode " 	 + mode.toString());	  
	  testFuncs.searchStr(driver, "voip_services_vocanom_server_enabled "    + isEnableVocserverStr);
	  testFuncs.searchStr(driver, "voip_services_vocanom_server_ip_address " + vocaIp);
	  testFuncs.searchStr(driver, "voip_services_vocanom_server_port "       + vocaPort);
  }
  
  // Add CAP profile
  private void addCAPProfile(WebDriver driver, boolean isSelectAll) {
	  
	  testFuncs.myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[1]/button"), 3000);		
	  testFuncs.myClick(driver, By.xpath("//*[@id='CAP Profile']")										, 3000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']", "CAP Profile");
	  
	  if (isSelectAll) {
		  
		  if ( driver.findElement(By.xpath("//*[@id='voip_common_area_is_cap_device']")).isSelected()) {	  
			  testFuncs.myDebugPrinting("Web sign-in will use the CAP web sign-in method is checked !!", enumsClass.logModes.MINOR);			
		  } else {	  
			  testFuncs.myClick(driver, By.xpath("//*[@id='voip_common_area_is_cap_device']"), 1000);		  	  
		  }
		  if ( driver.findElement(By.xpath("//*[@id='lync_corporate_directory_enabled']")).isSelected()) {	  
			  testFuncs.myDebugPrinting("Enable Corporate Directory is checked !!", enumsClass.logModes.MINOR);			
		  } else {	  
			  testFuncs.myClick(driver, By.xpath("//*[@id='lync_corporate_directory_enabled']"), 1000);		  	  
		  }
		  if ( driver.findElement(By.xpath("//*[@id='lync_userSetting_prevent_user_sign_out']")).isSelected()) {	  
			  testFuncs.myDebugPrinting("No sign out option from status screen is checked !!", enumsClass.logModes.MINOR);			
		  } else {	  
			  testFuncs.myClick(driver, By.xpath("//*[@id='lync_userSetting_prevent_user_sign_out']"), 1000);		  	  
		  }
		  testFuncs.myClick(driver, By.xpath("//*[@id='lync_calendar_enabled']") 			   , 1000);		  
		  testFuncs.myClick(driver, By.xpath("//*[@id='lync_VoiceMail_enabled']")			   , 1000);			  
		  testFuncs.myClick(driver, By.xpath("//*[@id='lync_BToE_enable']")	     			   , 1000);		
		  testFuncs.myClick(driver, By.xpath("//*[@id='voip_line_0_call_forward_enabled']")	   , 1000);		
		  testFuncs.myClick(driver, By.xpath("//*[@id='voip_services_do_not_disturb_enabled']"), 1000);		
		  testFuncs.myClick(driver, By.xpath("//*[@id='system_pin_lock_enabled']")	     	   , 1000);		
		  testFuncs.myClick(driver, By.xpath("//*[@id='system_enable_key_configuration']")	   , 1000);	
		  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]")	   			   , 1000);
	  
		  // Verify create
		  testFuncs.myDebugPrinting("Verify create", enumsClass.logModes.MINOR);			
		  testFuncs.searchStr(driver, "voip_common_area_is_cap_device 1");
		  testFuncs.searchStr(driver, "lync_corporate_directory_enabled 1");
		  testFuncs.searchStr(driver, "lync_userSetting_prevent_user_sign_out 1");		
		  testFuncs.searchStr(driver, "lync_calendar_enabled 1");
		  testFuncs.searchStr(driver, "lync_VoiceMail_enabled 1");
		  testFuncs.searchStr(driver, "lync_BToE_enable 1");
		  testFuncs.searchStr(driver, "voip_line_0_call_forward_enabled 1");			
		  testFuncs.searchStr(driver, "voip_services_do_not_disturb_enabled 1");
		  testFuncs.searchStr(driver, "system_pin_lock_enabled 1");			
		  testFuncs.searchStr(driver, "system_enable_key_configuration 1");  
	  } else {
		  
		  if (!driver.findElement(By.xpath("//*[@id='voip_common_area_is_cap_device']")).isSelected()) {	  
			  testFuncs.myDebugPrinting("Web sign-in will use the CAP web sign-in method is not checked !!", enumsClass.logModes.MINOR);			
		  } else {	  
			  testFuncs.myClick(driver, By.xpath("//*[@id='voip_common_area_is_cap_device']"), 1000);		  	  
		  }
		  if (!driver.findElement(By.xpath("//*[@id='lync_corporate_directory_enabled']")).isSelected()) {	  
			  testFuncs.myDebugPrinting("Enable Corporate Directory is not checked !!", enumsClass.logModes.MINOR);			
		  } else {	  
			  testFuncs.myClick(driver, By.xpath("//*[@id='lync_corporate_directory_enabled']"), 1000);		  	  
		  }
		  if (!driver.findElement(By.xpath("//*[@id='lync_userSetting_prevent_user_sign_out']")).isSelected()) {	  
			  testFuncs.myDebugPrinting("No sign out option from status screen is not checked !!", enumsClass.logModes.MINOR);			
		  } else {	  
			  testFuncs.myClick(driver, By.xpath("//*[@id='lync_userSetting_prevent_user_sign_out']"), 1000);		  	  
		  }
		  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]")	   			   , 1000);
	  
		  // Verify create
		  testFuncs.myDebugPrinting("Verify create", enumsClass.logModes.MINOR);			
		  testFuncs.searchStr(driver, "voip_common_area_is_cap_device 0");
		  testFuncs.searchStr(driver, "lync_corporate_directory_enabled 0");	
		  testFuncs.searchStr(driver, "lync_userSetting_prevent_user_sign_out 0");		
		  testFuncs.searchStr(driver, "lync_calendar_enabled 0");
		  testFuncs.searchStr(driver, "lync_VoiceMail_enabled 0");
		  testFuncs.searchStr(driver, "lync_BToE_enable 0");		
		  testFuncs.searchStr(driver, "voip_line_0_call_forward_enabled 0");			
		  testFuncs.searchStr(driver, "voip_services_do_not_disturb_enabled 0");
		  testFuncs.searchStr(driver, "system_pin_lock_enabled 0");			
		  testFuncs.searchStr(driver, "system_enable_key_configuration 0");  
	  }
  }

  // Add PIN access value
  private void addPINAccessValue(WebDriver driver, boolean isAddPInAccess) {
	  	
	  testFuncs.myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[1]/button"), 3000);		
	  testFuncs.myClick(driver, By.xpath("//*[@id='pinlock']")										  	, 3000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']", "Pin Lock");
	  if (isAddPInAccess) {
		  
		  testFuncs.myDebugPrinting("isAddPInAccess - true", enumsClass.logModes.MINOR);			
		  testFuncs.myClick(driver, By.xpath("//*[@id='system_pin_lock_enabled']"), 3000);		
	  }
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);
	
	  // Verify create	
	  testFuncs.myDebugPrinting("Verify create", enumsClass.logModes.MINOR);
	  if (isAddPInAccess) {
			 
		testFuncs.searchStr(driver, "system_pin_lock_enabled 1");
	  } else {
			
		  testFuncs.searchStr(driver, "system_pin_lock_enabled 0");
	  }
  }

  // Delete all values
  private void deleteAllValues(WebDriver driver) {
	  
	  testFuncs.myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[2]/button")		 , 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[2]/ul/li[1]/a/span"), 3000);	
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete configuration settings");		
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete all configuration settings and save empty content?");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);	
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save Configuration");		
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Please choose users from the list.");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);  
  }

  // Add Telnet values
  private void addTelnetValue(WebDriver driver, boolean isActivateTelnetAccess) {
	  
		testFuncs.myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[1]/button"), 3000);
		testFuncs.myClick(driver, By.xpath("//*[@id='telnet']")										  	  , 3000);
		testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']", "Activate Telnet access");
		if (isActivateTelnetAccess) {
					 
			testFuncs.myDebugPrinting("isActivateTelnetAccess - true", enumsClass.logModes.MINOR);	
			testFuncs.myClick(driver, By.xpath("//*[@id='management_telnet_enabled']"), 3000);	
		}
		testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);

		// Verify create
		testFuncs.myDebugPrinting("Verify create", enumsClass.logModes.MINOR);
		if (isActivateTelnetAccess) {
			 
			testFuncs.searchStr(driver, "management_telnet_enabled 1");
		} else {
			
			testFuncs.searchStr(driver, "management_telnet_enabled 0");
		}
  }

  // Add Daylight values
  private void addDaylightValues(WebDriver driver) {
	  
		testFuncs.myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[1]/button"), 3000);
		testFuncs.myClick(driver, By.xpath("//*[@id='daylight']")										  , 3000);
		testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']", "Daylight Savings Time");
		testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]")							  , 3000);
		
		// Verify create
		testFuncs.myDebugPrinting("Verify create", enumsClass.logModes.MINOR);
		testFuncs.searchStr(driver, "system_daylight_saving_activate ENABLE");
		testFuncs.searchStr(driver, "system_daylight_saving_mode FIXED");
		testFuncs.searchStr(driver, "system_daylight_saving_offset 60");
		testFuncs.searchStr(driver, "system_daylight_saving_start_date_month 1");
		testFuncs.searchStr(driver, "system_daylight_saving_start_date_day 1");
		testFuncs.searchStr(driver, "system_daylight_saving_start_date_hour 2");
		testFuncs.searchStr(driver, "system_daylight_saving_end_date_month 1");
		testFuncs.searchStr(driver, "system_daylight_saving_end_date_day 1");
		testFuncs.searchStr(driver, "system_daylight_saving_end_date_hour 2");
		testFuncs.searchStr(driver, "system_daylight_saving_start_date_week 1");
		testFuncs.searchStr(driver, "system_daylight_saving_start_date_day_of_week SUNDAY");
		testFuncs.searchStr(driver, "system_daylight_saving_end_date_week 1");
		testFuncs.searchStr(driver, "system_daylight_saving_end_date_day_of_week SUNDAY");
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
