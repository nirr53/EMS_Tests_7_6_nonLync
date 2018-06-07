package EMS_Tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
* This test tests the Templates configuration features
* ----------------
* Tests:
* 	 - Login, enter the Phone Templates menu, add a Template and select the create template
* 	 1. Add Daylight value
* 	 2. Add Pin-value (enabled + disabled)
* 	 3. Add Telnet-Access (enabled + disabled)
*    4. Delete the created template	
* 
* Results:
* 	 1-3. Values should be added successfully. 
* 	   4. template should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test96__template_features {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test96__template_features(browserTypes browser) {
	  
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
  public void Templates_actions_features() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
    Map<String, String> map = new HashMap<String, String>();   
	String Id 			  = testFuncs.getId();
	String site   		  = testVars.getDefSite();
	String tenant 		  = testVars.getDefTenant();
	String telnetPrefix   = "management/telnet/enabled";
	String pinPrefix 	  = "system/pin_lock/enabled";
    map.put("isRegionDefault"		   ,  "false");
    map.put("isDownloadSharedTemplates",  "false");
  	map.put("cloneFromtemplate"		   , "Audiocodes_420HD_LYNC");

    // Login, enter the Phone Templates menu, add a Template and select the create template
	testFuncs.myDebugPrinting("Login, enter the Phone Templates menu, add a Template and select the create template");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_TEMPLATES, "IP Phones Configuration Templates");	
  	testFuncs.addTemplate(driver, "my" + "420HD" + "Template_" + Id, "my" + "420HD" + "desc", testVars.getNonDefTenant(0), "420HD", map);
  	pressEditOfCreatedTemplate(driver, "my" + "420HD" + "Template_" + Id);
  	
	// Step 1 - Add Daylight value
	testFuncs.myDebugPrinting("Step 1 - Add Daylight value");
	addDayLightValue(driver, site, tenant);

	// Step 2 - Add Telnet-Access (enabled + disabled)
	testFuncs.myDebugPrinting("Step 2 - Add Telnet-Access (enabled + disabled)");
	addTelnetAccessValue(driver, telnetPrefix, false);
	addTelnetAccessValue(driver, telnetPrefix, true);
	
	// Nir 1\5\18 VI 152905
	deleteAllConfValues(driver , telnetPrefix);
	
	// Step 3 - Add Pin-value (enabled + disabled)
	testFuncs.myDebugPrinting("Step 3 - Add Pin-value (enabled + disabled)");
	addPinLockValue(driver, pinPrefix, false);
	addPinLockValue(driver, pinPrefix, true);
	deleteAllConfValues(driver , pinPrefix);

	// Step 4 - Delete the created template  		
  	testFuncs.myDebugPrinting("Step 4 -  Delete the created template");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_TEMPLATES, "IP Phones Configuration Templates");	
	testFuncs.deleteTemplate(driver, "my" + "420HD" + "Template_" + Id);
  }
  
  // Add PIN-lock value
  private void addPinLockValue(WebDriver driver, String prefix, Boolean isLock) {
	  
	  // Add PIN lock value
	  testFuncs.myDebugPrinting("Add PIN lock value", enumsClass.logModes.NORMAL);  
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[4]/div/div[2]/div[1]/div[5]/div[1]/button"), 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='pinlock']")															   , 3000);	
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Pin Lock");
	  if (isLock) {
		  
		  testFuncs.myDebugPrinting("isLock - TRUE", enumsClass.logModes.MINOR);
		  testFuncs.myClick(driver, By.xpath("//*[@id='system_pin_lock_enabled']"), 3000);
	  }  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Save Configuration");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Successfully to save the template new configuration settings");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);	  

	  // Verify create
	  testFuncs.myDebugPrinting("Verify create", enumsClass.logModes.MINOR);
	  testFuncs.searchStr(driver, prefix);
	  String valueForsearch = "0";
	  if (isLock) { valueForsearch = "1"; }
	  testFuncs.searchStr(driver, valueForsearch);
  }
  
  // Press the Edit button of the created Template
  private void pressEditOfCreatedTemplate(WebDriver driver, String tempName) throws IOException {
	
	  // Get idx
	  BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
	  String l = null;
	  int i = 1;
	  while ((l = r.readLine()) != null) {
		  
		  testFuncs.myDebugPrinting("i - " + i + " " + l, enumsClass.logModes.DEBUG);
		  if (l.contains(tempName)) {
			  
			  testFuncs.myDebugPrinting("i - " + i, enumsClass.logModes.MINOR);
			  break;
		  }
		  if (l.contains("Edit" )) {
			  
			  ++i;
		  }
	  } 
	  testFuncs.myClick(driver, By.xpath("//*[@id='tenants1']/tbody/tr[" + i + "]/td[8]/div/buttton[1]"), 3000);		  	  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3"        , "IP Phone " + tempName + " Configuration Template");
  }
  
  // Add Daylight values
  private void addDayLightValue(WebDriver driver, String site, String tenant) {
	  
	  // Add daylight values
	  testFuncs.myDebugPrinting("Add daylight values", enumsClass.logModes.NORMAL);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[4]/div/div[2]/div[1]/div[5]/div[1]/button"), 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='daylight']")															 , 3000);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Daylight Savings Time");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 9000);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Save Configuration");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Successfully to save the template new configuration settings");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);	  
	  
	  // Verify that all the values were added
	  testFuncs.myDebugPrinting("Verify that all the values were added", enumsClass.logModes.NORMAL);
	  String dayLightValues[] = {"system/daylight_saving/activate"			  , "system/daylight_saving/end_date/day" 		   ,
			  					 "system/daylight_saving/end_date/day_of_week", "system/daylight_saving/end_date/hour"		   ,
			  					 "system/daylight_saving/end_date/month"	  , "system/daylight_saving/end_date/week"		   ,
			  					 "system/daylight_saving/mode"				  , "system/daylight_saving/offset"       		   ,
			  					 "system/daylight_saving/start_date/day"      , "system/daylight_saving/start_date/day_of_week",
			  					 "system/daylight_saving/start_date/hour"	  , "system/daylight_saving/start_date/month"	   ,
			  					 "system/daylight_saving/start_date/week"};
	  
	  
	  
	  int dayLightValuesNumber = dayLightValues.length;
	  for (int i = 0; i < dayLightValuesNumber; ++i) {
		  
		  testFuncs.myDebugPrinting(i + ". The searched value is: " + dayLightValues[i], enumsClass.logModes.MINOR);
		  testFuncs.searchStr(driver, dayLightValues[i]);	  
		  testFuncs.myWait(3000); 
	  }  
  }
  
  // Add Telnet-Access value
  private void addTelnetAccessValue(WebDriver driver, String prefix, boolean isTelnetAccess) {
	  
	  // Add Telnet-Access value
	  testFuncs.myDebugPrinting("Add Telnet-Access value", enumsClass.logModes.NORMAL);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[4]/div/div[2]/div[1]/div[5]/div[1]/button"), 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='telnet']")															   , 3000);	
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Activate Telnet access");
	  if (isTelnetAccess) {
		  
		  testFuncs.myDebugPrinting("isTelnetAccess - TRUE", enumsClass.logModes.MINOR);
		  testFuncs.myClick(driver, By.xpath("//*[@id='management_telnet_enabled']"), 3000);
	  }
	  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Save Configuration");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Successfully to save the template new configuration settings");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);	
  
	  // Verify create
	  testFuncs.myDebugPrinting("Verify create", enumsClass.logModes.MINOR);
	  testFuncs.searchStr(driver, prefix);
	  String valueForsearch = "0";
	  if (isTelnetAccess) { valueForsearch = "1"; }
	  testFuncs.searchStr(driver, prefix + " " + valueForsearch);  	  
  }
  
  // Delete all configuration values
  private void deleteAllConfValues(WebDriver driver, String prefix) {

	  // Delete all Configuration values
	  testFuncs.myDebugPrinting("Delete all Configuration values", enumsClass.logModes.NORMAL);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[4]/div/div[2]/div[1]/div[5]/div[2]/button")    , 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[4]/div/div[2]/div[1]/div[5]/div[2]/ul/li[1]/a"), 3000);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Delete configuration settings");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Are you sure you want to delete all configuration settings and save empty content?");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);	  
	  
//	   Nir - bug 7.4.245 7.4.17 (VI 145708)
	   testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Save Configuration");
	   testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Failed to save the template new configuration settings");
	   testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);	

	  // verify delete
	  testFuncs.myDebugPrinting("Verify delete", enumsClass.logModes.MINOR);
	  String txt = driver.findElement(By.tagName("body")).getText();
	  testFuncs.myAssertTrue("All tenant configuration values were not delete successfully !!\ntxt - " + txt, !txt.contains(prefix));
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
