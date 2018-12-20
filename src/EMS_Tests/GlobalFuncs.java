package EMS_Tests;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import EMS_Tests.enumsClass.*;

/**
* This class holds all the functions which been used by the tests
* @author Nir Klieman
* @version 1.00
*/

public class GlobalFuncs extends BasicFuncs {
	
	  /**
	  *  webUrl  	  - default url for the used funcs
	  *  username  	  - default username for the used funcs
	  *  password 	  - default password for the used funcs
	  *  StringBuffer - default string for errors buffering
	  */
	  GlobalVars 		   testVars;
	  MenuPaths            testMenuPaths;
	  private String	   webUrl;

	  /**
	  *  Default constructor
	  */
	  public GlobalFuncs() {
		  
		  testVars 		= new GlobalVars();
		  testMenuPaths = new  MenuPaths();
		  webUrl        = testVars.getUrl();
	  }
	  
	  /**
	  *  Constructor which get data
	  *  @param givenUrl 	  - given url
	  *  @param givenUsername - given username
	  *  @param givenPassword - given password
	  */
	  public GlobalFuncs(String givenUrl, String givenUsername, String givenPassword) {
		 
		  testVars      = new GlobalVars();
		  testMenuPaths = new  MenuPaths(); 
		  webUrl        = givenUrl;	 
	  }
	  
