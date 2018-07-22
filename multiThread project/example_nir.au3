; Global source files
#include 'authread_nir.au3'
#include <Constants.au3>
#include <MsgBoxConstants.au3>
#include <IE.au3>
#include <Array.au3>
#include <Date.au3>
#include <File.au3>
#include <http.au3>

; Global variables (this section can be modified)
Global $IP	 		 		 = "10.21.8.35"		; Target IP of the system
Global $PORT	 		 	 = "8081"				; Target Port of the system
Global const $STATUS 	     = "registered"			; Wanted status of create
Global const $PHONE_TYPE 	 = "430HD"				; Wanted model
Global const $TENANT 	  	 = "Nir"				; Wanted tenant
Global const $LOCATION 		 = "myLocation"			; Wanted location
Global const $RATE_OF_CREATE = 500					; Wanted of rate (1 user every <$RATE_OF_CREATE> ms)
Global const $USRS_PREFIX	 = "loadThreads_"   	; Wanted prefix
Global const $DOMAIN_NAME	 = "cloudbond365b.com"  ; Wanted domain
Global const $USERS_NUM   	 = 5					; Wanted users number

; Gloabal phone variables (this section better NOT to be modified)
Global const $THREADS_NUM 			 = 10																				; Threads number
Global const $EMS_PHONE_FIRM_VERSION = "UC_2.0.13.121"							 										; Version number for users which created via POST query
Global const $EMS_URL		 		 = "/rest/v1/ipphoneMgrStatus/keep-alive"											; URL for the second POST query that sent when creating a user via POST query
Global const $EMS_PHONE_USER_AGENT	 = "AUDC-IPPhone-" & $PHONE_TYPE & "_"  & $EMS_PHONE_FIRM_VERSION & "/1.0.0000.0"	; User-agent for users which created via POST query
Global const $AUDC_MAC_PREFIX		 = "00908f"									 										; General prefix for all Audiocodes given MAC addresses
Global const $EMS_SUBNET 	 	     = "255.255.255.0"										 							; EMS Subnet
Global const $EMS_PASSWORD		     = "3f6d0199102b53ca0a37a527f0efc221"	  				 							; EMS password
Global const $EMS_USERNAME	         = "system"							  					 							; EMS username
Global const $SESSION_ID	     	 = "3f6d0199102b53ca0a37a527f0efc221"  					 							; Session ID
Global const $USERS_PREFIX_NAME	 	 = $USRS_PREFIX & StringReplace(_NowTime(), ":", "_")  								; Users prefix In the format of <$USRS_PREFIX><Time> (I.e 'loadThreads_11_2_14')

; Callback data (must not be MODIFIED)
Global const $crUsrsThrd  	 = "sendalert"
Global $hThreadPidArr[$THREADS_NUM]

; Create & Initliaze $isThrFreeArr &  $hThreadPidArr arrays
_AuThread_Startup()
For $i = 0 to $THREADS_NUM - 1

	$hThreadPidArr[$i] = _AuThread_StartThread($crUsrsThrd)
    myToolTip($i & " pid - " & $hThreadPidArr[$i])

Next

