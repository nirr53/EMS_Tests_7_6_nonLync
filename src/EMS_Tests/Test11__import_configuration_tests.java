package EMS_Tests;

import java.io.IOException;
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
* This test tests the Import of users and configuration
* ----------------
* Tests:
* 	 - Login and enter the Import configuration menu.
* 	 1. Import existing configuration
* 	 2. Remove values from each of the sections and verify that the import action re-add them
*    3. Re-import configuration and check imported data
* 
* Results:
* 	 1. Import should end successfully.
* 	 2. All removes should end  successfully
* 	 3. Re-import should end successfully.
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
	Map<String, String> map = new HashMap<String, String>();
	String path        	  	 = "";
	String xpathUploadField  = "//*[@id='fileToUpload']";
	String xpathUploadButton = "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[3]/input";
	map = buildConfMap(map);
	
    // Login enter the Import users menu and upload Configuration file
	testFuncs.myDebugPrinting("Login enter the Import users menu and upload Configuration file");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	
	testFuncs.enterMenu(driver, "Setup_Import_export_configuration_import", "To Import Phone Configuration Files");
	path  = testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("11");
	testFuncs.uploadFile(driver, path, xpathUploadField, xpathUploadButton);
	
	// Step 1 - Check headers of the Import result
	testFuncs.myDebugPrinting("Step 1 - Check headers of the Import-result");
	checkHeaders();
	checkData(map);
	
    // Step 2 - Remove values from each of the sections and verify that the import action re-add them
 	testFuncs.myDebugPrinting("Step 2 - Remove values from each of the sections and verify that the import action re-add them");
 	deleteValues(map);
 	
	// Step 3 - Re-import configuration and check imported data
	testFuncs.myDebugPrinting("Step 3 - Re-import configuration and check imported data");
	testFuncs.enterMenu(driver, "Setup_Import_export_configuration_import", "To Import Phone Configuration Files");
	path  = testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("11");
	testFuncs.uploadFile(driver, path, xpathUploadField, xpathUploadButton);
	checkImportedData(map);
  }
  
  // Check imported data
  private void checkImportedData(Map<String, String> map) {
	  
	  // Check imported data
	  testFuncs.myDebugPrinting("Check imported data", enumsClass.logModes.NORMAL);
	  	 		
	  // Check import of Template
	  testFuncs.myDebugPrinting("Check import of Template", enumsClass.logModes.MINOR);
	  testFuncs.enterMenu(driver, "Setup_Phone_conf_templates", "IP Phones Configuration Templates");
	  testFuncs.searchStr(driver, map.get("tempName"));
	
	  // Check re-edit of System-settings
	  testFuncs.myDebugPrinting("Check re-edit of System-settings", enumsClass.logModes.MINOR);
	  testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings", "System Settings");
	  testFuncs.myDebugPrinting("mwi - " + driver.findElement(By.xpath("//*[@id='MwiVmNumber']")).getText(), enumsClass.logModes.MINOR);
	  testFuncs.myDebugPrinting("ntpserver - " + driver.findElement(By.xpath("//*[@id='ntpserver']")).getText(), enumsClass.logModes.MINOR);
	  Select select = new Select(driver.findElement(By.xpath("//*[@id='ipplanguage']")));
	  WebElement option = select.getFirstSelectedOption();
	  String defaultItem = option.getText();
	  System.out.println(defaultItem );
	  
	  // Check import of Template placeholders
	  testFuncs.myDebugPrinting("Check import of Template placeholders", enumsClass.logModes.MINOR);
	  testFuncs.enterMenu(driver, "Setup_Phone_conf_templates_placeholders", "Template Placeholders");
	  new Select(driver.findElement(By.xpath("//*[@id='models']"))).selectByVisibleText(map.get("tempPHTemp"));
	  testFuncs.myWait(5000); 
	  testFuncs.searchStr(driver, map.get("tempPHName"));
	  
	  // Check import of Tenant placeholders
	  testFuncs.myDebugPrinting("Check import of Tenant placeholders", enumsClass.logModes.MINOR);
	  testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	  testFuncs.selectTenant(driver, map.get("tenPHTenant"));
	  testFuncs.searchStr(driver, map.get("tenPHName"));
	
//	  // Delete Site-place-holders	
//	  testFuncs.myDebugPrinting("Delete Site-placeholders", enumsClass.logModes.MINOR);	
//	  testFuncs.enterMenu(driver, "Site_configuration", "Site Configuration");	
//	  String sitePhSite     = map.get("sitePHSite");
//	  String sitePhSiteMsg  = map.get("sitePHSiteMsg");
//	  String sitePhName  	= map.get("sitePHName");
//	  String sitePhValue 	= map.get("sitePHValue");
//	  testFuncs.selectSite(driver, sitePhSite);	
//	  testFuncs.deleteSitePH(driver, sitePhName, sitePhValue, sitePhSiteMsg);  
	  
	  // Check import of Phone-firmware-files
	  testFuncs.myDebugPrinting("Check import of Phone-firmware-files", enumsClass.logModes.MINOR);
	  testFuncs.enterMenu(driver, "Setup_Phone_conf_phone_firmware_files", "Phone firmware files");
	  testFuncs.searchStr(driver, map.get("firmName"));
  }

  // Delete values
  private void deleteValues(Map<String, String> map) throws IOException {
	  
	  // Delete and modify values before import
	  testFuncs.myDebugPrinting("Delete and modify values before import", enumsClass.logModes.NORMAL);
	  	 		
	  // Delete Templates
	  testFuncs.myDebugPrinting("Delete Templates", enumsClass.logModes.MINOR);
	  testFuncs.enterMenu(driver, "Setup_Phone_conf_templates", "IP Phones Configuration Templates");
	  String templatesName = map.get("tempName");
	  testFuncs.deleteTemplate(driver, templatesName);

	  // Change System Settings
	  testFuncs.myDebugPrinting("Change System Settings", enumsClass.logModes.MINOR);
	  testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings", "System Settings");
	  Select sysLangs = new Select(driver.findElement(By.xpath("//*[@id='ipplanguage']")));
	  sysLangs.selectByVisibleText("Russian");
	  testFuncs.myWait(3000);	
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ntpserver']")  , "1.2.3.4", 2000);	
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='MwiVmNumber']"), "1234"   , 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save general settings");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Server successfully updated.");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]")							, 5000);
	  
	  // Delete Template place-holders
	  testFuncs.myDebugPrinting("Delete Template placeholders", enumsClass.logModes.MINOR);
	  testFuncs.enterMenu(driver, "Setup_Phone_conf_templates_placeholders", "Template Placeholders");
	  String tempPhName = map.get("tempPHName");
	  String tempPhTemp  = map.get("tempPHTemp");
	  testFuncs.deleteTemplatePlaceholder(driver, tempPhTemp, tempPhName);
	  
	  // Delete Tenant place-holders
	  testFuncs.myDebugPrinting("Delete Tenant placeholders", enumsClass.logModes.MINOR);
	  testFuncs.enterMenu(driver, "Tenant_configuration", "Tenant Configuration");
	  String tenPhName   = map.get("tenPHName");
	  String tenPhValue  = map.get("tenPHValue");
	  String tenPhTenant = map.get("tenPHTenant");
	  testFuncs.selectTenant(driver, tenPhTenant);
	  testFuncs.deleteTenantPH(driver, tenPhName, tenPhValue);
		
