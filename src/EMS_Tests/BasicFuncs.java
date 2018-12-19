package EMS_Tests;

import static org.junit.Assert.fail;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import EMS_Tests.enumsClass.logModes;
import EMS_Tests.enumsClass.selectTypes;

public class BasicFuncs {
	
	  private static final Logger logger = LogManager.getLogger();
	  GlobalVars 		   		testVars;

	  /**
	  *  testVars  	  - Container class that store data
	  */  
	  public BasicFuncs() {
			    		
		  testVars 		= new GlobalVars();	
	  }
	
	  /**
	  *  Verify string in page based on read the whole page
	  *  @param driver  - given driver
	  *  @param strName - given string for detect
	  */
	  public void searchStr(WebDriver 	driver, String strName) {
		  
		  String bodyText     = driver.findElement(By.tagName("body")).getText();
		  if (bodyText.contains(strName)) {
			  
			  myDebugPrinting("<" + strName + "> was detected !!",  enumsClass.logModes.DEBUG);
		  } else {
			  
			  //myDebugPrinting("<" + strName + "> was NOT detected !!",  enumsClass.logModes.DEBUG);
			  myFail("<" + strName + "> was not detected !! \nbodyText - " + bodyText);
		  }
	  }
	  
	  /**
	  *  Delete all files in directory by given prefix
	  *  @param dir    - given directory path
	  *  @param prefix - given prefix
	  */
	  public void deleteFilesByPrefix(String dir, String prefix) {
	    	
		myDebugPrinting("dir    - " + dir   ,  enumsClass.logModes.MINOR);
		myDebugPrinting("prefix - " + prefix,  enumsClass.logModes.MINOR);
    	File[] dirFiles = new File(dir).listFiles();
    	int filesNum = dirFiles.length;
    	for (int i = 0; i < filesNum; i++) {
    		
    	    if (dirFiles[i].getName().startsWith(prefix, 0)) {
    	    	
    			myDebugPrinting("Delete file - " + dirFiles[i].getName(),  enumsClass.logModes.MINOR);
    	        new File(dir + "\\" + dirFiles[i].getName()).delete();
    		    myWait(1000);    
    	    }
    	}	
	    myWait(1000);
	  }
	  
	  /**
	  *  read file method
	  *  @param  path    - given path for file to read
	  *  @return content - string of the readed file
	  */
	  public String readFile(String path) {
		  
		    String content = null;
		    File file = new File(path);
		    FileReader reader = null;
		    try {
		    	
		        reader = new FileReader(file);
		        char[] chars = new char[(int) file.length()];
		        reader.read(chars);
		        content = new String(chars);
		        reader.close();
		    } catch (IOException e) {
		    } finally {
		    	
		        if(reader !=null) {
		        	
		        	try {
		        		
		        		reader.close();
		        	} catch (IOException e) {}
		        }
		    }
		    
			myWait(3000);
	    	myDebugPrinting("content - " + content, enumsClass.logModes.MINOR);
		    return content;
	  }
	  
	  /**
	  *  Write data to file
	  *  @param driver   - given driver
	  *  @param elemName - given element xpath
	  *  @param strName  - given string for detect
	  *  @throws UnsupportedEncodingException 
	  *  @throws FileNotFoundException 
	  */	  
	  public void writeFile(String path, String txt) {
		  	
		  PrintWriter writer;
		  try {
			
			  writer = new PrintWriter(path, "UTF-8");
			  writer.print(txt);
			  writer.close();	
		  } catch (FileNotFoundException | UnsupportedEncodingException e) {
			
			e.printStackTrace();
		  }
	  }
	  
	  /**
	  *  Verify xpath contains a string
	  *  @param driver   - given driver
	  *  @param elemName - given element xpath
	  *  @param strName  - given string for detect
	  */
	  public void verifyStrByXpathContains(WebDriver 	driver, String xpath, String strName) {
	  	  
		  String txt = driver.findElement(By.xpath(xpath)).getText();
		  if (txt.contains(strName)) {
			  
		    	myDebugPrinting("<" + strName + "> was detected", enumsClass.logModes.DEBUG);
		  } else {
			  
			  myDebugPrinting(driver.findElement(By.xpath(xpath)).getText());
			  myFail ("<" + strName + "> was not detected !! (txt - <" + txt + ">)");
		  }
		  myWait(1000);
	  }
	    
