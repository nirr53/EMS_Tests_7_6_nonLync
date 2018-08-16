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
* This test tests the Templates menu via Monitoring users (system+tenant)
* ----------------
* Tests:
* 	 - Login via a Monitoring user (system) and enter the Phone Templates menu.
* 	 1. Try to add a template
* 	 2. Try to edit a template
* 	 3. Try to delete a template
* 
* 	 - Logout, re-login via a Monitoring user (tenant) and enter the Phone Templates menu.
* 	 4. Try to add a template
* 	 5. Try to edit a template
* 	 6. Try to delete a template
* 
* 	 7. Logout, re-login as Administrator and delete the created Template
* 
* Results:
*   1-6. All the operations should be forbidden.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test90__Monitoring_system_tenant_templates {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test90__Monitoring_system_tenant_templates(browserTypes browser) {
	  
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
  public void Monitoring_system_tenant_Templates() throws Exception {
	  
	  Log.startTestCase(this.getClass().getName());
	  
	  // Set variables
	  String Id 			  = testFuncs.getId();
	  String tempName 		  = "my" + "420HD" + "Template_" + Id;
	  String tempDesc 		  = "my" + "420HD" + "desc" 	 + Id;
	  Map<String, String> map = new HashMap<String, String>();
	  map.put("isRegionDefault"		     ,  "false");
	  map.put("cloneFromtemplate"        ,  "");   
	  map.put("isDownloadSharedTemplates",  "false");
	   
	  // Login, enter the Phone Templates menu, add a Template and logout
	  testFuncs.myDebugPrinting("Login, enter the Phone Templates menu, add a Template and logout");
	  testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_TEMPLATES, "IP Phones Configuration Templates");		
	  testFuncs.addTemplate(driver, tempName, tempDesc, testVars.getNonDefTenant(0), "420HD", map);
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());

	  // Login via a Monitoring user (system) and enter the Phone Templates menu
	  testFuncs.myDebugPrinting("Login via a Monitoring user (system) and enter the Phone Templates menu");
	  testFuncs.login(driver, testVars.getMonitSysLoginData(enumsClass.loginData.USERNAME), testVars.getMonitSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_TEMPLATES, "IP Phones Configuration Templates");
		  
	  // Step 1 - Verify that Add-Template button is deactivated
	  testFuncs.myDebugPrinting("Step 1 - Verify that Add-Template button is deactivated");	
	  String addButton = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/buttton")).getAttribute("class");
	  testFuncs.myAssertTrue("Add Template is not deactivated !!", addButton.contains("not-active"));

	  // Step 2 - Verify that you cannot edit any Template
	  testFuncs.myDebugPrinting("Step 2 - Verify that you cannot edit any Template");	
	  testFuncs.myClick(driver, By.xpath("//*[@id='tenants1']/tbody/tr[1]/td[8]/div/buttton[1]")							   , 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[3]/div/div[3]/button"), 3000);	
	  testFuncs.searchStr(driver, "Unauthorized");		
	  testFuncs.searchStr(driver, "You do not have permission to modify this item");   
	  
	  // Step 3 - Verify that you cannot delete any Template
	  testFuncs.myDebugPrinting("Step 3 - Verify that you cannot delete any Template");	
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_TEMPLATES, "IP Phones Configuration Templates");
	  String idx = getIdx(tempName);
	  testFuncs.myClick(driver, By.xpath("//*[@id='tenants1']/tbody/tr[" + idx + "]/td[8]/div/buttton[2]"), 3000);	  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete Template");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the " + tempName + " IP Phone Model?");
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 10000);	  
	  testFuncs.searchStr(driver, "Unauthorized");		
	  testFuncs.searchStr(driver, "You do not have permission to modify this item"); 

	  // Logout, re-login via Monitoring user (tenant) and enter the Phone Templates menu
	  testFuncs.myDebugPrinting("Logout, re-login via Monitoring user (tenant) and enter the Phone Templates menu");
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	  testFuncs.login(driver, testVars.getMonitTenLoginData(enumsClass.loginData.USERNAME), testVars.getMonitTenLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_TEMPLATES, "IP Phones Configuration Templates");

	  // Step 4 - Verify that Add-Template button is deactivated
	  testFuncs.myDebugPrinting("Step 4 - Verify that Add-Template button is deactivated");	
	  addButton = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/buttton")).getAttribute("class");
	  testFuncs.myAssertTrue("Add Template is not deactivated !!", addButton.contains("not-active"));

	  // Step 5 - Verify that you cannot edit any Template
	  testFuncs.myDebugPrinting("Step 5 - Verify that you cannot edit any Template");	
	  testFuncs.myClick(driver, By.xpath("//*[@id='tenants1']/tbody/tr[1]/td[8]/div/buttton[1]")							   , 3000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[3]/div/div[3]/button"), 3000);	
	  testFuncs.searchStr(driver, "Unauthorized");		
	  testFuncs.searchStr(driver, "You do not have permission to modify this item");   
	  
	  // Step 6 - Verify that you cannot delete any Template
	  testFuncs.myDebugPrinting("Step 6 - Verify that you cannot delete any Template");
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_TEMPLATES, "IP Phones Configuration Templates");
	  idx = getIdx("NirTemplate445");
	  testFuncs.myDebugPrinting("idx - " + idx, enumsClass.logModes.MINOR);
	  testFuncs.myClick(driver, By.xpath("//*[@id='tenants1']/tbody/tr[" + idx + "]/td[8]/div/buttton[2]")							   , 3000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete Template");  
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	  testFuncs.searchStr(driver, "Unauthorized");		
	  testFuncs.searchStr(driver, "You do not have permission to modify this item"); 
	  
	  // Step 7 - Logout, re-login as Administrator and delete the created Template
	  testFuncs.myDebugPrinting("Step 7 - Logout, re-login as Administrator and delete the created Template");
	  testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_GEN_INFOR_LOGOUT, testVars.getMainPageStr());
	  testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	  testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONF_TEMPLATES, "IP Phones Configuration Templates");		
	  testFuncs.deleteTemplate(driver, tempName);
  }
  
  // Get index of template according to its name
  private String getIdx(String tempName) throws IOException {
	  
	  testFuncs.myDebugPrinting("tempName - " + tempName, enumsClass.logModes.MINOR);	  
	  
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
	  
	  return String.valueOf(i);
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