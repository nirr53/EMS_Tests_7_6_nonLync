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
* This test tests the Site configuration menu via Monitoring users (system)
* ----------------
* Tests:
* 	 - Login via Administrator user and enter the Site configuration menu
* 	 - Create a Site configuration value and Site-PH
* 	 - Logout and re-login via Monitoring user (system) and enter the Site configuration menu.
* 	 1. Try to add a new Site configuration value
*    2. Try to delete a site configuration value
*    3. Verify that Features menu is disabled
*    4. Verify that Actions menu is disabled
*    5. Verify that Add-Configuration-key button is disabled
* 	 6. Try to add a new Site Placeholder
*    7. Try to edit the created Site Placeholder
*    8. Try to delete the created Site Placeholder
*    9. Logout, re-login as Administrator, enter the Site configuration menu and delete the created Site configuration key and and PH 
* 
* Results:
* 	 1-8. Via monitoring, all actions should forbidden / not-active.
* 	   9. The user should be deleted successfully.
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test88__Monitoring_system_site_configuration {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test88__Monitoring_system_site_configuration(browserTypes browser) {
	  
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
  public void Monitoring_system_Site_configuration_placeholders() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String Id = testFuncs.getId();
	String siteCfgKeyName  = "user_name"   + Id;
	String siteCfgKeyValue = "userValue"   + Id;
	String sitePhName      = "sitePhName"  + Id;
	String sitePhValue     = "sitePhValue" + Id;
	String tenant 		   = testVars.getDefTenant();
	String sitePHSite	   = testVars.getDefSite() + " [" + testVars.getDefSite() + "] / " + testVars.getDefTenant();
	String sitePHTenant	   = testVars.getDefTenant();
	
	// Enter the Site Configuration menu, add new site CFG key and new Site PH and logout
	testFuncs.myDebugPrinting("Enter the Site Configuration menu, add new site CFG key and new Site PH and logout");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SITE_CONFIGURATION, "Site Configuration");
	testFuncs.selectSite(driver, sitePHSite);
    testFuncs.addNewSiteCfgKey(driver, siteCfgKeyName, siteCfgKeyValue, tenant, sitePHSite);
	testFuncs.addSitePH(driver, sitePhName, sitePhValue, sitePHSite, sitePHTenant);
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());

	//  Login via Monitoring user (system) and enter the Site configuration menu
	testFuncs.myDebugPrinting("Login via Monitoring user (system) and enter the Site configuration menu");
	testFuncs.login(driver, testVars.getMonitSysLoginData(enumsClass.loginData.USERNAME), testVars.getMonitSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SITE_CONFIGURATION, "Site Configuration");
	testFuncs.selectSite(driver, sitePHSite);
	
	//	Step 1 - Verify that Add Site-Configuration-key is disabled
	testFuncs.myDebugPrinting("Step 1 - Verify that Add Site-Configuration-key is disabled");	
	String addSiteCfgKey = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[3]/a")).getAttribute("class");
	testFuncs.myAssertTrue("Add Site-configuration-key button is active !!", addSiteCfgKey.contains("not-active"));
	
	//	Step 2 - Verify that Delete Site-Configuration-key is disabled
	testFuncs.myDebugPrinting("Step 2 - Verify that Delete Site-Configuration-key is disabled");
	testFuncs.myClick(driver, By.xpath("//*[@id='table_keys']/tbody/tr[1]/td[3]/div/a/i"), 3000);
	testFuncs.verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Delete configuration setting");
	testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000); 
	testFuncs.searchStr(driver, "Unauthorized");
	testFuncs.searchStr(driver, "You do not have permission to modify this item"); 
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SITE_CONFIGURATION, "Site Configuration");
	
	// Step 3 - Verify that Features menu is disabled
	testFuncs.myDebugPrinting("Step 3 - Verify that Features menu is disabled");
	String features = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[3]/a")).getAttribute("class");
	testFuncs.myAssertTrue("Features menu is enbled !!", features.contains("not-active"));

	// Step 4 - Verify that Actions menu is disabled
	testFuncs.myDebugPrinting("Step 4 - Verify that Actions menu is disabled");
	String actions = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[5]/div[1]/button")).getAttribute("class");
	testFuncs.myAssertTrue("Actions menu is enbled !!", actions.contains("not-active"));
	
	// Step 5 - Verify that Add-Configuration-key button is disabled
	testFuncs.myDebugPrinting("Step 5 - Verify that Add-Configuration-key button is disabled");
	String addCfgKey = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[3]/a")).getAttribute("class");
	testFuncs.myAssertTrue("Add configuration-key button is enbled !!", addCfgKey.contains("not-active"));
	 	
	// Step 6 - Verify that Add Site-PH is disabled
	testFuncs.myDebugPrinting("Step 6 - Verify that Add Site-PH is disabled");	
	String addSitePHKey = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[4]/div[2]/div/span[2]/a")).getAttribute("class");
	testFuncs.myAssertTrue("Add Site-PH button is active !!", addSitePHKey.contains("not-active"));
	
	// Step 7 - Verify that Edit Site-PH is disabled
	testFuncs.myDebugPrinting("Step 7 - Verify that Edit Site-PH is disabled");	
	testFuncs.selectSite(driver, sitePHSite);
	String editeSitePHKey = driver.findElement(By.xpath("//*[@id='sites1']/tbody[1]/tr[1]/td[7]/button[1]")).getAttribute("class");
	testFuncs.myAssertTrue("Edit Site-PH button is active !!", editeSitePHKey.contains("not-active"));
	
	// Step 8 - Verify that Delete Site-PH is disabled
	testFuncs.myDebugPrinting("Step 8 - Verify that Delete Site-PH is disabled");	
	String deleteSitePHKey = driver.findElement(By.xpath("//*[@id='sites1']/tbody[1]/tr[1]/td[7]/button[2]")).getAttribute("class");
	testFuncs.myAssertTrue("Delete Site-PH button is active !!", deleteSitePHKey.contains("not-active"));
	
	// Step 9 - Logout, re-login as Administrator, enter the Site configuration menu and delete the created Site configuration key and and PH 
	testFuncs.myDebugPrinting("Step 9 - Logout, re-login as Administrator, enter the Site configuration menu and delete the created Site configuration key and and PH");	
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SITE_CONFIGURATION, "Site Configuration");
	testFuncs.selectSite(driver, sitePHSite);
	testFuncs.deleteSiteCfgKey(driver, siteCfgKeyName, siteCfgKeyValue, tenant, sitePHSite, testVars.getDefSite());
	testFuncs.deleteSitePH(driver, sitePhName, sitePhValue, testVars.getDefSite() + " [" + testVars.getDefSite() + "]");
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