	  /**
	  *  Print a given string to the console
	  *  @param str   - given string to print
	  *  @param level - given print level (MAJOR, NORMAL, MINOR, DEBUG)
	  */
	  public void myDebugPrinting(String str, logModes level) {
		    
		  logger.info(level.getLevel() + str);
	  }
	 
	  /**
	  *  Print a given string to the console with default level of MAJOR
	  *  @param str - A given string to print
	  */
      public void myDebugPrinting(String str) {
				
    	  logger.info(enumsClass.logModes.MAJOR.getLevel() + str);
	  }
      
	  /**
	  *  Print a given error string and declares the test as a myFailure
	  *  @param str - A given error string
	  */
      public void myFail(String str) {
				
    	  logger.error(str);		
    	  fail(str);
	  }
	 
	  /**
	  *  Verify string  method by xpath
	  *  @param driver   - given driver
	  *  @param elemName - given element name
	  *  @param strName  - given string for detect
	  */
	  public void verifyStrByXpath(WebDriver 	driver, String elemName, String strName) {		  
	   
		  markElemet(driver, driver.findElement(By.xpath(elemName)));	   
		  String txt = driver.findElement(By.xpath(elemName)).getText();  	   
		  myAssertTrue("<" + strName + "> was not detected !! <" + txt + ">", txt.contains(strName));	  	 		
		  myDebugPrinting("<" + strName + "> was detected !", enumsClass.logModes.DEBUG);	   
		  myWait(500);
	  }
	  
