package EMS_Tests;
import EMS_Tests.enumsClass.*;

/**
* This class holds all the data which is used by the tests
* @author Nir Klieman
* @version 1.00
*/

public class GlobalVars {
	
	/**
	*  General data 
	*  ip             - IP of the system
	*  port           - port for create the users (i.e. 8081)
	*  domain         - domain for create new users (i.e. onebox3.com)
	*  url  		  - Default url for access the system (created by ip)
	*  chromeDrvPath  - Chrome driver path
	*  ieDrvPath      - IE driver path
	*  geckoPath	  - Gecko path (marionate for FF)
	*  version    	  - EMS ipp version (i.e 7.4.XXXX)
	*  downloadsPath  - Downloads path
	*  exportAlarms   - Name of the alarms page
	*  sysLogsPrefix  - Prefix of the logs of the system
	*  sysLogsActPre  - Prefix of the logs of the system (Activity)
	*  shFilesFiName  - Name of sherd-files
	*/
	private  String ip               = "10.21.8.32";
	private  String port             = "8081";
	private  String domain           = "cloudbond365b.com";
    private  String url  		     = ip + "/ipp/admin/AudioCodes_files/login.php";
	private  String chromeDrvPath    = "C:\\Users\\nirk\\Desktop\\Selenium\\chromedriver_win32_4\\chromedriver.exe";
	private  String ieDrvPath        = "C:\\Users\\nirk\\Desktop\\Selenium\\IEDriverServer_x64_2.53.1\\IEDriverServer.exe";	
	private  String geckoPath        = "C:\\Users\\nirk\\Desktop\\Selenium\\geckodriver-v0.11.1-win64\\geckodriver.exe";
	private  String version			 = "7.6.132";
	private  String downloadsPath    = "";
	private  String exportAlarms     = "ExportAlarmsStatus.csv";
	private  String sysLogsPrefix    = "IPP.Manage.Web.Admin.";
	private  String sysLogsActPre    = "IPP.Manage.Activity.";
	private  String shFilesFiName    = "Files.zip";
	
	/**
	*  Login data
	*  mainPageStr    - Main page welcome string (used for detect good login)
	*  sysLogin    	  - Default system login data
	*  monitSysLogin  - Default Monitoring (System) login data
	*  monitTenLogin  - Default Monitoring (Tenant) login data
	*  operSysLogin   - Default Operation (System) login data
	*  operTenLogin   - Default Operation (System) login data
	*  sysMainStr     - Default string in the welcome page (used for verify access)
	*  failLogMainStr - Default string for login-error
	*  sysInvalidStr  - Default string for detect invalid login
	*  browsersList	  - Array of current browsers for run for each Junit
	*/
	private  String mainPageStr		 = "Login to IP Phone Manager Pro";
    private  String [] sysLogin  	 = {"nir"					, "1q2w3e$r5t"};
    private  String [] monitSysLogin = {"Nir_Monitoring4"		, "1q2w3e$r5t"};
    private  String [] monitTenLogin = {"Nir_Monitoring_Tenant" , "1q2w3e$r5t"};
    private  String [] operSysLogin  = {"Nir_Operation_Tenant2"	, "1q2w3e$r5t"};
    private  String [] operTenLogin  = {"Nir_Operation2"		, "1q2w3e$r5t"};
	private  String sysMainStr       = "NETWORK TOPOLOGY";
	private  String failLogMainStr   = "Invalid credentials";
	private  String sysInvalidStr    = "Wrong username or password";
	private  Object[][] browsersList = {{enumsClass.browserTypes.CHROME}};

