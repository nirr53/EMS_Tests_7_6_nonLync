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
* This test tests a users+device import mechanism and configuration import while login Via Operation users (system+tenant)
* ----------------
* Tests:
* 	 - Login via an Operation user (system)
* 	 1. Enter the import users+devices menu and try to import some users.
* 	 2. Enter the import configuration menu and try to import a configuration.
* 	 3. Delete the imported configuration and users.
* 
 	 - Logout, re-login via an Operation user (tenant)
* 	 4. Enter the import users+devices menu and try to import some users.
* 	 5. Enter the import configuration menu and try to import a configuration.
* 	 6. Logout, re-login and delete the created users
* 
* Results:
* 	1-3. Via an Operation user (system) all import actions should work work successfully.
*  	4-6. Via an Operation user (tenant) only import-users should be allowed.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test54__Operation_import {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test54__Operation_import(browserTypes browser) {
	  
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
  public void Operation_imports() throws Exception {
	  
	  Log.startTestCase(this.getClass().getName());

	  // Set variables
	  String path        	  	   = "";
	  String prefixUser		 	   = "operImpUser";
	  String prefixDevice		   = "operImpDevice";
	  String usrsXpathUploadField  = "//*[@id='file_source']";
	  String usrsXpathUploadButton = "//*[@id='uploadForm']/div[2]/a";
	  String confXpathUploadField  = "//*[@id='fileToUpload']";
	  String confXpathUploadButton = "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[3]/input";
	  String[] confirmMessageStrs  = {"Import Users & Devices", "The process might take a few minutes. Do you want to continue?"};
	  String usersNumber   		   = "5"; 
	  Map<String, String> map 	   = new HashMap<String, String>();
	
	  // Login via Operation-user (system )and enter the Import users-devices menu
	  testFuncs.myDebugPrinting("Login via Operation-user (system )and enter the Import users-devices menu");
	  testFuncs.login(driver, testVars.getOperUsername(), testVars.getOperPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_IMPORT_EXPORT_USRS_DEVICES_IMPORT, "Import Users and Devices information");
	  
	  // Step 1 - Import users+devices
	  testFuncs.myDebugPrinting("Step 1 - Import users+devices");
	  path  = testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("54.1");
	  testFuncs.uploadFile(driver, path, usrsXpathUploadField, usrsXpathUploadButton, confirmMessageStrs);		
	  for (int i = 1; i <= Integer.valueOf(usersNumber); ++i) {

		  String tempIdx = String.valueOf(i);  
		  testFuncs.searchStr(driver, prefixUser + "_" + tempIdx + "@" + testVars.getDomain() + " Added, Device " + prefixDevice + "_" + tempIdx + " - added");   
	  }
	  
	  // Step 2 - Import configuration
	  testFuncs.myDebugPrinting("Step 2 - Import condiguration");
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_IMPORT_EXPORT_CONFIGURATION_IMPORT, "To Import Phone Configuration Files");
	  path  = testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("11"); 
	  testFuncs.uploadFile(driver, path, confXpathUploadField, confXpathUploadButton);
	  checkData();
	  
	  // Step 3 - Delete the created users
	  testFuncs.myDebugPrinting("Step 3 - Delete the created users");
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");  
	  testFuncs.selectMultipleUsers(driver, prefixUser, usersNumber);
	  map.put("usersPrefix"	  , prefixUser + "_");
	  map.put("usersNumber"	  , usersNumber);    
	  map.put("startIdx"   	  , String.valueOf(1));    
	  map.put("srcUsername"	  , "Finished");
	  map.put("action"	 	  , "Delete Users");	    
	  map.put("skipVerifyDelete", "true");
	  testFuncs.setMultipleUsersAction(driver, map);
	  for (int i = 1; i < Integer.valueOf(usersNumber); ++i) {
				
		  String tempIdx = String.valueOf(i);
		  testFuncs.searchStr(driver, prefixUser.toLowerCase() + "_" + tempIdx + "@" + testVars.getDomain() + " Finished");			
	  }
	  
	  // Logout, and re-login via Operation-user (tenant) and enter the Import users-devices menu
	  testFuncs.myDebugPrinting("Logout, and re-login via Operation-user (tenant) and enter the Import users-devices menu");	
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	  testFuncs.login(driver, testVars.getOperTenUsername(), testVars.getOperTenPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_IMPORT_EXPORT_USRS_DEVICES_IMPORT, "Import Users and Devices information");

	  // Step 4 - Import users+devices
	  testFuncs.myDebugPrinting("Step 4 - Import users+devices");
	  path  = testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("54.1");
	  testFuncs.uploadFile(driver, path, usrsXpathUploadField, usrsXpathUploadButton, confirmMessageStrs);		
	  for (int i = 1; i <= Integer.valueOf(usersNumber); ++i) {

		  String tempIdx = String.valueOf(i);  
		  testFuncs.searchStr(driver, prefixUser + "_" + tempIdx + "@" + testVars.getDomain() + " Added, Device " + prefixDevice + "_" + tempIdx + " - added");   
	  }
	  
	  // Step 5 - Import configuration should be forbidden
	  testFuncs.myDebugPrinting("Step 5 - Import configuration should be forbidden");
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_IMPORT_EXPORT_CONFIGURATION_IMPORT, "To Import Phone Configuration Files");
	  path  = testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("54.2");
	  testFuncs.uploadFile(driver, path, confXpathUploadField, confXpathUploadButton);  
	  testFuncs.searchStr(driver, "Unauthorized");
	  testFuncs.searchStr(driver, "You do not have permission to modify this item");
	  
	  // Step 6 - Logout, re-login and delete the created users
	  testFuncs.myDebugPrinting("Step 6 - Logout, re-login and delete the created users");
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	  testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");  
	  testFuncs.selectMultipleUsers(driver, prefixUser, usersNumber);
	  map.put("usersPrefix"	  , prefixUser + "_");
	  map.put("usersNumber"	  , usersNumber);    
	  map.put("startIdx"   	  , String.valueOf(1));    
	  map.put("srcUsername"	  , "Finished");
	  map.put("action"	 	  , "Delete Users");	    
	  map.put("skipVerifyDelete", "true");
	  testFuncs.setMultipleUsersAction(driver, map);
	  for (int i = 1; i < Integer.valueOf(usersNumber); ++i) {
				
		  String tempIdx = String.valueOf(i);
		  testFuncs.searchStr(driver, prefixUser.toLowerCase() + "_" + tempIdx + "@" + testVars.getDomain() + " Finished");			
	  }
  }
  
  // Check configuration import
  private void checkData() {
	  	
	  // Check headers of the import-result
	  testFuncs.myDebugPrinting("Check headers of the import-result");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[1]/h1", "Import Result");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[2]/div/div/label" , "Tenants");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[3]/div/div/label" , "Regions");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[4]/div/div/label" , "Sites");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[5]/div/div/label" , "Templates");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[6]/div/div/label" , "System Settings");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[7]/div/div/label" , "Template Placeholders");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[8]/div/div/label" , "Tenant Placeholders");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[10]/div/div/label", "Phone Firmware Files");

	  // Check data of the import-result	
	  testFuncs.myDebugPrinting("Check data of the import-result", enumsClass.logModes.NORMAL);
	  testFuncs.searchStr(driver, "Nir Tenant for Nir auto testing");
	  testFuncs.searchStr(driver, "AutoDetection Nir AutoDetection");
	  testFuncs.searchStr(driver, "AutoDetection NirTest1 AutoDetection");
	  testFuncs.searchStr(driver, "NirTemplate440 Template for Nir auto testing");
	  testFuncs.searchStr(driver, "MwiVmNumber 7521");
	  testFuncs.searchStr(driver, "ntpserver 10.1.1.10");	
	  testFuncs.searchStr(driver, "430HD 430HDUC_2.0.13.121 430HD - default firmware"); 
  }

  @After
  public void tearDown() throws Exception {
	  
//    driver.quit();
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
    	testFuncs.myFail(verificationErrorString);
    }
  }
}
