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
*  This test test loads of high-rate create of users+devices
* ----------------
* Tests:
* 	 - Enter Manage multiple users changes menu
* 	 1.   Create 30 users at rate of 1 user per second using POST query
* 	 2.   Create 30 users at rate of 2 user per second using POST query
* 	 3.   Create 30 users at rate of 5 user per second using POST query
* 	 4.   Delete the created users
* 
* Results:
*	 1.   Users should be created successfully.
* 	 2.   The message should be sent successfully.
* 	 3-5. The user should be reset successfully.
* 	 6.   Users should be deleted successfully.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Test164__testThreads {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private browserTypes  usedBrowser;
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  
  // Default constructor for print the name of the used browser 
  public Test164__testThreads(browserTypes browser) {
	  
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
  public void Test_threads_load()  throws Exception {
	 
	  int 	 threadsNum	= 10;
	  String dispPrefix = "load" + testFuncs.getId();
	  
	  // Create Threads array and login
	  testFuncs.myDebugPrinting("Create Threads array and login", enumsClass.logModes.MAJOR);
	  RunnableDemo [] ThreadsArray = new RunnableDemo[threadsNum];
	  for (int i = 0; i < ThreadsArray.length; ++i) {
		  
		  ThreadsArray[i] = new RunnableDemo("Thread <" + i + ">", testFuncs, testVars, dispPrefix + "_" + i);
	  }	
	  testFuncs.login(driver, testVars.getSysLoginData(enumsClass.loginData.USERNAME), testVars.getSysLoginData(enumsClass.loginData.PASSWORD), testVars.getSysMainStr(), "https://", this.usedBrowser);  
	  
	  // Step 1 - run Load of 1 create per second
	  testFuncs.myDebugPrinting("Step 1 - run Load of 1 create per second", enumsClass.logModes.MAJOR);
	  for (int i = 0; i < ThreadsArray.length; ++i) {
		  
		  ThreadsArray[i].start();
		  testFuncs.myWait(500);
	  }


      
 
  }
  
  @After
  public void tearDown() throws Exception {
	  
//    driver.quit();
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
    	testFuncs.myFail(verificationErrorString);
    }
  }
}



class RunnableDemo implements Runnable {
	
	   private Thread t;
	   private String threadName;
	   GlobalVars 	  testVars;
	   GlobalFuncs	  testFuncs;
	   String 		  dispPrefix;
	   
	   // Default constructor
	   RunnableDemo( String name, GlobalFuncs testFuncs, GlobalVars testVars, String dispPrefix) {
		   
	      threadName 	  = name;
	      this.testFuncs  = testFuncs;
	      this.testVars   = testVars;
	      this.dispPrefix = dispPrefix;
		  testFuncs.myDebugPrinting("Creating " +  threadName, enumsClass.logModes.MAJOR);
	   }
	   
	   // Run method for create the User+device
	   public void run() {
		   
		   testFuncs.myDebugPrinting("Running " +  threadName, enumsClass.logModes.MAJOR);
		   testFuncs.createUsers(testVars.getIp()			,
				   				 testVars.getPort() 	 	,
				   				 1							,	
				   				 dispPrefix  		 		,
				   				 testVars.getDomain()		,
				   				 "registered"		  		,
				   				 testVars.getDefPhoneModel(),
				   				 testVars.getDefTenant()    ,
				   				 testVars.getDefLocation());
		   testFuncs.myDebugPrinting("Thread " +  threadName + " exiting.", enumsClass.logModes.MAJOR);	      
	   }
	   
	   // Start method for create the working thread and start its running
	   public void start () {
		   
		   testFuncs.myDebugPrinting("Thread " +  threadName + " exiting", enumsClass.logModes.MAJOR);	      
		   if (t == null) {
			   
			   t = new Thread (this, threadName);
			   t.start ();
	      }
	   }
	}