	  /**
	  *  Highlight given element
	  *  @param driver  - given driver
	  *  @param element - given element
	  */
	  public void markElemet(WebDriver 	driver, WebElement element) {
					
		  // Mark element    
		  try {
	    	  
			  ((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid yellow'", element);
			  TimeUnit.MILLISECONDS.sleep(500);
		  } catch (InterruptedException e1) {}   
		  ((JavascriptExecutor)driver).executeScript("arguments[0].style.border=''", element); 
	  }
	  
	  /**
	  *  Sleep for a given time
	  *  @param sleepValue - given sleep factor
	  */
	  public void myWait(int sleepValue) {
			  
		  try {
			  TimeUnit.MILLISECONDS.sleep(sleepValue);		
		
		  } catch (InterruptedException e1) {
		  }	
	  }
	  
	  /**
	  *  Create a unique Id based on current time
	  *  @return - unique id based on current time 
	  */
	  public String getId() {
		
	    // set id
	    DateFormat dateFormat = new SimpleDateFormat("HH_mm_dd_MM");
	    Date date     = new Date();
	    String id     = dateFormat.format(date);
	    id = id.replaceAll("_", "");
		myDebugPrinting("Id is:" + id, enumsClass.logModes.MAJOR);
		
	    return id;
	  }
	  
	  /**
	  *  Wrap assertTrue with logger
	  *  @param errorStr  - error message for display at the logger
	  *  @param condition - condition for mark if the assert succeeded or not
	  */
	  public void myAssertTrue(String errorStr, Boolean condition) {
		  
		  if (!condition) {
			  myFail(errorStr);  
		  }
	  }
	  
	  /**
	  *  Wrap assertFalse with logger
	  *  @param errorStr  - error message for display at the logger
	  *  @param condition - condition for mark if the assert succeeded or not
	  */
	  public void myAssertFalse(String errorStr, Boolean condition) {
		  
		  if (condition) {
			  myFail(errorStr);  
		  }
	  }
	  
	  /**
	  *  Get Random number according to given limit
	  *  @param limit - upper limit for the random function
	  *  @return      - random number in range of [1 - <limit>]
	  */
	  public int getNum(int limit) {
		  
		  Random rand = new Random();	  
		  return (rand.nextInt(limit) + 1);
	  }
	  
	  /**
	  *  Get a Random IP address
	  *  @return - random IP address
	  */
	  public String getRandomIp() {
		  	  
		  return  (String.valueOf(getNum(128)) + "." +
				   String.valueOf(getNum(128)) + "." +
				   String.valueOf(getNum(128)) + "." +			 
				   String.valueOf(getNum(128)));  
	  }
	  
	  /**
	  *  Get a Random Port (at range of 1000-9999 range)
	  *  @return - random Port number
	  */
	  public String getRandomPort() {
		  	  
		  return  (String.valueOf(getNum(9)) +
				   String.valueOf(getNum(9)) +
				   String.valueOf(getNum(9)) +			 
				   String.valueOf(getNum(9)));
	  }
	  
	  /**
	  *  Find files in a given directory by a given prefix
	  *  @param dir    - given directory path
	  *  @param prefix - given prefix
	  *  @return       - TRUE if files were found
	  */
	  public boolean findFilesByGivenPrefix(String dir, String prefix) {
	    	
			myDebugPrinting("dir    - " + dir   ,  enumsClass.logModes.MINOR);
			myDebugPrinting("prefix - " + prefix,  enumsClass.logModes.MINOR);
	    	File[] dirFiles = new File(dir).listFiles();
	    	int filesNum = dirFiles.length;
	    	for (int i = 0; i < filesNum; i++) {
	    				
    	    	myDebugPrinting(dirFiles[i].getName(),  enumsClass.logModes.DEBUG);
	    	    if (dirFiles[i].getName().startsWith(prefix, 0)) {
	    			
	    	    	int retNum = 0;
	    	    	while (dirFiles[i].getName().contains("crdownload")) {
	    	    		
		    	    	myDebugPrinting("crdownload suffix is detected. Waiting for another 10 seconds.",  enumsClass.logModes.MINOR);
	    	    		myWait(10000);
	    	    		if (retNum > 20) {
	    	    			
	    	    			myFail("After 20 retries, crdownload suffix is still detected !!");
	    	    		}
	    	    		retNum++;
	    	    	}
	    	    	myDebugPrinting("Find a file ! (" + dirFiles[i].getName() + ")",  enumsClass.logModes.MINOR);
	    	        return true;
	    	    }
	    	}
	    	
	    	return false;
	  }
	
	  /**
	  *  Create random MAC address
	  *  @return - MAC address based on AC prefix (i.e. 00908f) + random 6 characters long hex
	  */
	  public String getMacAddress() {
		
		char[] chars = "abcdef1234567890".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random2 = new Random();
		for (int i = 0; i < 6; i++) {
			
		    char c = chars[random2.nextInt(chars.length)];
		    sb.append(c);
		}
		
		return testVars.getAcMacPrefix() + sb.toString();	
	  }

	  /**
	   *  Wait till the given string not displayed on the screen
	   *  @param driver  - given driver
	   *  @param string  - given string that indicate if we should stop the loop
	  */
	  public void waitTillString(WebDriver driver, String string) {
		
		String bodyText = "";
		int idx         = 0;
		int gapTime     = 5;
		int gapDelay    = gapTime  * 1000;
		while (true) {
			
	      bodyText = driver.findElement(By.tagName("body")).getText();
	      if (!bodyText.contains(string) && !bodyText.contains("Performing")) {	 
	    	  
	    	  myAssertTrue("Users are already exist ..", !bodyText.contains("Already exists"));
	    	  break;    	  
	      } else { 
	    	  
	    	  idx += gapTime;
	    	  myDebugPrinting(string + " is still detected after " + idx + " seconds", enumsClass.logModes.MINOR);
	    	  myWait(gapDelay);
	      }
		}
	    myWait(2000);		
	  }
	  
	  /**
	  *  Wait till the download finishes
	  *  @param driver  - given driver
	  *  @param string  - given fileName that finishes the loop
	  */
	  public void waitTillDownloadFinishesString(WebDriver driver, String fileName, int timeOut) {
		
		int idx         = 0;
		myDebugPrinting("waitTillDownloadFinishesString()", enumsClass.logModes.MINOR);
		while (true) {
			
			myDebugPrinting("TRUE loop", enumsClass.logModes.DEBUG);
			if (findFilesByGivenPrefix(testVars.getDownloadsPath(), fileName)) {
					    	  
				myDebugPrinting("<" + fileName + "> was detected after <" + (idx / 1000) + "> seconds !!", enumsClass.logModes.MINOR);
				break;
				
			} else {
				
				if (idx > timeOut) {
					
					myFail("Timeout <" + timeOut + "> was passed, and <" + fileName + "> download not finished !!");
				} else{
					
					myDebugPrinting(fileName + " is still not detected after " + (idx / 1000 ) + " seconds", enumsClass.logModes.DEBUG);
					myWait(5000);  
					idx += 5000;
				}
			}		
		}
	  }
	
	  /**
	  *  Upload file with given path displayed on the screen
	  *  @param driver  		  - given driver
	  *  @param path    		  - path for a file for upload
	  *  @param uploadFieldXpath  - xpath for upload field
	  *  @param uploadButtonXpath - xpath for upload button
	  */
	  public void uploadFile(WebDriver driver, String path, String uploadFieldXpath, String uploadButtonXpath) {
	      		  
		myDebugPrinting("path -   " + path, enumsClass.logModes.MINOR);
		mySendKeys(driver, By.xpath(uploadFieldXpath), path  , 2000);
		myClick(driver   , By.xpath(uploadButtonXpath)		 , 200000);
		if (driver.findElement(By.tagName("body")).getText().contains("Failed to import from selected file.")) {
			
			myFail("Upload configuration-file was failed !!");
		}
	  }
	  
	  /**
	  *  Upload file with given path with confirm message-box
	  *  @param driver  		   - given driver
	  *  @param path    		   - path for a file for upload
	  *  @param uploadFieldXpath   - xpath for upload field
	  *  @param uploadButtonXpath  - xpath for upload button
	  *  @param confirmMessageStrs - array of confirm-box strings
	  */
	  public void uploadFile(WebDriver driver, String path, String uploadFieldXpath, String uploadButtonXpath, String[] confirmMessageStrs) {
	      		  
		myDebugPrinting("path -   " + path, enumsClass.logModes.MINOR);
		mySendKeys(driver, By.xpath(uploadFieldXpath), path  , 2000);
		myClick(driver   , By.xpath(uploadButtonXpath)		 , 5000);
		if (driver.findElement(By.tagName("body")).getText().contains("Failed to import from selected file.")) {
			
			myFail("Upload configuration-file was failed !!");
		}
		
		if (confirmMessageStrs != null && !confirmMessageStrs[0].isEmpty()) {
		
	    	verifyStrByXpath(driver, "//*[@id='modalTitleId']"	, confirmMessageStrs[0]);	
	    	verifyStrByXpath(driver, "//*[@id='modalContentId']", confirmMessageStrs[1]);	
			myClick(driver, By.xpath("/html/body/div[2]/div/button[1]"), 20000);	
		}
	  }
	  
	  /**
	  *  Wait for page to load
	  *  @param driver  		   - given driver
	  */ 
	  public void waitForLoad(WebDriver driver) {
		    
		  ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
		        	
			  public Boolean apply(WebDriver driver) {
		      
				  return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
			  }
		  };
		  WebDriverWait wait = new WebDriverWait(driver,30);	        	          
		  try {
		        	             
			  wait.until(expectation);        	         
		  } catch(Throwable error) {
		        	            
			  myFail("Timeout waiting for Page Load Request to complete.");	        	      
		  }	
	  }
	  
	  /**
	  *  Send a string to a given element using given parameters
	  *  @param driver  - given driver
	  *  @param byType  - given By element (By xpath, name or id)
	  *  @param currUsr - given string to send
	  *  @param timeout - given timeout (Integer)
	  */
	  public void mySendKeys(WebDriver driver, By byType, String currUsr, int timeOut) {
		  
		  WebElement  clickedElem = driver.findElement(byType);
		  WebDriverWait wait 	  = new WebDriverWait(driver , 20);
		  wait.until(ExpectedConditions.visibilityOfElementLocated(byType));
		  wait.until(ExpectedConditions.elementToBeClickable(byType));	
		  wait.until(ExpectedConditions.presenceOfElementLocated(byType));	
		  clickedElem.clear();
		  myWait(1000);
		  clickedElem.sendKeys(currUsr);
		  myWait(timeOut);	
		  wait = null;
	  }
	  
	  /**
	  *  Click on given element by given xpath and waits a given timeout
	  *  @param driver  - given driver
	  *  @param byType  - given By element (By xpath, name or id)
	  *  @param timeout - given timeout value (Integer)
	  */
	  public void myClick(WebDriver driver, By byType, int timeout) {
		  	
			markElemet(driver, driver.findElement(byType));	   
			WebElement  clickedElem = driver.findElement(byType);	
			WebDriverWait wait 	    = new WebDriverWait(driver , 30);
			wait.until(ExpectedConditions.visibilityOfElementLocated(byType));
			wait.until(ExpectedConditions.elementToBeClickable(byType));
			clickedElem.click();	    
			waitForLoad(driver);
			myWait(timeout);
			wait = null;  
	  }
	    
	  /**
	  *  Click on given element by given xpath and waits a given timeout
	  *  @param driver  - given driver
	  *  @param byType  - given By element (By xpath, name or id)
	  *  @param timeout - given timeout value (Integer)
	  */
	  public void myClickNoWait(WebDriver driver, By byType, int timeout) {
		  		  
		  WebElement ele5 = driver.findElement(byType);    
		  ele5.click();  
		  myWait(timeout);  	  
	  }
	  
	  /**
	  *  Select a List element by given By element and a select-mode
	  *  @param driver        - given driver
	  *  @param byType        - given By element (By xpath, name or id)
	  *  @param selectoption  - given Select option (Index, Value, GivenText)
	  *  @param valueToSelect - given string value for select (if selectType = Index it's a number of index)
	  *  @param timeout       - given timeout value (Integer)
	  */
	  public void mySelect(WebDriver driver, By byType, selectTypes selectoption, String valueToSelect, int timeout) {
		  
		  Select tempSelect = new Select(driver.findElement(byType));  
		  switch (selectoption) {
		  	case INDEX:
				tempSelect.selectByIndex(Integer.valueOf(valueToSelect));
            	break;
			case NAME:
				tempSelect.selectByValue(valueToSelect);	
            	break;
			case GIVEN_TEXT:
				tempSelect.selectByVisibleText(valueToSelect);	
            	break;
		  }
		  myWait(timeout);
	  }  
	  
	   /**
	   *  Get current time in HH:MM format
	   **/
	   public ArrayList<String> getCurrHours() {
	 	  
	 	  DateFormat timeFormat 	   = new SimpleDateFormat("HH:mm");    
	 	  Date time     			   = new Date();  
	 	  ArrayList<String> delayTimes = new ArrayList<String>();
	 	  String origTime     		   = timeFormat.format(time);
	 	  
	 	  delayTimes.add(origTime);
	 	  for (int i = 0; i < 5; ++i) {
	 		  
	 		  String tempTime = timeFormat.format(new Date(time.getTime() + (i * 60000) + (1 * 3600*1000)));
	 		  delayTimes.add(tempTime);
			  myDebugPrinting(i + ". tempTime - " + tempTime, enumsClass.logModes.MINOR);		  
	 	  }
	 	  return delayTimes;
	   }
	   
	   /**
	   *  Get current data in dd.MM.YYYY format
	   **/
	   public String getCurrdate() {
	 	  
	 	  DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");    
	 	  Date date     		= new Date();   
	 	  String myDate     	= dateFormat.format(date);
	 	  myDebugPrinting("date - " + myDate, enumsClass.logModes.MINOR);
	 	  return myDate;
	   }

}
