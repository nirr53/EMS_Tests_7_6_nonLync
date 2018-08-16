package EMS_Tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
* This test tests the Site placeholder
* ----------------
* Tests:
* 	 - Enter Site configuration menu.
* 	 1. Add a new Site Placeholder
*    2. Edit the Site Tenant Placeholder
*    3. Delete the Site Tenant Placeholder
* 
* Results:
* 	 1. The Site Placeholder should be added successfully
* 	 2. The Site Placeholder should be edited successfully
* 	 3. The Site Placeholder should be deleted successfully
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test87__site_placeholders {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test87__site_placeholders(browserTypes browser) {
	  
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
  public void Site_placeholders() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String Id 	          = testFuncs.getId();
	String sitePhName     = "sitePhName"     + Id;
	String sitePhValue    = "sitePhValue"    + Id;
	String sitePhNewValue = "sitePhNewValue" + Id;
	String sitePHSite	  = testVars.getDefSite() + " [" + testVars.getDefSite() + "] / " + testVars.getDefTenant();
	String sitePHTenant	  = testVars.getDefTenant();
	
	testFuncs.myDebugPrinting("Enter the Sites configuration menu");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_SITE_CONFIGURATION, "Site Configuration");
	 	
	// Step 1 - Add a new Site Placeholder
	testFuncs.myDebugPrinting("Step 1 - Add a new Site Placeholder");
	testFuncs.addSitePH(driver, sitePhName, sitePhValue, sitePHSite, sitePHTenant);
	
	// Step 2 - Edit the created Site Placeholder
	testFuncs.myDebugPrinting("Step 2 - Edit the created Site Placeholder");
	editSitePH(driver, sitePhName, sitePhNewValue);
	
	// Step 3 - Delete the created Site Placeholder
	testFuncs.myDebugPrinting("Step 3 - Delete the created Site Placeholder");
	testFuncs.deleteSitePH(driver, sitePhName, sitePhValue, testVars.getDefSite() + " [" + testVars.getDefSite() + "]");
  }
  
  
  /**
  *  Edit a Site-PH with given variables
  *  @param driver         - given element
  *  @param sitePhName     - given Site-ph name
  *  @param sitePhNewValue - new given Site-ph value
  *  @throws IOException 
  */
  public void editSitePH(WebDriver driver, String sitePhName, String sitePhNewValue) throws IOException {
	  
	  // Get idx for edit
	  testFuncs.myDebugPrinting("Get idx for edit", enumsClass.logModes.NORMAL);  
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='sites1-filtering']"), " " , 6000);
	  BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
	  String l = null;
	  int i = 1;
	  while ((l = r.readLine()) != null) {
			
		  testFuncs.myDebugPrinting("i - " + i + " " + l, enumsClass.logModes.DEBUG);	
		  if (l.contains(sitePhName)) {
						  
			  testFuncs.myDebugPrinting("i - " + i, enumsClass.logModes.MINOR);	
			  break;
		  }
			
		  if (l.contains(" %ITCS_" )) {
					
			  ++i;		
		  }
	  }
		
	//	// Check if the current user is "Monitoring" if so - edit should fail
	//	myDebugPrinting("Check if the current user is \"Monitoring\" if so - edit should fail", enumsClass.logModes.NORMAL);  
	//	if (sitePhName.contains("tenMonitPhName")) {
	//			
	//		String editButton = driver.findElement(By.xpath("//*[@id='tenants1']/tbody[1]/tr[" + i + "]/td[6]/button[1]")).getAttribute("class");
	//		myAssertTrue("Edit button is not deactivated !!\neditButton - " + editButton, editButton.contains("not-active"));
	//		return;
	//	}
		    
	  testFuncs.myDebugPrinting("xpath - " + "//*[@id='sites1']/tbody[1]/tr[" + i + "]/td[7]/button[1]", enumsClass.logModes.DEBUG);  
	  testFuncs.myClick(driver, By.xpath("//*[@id='sites1']/tbody[1]/tr[" + i + "]/td[7]/button[1]"), 5000);
				
	  // Fill data
	  testFuncs.myDebugPrinting("Fill data", enumsClass.logModes.MINOR);  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "Edit placeholder");
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='ph_value']"), sitePhNewValue, 2000);
	  testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 5000);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update Placeholder.");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='modalContentId']", "Update placeholder successfully: " + sitePhName);
	  testFuncs.myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
				
	  // Verify the create
	  testFuncs.myDebugPrinting("Verify the create", enumsClass.logModes.MINOR);  
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='sites1-filtering']"), sitePhName , 6000);
	  testFuncs.searchStr(driver, "%ITCS_" + sitePhName + "%");
	  testFuncs.searchStr(driver, sitePhNewValue);
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
