package EMS_Tests;

import java.net.Inet4Address;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;
import EMS_Tests.enumsClass.*;

/**
* ----------------
* This test tests the Network-Topology menu
* ----------------
* Tests:
* 	 - Add 2 registered users.
* 	 - Add 2 unregistered users.
* 	 1. Enter the Network Topology (Ips) and verify that users are displayed
* 	 2. Enter the Network Topology (External Ips) and verify that source-Ips are displayed
* 
* Results:
* 	 1+2. Users should be displayed at Network Topology menu.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test121__network_topology {
	
  private browserTypes  usedBrowser;
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test121__network_topology(browserTypes browser) {
	  
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
  public void Network_Topology() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String id 				= testFuncs.getId();
	String usersNumber		= "1";
	String regPrefix		= "regPrefix"   + id;
	String unregPrefix		= "unregPrefix" + id;
	Map<String, String> map = new HashMap<String, String>();
    map.put("usersNumber",  usersNumber); 

	// Login and create 2 registered users and 2 unregistered users
	testFuncs.myDebugPrinting(" Login and create 2 registered users and 2 unregistered users");
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()       	,
			 												 testVars.getPort()     	,
			 												usersNumber				    ,
			 												regPrefix					,
			 												 testVars.getDomain()       ,
			 												 "registered"               ,
			 												 testVars.getDefPhoneModel(),
			 												 testVars.getDefTenant()    ,
			 												 "myLocation");
    testFuncs.verifyPostUserCreate(driver, regPrefix, regPrefix, true);    
    String ip1 = testFuncs.readFile("ip_1.txt");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
    testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()           ,
			 												 testVars.getPort()    	    ,
			 												 usersNumber				,
			 												 unregPrefix     			,
			 												 testVars.getDomain()       ,
			 												 "unregistered"             ,
			 												 testVars.getDefPhoneModel(),
			 												 testVars.getDefTenant()    ,
			 												 "myLocation");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "Manage Users");
    testFuncs.verifyPostUserCreate(driver, unregPrefix, unregPrefix, false);
    String ip2 = testFuncs.readFile("ip_1.txt");    
    
    // Step 1 - Enter the Network Topology (Ips) and verify that users are displayed
 	testFuncs.myDebugPrinting("Step 1 - Enter the Network Topology (Ips) and verify that users are displayed");
	testFuncs.pressNetworkTopologyButton(driver);
	String bodyText = driver.findElement(By.tagName("body")).getText();				
	int cirCount = bodyText.length() - bodyText.replace(".", "").length();
 	testFuncs.myDebugPrinting("cirCount - " + cirCount, enumsClass.logModes.MINOR);
 	ip1 = removeSuffix(ip1);
 	ip2 = removeSuffix(ip2); 	
 	testFuncs.myAssertTrue("Endpoint <" + ip1 + "> is not detected !!", bodyText.contains(ip1));
 	testFuncs.myAssertTrue("Endpoint <" + ip2 + "> is not detected !!", bodyText.contains(ip2));

 	// Step 2 - Enter the Network Topology (External Ips) and verify that source-Ips are displayed
 	testFuncs.myDebugPrinting("Step 2 - Enter the Network Topology (External Ips) and verify that source-Ips are displayed");
 	testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div[2]/div/div/div/button"), 3000);
	bodyText = driver.findElement(By.tagName("body")).getText();				
	cirCount = bodyText.length() - bodyText.replace(".", "").length();
 	testFuncs.myDebugPrinting("cirCount - " + cirCount, enumsClass.logModes.MINOR);
 	String localIp = removeSuffix(Inet4Address.getLocalHost().getHostAddress()); 
 	testFuncs.myAssertTrue("External Point <" + localIp + "> is not detected !!", bodyText.contains(localIp));
  
	// Step 3 - Delete all created users
 	testFuncs.myDebugPrinting("Step 3 - Delete all created users");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, regPrefix, usersNumber);
    map.put("usersPrefix"	  , regPrefix);
    map.put("usersNumber"	  , usersNumber); 
    map.put("startIdx"   	  , String.valueOf("1"));
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    regPrefix = regPrefix.toLowerCase();
    testFuncs.searchStr(driver, regPrefix + "@" + testVars.getDomain() + " Finished");
  }

  // Return only two first parts of given Ip address
  private String removeSuffix(String ip) {
	   	
	  String[] parts = ip.split("\\.");	
	  return (parts[0] + "." + parts[1]);
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
