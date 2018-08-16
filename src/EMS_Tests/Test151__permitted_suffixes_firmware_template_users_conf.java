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
* This test tests the permitted suffix feature on Firmware, Template and Import users & configuration menus. 
* ----------------
* Tests:
* 	 - Enter the System-Settings menu and add jpeg suffix file to the permitted-list.
* 	 1. Try to upload a jpeg file to Firmware menu.
* 	 2. Try to upload a jpeg file to Templates menu.
* 	 3. Try to upload a jpeg file to Import-users menu.
* 	 4. Try to upload a jpeg file to Import-configuration menu.
*    5. Remove jpeg suffix file to the permitted-list
* 
* Results:
*  1-4. Upload should should succeed
*    5. The remove should succeed.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test151__permitted_suffixes_firmware_template_users_conf {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  String permittedSuffixes = ".cab,.cfg,.csv,.id,.img,.zip";

  
  // Default constructor for print the name of the used browser 
  public Test151__permitted_suffixes_firmware_template_users_conf(browserTypes browser) {
	  
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
  public void Permitted_suffixes_firmware_template_users_configuration() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String path  				 = testVars.getImportFile("35.2");
	String fullPath  			 = testVars.getSrcFilesPath() + "\\" + path;
	String Id 				     = testFuncs.getId();
	String firmName    		     = "permittedName"  + Id;
	String firmDesc     	     = "permittedDesc"  + Id;
	String firmVersion  	     =  String.valueOf(testFuncs.getNum(128)) + "." + 
						            String.valueOf(testFuncs.getNum(128)) + "." +
						            String.valueOf(testFuncs.getNum(128)) + "." +
						            String.valueOf(testFuncs.getNum(128));
	String firmRegion   	     = testVars.getNonDefTenant(0);	
	String xpathUploadField  	 = "//*[@id='file_source']";
	String xpathUploadButton     = "//*[@id='uploadForm']/div[2]/a";
	String confXpathUploadField  = "//*[@id='fileToUpload']";
	String confXpathUploadButton = "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[3]/input";
	String[] confirmMessageStrs = {"Import Users and Devices", "Please choose a *.zip or *.csv file."};
	 
    // Enter the System configuration menu and add jpeg suffix file to the permitted-list
	testFuncs.myDebugPrinting("Enter the System configuration menu and add jpeg suffix file to the permitted-list");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_SYSTEM_SETTINGS, "System Settings");
	editPermSuffixesField(permittedSuffixes + ",.jpeg", "Save Upload File Extensions", "Successful to save file extensions to upload.");
	  
	// Nir VI 153180
	// Step 1 - Try to upload a jpeg file to Firmware menu
	testFuncs.myDebugPrinting("Step 1 - Try to upload a jpeg file to Firmware menu");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_FIRM_FILES, "Phone firmware files");
	testFuncs.addNewFirmware(driver, firmName, firmDesc, firmVersion, firmRegion, path);
	
	// Step 2 - Try to upload a jpeg file to Templates menu
	testFuncs.myDebugPrinting("Step 1 - Try to upload a jpeg file to Firmware menu");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_TEMPLATES, "IP Phones Configuration Templates");
  	uploadNonCfgToTemplates(driver, fullPath); 	
	
	// Nir VI 153182
	// Step 3 - Try to upload a jpeg file to Import-users menu
	testFuncs.myDebugPrinting("Step 3 - Try to upload a jpeg file to Import-users menu");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_IMPORT_EXPORT_USRS_DEVICES_IMPORT, "Import Users and Devices information");
	testFuncs.uploadFile(driver, fullPath, xpathUploadField, xpathUploadButton, confirmMessageStrs);
	
	// Nir VI 153182
	// Step 4 - Try to upload a jpeg file to Import-configuration menu
	testFuncs.myDebugPrinting("Step 4 - Try to upload a jpeg file to Import-configuration menu");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_IMPORT_EXPORT_CONFIGURATION_IMPORT, "To Import Phone Configuration Files");
	testFuncs.uploadFile(driver, fullPath, confXpathUploadField, confXpathUploadButton, null);
	testFuncs.searchStr(driver, "Please select the Configuration ZIP file.");
  }
  
  // Edit the Permitted-Suffixes menu
  private void editPermSuffixesField(String permStr, String msgBoxHdr, String msgBoxHdr2) {
	  
	  testFuncs.myDebugPrinting("permStr - <" + permStr + ">", enumsClass.logModes.MINOR);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div/table/tbody/tr/td/table/tbody/tr/td[2]/input"), permStr, 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div/table/tbody/tr/td/table/tbody/tr/td[4]/button"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , msgBoxHdr);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", msgBoxHdr2);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
  }
  
  /**
  *  Upload non-cfg file to created Template
  *  @param driver         - A given driver
  *  @param nonCfgFileName -  An invalid file path
  */
  private void uploadNonCfgToTemplates(WebDriver driver, String nonCfgFileName) {
	  
	  // Try to edit one of the Templates by uploading a file
	  testFuncs.myDebugPrinting("Try to edit one of the Templates by uploading a file", enumsClass.logModes.MINOR);
	  testFuncs.myDebugPrinting("nonCfgFileName - " + nonCfgFileName, enumsClass.logModes.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='tenants1']/tbody/tr[3]/td[8]/div/buttton[1]")							 , 5000);  
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/div/div[2]/div[1]/a[3]"), 5000);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "Upload IP Phone Template "); 
	  WebElement fileInput = driver.findElement(By.name("uploadedfile"));
	  fileInput.sendKeys(nonCfgFileName);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 5000);
	  testFuncs.myWait(7000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Upload IP Phone Template Audiocodes");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "The IP Phone template has been uploaded successfully.");	  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000); 
	  
	  
	  // Restore configuration to default
	  testFuncs.myClick(driver, By.xpath("//*[@id='restoreToDefId']"), 5000); 
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Reset the IP Phone template");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to reset the IP Phone template?");	    
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000); 				  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update configuration template");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Succesfull to reset the configuration template to default file content.");	    
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
  }

  @After
  public void tearDown() throws Exception {
	    
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_SYSTEM_SETTINGS, "System Settings");	
	editPermSuffixesField(permittedSuffixes, "Save Upload File Extensions", "Successful to save file extensions to upload.");
    driver.quit();
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
    	testFuncs.myFail(verificationErrorString);
    }
  }
}