; Main function of the thread
Func sendalert()

	While 1
		$currThrData = _AuThread_GetMessage()

		; If data was received from the Main thread
		If $currThrData Then

			; Read data from main thread
			Local $values  = StringSplit($currThrData, ";")
			$_index 	 	 		 = $values[1]
			$_userName 	 	 		 = $values[2]
			$_userId 	 	 	 	 = $values[3]
			$_macAddress    	 	 = $values[4]
			$_ipAddress	 	 		 = $values[5]
			$_phoneStatus  	 		 = $values[6]
			$_phoneNumber  	 		 = $values[7]
			$_location  	 	 	 = $values[8]
			$_btoeStatus		 	 = $values[9]
			$_btoeversion		 	 = $values[10]
			$_usbHeadsetType	 	 = $values[11]
			$_hrsSpeakerModel	 	 = $values[12]
			$_hrsSpeakerFW			 = $values[13]

			; Set extra data
			$target2 = "http://" & $IP & ":" & $PORT & $EMS_URL
			$extraDot        = ""

			; Stage 0 -  send packet with primary data to make the system be reasy for the 'create-user' method.
			myToolTip("Stage 0 -  send packet with primary data to make the system be reasy for the 'create-user' method.")
			$PostData0 = "{" 												   			  				   		   & @LF & _
							   '"sessionId"'       & ":" & " " & '"' & $SESSION_ID 				& '",' 			   & @LF & _
							   '"emsUserName"' 	   & ":" & " " & '"' & $EMS_USERNAME 			& '",' 			   & @LF & _
							   '"emsUserPassword"' & ":" & " " & '"' & $EMS_PASSWORD 			& '",' 			   & @LF & _
							   '"mac"' 			   & ":" & " " & '"' & $_macAddress    			& '",' 			   & @LF & _
							   '"ip"'	  		   & ":" & " " & '"' & $_ipAddress				& '",' 			   & @LF & _
							   '"subnet"'		   & ":" & " " & '"' & $EMS_SUBNET       		& '",' 		   	   & @LF & _
							   '"region"'		   & ":" & " " & '"' & $TENANT       			& '",' 		   	   & @LF & _
							   '"model"'		   & ":" & " " & '"' & $PHONE_TYPE    			& '",' 			   & @LF & _
							   '"fwVersion"'	   & ":" & " " & '"' & $EMS_PHONE_FIRM_VERSION  & '",' 			   & @LF & _
							   '"userAgent"'	   & ":" & " " & '"' & $EMS_PHONE_USER_AGENT    & '",' 			   & @LF & _
							   '"userName"'		   & ":" & " " & '"' & ""     					& '",' 			   & @LF & _
							   '"userId"'		   & ":" & " " & '"' & ""       				& '",' 			   & @LF & _
							   '"status"'		   & ":" & " " & '"' & "started"  	    		& '",' 			   & @LF & _
							   '"phoneNumber"' 	   & ":" & " " & '"' & ""  						& '",' 			   & @LF & _
							   '"location"' 	   & ":" & " " & '"' & $_location  				& '",' 			   & @LF & _
							   '"sipProxy"'		   & ":" & " " & '"' & ""     					& '"'  & $extraDot & @LF & _
																	   $_btoeStatus					 	   				 & _
																	   $_btoeversion					 	   			 & _
																	   $_usbHeadsetType		   			   				 & _
																	   $_hrsSpeakerModel		   			   			 & _
																	   $_hrsSpeakerFW		   			   	   			 & _
						   "}"
			myToolTip("$PostData0 is:" & @CRLF & $PostData0)
			myToolTip("$target2 - " & $target2)
			$Socket = _HTTPConnect($IP)
			Sleep(7000)
			_HTTPPost($IP, $target2, $Socket, $PostData0)
			$recv = _HTTPRead($Socket,0)
			_HTTPClose($Socket)
			Sleep(1000)

			; Stage 1 -  Start create user according to given MAC and region.
			myToolTip("Stage 1 -  Start create user according to given MAC and region.")
			$target = "http://" & $IP & "/ipp/tenant/" & $TENANT & "/" & $_macAddress &  ".cfg"
			myToolTip("$target - " & $target)
			$oHTTP = ObjCreate("winhttp.winhttprequest.5.1")
			$oHTTP.Open("GET", $target, False)
			$oHTTP.SetRequestHeader("User-Agent", $EMS_PHONE_USER_AGENT)
			$oHTTP.Send()
			Sleep(7000)
			$oReceived = $oHTTP.ResponseText
			$oStatusCode = $oHTTP.Status
			myToolTip("$oStatusCode - " & $oStatusCode)

			; Stage 2 -  Create the user + device with all the data
			myToolTip("Stage 2 -  Create the user + device with all the data")
			$PostData = "{" 														   			   					  & @LF & _
							   '"sessionId"'          & ":" & " " & '"' & $SESSION_ID 			   & '",' 			  & @LF & _
							   '"emsUserName"' 	      & ":" & " " & '"' & $EMS_USERNAME 		   & '",' 			  & @LF & _
							   '"emsUserPassword"'    & ":" & " " & '"' & $EMS_PASSWORD 		   & '",'			  & @LF & _
							   '"mac"' 			      & ":" & " " & '"' & $_macAddress    		   & '",'			  & @LF & _
							   '"ip"'	  			  & ":" & " " & '"' & $_ipAddress			   & '",' 			  & @LF & _
							   '"subnet"'		  	  & ":" & " " & '"' & $EMS_SUBNET       	   & '",' 			  & @LF & _
							   '"model"'		  	  & ":" & " " & '"' & $PHONE_TYPE    		   & '",' 			  & @LF & _
							   '"fwVersion"'		  & ":" & " " & '"' & $EMS_PHONE_FIRM_VERSION  & '",' 			  & @LF & _
							   '"userAgent"'		  & ":" & " " & '"' & $EMS_PHONE_USER_AGENT    & '",' 			  & @LF & _
							   '"userName"'		  	  & ":" & " " & '"' & $_userName     		   & '",' 		  	  & @LF & _
							   '"userId"'		  	  & ":" & " " & '"' & $_userId       		   & '",' 		 	  &	@LF & _
							   '"status"'		  	  & ":" & " " & '"' & $_phoneStatus  		   & '",' 	 		  & @LF & _
							   '"phoneNumber"' 	      & ":" & " " & '"' & $_phoneNumber  		   & '",' 			  & @LF & _
							   '"location"' 	  	  & ":" & " " & '"' & $_location  			   & '",' 			  & @LF & _
							   '"sipProxy"'		  	  & ":" & " " & '"' & $DOMAIN_NAME     		   & '"'  & $extraDot & @LF & _
																		  $_btoeStatus					 			  		& _
																		  $_btoeversion					 			  		& _
																		  $_usbHeadsetType		   			   		  		& _
																		  $_hrsSpeakerModel		   			   		  		& _
																		  $_hrsSpeakerFW		   			   		  		& _
						"}"
			   myToolTip("$PostData is:" & @CRLF & $PostData)
				 ;~    HttpPost($target2, $PostData)
			   $Socket = _HTTPConnect($IP)
			   $retBytes = _HTTPPost($IP, $target2, $Socket, $PostData)
			   Sleep(15000)
			   $recv = _HTTPRead($Socket,0)
			   _HTTPClose($Socket)

			   ; Mark that we finished
 			   _AuThread_SendMessage(_AuThread_MainThread(), $_index)
		EndIf

	WEnd
 EndFunc

