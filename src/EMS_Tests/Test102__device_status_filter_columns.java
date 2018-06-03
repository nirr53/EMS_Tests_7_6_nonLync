package EMS_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;
import EMS_Tests.enumsClass.*;

/**
* ----------------
* This test tests the display-columns feature of Device-Status menu
* ----------------
* Tests:
*    1. Select all the columns via Display-Columns and verify that they displayed.
*    2. Restore the columns via via Display-Columns and verify that they displayed
* 
* Results:
* 	 1. All columns should be displayed.
* 	 2. All default columns should be displayed.
* 		All non-default columns should NOT be displayed.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test102__device_status_filter_columns {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test102__device_status_filter_columns(browserTypes browser) {
	  
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
  public void Device_status_display_columns() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Login via Administrator and create a user using POST query
	testFuncs.myDebugPrinting("Login via Administrator and create a user using POST query");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Monitor_device_status", "Devices Status");
	
	// Step 1 - Select all the columns via Display-Columns and verify that they displayed
	testFuncs.myDebugPrinting("Step 1 - Select all the columns via Display-Columns and verify that they displayed");  
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/h3/div/a[1]"), 7000);     
	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3"  		 , "Devices Status Columns"); 
	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/label", "Please select columns to display in Devices Status table"); 
	String checkboxNames[] = {"FW_VERSION"  , "IP" 		  		, "MODEL" 	 		 , "LAST_STATUS_UPDATE_TIME", "LOCATION",
			  "MAC"		    , "PHONE_NUMBER"	, "REPORT_TIME"		 , "SIP_PROXY"              , "STATUS"  ,
			  "SUBNET"	    , "USER_AGENT"      , "USER_NAME"    	 , "TENANT_NAME"            , "MODEL_NAME",
			  "BTOE_VERSION", "USB_HEADSET_TYPE", "HRS_SPEAKER_MODEL", "HRS_SPEAKER_FW"			, "BTOE_PAIRING_STATUS",
			  "SITE_NAME", "VLAN_ID"};
	int length = checkboxNames.length;
	for (int i = 0; i < length; ++i) {
		
		String xpath = "//*[@id='" + checkboxNames[i] + "']";
		testFuncs.myDebugPrinting("The checkbox xpath is - " + xpath, enumsClass.logModes.MINOR);  
		if (!driver.findElement(By.xpath(xpath)).isSelected()) {
			
			testFuncs.myClick(driver, By.xpath(xpath), 5000);
		}
	}
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/a[3]"), 10000);      

	// Search for the column names at table
	testFuncs.myDebugPrinting("Search for the column names at table", enumsClass.logModes.MINOR);  
	String columnNames[] = {"User Name"		   , "Phone Number"	 , "Last Update Status", "Mac Address",
			   				"IP Address"  	   , "IPP Model" 	 , "Firmware"          , "Tenant",
			   				"Site"			   , "Template"		 , "Report Time"       , "Location",
			   				"Subnet"		   , "VLAN ID"		 , "BToE Version"	   , "USB Headset Type",
			   				"HRS Speaker Model", "HRS Speaker FW", "User Agent"		   , "SIP Proxy"};
	length = columnNames.length;
	for (int i = 0; i < length; ++i) {
		
		testFuncs.myDebugPrinting("The searched column name is - " + columnNames[i], enumsClass.logModes.MINOR);  
		testFuncs.searchStr(driver, columnNames[i]);
	}
	
	// Step 2 - Restore the columns via via Display-Columns and verify that only-default columns are displayed
	testFuncs.myDebugPrinting("Step 2 - Restore the columns via via Display-Columns and verify that only-default columns are displayed");  
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/h3/div/a[1]"), 7000);     
	testFuncs.verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3"  		 , "Devices Status Columns"); 
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/a[2]"), 10000);      
	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/a[3]"), 10000);      

	// Search for the default-column names at table
	testFuncs.myDebugPrinting("2.1 Search for the default-column names at table", enumsClass.logModes.MINOR);  
	String defColumnNames[] = {"BToE"		, "User Name"   , "Phone Number", "Last Update Status",
			   				   "Mac Address", "IP Address"  , "IPP Model"   , "Firmware",
			   				   "Tenant"		, "Site"        , "Template"    , "Report Time",
			   				   "Location"   , "BToE Version", "USB Headset Type"};
	length = defColumnNames.length;
	for (int i = 0; i < length; ++i) {
		
		testFuncs.myDebugPrinting("The searched column name is - " + defColumnNames[i], enumsClass.logModes.MINOR);  
		testFuncs.searchStr(driver, defColumnNames[i]);
	}
	
	testFuncs.myDebugPrinting("2.2 Verify that non-default columns are not displayed", enumsClass.logModes.MINOR);  
	String pageTxt = driver.findElement(By.tagName("body")).getText();
	testFuncs.myAssertTrue("VLAN-ID column  is displayed!"   	   , !pageTxt.contains("VLAN ID"));
	testFuncs.myAssertTrue("SIP Proxy column  is displayed!" 	   , !pageTxt.contains("SIP Proxy"));
	testFuncs.myAssertTrue("Subnet column  is displayed!"    	   , !pageTxt.contains("Subnet"));
	testFuncs.myAssertTrue("User Agent column  is displayed!"	   , !pageTxt.contains("User Agent"));
	testFuncs.myAssertTrue("HRS Speaker Model column is displayed!", !pageTxt.contains("HRS Speaker Model"));
	testFuncs.myAssertTrue("HRS Speaker FW column  is displayed!"  , !pageTxt.contains("HRS Speaker FW"));
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
