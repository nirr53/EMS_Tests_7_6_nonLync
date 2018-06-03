package EMS_Tests;

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
* This test tests the send message and delete device of multiple-devices-changes menu
* ----------------
* Tests:
* 	- Enter Manage multiple devices changes menu.
* 	- Create several users using POST query.
* 	 2. send message with timing.
* 	 3. delete device.
* 	 4. Delete the users.
* 
* Results:
*	 - Users should be created successfully.
* 	 2. The message should be sent to the devices successfully.
* 	 3. The devices (but not the users!) should be delete successfully.
* 	 4. Users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test20__multiple_devices_send_message_delete_device {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test20__multiple_devices_send_message_delete_device(browserTypes browser) {
	  
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
  public void Manage_multiple_devices_timing() throws Exception {
	 
	Log.startTestCase(this.getClass().getName());
	
	// Set variables
	String usersNumber   = "4";
	String dispPrefix    = "sMsgDlDvUsr" + testFuncs.getId();
	int usStartIdx 		 = 1;
    Map<String, String> map = new HashMap<String, String>();

    // Create several users using POST query
	testFuncs.myDebugPrinting("Create several users using POST query");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Setup_Manage_users", "New User");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()           ,
			 testVars.getPort()         ,
			 usersNumber		        ,
			 dispPrefix  		        ,
			 testVars.getDomain()       ,
			 "registered"               ,
			 testVars.getDefPhoneModel(),
			 testVars.getDefTenant()    ,
			 "myLocation");
	testFuncs.verifyPostUserCreate(driver,  dispPrefix,  dispPrefix, true);
    
    // Step 1 - send message
  	testFuncs.myDebugPrinting("Step 1 - send message");  	
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_devices", "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("action"     ,  "Send Message");
    map.put("message"    ,  "myMessage");
    testFuncs.setMultipleDevicesAction(driver, map);
    testFuncs.searchStr(driver, dispPrefix + "_1@" + testVars.getDomain() + " " + testFuncs.readFile("mac_1.txt"));
    testFuncs.searchStr(driver, dispPrefix + "_2@" + testVars.getDomain() + " " + testFuncs.readFile("mac_2.txt"));
    testFuncs.searchStr(driver, dispPrefix + "_3@" + testVars.getDomain() + " " + testFuncs.readFile("mac_3.txt"));
   
    // Step 3 - delete devices
  	testFuncs.myDebugPrinting("Step 3 - delete devices");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_devices", "Manage Multiple Devices");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("action"     ,  "Delete Devices");
    testFuncs.setMultipleDevicesAction(driver, map);
    testFuncs.searchStr(driver, dispPrefix + "_1@" + testVars.getDomain() + " " + testFuncs.readFile("mac_1.txt"));
    testFuncs.searchStr(driver, dispPrefix + "_2@" + testVars.getDomain() + " " + testFuncs.readFile("mac_2.txt"));
    testFuncs.searchStr(driver, dispPrefix + "_3@" + testVars.getDomain() + " " + testFuncs.readFile("mac_3.txt"));
    testFuncs.myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/button[1]"), 3000);
    testFuncs.selectMultipleDevices(driver, dispPrefix, "0");

    // Step 4 - Delete the created users
  	testFuncs.myDebugPrinting("Step 4 - Delete the created users");
	testFuncs.enterMenu(driver, "Setup_Manage_multiple_users", " Manage Multiple Users");
    testFuncs.selectMultipleUsers(driver, dispPrefix, usersNumber);
    map.put("usersPrefix"	  , dispPrefix + "_");
    map.put("usersNumber"	  , usersNumber); 
    map.put("startIdx"   	  , String.valueOf(usStartIdx));
    map.put("srcUsername"	  , "Finished");
    map.put("action"	 	  , "Delete Users");
    map.put("skipVerifyDelete", "true");
    testFuncs.setMultipleUsersAction(driver, map);
    dispPrefix = dispPrefix.toLowerCase();
    testFuncs.searchStr(driver, dispPrefix + "_1@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, dispPrefix + "_2@" + testVars.getDomain() + " Finished");
    testFuncs.searchStr(driver, dispPrefix + "_3@" + testVars.getDomain() + " Finished");
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
