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
* This test tests the permitted suffix feature
* ----------------
* Tests:
* 	 - Enter the System-Settings menu.
* 	 1. Check headers of the Permitted-Suffix section
* 	 2. Try to delete all the permitted suffixes
* 	 3. Try to add invalid format string
*    4. Add jpeg suffix file to the permitted-list and try to upload a jpeg to the system
*    5. Remove jpeg suffix file to the permitted-list and try to upload a jpeg to the system
* 
* Results:
* 	 1. Section should be displayed properly.
*  2-3. An error-message box should be displayed.
*    4. The upload should succeed
* 	 5. The remove should NOT succeed.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test150__permitted_suffixes {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  String permittedSuffixes = ".cab,.cfg,.csv,.id,.img,.zip";
  
  // Default constructor for print the name of the used browser 
  public Test150__permitted_suffixes(browserTypes browser) {
	  
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
  public void Permitted_suffixes() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	String usedFile = testVars.getImportFile("35.2");
	 
    // Enter the System configuration menu
	testFuncs.myDebugPrinting("Enter the System configuration menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings", "System Settings");

	// Step 1 - Check headers of the Permitted-Suffix section
	testFuncs.myDebugPrinting("Step 1 - Check headers of the Permitted-Suffix section");
	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[1]/h3"												  , "Upload File Extensions");
	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[2]/div/table/tbody/tr/td/table/tbody/tr/td[1]"		  , "Accept Extensions");
	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[3]/div[2]/div/table/tbody/tr/td/table/tbody/tr/td[3]/strong", "Note:   Use ' , ' as delimeter of the extensions ( '.cfg,.img,.zip' ).");
	  
	// Step 2 - Try to delete all the permitted suffixes
	testFuncs.myDebugPrinting("Step 2 - Try to delete all the permitted suffixes");
	editPermSuffixesField("", "Save Upload File Extensions", "Please enter the file extension to upload.");
	  
	// Step 3 - Try to add invalid format string
	testFuncs.myDebugPrinting("Step 3 - Try to add invalid format string");
	// Nir 9\5\18 - VI 153160 was inserted
	editPermSuffixesField("gdggd", "Save Upload File Extensions", "Successful to save file extensions to upload.");

	// Step 4 - Add jpeg suffix file to the permitted-list and try to upload a jpeg to the system
	testFuncs.myDebugPrinting("Step 4 - Add jpeg suffix file to the permitted-list and try to upload a jpeg to the system");
	editPermSuffixesField(permittedSuffixes + ",.jpeg", "Save Upload File Extensions", "Successful to save file extensions to upload.");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_phone_configuration_files", "Manage Configuration Files");
	testFuncs.verifyStrByXpath(driver, "//*[@id='trunkTBL']/div[2]/div[2]/div/div/div/strong[2]", "Acceptable file extension(s) to upload : *.cab, *.cfg, *.csv, *.id, *.img, *.jpeg, *.zip. Configuration standard file extension(s): *.cfg");
	testFuncs.uploadConfigurationFile(driver,  testVars.getSrcFilesPath() + "\\" + usedFile, usedFile);
	testFuncs.deleteConfigurationFile(driver,  usedFile);

	// Step 5 - Remove jpeg suffix file to the permitted-list and try to upload a jpeg to the system
	testFuncs.myDebugPrinting("Step 5 - Remove jpeg suffix file to the permitted-list and try to upload a jpeg to the system");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings", "System Settings");
	editPermSuffixesField(permittedSuffixes, "Save Upload File Extensions", "Successful to save file extensions to upload.");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_phone_configuration_files", "Manage Configuration Files");
	testFuncs.verifyStrByXpath(driver, "//*[@id='trunkTBL']/div[2]/div[2]/div/div/div/strong[2]", "Acceptable file extension(s) to upload : *.cab, *.cfg, *.csv, *.id, *.img, *.zip. Configuration standard file extension(s): *.cfg.");
	testFuncs.uploadNonCfgToPhoneConfiguration(driver,  testVars.getSrcFilesPath() + "\\" + usedFile); 
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

  @After
  public void tearDown() throws Exception {
	  
	testFuncs.enterMenu(driver, "Setup_Phone_conf_system_settings", "System Settings");
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