	/**  
	*  // Create user via POST data
	*  crUserBatName  - Name of the batch file that call the exe that create the users via POST queries
	*  crAlrmsBatName - Name of the batch file that call the exe that sends alarms to the system via POST queries
	*  crKpAlvBatName - Name of batch file that sends keep alive packet for a pre-created device
	*  acMacPrefix    - Audiocodes MAC prefix
	*  defTenant	  - Default Tenant
	*  defSite		  - Default Site
	*  defPhoneModel  - Default phone model
	*  nonDefTeanants - Array of non default Tenant
	*  spclCrsSite	  - Site with special characters
	*  firmVersion	  - Firmware version
	*/
	private  String crUserBatName    = "createUsersViaPost.exe"; 
    private  String crAlrmsBatName   = "alertEngine.exe";
    private  String crKpAlvBatName   = "changeStatus.exe";
	private  String acMacPrefix      = "00908f";	
	private  String defTenant      	 = "Nir";
	private  String defSite			 = "AutoDetection";
	private  String defPhoneModel    = "430HD";
	private  String nonDefTeanants[] = {"NirTest3", "NirTest2"};
	private  String spclCrsSite      = "Nir_()'<>/\\\":*&^%#@!~";
	private  String firmVersion      = "UC_2.0.13.121";
	
	/**
	*  Source-files names
	*  invalidCfg	  - Source File for invalid configuration
	*  srcImpConf	  - Source File name for import configuration
	*  srcImpUserDvcs - Source File name for import users + devices
	*  srcImp1000Usrs - Source File name for import 1000 users
	*  srcConfFile    - Source File name for import a configuration
	*  srcFirmFile	  - Source File name for import a firmware
	*  srcBigCfgFile  - Source File name for import a big configuration
	*  srcOperImport1 - Source File name for import users via Operation user
	*  srcOperImport2 - Source File name for import users via Operation user
	*  srcLangsFile   - Source File name for import users in different languages
	*  srcDhcpCnfFile - Source File name for import DHCP configuration file
	*  srcSbcConfFile - Source File name for import SBC configuration file
	*  srcFilesPath   - Source of files
	*/
	private  String invalidCfg   	 = "users.jpeg";
	private  String srcImpConf       = "Configuration.zip";
	private  String srcImpUserDvcs   = "users.csv";
	private  String srcImp1000Usrs   = "users1000.zip";
	private  String srcConfFile      = "my.cfg";
	private  String srcFirmFile      = "430HD.img";
	private  String srcBigCfgFile    = "templateVeryBig.cfg";
	private  String srcOperImport1   = "operImpUsersDevcs.zip";
	private  String srcOperImport2   = "Test54_2.zip";
	private  String srcLangsFile     = "nirDdifferentLanguages.zip";
	private  String srcDhcpCnfFile   = "dhcp_option_template.cfg";
	private  String srcSbcConfFile   = "proxy_dhcp_option_template.cfg";
	private  String srcFilesPath     = "C:\\Users\\nirk\\Desktop\\myEclipseProjects\\EMS_Tests_7_6_nonLync\\sourceFiles\\EMS";	

    /**
    *  Non-default constructor for provide another data
    *  @param _url  	Non-default url for access the system
	*  @param _username Non-default username for access the system
	*  @param _password Non-default password for access the system
	*  @param _mainStr  Non-default string for verify good access
    */
	public GlobalVars(String _url, String _username, String _password, String _mainStr) {
		
		System.out.println("GlobalVars constructor is called");
		this.url 		   = _url;
		this.sysLogin[0]   = _username;
		this.sysLogin[1]   = _password;
		this.sysMainStr    = _mainStr;
    	this.downloadsPath = "C:\\Users\\" + System.getProperty("user.name") + "\\Downloads";
	}
    
    public GlobalVars() {
    	
    	this.downloadsPath = "C:\\Users\\" + System.getProperty("user.name") + "\\Downloads";  		
	}
    
	/**
    *  Default method for return the url variable
    *  @return url of the system
    */
	public String getUrl()         {  return  url;             }
	
	/**
    *  Default method for return the mainPageStr variable
    *  @return main string of the system
    */
	public String getMainPageStr() { return  mainPageStr;      }
	
    /**
    *  Default method for return the main-str variable
    *  @return sysStr of the system
    */
	public String getSysMainStr() { return  sysMainStr;        }
	
