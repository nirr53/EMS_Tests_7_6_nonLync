package EMS_Tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import EMS_Tests.enumsClass.*;

public class SendPOSTRequset {
	
	
	private enum reqMode  {INITIAL, GET_CONF, CREATE_USR_DVC; }	
	GlobalFuncs	   testFuncs;
	private String ip;
	private String port;
	private String sessionID;
	private String emsUsername;
	private String emsPassword;
	private String subnet;
	private String fwVersion;

	// Default constructor  
	public SendPOSTRequset(String ip, String port) {
		  		  
		testFuncs = new GlobalFuncs(); 
		this.ip   = ip;
		this.port = port;
		
		// Set inner data
		this.sessionID   = "a033bb54";
		this.emsUsername = "system";
		this.emsPassword = "3f6d0199102b53ca0a37a527f0efc221";
		this.subnet 	 = "255.255.255.255";
		this.fwVersion 	 = "UC_2.0.13.121";
	}
	
    /**
    *  Set IP
    *  @param ip - IP for set
    */
	public void setIP(String ip) {
		
		this.ip = ip;	
	}
	
    /**
    *  Set fwVersion
    *  @param fwVersion - fwVersion for set
    */
	public void setFwVersion(String fwVersion) {
		
		this.fwVersion = fwVersion;	
	}
	
    /**
    *  Get IP
    *  @return ip - IP for send
    */
	public String getIP() {
		
		return this.ip;	
	}
	
    /**
    *  Set Port
    *  @param port - Port for set
    */	
	public void setPort(String port) {
		
		this.port = port;	
	}
	
    /**
    *  Get Port
    *  @return port - port for send
    */
	public String getPort() {
		
		return this.port;	
	}
	
    /**
    *  Send the data to the given url
    *  @param url       - Url of the destination
    *  @param postData  - The data that should be sent
    *  @param userAgent - Given User agent
    *  @return result   - Result of the send
    */	
	private String sendPostQuery(String url, String postData, String userAgent) {
		
	    PrintWriter    out    = null;
	    BufferedReader in 	  = null;
	    String 		   result = "";
	    try {
	    	  	  
	    	testFuncs.myDebugPrinting("url - " + url ,enumsClass.logModes.MINOR);
	        URL realUrl = new URL(url);
	        
	        // Build connection
	    	testFuncs.myDebugPrinting("Build connection" ,enumsClass.logModes.MINOR);
	        URLConnection conn = realUrl.openConnection();
	        
	        // Set request properties
	    	testFuncs.myDebugPrinting("Set request properties" ,enumsClass.logModes.MINOR);
	        conn.setRequestProperty("accept", "*/*");
	        conn.setRequestProperty("connection", "Keep-Alive");
	        conn.setRequestProperty("user-agent", userAgent);
	        
	        // Enable output and input
	    	testFuncs.myDebugPrinting("Enable output and input" ,enumsClass.logModes.MINOR);
	        conn.setDoOutput(true);
	        conn.setDoInput(true);
	        out = new PrintWriter(conn.getOutputStream());
	        
	        // Send POST DATA
	    	testFuncs.myDebugPrinting("Send POST DATA" ,enumsClass.logModes.MINOR);
	        out.print(postData);
	        out.flush();
	        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String line;
	        while ((line = in.readLine()) != null) {
	        	
	            result += "\n" + line;
//		    	testFuncs.myDebugPrinting(result ,enumsClass.logModes.DEBUG);

	        }
	    } catch (Exception e) {
	    	
	        e.printStackTrace();
	    }
	  
	    finally {
	    	
	    	try {
		    	testFuncs.myDebugPrinting("Close connections" ,enumsClass.logModes.MINOR);
	            if (out != null) {            	
	                out.close();
	            }
	            if (in != null) {
	                in.close();
	            }      
	    	} catch (IOException ex) {
	        	
	            ex.printStackTrace();   
	    	}
	    }
	    return result;
	}
	
    /**
    *  Wrap the data with semicolons and line-breaks
    *  @param header    - Header for wrap
    *  @param value     - Data for wrap
    *  @return request  - Request after wrap
    */	
	private String addRow(String header, String value) {
		
		String temp = "\"" + header + "\"" + ":" + "\"" + value + "\"" + ",\n";
		return temp;		
	}
	
    /**
    *  Build a request according to given mode
    *  @param mode      - Mode according to it, we build the request
    *  @param data      - Map of keys and values, used for building the request
    *  @return request  - Request after full wrap
    */	
	private String buildRequest(reqMode mode, Map<String, String> data) {
				
		String request = "{" + "\n";
    	testFuncs.myDebugPrinting("Case - " + mode.toString() ,enumsClass.logModes.MINOR);
		switch (mode) {
		
			case INITIAL:
			case CREATE_USR_DVC:

				for (String key : data.keySet()) {
					
					request += addRow(key, data.get(key));
				}
				break;
			case GET_CONF:
				break;
			default:
				break;	
		}
		
		request += "}";
		return request;	
	}
	
    /**
    *  Send request to given URL by given mode and data
    *  @param url  - URL of the destination
    *  @param mode - Mode according to it, we send the different requests
    *  @param data - Map of keys and values, used for building the request
    */	
	private void sendRequest(String url, reqMode mode, Map<String, String> data) {
		
		String request = buildRequest(mode, data);
    	testFuncs.myDebugPrinting("request - " + request, enumsClass.logModes.MINOR);
		sendPostQuery(url, request, data.get("userAgent"));
	}
	
    /**
    *  Get the data from other classes and perform several requests according to given mode
    *  @param workingMode - mode according to it we send the different requests - enum
    *  @param data 		  -  Map of keys and values, used for building the request
     * @param crDeviceData - Map of keys and values, used for building the second request (create device)
    */
	public void manageRequests(sendPOSTModes workingMode, Map<String, String> data, Map<String, String> crDeviceData) {
		
		switch (workingMode) {
		
			case CREATE_USER_DEVICE:
		    	testFuncs.myDebugPrinting("workingMode - " + "createUserDEvice", enumsClass.logModes.MINOR);
		    	
		    	// Set inner data
				data.put("sessionId"	  , this.sessionID);		
				data.put("emsUserName"    , this.emsUsername);
				data.put("emsUserPassword", this.emsPassword);
				data.put("subnet"		  , this.subnet);
				data.put("fwVersion"	  , this.fwVersion);
				data.put("userAgent"	  , "AUDC-IPPhone-" + data.get("model") + "_" + this.fwVersion + "/1.0.0000.0");
				data.put("userName"		  , "");
				data.put("userId"		  , "");
				data.put("phoneNumber"	  , "");
				data.put("sipProxy"		  , "");
				data.put("status"		  , "started");
				
				// Send first query
				String url = "http://" + this.ip + ":" + this.port + "/rest/v1/ipphoneMgrStatus/keep-alive";
				sendRequest(url, reqMode.INITIAL, data);	
				
				// Send second query
				String urlForConf = "http://" + this.ip + "/ipp/region/" + data.get("region") + "/" + data.get("mac") +  ".cfg";
				sendRequest(urlForConf, reqMode.GET_CONF, data);
				
				// Add extra data to map	
				for (String key : crDeviceData.keySet()) {			
					
					data.put(key, crDeviceData.get(key));					
				}
				sendRequest(url, reqMode.CREATE_USR_DVC, data);		
				break;

			default:
				break;	

		}
	}
}