//	  // Delete Site-place-holders	
//	  testFuncs.myDebugPrinting("Delete Site-placeholders", enumsClass.logModes.MINOR);	
//	  testFuncs.enterMenu(driver, "Site_configuration", "Site Configuration");	
//	  String sitePhSite     = map.get("sitePHSite");
//	  String sitePhSiteMsg  = map.get("sitePHSiteMsg");
//	  String sitePhName  	= map.get("sitePHName");
//	  String sitePhValue 	= map.get("sitePHValue");
//	  testFuncs.selectSite(driver, sitePhSite);	
//	  testFuncs.deleteSitePH(driver, sitePhName, sitePhValue, sitePhSiteMsg);
	  
	  // Delete from Phone-firmware-files
	  testFuncs.myDebugPrinting("Delete from Phone-firmware-files", enumsClass.logModes.NORMAL);
	  testFuncs.enterMenu(driver, "Setup_Phone_conf_phone_firmware_files", "Phone firmware files");
	  String firmName    = map.get("firmName");	  
	  String firmDesc    = map.get("firmDesc");
	  String firmVersion = map.get("firmVersion"); 
	  testFuncs.deleteFirmware(driver,  firmName, firmDesc, firmVersion);  
  }

  // Build configuration map
  private Map<String, String> buildConfMap(Map<String, String> map) {
	  
	  testFuncs.myDebugPrinting("Build configuration", enumsClass.logModes.NORMAL);
	  map.put("tempName"   		 , "importtemplate");	  
	  map.put("mwiVMnumber"		 , "4718");
	  map.put("ippLanguage"		 , "English");
	  map.put("ntpServer"  		 , "10.1.1.11"); 
	  map.put("tempPHName" 		 , "importTemplatePH");
	  map.put("tempPHValue"		 , "12345678");
	  map.put("tempPHDesc" 		 , "Value for import configuration test");
	  map.put("tempPHTemp" 		 , "NirTemplate445");
	  map.put("tenPHName"  		 , "importTenantPH");
	  map.put("tenPHValue" 		 , "1234");
	  map.put("tenPHTenant"		 , "Nir");
	  map.put("sitePHName" 		 , "importSitePH");
	  map.put("sitePHValue"      , "123456");
	  map.put("sitePHSitePrefix" , "AutoDetection");
	  map.put("sitePHSite" 		 , "AutoDetection [AutoDetection] / Nir");
	  map.put("sitePHSiteMsg" 	 , "AutoDetection [AutoDetection]");
	  map.put("firmName"   		 , "importFirmware");
	  map.put("firmDesc"   		 , "Firmware for testing the import configuration");
	  map.put("firmVersion"		 , "430HDUC_2.0.13.121");
	  return map;
  }
 
  // Check headers of imported data
  private void checkHeaders() {
	  
	  testFuncs.myDebugPrinting("Check headers", enumsClass.logModes.NORMAL);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[1]/h1", "Import Result");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[2]/div/div/label", "Tenants");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[3]/div/div/label", "Regions");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[4]/div/div/label", "Sites");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[5]/div/div/label", "Templates");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[6]/div/div/label", "System Settings");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[7]/div/div/label", "Template Placeholders");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[8]/div/div/label", "Tenant Placeholders");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[9]/div/div/label", "Site Placeholders");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[10]/div/div/label", "Phone Firmware Files");
  }

  // Check imported data
  private void checkData(Map<String, String> map) {
	  
	  // Check imported data
	  testFuncs.myDebugPrinting("Check imported data", enumsClass.logModes.NORMAL);
	  
	  // Tenants and sites
	  testFuncs.myDebugPrinting("Check Tenants and sites", enumsClass.logModes.MINOR);	
	  testFuncs.searchStr(driver, "Nir Tenant for Nir auto testing");
	  testFuncs.searchStr(driver, "NirTest1 Non default Tenant for Nir auto testing");
	  testFuncs.searchStr(driver, "NirTest2 Non default Tenant for Nir auto testing");
	  testFuncs.searchStr(driver, "AutoDetection This region is intended for automatic detection nodes NirTest1");
	  testFuncs.searchStr(driver, "AutoDetection Nir NirRegion");
	  testFuncs.searchStr(driver, "Nir_()'<>/\\\":*&^%#@!~ Nir AutoDetection");

	  // Templates	
	  testFuncs.myDebugPrinting("Check Templates", enumsClass.logModes.MINOR);	
	  String templatesName = map.get("tempName");
	  testFuncs.myDebugPrinting("templatesName - " + templatesName, enumsClass.logModes.MINOR);
	  testFuncs.searchStr(driver, templatesName + " Template for import configuration test");
			
	  // System Settings
	  testFuncs.myDebugPrinting("Check System Settings", enumsClass.logModes.MINOR);	
	  testFuncs.searchStr(driver, "MwiVmNumber 4718");
	  testFuncs.searchStr(driver, "ipplanguage English");
	  testFuncs.searchStr(driver, "ntpserver 10.1.1.11");

	  // Template place-holders
	  testFuncs.myDebugPrinting("Check Template place-holders", enumsClass.logModes.MINOR);	
	  String tempPhName  = map.get("tempPHName");
	  String tempPhValue = map.get("tempPHValue");
	  String tempPhDesc  = map.get("tempPHDesc");
	  String tempPhTemp  = map.get("tempPHTemp");
	  testFuncs.searchStr(driver, tempPhName + " " +  tempPhValue + " " + tempPhTemp + " " + tempPhDesc);

	  // Tenant place-holders
	  testFuncs.myDebugPrinting("Check Tenant place-holders", enumsClass.logModes.MINOR);	
	  String tenPhName   = map.get("tenPHName");
	  String tenPhValue  = map.get("tenPHValue");
	  String tenPhTenant = map.get("tenPHTenant");
	  testFuncs.searchStr(driver, tenPhName + " " + tenPhValue + " " + tenPhTenant);
		
	  // Site place-holders
	  testFuncs.myDebugPrinting("Check Site place-holders", enumsClass.logModes.MINOR);
	  String sitePhName  = map.get("sitePHName");
	  String sitePhValue = map.get("sitePHValue");
	  String sitePhSite  = map.get("sitePHSitePrefix");
	  testFuncs.searchStr(driver, sitePhName + " " + sitePhValue + " " + sitePhSite);
		
	  // Phone Firmware Files	
	  testFuncs.myDebugPrinting("Check Phone Firmware Files", enumsClass.logModes.MINOR);	
	  String firmName    = map.get("firmName");
	  String firmVersion = map.get("firmVersion");
	  testFuncs.searchStr(driver, firmName + " " + firmVersion);
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
