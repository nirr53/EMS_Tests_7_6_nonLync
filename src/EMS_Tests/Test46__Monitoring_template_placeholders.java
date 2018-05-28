package EMS_Tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;import EMS_Tests.enumsClass.browserTypes;

/**
* ----------------
* This test tests the Template placeholders menu
* ----------------
* Tests:
* 	 - Login via Monitoring user (system) and enter the Add new template placeholder menu
* 	 1. Try to add a Template PH via a Monitoring user
* 	 2. Try to edit a Template PH via a Monitoring user
* 	 3. Try to delete a Template PH via a Monitoring user
* 	 4. Verify that Show placeholders button is still active.
* 
* 	 - Login via Monitoring user (Tenant) and enter the Add new template placeholder menu
* 	 5. Try to add a Template PH via a Monitoring user
* 	 6. Try to edit a Template PH via a Monitoring user
* 	 7. Try to delete a Template PH via a Monitoring user
* 	 8. Verify that Show placeholders button is still active.
* 
* Results:
*  	 1-3. Add, edit and delete should be disabled while we we login via a Monitoring user.
*  	 4.   The Show placeholders button should still be active
*  	 5-7. Add, edit and delete should be disabled while we we login via a Monitoring user.
*  	 8.   The Show placeholders button should still be active
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test46__Monitoring_template_placeholders {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test46__Monitoring_template_placeholders(browserTypes browser) {
	  
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
  public void Monitoring_Template_placeholders_menu() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	 
    // Login via Monitoring user (system) and enter the Add new template placeholder menu
	testFuncs.myDebugPrinting("Login via Monitoring user (system) and enter the Add new template placeholder menu");
	testFuncs.login(driver, testVars.getMonitUsername(), testVars.getMonitPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates_placeholders", "Template Placeholders");
	
	// Step 1 - Try to add a Template PH via a Monitoring user
	testFuncs.myDebugPrinting("Step 1 - Try to add a Template PH via a Monitoring user");
	addTemplatePHMonitoring(driver);

	// Step 2 - Try to edit a Template PH via a Monitoring user
	testFuncs.myDebugPrinting("Step 2 - Try to edit a Template PH via a Monitoring user");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates_placeholders", "Template Placeholders");
	editTemplatePHMonitoring(driver);
	
	// Step 3 - Try to delete a Template PH via a Monitoring user
	testFuncs.myDebugPrinting("Step 3 - Try to delete a Template PH via a Monitoring user");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates_placeholders", "Template Placeholders");
	deleteTemplatePHMonitoring(driver);
	
    // Step 4 - Verify that Show placeholders button is still active
 	testFuncs.myDebugPrinting("Step 4 - Verify that Show placeholders button is still active");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates_placeholders", "Template Placeholders");
 	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[8]/div[2]/table/tbody/tr[1]/td/table/tbody/tr[2]/td/div/a[2]"), 3000);
	ArrayList<?> tabs = new ArrayList<Object> (driver.getWindowHandles());
	driver.switchTo().window((String) tabs.get(1));
	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[8]/div[1]/h3/span"		   					  , "Templates Place Holders");
	testFuncs.verifyStrByXpath(driver, "//*[@id='phs1']/thead/tr/th[1]/b", "Template Model");
	testFuncs.verifyStrByXpath(driver, "//*[@id='phs1']/thead/tr/th[2]/b", "Placeholder");
	testFuncs.verifyStrByXpath(driver, "//*[@id='phs1']/thead/tr/th[3]/b", "IPP Parameter");
	testFuncs.verifyStrByXpath(driver, "//*[@id='phs1']/thead/tr/th[4]/b", "Description");
	
    // Switch back to main frame, logout and re-login via Monitoring user (tenant) and enter the Add new template placeholder menu
	testFuncs.myDebugPrinting("Switch back to main frame, logout and re-login via Monitoring user (tenant) and enter the Add new template placeholder menu");
	driver.switchTo().window((String) tabs.get(0));
	testFuncs.enterMenu(driver, "General_Informatiom_logout", testVars.getMainPageStr());
	testFuncs.login(driver, testVars.getMonitTenUsername(), testVars.getMonitTenPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates_placeholders", "Template Placeholders");
	
	// Step 5 - Try to add a Template PH via a Monitoring user
	testFuncs.myDebugPrinting("Step 5 - Try to add a Template PH via a Monitoring user");
	addTemplatePHMonitoring(driver);

	// Step 6 - Try to edit a Template PH via a Monitoring user
	testFuncs.myDebugPrinting("Step 6 - Try to edit a Template PH via a Monitoring user");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates_placeholders", "Template Placeholders");
	editTemplatePHMonitoring(driver);
	
	// Step 7 - Try to delete a Template PH via a Monitoring user
	testFuncs.myDebugPrinting("Step 7 - Try to delete a Template PH via a Monitoring user");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates_placeholders", "Template Placeholders");
	deleteTemplatePHMonitoring(driver);
	
    // Step 8 - Verify that Show placeholders button is still active
 	testFuncs.myDebugPrinting("Step 8 - Verify that Show placeholders button is still active");
	testFuncs.enterMenu(driver, "Setup_Phone_conf_templates_placeholders", "Template Placeholders");
 	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[8]/div[2]/table/tbody/tr[1]/td/table/tbody/tr[2]/td/div/a[2]"), 3000);
	ArrayList<?> tabs2 = new ArrayList<Object> (driver.getWindowHandles());
	driver.switchTo().window((String) tabs2.get(1));
	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[8]/div[1]/h3/span"		   					  , "Templates Place Holders");
	testFuncs.verifyStrByXpath(driver, "//*[@id='phs1']/thead/tr/th[1]/b", "Template Model");
	testFuncs.verifyStrByXpath(driver, "//*[@id='phs1']/thead/tr/th[2]/b", "Placeholder");
	testFuncs.verifyStrByXpath(driver, "//*[@id='phs1']/thead/tr/th[3]/b", "IPP Parameter");
	testFuncs.verifyStrByXpath(driver, "//*[@id='phs1']/thead/tr/th[4]/b", "Description");
  }
  
  // Try to delete a Template PH via a Monitoring user
  private void deleteTemplatePHMonitoring(WebDriver driver) {
	
	  testFuncs.myDebugPrinting("Try to delete a Template PH via a Monitoring user", enumsClass.logModes.NORMAL);  
	  String deleteButton = driver.findElement(By.xpath("//*[@id='tenants1']/tbody/tr[1]/td[6]/a[2]")).getAttribute("class");
	  testFuncs.myAssertTrue("Delete button is not deactivated !!\ndeleteButton - " + deleteButton, deleteButton.contains("not-active"));
  }

  // Try to edit a Template PH via a Monitoring user
  private void editTemplatePHMonitoring(WebDriver driver) {
	  
	  testFuncs.myDebugPrinting("Try to edit a Template PH via a Monitoring user", enumsClass.logModes.NORMAL);  
	  String editButton = driver.findElement(By.xpath("//*[@id='tenants1']/tbody/tr[1]/td[6]/a[1]")).getAttribute("class");
	  testFuncs.myAssertTrue("Edit button is not deactivated !!\neditButton - " + editButton, editButton.contains("not-active"));
  }
  
  // Try to add a Template PH via a Monitoring user
  private void addTemplatePHMonitoring(WebDriver driver) {
	  
	  testFuncs.myDebugPrinting("Try to add a Template PH via a Monitoring user", enumsClass.logModes.NORMAL);
	  String addButton = driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[8]/div[2]/table/tbody/tr[1]/td/table/tbody/tr[1]/td[2]/a")).getAttribute("class");
	  testFuncs.myAssertTrue("Add button is not deactivated !!\naddButton - " + addButton, addButton.contains("not-active"));
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