    /**
    *  Default method for return the main-str fail-login variable
    *  @return sysStr of the system
    */
	public String getFailLogStr() { return  failLogMainStr;    }
	
    /**
    *  Default method for return the invalid-str variable
    *  @return sysInvalidStr of the system
    */
	public String getSysInvalidStr() { return  sysInvalidStr;  }

    /**
    *  Default method for return the Chrome driver path
    *  @return chromeDrvPath of the system
    */
	public String getchromeDrvPath() { 	return  chromeDrvPath; }
	
    /**
    *  Default method for return the IE driver path
    *  @return ieDrvPath of the system
    */
	public String getIeDrvPath()     { return  ieDrvPath;      }
	
    /**
    *  Default method for return the path for the directory of the source files
    *  @return srcFilesPath of the system
    */
	public String getSrcFilesPath() {  return  srcFilesPath;   }
	
    /**
    *  Default method for return the System IP
    *  @return ip
    */
	public String getIp() 			{ return  ip;			   }
	
    /**
    *  Default method for return the System Port
    *  @return port
    */
	public String getPort()        { return  port;			   }
		
    /**
    *  Default method for return the System Domain
    *  @return port
    */
	public String getDomain() 	   { return  domain;		   }
	
    /**
    *  Default method for return the AutoIT exe  that creates a user
    *  @return crUserBatName
    */
	public String getCrUserBatName() { return  crUserBatName;  }
	
    /**
    *  Default method for return the AutoIT exe  that sends an alarm
    *  @return crAlrmsBatName
    */
	public String getAlarmsBatName() { return  crAlrmsBatName;  }
	
    /**
    *  Default method for return the AutoIT exe  that sends a Keep-alive packet
    *  @return crSndKpAlveBatName
    */
	public String getKpAlveBatName() { return  crKpAlvBatName;  }
	
    /**
    *  Default method for return the AC prefix string
    *  @return acMacPrefix
    */
	public String getAcMacPrefix()   { return  acMacPrefix;    }
	
    /**
    *  Default method for return the default Tenant
    *  @return defTenant
    */
	public String getDefTenant()    { return  defTenant;       }
	
	
    /**
    *  Default method for return the default Site
    *  @return defSite
    */
	public String getDefSite()    { return  defSite;       	   }
	
    /**
    *  Default method for return a Site or Tenant with special characters in its name
    *  @return spclCrsSiteTen[i]
    */
	public String getSpecialCharsSite(int i)  { return  spclCrsSite; }  
	
    /**
    *  Default method for return ojne of the non-default Tenants
    *  @return nonDefTeanants[i]
    */
	public String getNonDefTenant(int i) {
		
		return  nonDefTeanants[i];
	}	
	
    /**
    *  Default method for return the default phone-model (used mainly for create-user-via-post method)
    *  @return defPhoneModel
    */
	public String getDefPhoneModel() { return  defPhoneModel;  }
	
    /**
    *  Default method for return the version
    *  @return version
    */
	public String getVersion()       { return  version;        }
	
    /**
    *  Default method for return the default firmware version of phone
    *  @return firmVesrion
    */
	public String getFirmVersion()   { return  firmVersion;    }
	
    /**
    *  Default method for return the downloads path
    *  @return version
    */
	public String getDownloadsPath() { return  downloadsPath;  }
	
    /**
    *  Default method for return the alarms export file name
    *  @return exportAlarms
    */
	public String getAlarmsExport() { return  exportAlarms;    }
	
    /**
    *  Default method for return the prefix of the System-logs
    *  @return sysLogsPrefix
    */
	public String getSysLogsPrefix() { return  sysLogsPrefix; }
	
    /**
    *  Default method for return the prefix of the System-logs-Activity
    *  @return sysLogsActPre
    */
	public String getSysLogsActPre() { return  sysLogsActPre; }
	
