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
* This test tests the system license of the  permitted length of Location field during a user create.
* ----------------
* Tests:
*    - Login the system
*    1. Enter the License menu and check its headers.
*  
*  Results:
*    1. License menu should be detected. All its headers should appear.
*  
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test75__system_licsense {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test75__system_licsense(browserTypes browser) {
	  
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
  public void System_license() throws Exception {
	  
	  Log.startTestCase(this.getClass().getName());
	  
	  // Login and enter the System license
	  testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "https://", this.usedBrowser);
	  testFuncs.enterMenu(driver, "Setup_System_license", "License Properties");
	  
	  // Check headers
	  testFuncs.myDebugPrinting("Check headers");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div/table/thead/tr/th[2]", "Property");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div/table/thead/tr/th[3]", "Value");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/div/table/thead/tr/th[4]", "Description");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='license_prop_body']/tr[1]/td[2]"				   						   , "Status");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='license_prop_body']/tr[2]/td[2]"				   						   , "Days Left");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='license_prop_body']/tr[3]/td[2]"				   						   , "Number of devices");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='license_prop_body']/tr[1]/td[3]"				   						   , "Enable");
	  String numDays = driver.findElement(By.xpath("//*[@id='license_prop_body']/tr[2]/td[3]")).getText();
	  testFuncs.myDebugPrinting("Number of days - " + numDays, enumsClass.logModes.MINOR);
	  testFuncs.verifyStrByXpath(driver, "//*[@id='license_prop_body']/tr[1]/td[4]"				   						   , "License status");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='license_prop_body']/tr[2]/td[4]"				   						   , "Days left until license expires");
	  testFuncs.verifyStrByXpath(driver, "//*[@id='license_prop_body']/tr[3]/td[4]"				   						   , "Total number of devices");	  
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
