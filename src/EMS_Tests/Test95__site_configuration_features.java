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
* This test tests the Site configuration features
* ----------------
* Tests:
* 	 - Enter Site configuration menu.
* 	 1. Add Daylight value
* 	 2. Add Pin-value (enabled + disabled)
* 	 3. Add Telnet-Access (enabled + disabled)
* 	 
* Results:
* 	 1-3. Values should be added successfully. 
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test95__site_configuration_features {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test95__site_configuration_features(String browser) {
	  
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
  public void Site_configuration_actions_features() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String site   		  = testVars.getDefSite();
	String tenant 		  = testVars.getDefTenant();
	String daylightPrefix = "system/daylight_saving";
	String pinPrefix 	  = "system/pin_lock/enabled";
	String telnetPrefix   = "management/telnet/enabled";
	String sitePHSite	  = site + " [" + site + "] / " + tenant;

	// Login and enter the View-Sites menu
	testFuncs.myDebugPrinting("Enter the Add new region placeholders menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Site_configuration", "Site Configuration");
	testFuncs.selectSite(driver, sitePHSite);
	
	// Step 1 - Add Daylight value
	testFuncs.myDebugPrinting("Step 1 - Add Daylight value");	
	addDayLightValue(driver, site, tenant);
	deleteAllConfValues(driver, site, tenant, daylightPrefix);

	// Step 2 - Add Telnet-Access (enabled + disabled)
	testFuncs.myDebugPrinting("Step 2 - Add Telnet-Access (enabled + disabled)");
	addTelnetAccessValue(driver, site, tenant, false);
	addTelnetAccessValue(driver, site, tenant, true);
	deleteAllConfValues(driver , site, tenant, telnetPrefix);
	
	// Step 3 - Add Pin-value (enabled + disabled)
	testFuncs.myDebugPrinting("Step 3 - Add Pin-value (enabled + disabled)");
	addPinLockValue(driver, site, tenant, false);
	addPinLockValue(driver, site, tenant, true);
	deleteAllConfValues(driver , site, tenant, pinPrefix);
  }
  
  // Add Telnet-Access value
  private void addTelnetAccessValue(WebDriver driver2, String site, String tenant, boolean isTelnetAccess) {
	  
	  // Add Telnet-Access value
	  testFuncs.myDebugPrinting("Add Telnet-Access value", testVars.logerVars.NORMAL);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[5]/div[1]/button"), 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='telnet']")															   , 3000);	
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Activate Telnet access");
	  if (isTelnetAccess) {
		  
		  testFuncs.myDebugPrinting("isTelnetAccess - TRUE", testVars.logerVars.MINOR);
		  testFuncs.myClick(driver, By.xpath("//*[@id='management_telnet_enabled']"), 3000);
	  }
	  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Save Configuration ( " + site + " )");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Site configuration was saved successfully.");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);

	  
	  // Verify create
	  testFuncs.myDebugPrinting("Verify create", testVars.logerVars.MINOR);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='table_keys']/tbody/tr/td[1]"  , "management/telnet/enabled");
	  String valueForsearch = "0";
	  if (isTelnetAccess) { valueForsearch = "1"; }
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='table_keys']/tbody/tr/td[2]"  , valueForsearch);  	  
  }

  // Add PIN-lock value
  private void addPinLockValue(WebDriver driver, String site, String tenant, Boolean isLock) {
	  
	  // Add PIN lock value
	  testFuncs.myDebugPrinting("Add PIN lock value", testVars.logerVars.NORMAL);  
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[5]/div[1]/button"), 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='pinlock']")															   , 3000);	
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Pin Lock");
	  if (isLock) {
		  
		  testFuncs.myDebugPrinting("isLock - TRUE", testVars.logerVars.MINOR);
		  testFuncs.myClick(driver, By.xpath("//*[@id='system_pin_lock_enabled']"), 3000);
	  }  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Save Configuration ( " + site + " )");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Site configuration was saved successfully.");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);

	  // Verify create
	  testFuncs.myDebugPrinting("Verify create", testVars.logerVars.MINOR);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='table_keys']/tbody/tr/td[1]"  , "system/pin_lock/enabled");
	  String valueForsearch = "0";
	  if (isLock) { valueForsearch = "1"; }
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='table_keys']/tbody/tr/td[2]"  , valueForsearch);  
  }

  // Delete all configuration values
  private void deleteAllConfValues(WebDriver driver, String site, String tenant, String deleteValuePrefix) {

	  // Delete all Configuration values
	  testFuncs.myDebugPrinting("Delete all Configuration values", testVars.logerVars.NORMAL);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[5]/div[2]/button")    , 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[5]/div[2]/ul/li[1]/a"), 3000);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Delete configuration settings");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Are you sure you want to delete all configuration settings and save empty content?");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Save Configuration ( " + site + " )");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Site configuration was saved successfully.");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);

	  // verify delete
	  testFuncs.myDebugPrinting("Verify delete", testVars.logerVars.MINOR);
	  String txt = driver.findElement(By.tagName("body")).getText();
	  testFuncs.myAssertTrue("All Site configuration values were not delete successfully !!\ntxt - " + txt, !txt.contains(deleteValuePrefix));
  }

  // Add Daylight values
  private void addDayLightValue(WebDriver driver, String site, String tenant) {
	  
	  // Add daylight values
	  testFuncs.myDebugPrinting("Add daylight values", testVars.logerVars.NORMAL);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[5]/div[1]/button"), 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='daylight']")															 , 3000);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Daylight Savings Time");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Save Configuration ( " + site + " [" + site + "] / " + tenant + " )");
	  testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Site configuration was saved successfully.");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);	  
	  
	  // Verify that all the values were added
	  testFuncs.myDebugPrinting("Verify that all the values were added", testVars.logerVars.NORMAL);
	  String dayLightValues[] = {"system/daylight_saving/activate"			  , "system/daylight_saving/end_date/day" 		   ,
			  					 "system/daylight_saving/end_date/day_of_week", "system/daylight_saving/end_date/hour"		   ,
			  					 "system/daylight_saving/end_date/month"	  , "system/daylight_saving/end_date/week"		   ,
			  					 "system/daylight_saving/mode"				  , "system/daylight_saving/offset"       		   ,
			  					 "system/daylight_saving/start_date/day"      , "system/daylight_saving/start_date/day_of_week",
			  					 "system/daylight_saving/start_date/hour"	  , "system/daylight_saving/start_date/month"	   ,
			  					 "system/daylight_saving/start_date/week"};
	  
	  
	  
	  int dayLightValuesNumber = dayLightValues.length;
	  for (int i = 0; i < dayLightValuesNumber; ++i) {
		  
		  testFuncs.myDebugPrinting(i + ". The searched value is: " + dayLightValues[i], testVars.logerVars.MINOR);
		  testFuncs.searchStr(driver, dayLightValues[i]);	  
		  testFuncs.myWait(3000); 
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