	  /**
	  *  login method
	  *  @param driver   - given driver for make all tasks
	  *  @param username - given string for system
	  *  @param password - given password for the system
	  *  @param mainStr  - given string for verify good access
	  *  @param httpStr  - given string for using as a prefix for the url
	  *  @param brwType  - name of the used browser
	  */
	  public void login(WebDriver 	driver, String username, String password, String mainStr, String httpStr, browserTypes brwType) {
		  
	      String title = driver.getTitle();
    	  myDebugPrinting("1. title - "   	    + title  			,enumsClass.logModes.MINOR);
    	  myDebugPrinting("httpStr + webUrl - " + httpStr + webUrl  ,enumsClass.logModes.MINOR);
	      driver.get(httpStr + webUrl);
		  WebDriverWait wait = new WebDriverWait(driver, 30);
		  wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.name("username")));
		  myWait(1000);	      
	  	
		  if (brwType.equals(enumsClass.browserTypes.IE) && title.equals("WebDriver") && httpStr.equals("https://")) {
	    	  
	    	  driver.findElement(By.xpath("//a[@id='overridelink']")).click();
	    	  myWait(5000);
	    	  
	      }
		  
    	  searchStr(driver, testVars.getMainPageStr());  
	      myDebugPrinting("username - " + username ,enumsClass.logModes.MINOR);
    	  myDebugPrinting("password - " + password ,enumsClass.logModes.MINOR);
    	  mySendKeys(driver, By.xpath("//*[@id='loginform']/div[1]/input")	   , username, 1000);
    	  mySendKeys(driver, By.xpath("//*[@id='loginform']/div[2]/input")	   , password, 1000);    	  
    	  myClick(driver, By.xpath("//*[@id='loginform']/div[4]/div[2]/button"), 3000);
	      
	      // Verify good access
    	  myAssertTrue("Login fails !!!! (mainStr - " + mainStr + " was not detected !!)", driver.findElement(By.tagName("body")).getText().contains(mainStr)); 
	  }
	  
	  /**
	  *  Press the Home Button
	  *  @param driver - given driver
	  */
	  public void pressHomeButton(WebDriver driver) {
		  
		  myWait(7000);		  
		  myClick(driver, By.xpath("//*[@id='navbar-collapse']/ul[1]/li[2]/a"), 7000); 
		  searchStr(driver, "Statistics");
	  }
	  
	  /**
	  *  Press the Network Topology Button
	  *  @param driver - given driver
	  */
	  public void pressNetworkTopologyButton(WebDriver driver) {
		  
		  myWait(3000);		  
		  myClick(driver, By.xpath("//*[@id='subHeader']/a[2]"), 2000); 
		  searchStr(driver, "Network Devices Topology");
	  }

	  /**
	  *  Add user manually
	  *  @param driver       - given driver
	  *  @param currUsername - username for the created user
	  *  @param userPass     - password for the created user
	  *  @param userDisName  - display name for the created user
	  *  @param tenant       - tenant for the created user
	  */
	  public void addUser(WebDriver 	driver, String currUsername, String userPass, String userDisName, String tenant) {
		
		// Enter the Manage-users menu & Add-User menu
		enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
		myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/a"), 5000);
		verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "Add User");
				
	    // Fill details
		myDebugPrinting("currUsername - " + currUsername, enumsClass.logModes.MINOR);
		myDebugPrinting("userDisName - "  + userDisName , enumsClass.logModes.MINOR);
		mySendKeys(driver, By.xpath("//*[@id='extension']")  , currUsername, 2000);
		mySendKeys(driver, By.xpath("//*[@id='secret']")     , userPass, 2000);
		mySendKeys(driver, By.xpath("//*[@id='displayname']"), userDisName, 2000);
	    
	    // Set Tenant
		myDebugPrinting("Set Tenant - " + tenant, enumsClass.logModes.MINOR);
		Select displayOptions = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
		displayOptions.selectByVisibleText(tenant);	
		myWait(5000);
		
		// Submit
		myDebugPrinting("Submit", enumsClass.logModes.MINOR);
	    myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[1]"), 6000);
		verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "User " + currUsername + " was successfully added.");
		verifyStrByXpath(driver, "//*[@id='modalContentId']", "Do you want to edit this user?");		
	    myClick(driver, By.xpath("//*[@id='modalContentId']/button[2]"), 5000);
		searchStr(driver, "New User");
	    
	    // Verify Create
		myDebugPrinting("Verify Create", enumsClass.logModes.MINOR);
		searchUser(driver, currUsername);  
	  }
	
	  /**
	  *  Verify the create /non-create of POST user
	  *  @param driver       - given driver
	  *  @param username     - username name (same name for the device)
	  *  @param dispName     - display name
	  *  @param isRegistsred - flag for identify if a registered user was created or not
	  */
	  public void verifyPostUserCreate(WebDriver driver, String username, String dispName, boolean isRegistered) {
				  	  
		// Search user 
		myDebugPrinting("Search user " + username, enumsClass.logModes.NORMAL);
		mySendKeys(driver, By.xpath("//*[@id='searchtext']"), username								, 1000);
		myClick(driver, By.xpath("//*[@id='searchusersform']/div/div/div/div/div[2]/button/span[2]"), 2000);
		myClick(driver, By.xpath("//*[@id='all_search']/li[1]/a")									, 2000);
		driver.findElement(By.xpath("//*[@id='searchtext']")).sendKeys(Keys.ENTER);
		myClick(driver, By.xpath("//*[@id='searchusersform']/div/div/div/div/span/a/button/span"), 5000);
	    waitForLoad(driver); 
	    if (isRegistered) {
	    		    	
	    	if (!username.contains("location_2049")) {
	    	
	    		searchStr(driver, username);
	    		searchStr(driver, dispName);
	    	} else {
	    		
	    		searchStr(driver, "No users found");
	    	}
	    } else {
	    	
		   verifyStrByXpath(driver, "//*[@id='trunkTBL']/div/div[2]/table/tbody/tr/td/table/tbody/tr[2]/td/font", "No users found");
	    }

		// Verify that the device was also created
	    myDebugPrinting("Verify that the device was also created", enumsClass.logModes.NORMAL);
		enterMenu(driver, enumsClass.menuNames.MONITOR_DEVICE_STATUS, "Devices Status");
	    if (username.contains("location")) {
	    	
			mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input"), "mac:" + readFile("mac_1.txt"), 3000);	
	    } else {
	    	
	    	mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input"), "user:" + dispName.trim(), 3000);
	    }
		driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).sendKeys(Keys.ENTER);	    
	    myWait(10000);  
	    waitForLoad(driver); 
	    if (isRegistered) { 
	    	
	    	if (!dispName.contains("location_2049")) {
	    		
	    		if (driver.findElement(By.tagName("body")).getText().contains("There are no devices that fit this search criteria")) {
	    			
	    			myFail("No users were found for <" + dispName + "> !!");
	    		}
			    searchStr(driver, dispName.trim()); 
			    for (int i = 0; i < 3; ++i) {
			    	
				    String txt = driver.findElement(By.tagName("body")).getText();
				    if (txt.contains("Approve")) {
				    	if (i > 3) {
				    		
				    		myFail("Approve button is displayed !!");
				    	} 
				    } else {
				    	
				    	break;
				    }
				    myWait(10000);
			    } 
	    	} else {
	    		
	    		searchStr(driver, "There are no devices that fit this search criteria");
	    	}	    	
	    } else {
	    	
	    	String txt = driver.findElement(By.tagName("body")).getText();
		    myAssertTrue("Approve button is not displayed !!\ntxt - " + txt, txt.contains("Approve"));
	    }
	  }
	
	  /**
	  *  Create device which added to user
	  *  @param driver       - given driver
	  *  @param username     - user name
	  *  @param deviceName   - device name
	  *  @param phoneType    - phone type
	  *  @param macAddName   - MAC address name
	  *  @param firmWareType - firmware name
	  */
	  public void addDevice(WebDriver driver, String username, String deviceName, String phoneType, String macAddName, String firmWareType) {
		  
		myClick(driver, By.xpath("//*[@id='searchusersform']/div/div/div/div/span/a/button/span"), 5000); 
		myClick(driver, By.xpath("//*[@id='results']/tbody/tr/td[9]/a[1]"), 5000);
		verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/form/div/div[2]/div[1]/h3", "Add new device to " + username.toLowerCase());
		
		// Set device name
		myDebugPrinting("Set device name " + deviceName, enumsClass.logModes.MINOR);
	    driver.findElement(By.xpath("//*[@id='display_name']")).clear();
	    driver.findElement(By.xpath("//*[@id='display_name']")).sendKeys(deviceName);
	    
	    // Set IP phone type	
		myDebugPrinting("Set IP phone type " + phoneType, enumsClass.logModes.MINOR);
		Select displayOptions = new Select(driver.findElement(By.xpath("//*[@id='ipphoneid']")));
		displayOptions.selectByVisibleText(phoneType);
		myWait(5000);
	      
	    // Set MAC address
		myDebugPrinting("Set MAC address " + macAddName, enumsClass.logModes.MINOR);
	    driver.findElement(By.xpath("//*[@id='macaddress_val']")).clear();
	    driver.findElement(By.xpath("//*[@id='macaddress_val']")).sendKeys(macAddName);
	    
	    // Set firmware
		myDebugPrinting("Set firmware " + firmWareType, enumsClass.logModes.MINOR);
		Select firmwareOptions = new Select(driver.findElement(By.xpath("//*[@id='firmware_id']")));
		firmwareOptions.selectByVisibleText(firmWareType);
		myWait(5000);
				
		// Submit & verify create
		myDebugPrinting("Submit & verify create", enumsClass.logModes.MINOR);	
	    myClick(driver, By.xpath("//*[@id='contentwrapper']/section/form/div/div[2]/div[3]/button[2]"), 7000);
		myClick(driver, By.xpath("//*[@id='modalContentId']/button[2]")   							  , 7000);
	    myClick(driver, By.xpath("//*[@id='contentwrapper']/section/form/div/div[2]/div[3]/button[1]"), 7000);
	    username = username.toLowerCase();
		searchUser(driver, username);  

		// Verify create
		searchUser(driver, username);  
	    myClick(driver, By.xpath("//*[@id='" + username + "tree']")			, 2000);
		searchStr(driver, "Devices Display name: " + deviceName);  
		searchStr(driver, "Template: " 			   + phoneType);  
		searchStr(driver, "MAC Address: " 		   + macAddName);
		searchStr(driver, "Firmware: " 			   + firmWareType);
	  }
	  
	  /**
	  *  Search user in the Manage multiple users menu by given username
	  *  @param driver     - given driver
	  *  @param deviceName - user name
	  */
	  public void searchUser(WebDriver driver, String username) {
		
			myDebugPrinting("username - " + username   ,  enumsClass.logModes.MINOR);
		    driver.findElement(By.xpath("//*[@id='searchtext']")).clear();
		    myWait(1000);
		    driver.findElement(By.xpath("//*[@id='searchtext']")).sendKeys(username);
		    driver.findElement(By.xpath("//*[@id='searchtext']")).sendKeys(Keys.ENTER);
		    myWait(5000);
		    myClick(driver, By.xpath("//*[@id='searchusersform']/div/div/div/div/span/a/button"), 5000); 
		    searchStr(driver, username); 
		    myWait(2000);
	  }
	
	  /**
	  *  Select multiple users in the Manage multiple users menu according to a given prefix
	  *  @param driver    - given driver
	  *  @param prefix    - prefix for search the created users
	  *  @param expNumber - the expected number of users
	  */
	  public void selectMultipleUsers(WebDriver driver, String prefix, String expNumber) {
		    		
		myDebugPrinting("selectMultipleUsers() - prefix - " + prefix + " expNumber - " + expNumber, enumsClass.logModes.NORMAL);
		mySendKeys(driver, By.xpath("//*[@id='filterinput']"), prefix, 5000);
		myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/form/div/div[2]/div/table/tbody/tr[2]/td/div/div/a/span"), 5000);      
		waitForLoad(driver); 		
		if (Integer.parseInt(expNumber) == 0) {
	    	
	      	myDebugPrinting("verify delete", enumsClass.logModes.NORMAL);
	    	verifyStrByXpath(driver, "//*[@id='modalContentId']", "No user found.");	
			myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);
	    	return;
	    }
		myDebugPrinting("Filter ended", enumsClass.logModes.MINOR);
		myClick(driver, By.xpath("//*[@id='maintable']/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/table/tbody/tr[4]/td/a"), 4000);
	    if (Integer.parseInt(expNumber) > 500) {
	    	
			myClick(driver, By.xpath("//*[@id='left_total_id']/a[1]"), 2000);		    
			myClick(driver, By.xpath("//*[@id='maintable']/tbody/tr[1]/td[1]/table/tbody/tr[2]/td[2]/table/tbody/tr[4]/td/a"), 2000);
			verifyStrByXpathContains(driver, "//*[@id='left_total_id']", "of " + expNumber + " users");	    	
	    } else if (Integer.parseInt(expNumber) == -1) {
	    	
	      	myDebugPrinting("users number is unknown ..", enumsClass.logModes.MINOR);
	    } else {
	    	
	    	verifyStrByXpath(driver, "//*[@id='left_total_id']", "Showing 1 to " + expNumber + " of " + expNumber);
	    }
	  }
	  
	  /**
	  *  Select multiple devices in the Manage multiple devices menu according to a given prefix
	  *  @param driver    - given driver
	  *  @param prefix    - prefix for search the created devices
	  *  @param expNumber - the expected number of devices
	  */
	  public void selectMultipleDevices(WebDriver driver, String prefix, String expNumber) {
		    		
		myDebugPrinting("selectMultipleDevices() - prefix - " + prefix + " expNumber - " + expNumber, enumsClass.logModes.NORMAL);
		mySendKeys(driver, By.xpath("//*[@id='filterinput']"), prefix, 5000);
		myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/form/div/div[2]/div/table/tbody/tr[2]/td/div/div/a/span"), 5000);
		if (Integer.parseInt(expNumber) == 0) {
	    	
	      	myDebugPrinting("verify delete", enumsClass.logModes.NORMAL);
	    	verifyStrByXpath(driver, "//*[@id='modalContentId']", "No device found.");	
			myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);
	    	return;
	    }
		myClick(driver, By.xpath("//*[@id='maintable']/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/table/tbody/tr[4]/td/a"), 7000);
	    if (Integer.parseInt(expNumber) > 500) {
	    	
			myClick(driver, By.xpath("//*[@id='left_total_id']/a[1]"), 2000);		    
			myClick(driver, By.xpath("//*[@id='maintable']/tbody/tr[1]/td[1]/table/tbody/tr[2]/td[2]/table/tbody/tr[4]/td/a"), 2000);
			verifyStrByXpathContains(driver, "//*[@id='left_total_id']", "of " + expNumber + " users");	    	
	    } else if (Integer.parseInt(expNumber) == -1) {
	    	
	      	myDebugPrinting("devices number is unknown ..", enumsClass.logModes.MINOR);
	    } else {
	    	
	    	verifyStrByXpath(driver, "//*[@id='left_total_id']", "Showing 1 to " + expNumber + " of " + expNumber);
	    }
	  }
	  
	  /**
	  *  Verify that all the actions were made
	  *  @param bodyText - text of the page
	  *  @param arg1	 - users prefix
	  *  @param arg2     - users number
	  *  @param arg3     - user initial prefix
	  *  @param arg4     - action suffix
	  */
	  private void verifyAction(String bodyText, String arg1, int arg2, int arg3, String arg4) throws IOException {

	    int lim     = arg2 + arg3;
	    String temp = "";
	    Boolean isEqSuffix = false;
	    Boolean isMac      = false;

	    if (arg4.equals(arg1)) {
	    	
	    	myDebugPrinting("isEqSuffix - true", enumsClass.logModes.MINOR);
	    	isEqSuffix = true;
	    }
	    if (arg4.equals("isMac")) {
	    	
	    	myDebugPrinting("isMac - true", enumsClass.logModes.MINOR);
	    	isMac = true;
	    }
	    temp = arg4;
		for (int i = arg3; i < lim; i++) {
			
			if (isEqSuffix) {
				
				temp = arg1 + i;
			}
			if (isMac) {
				
				temp = readFile("mac_" + i + ".txt");
				myDebugPrinting("temp - " + temp, enumsClass.logModes.MINOR);
			}
			if (arg2 == 1) {	
				
				if (!bodyText.contains(arg1 + " " + temp)) {
					
					myFail("Error, the string <" +  arg1 + " " + temp + "> was not recognized !!");
				} else {
					
					return;
				}
			}
			if (!bodyText.contains(arg1 + i + " " + temp)) {
				
				myDebugPrinting("The search prefix is:" + arg1 + i + " " + temp, enumsClass.logModes.MINOR);
				myDebugPrinting("bodyText - \n" + bodyText, enumsClass.logModes.MINOR);
				myFail("Error, the string <" +  arg1 + i + " " + temp + "> was not recognized !!");
			}
		}  
	  }
	  
	  /**
	  *  Enter a menu
	  *  @param driver       - given driver for make all tasks
	  *  @param mainpageDashboardAlarms 	 - given menu name for the paths function
	  *  @param verifyHeader - string for verify that enter the menu succeeded
	  */
	  public void enterMenu(WebDriver 	driver, menuNames mainpageDashboardAlarms, String verifyHeader) {
		  
		  myDebugPrinting("enterMenu  - " +  mainpageDashboardAlarms, enumsClass.logModes.NORMAL);
		  String paths[] = testMenuPaths.getPaths(mainpageDashboardAlarms);
		  int length = paths.length;
		  for (int i = 0; i < length; ++i) {
		  
			  if (paths[i].isEmpty()) {
				  
				  break;
			  }
			  myDebugPrinting("paths[" + i + "] - " +  paths[i], enumsClass.logModes.MINOR);
			  myWait(100);		   
			  myClick(driver, By.xpath(paths[i]), 5000);
		      waitForLoad(driver); 
		  }
		  String title = driver.findElement(By.tagName("body")).getText();
		  driver.switchTo().defaultContent();
		  if (!verifyHeader.isEmpty()) {
			  
			  Assert.assertTrue("Title (" + verifyHeader + ") was not found !! \n title - " + title, title.contains(verifyHeader));	  
			  myDebugPrinting("Title <" +  verifyHeader + "> was detected successfully !!", enumsClass.logModes.NORMAL);
		  } else {
			  
			  myFail("Given header is empty !!");
		  }
		  myWait(1000);
		  myDebugPrinting("enterMenu  - " +  mainpageDashboardAlarms + " ended successfully !!", enumsClass.logModes.NORMAL);
      }

	  /**
	  * Set a driver according to a given browser name
	  * @param  usedBrowser - given browser name (Chrome, FireFox or IE)
	  * @return driver      - driver object (Failure if usedBrowser is not a valid browser name)
	  */
	  @SuppressWarnings("incomplete-switch")
	  public WebDriver defineUsedBrowser(browserTypes usedBrowser) {
		  
		  // Clean working directory
		  deleteFilesByPrefix(System.getProperty("user.dir"), "ip_");
		  deleteFilesByPrefix(System.getProperty("user.dir"), "mac_");		  
		  switch (usedBrowser) {  
			  case CHROME:
				  ChromeOptions options = new ChromeOptions();			
				  options.addArguments("--start-maximized");			
				  return new ChromeDriver(options);
			  
			  case FF:	
				  myDebugPrinting(testVars.getGeckoPath());					
				  System.setProperty("webdriver.gecko.driver", testVars.getGeckoPath());					
				  System.setProperty("webdriver.firefox.marionette", "false");		
				  return new FirefoxDriver();			  
		  }
		  
		return null;
	  }
	  
	  /**
	  * Wait to a file from the au3 script that marks the end of users create
	  * @param filePath - given full path to file that marks a successful create
	  * @param maxWait  - max waiting time
	  */
	  public void waitForPostUsersCreate(String filePath, int maxWait) {
		  
	    File f = new File(filePath);
	    int i = 0;
	    while (true) {  
	    	
	    	if (i > maxWait) {
	    		
	    		myFail("maxWait was over .. (" + maxWait + ")");
	    	} else {
	    		
	    		i++;
			    if(f.exists() && !f.isDirectory() && i > 20) {
			    	
			    	break;
			    } else {
			    	
			    	myWait(1100);
			    	myDebugPrinting(i + ".waiting .. (" + maxWait + ")", enumsClass.logModes.MINOR);
			    }
	    	}	
	    }
    	myDebugPrinting("Waiting was over ..", enumsClass.logModes.MINOR);
	  }  
	  
	  /**
	  *  Perform an action to multiple selected users in the Manage multiple users menu
	  *  @param driver      - given driver
	  *  @param map         - abstract data for the function
	 *   @throws IOException 
	  */
	  public void setMultipleUsersAction(WebDriver driver, Map<String, String> map) throws IOException {
		
			String action     = map.get("action");
		    String usrsPrefix = map.get("usersPrefix");
		    String acSuffix   = map.get("srcUsername");
		    int    usrsNumber = Integer.parseInt(map.get("usersNumber"));
		    int    usrsInIdx  = Integer.parseInt(map.get("startIdx"));

			// Set action
			Select acionsList = new Select(driver.findElement(By.xpath("//*[@id='action']")));
			acionsList.selectByVisibleText(action);
			myWait(2000);
			
			// Set timing
			if (map.containsKey("timeoutValue")) {
					
				myDebugPrinting("timeoutValue - true", enumsClass.logModes.MINOR);
				String timeOutBetweenAction  = map.get("timeoutValue");			  
				myDebugPrinting("timeoutValue - " + timeOutBetweenAction, enumsClass.logModes.MINOR);				  
				new Select(driver.findElement(By.xpath("//*[@id='maintable']/tbody/tr[4]/td/div/select[2]"))).selectByValue(timeOutBetweenAction);;	  
				myWait(5000);  
			}
			
			// Perform action
			switch (action) {
			
				case "Delete Users":				
					myDebugPrinting("Enter Delete Users block", enumsClass.logModes.NORMAL);	 
					new Select(driver.findElement(By.xpath("//*[@id='maintable']/tbody/tr[4]/td/div/select[1]"))).selectByValue("1");
					myWait(2000);
					new Select(driver.findElement(By.xpath("//*[@id='maintable']/tbody/tr[4]/td/div/select[2]"))).selectByValue("2");
					myWait(2000);
					myClick(driver, By.xpath("//*[@id='deleteUsersTR']/td[1]/div/a"), 3000);
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the selected users?");					
					myClickNoWait(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);
					waitForLoad(driver);
				    
				    // test 34 - Stop and continue test
				    if (usrsPrefix.contains("stopAndContUser")) {
				    	
						myDebugPrinting("stopAndContUser - TRUE (stop action  at middle and than continue)", enumsClass.logModes.NORMAL);
					    myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/button[2]"), 20000);
						myDebugPrinting("STOP button was pressed ..", enumsClass.logModes.MINOR);
						String bodyText = driver.findElement(By.tagName("body")).getText();
					    myAssertTrue("Delete action continued although Stop was pressed /n bodyText - " + bodyText, bodyText.contains("Waiting"));
					    myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/button[3]"), 3000);
					    myDebugPrinting("Continue button was pressed ..", enumsClass.logModes.MINOR);
				    	waitTillString(driver, "Waiting");
				    }
				    // test 33 - Stop and not continue test
				    else if (usrsPrefix.contains("stopDelUser") && usrsNumber != -1) {
				    	
						myDebugPrinting("stopAndContUser - TRUE (stop action  at middle and than continue)", enumsClass.logModes.NORMAL);
					    myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/button[2]"), 20000);
						myDebugPrinting("STOP button was pressed ..", enumsClass.logModes.MINOR);
					    String bodyText = driver.findElement(By.tagName("body")).getText();
					    myAssertTrue("Delete action continued although Stop was pressed /n bodyText - " + bodyText, bodyText.contains("Waiting"));						
					    myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/button[1]"), 5000);
					    return;	
				    } else {
				    	
				    	waitTillString(driver, "Waiting");
				    	waitTillString(driver, "Loading");
				    	waitTillString(driver, "Performing");				    	
				    }
				    if (usrsPrefix.contains("lang")) {
				    	
				    	return;
				    } else {
				    	
				    	if (map.containsKey("skipVerifyDelete") && map.get("skipVerifyDelete").equals("true")) {
							
				    		myDebugPrinting("skipVerifyDelete - TRUE", enumsClass.logModes.MINOR);
				    		break;
				    	} else {
							
				    		myDebugPrinting("skipVerifyDelete - FALSE", enumsClass.logModes.MINOR);
				    		verifyAction(driver.findElement(By.tagName("body")).getText(), usrsPrefix, usrsNumber, usrsInIdx, acSuffix);
				    	}
				    }
				    break;
					
				case "Reset Users Passwords":
					String subAction   = map.get("subAction");
					String password    = map.get("password");
					if (!subAction.isEmpty() && subAction.equals("changePassword")) {
						
						myDebugPrinting("Enter Change Users Passwords block", enumsClass.logModes.NORMAL);	
						myClickNoWait(driver, By.xpath("//*[@id='resetPassword2All']"), 3000);
						myDebugPrinting("The password we want to modify is - " + password, enumsClass.logModes.MINOR);	
						mySendKeys(driver, By.xpath("//*[@id='resetToPassword']"), password, 3000);
					} else {
						
						myDebugPrinting("Enter Reset Users Passwords block", enumsClass.logModes.NORMAL);
					}
					myClickNoWait(driver, By.xpath("//*[@id='resetPasswordsTR']/td/div[1]/table/tbody/tr/td[4]/a"), 3000);
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to change the password to selected user(s) ?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);
					waitTillString(driver, "Waiting");
					break;
					
				case "Set Users Tenant":
					String region     = map.get("region");
					myDebugPrinting("Enter Set Users region block", enumsClass.logModes.NORMAL);
					myDebugPrinting("The wanted region is - " + region, enumsClass.logModes.MINOR);		
					Select regionsList = new Select(driver.findElement(By.xpath("//*[@id='branch']")));				
					regionsList.selectByVisibleText(region);
					myWait(2000);
					myClick(driver, By.xpath("//*[@id='resetTenantTR']/td/div/a[2]/span"), 7000);
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to change the tenant to selected user(s) ?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);
					waitTillString(driver, "Waiting");
					break;
					
				case "Update Configuration Files":
					myDebugPrinting("Enter Update Configuration Files block", enumsClass.logModes.NORMAL); 
					myClick(driver, By.xpath("//*[@id='updateConfigFilesTR']/td/div/a"), 3000);
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Note: update configuration command will work only on supported IP Phones.\nAre you sure you want to update the selected IP Phones files?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);
					waitTillString(driver, "Waiting");
					break;
					
				case "Generate IP Phones Configuration Files":
					myDebugPrinting("Enter Generate IP Phones Configuration Files block", enumsClass.logModes.NORMAL);				    
					myClick(driver, By.xpath("//*[@id='setIpPhonesTR']/td/div/div/a/span"), 3000);  
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "The configuration files will be generated to the location defined in the template (destinationDir)..\nDo you want to continue?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);	
			    	waitTillString(driver, "Waiting");
					break;				
					
				case "Send Message":
					myDebugPrinting("Enter Send Message block", enumsClass.logModes.NORMAL);
					String message = map.get("message");
				    mySendKeys(driver, By.xpath("//*[@id='msgText']"), message   , 5000);			
				    myClick(driver, By.xpath("//*[@id='sendMessageTR']/td/div/a"), 3000);	
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to send the message to the selected user(s) ?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);	
					waitTillString(driver, "Waiting");
					break;
					
				case "Delete User configuration":
					myDebugPrinting("Enter Delete User configuration block", enumsClass.logModes.NORMAL);
				    myClick(driver, By.xpath("//*[@id='deletePersonalInfoTR']/td[1]/div/a"), 3000);				    
					verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete User configuration");
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete User configuration from selected user(s) ?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);	
					
					// Check confirm box
					myDebugPrinting("Check confirm box", enumsClass.logModes.NORMAL);
					verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete User configuration");
					verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/thead/tr/th[1]", "Name");
					verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/thead/tr/th[2]", "Result");
					String dispPrefix0 = map.get("dispPrefix");
					for (int i = 1; i <= usrsNumber; ++i) {
						
						verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/tbody/tr[" + i + "]/td[1]", dispPrefix0 + "_" + i + "@" + testVars.getDomain());
						verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/tbody/tr[" + i + "]/td[2]", "User configuration was saved successfully.");
					}
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);	
					break;
					
				case "User configuration":
					myDebugPrinting("Enter User configuration block", enumsClass.logModes.NORMAL);
					String confKey    = map.get("confKey");
					myDebugPrinting("confKey - " + confKey, enumsClass.logModes.MINOR);
					
					// Features block
					if (confKey.contains("features")) {
						
						myDebugPrinting("Enter features configuration features block", enumsClass.logModes.NORMAL);
						if   	  (map.containsKey("daylight"))   {
							
						    myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[1]/button"), 3000);
							myDebugPrinting("Add daylight configuration values", enumsClass.logModes.NORMAL);
						    myClick(driver, By.xpath("//*[@id='daylight']"), 5000);
							verifyStrByXpath(driver, "//*[@id='modalTitleId']", "Daylight Savings Time");
						    myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000); 
						    
						    // Verify that all daylight values were added correctly
							myDebugPrinting("Verify that all daylight values were added correctly", enumsClass.logModes.MINOR);
						    String dayLightValues[] = {"system_daylight_saving_activate ENABLE"                ,
									  				   "system_daylight_saving_end_date_day 1" 		  	       ,
									  				   "system_daylight_saving_end_date_day_of_week SUNDAY"    ,
									  				   "system_daylight_saving_end_date_hour 2"		           ,
									  				   "system_daylight_saving_end_date_month 1"	           ,
									  				   "system_daylight_saving_start_date_week 1"		       ,
									  				   "system_daylight_saving_end_date_week 1"		       	   ,
									  				   "system_daylight_saving_mode FIXED"				       ,
									  				   "system_daylight_saving_offset 60"       		   	   ,
									  				   "system_daylight_saving_start_date_day 1"               ,
									  				   "system_daylight_saving_start_date_day_of_week SUNDAY"  ,									  					
									  				   "system_daylight_saving_start_date_hour 2"	           ,
									  				   "system_daylight_saving_start_date_month 1"	           ,
					  					 			   "system_daylight_saving_start_date_week 1"};
			 
							  int dayLightValuesNumber = dayLightValues.length;
							  for (int i = 0; i < dayLightValuesNumber; ++i) {
				  
								  myDebugPrinting(i + ". The searched value is: " + dayLightValues[i], enumsClass.logModes.MINOR);
								  searchStr(driver, dayLightValues[i]);	  
								  myWait(2000); 
							  }	  
							  
						} else if (map.containsKey("telnet"))     {
							
							myDebugPrinting("Add Telnet-access value", enumsClass.logModes.NORMAL);
							for (int i = 0; i < 2; ++i) {
								
							    myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[1]/button"), 3000);
							    myClick(driver, By.xpath("//*[@id='telnet']")											, 7000);	
								myDebugPrinting("Create configuration value with value - " + i, enumsClass.logModes.MINOR);
								verifyStrByXpath(driver, "//*[@id='modalTitleId']"								   , "Activate Telnet access");
								verifyStrByXpath(driver, "//*[@id='modalContentId']/table/tbody/tr[1]/td/div/label", "Activate Telnet access");
								if (i == 1) {
									
									myClickNoWait(driver, By.xpath("//*[@id='management_telnet_enabled']"), 3000);
								}
								myClickNoWait(driver, By.xpath("/html/body/div[2]/div/button[1]")		, 3000);  
							    searchStr(driver, "management_telnet_enabled " + i);	
							}
							
						} else if (map.containsKey("pinLock"))  {	
							
							myDebugPrinting("Add PIN-lock value", enumsClass.logModes.NORMAL);
							for (int i = 0; i < 2; ++i) {
								
							    myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[1]/button"), 3000);
							    myClick(driver, By.xpath("//*[@id='pinlock']")											, 7000);	
								myDebugPrinting("Create configuration value with value - " + i, enumsClass.logModes.MINOR);
								verifyStrByXpath(driver, "//*[@id='modalTitleId']"								   , "Pin Lock");
								verifyStrByXpath(driver, "//*[@id='modalContentId']/table/tbody/tr[1]/td/div/label", "Enable Pin Lock");
								if (i == 1) {
									
									myClickNoWait(driver, By.xpath("//*[@id='system_pin_lock_enabled']"), 3000);
								}
								myClickNoWait(driver, By.xpath("/html/body/div[2]/div/button[1]")		, 3000);  
							    searchStr(driver, "system_pin_lock_enabled " + i);	
							}
							
						} else if (map.containsKey("capProfile")) {
							
							myDebugPrinting("Set CAP profile", enumsClass.logModes.NORMAL);
						    myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[1]/button"), 3000);
						    myClick(driver, By.xpath("//*[@id='CAP Profile']")									, 7000);				
							verifyStrByXpath(driver, "//*[@id='modalTitleId']"								   , "CAP Profile");

							// Check that default configuration (three first check-boxes are checked) is set
							myDebugPrinting("Check that defauult configuration (three first check-boxes are checked) is set", enumsClass.logModes.MINOR);
							myAssertTrue("Checkbox is not checked at default !!", driver.findElement(By.xpath("//*[@id='voip_common_area_is_cap_device']")).getAttribute("checked") 			   != null);
							myAssertTrue("Checkbox is not checked at default !!", driver.findElement(By.xpath("//*[@id='lync_corporate_directory_enabled']")).getAttribute("checked")			   != null);
							myAssertTrue("Checkbox is not checked at default !!", driver.findElement(By.xpath("//*[@id='lync_userSetting_prevent_user_sign_out']")).getAttribute("checked")        != null);
							myAssertTrue("Checkbox is checked at default !!", driver.findElement(By.xpath("//*[@id='lync_calendar_enabled']")).getAttribute("checked") 				  			   == null);
							myAssertTrue("Checkbox is checked at default !!", driver.findElement(By.xpath("//*[@id='lync_VoiceMail_enabled']")).getAttribute("checked") 			  			   == null);
							myAssertTrue("Checkbox is checked at default !!", driver.findElement(By.xpath("//*[@id='lync_BToE_enable']")).getAttribute("checked") 					  			   == null);
							myAssertTrue("Checkbox is checked at default !!", driver.findElement(By.xpath("//*[@id='voip_line_0_call_forward_enabled']")).getAttribute("checked") 	  			   == null);
							myAssertTrue("Checkbox is checked at default !!", driver.findElement(By.xpath("//*[@id='voip_services_do_not_disturb_enabled']")).getAttribute("checked") 			   == null);
							myAssertTrue("Checkbox is checked at default !!", driver.findElement(By.xpath("//*[@id='system_pin_lock_enabled']")).getAttribute("checked")			  			   == null);
							myAssertTrue("Checkbox is checked at default !!", driver.findElement(By.xpath("//*[@id='system_enable_key_configuration']")).getAttribute("checked") 	  			   == null);

							// Check all available check-boxes
							myDebugPrinting("Check all avalible check-boxes", enumsClass.logModes.MINOR);
							myClickNoWait(driver, By.xpath("//*[@id='lync_calendar_enabled']")			   , 2000);				
							myClickNoWait(driver, By.xpath("//*[@id='lync_VoiceMail_enabled']")			   , 2000);				
							myClickNoWait(driver, By.xpath("//*[@id='lync_BToE_enable']")					   , 2000);				
							myClickNoWait(driver, By.xpath("//*[@id='voip_line_0_call_forward_enabled']")    , 2000);				
							myClickNoWait(driver, By.xpath("//*[@id='voip_services_do_not_disturb_enabled']"), 2000);				
							myClickNoWait(driver, By.xpath("//*[@id='system_pin_lock_enabled']")			   , 2000);				
							myClickNoWait(driver, By.xpath("//*[@id='system_enable_key_configuration']")	   , 2000);				
							myClickNoWait(driver, By.xpath("/html/body/div[2]/div/button[1]")				   , 5000);  
						    myWait(5000);
						    
							// Check that all values were added
							myDebugPrinting("Check that all values were added", enumsClass.logModes.MINOR);
						    searchStr(driver, "voip_common_area_is_cap_device 1");
						    searchStr(driver, "lync_corporate_directory_enabled 1");
						    searchStr(driver, "lync_userSetting_prevent_user_sign_out 1");
						    searchStr(driver, "lync_calendar_enabled 1");
						    searchStr(driver, "lync_VoiceMail_enabled 1");
						    searchStr(driver, "lync_BToE_enable 1");
						    searchStr(driver, "voip_line_0_call_forward_enabled 1");
						    searchStr(driver, "voip_services_do_not_disturb_enabled 1");
						    searchStr(driver, "system_pin_lock_enabled 1");
						    searchStr(driver, "system_enable_key_configuration 1"); 
						    
						} else if (map.containsKey("deleteAllConfValues"))  {	
						
							myDebugPrinting("Delete all configuration values", enumsClass.logModes.NORMAL);	    
							myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[2]/button")		 , 3000);
							myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[5]/div[2]/ul/li[1]/a/span"), 7000);
							verifyStrByXpath(driver, "//*[@id='modalTitleId']"	, "Delete configuration settings");
							verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete all configuration settings and save empty content?");
							myClickNoWait(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);  
							myClickNoWait(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);  

					
						    // Verify delete
							myDebugPrinting("Verify delete", enumsClass.logModes.MINOR);		  
							String bodyText     = driver.findElement(By.tagName("body")).getText();
							myAssertTrue("Value was still detected !!", !bodyText.contains("voip_common_area_is_cap_device"));
							myAssertTrue("Value was still detected !!", !bodyText.contains("system_enable_key_configuration"));
						}
										
						return;
					}		
					
					// Regular configuration key
					String confVal    = map.get("confValue");
					String dispPrefix = map.get("dispPrefix");
					myDebugPrinting("confVal - " + confVal, enumsClass.logModes.MINOR);
					
					// Add configuration key
					mySendKeys(driver, By.xpath("//*[@id='ini_name']"), confKey, 7000);	
				    mySendKeys(driver, By.xpath("//*[@id='ini_value']"),confVal, 7000);			    				    
				    myClickNoWait(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[3]/a/span"), 5000);    
				    
				    // Verify that configuration key was added
					myDebugPrinting("Verify that configuration key was added", enumsClass.logModes.MINOR); 
				    searchStr(driver, confKey);
				    searchStr(driver, confVal);
				    
				    // Save configuration key
					myDebugPrinting("Save configuration key", enumsClass.logModes.MINOR); 
				    myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[4]/a"), 7000);    
					verifyStrByXpath(driver, "//*[@id='modalTitleId']", "Save Configuration");
					verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/thead/tr/th[1]", "Name");
					verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/thead/tr/th[2]", "Result");
					for (int i = 1; i <= usrsNumber; ++i) {
						
						verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/tbody/tr[" + i + "]/td[1]", dispPrefix + "_" + i + "@" + testVars.getDomain());
						verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/tbody/tr[" + i + "]/td[2]", "User configuration was saved successfully.");		
					}
				    myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000); 
					break;
					
				case "Restart Devices":
					myDebugPrinting("Enter Restart Devices block", enumsClass.logModes.NORMAL);
					String resetMode = map.get("resMode");
					Select resetList = new Select(driver.findElement(By.xpath("//*[@id='resetIpPhonesTR']/td/div[1]/select")));
					resetList.selectByVisibleText(resetMode);				
				    myWait(2000);
					myDebugPrinting("Reset mode is - " + resetMode, enumsClass.logModes.MINOR);
					if (resetMode.equals("Scheduled")) {
						
						String scDelay = map.get("scMinutes");
						Select resetList2 = new Select(driver.findElement(By.xpath("//*[@id='resetIpPhonesTR']/td/div[1]/span/select")));			
						resetList2.selectByVisibleText(scDelay);
						myWait(2000);
					}
					myClick(driver, By.xpath("//*[@id='resetIpPhonesTR']/td/div[1]/a"), 3000);
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Note: restart command will work only on supported IP Phones.\nAre you sure you want to restart the selected IP Phones?");		
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);	
					waitTillString(driver, "Waiting");
					break;
					
				default:
					myFail("Action is not recognized !!");	
					break;
				}	
	  }
	  
	  /**
	  *  Perform an action to multiple selected devices in the Manage multiple devices menu
	  *  @param driver        - given driver
	  *  @param map           - abstract data for the function
	  *   @throws IOException 
	  */
	  public void setMultipleDevicesAction(WebDriver driver, Map<String, String> map) throws IOException {
		  
		  // Set action
		  String action     = map.get("action");
		  Select devAction = new Select(driver.findElement(By.xpath("//*[@id='action']")));	    
		  devAction.selectByVisibleText(action);
		  myWait(3000);
		  
		  // Set timing
		  if (map.containsKey("timeoutValue")) {
				
			  myDebugPrinting("timeoutValue - true", enumsClass.logModes.MINOR);
			  String timeOutBetweenAction  = map.get("timeoutValue");			  
			  myDebugPrinting("timeoutValue - " + timeOutBetweenAction, enumsClass.logModes.MINOR);				  
			  new Select(driver.findElement(By.xpath("//*[@id='maintable']/tbody/tr[4]/td/div/select[2]"))).selectByValue(timeOutBetweenAction);;	  
			  myWait(2000);
			  return;
		  }
	
		  // Perform action
		  switch (action) {	
			
				case "Change Language":
					myDebugPrinting("Enter Change Language block", enumsClass.logModes.NORMAL);
					String language = map.get("language");
				    Select langs = new Select(driver.findElement(By.xpath("//*[@id='deviceLanguage']")));	    
				    langs.selectByVisibleText(language);
					myWait(2000);
					myClick(driver, By.xpath("//*[@id='changeLanguageTR']/td/div[1]/a[2]"), 3000);	
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to change the device's language?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);	
					waitTillString(driver, "Waiting");
					break;
					
				case "Send Message":
					myDebugPrinting("Enter Send Message block", enumsClass.logModes.NORMAL);
					String message = map.get("message");	
					myClick(driver, By.xpath("//*[@id='sendMessageTR']/td/div/input"), 2000);				
					mySendKeys(driver, By.xpath("//*[@id='sendMessageTR']/td/div/input"), message, 4000);
					myClick(driver, By.xpath("//*[@id='sendMessageTR']/td/div/a"), 2000);
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to send message to the selected devices?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);	
					waitTillString(driver, "Waiting");
					break;
					
				case "Change Template":
					myDebugPrinting("Enter Change Template block", enumsClass.logModes.NORMAL);
					String phoneType = map.get("phoneType");
				    Select templates = new Select(driver.findElement(By.xpath("//*[@id='ipptype']")));	    
				    templates.selectByVisibleText(phoneType);
					myWait(2000);
					myClick(driver, By.xpath("//*[@id='changeTypeTR']/td/div[1]/a[2]"), 2000);	
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to change the Template of the selected devices?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);	
					waitTillString(driver, "Waiting");
					break;
					
				case "Restart Devices":
					myDebugPrinting("Enter Restart Devices block", enumsClass.logModes.NORMAL);
					String resMode = map.get("resMode");
					myDebugPrinting("resMode - " + resMode, enumsClass.logModes.MINOR);
					Select resSelectMode = new Select(driver.findElement(By.xpath("//*[@id='resetIpPhonesTR']/td/div[1]/select")));
					resSelectMode.selectByVisibleText(resMode);
					myWait(2000);
					if (resMode.equals("Scheduled")) {
						
						String scTime = map.get("schTime");
						myDebugPrinting("scTime - " + scTime, enumsClass.logModes.MINOR);
						Select scTimeSelect = new Select(driver.findElement(By.xpath("//*[@id='resetIpPhonesTR']/td/div[1]/span/select")));
						scTimeSelect.selectByVisibleText(scTime);
						myWait(2000);
					}
				    myClick(driver, By.xpath("//*[@id='resetIpPhonesTR']/td/div[1]/a"), 2000);
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Note: restart command will work only on supported IP Phones.\nAre you sure you want to restart the selected IP Phones?");
				    myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);
					waitTillString(driver, "Waiting");
					break;	
					
				case "Generate IP Phones Configuration Files":
					myDebugPrinting("Enter Generate IP Phone Configuration block", enumsClass.logModes.NORMAL);  
				    myClick(driver, By.xpath("//*[@id='setIpPhonesTR']/td/div/div/a"), 2000);
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "The configuration files will be generated to the location defined in the template (destinationDir).\nDo you want to continue?");
				    myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);
					waitTillString(driver, "Waiting");
					break;
					
				case "Update Configuration file":
					myDebugPrinting("Enter Update Configuration block", enumsClass.logModes.NORMAL); 
				    myClick(driver, By.xpath("//*[@id='updateConfigFilesTR']/td/div/a"), 5000);
					verifyStrByXpath(driver, "//*[@id='modalContentId']", "Note: update configuration command will work only on supported IP Phones.\nAre you sure you want to update the selected IP Phones files?");
				    myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);
					waitTillString(driver, "Waiting");
					break;
					
				case "Change Firmware":
					myDebugPrinting("Enter Change Firmware block", enumsClass.logModes.NORMAL);
					String firmware = map.get("firmware");
				    Select firmTypes = new Select(driver.findElement(By.xpath("//*[@id='firmware_id']")));
				    firmTypes.selectByVisibleText(firmware);
					myWait(2000);
				    myClick(driver, By.xpath("//*[@id='updateFirmwareTR']/td/div[1]/a[2]"), 5000);
				    verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to change the device's firmware?");
				    myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);
					waitTillString(driver, "Waiting");
					break;
					
				case "Change VLAN Discovery Mode":
					myDebugPrinting("Enter Change VLAN Discovery Mode block", enumsClass.logModes.NORMAL);
					String vlanMode = map.get("vlanMode");
					myDebugPrinting("vlanMode - " + vlanMode, enumsClass.logModes.MINOR);				
				    Select vlanTypes = new Select(driver.findElement(By.xpath("//*[@id='changeVlanTR']/td/div[1]/select")));
				    vlanTypes.selectByVisibleText(vlanMode);
				    myWait(2000);
				    if (vlanMode.equals("Manual Configuration")) {
				    	
						String vlanId = map.get("vlanId");
						String vlanpr = map.get("vlanPriority");
						mySendKeys(driver, By.xpath("//*[@id='changeVlanTR']/td/div[2]/input[1]"), vlanId, 3000);	
						mySendKeys(driver, By.xpath("//*[@id='changeVlanTR']/td/div[2]/input[2]"), vlanpr, 3000);	
				    } else if (vlanMode.equals("Disabled")) {
				    	
				    	return;
				    }
				    myClick(driver, By.xpath("//*[@id='changeVlanTR']/td/div[1]/a[2]"), 5000);
				    verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to change the device's VLAN discovery mode?");
				    myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
					waitTillString(driver, "Waiting");
					break;	
					
				case "Delete Devices":
					myDebugPrinting("Enter Delete Devices block", enumsClass.logModes.NORMAL);
					myClick(driver, By.xpath("//*[@id='deleteDevicesTR']/td/div/a"), 2000);	
				    verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the selected devices?");
					myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);	
					waitTillString(driver, "Waiting");
					break;
					
				default:
					myFail("Action is not recognized !!");	
					break;
		  }  
	  }
	  
	  /**
	  *  Add new template according to given parameters
	  *  @param driver   - given driver
	  *  @param tempName - given template name
	  *  @param tempDesc - given template description
	  *  @param tenant   - given template tenant
	  *  @param model    - given template model
	  *  @param map      - object for all the optional parameters
	  */
	  public void addTemplate(WebDriver driver, String tempName, String tempDesc, String tenant, String model, Map<String, String> map) {
		  
		// Create new template
		myDebugPrinting("Create new template"    , enumsClass.logModes.MINOR);
		myDebugPrinting("tempName - "  + tempName, enumsClass.logModes.MINOR);
		myDebugPrinting("model - "     + model    , enumsClass.logModes.MINOR);
		
		myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/buttton"), 4000);
		verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3"		   , "Add new Template");
		mySendKeys(driver, By.xpath("//*[@id='modelname']")  , tempName, 2000);	
		mySendKeys(driver, By.xpath("//*[@id='description']"), tempDesc, 2000);
		Select tenantType = new Select (driver.findElement(By.xpath("//*[@id='tenant_id']")));
		tenantType.selectByVisibleText(tenant);
		myWait(2000);
		Select tempType = new Select (driver.findElement(By.xpath("//*[@id='model_type']")));
		tempType.selectByVisibleText(model);
		myWait(2000);
	  
		// Check region default template check-box
		if (map.get("isRegionDefault").equals("true")) {
			
			myDebugPrinting("isRegionDefault - TRUE", enumsClass.logModes.MINOR);
		}
	  
		// Is clone from other template is needed
		String tempTemplate = map.get("cloneFromtemplate");
		if (!tempTemplate.isEmpty() /*&& map.get("cloneFromtemplate").equals("true")*/) {
			
			myDebugPrinting("cloneFromtemplate is not empty, clone starts !"      , enumsClass.logModes.MINOR);
			myDebugPrinting("cloneFromtemplate - " + tempTemplate , enumsClass.logModes.MINOR);		
			Select cloneTempName = new Select (driver.findElement(By.xpath("//*[@id='clone_model_id']")));
			cloneTempName.selectByVisibleText(tempTemplate);
			myWait(5000);
		}
	  
		// Is download shared templates
		if (map.get("isDownloadSharedTemplates").equals("true")) {
			
			myDebugPrinting("isDownloadSharedTemplates - TRUE", enumsClass.logModes.MINOR);	
			
			// Enter the Sharefiles menu
			myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div/table/tbody/tr[7]/td/a"), 15000);	
			ArrayList<?> tabs = new ArrayList<Object> (driver.getWindowHandles());
			driver.switchTo().window((String) tabs.get(1));
			
			for (int i = 0; i < 20; ++i) {
				
				myWait(5000);
				myDebugPrinting("Check if menu was opened", enumsClass.logModes.MINOR);	
				if (driver.findElement(By.tagName("body")).getText().contains("I agree: Terms and Conditions")) {
					
					break;
				}
			}
			myDebugPrinting("Check Sharefiles menu buttons", enumsClass.logModes.MINOR);	
			String txt = driver.findElement(By.tagName("body")).getText();
			myAssertTrue("Latest header was not detected !! ("  + txt + ")", txt.contains("Latest EMS IPP configuration templates"));
			myAssertTrue("Confirm header was not detected !! (" + txt + ")", txt.contains("I agree: Terms and Conditions"));
			
			// Click on Confirm and Select-all check-boxes
			myDebugPrinting("Click on Confirm and Select-all checkboxes", enumsClass.logModes.MINOR);	
			myClick(driver, By.xpath("//*[@id='applicationHost']/div/div[1]/div/div[1]/div[5]/div/div[1]/label/div[1]/span/span")   ,  3000);		
			myClick(driver, By.xpath("//*[@id='applicationHost']/div/div[1]/div/div[1]/div[7]/div[2]/div/div[1]/label/div/span/span"), 3000);	
			String shareFilesFile = testVars.getShareFilesName();
			myDebugPrinting("shareFilesFile - " + shareFilesFile, enumsClass.logModes.MINOR);	
			deleteFilesByPrefix(testVars.getDownloadsPath(), shareFilesFile);		
			
			// Click on Download and check the download
			myClick(driver, By.className("button_1rm2hy7"), 60000);
			//*[@id="applicationHost"]/div/div[1]/div/div[1]/div[7]/div[2]/div/div[2]/div[2]/button/div			
			myAssertTrue("File was not downloaded successfully !!", findFilesByGivenPrefix(testVars.getDownloadsPath(), shareFilesFile));
			deleteFilesByPrefix(testVars.getDownloadsPath(), shareFilesFile);		
			return;
		}	
		
		// Is two templates create or bad name
		if (map.containsKey("secondCreate")) {
			
			myDebugPrinting("secondCreate - TRUE (create two templates with same name test)", enumsClass.logModes.NORMAL);
			myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 3000);		
			verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save New Template");
			verifyStrByXpath(driver, "//*[@id='modalContentId']", "Failed to create new template. Reason: this template name exists.");
			myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);				
			myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[1]"), 3000);		
			return;
			
		} else if (map.containsKey("falseCreateName")) {
			
			myDebugPrinting("falseCreateName - TRUE (false data)", enumsClass.logModes.MINOR);
			tempName = map.get("falseCreateName");
			tempDesc = map.get("falseCreateDesc");
			
			// Try to confirm empty name
			myDebugPrinting("Try to confirm empty name", enumsClass.logModes.MINOR);
			myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 3000);	
			verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[1]/div[3]/h4", "Please enter template name");
			mySendKeys(driver, By.xpath("//*[@id='modelname']")  , tempName, 2000);	
			
			// Try to confirm empty description
			myDebugPrinting("Try to confirm empty description", enumsClass.logModes.MINOR);
			myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 3000);	
			verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[1]/div[3]/h4", "Please enter template description");
			mySendKeys(driver, By.xpath("//*[@id='description']"), tempDesc, 2000);
		}
		
		// Is two templates create or bad name
		if (map.containsKey("secondCreate")) {
			
			myDebugPrinting("secondCreate - TRUE (create two templates with same name test)", enumsClass.logModes.NORMAL);
			myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 3000);		
			verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save New Template");
			verifyStrByXpath(driver, "//*[@id='modalContentId']", "Failed to create new template. Reason: this template name exists.");
			myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);				
			myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[1]"), 3000);		
			return;
		}
		
		// Submit
		myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 7000);
		if (map.containsKey("speicalChrsName")) {
			
			myDebugPrinting("speicalChrsName - TRUE (name with special characters)", enumsClass.logModes.NORMAL);
			verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[1]/div[3]/h4"  , "Invalid template name. When naming your template, avoid characters incompatible with the file system: <, >, :, |, ?, *, double quote, (period) or a space at the end, etc.");
			myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[1]"), 7000);	
			return;
		}
		
		
		verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save New Template");
		verifyStrByXpath(driver, "//*[@id='modalContentId']", "Add new template of tenant successfully.");
		myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);	
		verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "IP Phones Configuration Templates");

		// Verify create
		String currText     = driver.findElement(By.tagName("body")).getText();
	    Assert.assertTrue("Text not found!" 
	    				   + "\ncurrTemplate - " + tempName
	    				   + "\ncurrText - "     + currText    , currText.contains(tempName)); 	
	  }
	  
	  /**
	  *  Delete a template
	  *  @param driver       - given driver
	  *  @param tempName     - given template name
	  *  @throws IOException 
	  */
	  public void deleteTemplate(WebDriver driver, String tempName) throws IOException {
		  
		  myDebugPrinting("tempName - " + tempName, enumsClass.logModes.MINOR);	  
		  
		  // Get idx
		  BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		  String l = null;
		  int i = 1;
		  while ((l = r.readLine()) != null) {
			  
			  myDebugPrinting("i - " + i + " " + l, enumsClass.logModes.DEBUG);
			  if (l.contains(tempName)) {
				  
				  myDebugPrinting("i - " + i, enumsClass.logModes.MINOR);
				  break;
			  }
			  if (l.contains("Edit" )) {
				  
				  ++i;
			  }
		  } 
		  
		  myClick(driver, By.xpath("//*[@id='tenants1']/tbody/tr[" + i + "]/td[8]/div/buttton[2]"), 3000);		  
		  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete Template");
		  verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the " + tempName + " IP Phone Model?");
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
		  verifyStrByXpath(driver, "//*[@id='modalContentId']", "Delete template successfully.");
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);

		  // Verify delete
		  String bodyText = driver.findElement(By.tagName("body")).getText();
		  myAssertTrue("Delete didn't succeed !! (" + tempName + " still appear)", !bodyText.contains(tempName));  
	  }
	  
	  /**
	  *  Edit a template
	  *  @param driver     					- given driver
	  *  @param tempName   					- given template name
	  *  @param map 	   					- data structure of strings for edit
	  *  @throws IOException 
	  *  @throws UnsupportedFlavorException 
	  */
	  public void editTemplate(WebDriver driver, String tempName, Map<String, String> map) throws UnsupportedFlavorException, IOException {
		  
		  myDebugPrinting("tempName - " + tempName, enumsClass.logModes.MINOR);
		  
		  // Get idx
		  BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		  String l = null;
		  int i = 1;
		  while ((l = r.readLine()) != null) {
			  
			  myDebugPrinting("i - " + i + " " + l, enumsClass.logModes.DEBUG);
			  if (l.contains(tempName)) {
				  
				  myDebugPrinting("i - " + i, enumsClass.logModes.MINOR);
				  break;
			  }
			  if (l.contains("Edit" )) {
				  
				  ++i;
			  }
		  } 
		  myClick(driver, By.xpath("//*[@id='tenants1']/tbody/tr[" + i + "]/td[8]/div/buttton[1]"), 3000);		  	  
		  verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3"        , "IP Phone " + tempName + " Configuration Template");

		  // Edit template
		  myDebugPrinting("Edit template", enumsClass.logModes.NORMAL); 		  
		  if (map.containsKey("tenant")) {
			  
			  Select tenId  = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
			  tenId.selectByVisibleText(map.get("tenant"));
				myWait(5000);
		  }
		  if (map.containsKey("model")) {
			  
			  Select motype = new Select(driver.findElement(By.xpath("//*[@id='model_type']")));		
			  motype.selectByVisibleText(map.get("model"));
				myWait(5000);
		  }
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[3]/div/div[3]/button"), 7000);	
		  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update configuration template");
		  verifyStrByXpath(driver, "//*[@id='modalContentId']", "Successful to update configuration template.");
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);
		
		  // Verify edit
		  myDebugPrinting("Verify edit", enumsClass.logModes.NORMAL);  
		  r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		  l = null;
		  while ((l = r.readLine()) != null) {
			  
			  myDebugPrinting(l, enumsClass.logModes.DEBUG);
			  if (l.contains(tempName)) {
				  
				  if (map.containsKey("tenant")) {
					  				  
					  myAssertTrue("Tenenat was not edited !!", l.contains(map.get("tenant")));
				  }
				  if (map.containsKey("model")) {
					  
					  myAssertTrue("Model was not edited !!", l.contains(map.get("model")));
				  } 
				  break;
			  }
		  }		  
	  }
	  
	  /**
	  *  Copy a placeholder from another Tenant
	  *  @param driver      - given driver
	  *  @param tenThatCopy - tenant to whom who copy a placeholder
	  *  @param tenPhName   - given tenant placeholder name which was copied
	  *  @param tenValue    - given tenant placeholder value which was copied
	  *  @param tenTenant   - given tenant-placeholder tenant we copied from
	  */
	  public void copyPlaceholder(WebDriver driver, String tenThatCopy, String tenPhName, String tenValue, String tenTenant) {
		 
		 // Copy PH
		 myDebugPrinting("Copy PH", enumsClass.logModes.NORMAL);
		  
		 // Select new Tenant
		 myDebugPrinting("Select new Tenant", enumsClass.logModes.MINOR);		  
		 Select tenId = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
		 tenId.selectByVisibleText(tenThatCopy);
		 myWait(5000);
		 
		 // Copy the PH from original tenant
		 myDebugPrinting("Copy the PH from original tenant", enumsClass.logModes.MINOR);		  
		 myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[4]/div[2]/div/span[1]/a"), 5000);
		 verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Copy Tenant Placeholders From");  
		 verifyStrByXpath(driver, "//*[@id='modalContentId']", "Please select the Tenant Placeholders to copy.");  
		 Select tenFromCopy = new Select(driver.findElement(By.xpath("/html/body/div[2]/div/select")));
		 tenFromCopy.selectByVisibleText(tenTenant);
		 myWait(5000);
		 myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);
		 verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Copy Tenant Placeholders From");  
		 myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);
		  
		 // Verify copy
		 myDebugPrinting("Verify copy", enumsClass.logModes.MINOR);
		 mySendKeys(driver, By.xpath("//*[@id='tenants1-filtering']"), tenPhName , 6000);
		 searchStr(driver, tenPhName);
		 searchStr(driver, tenValue);
		 searchStr(driver, tenTenant);
	  }
	  
	  /**
	  *  Add new Template placeholder
	  *  @param driver      	  - given driver
	  *  @param tempName     	  - template for which we create the placeholder
	  *  @param tempPhName   	  - name for the Template placeholder
	  *  @param tempPhValue       - value for the Template placeholder
	  *  @param tempPhDescription - description for the Template placeholder
	  */  
	  public void addTemplatePlaceholder(WebDriver driver, String tempName, String tempPhName, String tempPhValue, String tempPhDescription) {
		    
		  // Select a model
		  Select models = new Select(driver.findElement(By.xpath("//*[@id='models']")));
		  models.selectByVisibleText(tempName);
		  myWait(5000);
		  
		  // Fill data
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[8]/div[2]/table/tbody/tr[1]/td/table/tbody/tr[1]/td[2]/a"), 5000);

		  // Nir: bug, switch Template and press Add cause the pre-last Template to appear
//		  verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div", "IP Phone Model - " + tempName);
		  mySendKeys(driver, By.id("ph_name")  , tempPhName       , 2000);  
		  myClick(driver, By.id("ph_value"), 2000);
		  mySendKeys(driver, By.id("ph_value") , tempPhDescription, 2000);
		  myClick(driver, By.id("ph_desc"), 2000);
		  mySendKeys(driver, By.id("ph_desc")  , tempPhValue      , 2000);
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[4]/button[2]"), 6000);
		  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save New Placeholder.");
		  verifyStrByXpath(driver, "//*[@id='modalContentId']", "The new placeholder was saved successfully.");
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);  
		  
		  // Verify create
		  myDebugPrinting("verify create", enumsClass.logModes.MINOR);
		  searchStr(driver, "%ITCS_" + tempPhName + "%");
		  searchStr(driver, tempPhValue);
		  searchStr(driver, tempPhDescription);
	  }  
	  
	  /**
	  *  Add another Template placeholder with the same name
	  *  @param driver      	  - given driver
	  *  @param tempName     	  - template for which we create the placeholder
	  *  @param tempPhName   	  - name for the Template placeholder
	  */  
	  public void addTemplatePlaceholder(WebDriver driver, String tempName, String tempPhName) {
		   
		  // Add another existing Template PH
		  myDebugPrinting("Add another existing Template PH", enumsClass.logModes.NORMAL);
		  
		  // Select a model
		  Select models = new Select(driver.findElement(By.xpath("//*[@id='models']")));
		  models.selectByVisibleText(tempName);
		  myWait(5000);
		  
		  // Fill data and verify error
		  myDebugPrinting("Fill data and verify error", enumsClass.logModes.MINOR);
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[8]/div[2]/table/tbody/tr[1]/td/table/tbody/tr[1]/td[2]/a"), 7000);
		  mySendKeys(driver, By.xpath("//*[@id='ph_name']") , tempPhName       , 2000);
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[4]/button[2]"), 7000);
		  searchStr(driver, "Failed to save new placeholder. The name is in use.");
	  }
	  
	  /**
	  *  Edit an existing Template placeholder
	  *  @param driver      	  - given driver
	  *  @param tempName     	  - template for which we edit the placeholder
	  *  @param tempPhName   	  - name for edit in the Template placeholder
	  *  @param tempPhValue       - value for edit in the Template placeholder
	  *  @param tempPhDescription - description for edit in the Template placeholder
	  *  @throws IOException
	  */ 
	  public void editTemplatePlaceholder(WebDriver driver,  String tempName, String tempPhName, String tempPhValue, String tempPhDescription) throws IOException {
		   
		  // Select a model
		  myDebugPrinting("Select a model", enumsClass.logModes.MINOR);
		  Select models = new Select(driver.findElement(By.xpath("//*[@id='models']")));
		  models.selectByVisibleText(tempName);
		  myWait(5000);  
		  
		  // Get idx
		  BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		  String l = null;
		  int i = 0;
		  myDebugPrinting("tempPhName - " + tempPhName, enumsClass.logModes.MINOR);  
		  while ((l = r.readLine()) != null) {
			  
			  myDebugPrinting(l, enumsClass.logModes.MINOR); 
			  if (l.contains("Edit" )) { ++i; }
			  if (l.contains(tempPhName)) {
				  
				  myDebugPrinting("returned i - " + i, enumsClass.logModes.MINOR);
				  break;
			  }
		  }  
		  myClick(driver, By.xpath("//*[@id='tenants1']/tbody/tr[" + i + "]/td[6]/a[1]"), 7000);
		  
		  // Edit 
		  myDebugPrinting("Edit" + tempPhName, enumsClass.logModes.MINOR);
		  mySendKeys(driver, By.xpath("//*[@id='ph_value']"), tempPhValue      , 2000);
		  mySendKeys(driver, By.xpath("//*[@id='ph_desc']") , tempPhDescription, 2000);  				  
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[4]/button[2]"), 3000);
		  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update Placeholder.");
		  verifyStrByXpath(driver, "//*[@id='modalContentId']", "Update placeholder successfully.");
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);

		  // Verify edit
		  myDebugPrinting("verify edit", enumsClass.logModes.MINOR);
		  searchStr(driver, tempPhValue);
		  searchStr(driver, tempPhDescription);
	  }
	  
	  /**
	  *  Delete a template placeholder
	  *  @param driver     - given driver
	  *  @param tempName   - given Template from whom we want to delete a placeholder
	  *  @param tempPhName - given Template placeholder
	  *  @throws IOException 
	  */
	  public void deleteTemplatePlaceholder(WebDriver driver, String tempName, String tempPhName) throws IOException {
		  		  
		  // Select a model
		  myDebugPrinting("Select a model", enumsClass.logModes.MINOR);
		  Select models = new Select(driver.findElement(By.xpath("//*[@id='models']")));
		  models.selectByVisibleText(tempName);
		  myWait(5000);
		  
		  // Get idx
		  BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		  String l = null;
		  int i = 0;
		  myDebugPrinting("tempPhName - " + tempPhName, enumsClass.logModes.MINOR);  
		  while ((l = r.readLine()) != null) {
			  
			  myDebugPrinting(l, enumsClass.logModes.MINOR);
			  if (l.contains("Edit" )) { ++i; }
			  if (l.contains(tempPhName)) {
				  
				  myDebugPrinting("returned i - " + i, enumsClass.logModes.MINOR);
				  break;
			  }
		  }  
		  myClick(driver, By.xpath("//*[@id='tenants1']/tbody/tr[" + i + "]/td[6]/a[2]"), 7000);	  
		  
		  // Delete 
		  myDebugPrinting("Delete", enumsClass.logModes.MINOR);
		  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete " +  tempPhName);  
		  verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete this value?");  
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);

		  // Verify delete
		  myDebugPrinting("Verify delete", enumsClass.logModes.MINOR);
		  myAssertTrue("Delete did not succeeded !!", !driver.findElement(By.tagName("body")).getText().contains(tempPhName));
	  }
	  
	  /**
	  *  Copy a template placeholder from other Template
	  *  @param driver         - given driver
	  *  @param tempWecopyFrom - given Template from whom we want to delete a placeholder
	  *  @param tempWeCopyTo   - given Template placeholder name
	  */
	  public void copyTemplatePlaceholder(WebDriver driver, String tempWecopyFrom, String tempWeCopyTo, String tempPhName) {
		  	
			Select models = new Select(driver.findElement(By.xpath("//*[@id='models']")));
			models.selectByVisibleText(tempWeCopyTo);
			myWait(5000);
			myClick(driver, By.xpath("//*[@id='import']"), 3000);
			verifyStrByXpath(driver, "//*[@id='modalTitleId']"	, "Import Place Holders");
			verifyStrByXpath(driver, "//*[@id='modalContentId']", "Please select a model");
			Select modelsToCopy = new Select(driver.findElement(By.xpath("/html/body/div[2]/div/select")));
			modelsToCopy.selectByVisibleText(tempWecopyFrom);
			myWait(5000);
			myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);

			// Copy a template place holder to Default Template should not be possible
			if (tempWecopyFrom.contains("operTemplate")) {
				
				myDebugPrinting("Copy a template place holder to Default Template should not be possible", enumsClass.logModes.MINOR);
				verifyStrByXpath(driver, "/html/body/div/div/div[1]/h3"     , "Unauthorized");
				verifyStrByXpath(driver, "/html/body/div/div/div[2]/div/div", "You do not have permission to modify this item"); 
				return;
			}
			verifyStrByXpath(driver, "//*[@id='modalTitleId']"	, "Import Place Holders");
			verifyStrByXpath(driver, "//*[@id='modalContentId']", "Import place holders succesfully");
			myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);	
			
			// Verify import
			models = new Select(driver.findElement(By.xpath("//*[@id='models']")));
			models.selectByVisibleText(tempWecopyFrom);
			myWait(5000);
			models = new Select(driver.findElement(By.xpath("//*[@id='models']")));
			models.selectByVisibleText(tempWeCopyTo);
			myWait(5000);
			searchStr(driver, "%ITCS_" + tempPhName + "%");
	  }
	  
	  /**
	  *  Add new IP phone firmware
	  *  @param driver       - given driver
	  *  @param firmName     - name of the added firmware
	  *  @param firmDesc     - description of the added firmware
	  *  @param firmVersion  - version of the added firmware
	  *  @param firmTenant   - name of the region for the added firmware
	  *  @param firmFileName - name for the file we want to upload for the firmware
	  */
	  public void addNewFirmware(WebDriver driver, String firmName, String firmDesc, String firmVersion, String firmTenant, String firmFileName) {
		  	
		  // Enter the Add-New-Firmware menu
		  myDebugPrinting("Enter the Add-New-Firmware menu", enumsClass.logModes.MINOR);
		  myClick(driver, By.xpath("//*[@id='tbTemps']/tbody/tr[1]/td/a"), 2000);
		  verifyStrByXpath(driver, "//*[@id='trunkTBL']/div[2]/div[1]/h3", "Add new IP Phone firmware");
		  
		  // Fill data
		  myDebugPrinting("Fill data", enumsClass.logModes.MINOR);
		  mySendKeys(driver, By.xpath("//*[@id='name']")       , firmName   , 2000);
		  mySendKeys(driver, By.xpath("//*[@id='description']"), firmDesc   , 2000);
		  mySendKeys(driver, By.xpath("//*[@id='version']")    , firmVersion, 2000);
		  if (!firmTenant.isEmpty()) {

			  Select myFirmTenant = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
			  myFirmTenant.selectByVisibleText(firmTenant);
				myWait(5000);
		  }
		  myClick(driver, By.xpath("//*[@id='trunkTBL']/div[2]/div[3]/button[1]"), 7000);
		  
		  // Upload firmware file
		  myDebugPrinting("Upload firmware file", enumsClass.logModes.MINOR);		  
		  myClick(driver, By.xpath("//*[@id='trunkTBL']/div[2]/div[2]/form/div/div[7]/a"), 3000);
		  verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "Upload IP Phone Firmware " + firmName);
		  WebElement fileInput = driver.findElement(By.name("uploadedfile"));  
		  myDebugPrinting("Upload file - <" + testVars.getSrcFilesPath() + "\\" + firmFileName + ">", enumsClass.logModes.MINOR);		  
		  fileInput.sendKeys(testVars.getSrcFilesPath() + "\\" + firmFileName);
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 15000);
		  
		  // Check if Permitted suffixes test is performed
		  if (firmName.contains("permitted")) {
			  
			  myDebugPrinting("Permitted suffixes test is performed", enumsClass.logModes.MINOR);		  
			  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Failed To Upload");
			  verifyStrByXpath(driver, "//*[@id='modalContentId']", "(image/jpeg)");
			  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 15000);
			  
		  } else {
			  
			  myDebugPrinting("Permitted suffixes test is NOT performed", enumsClass.logModes.MINOR);		  
			  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Upload Successful");
			  verifyStrByXpath(driver, "//*[@id='modalContentId']", "The IP Phone firmware has been uploaded successfully.");
			  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 15000);

			  // Verify create
			  myDebugPrinting("Verify create", enumsClass.logModes.MINOR);		  
			  enterMenu(driver, enumsClass.menuNames.SETUP_PHONE_CONFIGURATION_PHONE_FIRM_FILES, "Phone firmware files");
			  searchStr(driver, firmName);
			  searchStr(driver, firmDesc);
			  searchStr(driver, firmDesc);	  
		  }
	  }
	  
	  /**
	  *  Add an existing IP phone firmware
	  *  @param driver       - given driver
	  *  @param firmName     - name of the added firmware
	  */
	  public void addNewFirmware(WebDriver driver, String firmName) {
		  	
		  // Enter the Add-New-Firmware menu
		  myDebugPrinting("Enter the Add-New-Firmware menu", enumsClass.logModes.MINOR);
		  myClick(driver, By.xpath("//*[@id='tbTemps']/tbody/tr[1]/td/a"), 2000);
		  verifyStrByXpath(driver, "//*[@id='trunkTBL']/div[2]/div[1]/h3", "Add new IP Phone firmware");
		  
		  // Fill data
		  myDebugPrinting("Fill data", enumsClass.logModes.MINOR);
		  mySendKeys(driver, By.xpath("//*[@id='name']")       , firmName, 2000);
		  mySendKeys(driver, By.xpath("//*[@id='description']"), "1234"  , 2000);
		  mySendKeys(driver, By.xpath("//*[@id='version']")    , "1234"  , 2000);
		  myClick(driver, By.xpath("//*[@id='trunkTBL']/div[2]/div[3]/button[1]"), 7000);

		  // Verify that error prompt is displayed
		  myDebugPrinting("Verify that error prompt is displayed", enumsClass.logModes.MINOR);		  
		  searchStr(driver, "Failed to add new IPP Phone " + firmName + " firmware.");
	  }
	  
	  /**
	  *  Edit an existing IP phone firmware
	  *  @param driver         - given driver
	  *  @param firmName       - name of the created firmware
	  *  @param firmDesc       - description of the created firmware
	  *  @param newFirmDesc    - description of the edited firmware
	  *  @param newFirmVersion - version of the edited firmware
	  *  @param newFirmTenant  - name of the Tenant for the edited firmware
	  *  @throws IOException 
	  */
	  public void editFirmware(WebDriver driver, String firmName, String firmDesc, String newFirmDesc, String newFirmVersion, String newFirmTenant) throws IOException {
		  
		  // Edit Firmware
		  myDebugPrinting("Edit Firmware", enumsClass.logModes.MINOR);		  
		  
		  // Get idx
		  BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		  String l = null;
		  int i = 1;
		  while ((l = r.readLine()) != null) {
			  
			  myDebugPrinting("i - " + i, enumsClass.logModes.DEBUG);
			  myDebugPrinting(l			, enumsClass.logModes.DEBUG);  
			  if (l.contains(firmName)) {
				  
				  myDebugPrinting("i - " + i, enumsClass.logModes.MINOR);
				  break;
			  }
			  if (l.contains("Edit" )) {
				  
				  ++i;
			  }
		  } 
		  myClick(driver, By.xpath("//*[@id='tbTemps']/tbody/tr[2]/td/table/tbody/tr["+ i + "]/td[7]/a"), 3000);

		  // Edit Firmware
		  verifyStrByXpath(driver, "//*[@id='trunkTBL']/div[2]/div[1]/h3", "IP Phone " + firmName + " Firmware");
		  mySendKeys(driver, By.xpath("//*[@id='description']"), newFirmDesc   , 2000);
		  mySendKeys(driver, By.xpath("//*[@id='version']")    , newFirmVersion, 2000); 
		  if (!newFirmTenant.isEmpty()) {
			  
			  Select myFirmTenant = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
			  myFirmTenant.selectByVisibleText(newFirmTenant);
				myWait(5000);
		  }  
		  
		  if (!newFirmTenant.isEmpty()) {
			  
			  // Check download
			  myDebugPrinting("Check download", enumsClass.logModes.MINOR);	
			  String downloadedFile = firmName + ".img";
			  deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile);
			  myClick(driver, By.xpath("//*[@id='trunkTBL']/div[2]/div[2]/form/div/div[6]/a"), 20000);
			  myAssertTrue("File was not downloaded successfully !!", findFilesByGivenPrefix(testVars.getDownloadsPath(), downloadedFile));
			  deleteFilesByPrefix(testVars.getDownloadsPath(), downloadedFile);  
		  }
		
		  // Submit and verify create  
		  myDebugPrinting("Submit and verify create", enumsClass.logModes.MINOR);	
		  myClick(driver, By.xpath("//*[@id='trunkTBL']/div[2]/div[3]/button[1]"), 7000);
		  String bodyText = driver.findElement(By.tagName("body")).getText();
	      myAssertTrue("Edited name was not detected !! ("        + firmName       + ")", bodyText.contains(firmName));
	      myAssertTrue("Edited description was not detected !! (" + newFirmDesc    + ")", bodyText.contains(newFirmDesc));
	      myAssertTrue("Edited version was not detected !! ("     + newFirmVersion + ")", bodyText.contains(newFirmVersion));	  
	  }
	  
	  /**
	  *  Delete an existing IP phone firmware
	  *  @param driver         - given driver
	  *  @param firmName       - name of the deleted firmware
	  *  @param newFirmDesc    - description of the deleted firmware
	  *  @param newFirmVersion - version of the deleted firmware
	  */
	  public void deleteFirmware(WebDriver driver, String firmName, String newFirmDesc, String firmVersion) {
		  
		  // Delete firmware 
		  myDebugPrinting("Delete firmware", enumsClass.logModes.MINOR);
		  myClick(driver, By.cssSelector("a[href*='" + firmName + "']"), 4000); 
		  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete Firmware");
		  verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the " + firmName + " IP Phone firmware?");
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000); 
		  
		  // Verify delete
		  myDebugPrinting("Verify delete", enumsClass.logModes.MINOR);
		  String bodyText = driver.findElement(By.tagName("body")).getText();
	      myAssertTrue("Firmware description still detected !! (" + newFirmDesc + ")", !bodyText.contains(newFirmDesc));
	  }
	  
	  /**
	  *  Add a device placeholder to existing registered user
	  *  @param driver   - given driver
	  *  @param userName - pre-create registered user
	  *  @param phName   - placeholder name
	  *  @param phValue  - placeholder value to override
	  */
	  public void addDevicePlaceholder(WebDriver driver, String userName, String phName, String phValue) {
		    
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/div[2]/a"), 7000);
		  verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/form/div/div[1]/h3", "Change IP Phone Device Placeholder");
		  verifyStrByXpath(driver, "//*[@id='table_all']/thead/tr/th"						 , "Please select a device");
		  mySendKeys(driver, By.xpath("//*[@id='table_all']/tbody/tr[1]/td[2]/div/input"), userName, 2000);
		  driver.findElement(By.xpath("//*[@id='table_all']/tbody/tr[1]/td[2]/div/input")).sendKeys(Keys.ENTER);	    
		  myWait(20000);		  
		  Actions action = new Actions(driver);
		  WebElement element=driver.findElement(By.xpath("//*[@id='devices_body']/tr/td[3]/b[1]"));
		  action.doubleClick(element).perform();
		  Select devKey = new Select(driver.findElement(By.xpath("//*[@id='key']")));
		  devKey.selectByVisibleText(phName);
		  myWait(5000);		  
		  mySendKeys(driver, By.xpath("//*[@id='over_value']"), phValue, 7000);
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/form/div/div[3]/button[2]") , 7000);
  
		  // Verify create
		  myDebugPrinting("Verify create", enumsClass.logModes.MINOR);  
		  mySendKeys(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[2]/form/div/input"), userName, 2000);
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/div[2]/form/div/span/button"), 5000);
		  String bodyText = driver.findElement(By.tagName("body")).getText();
		  myAssertTrue("placeholder name was not found!" , bodyText.contains("%ITCS_" + phName + "%"));
		  myAssertTrue("placeholder value was not found!", bodyText.contains(phValue));
	  }
	  
	  /**
	  *  Create a user using an external exe script on given element by given xpath and waits a given timeout
	  *  @param crUserBatName - given external script full path
	  *  @param ip  		  - IP for target the script 			  (I.e. 10.21.8.32)
	  *  @param	port		  - port for target the script 			  (I.e. 8090)
	  *  @param usrsNumber 	  - number for users for create
	  *  @param	dipName		  - display name	
	  *  @param domain		  - domain of the created users 		  (I.e onebox3.com)
	  *  @param crStatus	  - status of the create 				  (I.e registered / unregistered, offline etc.)
	  *  @param phoneType 	  - phone type 							  (I.e 420HD etc).
	  *  @param tenant		  - Tenant in which the user is created   (I.e ErezG)
	  *  @param	location	  - Location in which the user is created
	  *  @throws IOException 
	  */
	  public void createUserViaPost(String crUserBatName, String ip, String port, String usrsNumber, String dipName, String domain, String crStatus, String phoneType, String tenant, String location) throws IOException {
		
	 	myDebugPrinting("crUserBatName - " + System.getProperty("user.dir") + "\\" + testVars.getCrUserBatName(), enumsClass.logModes.MINOR);
	 	Process process = new ProcessBuilder(System.getProperty("user.dir") + "\\" + testVars.getCrUserBatName(), 
					 ip  	   ,
					 port	   ,
					 usrsNumber,
					 dipName   ,
					 domain    ,   
					 crStatus  ,
					 phoneType ,
					 tenant    ,
					 location).start();	    
	 	BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));	    
	 	String line;		    
	 	while ((line = br.readLine()) != null) {
		    	
	 		myDebugPrinting(line, enumsClass.logModes.MINOR);	
	 		if (line.contains("device_create_error")) {
	 			
	 			myFail(dipName + " device was not create !");
	 		}
	 	}
	  }  
	    
	  /**
	  *  Create a user using an external exe script on given element by given xpath and waits a given timeout
	  *  @param ip  		  - IP for target the script 			  (I.e. 10.21.8.32)
	  *  @param	port		  - port for target the script 			  (I.e. 8090)
	  *  @param usrsNumber 	  - number for users for create
	  *  @param	dispName	  - display name	
	  *  @param domain		  - domain of the created users 		  (I.e onebox3.com)
	  *  @param crStatus	  - status of the create 				  (I.e registered / unregistered, offline etc.)
	  *  @param phoneType 	  - phone type 							  (I.e 420HD etc).
	  *  @param tenant		  - Tenant in which the user is created   (I.e ErezG)
	  *  @param	location	  - Location in which the user is created
	  *  @param	extraData	  - Hashmap of keys and values as needed (most of time empty)
	  *  @throws IOException 
	  */ 	  
	  public void createUsers(String ip		  , String port  , int usersNumber,
			  				  String dispName , String domain, String crStatus,
			  				  String phoneType, String tenant, String location,
			  				  Map<String, String> extraData) {	
		   	
		  // Create SendPost object
		  myDebugPrinting("Create SendPost object", enumsClass.logModes.MINOR);	
		  SendPOSTRequset spr = new SendPOSTRequset(ip, port); 
		  
		  // Loop on all users
		  myDebugPrinting("Loop on all <" + usersNumber + "> users", enumsClass.logModes.MINOR);	
		  for (int i = 1; i <= usersNumber; ++i) {
			      
			  // Build data maps
			  myDebugPrinting("Build data maps for user <" + i + ">", enumsClass.logModes.MINOR);
			  String tmpIdxSuffix = (usersNumber == 1) 				   ? "" 								  :  "_" + String.valueOf(i);	  
			  String tmpMac       = (extraData.containsKey("samaMac")) ? testVars.getAcMacPrefix() + "123456" : getMacAddress();
			  Map<String, String> data 		   = new HashMap<String, String>();
			  Map<String, String> crDeviceData = new HashMap<String, String>();
			  data.put("region"  			, tenant);		  
			  data.put("model"   			, phoneType);	  
			  data.put("location"			, location);		 
			  data.put("mac"	 			, tmpMac);
			  data.put("ip"	 	 			, getRandomIp());	
			  for (String key : extraData.keySet()) {			
					
				  crDeviceData.put(key, extraData.get(key));								
			  }
			  crDeviceData.put("userName"	, dispName + tmpIdxSuffix);
			  crDeviceData.put("userId"		, crDeviceData.get("userName") + "@" + domain);	
			  crDeviceData.put("phoneNumber", String.valueOf(getNum(100000)));
			  crDeviceData.put("sipProxy"	, domain);
			  crDeviceData.put("status"		, crStatus);
			  
			  // Write MAC to file
			  myDebugPrinting("Write MAC to file", enumsClass.logModes.MINOR);
			  writeFile("mac_" + i + ".txt", tmpMac);
			  
			  // Send Query
			  myDebugPrinting("Send Query", enumsClass.logModes.MINOR);
			  spr.manageRequests(enumsClass.sendPOSTModes.CREATE_USER_DEVICE, data, crDeviceData);
			  data 		   = null;
			  crDeviceData = null;
			  myWait(3000);
		  }  
		  spr = null;
		  myWait(5000);		  
	  }
	  
	  /**
	  *  Create a user using an external exe script on given element by given xpath and waits a given timeout
	  *  @param ip  		  - IP for target the script 			  (I.e. 10.21.8.32)
	  *  @param	port		  - port for target the script 			  (I.e. 8090)
	  *  @param usrsNumber 	  - number for users for create
	  *  @param	dispName	  - display name	
	  *  @param domain		  - domain of the created users 		  (I.e onebox3.com)
	  *  @param crStatus	  - status of the create 				  (I.e registered / unregistered, offline etc.)
	  *  @param phoneType 	  - phone type 							  (I.e 420HD etc).
	  *  @param tenant		  - Tenant in which the user is created   (I.e ErezG)
	  *  @param	location	  - Location in which the user is created
	  *  @throws IOException 
	  */ 	  
	  public void createUsers(String ip		  , String port  , int usersNumber,
			  				  String dispName , String domain, String crStatus,
			  				  String phoneType, String tenant, String location) {	  
			
		  // Call createUsers() with null extraData hashMap
		  myDebugPrinting("Call createUsers() with null extraData hashMap", enumsClass.logModes.MINOR);
		  createUsers(testVars.getIp()	  		 ,
					  testVars.getPort() 	 	 ,
					  usersNumber				 ,
					  dispName  		 		 ,
					  testVars.getDomain()		 ,
					  crStatus		  		 	 ,
					  testVars.getDefPhoneModel(),
					  testVars.getDefTenant()    ,
					  location				 ,
					  new HashMap<String, String>());	  
	  }
	    
	  /**
	  *  Create a user using an external exe script on given element by given xpath and waits a given timeout
	  *  @param crUserBatName - given external script full path
	  *  @param ip  		  - IP for target the script  			(I.e. 10.21.8.32)
	  *  @param	port		  - port for target the script 			(I.e. 8081)
	  *  @param mac 	  	  - MAC address of registered device		(I.e. 00908fdf411a)
	  *  @param	alertName	  - alert name							(I.e. IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE)
	  *  @param alertDesc	  - alert description					(I.e. dateTest)
	  *  @param alertDate	  - alert date							(I.e. 2017-07-217T12:24:18)
	  *  @param alertInfo1 	  - alert info-1						(I.e. info1)
	  *  @param alertInfo2 	  - alert info-2						(I.e. info2)
	  *  @param	alertSev	  - alert severity						(I.e. "info", "minor", "normal", "major", "critical")
	  *  @throws IOException 
	  */
	  public void createAlarmViaPost(String crUserBatName, String ip, String port, String mac,
			  						 String alertName    , String alertDesc      , String alertDate,
			  						 String alertInfo1	 , String alertInfo2	 , String alertSev) throws IOException {
		
		myDebugPrinting("crUserBatName - " + crUserBatName, enumsClass.logModes.MINOR);
	    Process process = new ProcessBuilder(System.getProperty("user.dir") + "\\" + crUserBatName, 
				 ip  	   ,
				 port	   ,
				 mac	   ,
				 alertName ,
				 alertDesc ,   
				 alertDate ,
				 alertInfo1,
				 alertInfo2,
				 alertSev).start();
	    
	    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    String line;
	    while ((line = br.readLine()) != null) {

	    	myDebugPrinting(line, enumsClass.logModes.MINOR);
	    }
	  }
	  
	  /**
	  *  Create a Tenant-PH with given variables
	  *  @param driver     - given element
	  *  @param tenPhName  - given Tenant-ph name
	  *  @param tenPhValue - given Tenant-ph value
	  *  @param tenTenant  - given Tenant for the tenant-ph
	  */
	  public void addTenantPH(WebDriver driver, String tenPhName, String tenPhValue, String tenTenant) {
		  
		// Fill data
	    myDebugPrinting("Fill data", enumsClass.logModes.NORMAL);  
		myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[4]/div[2]/div/span[2]/a"), 9000);
		verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "Add new placeholder");
		Select tenId = new Select(driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[4]/select")));
		tenId.selectByVisibleText(tenTenant);
		myWait(5000);
		myDebugPrinting("tenPhName - "  + tenPhName ,enumsClass.logModes.MINOR);  
	    myDebugPrinting("tenPhValue - " + tenPhValue,enumsClass.logModes.MINOR);
		mySendKeys(driver, By.xpath("//*[@id='ph_name']") , tenPhName , 2000);
		mySendKeys(driver, By.xpath("//*[@id='ph_value']"), tenPhValue, 2000);
		myClickNoWait(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 7000);
		verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save New Placeholder.");
		verifyStrByXpath(driver, "//*[@id='modalContentId']", "The new placeholder (" + tenPhName + ") was saved successfully.");
		myClickNoWait(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);
		
		// Verify the create
	    myDebugPrinting("Verify the create", enumsClass.logModes.NORMAL);  
		mySendKeys(driver, By.xpath("//*[@id='tenants1-filtering']"), tenPhName , 6000);
		searchStr(driver, tenPhName);
		searchStr(driver, tenPhValue);
		searchStr(driver, tenTenant);
	  }
	  
	  /**
	  *  Delete a Tenant-PH with given variables
	  *  @param driver       - given element
	  *  @param tenPhName    - given Tenant-ph name
	  *  @param tenPhValue   - given Tenant-ph value
	  *  @throws IOException 
	  */  
	  public void deleteTenantPH(WebDriver driver, String tenPhName, String tenPhValue) throws IOException {
		  
	    // Get idx for delete
		myDebugPrinting("Get idx for delete", enumsClass.logModes.NORMAL);  
		mySendKeys(driver, By.xpath("//*[@id='tenants1-filtering']"), " " , 6000);
		BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		String l = null;
		int i = 1;
		while ((l = r.readLine()) != null) {
			
			myDebugPrinting("i - " + i, enumsClass.logModes.DEBUG);
			myDebugPrinting(l			, enumsClass.logModes.DEBUG);  
			if (l.contains(tenPhName)) {
				  
				myDebugPrinting("i - " + i, enumsClass.logModes.MINOR);
				break;
			}
			if (l.contains("Delete" )) {
				  
				  ++i;
			}
		}
		
		// Check if the current user is "Monitoring" if so - delete should fail
	    myDebugPrinting("Check if the current user is \"Monitoring\" if so - delete should fail", enumsClass.logModes.NORMAL);  
		if (tenPhName.contains("tenMonitPhName") && !tenPhValue.contains("Forced")) {
			
			String deleteButton = driver.findElement(By.xpath("//*[@id='tenants1']/tbody[1]/tr[" + i + "]/td[6]/button[2]")).getAttribute("class");
			myAssertTrue("Delete button is not deactivated !!\ndeleteButton - " + deleteButton, deleteButton.contains("not-active"));
			return;
		}
		
		// Delete PH
		myDebugPrinting("Delete PH", enumsClass.logModes.NORMAL);
		myClick(driver, By.xpath("//*[@id='tenants1']/tbody[1]/tr[" + i + "]/td[6]/button[2]"), 5000);
		verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete placeholder");
		verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete this placeholder: " + tenPhName);
		myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);	
		verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete placeholder");
		verifyStrByXpath(driver, "//*[@id='modalContentId']", "The selected placeholder (" + tenPhName + ") has been successfully removed from");
		myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);	
	
		// Verify delete
		myDebugPrinting("Verify delete", enumsClass.logModes.NORMAL);
		mySendKeys(driver, By.xpath("//*[@id='tenants1-filtering']"), tenPhName, 6000);
		String bodyText     = driver.findElement(By.tagName("body")).getText();
		myAssertTrue("Tenant-PH name still exist !!\nbodyText - "  + bodyText, !bodyText.contains(tenPhName));
	  }
	  
	  /**
	  *  Edit a Tenant-PH with given variables
	  *  @param driver        - given element
	  *  @param tenPhName     - given Tenant-ph name
	  *  @param tenNewPhValue - new given Tenant-ph value
	  *  @throws IOException 
	  */  
	  public void editTenantPH(WebDriver driver, String tenPhName,String tenNewPhValue) throws IOException {
		  
		// Get idx for delete
		myDebugPrinting("Get idx for delete", enumsClass.logModes.NORMAL);  
		mySendKeys(driver, By.xpath("//*[@id='tenants1-filtering']"), " " , 6000);
		BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		String l = null;
		int i = 1;
		while ((l = r.readLine()) != null) {
				
			myDebugPrinting("i - " + i + " " + l, enumsClass.logModes.DEBUG);
			if (l.contains(tenPhName)) {
					  
				myDebugPrinting("i - " + i, enumsClass.logModes.MINOR);
				break;
			}
			if (l.contains(" %ITCS_" )) {
				++i;
			}
		}
	
		// Check if the current user is "Monitoring" if so - edit should fail
	    myDebugPrinting("Check if the current user is \"Monitoring\" if so - edit should fail", enumsClass.logModes.NORMAL);  
		if (tenPhName.contains("tenMonitPhName")) {
			
			String editButton = driver.findElement(By.xpath("//*[@id='tenants1']/tbody[1]/tr[" + i + "]/td[6]/button[1]")).getAttribute("class");
			myAssertTrue("Edit button is not deactivated !!\neditButton - " + editButton, editButton.contains("not-active"));
			return;
		}
	    myDebugPrinting("xpath - " + "//*[@id='tenants1']/tbody[1]/tr[" + i + "]/td[6]/button[1]", enumsClass.logModes.DEBUG);  
		myClick(driver, By.xpath("//*[@id='tenants1']/tbody[1]/tr[" + i + "]/td[6]/button[1]"), 5000);
		
		// Fill data
		verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "Edit placeholder");
		mySendKeys(driver, By.xpath("//*[@id='ph_value']"), tenNewPhValue, 2000);
		myClickNoWait(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 5000);
		verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Update Placeholder.");
		verifyStrByXpath(driver, "//*[@id='modalContentId']", "The placeholder (" + tenPhName + ") was saved successfully.");
		myClickNoWait(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
		
		// Verify the create
	    myDebugPrinting("Verify the create", enumsClass.logModes.NORMAL);  
		mySendKeys(driver, By.xpath("//*[@id='tenants1-filtering']"), tenPhName , 6000);
		searchStr(driver, tenPhName);
		searchStr(driver, tenNewPhValue);
	  }
	  
	  /**
	  *  Create a Site-PH with given variables
	  *  @param driver      - given element
	  *  @param sitePhName  - given Site-PH name
	  *  @param sitePhValue - given Site-PH value
	  *  @param sitePHSite  - given Site-PH site
	  *  @param siteTenant  - given Site-PH tenant
	  */
	  public void addSitePH(WebDriver driver, String sitePhName, String sitePhValue, String sitePHSite, String siteTenant) {
		  
		// Add new Site-PH
	    myDebugPrinting("Add new Site-PH", enumsClass.logModes.NORMAL);  
		myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[4]/div[2]/div/span[2]/a"), 5000);	
		verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3", "Add new placeholder");		
		Select siteId = new Select(driver.findElement(By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[4]/select")));
		siteId.selectByVisibleText(sitePHSite);	
		myWait(5000);	
		mySendKeys(driver, By.xpath("//*[@id='ph_name']") , sitePhName , 4000);
		mySendKeys(driver, By.xpath("//*[@id='ph_value']"), sitePhValue, 4000);	
		myClickNoWait(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/button[2]"), 5000);
		verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save New Placeholder.");
		verifyStrByXpath(driver, "//*[@id='modalContentId']", "The new placeholder was saved successfully: " + sitePhName);
		myClickNoWait(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
		
		// Verify the create
	    myDebugPrinting("Verify the create", enumsClass.logModes.MINOR);  
		mySendKeys(driver, By.xpath("//*[@id='sites1-filtering']"), sitePhName , 5000);
		searchStr(driver, "%ITCS_" + sitePhName + "%");
		searchStr(driver, sitePhValue);
		searchStr(driver, sitePHSite);
		searchStr(driver, siteTenant);	
	  }
	  
	  /**
	  *  Delete a Site-PH with given variables
	  *  @param  driver      - given element
	  *  @param  sitePhName  - given Site-PH name
	  *  @param  sitePhValue - given Site-PH value
	  *  @param  sitePhSite	 - given Site-PH site
	  *  @throws IOException 
	  */  
	  public void deleteSitePH(WebDriver driver, String sitePhName, String sitePhValue, String sitePhSite) throws IOException {
		  
	    // Get idx for delete
		myDebugPrinting("Get idx for delete", enumsClass.logModes.NORMAL);  
		mySendKeys(driver, By.xpath("//*[@id='sites1-filtering']"), " " , 6000);
		BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		String l = null;
		int i = 1;
		while ((l = r.readLine()) != null) {
			
			myDebugPrinting("i - " + i, enumsClass.logModes.DEBUG);
			myDebugPrinting(l			, enumsClass.logModes.DEBUG);  
			if (l.contains(sitePhName)) {
				  
				myDebugPrinting("i - " + i, enumsClass.logModes.MINOR);
				break;
			}
			if (l.contains(" %ITCS_" )) {
				  
				  ++i;
			}
		}
		
//		// Check if the current user is "Monitoring" if so - delete should fail
//	    myDebugPrinting("Check if the current user is \"Monitoring\" if so - delete should fail", enumsClass.logModes.NORMAL);  
//		if (sitePhName.contains("tenMonitPhName") && !sitePhValue.contains("Forced")) {
//			
//			String deleteButton = driver.findElement(By.xpath("//*[@id='tenants1']/tbody[1]/tr[" + i + "]/td[6]/button[2]")).getAttribute("class");
//			myAssertTrue("Delete button is not deactivated !!\ndeleteButton - " + deleteButton, deleteButton.contains("not-active"));
//			return;
//		}
		
		// Delete PH
		myDebugPrinting("Delete PH", enumsClass.logModes.NORMAL);
		myDebugPrinting("xpath - " +  "//*[@id='tenants1']/tbody[1]/tr[" + i + "]/td[7]/button[2]", enumsClass.logModes.NORMAL);
		myClick(driver, By.xpath("//*[@id='sites1']/tbody[1]/tr[" + i + "]/td[7]/button[2]"), 5000);
		verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete Site Placeholder");
		verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete this site placeholder?: " + sitePhName);
		myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);	
		verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete Site Placeholder");
		verifyStrByXpath(driver, "//*[@id='modalContentId']", "The selected " + sitePhName + " placeholder has been successfully removed from " + sitePhSite);
		myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);	
		
		// Verify delete
		myDebugPrinting("Verify delete", enumsClass.logModes.NORMAL);
		mySendKeys(driver, By.xpath("//*[@id='sites1-filtering']"), sitePhName , 6000);
		String bodyText     = driver.findElement(By.tagName("body")).getText();
		myAssertTrue("Tenant-PH name still exist !!\nbodyText - "  + bodyText, !bodyText.contains(sitePhName));
	  }
	  
	  /**
	  *  Delete a Site Configuration-key by given variables
	  *  @param  driver       - given element
	  *  @param  cfgKeyName   - given configuration name
	  *  @param  cfgKeyValue  - given configuration value
	  *  @param  currTenant	  - given configuration tenant
	  *  @param  currSite     - given configuration site
	  *  @param  currSiteOnly - given configuration site
	  *  @throws IOException 
	  */
	  public void deleteSiteCfgKey(WebDriver driver, String cfgKeyName, String cfgKeyValue, String currTenant, String currSite, String currSiteOnly) throws IOException {
		  		    
		  // Select site
		  myDebugPrinting("Select site", enumsClass.logModes.MINOR);	
		  selectSite(driver, currSite);				
		  
		  // Get idx
		  myDebugPrinting("Get idx", enumsClass.logModes.MINOR);
		  BufferedReader r = new BufferedReader(new StringReader(driver.findElement(By.tagName("body")).getText()));
		  String l = null;
		  int i = 1;
		  Boolean countLines = false;
		  while ((l = r.readLine()) != null) {
					
			  myDebugPrinting("i - " + i + " " + l, enumsClass.logModes.DEBUG);
			  if (l.contains(cfgKeyName)) {
						  
				myDebugPrinting("i - " + i, enumsClass.logModes.MINOR);
				break;
			  } else if (countLines) {
				
				i++;
			  } else if (l.contains("Configuration Key Configuration Value")) {
				countLines = true;
			  }
		  }
		  if (l == null) {
			  
			  myFail("Configuration key was not found !!");			  
		  }
		  
		  // Delete key
		  myDebugPrinting("Delete key", enumsClass.logModes.MINOR);	  
		  myClick(driver, By.xpath("//*[@id='table_keys']/tbody/tr[" + i + "]/td[3]/div/a/i"), 4000);
		  verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Delete configuration setting");
		  verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Are you sure you want to delete the " + cfgKeyName + " from the configuration settings?");
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000); 
		  verifyStrByXpathContains(driver, "//*[@id='modalTitleId']",   "Save Configuration ( " + currSiteOnly + " [" + currSiteOnly + "] / " + currTenant);
		  verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Site configuration was saved successfully.");  
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 4000);
				  
		  // Verify delete
		  myDebugPrinting("Verify delete", enumsClass.logModes.MINOR);	  
		  String txt = driver.findElement(By.tagName("body")).getText();
		  myAssertTrue("Delete did not succeeded !!\ntxt - " + txt,  !txt.contains(cfgKeyName));
		  myAssertTrue("Delete did not succeeded !!\ntxt - " + txt,  !txt.contains(cfgKeyValue));
	  }
	  
	  /**
	  *  Add a new site CFG key according to given data
	  *  @param driver       - given element
	  *  @param cfgKeyName   - given configuration name
	  *  @param cfgKeyValue  - given configuration value
	  *  @param currTenant	 - given configuration tenant
	  *  @param currSite     - given configuration site
	  *  @throws IOException 
	  */
	  public void addNewSiteCfgKey(WebDriver driver, String cfgKeyName, String cfgKeyValue, String currTenant, String currSite) {  
		  
		  // Select site
		  myDebugPrinting("Select site - " + currSite, enumsClass.logModes.MINOR);	  
		  selectSite(driver, currSite); 
		  
		  // Select key, set data and submit
		  myDebugPrinting("Select key, set data and submit", enumsClass.logModes.MINOR);	 
		  myDebugPrinting("cfgKeyName - "  + cfgKeyName, enumsClass.logModes.MINOR);	  
		  myDebugPrinting("cfgKeyValue - " + cfgKeyValue, enumsClass.logModes.MINOR);	  
		  mySendKeys(driver, By.xpath("//*[@id='ini_name']") , cfgKeyName  , 2000);  
		  mySendKeys(driver, By.xpath("//*[@id='ini_value']"), cfgKeyValue, 2000);  	  
		  myClickNoWait(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[3]/a"), 7000);
		  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Save Configuration");
		  verifyStrByXpath(driver, "//*[@id='modalContentId']", "Site configuration was saved successfully.");
		  myClickNoWait(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);	
		  
		  // verify create
		  myDebugPrinting("verify create", enumsClass.logModes.MINOR);
		  searchStr(driver, cfgKeyName);
		  searchStr(driver, cfgKeyValue);
	  }
	  
	  /**
	  *  Select a site in Site configuration menu
	  *  @param driver   - a given driver
	  *  @param siteName - a given Site name
	  **/
	  public void selectSite(WebDriver driver, String siteName) {
		  
		  myDebugPrinting("Select site", enumsClass.logModes.MAJOR);
		  myDebugPrinting("siteName - " + siteName, enumsClass.logModes.MINOR);
		  Select currentSite = new Select(driver.findElement(By.xpath("//*[@id='site_id']")));
		  currentSite.selectByVisibleText(siteName);    
		  myWait(5000); 
	  }
	  
	  /**
	  *  Select a tenant in Tenant configuration menu
	  *  @param driver  - a given driver
	  *  @param tenName - a given Tenant name
	  **/
	  public void selectTenant(WebDriver driver, String tenName) {
		  
		  myDebugPrinting("Select tenant", enumsClass.logModes.MAJOR);
		  Select currentSite = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
		  currentSite.selectByVisibleText(tenName);    
		  myWait(5000);
	  }
	  
	  /**
	  *  Search for an Alert using given detector
	  *  @param driver          - given driver
	  *  @param info            - given searchMode (I.e. description, severity)
	  *  @param alertData       - search criteria
	  *  @param alertsForSearch - array of alert descriptions for search
	  **/
	  public void searchAlarm(WebDriver driver, alarmFilterModes info, String alertData, String [] alertsForSearch) {
		  
		  // Enter the filter menu & clear it before a new search
		  myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/a")						    	  , 3000);
		  myClickNoWait(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[4]/div[2]/div/button[2]"), 3000);	  
		  new Select(driver.findElement(By.xpath("//*[@id='inputAlarmsAndEvents']"))).selectByVisibleText("Alarms And Events");
		  myWait(3000);  
		  switch (info) {
			  case DESCRPTION:	
				  myDebugPrinting("Search according to description <" + alertData + ">", enumsClass.logModes.MINOR);
				  mySendKeys(driver, By.xpath("//*[@id='inputDescription']"), alertData, 2000);
				  break;
				  
			  case SEVERITY:	
				  myDebugPrinting("Search according to Severity <" + alertData + ">", enumsClass.logModes.MINOR);	  
				  new Select(driver.findElement(By.xpath("//*[@id='inputStatus']"))).selectByVisibleText(alertData);
				  myWait(3000);
				  break;
				  
			  case TENANT:	
				  myDebugPrinting("Search according to Tenant <" + alertData + ">", enumsClass.logModes.MINOR);	  
				  new Select(driver.findElement(By.xpath("//*[@id='inputTenant']"))).selectByVisibleText(alertData);
				  myWait(3000);
				  break; 
				  
			  case INFO:	  
				  myDebugPrinting("Search according to Info <" + alertData + ">", enumsClass.logModes.MINOR);	
				  mySendKeys(driver, By.xpath("//*[@id='inputInfo']"), alertData, 2000);
				  break;
				  
			  case REMOTE_HOST:	  
				  myDebugPrinting("Search according to Remote Host <" + alertData + ">", enumsClass.logModes.MINOR);	
				  mySendKeys(driver, By.xpath("//*[@id='inputRemoteHost']"), alertData, 2000);
				  break;
				  
			  case SOURCE:	  
				  myDebugPrinting("Search according to Source <" + alertData + ">", enumsClass.logModes.MINOR);	
				  mySendKeys(driver, By.xpath("//*[@id='inputSource']"), alertData, 2000);
				  break;  
				  
			  case NAME:	  
				  myDebugPrinting("Search according to Name <" + alertData + ">", enumsClass.logModes.MINOR);	
				  mySendKeys(driver, By.xpath("//*[@id='inputAlarm']"), alertData, 2000);
				  break;
				  
			  case ALARMS_ONLY:
				  myDebugPrinting("Search according to Alarms and Events <" + alertData + ">", enumsClass.logModes.MINOR);	
				  new Select(driver.findElement(By.xpath("//*[@id='inputAlarmsAndEvents']"))).selectByVisibleText(alertData);
				  myWait(3000);
				  break;
		
			  default:
				  break; 	  
		  }
		  myClickNoWait(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[4]/div[2]/div/button[1]"), 7000);
	      waitForLoad(driver);
		  
		  // Search for alerts
		  for (int i = 0; i < alertsForSearch.length; ++i) {
			
			  myDebugPrinting("alertsForSearch[i] - " + alertsForSearch[i], enumsClass.logModes.MINOR);
			  if (alertData.contains("unknownMac")) {
			  
				  searchStr(driver, "There are no devices that fit this search criteria");  	  
			  } else {
				  
				  searchStr(driver, alertsForSearch[i]);  
			  }	//  IPPhone Lync Login Failure
		  }
	  }  
	  
	  /**
	  *  Delete an alarm from Alarms table by a given parameters
	  *  @param driver    - given driver
	  *  @param alertDesc - alert description
	  **/
	  public void deleteAlarm(WebDriver driver, String alertDesc) {
		  		  
		  // Search for the alert according to description
		  myDebugPrinting("Search for the alert according to description", enumsClass.logModes.MINOR);
		  String[] alertsForSearch = {alertDesc};
		  searchAlarm(driver, enumsClass.alarmFilterModes.DESCRPTION, alertDesc, alertsForSearch); 
		   
		  // Delete the alarm
		  myDebugPrinting("Delete the alarm", enumsClass.logModes.MINOR);
		  myClick(driver, By.xpath("//*[@id='dl-menu']/a")		   , 3000);
		  myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[1]/a"), 3000);
		  verifyStrByXpath(driver, "//*[@id='jqistate_state0']/div[1]", "Delete");
		  verifyStrByXpath(driver, "//*[@id='jqistate_state0']/div[2]", "Are you sure you want to delete this IPP?");
		  myClick(driver, By.xpath("//*[@id='jqi_state0_buttonDelete']"), 3000);
		  
		  // Verify delete
		  myDebugPrinting("Verify delete", enumsClass.logModes.MINOR);
		  String bodyText     = driver.findElement(By.tagName("body")).getText();
		  myAssertTrue("String is not detcetd !!", bodyText.contains("There are no devices that fit this search criteria"));	  
	  }
	  
	  /**
	  *  Delete an alarm from Alarms table by a given parameters without pre-search
	  *  @param driver    - given driver
	  **/
	  public void deleteAlarmWithoutSearch(WebDriver driver) {
		  		  		   
		  // Delete the alarm
		  myDebugPrinting("Delete the alarm", enumsClass.logModes.MINOR);
		  myClick(driver, By.xpath("//*[@id='dl-menu']/a")		   , 3000);
		  myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[1]/a"), 3000);
		  verifyStrByXpath(driver, "//*[@id='jqistate_state0']/div[1]", "Delete");
		  verifyStrByXpath(driver, "//*[@id='jqistate_state0']/div[2]", "Are you sure you want to delete this IPP?");
		  myClick(driver, By.xpath("//*[@id='jqi_state0_buttonDelete']"), 5000);
		  
		  // Verify delete
		  myDebugPrinting("Verify delete", enumsClass.logModes.MINOR);
		  String bodyText     = driver.findElement(By.tagName("body")).getText();
		  myAssertTrue("String is not detcetd !!", bodyText.contains("There are no devices that fit this search criteria"));	  
	  }
	  
	  /**
	  *  Delete all configuration values at tenant menu
	  *  @param driver     - given driver
	  *  @param prefix 	   - prefix for searched values (if empty - verify of delete is not needed)
	  *  @param currTenant - tenant from whom we delete the values
	  **/
	  public void deleteAllConfValues(WebDriver driver, String prefix, String currTenant) {

		  // Delete all Configuration values
		  myDebugPrinting("Delete all Configuration values", enumsClass.logModes.NORMAL);
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[5]/div[2]/button")    , 5000);
		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[5]/div[2]/ul/li[1]/a"), 5000);
//		  myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[5]/div[2]/ul/li[1]/a"), 3000);
		  myDebugPrinting("Confirm delete", enumsClass.logModes.MINOR);
		  verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Delete configuration settings");
		  verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Are you sure you want to delete all configuration settings and save empty content?");
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);
		  verifyStrByXpathContains(driver, "//*[@id='modalTitleId']"  , "Save Configuration ( " + currTenant + " )");
		  verifyStrByXpathContains(driver, "//*[@id='modalContentId']", "Tenant configuration was saved successfully.");
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);

		  // verify delete if prefix is not empty
		  if (!prefix.isEmpty()) {
			  
			  myDebugPrinting("Verify delete", enumsClass.logModes.MINOR);
			  String txt = driver.findElement(By.tagName("body")).getText();		  
			  myAssertTrue("All tenant configuration values were not delete successfully !!\ntxt - " + txt, !txt.contains(prefix));
		  }
	  }
	  
	  /**
	  *  Send a keep-alive packet for a pre-created device
	  *  @param kpAlveBatName - given batch name that wraps and sends the packet
	  *  @param ip 	   		  - ip of the OVOC
	  *  @param port		  - port of the OVOC for send the keep alive packet
	  *  @param macAddress	  - MAC address of pre-created device
	  *  @param deviceName	  - Name of the device we want to update
	  *  @param phoneModel	  - Name of the phone model we want to update
	  *  @param domain		  - Domain of the OVOC
	  *  @param devStatus	  - Status of device we want to update
	  *  @param location	  - Name of the location we want to update
	  *  @param phoneNumber	  - Name of the phone-number we want to update
	  *  @param version		  - Version of the device
	  **/
	  public void sendKeepAlivePacket(String kpAlveBatName, String ip		 , String port      ,
									  String macAddress	  , String deviceName, String phoneModel,
									  String domain		  , String devStatus , String location  ,
									  String phoneNumber  , String version) throws IOException {
	 	
		  myDebugPrinting("kpAlveBatName - " + System.getProperty("user.dir") + "\\" + testVars.getKpAlveBatName(), enumsClass.logModes.MINOR); 	
		  Process process = new ProcessBuilder(System.getProperty("user.dir") + "\\" + testVars.getKpAlveBatName(), 
					 						   ip	   															  ,
										       port																  ,
											   macAddress														  ,
											   deviceName   													  ,
											   phoneModel    													  ,   
											   domain  														      ,
											   devStatus 														  ,
											   location    														  ,
											   phoneNumber														  ,
											   version).start();
		    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		    String line;
		    while ((line = br.readLine()) != null) {
	
		    	myDebugPrinting(line, enumsClass.logModes.MINOR);
		    }
	}

	/**
	*  Verify the create /non-create of POST users
	*  @param driver       - given driver
	*  @param userNamePrefix - Prefix of user-names (same name for the device)
	*  @param dispNamePrefix - Prefix of display-names
	*  @param isRegistsred - flag for identify if a registered user was created or not
	*  @param usersNumber	 - users number
	*/
	public void verifyPostUsersCreate(WebDriver driver, String userNamePrefix, String dispNamePrefix, boolean isRegistered, int usersNumber) {
		  
		  String username, dispName;
		  enterMenu(driver, enumsClass.menuNames.SETUP_MANAGE_USERS, "New User");
		  for (int idx = 1; idx <= usersNumber; ++idx) {
			  
				// Search user
			  	username = userNamePrefix  + "_" + idx;
			  	dispName = dispNamePrefix  + "_" + idx; 	
				myDebugPrinting("Search user <" + username + "> with prefix <" + dispName + ">", enumsClass.logModes.NORMAL);
				mySendKeys(driver, By.xpath("//*[@id='searchtext']"), username, 1000);
				myClick(driver, By.xpath("//*[@id='searchusersform']/div/div/div/div/div[2]/button/span[2]"), 2000);
				myClick(driver, By.xpath("//*[@id='all_search']/li[1]/a")									, 2000);
				driver.findElement(By.xpath("//*[@id='searchtext']")).sendKeys(Keys.ENTER);
				myClick(driver, By.xpath("//*[@id='searchusersform']/div/div/div/div/span/a/button/span"), 7000);
				waitForLoad(driver);		  
				if (isRegistered) {
			    		    	
			    	if (!username.contains("location_2049")) {
			    	
			    		searchStr(driver, username);
			    		searchStr(driver, dispName);
			    	} else {
			    		
			    		searchStr(driver, "No users found");
			    	}
			    } else {
			    	
				   verifyStrByXpath(driver, "//*[@id='trunkTBL']/div/div[2]/table/tbody/tr/td/table/tbody/tr[2]/td/font", "No users found");
			    } 
		  }
		  	
		  enterMenu(driver, enumsClass.menuNames.MONITOR_DEVICE_STATUS, "Devices Status");
		  for (int idx = 1; idx <= usersNumber; ++idx) {
			  
			  // Search device	  	
			  username = userNamePrefix  + "_" + idx;
			  dispName = dispNamePrefix  + "_" + idx; 	
			  myDebugPrinting("Search user <" + username + "> with prefix <" + dispName + ">", enumsClass.logModes.NORMAL);
		
			  // Verify that the device was also created		    
			  myDebugPrinting("Verify that the device was also created", enumsClass.logModes.MINOR);			    
			  mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input"), "user:" + dispName.trim(), 1000);			    
			  driver.findElement(By.xpath("//*[@id='trunkTBL']/div/div[2]/div[1]/div[2]/form/div/input")).sendKeys(Keys.ENTER);	     
			  myWait(3000);	
		      waitForLoad(driver);
			  if (isRegistered) { 
			    	    	
				  if (!dispName.contains("location_2049")) {
					 
					  myAssertTrue("No users were found for <" + dispName + "> !!", !driver.findElement(By.tagName("body")).getText().contains("There are no devices that fit this search criteria"));
					  searchStr(driver, dispName.trim()); 			    
					  String txt = driver.findElement(By.tagName("body")).getText();  
					  myAssertTrue("Approve button is displayed !! \ntxt - " + txt, !txt.contains("Approve"));	
			    	
				  } else {
			    				    		
					  searchStr(driver, "There are no devices that fit this search criteria");	    	
				  }	    	  
			  } else {
			    			    	
				  String txt = driver.findElement(By.tagName("body")).getText();			    
				  myAssertTrue("Approve button is not displayed !!\ntxt - " + txt, txt.contains("Approve"));		    
			  } 
		  }
	  }
	  
	  /**
	  *  Create a new  Tenant configuration key
	  *  @param driver      - given driver
	  *  @param cfgKeyName  - given configuration tenant name
	  *  @param cfgKeyValue - given configuration tenant value
	  *  @param currTenant  - given configuration tenant
	  */
	  public void addNewCfgKey(WebDriver driver, String cfgKeyName, String cfgKeyValue, String currTenant) {
		  
		  // Select tenant
		  myDebugPrinting("Select tenant (" + currTenant + ")", enumsClass.logModes.MINOR);	  
		  Select currentTenant = new Select(driver.findElement(By.xpath("//*[@id='tenant_id']")));
		  currentTenant.selectByVisibleText(currTenant);
		  myWait(20000);
		  
		  // Select key, set data and submit
		  myDebugPrinting("Add cfgKeyName - "  + cfgKeyName , enumsClass.logModes.MINOR);	
		  myDebugPrinting("Add cfgKeyValue - " + cfgKeyValue, enumsClass.logModes.MINOR);	 
		  mySendKeys(driver, By.xpath("//*[@id='ini_name']") , cfgKeyName , 8000);
		  mySendKeys(driver, By.xpath("//*[@id='ini_value']"), cfgKeyValue, 8000); 
		  myClickNoWait(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div[1]/div[3]/a"), 8000);	 
		  String txt = driver.findElement(By.tagName("body")).getText();
		  if (txt.contains("Tenant configuration was saved successfully.")) {
			  
			  myDebugPrinting("Confirmbox was detected .. ", enumsClass.logModes.MINOR);	
			  myClickNoWait(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);	
		  }   else {
			  
			  myWait(7000);			  
		  }

		  // verify create
		  myDebugPrinting("verify create", enumsClass.logModes.MINOR);
		  searchStr(driver, cfgKeyName);
		  searchStr(driver, cfgKeyValue);
		  myWait(5000);
	  }
	  
	  /**
	  *  Save configuration values
	  *  @param driver         - given driver
	  *  @param usersFullNames - given array of user-full-names
	  */
	  public void saveConfValues(WebDriver driver, String [] usersFullNames) {
		
		  // Save configuration
		  myDebugPrinting("Save configuration", enumsClass.logModes.MINOR);
		  myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[4]/a"), 5000);
		  
		  // Check Confirm-Box
		  myDebugPrinting("Check Confirm-Box", enumsClass.logModes.MINOR);
		  int usersNum = usersFullNames.length;
		  verifyStrByXpath(driver, "//*[@id='modalTitleId']"							 , "Save Configuration");
		  verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/thead/tr/th[1]", "Name");
		  verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/thead/tr/th[2]", "Result");
		  for (int i = 1; i <= usersNum; ++i) {
			  
			  verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/tbody/tr[" + i + "]/td[1]", usersFullNames[i-1]);
			  verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/tbody/tr[" + i + "]/td[2]", "User configuration was saved successfully");
		  }
		
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000);	  
	  }

	  /**
	  *  Add new configuration key according to given data
	  *  @param driver       - given driver
	  *  @param confValName  - given value name
	  *  @param confValValue - given value data
	  */
	  public void createNewConfValue(WebDriver driver, String confValName, String confValValue) {
		
		  // Select 'generate configuration' action, set data and submit	
		  myDebugPrinting("Select 'generate configuration' action, set data and submit", enumsClass.logModes.MINOR);
		  new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByVisibleText("User configuration");
		  myWait(5000);  
		  mySendKeys(driver, By.xpath("//*[@id='ini_name']") , confValName , 4000);
		  mySendKeys(driver, By.xpath("//*[@id='ini_value']"), confValValue, 4000);
		  myClick(driver, By.xpath("//*[@id='personalInfoTR']/td/div/div[1]/div[3]/a/span"), 5000);
		
		  // Verify create
		  myDebugPrinting("Verify create", enumsClass.logModes.MINOR);
		  searchStr(driver, confValName);
		  searchStr(driver, confValValue);  
	  }
	  
	  /**
	  *  Add new configuration key according to given data
	  *  @param driver       - given driver
	  *  @param confValName  - given value name
	  *  @param confValValue - given value data
	  */
	  public void deleteConfValue(WebDriver driver, String[] usersFullNames, String[] confNames, String[] confValues) {
		  	  
		  new Select(driver.findElement(By.xpath("//*[@id='action']"))).selectByVisibleText("Delete User configuration");
		  myWait(7000);	
		  myClick(driver, By.xpath("//*[@id='deletePersonalInfoTR']/td[1]/div/a"), 5000);		  
		  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete User configuration");			  
		  verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure you want to delete User configuration from selected user(s) ?"); 
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);		  
				  	
		  // Check confirm box	
		  myDebugPrinting("Check confirm box", enumsClass.logModes.NORMAL);		
		  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete User configuration");		
		  verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/thead/tr/th[1]", "Name");			
		  verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/thead/tr/th[2]", "Result");
		  for (int i = 1; i <= usersFullNames.length; ++i) {
				
			  verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/tbody/tr[" + i + "]/td[1]", usersFullNames[i-1]);
			  verifyStrByXpath(driver, "//*[@id='modalContentId']/div/table/tbody/tr[" + i + "]/td[2]", "User configuration was saved successfully.");		
		  }
		  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);	
		  
		  // Empty confNames and confValues
		  //verifyConfValues(driver, usersFullNames, confNames, confValues, false);
	  }
	  
	  /**
	  *  Delete a configuration file
	  *  @param driver   - A given driver
	  *  @param fileName - A name of the deleted file
	  */
	  public void deleteConfigurationFile(WebDriver driver, String fileName) {
		  
		  mySendKeys(driver, By.xpath("//*[@id='searchInput']"), fileName, 7000);
		  myClickNoWait(driver, By.id("selall"), 2000);
	 	  myClick(driver, By.xpath("//*[@id='filelist']/div[2]/a"), 3000);
	 	  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Delete Files");
	 	  verifyStrByXpath(driver, "//*[@id='modalContentId']", "Are you sure want to delete the selected files?\n" + fileName);
	 	  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 7000);

	 	  // Verify delete
	 	  myDebugPrinting("verify delete", enumsClass.logModes.MINOR);
	 	  verifyStrByXpath(driver, "//*[@id='modalContentId']/table/tbody[1]/tr/td[1]", fileName);
	 	  verifyStrByXpath(driver, "//*[@id='modalContentId']/table/tbody[1]/tr/td[2]", "Deleted");
	 	  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 3000); 
	  }
	   
	  /**
	  *  Upload a configuration file
	  *  @param driver   - A given driver
	  *  @param filePath - A given path to configuration file we want to upload
	  *  @param fileName - A name of the uploaded file
	  */
	  public void uploadConfigurationFile(WebDriver driver, String filePath, String fileName) {
		  
		  mySendKeys(driver, By.xpath("//*[@id='myfile']"), filePath, 3000);
	 	  myClick(driver, By.xpath("//*[@id='form_upload']/div/input[4]"), 5000);
	 	  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Upload Configuration File");
	 	  verifyStrByXpath(driver, "//*[@id='modalContentId']", "\"" + fileName + "\" File Successfully Uploaded.");	  
	 	  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	 		
	 	  // Verify upload
	 	  mySendKeys(driver, By.xpath("//*[@id='searchInput']"), fileName, 7000);
	 	  verifyStrByXpath(driver, "//*[@id='fbody']/tr[1]/td[3]/a[2]", fileName);
	  }
	  
	  /**
	  *  Upload non-cfg file to Phone configuration menu
	  *  @param driver         - A given driver
	  *  @param nonCfgFileName -  An invalid file path
	  */
	  public void uploadNonCfgToPhoneConfiguration(WebDriver driver, String nonCfgFileName) {
		  
		  // Try to upload non-cfg file to Configuration-files menu	 	  
		  myDebugPrinting("Try to upload non-cfg file to Configuration-files menu", enumsClass.logModes.MINOR);
	 	  mySendKeys(driver, By.xpath("//*[@id='myfile']"), nonCfgFileName, 2000);
	 	  myClick(driver, By.xpath("//*[@id='form_upload']/div/input[4]"), 5000);
	 	    
	 	  // Verify that an error is received
	 	  myDebugPrinting("Verify that an error is received", enumsClass.logModes.MINOR);
	 	  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Invalid file to upload");
	 	  verifyStrByXpath(driver, "//*[@id='modalContentId']", "Invalid file extension. Please select valid file or add this file extension on the System Settings page");
	 	  myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	 	  myDebugPrinting("Search for nonCfgFileName - " + nonCfgFileName, enumsClass.logModes.MINOR);
	 	  mySendKeys(driver, By.xpath("//*[@id='searchInput']"), nonCfgFileName, 4000);
	 	  String txt =  driver.findElement(By.tagName("body")).getText();;
	 	  myAssertTrue("Configuration file was uploaded successfully !!\ntxt - " + txt, !txt.contains(nonCfgFileName));   
	  }
	  
	  /**
	  *  Select a column in the Device-status menu by given xpath name
	  *  @param driver          - A given driver
	  *  @param xpathOfCheckbox -  A xpath of Xpath of the checkbox
	  *  @param isCheck			- A boolean flag that indicates if to check the field or not
	  */
	  public void selectColumn(WebDriver driver, String xpathOfCheckbox, Boolean isCheck) {
		  
		  // Enter Display columns menu
	 	  myDebugPrinting("Enter Display columns menu", enumsClass.logModes.MINOR);
		  myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[1]/h3/div/a[1]")			  , 7000);   
		  verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[1]/h3"  		 , "Devices Status Columns"); 
		  verifyStrByXpath(driver, "//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[1]/label", "Please select columns to display in Devices Status table"); 			
		
		  // Check the checkbox
		  if (!driver.findElement(By.xpath(xpathOfCheckbox)).isSelected() && isCheck) {
					 	  
			  myDebugPrinting("Check the checkbox ..", enumsClass.logModes.MINOR);			
			  myClickNoWait(driver, By.xpath(xpathOfCheckbox), 5000);
		  }
		  else if (driver.findElement(By.xpath(xpathOfCheckbox)).isSelected() && !isCheck) {
				
			  myDebugPrinting("Uncheck the checkbox ..", enumsClass.logModes.MINOR);			
			  myClickNoWait(driver, By.xpath(xpathOfCheckbox), 5000);
		  }
		  	
		  // Submit	
		  myDebugPrinting("Submit ..", enumsClass.logModes.MINOR);			
		  myClickNoWait(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[3]/a[3]"), 5000);   
	  }
	  
	  /**
	  *  Filter the devices by given filter and data
	  *  @param driver       - A given driver
	  *  @param deviceFilter - enum value of wanted filter criteria
	  *  @param isCheck	     - extra data which might be needed for the filter
	  *  @param srDevice     - device for search
	  */
	  public void deviceFilter(WebDriver driver, deviceFilter filterOption, String[] data, String srDevice) {
		  
		  // Filter by given criteria
		  myDebugPrinting("Filter device by <" + filterOption.toString() + ">", enumsClass.logModes.MINOR);			
		  myClick(driver, By.xpath("//*[@id='trunkTBL']/div/div[2]/a")			, 3000);  	  
		  myClick(driver, By.xpath("//*[@id='searchForm']/div[1]/div/button[2]"), 3000);  
		  switch (filterOption) {
		  
			  	case USER:
			  		mySendKeys(driver, By.xpath("//*[@id='inputUser']")		  , data[0], 2500);
			  		break; 				
			  	case PHONE_NUMBER:
			  		mySendKeys(driver, By.xpath("//*[@id='inputPhoneNumber']"), data[0], 2500);
			  		break;
			  	case MAC_ADDRESS:
			  		mySendKeys(driver, By.xpath("//*[@id='inputmac']")		  , data[0], 2500);
			  		break;
			  	case IP_ADDRESS:
			  		mySendKeys(driver, By.xpath("//*[@id='inputip']")		  , data[0], 2500);
			  		break;
			  	case TENANT:
			  		mySelect(driver,
			  				By.xpath("//*[@id='searchForm']/div[11]/div/select"),
			  				enumsClass.selectTypes.GIVEN_TEXT,
			  				data[0],
			  				3000);
			  		break;
			  	case VERSION:
			  		mySelect(driver,
			  				By.xpath("//*[@id='inputFWVer']"),
			  				enumsClass.selectTypes.GIVEN_TEXT,
			  				data[0],
			  				3000);
			  		break;
			  	case SITE:
			  		mySelect(driver,
			  				By.xpath("//*[@id='inputSite']"),
			  				enumsClass.selectTypes.GIVEN_TEXT,
			  				data[0],
			  				3000);
			  		break;
			  	default:
			  		break;
		  }
		  myClick(driver, By.xpath("//*[@id='searchForm']/div[15]/div/button[1]"), 3000);   
		
		  // Search the results for wanted device
		  myDebugPrinting("Search the results for wanted device <" + srDevice + ">", enumsClass.logModes.MINOR);			
		  searchStr(driver, srDevice);
	  }
	   
	  /**
	  *  Add a mapping value by given parameters
	  *  @param driver       - given driver
	  *  @param usedTemplate - given used template
	  *  @param newDataMap   - array of data for default mapping
	  */
	  public void addMapping(WebDriver driver, String usedTemplate, Map<String, String> newDataMap) {
	 	
	 	// Set a Template
	 	myDebugPrinting("Set a Template <" + usedTemplate + ">", enumsClass.logModes.MINOR);
	 	mySelect(driver,
	 			By.xpath("//*[@id='templates-def']"),
	 			enumsClass.selectTypes.GIVEN_TEXT,
	 			usedTemplate,
	 			5000);
	 	
	 	// Set extra data
	 	myDebugPrinting("Set extra data", enumsClass.logModes.MINOR);
	 	if (newDataMap.containsKey("isDefault") && newDataMap.get("isDefault").contains("true")) {
	 			
	 		// Mapping is default - Check the checkbox if needed
	 		myDebugPrinting("Mapping is default - Check the checkbox if needed", enumsClass.logModes.MINOR);		
	 		WebElement cbIsDef = driver.findElement(By.xpath("//*[@id='is_default']"));
	 		if (cbIsDef.getAttribute("value").contains("false")) {
	 			
	 			cbIsDef.click();
	 			myWait(2000);
	 		}
	 		
	 		// Set model
	 		myDebugPrinting("Set model - " + newDataMap.get("modelType"), enumsClass.logModes.MINOR);			
		 	mySelect(driver,
		 			By.xpath("//*[@id='def']/div/div/div[2]/table/tbody/tr/td[2]/div/table/tbody[1]/tr/td[3]/select"),
		 			enumsClass.selectTypes.GIVEN_TEXT,
		 			newDataMap.get("modelType"),
		 			5000);
	 		
	 		// Set Tenant
	 		myDebugPrinting("Set Tenant - " + newDataMap.get("tenantType"), enumsClass.logModes.MINOR);
		 	mySelect(driver,
		 			By.xpath("//*[@id='def']/div/div/div[2]/table/tbody/tr/td[2]/div/table/tbody[1]/tr/td[5]/select"),
		 			enumsClass.selectTypes.GIVEN_TEXT,
		 			newDataMap.get("tenantType"),
		 			5000);
	 	}
	 	
	 	// Confirm
	 	myClick(driver, By.xpath("//*[@id='def']/div/div/div[2]/table/tbody/tr/td[2]/div/table/tbody[1]/tr/td[6]/button"), 5000);
	 	verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , newDataMap.get("confMsgHeader"));
	 	verifyStrByXpath(driver, "//*[@id='modalContentId']", newDataMap.get("confMsgBody"));		
	 	myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	   }
	  
	   /**
	   *  Verify a mapping value by given parameters
	   *  @param driver       - given driver
	   *  @param usedTemplate - given used template
	   *  @param usedTenant   - given used tenant
	   *  @param	confName	 - wanted configuration file name for search
	   */
	   public void verifyMapping(WebDriver driver, String usedModel, String usedTenant, String confName) {
	 	  	  
		   // Verify headers of Test-Mapping section		
		   myDebugPrinting("Verify headers of Test-Mapping section", enumsClass.logModes.MINOR);
		   myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/ul/li[3]/a"), 5000);
		   verifyStrByXpath(driver, "//*[@id='test']/div/div/div/div[1]/h3", "Choose TENANT and MODEL and click test for the TEMPLATE");		
		   verifyStrByXpath(driver, "//*[@id='test']/div/div/div/div[2]/table/thead/tr/th[1]", "Model");		 	  
		   verifyStrByXpath(driver, "//*[@id='test']/div/div/div/div[2]/table/thead/tr/th[2]", "Tenant");
	 	  	  
		   // Select the tested model and tested tenant, and test the mapping
		   myDebugPrinting("Select the tested model and tested tenant, and test the mapping", enumsClass.logModes.MINOR);		  
		   mySelect(driver, By.xpath("//*[@id='test']/div/div/div/div[2]/table/tbody/tr/td[1]/select"), enumsClass.selectTypes.GIVEN_TEXT, usedModel , 4000);
		   mySelect(driver, By.xpath("//*[@id='test']/div/div/div/div[2]/table/tbody/tr/td[2]/select"), enumsClass.selectTypes.GIVEN_TEXT, usedTenant, 4000);	 	  
		   myClick(driver, By.xpath("//*[@id='test']/div/div/div/div[2]/table/tbody/tr/td[3]/buttton"), 5000);	 	  
		   verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Test Tenant URL"); 	  
		   verifyStrByXpath(driver, "//*[@id='modalContentId']", confName);
		   myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);  
	   }
	   
	   /**
	   *  Verify a mapping value by given parameters
	   *  @param driver       - given driver
	   *  @param usedTemplate - given used template
	   *  @param usedTenant   - given used tenant
	   *  @param confName	  - wanted configuration file name for search
	   */
	   public void verifyMappingWizard(WebDriver driver, String usedModel, String usedTenant, String confName) {
	 	  	  
		   // Verify headers of Test-Mapping section		
		   myDebugPrinting("Verify headers of Test-Mapping section", enumsClass.logModes.MINOR);
		   myClick(driver, By.xpath("//*[@id='step-4']/div/div/div[2]/ul/li[2]/a"), 5000);
		   myClick(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[2]/div[2]/div[2]/ul/li[3]/a"), 5000);
		   verifyStrByXpath(driver, "//*[@id='test']/div/div/div/div[1]/h3", "Choose TENANT and MODEL and click test for the TEMPLATE");		
		   verifyStrByXpath(driver, "//*[@id='test']/div/div/div/div[2]/table/thead/tr/th[1]", "Model");		 	  
		   verifyStrByXpath(driver, "//*[@id='test']/div/div/div/div[2]/table/thead/tr/th[2]", "Tenant");
	 	  	  
		   // Select the tested model and tested tenant, and test the mapping
		   myDebugPrinting("Select the tested model and tested tenant, and test the mapping", enumsClass.logModes.MINOR);		  
		   mySelect(driver, By.xpath("//*[@id='test']/div/div/div/div[2]/table/tbody/tr/td[1]/select"), enumsClass.selectTypes.GIVEN_TEXT, usedModel , 4000);
		   mySelect(driver, By.xpath("//*[@id='test']/div/div/div/div[2]/table/tbody/tr/td[2]/select"), enumsClass.selectTypes.GIVEN_TEXT, usedTenant, 4000);	 	  
		   myClick(driver, By.xpath("//*[@id='test']/div/div/div/div[2]/table/tbody/tr/td[3]/buttton"), 5000);	 	  
		   verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , "Test Tenant URL"); 	  
		   verifyStrByXpath(driver, "//*[@id='modalContentId']", confName);
		   myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);  
	   }
	   
	   /**
	   *  Delete an alarm from Alarms table by a given parameters without verify
	   *  @param driver    - given driver
	   *  @param alertDesc - alert description
	   **/
	   public void deleteAlarmNoVerify(WebDriver driver, String alertDesc) {
			  		  	
		   // Search for the alert according to description
		   myDebugPrinting("Search for the alert according to description", enumsClass.logModes.MINOR);		
		   String[] alertsForSearch = {alertDesc};		
		   searchAlarm(driver, enumsClass.alarmFilterModes.DESCRPTION, alertDesc, alertsForSearch); 
			
		   // Delete the alarm		
		   myDebugPrinting("Delete the alarm", enumsClass.logModes.MINOR);
		   myClick(driver, By.xpath("//*[@id='dl-menu']/a")		   , 3000);
		   myClick(driver, By.xpath("//*[@id='dl-menu']/ul/li[1]/a"), 3000);
		   verifyStrByXpath(driver, "//*[@id='jqistate_state0']/div[1]", "Delete");
		   verifyStrByXpath(driver, "//*[@id='jqistate_state0']/div[2]", "Are you sure you want to delete this IPP?");			
		   myClick(driver, By.xpath("//*[@id='jqi_state0_buttonDelete']"), 5000);
	   }
	   
	   /**
	   *  Edit the permitted suffixes
	   *  @param driver  - given driver
	   *  @param permStr - permitted suffixes string
	   *  @msgBoxHdr	 - needed message header
	   *  @msgBoxHdr2	 - needed message header
	   **/
	   public void editPermSuffixesField(WebDriver driver, String permStr, String msgBoxHdr, String msgBoxHdr2) {
	 	  
	 	  myDebugPrinting("permStr - <" + permStr + ">", enumsClass.logModes.MINOR);
	 	  mySendKeys(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div/table/tbody/tr/td/table/tbody/tr/td[2]/input"), permStr, 3000);
	 	  myClickNoWait(driver, By.xpath("//*[@id='contentwrapper']/section/div/div[3]/div[2]/div/table/tbody/tr/td/table/tbody/tr/td[4]/button"), 5000);
	 	  verifyStrByXpath(driver, "//*[@id='modalTitleId']"  , msgBoxHdr);
	 	  verifyStrByXpath(driver, "//*[@id='modalContentId']", msgBoxHdr2);
	 	  myClickNoWait(driver, By.xpath("/html/body/div[2]/div/button[1]"), 5000);
	   }
}
