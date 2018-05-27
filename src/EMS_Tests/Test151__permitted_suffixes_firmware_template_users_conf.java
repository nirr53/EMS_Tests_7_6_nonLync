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
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test151__permitted_suffixes_firmware_template_users_conf(String browser) {
	  
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
	String path  				= testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("35.2");
	String permittedSuffixes    = ".cab,.cfg,.csv,.id,.img,.zip";
	String Id 				    = testFuncs.getId();
	String firmName    		    = "myFirmName"  + Id;
	String firmDesc     	    = "myFirmDesc"  + Id;
	String firmVersion  	    = String.valueOf(testFuncs.getNum(128)) + "." + 
						          String.valueOf(testFuncs.getNum(128)) + "." +
						          String.valueOf(testFuncs.getNum(128)) + "." +
						          String.valueOf(testFuncs.getNum(128));
	String firmRegion   	    = testVars.getNonDefTenant(0);	
	String xpathUploadField  	= "//*[@id='file_source']";
	String xpathUploadButton    = "//*[@id='uploadForm']/div[2]/a";
	String[] confirmMessageStrs = {"Import Users & Devices", "The process might take a few minutes. Do you want to continue?"};
	 
    // Enter the System configuration menu and add jpeg suffix file to the permitted-list
	testFuncs.myDebugPrinting("Enter the System configuration menu and add jpeg suffix file to the permitted-list");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings", "System Settings");
	editPermSuffixesField(permittedSuffixes + ",.jpeg", "Save Upload File Extensions", "Successful to save file extensions to upload.");
	  
//	// Nir VI 153180
//	// Step 1 - Try to upload a jpeg file to Firmware menu
//	testFuncs.myDebugPrinting("Step 1 - Try to upload a jpeg file to Firmware menu");
//	testFuncs.enterMenu(driver, "Setup_Phone_conf_phone_firmware_files", "Phone firmware files");
//	testFuncs.addNewFirmware(driver, firmName, firmDesc, firmVersion, firmRegion, usedFile);
//	testFuncs.deleteFirmware(driver,  firmName, firmDesc, firmVersion);
	
//	// Nir VI 153182
//	// Step 3 - Try to upload a jpeg file to Import-users menu
//	testFuncs.myDebugPrinting("Step 3 - Try to upload a jpeg file to Import-users menu");
//	testFuncs.enterMenu(driver, "Setup_Import_export_users_devices_import", "Import Users and Devices information");
//	testFuncs.uploadFile(driver, path, xpathUploadField, xpathUploadButton, confirmMessageStrs);
//	
//	// Nir VI 153182
//	// Step 4 - Try to upload a jpeg file to Import-configuration menu
//	testFuncs.myDebugPrinting("Step 4 - Try to upload a jpeg file to Import-configuration menu");
//	testFuncs.enterMenu(driver, "Setup_Import_export_configuration_import", "To Import Phone Configuration Files");
//	testFuncs.uploadFile(driver, path, xpathUploadField, xpathUploadButton, confirmMessageStrs);

  }
  
  // Edit the Permitted-Suffixes menu
  private void editPermSuffixesField(String permStr, String msgBoxHdr, String msgBoxHdr2) {
	  
	  testFuncs.myDebugPrinting("permStr - <" + permStr + ">", testVars.logerVars.MINOR);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div/table/tbody/tr/td/table/tbody/tr/td[2]/input"), permStr, 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div/table/tbody/tr/td/table/tbody/tr/td[4]/button"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , msgBoxHdr);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", msgBoxHdr2);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
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
