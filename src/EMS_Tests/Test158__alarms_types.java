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
* This test tests a receive of alarms from all types
* ----------------
* Tests:
* 	- Create a registered user via POST query
* 	 1-9. Create an alarm with different name sent to the user we created.
* 	  10. Delete all the created Alarms
* 	  11. Delete the created user 
* 
* Results:
* 	1+9. Alert should be displayed correctly.
*    10. All the alerts should be deleted successfully.
*    11. User should be deleted.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test158__alarms_types {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test158__alarms_types(browserTypes browser) {
	  
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
  public void Alarms_types() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	  
	// Set variables and login
	String Id 		= testFuncs.getId();
	int    loopIdx  = 1;
	String username = ("alrmsTypesUsr" + Id).toLowerCase();
    Map<String, String> alarmNamesMap 	= new HashMap<String, String>();
    alarmNamesMap.put("IPPHONE REGISTRATION FAILURE"	 			 , "IPPhone Register Failure");
    alarmNamesMap.put("IPPHONE SURVIVABLE MODE START"			 	 , "IPPhone Survivable Mode Start");
    alarmNamesMap.put("IPPHONE LYNC LOGIN FAILURE"				 	 , "IPPhone Lync Login Failure");
    alarmNamesMap.put("IPPHONE SPEAKER FIRMWARE DOWNLOAD FAILURE"	 , "IPPhone Speaker Firmware Download Failure");
    alarmNamesMap.put("IPPHONE SPEAKER FIRMWARE UPGRADE FAILURE" 	 , "IPPhone Speaker Firmware Upgrade Failure");
    alarmNamesMap.put("IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE", "IPPhone Conference Speaker Connection Failure");
	alarmNamesMap.put("IPPHONE GENERAL LOCAL EVENT"					 , "IPPhone General Local Event");
	alarmNamesMap.put("IPPHONE WEB SUCCESSIVE LOGIN FAILED TRIALS"   , "gg"); // not work - yields name is 88 
	alarmNamesMap.put("IPPHONE REQUIRES RESET"						 , "gg"); //not work - no alarm is displayed
	
	testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	
    // Create a registered user using POST method
	testFuncs.myDebugPrinting("Create a registered user using POST method");
	testFuncs.createUsers(testVars.getIp()		  	 	,
						  testVars.getPort() 	 	 	,
						  Integer.valueOf(1)			,	
						  username  	  		     	,			 
						  testVars.getDomain()	     	,					
						  "registered"		  	     	,						
						  testVars.getDefPhoneModel()	,
						  testVars.getDefTenant()    	,												
						  testVars.getDefLocation());
    testFuncs.verifyPostUserCreate(driver, username, username, true);
	String mac1 = testFuncs.readFile("mac_1.txt");

	// Steps 1- 9 Loop on all alarm names and create an Alarm for each one of them
	testFuncs.myDebugPrinting("Steps 1-9 Loop on all alarm names and create an Alarm for each one of them");
	String []alarmsForSearch = {"infoAlarm" + "_" + Id};
	for (String alarmName : alarmNamesMap.keySet()) {
		
		testFuncs.myDebugPrinting("\nStep " + (loopIdx++) + " - Create an Alarm with name <" + alarmName + ">");
		testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_DASHBOARD_ALARMS, "Export");
		testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(), testVars.getIp()  							 ,
																  testVars.getPort()							 ,
																  mac1								 			 ,
																  alarmName,
																  alarmsForSearch[0]				    		 ,
																  "2017-07-217T12:24:18"						 ,
																  Id	 									 	 ,
																  Id	 									 	 ,
																  "minor");
	
		// Verify the Alarm create
		testFuncs.myDebugPrinting("Verify the Alarm create");	
		testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.NAME, alarmNamesMap.get(alarmName), alarmsForSearch);
		testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/h3/div/a[3]"), 3000);
	}
	
	// Step 10 - Delete the created alarms
	testFuncs.myDebugPrinting("Step 10 - Delete the created alarm");			
	testFuncs.deleteAlarm(driver, alarmsForSearch[0]);	
	
	// Step 11 - Delete the created and user
	testFuncs.myDebugPrinting("Step 11 - Delete the created user");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");    
	testFuncs.selectMultipleUsers(driver, username, "1");
	Map<String, String> map = new HashMap<String, String>();
	map.put("usersPrefix"	  , username);  
	map.put("usersNumber"	  , "1"); 
	map.put("startIdx"   	  , String.valueOf("1"));	  
	map.put("srcUsername"	  , "Finished");	  
	map.put("action"	 	  , "Delete Users");	  
	map.put("skipVerifyDelete", "true");	  
	testFuncs.setMultipleUsersAction(driver, map);
	username = username.toLowerCase();
	testFuncs.searchStr(driver, username + "@" + testVars.getDomain() + " Finished");	  
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
