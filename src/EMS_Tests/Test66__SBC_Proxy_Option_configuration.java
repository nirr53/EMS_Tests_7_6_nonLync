package EMS_Tests;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
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
* This test tests the SBC Proxy Option configuration menu
* ----------------
* Tests:
* 	 - Login the EMS environment.
* 	 - Enter the System settings menu & the SBC Proxy Option configuration menu.
* 	 1. Edit SBC-proxy configuration template
*	 2. Restore SBC-proxy configuration template.
*	 3. Upload new SBC-proxy configuration template
* 
* Results:
* 	 1.3 All actions should succeed.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test66__SBC_Proxy_Option_configuration {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test66__SBC_Proxy_Option_configuration(browserTypes browser) {
	  
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
  public void SBC_proxy_option_configuration_menu() throws Exception {
	  
	  Log.startTestCase(this.getClass().getName());
	  
	  // Set variables
	  String strForEdit = "ems_server/keep_alive_period=" + testFuncs.getId();
	  String uploadStr  = "1234";
	  testFuncs.myDebugPrinting("strForEdit - " + strForEdit, enumsClass.logModes.MINOR);
	  
	  // Enter System settings and SBC proxy configuration menu
	  testFuncs.myDebugPrinting("Enter System settings and SBC proxy configuration menu");
	  testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_SYSTEM_SETTINGS_SBC_CONF, "Proxy DHCP Options Configuration");
	  
	  // Step 1 - Edit SBC proxy configuration template
	  testFuncs.myDebugPrinting("Step 1 - Edit SBC proxy configuration template");
	  editSBCproxyConfigurationTemplate(driver, strForEdit);
	  
	  // Step 2 - Restore SBC proxy configuration template
	  testFuncs.myDebugPrinting("Step 2 - Restore SBC proxy configuration template");
	  restoreSBCproxyConfigurationTemplate(driver, strForEdit);
	  
	  // Step 3 - Upload SBC proxy configuration template
	  testFuncs.myDebugPrinting("Step 3 - Upload SBC proxy configuration template");
	  uploadSBCproxyConfigurationTemplate(driver, testVars.getSrcFilesPath() + "\\" + testVars.getImportFile("66"), uploadStr);
	  restoreSBCproxyConfigurationTemplate(driver, uploadStr);
  }

  // Upload new configuration
  private void uploadSBCproxyConfigurationTemplate(WebDriver driver, String filePath, String uploadStr) throws UnsupportedFlavorException, IOException {
	  
	  // Upload SBC configuration
	  testFuncs.myDebugPrinting("Upload SBC configuration", enumsClass.logModes.NORMAL);	    
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div/a[3]"), 7000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "Upload Configuration Template");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='myform']/label"								 , "Press the Browse... button to locate the file and then press the Upload button. When file upload is complete The file has been uploaded successfully message will be shown.");	  
	  WebElement fileSend = driver.findElement(By.name("uploadedfile"));
	  fileSend.sendKeys(filePath);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 5000); 
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Upload Configuration Template");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "The IP Phone template has been uploaded successfully.");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	  
	  // Verify upload
	  testFuncs.myDebugPrinting("Verify upload", enumsClass.logModes.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[1]/a[1]"), 5000);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/textarea"), 5000);
	  driver.findElement(By.xpath("/html/body/div[2]/div/textarea")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
	  driver.findElement(By.xpath("/html/body/div[2]/div/textarea")).sendKeys(Keys.chord(Keys.CONTROL, "c"));
	  Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	  String txt = (String) clipboard.getData(DataFlavor.stringFlavor);
	  testFuncs.myAssertTrue("SBC configuration file was not uploaded successfully !! \n" + txt, txt.contains(uploadStr));
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
  }

  // Restore SBC configuration
  private void restoreSBCproxyConfigurationTemplate(WebDriver driver, String strForEdit) throws UnsupportedFlavorException, IOException {
	  
	  // Restore default configuration
	  testFuncs.myDebugPrinting("Restore default configuration", enumsClass.logModes.NORMAL);	    
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div/a[4]"), 3000);	
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Reset the Proxy DHCP Options Configuration");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to reset the Proxy DHCP option template?");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	  
	  // Verify reset
	  testFuncs.myDebugPrinting("Verify reset", enumsClass.logModes.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div[1]/a[1]"), 5000);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/textarea"), 5000);
	  driver.findElement(By.xpath("/html/body/div[2]/div/textarea")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
	  driver.findElement(By.xpath("/html/body/div[2]/div/textarea")).sendKeys(Keys.chord(Keys.CONTROL, "c"));
	  Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	  String txt = (String) clipboard.getData(DataFlavor.stringFlavor);
	  testFuncs.myAssertTrue("SBC configuration file was not edited successfully !! \n" + txt, !txt.contains(strForEdit));
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
  }

  // Edit SBC configuration
  private void editSBCproxyConfigurationTemplate(WebDriver driver, String strForEdit) throws UnsupportedFlavorException, IOException {
	  
	  // Edit SBC template
	  testFuncs.myDebugPrinting("Edit SBC template", enumsClass.logModes.NORMAL);	    
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div/a[1]"), 5000);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/textarea"), 5000); 
	  testFuncs.mySendKeys(driver, By.xpath("/html/body/div[2]/div/textarea"), strForEdit,  2000);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
//	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update DHCP option template");
//	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "DHCP option template was updated successfully.");
//	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	    
	  // Verify edit
	  testFuncs.myDebugPrinting("Verify edit", enumsClass.logModes.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div/a[1]"), 5000);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/textarea"), 5000);
	  driver.findElement(By.xpath("/html/body/div[2]/div/textarea")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
	  driver.findElement(By.xpath("/html/body/div[2]/div/textarea")).sendKeys(Keys.chord(Keys.CONTROL, "c"));
	  Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	  String txt = (String) clipboard.getData(DataFlavor.stringFlavor);
	  testFuncs.myAssertTrue("SBC configuration file was not edited successfully !! \n" + txt, txt.contains(strForEdit));
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
//	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update DHCP option template");
//	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "DHCP option template was updated successfully.");
//	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
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
