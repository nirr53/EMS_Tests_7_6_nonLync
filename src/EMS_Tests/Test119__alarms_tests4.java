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
* This test tests alarms with different info headers
* ----------------
* Tests:
* 	- Create a registered user via POST query
* 	 1. Create an alarm that with special info field
* 	 2. Create an alarm that with special-characters info field
* 	 3. Create an alarm that with very long languages info field
*  	 4. Create an alarm that with empty info field
* 	 5. Delete all the created Alarms
* 
* Results:
* 	1-4. Alert should be displayed as it sent
* 	  5. All data should be deleted
*
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test119__alarms_tests4 {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test119__alarms_tests4(browserTypes browser) {
	  
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
  public void Alarms_tests() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
		  
	// Set variables and login
	String Id     = testFuncs.getId();
	String prefix = "regAlert" + Id;
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
	
    // Create a registered user using POST method
	testFuncs.myDebugPrinting("Create a registered user using POST method");
	testFuncs.createUserViaPost(testVars.getCrUserBatName(), testVars.getIp()       	,
			 												 testVars.getPort()     	,
			 												 " 1"				    	,
			 												 prefix 					,
			 												 testVars.getDomain()       ,
			 												 "registered"               ,
			 												 testVars.getDefPhoneModel(),
			 												 testVars.getDefTenant()    ,
			 												 "myLocation");
    testFuncs.verifyPostUserCreate(driver, prefix, prefix, true);
	String mac1 = testFuncs.readFile("mac_1.txt");
	testFuncs.enterMenu(driver, enumsClass.menuNames.MAINPAGE_DASHBOARD_ALARMS, "Export");
	
	// Step 1 - Create an alarm with special info field
	testFuncs.myDebugPrinting("Step 1 - Create an alarm with special info field");	
	String[] alartNames = {"specialInfoAlarm1_" + Id};
	String info1     = "info1_1" + Id;
	String info2     = "info2_1" + Id;
	testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(), testVars.getIp()  							 ,
			  testVars.getPort()							 ,
			  mac1								 			 ,
			  "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE",
			  alartNames[0]			    		 	 			 ,
			  "2017-07-217T12:24:18"						 ,
			  info1	 								 		 ,
			  info2	 								 		 ,
			  "minor");

	// Search the alerts according to their description
	testFuncs.myDebugPrinting("Search the alerts according to their description", enumsClass.logModes.MINOR);
	testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.DESCRPTION, alartNames[0]  , alartNames);
	testFuncs.searchStr(driver, info1);
	testFuncs.deleteAlarm(driver, alartNames[0]);	
	
	// Step 2 - Create an alarm with special-characters info field
	testFuncs.myDebugPrinting("Step 2 - Create an alarm with special-characters info field");	
	String[] alartNames2 = {"specialInfoAlarm2_" + Id};
	info1 = "info1_2_!#$/=?^_`{|}~;*+'__" + Id;
	info2 = "info1_2_!#$/=?^_`{|}~;*+'__" + Id;
	testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(), testVars.getIp()  							 ,
			  testVars.getPort()							 ,
			  mac1								 			 ,
			  "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE",
			  alartNames2[0]			    		 	 			 ,
			  "2017-07-217T12:24:18"						 ,
			  info1	 								 		 ,
			  info2	 								 		 ,
			  "minor");

	// Search the alerts according to their description
	testFuncs.myDebugPrinting("Search the alerts according to their description", enumsClass.logModes.MINOR);
	testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.DESCRPTION, alartNames2[0]  , alartNames2);
	testFuncs.searchStr(driver, info1);
	testFuncs.deleteAlarm(driver, alartNames2[0]);	
	
	// Step 3 - Create an alarm with very long info field
	testFuncs.myDebugPrinting("Step 3 - Create an alarm with very long info field");	
	String[] alartNames3 = {"specialInfoAlarm3_" + Id};
	info1 = "info1_3_abcdefghijklmnopqrstuvwxyz1234567890abcdefghijkl__" + Id;
	info2 = "info2_3_abcdefghijklmnopqrstuvwxyz1234567890abcdefghijkl__" + Id;
	testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(), testVars.getIp()  							 ,
			  testVars.getPort()							 ,
			  mac1								 			 ,
			  "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE",
			  alartNames3[0]			    		 	 			 ,
			  "2017-07-217T12:24:18"						 ,
			  info1	 								 		 ,
			  info2	 								 		 ,
			  "minor");

	// Search the alerts according to their description
	testFuncs.myDebugPrinting("Search the alerts according to their description", enumsClass.logModes.MINOR);
	testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.DESCRPTION, alartNames3[0]  , alartNames3);
	testFuncs.searchStr(driver, info1);
	testFuncs.deleteAlarm(driver, alartNames3[0]);	
	
	// Step 4 - Create an alarm with very empty info field
	testFuncs.myDebugPrinting("Step 4 - Create an alarm with very empty info field");	
	String[] alartNames4 = {"specialInfoAlarm4_" + Id};
	info1 = "empty";
	info2 = "empty";
	testFuncs.createAlarmViaPost(testVars.getAlarmsBatName(), testVars.getIp()  							 ,
			  testVars.getPort()							 ,
			  mac1								 			 ,
			  "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE",
			  alartNames4[0]			    		 	 			 ,
			  "2017-07-217T12:24:18"						 ,
			  info1	 								 		 ,
			  info2	 								 		 ,
			  "minor");

	// Search the alerts according to their description
	testFuncs.myDebugPrinting("Search the alerts according to their description", enumsClass.logModes.MINOR);
	testFuncs.searchAlarm(driver, enumsClass.alarmFilterModes.DESCRPTION, alartNames4[0]  , alartNames4);
	testFuncs.searchStr(driver, "IPPhone/" + mac1 + " " + Inet4Address.getLocalHost().getHostAddress());
	testFuncs.deleteAlarm(driver, alartNames4[0]);
	
	// Step 5 - Delete the created alarms and users
	testFuncs.myDebugPrinting("Step 5 - Delete the created alarms and users");
	testFuncs.enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_MULTIPE_USERS, " Manage Multiple Users");    
	testFuncs.selectMultipleUsers(driver, prefix, "1");
	Map<String, String> map = new HashMap<String, String>();
	map.put("usersPrefix"	  , prefix);  
	map.put("usersNumber"	  , "1"); 
	map.put("startIdx"   	  , String.valueOf("1"));	  
	map.put("srcUsername"	  , "Finished");	  
	map.put("action"	 	  , "Delete Users");	  
	map.put("skipVerifyDelete", "true");	  
	testFuncs.setMultipleUsersAction(driver, map);
	prefix = prefix.toLowerCase();
	testFuncs.searchStr(driver, prefix + "@" + testVars.getDomain() + " Finished");	  
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
