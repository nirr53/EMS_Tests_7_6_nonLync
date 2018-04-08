package EMS_Tests;

/**
* This class holds all the data which is used by the tests
* @author Nir Klieman
* @version 1.00
*/

public class GlobalVars {
	
	/**
	*  // General data 
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
	*  
	*  // Login data
	*  mainPageStr    - Main page welcome string (used for detect good login)
	*  sysUsername    - Default username for access the system
	*  sysPassword    - Default password for access the system
	*  monitUsername  - Default username for access the system via a Monitoring user with 'System'
	*  monitPassword  - Default password for access the system via a Monitoring user with 'System'
	*  monitTenUsrnme - Default username for access the system via a Monitoring user with 'Tenant'
	*  monitTenPswd   - Default password for access the system via a Monitoring user with 'Tenant'
	*  operUsername   - Default username for access the system via an Operation user with 'System'
	*  operPassword   - Default password for access the system via an Operation user with 'System'
	*  sysMainStr     - Default string in the welcome page (used for verify access)
	*  sysInvalidStr  - Default string for detect invalid login
	*  
	*  // Create user via POST data
	*  crUserBatName  - Name of the batch file that call the exe that create the users via POST queries
	*  crAlrmsBatName - Name of the batch file that call the exe that sends alarms to the system via POST queries
	*  crKpAlvBatName - Name of batch file that sends keep alive packet for a pre-created device
	*  acMacPrefix    - Audiocodes MAC prefix
	*  defTenant	  - Default Tenant
	*  defSite		  - Default Site
	*  defPhoneModel  - Default phone model
	*  
	*  // Source-files names
	*  srcImpConf	  - Source File name for import configuration
	*  srcImpUser  	  - Source File name for import users
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
	*  
	*  // Browsers names
	*  CHROME 		  - name of Chrome browser
	*  FF 			  - name of Firefox browser
	*  IE		      - name of Internet-Explorer browser
	*/
    public   LogVars 				logerVars;
	private  String ip               = "10.21.8.35";
	private  String port             = "8081";
	private  String domain           = "cloudbond365b.com";
    private  String url  		     = ip + "/ipp/admin/AudioCodes_files/login.php";
    private  String crUserBatName    = "createUsersViaPost.exe"; 
    private  String crAlrmsBatName   = "alertEngine.exe";
    private  String crKpAlvBatName   = "changeStatus.exe";
    private  String mainPageStr		 = "Login to IP Phone Manager Pro";
    private  String sysUsername      = "acladmin";
	private  String sysPassword      = "pass_1234";
    private  String monitUsername    = "Nir_Monitoring4";
	private  String monitPassword    = "1q2w3e4r5t6y";
    private  String monitTenUsrnme   = "Nir_Monitoring_Tenant";
	private  String monitTenPswd     = "1q2w3e4r5t";
    private  String operTenUsrnme    = "Nir_Operation_Tenant2";
	private  String operTenPswd      = "1q2w3e4r5t";
    private  String operUsername     = "Nir_Operation2";
	private  String operPassword     = "1q2w3e4r5t";
	private  String sysMainStr       = "NETWORK TOPOLOGY";
	private  String failLogMainStr   = "Invalid credentials";
	private  String sysInvalidStr    = "Wrong username or password";
	private  String chromeDrvPath    = "C:\\Users\\nirk\\Desktop\\Selenium\\chromedriver_win32_4\\chromedriver.exe";
	private  String ieDrvPath        = "C:\\Users\\nirk\\Desktop\\Selenium\\IEDriverServer_x64_2.53.1\\IEDriverServer.exe";	
	private  String geckoPath        = "C:\\Users\\nirk\\Desktop\\Selenium\\geckodriver-v0.11.1-win64\\geckodriver.exe";
	private  String srcFilesPath     = "C:\\Users\\nirk\\Desktop\\myEclipseProjects\\EMS_Tests_7_4\\sourceFiles\\EMS";	
	private  String acMacPrefix      = "00908f";	
	private  String defTenant      	 = "Nir";
	private  String defSite			 = "AutoDetection";
	private  String spclCrsSite      = "Nir_()'<>/\\\":*&^%#@!~";
	private  String nonDefTeanants[] = {"NirTest1", "NirTest2"};
	private  String defPhoneModel    = "440HD";
	private  String version			 = "7.4.2101";
	private  String downloadsPath    = "";
	private  String exportAlarms     = "ExportAlarmsStatus.csv";
	private  String sysLogsPrefix    = "IPP.Manage.Web.Admin.";
	private  String sysLogsActPre    = "IPP.Manage.Activity.";
	private  String shFilesFiName    = "Files.zip";
	private  String srcImpConf       = "Configuration.zip";
	private  String srcImpUserDvcs   = "users.zip";
	private  String srcImp1000Usrs   = "users1000.zip";
	private  String srcConfFile      = "my.cfg";
	private  String srcFirmFile      = "430HD.img";
	private  String srcBigCfgFile    = "templateVeryBig.cfg";
	private  String srcOperImport1   = "operImpUsersDevcs.zip";
	private  String srcOperImport2   = "Test54_2.zip";
	private  String srcLangsFile     = "nirDdifferentLanguages.zip";
	private  String srcDhcpCnfFile   = "dhcp_option_template.cfg";
	private  String srcSbcConfFile   = "proxy_dhcp_option_template.cfg";
	public   String CHROME  	     = "Chrome";
	public   String FF 			     = "Firefox";
	public 	 String IE			     = "IE";
	private  Object[][] browsersList = {{CHROME}};