; main thread
Local $crrThrIdx = -1
Local $currThrData = ""
For $i = 0 To $USERS_NUM Step 1

   myToolTip("------------------------------------")
   myToolTip("Round <" & $i & ">")
   $crrThrIdx = selectFreeThread($crrThrIdx)

   ; Create Data
   $userName     = $USERS_PREFIX_NAME & "_" & $i
   myToolTip("   $userName - "     & $userName)
   $userId	     = $userName & "@" & $DOMAIN_NAME
   $macAddress   = myRandomMacAddress()
   $phoneNumber  = myRandomPhoneNumber()
   $ipAddress    = "http://172.17.121.13" ;myRandomIp()
   $createData = createData($i, $currThrData, $userName, $userId, $macAddress, $phoneNumber, $ipAddress, $STATUS, "", "", "", "", "")

   ; Send action for the Thread
   _AuThread_SendMessage($hThreadPidArr[$crrThrIdx],  $createData)
   myToolTip("------------------------------------")
   Sleep($RATE_OF_CREATE)

Next

; Close threads
myToolTip("Close threads")
$idx = 0
While True

   ; Read message from the threads
   $msg = _AuThread_GetMessage()
   If $msg Then

	  myToolTip($msg)
	  _AuThread_CloseThread($hThreadPidArr[$msg])
	  $idx += 1
	  myToolTip("$idx - " & $idx)
	  If $idx = $USERS_NUM Then

			myToolTip("All the users were created !!")
			ExitLoop
	  EndIf
   EndIf

WEnd

; Close the other un-busy threads
myToolTip("Close the other un-busy threads")
For $i = $USERS_NUM To $THREADS_NUM - 1 Step 1

    myToolTip("Close Thread <" & $i & ">")
   _AuThread_CloseThread($hThreadPidArr[$i])

Next

;--------------------------------------------------------------------------------------------------
; Retrun index of Thread by RR algorythm
;
; Input:
;	$crrThrIdx - index  of last used Thread
;
; Output:
;	$crrThrIdx - index of next used Thread
;
;--------------------------------------------------------------------------------------------------
Func selectFreeThread($crrThrIdx)

   $crrThrIdx += 1
   If $crrThrIdx == $THREADS_NUM Then
	  $crrThrIdx = 0
   EndIf
   myToolTip("selectFreeThread() Select Thread <" & $crrThrIdx& ">")
   Return $crrThrIdx

