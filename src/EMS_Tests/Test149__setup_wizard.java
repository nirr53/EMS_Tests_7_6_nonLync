package EMS_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

/**
* ----------------
* This test tests the Setup wizard menu
* ----------------
* Tests:
* 	 - Login and enter the Setup Wizard menu 
* 	 1. Check Step-1 menu headers
* 	 2. Check Step-2 at Traditional Enterprise path 
* 	 3. Check Step-2 at Skype-For-Business path 
* 	 4. Check Step-2-6 at Skype-For-Business path (when ZT is set to 'no')
* 	 5. Check Step-2-3 at Skype-For-Business path (when ZT is set to 'yes')
* 	 6. Check Step-2-4 at Skype-For-Business path (when ZT is set to 'yes')
* 	 7. Check Step-2-5 at Skype-For-Business path (when ZT is set to 'yes')
* 	 8. Check Step-2-6 at Skype-For-Business path (when ZT is set to 'yes')
* 	 9. Return to to first step
* 
* Results:
* 	 1-9. As described.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test149__setup_wizard {
	
  private String        usedBrowser = "";
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test149__setup_wizard(String browser) {
	  
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
  public void Setup_wizrd_tests() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());

	// Login the system and enter Setup-Wizard menu
	testFuncs.myDebugPrinting("Login the system and enter Setup-Wizard menu");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Wizard", "System Properties");

	// Step 1 - Check Step-1 menu headers
	testFuncs.myDebugPrinting("Step 1 - Check Step-1 menu headers");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-0']/h2"					  , "System Properties");	
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-0']/div/div[1]"			  , "System Type");	
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-0']/div/div[2]/div[1]/label", "Skype For Business");	
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-0']/div/div[2]/div[2]/label", "Traditional Enterprise Phone Systems");	
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-0']/div/div[3]"			  , "(The system will choose the most appropriate templates for configurating the devices.)");	

	// Step 2 - Check Step-2 at Traditional Enterprise path
	testFuncs.myDebugPrinting("Step 2 - Check Step-2 at Traditional Enterprise path");
	testFuncs.myClick(driver, By.xpath("//*[@id='pbx']")								, 4000);
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[2]/div[2]/button[2]"), 4000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/h2"		  			, "Finish");	
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/h5[3]"				, "This wizard can not help you. Please contact you administrator and get the PBX template files.");	
	testFuncs.verifyStrByXpath(driver, "//*[@id='smartwizard']/ul/li[6]/a"		, "Step 6");	
	testFuncs.verifyStrByXpath(driver, "//*[@id='smartwizard']/ul/li[6]/a/small", "DHCP Configuration");	
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[1]/div[2]/button[1]"), 4000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-0']/h2"					  , "System Properties");	

	// Step 3 - Check Step-2 at Skype-For-Business path
	testFuncs.myDebugPrinting("Step 3 - Check Step-2 at Skype-For-Business path");
	testFuncs.myClick(driver, By.xpath("//*[@id='sfb']")								, 4000);
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[2]/div[2]/button[2]"), 4000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-1']/h2"		 , "Zero Touch");	
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-1']/div/div[1]", "Using Zero Touch Provisioning");	
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-1']/div/div[3]", "(For Zero Touch Provisioning need tenants.)");	

	// Step 4 - Check Step-2-6 at Skype-For-Business path (when ZT is set to 'no')
	testFuncs.myDebugPrinting("Step 4 - Check Step-2-6 at Skype-For-Business path (when ZT is set to 'no')");
    List<WebElement>radioButton = driver.findElements(By.xpath("//*[@id='nozerotouch']"));
	radioButton.get(1).click();
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[2]/div[2]/button[2]"), 4000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/h2"									, "Finish");	
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/h5[1]"								, "All is left to do is copy this URL to the DHCP option 160");	
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/h5[2]"								, "Note: A device that will get this URL from the DHCP will automatically be entered to tenant AutoDetection");	
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/div/div"								, "DHCP option 160 configuration");	
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/div/table[2]/tbody/tr[1]/td[1]	  " , "EMS accesses phones directly:");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/div/table[2]/tbody/tr[2]/td[1]/span"  , "EMS accesses phones via SBC HTTP Proxy:");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/div/table[3]/tbody/tr[1]/td[1]"	    , "EMS accesses phones directly:");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/div/table[3]/tbody/tr[2]/td[1]/span"  , "EMS accesses phones via SBC HTTP Proxy");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/div/table[2]/tbody/tr[1]/td[2]/b/span", "http://" + testVars.getIp() + "/firmwarefiles;ipp/dhcpoption160.cfg");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/div/table[2]/tbody/tr[2]/td[2]/b/span", "http://SBC_PROXY_IP:SBC_PROXY_PORT/firmwarefiles;ipp/httpproxy/");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/div/table[3]/tbody/tr[1]/td[2]/b/span", "http://" + testVars.getIp() + "/firmwarefiles;ipp/dhcpoption160.cfg");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/div/table[3]/tbody/tr[2]/td[2]/b/span", "http://SBC_PROXY_IP:SBC_PROXY_PORT/firmwarefiles;ipp/httpproxy/");
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[1]/div[2]/button[1]"), 4000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-1']/h2"		 , "Zero Touch");
	
	// Step 5 - Check Step-2-3 at Skype-For-Business path (when ZT is set to 'yes')
	testFuncs.myDebugPrinting("Step 5 - Check Step-2-3 at Skype-For-Business path (when ZT is set to 'yes')");
    radioButton = driver.findElements(By.xpath("//*[@id='nozerotouch']"));
	radioButton.get(0).click();
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[2]/div[2]/button[2]"), 4000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-2']/h2"									, "Choose Tenant");	
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-2']/div/div[1]"							, "Choose Tenant");	
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-2']/div/div[2]/div[1]/label"				, "Select an existing tenant");	
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-2']/div/div[3]"							, "(Create tenant or use an exists one.)");	
	new Select(driver.findElement(By.xpath("//*[@id='step-2']/div/div[2]/div[1]/select"))).selectByVisibleText(testVars.getDefTenant());;
	testFuncs.myWait(4000);
	
	// Step 6 - Check Step-2-4 at Skype-For-Business path (when ZT is set to 'yes')
	testFuncs.myDebugPrinting("Step 6 - Check Step-2-4 at Skype-For-Business path (when ZT is set to 'yes')");
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[2]/div[2]/button[2]"), 4000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-3']/h2"							  	 , "Tenant Configuration");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-3']/div/div[1]"						 , "Set configuration of the tenant: " + testVars.getDefTenant());	
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-3']/div/div[2]/div/div[1]/div[1]/label", "Configuration Key:");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-3']/div/div[2]/div/div[1]/div[2]/label", "Configuration Value:");
	testFuncs.verifyStrByXpath(driver, "//*[@id='table_keys']/thead/tr/th[1]"				 , "Configuration Key");
	testFuncs.verifyStrByXpath(driver, "//*[@id='table_keys']/thead/tr/th[2]"				 , "Configuration Value");
	
	// Step 7 - Check Step-2-5 at Skype-For-Business path (when ZT is set to 'yes')
	testFuncs.myDebugPrinting("Step 7 - Check Step-2-5 at Skype-For-Business path (when ZT is set to 'yes')");
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[2]/div[2]/button[2]"), 4000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-4']/h2"				   						 , "Zero Touch Templates Mapping");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-4']/div/div/div[1]/pre/p"						 , "After creating the TENANTs we need to map the TEMPLATE for each device.");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-4']/div/div/div[1]/pre/p"						 , "The TEMPALTE will be choosen according to the {MODEL + TENANT}.");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-4']/div/div/div[1]/pre/p"						 , "With this mapping a new device that registered to the IPP Manager,");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-4']/div/div/div[1]/pre/p"						 , "will get the TEMPLATE according to its {MODEL + TENANT}.");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-4']/div/div/div[2]/ul/li[1]/a"				 , "Setup Template");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-4']/div/div/div[2]/ul/li[2]/a"				 , "Templates Mapping");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-4']/div/div/div[2]/ul/li[3]/a"				 , "Test");
	testFuncs.verifyStrByXpath(driver, "//*[@id='def']/div/div/div[1]/h3"				 			 , "Default template per model and tenant");
	testFuncs.verifyStrByXpath(driver, "//*[@id='def']/div/div/div[2]/table/tbody/tr/td[1]/div/label", "Template");
	
	// Step 8 - Check Step-2-6 at Skype-For-Business path (when ZT is set to 'yes')
	testFuncs.myDebugPrinting("Step 8 - Check Step-2-6 at Skype-For-Business path (when ZT is set to 'yes')");
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[2]/div[2]/button[2]"), 4000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/h2"						 	  , "Finish");	
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/h5[1]"						  , "All is left to do is copy this URL to the DHCP option 160");	
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/h5[2]"					      , "Note: A device that will get this URL from the DHCP will automatically be entered to tenant " + testVars.getDefTenant());	
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/div/div"					      , "DHCP option 160 configuration");	
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/div/table[1]/tbody/tr[1]/td[1]", "Copy this URL to the DHCP option 160:");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/div/table[1]/tbody/tr[3]/td[1]", "For testing the device you can set this URL directly to the device(see tooltip)");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/div/table[1]/tbody/tr[1]/td[2]", "http://" + testVars.getIp() + "/firmwarefiles;ipp/tenant/Nir");
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-5']/div/table[1]/tbody/tr[3]/td[2]", "http://" + testVars.getIp() + "/ipp/tenant/Nir");
	
	// Step 9 - Return to first step//*[@id="step-5"]/div/table[1]/tbody/tr[1]/td[1]
	testFuncs.myDebugPrinting("Step 9 - Return to first step");
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[1]/div[2]/button[1]"), 4000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-4']/h2"				   						 , "Zero Touch Templates Mapping");
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[1]/div[2]/button[1]"), 4000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-3']/h2"				   						 , "Tenant Configuration");
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[1]/div[2]/button[1]"), 4000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-2']/h2"				   						 , "Choose Tenant");
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[1]/div[2]/button[1]"), 4000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-1']/h2"				   						 , "Zero Touch");
	testFuncs.myClick(driver, By.xpath("//*[@id='smartwizard']/nav[1]/div[2]/button[1]"), 4000);
	testFuncs.verifyStrByXpath(driver, "//*[@id='step-0']/h2"				   						 , "System Properties");
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