    /**
    *  Non-default constructor for provide another data
    *  @param _url  	Non-default url for access the system
	*  @param _username Non-default username for access the system
	*  @param _password Non-default password for access the system
	*  @param _mainStr  Non-default string for verify good access
    */
	public GlobalVars(String _url, String _username, String _password, String _mainStr) {
		
		System.out.println("GlobalVars constructor is called");
		this.url 		 = _url;
		this.sysUsername = _username;
		this.sysPassword = _password;
		this.sysMainStr  = _mainStr;
    	this.downloadsPath = "C:\\Users\\" + System.getProperty("user.name") + "\\Downloads";
    	this.logerVars     = new LogVars();
	}
	
    /**
    *  Default constructor for provide interface
    */
    public GlobalVars() {
    	
    	this.logerVars     = new LogVars();
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
    *  Default method for return the username variable
    *  @return username of the system for Admin
    */
	public String getSysUsername() { return  sysUsername; 	   }
	
    /**
    *  Default method for return the password variable
    *  @return password of the system for Admin
    */
	public String getSysPassword() { return  sysPassword;      }
	
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
    *  Default method for return the Monitoring username
    *  @return monitUsername
    */
	public String getMonitUsername()     { return  monitUsername;  }
	
    /**
    *  Default method for return the Monitoring password
    *  @return monitPassword
    */
	public String getMonitPassword()     { return  monitPassword;  }
	
    /**
    *  Default method for return the Monitoring username (with Tenant permission)
    *  @return monitTenUsrnme
    */
	public String getMonitTenUsername()  { return  monitTenUsrnme; }
	
    /**
    *  Default method for return the Monitoring password (with Tenant permission)
    *  @return monitTenPswd
    */
	public String getMonitTenPassword()  { return  monitTenPswd;   }
	
    /**
    *  Default method for return the Operation username
    *  @return operUsername
    */
	public String getOperUsername()      { return  operUsername;   }
	
    /**
    *  Default method for return the Operation password
    *  @return operPassword
    */
	public String getOperPassword()      { 	return  operPassword;  }
	
	
    /**
    *  Default method for return the Operation username (with Tenant permission)
    *  @return operTenUsrnme
    */
	public String getOperTenUsername()  { return  operTenUsrnme; }
	
    /**
    *  Default method for return the Operation password (with Tenant permission)
    *  @return operTenPswd
    */
	public String getOperTenPassword()  { return  operTenPswd;   }
	
    /**
    *  Default method for return the Sharefiles file-name
    *  @return shareFilesFileName
    */
	public String getShareFilesName()    {  return  shFilesFiName; }

}