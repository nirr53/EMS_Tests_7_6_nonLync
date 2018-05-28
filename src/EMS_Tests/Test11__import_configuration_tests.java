package EMS_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;
import EMS_Tests.enumsClass.browserTypes;

/**
* ----------------
* This test tests the Import of users and configuration
* ----------------
* Tests:
* 	 - Login and enter the Import configuration menu.
* 	 1. Import existing configuration
* 	 2. Check headers of the import-result
* 	 3. Remove a value from each of the sections, and verify that the import action re-add them
* 
* Results:
* 	 1. Import should end successfully.
* 	 2. All headers should be displayed
* 
* 
* 	 4.  Delete should be ended successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test11__import_configuration_tests {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test11__import_configuration_tests(browserTypes browser) {
	  
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
  public void Import_configuration() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	String path        	  	 = "";
	String xpathUploadField  = "//*[@id='fileToUpload']";
	String xpathUploadButton = "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[3]/input";

    // Login enter the Import users menu and upload Configuration file
	testFuncs.myDebugPrinting("Login enter the Import users menu and upload Configuration file");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Import_export_configuration_import", "To Import Phone Configuration Files");
	path  = testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("11");
	testFuncs.uploadFile(driver, path, xpathUploadField, xpathUploadButton);
	
	// Step 1 - Check headers of the import-result
	testFuncs.myDebugPrinting("Step 1 - Check headers of the import-result");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[1]/h1", "Import Result");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[2]/div/div/label", "Tenants");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[3]/div/div/label", "Regions");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[4]/div/div/label", "Sites");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[5]/div/div/label", "Templates");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[6]/div/div/label", "System Settings");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[7]/div/div/label", "Template Placeholders");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[8]/div/div/label", "Tenant Placeholders");
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[10]/div/div/label", "Phone Firmware Files");
	checkData();
	
    // Step 3 - Remove a value from each of the sections, and verify that the import action re-add them
 	testFuncs.myDebugPrinting("Step 2 - Remove a value from each of the sections, and verify that the import action re-add them");
//	
// 	// Delete Templates
//	testFuncs.myDebugPrinting("Delete Templates", enumsClass.logModes.NORMAL);
// 	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates", "IP Phones Configuration Templates");
//	testFuncs.deleteTemplate(driver, templatesName);
//	
	// Change MWI number
	testFuncs.myDebugPrinting("Change MWI number", enumsClass.logModes.NORMAL);
	testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings", "System Settings");
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='MwiVmNumber']"), "1234", 2000);
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button"), 5000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save general settings");
	testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Server successfully updated.");
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]")							, 5000);
	
	// Change System Settings
	testFuncs.myDebugPrinting("Change System Settings", enumsClass.logModes.NORMAL);
	testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings", "System Settings");
	testFuncs.mySendKeys(driver, By.xpath("//*[@id='MwiVmNumber']"), "1234", 2000);
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button"), 5000);
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]")							, 5000);
//	
//	// Delete Template place-holders
//	testFuncs.myDebugPrinting("Delete Template placeholders", enumsClass.logModes.NORMAL);
//	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates_placeholders", "Template Placeholders");
//    testFuncs.deleteTemplatePlaceholder(driver, tempPhTemplate, tempPhName);
//
//    // Delete Tenant place-holders
//	testFuncs.myDebugPrinting("Delete Tenant placeholders", enumsClass.logModes.NORMAL);
//	testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
//	testFuncs.selectTenant(driver, tenPhTenant);
//	testFuncs.deleteTenantPH(driver, tenPhName, tenPhValue);
//
//	// Delete Site-place-holders
//	testFuncs.myDebugPrinting("Delete Site-placeholders", enumsClass.logModes.NORMAL);
//	testFuncs.enterMenu(driver, "Site_configuration", "Site Configuration");
//	testFuncs.selectSite(driver, siteForSearch);
//	testFuncs.deleteSitePH(driver, sitePhName, sitePhValue, siteForDelete);
//
//	// Delete from Phone-firmware-files
//	testFuncs.myDebugPrinting("Delete from Phone-firmware-files", enumsClass.logModes.NORMAL);
//	testFuncs.enterMenu(driver, "Setup_Phone_conf_phone_firmware_files", "Phone firmware files");
//	testFuncs.deleteFirmware(driver,  firmName, firmDesc, firmVersion);
//
//	// Re-import	
//	testFuncs.myDebugPrinting("Re-import", enumsClass.logModes.NORMAL);
//	testFuncs.enterMenu(driver, "Setup_Import_export_configuration_import", "To Import Phone Configuration Files");
//	path  = testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("11");
//	testFuncs.uploadFile(driver, path, xpathUploadField, xpathUploadButton);
//	
//	// Tenants
//	testFuncs.searchStr(driver, "NirTest1 Tenant that used for Nir auto testing " + "Exist");
//	
//	// Regions
//	testFuncs.searchStr(driver, "Test1Region Test1 Test1Region " 										   		+ "Exist");
//	testFuncs.searchStr(driver, "AutoDetection Nir This region is intended for automatic detection nodes " 		+ "Exist");
//	testFuncs.searchStr(driver, "AutoDetection NirTest1 This region is intended for automatic detection nodes " + "Exist");
//	
//	// Sites
//	testFuncs.searchStr(driver, "Test1Site Test1Site Test1 Test1Region " + "Exist");
//	testFuncs.searchStr(driver, "AutoDetection Nir AutoDetection " 		 + "Exist");
//	testFuncs.searchStr(driver, "AutoDetection NirTest1 AutoDetection "  + "Exist");
//
//	// Templates		
//	testFuncs.searchStr(driver, templatesName + " Template for Import configuration tests");
//
//	// System Settings
//	testFuncs.searchStr(driver, "MwiVmNumber 888 "     + "Exist");
//	testFuncs.searchStr(driver, "ntpserver 10.1.1.10 " + "Exist");
//
//	// Template place-holders
//	testFuncs.searchStr(driver, tempPhName + " " +  tempPhValue + " Template PH that used for configuration import tests " + tempPhTemplate + " Added");
//
//	// Tenant place-holders
//	testFuncs.searchStr(driver, tenPhName + " " + tenPhValue + " Nir " + "Added");
//	
//	// Site place-holders
//	testFuncs.searchStr(driver, sitePhName + " " + sitePhValue + " " + sitePhSite + " Added");
//
//	// Phone Firmware Files
//	testFuncs.searchStr(driver, firmName + " " + firmDesc + " " + firmVersion + " /data/NBIF/ippmanager/generate//firmware/430HD.img " + "Added");
  }
  
  private void checkData() {
	  
	  // Tenants
	  testFuncs.myDebugPrinting("Check Tenants", enumsClass.logModes.NORMAL);
	  testFuncs.searchStr(driver, "Nir Tenant for Nir auto testing");
	  testFuncs.searchStr(driver, "NirTest1 Non default Tenant for Nir auto testing");
	  testFuncs.searchStr(driver, "NirTest2 Non default Tenant for Nir auto testing");
		
	  // Regions
	  testFuncs.myDebugPrinting("Check Regions", enumsClass.logModes.NORMAL);
	  testFuncs.searchStr(driver, "AutoDetection This region is intended for automatic detection nodes NirTest1");
		
	  // Sites
	  testFuncs.myDebugPrinting("Check Sites", enumsClass.logModes.NORMAL);
	  testFuncs.searchStr(driver, "AutoDetection Nir NirRegion");
	  testFuncs.searchStr(driver, "Nir_()'<>/\":*&^%#@!~ Nir AutoDetection");

		// Templates
		testFuncs.myDebugPrinting("Check Templates", enumsClass.logModes.NORMAL);
		String templatesName = "Template_for_configuration_test";
//		testFuncs.myDebugPrinting("templatesName - " + templatesName, enumsClass.logModes.MINOR);
//		testFuncs.searchStr(driver, templatesName + " Template for Import configuration tests");
			
		// System Settings
		testFuncs.myDebugPrinting("Check System Settings", enumsClass.logModes.NORMAL);
		testFuncs.searchStr(driver, "MwiVmNumber 7521");
		testFuncs.searchStr(driver, "ntpserver 10.1.1.10");

//		// Template place-holders
//		testFuncs.myDebugPrinting("Check Template place-holders", enumsClass.logModes.NORMAL);
//		String tempPhName 	  = "ConfTest_ph";
//		String tempPhValue	  = "1234";
//		String tempPhTemplate = "Audiocodes_430HD";
//		testFuncs.myDebugPrinting("tempPhName - "  + tempPhName, enumsClass.logModes.MINOR);
//		testFuncs.myDebugPrinting("tempPhValue - " + tempPhValue, enumsClass.logModes.MINOR);
//		testFuncs.searchStr(driver, tempPhName + " " +  tempPhValue + " Template PH that used for configuration import tests " + tempPhTemplate + "");

//		// Tenant place-holders
//		testFuncs.myDebugPrinting("Check Tenant place-holders", enumsClass.logModes.NORMAL);
//		String tenPhName   = "ConfTest_Tenant_ph_key";
//		String tenPhValue  = "1234";
//		String tenPhTenant = "Nir";
//		testFuncs.searchStr(driver, tenPhName + " " + tenPhValue + " " + tenPhTenant + "");
	//	
//		// Site place-holders
//		testFuncs.myDebugPrinting("Check Site place-holders", enumsClass.logModes.NORMAL);
//		String sitePhName    = "ConfTest_Site_ph_key";
//		String sitePhValue   = "1234";
//		String sitePhSite    = "AutoDetection";
//		String siteForDelete = sitePhSite + " [" + sitePhSite + "]";
//		String siteForSearch = sitePhSite + " [" + sitePhSite + "] / Nir";
//		testFuncs.searchStr(driver, sitePhName + " " + sitePhValue + " " + sitePhSite + "");

		// Phone Firmware Files
		testFuncs.myDebugPrinting("Check Phone Firmware Files", enumsClass.logModes.NORMAL);
		String firmName    = "430HDUC_2.0.13.121";
		testFuncs.searchStr(driver, firmName);
	

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