EndFunc
;--------------------------------------------------------------------------------------------------
; Retrun Struct of data that sent to the Threads
;
; Input:
;	$index			 - index of the current thread
; 	$currThrData 	 - String which will be used as a container for all the data
; 	$userName 		 - Wanted username
; 	$userId 		 - Wanted userId (conatant)
; 	$macAddress 	 - Wanted MAC address
; 	$phoneNumber 	 - Wanted phone Number
; 	$ipAddress 		 - Wanted IP address
; 	$phoneStatus 	 - Wanted phone status
; 	$btoeStatus 	 - Wanted BtoE status
; 	$btoeversion 	 - Wanted BtoE version
; 	$usbHeadsetType  - Wanted headset type
; 	$hrsSpeakerModel - Wanted Speakers model
; 	$hrsSpeakerFW    - Wanted HRS speaker-FW
;
; Output:
;	$currThrData - Struct of data
;
;--------------------------------------------------------------------------------------------------
Func createData($index, $currThrData, $userName, $userId, $macAddress, $phoneNumber, $ipAddress, $phoneStatus, $btoeStatus, $btoeversion, $usbHeadsetType, $hrsSpeakerModel, $hrsSpeakerFW)

   $currThrData =  $index 	 	 								   & ";" & _
				   $userName 	 	 							   & ";" & _
				   $userId 	 	 	 							   & ";" & _
				   $macAddress    	 							   & ";" & _
				   $ipAddress	 	 							   & ";" & _
				   $phoneStatus  	 							   & ";" & _
				   $phoneNumber  	 							   & ";" & _
				   $location  	 	 							   & ";" & _
				   $btoeStatus		 							   & ";" & _
				   $btoeversion		 							   & ";" & _
				   $usbHeadsetType	 							   & ";" & _
				   $hrsSpeakerModel	 							   & ";" & _
				   $hrsSpeakerFW
   myToolTip("   createData()")
   Return $currThrData

EndFunc
;--------------------------------------------------------------------------------------------------
; Retrun random MAC address in format of <AUDC_PREFIX>+<prefix>
;
; Output:
;	$sText - Random MAC address in format of <AUDC_PREFIX>+<prefix>
;
;--------------------------------------------------------------------------------------------------
Func myRandomMacAddress()

	Local $sText = ""
	For $i = 1 To 6

		; Decide if small character or number is needed
		$result = Random(0, 1, 1)
		 If $result = 0 Then
			$sText &= Chr(Random(97, 102, 1))
		 Else
			$sText &= Chr(Random(48, 57, 1))
		 EndIf

	  Next
    $sText2 = $AUDC_MAC_PREFIX & $sText
    myToolTip("   myRandomMacAddress() MAC address - " & $sText2)
   return $sText2

EndFunc
;--------------------------------------------------------------------------------------------------
; Retrun random phone number
;
; Optional-Input:
; 	$length - length of wanted phone number. Default is 9.
;
; Output:
;	$sText - Random Phone number
;
;--------------------------------------------------------------------------------------------------

Func myRandomPhoneNumber($length = "9")

   Local $sText = ""
   For $i = 1 To $length

	  $sText &= Chr(Random(48, 57, 1))
   Next
    myToolTip("   myRandomPhoneNumber() Phone-number - " & $sText)
   return $sText

EndFunc
;--------------------------------------------------------------------------------------------------
; Retrun random IP address
;
; Output:
;	$sText - Random IP address
;
;--------------------------------------------------------------------------------------------------
Func myRandomIp()

   Local $sText = ""
   For $i = 1 To 3

	  $sText &= Random(1, 127, 1) & "."
   Next
   $sText &= Random(1, 127, 1)
   myToolTip("   myRandomIp() IP - ")
   return $sText

EndFunc
;--------------------------------------------------------------------------------------------------
; Print a given text on tooltip and console
;
; Input:
;	$txt - a given text
;
;--------------------------------------------------------------------------------------------------
Func myToolTip($txt)

   $disTxt = _NowTime() & "   " & $txt & @LF
   ConsoleWrite($disTxt)
   ToolTip(@LF & $disTxt & @LF & "---------------------------------------" , 150, 150)

EndFunc