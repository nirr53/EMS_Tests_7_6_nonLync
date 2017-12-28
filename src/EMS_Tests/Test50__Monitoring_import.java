package EMS_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.openqa.selenium.*;

/**
* ----------------
* This test tests the Import abilities when login via Monitoring users (system + tenant)
* ----------------
* Tests:
* 	 - Login via a Monitoring user (system)
* 	 1. Enter the Import users+devices menu and try to import a users+devices zip file
*    2. Enter the Import configuration menu and try to import a configuration zip file
* 
* 	 - Logout, and re-login via a Monitoring user (tenant)
* 	 3. Enter the Import users+devices menu and try to import a users+devices zip file
*    4. Enter the Import configuration menu and try to import a configuration zip file
*
* Results:
* 	 1-4. In all cases, You cannot import a zip file while login via a Monitoring user (system+tenant).
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test50__Monitoring_import {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test50__Monitoring_import(String browser) {
	  
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
  public void Monitoring_import_tests() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String path        	  	 	= "";
	String xpathUploadField  	= "//*[@id='fileToUpload']";	 	
	String xpathUploadButton 	= "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[3]/input";
	
    // Login via a Monitoring user (system) and enter Import users+devices menu
	testFuncs.myDebugPrinting("Login via a Monitoring user (system) and enter Import users+devices menu");
	testFuncs.login(driver, testVars.getMonitUsername(), testVars.getMonitPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Import_export_users_devices_import", "Import Users and Devices information");
	
	// Step 1 - Verify that you cannot import a zip file
	testFuncs.myDebugPrinting("Step 1 - Verify that you cannot import a zip file");
	String uploadStr = driver.findElement(By.xpath("//*[@id='uploadForm']/div[2]/a")).getAttribute("class");
	testFuncs.myAssertTrue("Import button is not deactivated !!\nuploadStr - " + uploadStr, uploadStr.contains("not-active"));
	
	// Step 2 - Enter the import-configuration menu and verify that you cannot import a zip file
	testFuncs.myDebugPrinting("Step 2 - Enter the import-configuration menu and verify that you cannot import a zip file");
	testFuncs.enterMenu(driver, "Setup_Import_export_configuration_import", "To Import Phone Configuration Files");
	path  = testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("11");
	testFuncs.uploadFile(driver, path, xpathUploadField, xpathUploadButton);
    testFuncs.searchStr(driver, "Unauthorized");
	testFuncs.searchStr(driver, "You do not have permission to modify this item");
	  
	// Logout and re-login via a Monitoring user (tenant) and enter Import users+devices menu
	testFuncs.myDebugPrinting("Logout and re-login via a Monitoring user (tenant) and enter Import users+devices menu"); 
	testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getMonitTenUsername(), testVars.getMonitTenPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Import_export_users_devices_import", "Import Users and Devices information");

	// Step 3 - Verify that you cannot import a zip file
	testFuncs.myDebugPrinting("Step 3 - Verify that you cannot import a zip file");
	uploadStr = driver.findElement(By.xpath("//*[@id='uploadForm']/div[2]/a")).getAttribute("class");
	testFuncs.myAssertTrue("Import button is not deactivated !!\nuploadStr - " + uploadStr, uploadStr.contains("not-active"));
	
	// Step 4 - Enter the import-configuration menu and verify that you cannot import a zip file
	testFuncs.myDebugPrinting("Step 4 - Enter the import-configuration menu and verify that you cannot import a zip file");
	testFuncs.enterMenu(driver, "Setup_Import_export_configuration_import", "To Import Phone Configuration Files");
	path  = testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("11");
	testFuncs.uploadFile(driver, path, xpathUploadField, xpathUploadButton);
    testFuncs.searchStr(driver, "Unauthorized");
	testFuncs.searchStr(driver, "You do not have permission to modify this item");
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
