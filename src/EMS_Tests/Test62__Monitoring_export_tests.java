package EMS_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.openqa.selenium.*;import EMS_Tests.enumsClass.*;

/**
* ----------------
* This test tests an export of users when login via a Monitoring user.
*  ----------------
*  Tests:
* 	 - Login the EMS via a Monitoring user (System).
* 	 1. Enter the Export-Users-Devices menu and export users+devices
* 	 2. Enter the Export-Configuration menu and export configuration
* 
* 	 - Login the EMS via an Monitoring user (Tenant).
* 	 3. Enter the Export-Users-Devices menu and export users+devices
* 	 4. Enter the Export-Configuration menu and export configuration
* 
* Results:
*  	 1-4. Export should work.
*  
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test62__Monitoring_export_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test62__Monitoring_export_tests(browserTypes browser) {
	  
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
  public void Monitoring_export_users() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	if (!this.usedBrowser.toString().equals(enumsClass.browserTypes.IE)) {

	    // Login via a Monitoring user (System)
		testFuncs.myDebugPrinting("Login via a Monitoring user (System)");
		testFuncs.login(driver, testVars.getMonitUsername(), testVars.getMonitPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
		
	    // Step 1 - Enter the Export-Users-Devices menu and export users+devices
		String downloadedFile = "users.zip";
		testFuncs.myDebugPrinting("Step 1 - Enter the Export-Users-Devices menu and export users+devices");
		testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_IMPORT_EXPORT_USRS_DEVICES_EXPORT, "Export Users and Devices information");
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile);
		testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[2]/a"), 60000);
		testFuncs.myAssertTrue("File was not downloaded successfully !!", testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), downloadedFile));
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile);
	
	    // Step 2 - Enter the Export-Configuration menu and export configuration
		String downloadedFile2 = "Configuration.zip";
		testFuncs.myDebugPrinting("Step 2 - Enter the Export-Configuration menu and export configuration");
		testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_IMPORT_EXPORT_CONFIGURATION_EXPORT, "To export phone configuration files");
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile2);
		testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[4]/button"), 60000);
		testFuncs.myAssertTrue("File was not downloaded successfully !!", testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), downloadedFile2));
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile2);	
		
	    // Logout and re-login via an Monitoring user (Tenant)
		testFuncs.myDebugPrinting("Logout and re-login via an Monitoring user (Tenant)");
		testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
		testFuncs.login(driver, testVars.getMonitTenUsername(), testVars.getMonitTenPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
		
	    // Step 3 - Enter the Export-Users-Devices menu and export users+devices
		testFuncs.myDebugPrinting("Step 3 - Enter the Export-Users-Devices menu and export users+devices");
		testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_IMPORT_EXPORT_USRS_DEVICES_EXPORT, "Export Users and Devices information");
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile);
		testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[2]/a"), 60000);
		testFuncs.myAssertTrue("File was not downloaded successfully !!", testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), downloadedFile));
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile);
	
	    // Step 4 - Enter the Export-Configuration menu and export configuration
		testFuncs.myDebugPrinting("Step 4 - Enter the Export-Configuration menu and export configuration");
		testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_IMPORT_EXPORT_CONFIGURATION_EXPORT, "To export phone configuration files");
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile2);
		testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[4]/button"), 60000);
		testFuncs.myAssertTrue("File was not downloaded successfully !!", testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), downloadedFile2));
		testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile2);
	}
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
