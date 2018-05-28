package EMS_Tests;

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
* This test tests the main-page links
* ----------------
* Tests:
* 	 1. Press the "Facbook" icon.
* 	 2. Press the "Twitter" icon.
* 	 3. Press the "Youtube" icon.
* 	 4. Press the "Linkedin" icon.
* 	 5. Press the "Google_plus" icon.
* 
* Results:
*    1-5. All links should work.
*    
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test155__mainpage_links {
	
  private WebDriver 	driver;
  private browserTypes  usedBrowser;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test155__mainpage_links(browserTypes browser) {
	  
	  System.out.println("Browser - "  + browser);
	  this.usedBrowser = browser;
  }
  
  //Define each browser as a parameter
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
  public void Mainpage_links() throws Exception {
	
	Log.startTestCase(this.getClass().getName());
	
	// Log main page
    driver.get("https://" + testVars.getUrl());
    testFuncs. myWait(5000); 	
    testFuncs.searchStr(driver, testVars.getMainPageStr());  
	  
    // Step 1 - Press the Facebook Icon
	testFuncs.myDebugPrinting("Step 1 - Press the Facebook Icon");
    pressIcon(" /html/body/div/div[3]/ul/li[1]/a", "https://www.facebook.com/audiocodes");

    // Step 2 - Press the Twitter Icon
	testFuncs.myDebugPrinting("Step 2 - Press the Twitter Icon");
    pressIcon(" /html/body/div/div[3]/ul/li[2]/a", "https://twitter.com/audiocodes");
    
    // Step 3 - Press the Youtube Icon
	testFuncs.myDebugPrinting("Step 3 - Press the Youtube Icon");
    pressIcon(" /html/body/div/div[3]/ul/li[3]/a", "https://www.youtube.com/channel/UCZFOqdMjlnmqmiwoSmHXAAg");
    
    // Step 4 - Press the Linkedin Icon
	testFuncs.myDebugPrinting("Step 4 - Press the Linkedin Icon");
    pressIcon(" /html/body/div/div[3]/ul/li[4]/a", "https://www.linkedin.com/company/audiocodes");
    
    // Step 5 - Press the Google plus Icon
	testFuncs.myDebugPrinting("Step 5 - Press the Google plus Icon");
    pressIcon(" /html/body/div/div[3]/ul/li[5]/a", "https://plus.google.com/117386317157355405525");
  }

  // Press the Audiocodes product button
  private void pressIcon(String xpath, String searchedUrl) {
	
      String winHandleBefore = driver.getWindowHandle();
      
      // Press the icon
      testFuncs.myDebugPrinting("Press the icon", enumsClass.logModes.NORMAL);
	  testFuncs.myClick(driver, By.xpath(xpath), 10000);    
	  for(String winHandle : driver.getWindowHandles()) {
	    	
	        driver.switchTo().window(winHandle);  
	  }
	  String tempCurrUrl = driver.getCurrentUrl();
      testFuncs.myDebugPrinting("tempCurrUrl - " + tempCurrUrl, enumsClass.logModes.MINOR);
      testFuncs.myDebugPrinting("searchedUrl - " + searchedUrl, enumsClass.logModes.MINOR);
	  testFuncs.myAssertTrue("Wanted url was not detected !! current url - <" + tempCurrUrl + ">", tempCurrUrl.contains(searchedUrl)); 
	  driver.close();
	  
	  // Return to OVOC
      testFuncs.myDebugPrinting("Return to OVOC", enumsClass.logModes.NORMAL);
      driver.switchTo().window(winHandleBefore);
      testFuncs.myWait(3000);
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