    /**
    *  Default method for return a name of source file by given integer
    *  @param idx Index of the current test in the format of <test>.<sub-test>
    *  @return String that represent name of the used file
    */
	public String getImportFile(String idx) {
		
		String usedSrcFile = "";
		switch (idx) {
		
			case "11":
				 usedSrcFile =  srcImpConf;
				 break;
			case "12.1":
				 usedSrcFile =  srcImpUserDvcs;
				 break;	
			case "12.2":
				 usedSrcFile =  srcImp1000Usrs;
				 break;			 
			case "30":
				 usedSrcFile =  srcConfFile;
				 break;
			case "31":
				 usedSrcFile =  srcFirmFile;
				 break;
			case "35.2":
				 usedSrcFile =  invalidCfg;
				 break;	
			case "36":
				 usedSrcFile =  srcBigCfgFile;
				 break;
			case "54.1":
				 usedSrcFile =  srcOperImport1;
				 break;
			case "54.2":
				 usedSrcFile =  srcOperImport2;
				 break;
			case "65":
				 usedSrcFile =  srcLangsFile;
				 break;
			case "66":
				 usedSrcFile =  srcSbcConfFile;
				 break;
			case "67":
				 usedSrcFile =  srcDhcpCnfFile;
				 break;
			default:
				usedSrcFile = "";
				break;
		}
		System.out.println("   usedSrcFile - " + usedSrcFile);	
		return usedSrcFile;
	}

    /**
    *  Default method for return the used browsers in the current test
    *  @return browsersList
    */
	public Object[][] getBrowsers() 	 { return  browsersList;   }

    /**
    *  Default method for return the gecko driver path (the external driver for ff)
    *  @return ieDrvPath of the system
    */
	public String getGeckoPath()         { return  geckoPath;      }
	
    /**
    *  Default method for return the System login data
    *  @param wantedData - wanted data (username or password)
    *  @return sysLogin  - username or password
    */
	public String getSysLoginData(loginData wantedData)     {
		
		switch (wantedData) {
			case USERNAME:
				return  sysLogin[0];      	
			case PASSWORD:
				return  sysLogin[1];
		}
		return null;
	}
	
    /**
    *  Default method for return the Monitoring-System login data
    *  @param wantedData     - wanted data (username or password)
    *  @return monitSysLogin - username or password
    */
	public String getMonitSysLoginData(loginData wantedData)     {
		
		switch (wantedData) {
			case USERNAME:
				return  monitSysLogin[0];      	
			case PASSWORD:
				return  monitSysLogin[1];
		}
		return null;
	}
	
    /**
    *  Default method for return the Monitoring-Tenant login data
    *  @param  wantedData    - wanted data (username or password)
    *  @return monitTenLogin - username or password
    */
	public String getMonitTenLoginData(loginData wantedData)     {
		
		switch (wantedData) {
			case USERNAME:
				return  monitTenLogin[0];      	
			case PASSWORD:
				return  monitTenLogin[1];
		}
		return null;
	}
	
    /**
    *  Default method for return the Operation-System login data
    *  @param  wantedData   - wanted data (username or password)
    *  @return operSysLogin - username or password
    */
	public String getOperSysLoginData(loginData wantedData)     {
		
		switch (wantedData) {
			case USERNAME:
				return  operSysLogin[0];      	
			case PASSWORD:
				return  operSysLogin[1];
		}
		return null;
	}
	
    /**
    *  Default method for return the Operation-Tenant login data
    *  @param wantedData    - wanted data (username or password)
    *  @return operTenLogin - username or password
    */
	public String getOperTenLoginData(loginData wantedData)     {
		
		switch (wantedData) {
			case USERNAME:
				return  operTenLogin[0];      	
			case PASSWORD:
				return  operTenLogin[1];
		}
		return null;
	}
	
    /**
    *  Default method for return the Sharefiles file-name
    *  @return shareFilesFileName
    */
	public String getShareFilesName()    {  return  shFilesFiName; }

}