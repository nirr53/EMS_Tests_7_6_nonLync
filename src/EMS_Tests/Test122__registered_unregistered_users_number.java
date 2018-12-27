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
* This test tests the changes in registered /  unregistered users number.
* ----------------
* Tests:
*  	 - Login the system
*  	 - Check the number of registered and unregistered users
*  	 1. Create a registered user
*  	 2. Create an unregistered user
*  	 3. Create an offline started user
*  	 4. Delete the users.
*  
* Results:
*    1. The status of registered users should be updated.
*    2. The status of unregistered users should be updated.
*    3. The status of offline users should be updated.
*    4. The users should be deleted successfully.
*  
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test122__registered_unregistered_users_number {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test122__registered_unregistered_users_number(browserTypes browser) {
	  
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
  public void Registered_unregistered_users_status() throws Exception {
	  
		Log.startTestCase(this.getClass().getName());

		// Set variables + login
		String regDevicesNumber = "";
		String usersPrefix      = "rgTsts" + testFuncs.getId();
		testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	    
	    // Get registration-data and total devices number
		testFuncs.myDebugPrinting("Get registration-data and total devices number");
	    regDevicesNumber = driver.findElement(By.xpath("//*[@id='card']/div/article/div[1]/div[2]")).getText();
		testFuncs.myDebugPrinting("\nregDevicesNumber - " + regDevicesNumber + "\n", enumsClass.logModes.MINOR);
	    String totalDeviceNumber 			= getTotalDevicesNumber();
	    String totalUnregDeviceNumber	    = getTotalUnregistsredDvicesNumber();

	  	// Step 1 - Create a registered
		testFuncs.myDebugPrinting("Step 1 - Create a registered user");
		String regUserName = usersPrefix + "_1";
		testFuncs.createUsers(testVars.getIp()		  	 	,
							  testVars.getPort() 	 	 	,
							  Integer.valueOf(1)			,	
							  regUserName  	  		     	,			 
							  testVars.getDomain()	     	,					
							  "registered"		  	     	,						
							  testVars.getDefPhoneModel()	,
							  testVars.getDefTenant()    	,												
							  testVars.getDefLocation());
		testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	    testFuncs.verifyPostUserCreate(driver, regUserName, regUserName, true);
		testFuncs.pressHomeButton(driver);
		testFuncs.myWait(7000);
		
		// Verify change in number at main page
		testFuncs.myDebugPrinting("Verify change in number at main page", enumsClass.logModes.NORMAL);
	    String regDevicesNumberNew = driver.findElement(By.xpath("//*[@id='card']/div/article/div[1]/div[2]")).getText();
	    testFuncs.myDebugPrinting("\nregDevicesNumberNew - " + regDevicesNumberNew + "\n", enumsClass.logModes.MINOR);
		testFuncs.myAssertTrue("Registered users number was not increased !! <" + regDevicesNumberNew + ">", (Integer.valueOf(regDevicesNumber) + 1) == Integer.valueOf(regDevicesNumberNew));
	    
	  	// Verify change at Registered users menu
		testFuncs.myDebugPrinting("Verify change at Registered users menu", enumsClass.logModes.NORMAL);
		testFuncs.myClick(driver, By.xpath("//*[@id='card']/div/article/div[1]/div[3]/span/a"), 7000);
	    String regDevicesNumberNewStr = driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[2]/div[1]/span")).getText();
		testFuncs.myDebugPrinting("regDevicesNumberNewStr - " + regDevicesNumberNewStr, enumsClass.logModes.MINOR);
		String tempStr = "Showing 1 to " + regDevicesNumberNew + " of " + regDevicesNumberNew + " entries";
		testFuncs.myAssertTrue("Registered users number was not increased !! <tempStr - " + tempStr + ">", regDevicesNumberNewStr.contains(tempStr));
		testFuncs.pressHomeButton(driver);
		
	  	// Verify change at Total devices menu
		testFuncs.myDebugPrinting("Verify change at Total devices menu", enumsClass.logModes.NORMAL);
	    String totalDeviceNumberNew = getTotalDevicesNumber();
		testFuncs.myAssertTrue("Total users number was not increased !! <" + totalDeviceNumberNew + " -- " + totalDeviceNumber + ">", (Integer.valueOf(totalDeviceNumber) + 1) == Integer.valueOf(totalDeviceNumberNew));
		
	  	// Step 2 - Create an unregistered user
		testFuncs.myDebugPrinting("Step 2 - Create an unregistered user");
		String unRegUserName = usersPrefix + "_2";
		testFuncs.createUsers(testVars.getIp()		  	 	,
							  testVars.getPort() 	 	 	,
							  Integer.valueOf(1)			,	
							  unRegUserName  	  		   	,			 
							  testVars.getDomain()	     	,					
							  "unregistered"		  	   	,						
							  testVars.getDefPhoneModel()	,
							  testVars.getDefTenant()    	,												
							  testVars.getDefLocation());
		testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
		testFuncs.pressHomeButton(driver);	
		testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "Manage Users");
	    testFuncs.verifyPostUserCreate(driver, unRegUserName, unRegUserName, false);
		testFuncs.pressHomeButton(driver);	
		testFuncs.myWait(7000);

		// Verify that there is no change in number at main page
		testFuncs.myDebugPrinting("Verify that there is no change in number at main page", enumsClass.logModes.NORMAL);
	    regDevicesNumberNew = driver.findElement(By.xpath("//*[@id='card']/div/article/div[1]/div[2]")).getText();
		testFuncs.myDebugPrinting("regDevicesNumberNew - " + regDevicesNumberNew, enumsClass.logModes.MINOR);
		testFuncs.myAssertTrue("Registered users number was not increased !! <" + regDevicesNumberNew + ">", (Integer.valueOf(regDevicesNumber) + 1) == Integer.valueOf(regDevicesNumberNew));

	  	// Verify change at Total devices menu		
		testFuncs.myDebugPrinting("Verify change at Total devices menu", enumsClass.logModes.NORMAL); 
		totalDeviceNumberNew = getTotalDevicesNumber();
		testFuncs.myAssertTrue("Total users number was not increased !! <" + totalDeviceNumberNew + ">", (Integer.valueOf(totalDeviceNumber) + 2) == Integer.valueOf(totalDeviceNumberNew));
		testFuncs.pressHomeButton(driver);
			
	  	// Step 3 - Change device status to Offline
		testFuncs.myDebugPrinting("Step 3 - Change device status to Offline");	
		testFuncs.sendKeepAlivePacket(testVars.getKpAlveBatName()	 ,
				  					  testVars.getIp()               ,
				  					  testVars.getPort()           	 ,
				  					  testFuncs.readFile("mac_1.txt"),
				  					  unRegUserName				 	 ,
				  					  testVars.getDefPhoneModel()	 ,
				  					  testVars.getDomain()        	 ,
				  					  "offline"						 ,
				  					  testVars.getDefLocation()					 ,
				  					  "+97239764713"				 ,
				  					  testVars.getFirmVersion());
		String newUnregDeviceNumber	    = getTotalUnregistsredDvicesNumber();
		testFuncs.myAssertTrue("Unregistered device number was not changed !!", (Integer.valueOf(totalUnregDeviceNumber) + 1) == Integer.valueOf(newUnregDeviceNumber));
		testFuncs.myWait(7000);

	  	// Verify no change at Total devices menu		
		testFuncs.myDebugPrinting("Verify no change at Total devices menu", enumsClass.logModes.NORMAL); 
		totalDeviceNumberNew = getTotalDevicesNumber();
		testFuncs.myAssertTrue("Total users number was increased !! <" + totalDeviceNumberNew + ">", (Integer.valueOf(totalDeviceNumber) + 2) == Integer.valueOf(totalDeviceNumberNew));
		testFuncs.pressHomeButton(driver);	
		
	    // Step 3 - Delete the users
	  	testFuncs.myDebugPrinting("Step 3 - Delete the created users");
		testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");
	    testFuncs.selectMultipleUsers(driver, usersPrefix, "1");
		Map<String, String> map = new HashMap<String, String>();
	    map.put("usersPrefix"	  , usersPrefix);
	    map.put("usersNumber"	  , "1"); 
	    map.put("startIdx"   	  , "1");
	    map.put("srcUsername"	  , "Finished");
	    map.put("action"	 	  , "Delete Users");
	    map.put("skipVerifyDelete", "true");
	    testFuncs.setMultipleUsersAction(driver, map);
	    testFuncs.searchStr(driver, regUserName.toLowerCase() + "@" + testVars.getDomain() + " Finished");
  }
  
  // Get unregistered devices number  
  private String getTotalUnregistsredDvicesNumber() {
	  
	  String unRegNumber = driver.findElement(By.xpath("//*[@id='card']/div/article/div[1]/div[2]")).getText();
	  testFuncs.myDebugPrinting("\nUnregistered - <" + unRegNumber + ">\n", enumsClass.logModes.MINOR);
	  testFuncs.pressHomeButton(driver);
	  return unRegNumber;
  }

  // Get total devices number
  private String getTotalDevicesNumber() {
	  
	  testFuncs.myClick(driver, By.xpath("//*[@id='card']/div/article/div[1]/div[3]/span/a"), 10000);  
	  testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/h3/div/a[4]")		, 10000);  
	  String totalDeviceNumber = driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[2]/div[1]/span")).getText();
	  totalDeviceNumber = totalDeviceNumber.substring(totalDeviceNumber.indexOf("of ") + "of ".length(), totalDeviceNumber.indexOf("entries")).trim();	  
	  testFuncs.myDebugPrinting("\ntotalDeviceNumber - <" + totalDeviceNumber + ">\n", enumsClass.logModes.MINOR);
	  testFuncs.pressHomeButton(driver);
	  return totalDeviceNumber;
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
