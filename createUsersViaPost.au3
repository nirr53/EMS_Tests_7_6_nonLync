; Global source files
#include <Constants.au3>
#include <MsgBoxConstants.au3>
#include <IE.au3>
#include <Array.au3>
#include <Date.au3>
#include <File.au3>
#include <http.au3>

; Set arguments via command line
$IP	 				= $CmdLine[1]  ; I.e.  10.21.51.12
$PORT	 			= $CmdLine[2]  ; I.e.  8081
$USERS_NUMBER  	  	= $CmdLine[3]  ; I.e.  3
$USERS_PREFIX_NAME  = $CmdLine[4]  ; I.e.  mySahiUser
$DOMAIN_NAME 		= $CmdLine[5]  ; I.e.  onebox3.com
$CREATE_STATUS 		= $CmdLine[6]  ; I.e.  registered
$PHONE_TYPE 		= $CmdLine[7]  ; I.e.  430HD
$REGION_NAME 		= $CmdLine[8]  ; I.e.  Region_1
$LOCATION 		 	= $CmdLine[9]  ; I.e.  myLocation

; Set arguments directly
;~ $IP	 			  = "10.21.8.32"
;~ $PORT	 		  = "8081"
;~ $USERS_NUMBER  	  = "10"
;~ $USERS_PREFIX_NAME = "testWithMark" & Random(0, 100, 1)
;~ $DOMAIN_NAME 	  = "cloudbond365b.com"
;~ $CREATE_STATUS 	  = "registered"
;~ $PHONE_TYPE 		  = "430HD"
;~ $REGION_NAME 	  = "Nir"
;~ $LOCATION 		  = "myLocation"

; POST variables
Global $EMS_VERSION					= "UC_2.0.13.121"							 							; Version number for users which created via POST query
Global const $EMS_SUBNET 			= "255.255.255.0"														; Subnet for sending the packet
Global const $EMS_POST_USERS_PSWD	= "3f6d0199102b53ca0a37a527f0efc221"									; Password for users which created via POST query
Global const $SESSION_ID   			= "a033bb54"															; Defult session-ID for initiate a packet
Global const $EMS_USERNAME 			= 'system'																; Defult username
Global const $EMS_USER_AGENT		= "AUDC-IPPhone-" & $PHONE_TYPE & "_"  & $EMS_VERSION & "/1.0.0000.0"	; User-agent for users which created via POST query
Global const $EMS_POST_URL			= "/rest/v1/ipphoneMgrStatus/keep-alive"								; URL for the second POST query that sent when creating a user via POST query
Global const $AUDC_MAC_DEF_PREFIX	= "00908f"									 							; General prefix for all Audiocodes given MAC addresses

; DEBUG levels const variables
Global enum $DEBUG_TEST_DCC_LEVEL, _
			$DEBUG_TESTER_LEVEL	 , _
			$DEBUG_OUTFUNC_LEVEL , _
			$DEBUG_INFUNC_LEVEL  , _
			$DEBUG_SEARCH_LEVEL  , _
			$DEBUG_CNTDWN_LEVEL

; Global data
$USERS_NUMBER = StringStripWS($USERS_NUMBER, $STR_STRIPALL)
$USERS_NUMBER 					= Number($USERS_NUMBER)
$sFilePath = @WorkingDir & "\" & "success.txt"
FileDelete($sFilePath)


; -----------
;  Main() function
; -----------

myToolTip("Activate main()", 150, 150, $DEBUG_TEST_DCC_LEVEL)
myToolTip("$USERS_PREFIX_NAME - " & $USERS_PREFIX_NAME, 150, 150, $DEBUG_TEST_DCC_LEVEL)

; Take BToE version from 'Location' field.
if StringInStr($USERS_PREFIX_NAME, "BToE_user_version") Then
   myToolTip("Take BToE version from 'Location' field", 150, 150, $DEBUG_INFUNC_LEVEL)
   $EMS_VERSION = $LOCATION
   $LOCATION 	   = "mylocation"

EndIf

; Change the version field.
if StringInStr($USERS_PREFIX_NAME, "dvFilter2") Then
   myToolTip("Change the regular Version field", 150, 150, $DEBUG_INFUNC_LEVEL)
   $EMS_VERSION = "UC_3.1.0.478"

EndIf

createUsersViaPost($USERS_NUMBER, $USERS_PREFIX_NAME, $DOMAIN_NAME, $CREATE_STATUS, $PHONE_TYPE, $REGION_NAME, $LOCATION)
markEnd($sFilePath)
myToolTip("Exit main()", 150, 150, $DEBUG_TEST_DCC_LEVEL)

;--------------------------------------------------------------------------------------------------
Func myCountDown($target, $txt = "")

   For $i = 1000 To $target Step 1000
	  Sleep(1000)
	  ToolTip($txt & "(" & $i & "/" & $target, 150, 150)

   Next

EndFunc
;--------------------------------------------------------------------------------------------------
func createUsersViaPost($usersNumber	, _
						$usersPrefixName, _
						$domainName		, _
						$createStatus	, _
						$phoneType		, _
						$regionName		, _
						$location)

   myToolTip("enter createUsersViaPost()", 150, 150, $DEBUG_TESTER_LEVEL)
   For $i = 1 To $usersNumber Step 1

	  ; Set user-name
	  $userName = ""
	  If $usersNumber <> 1 Then
		 $userName     = $usersPrefixName & "_" & $i

	  Else
		 $userName     = $usersPrefixName

	  EndIf
	  ConsoleWrite(@LF & @LF)
	  ConsoleWrite("+-------------------------------------------------------+" & @LF)
      ConsoleWrite("|                                                       |" & @LF)
	  ConsoleWrite("|       " &  $userName & "                              |" & @LF)
      ConsoleWrite("|                                                       |" & @LF)
	  ConsoleWrite("+-------------------------------------------------------+" & @LF & @LF)

	  ; Set MAC address
	  $myMacAddress = ""
	  If StringInStr($usersPrefixName, "sameMac") Or StringInStr($usersPrefixName, "chngStts") Then
		 $myMacAddress = $AUDC_MAC_DEF_PREFIX & "123456"
		 myToolTip("Constant MAC address - " & $myMacAddress, 150, 150, $DEBUG_INFUNC_LEVEL)

	  Else
		 $myMacAddress = myRandomMacAddress()
		 myToolTip("Random MAC address - " & $myMacAddress, 150, 150, $DEBUG_INFUNC_LEVEL)

	  EndIf
	  writeMacToFile($myMacAddress, $i)
	  $phoneNumber  = myRandomPhoneNumber()
	  $ipAddress    = myRandomIp()
	  writeIPToFile($ipAddress, $i)
	  myToolTip("$userName - " & $userName, 150, 150, $DEBUG_OUTFUNC_LEVEL)
	  createUserViaPost($myMacAddress, $ipAddress, $phoneType, $userName,  $userName & "@" & $domainName, $createStatus, $phoneNumber, $DOMAIN_NAME, $regionName, $location)
	  myCountDown(1000,  "Create user " & $userName & " ended !!")

   Next
   myToolTip("exit createUsersViaPost()", 150, 150, $DEBUG_TESTER_LEVEL)

EndFunc
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
    $sText2 = $AUDC_MAC_DEF_PREFIX & $sText
   myToolTip("myRandomMacAddress() mac - " & $sText2, 150, 150, $DEBUG_OUTFUNC_LEVEL)
   return $sText2

EndFunc
;--------------------------------------------------------------------------------------------------
Func myRandomPhoneNumber($length = "9")

   Local $sText = ""
   For $i = 1 To $length
		 $sText &= Chr(Random(48, 57, 1))

   Next
   myToolTip("myRandomPhoneNumber() $sText - " & $sText, 150, 150, $DEBUG_OUTFUNC_LEVEL)
   return $sText

EndFunc
;--------------------------------------------------------------------------------------------------
Func myRandomIp()

   Local $sText = ""
   For $i = 1 To 3
	  $sText &= Random(1, 127, 1) & "."

   Next
   $sText &= Random(1, 127, 1)
   myToolTip("myRandomPhoneNumber() $sText - " & $sText, 150, 150, $DEBUG_OUTFUNC_LEVEL)
   return $sText

EndFunc
;--------------------------------------------------------------------------------------------------
Func createUserViaPost($macAdress  , _
					   $ipAddress  , _
					   $phoneType  , _
					   $userName   , _
					   $userId	   , _
					   $phoneStatus, _
					   $phoneNumber, _
					   $sipProxy   , _
					   $regionName , _
					   $location)

   myToolTip("enter createUserViaPost()", 150, 150, $DEBUG_OUTFUNC_LEVEL)
   myToolTip("$userName - " & $userName, 150, 150, $DEBUG_INFUNC_LEVEL)

   ; Set path
   $target2 = "http://" & $IP & ":" & $PORT & $EMS_POST_URL

   ; Add BtOe status and version
   $btoeStatus  = ""
   $btoeversion = ""
   $extraDot    = ""
   If 	  StringInStr($userName, "BToE_user") Then
	  myToolTip("BToE user was detected !! ($userName - " & $userName & ")", 150, 150, $DEBUG_INFUNC_LEVEL)
	  $btoeStatusStr = ""
	  If 	   StringInStr($userName, "BToE_user_dis")  Or StringInStr($userName, "BToE_user_version_dis") Then
		 myToolTip("BToE disabled was detected !!", 150, 150, $DEBUG_INFUNC_LEVEL)
		 $btoeStatusStr = "BToE disabled"

	  ElseIf   StringInStr($userName, "BToE_user_auto") Or StringInStr($userName, "BToE_user_version_auto") Then
		 myToolTip("BToE auto was detected !!", 150, 150, $DEBUG_INFUNC_LEVEL)
		 $btoeStatusStr = "auto paired"

	  ElseIf   StringInStr($userName, "BToE_user_man")  Or StringInStr($userName, "BToE_user_version_man") Then
		 myToolTip("BToE manual was detected !!", 150, 150, $DEBUG_INFUNC_LEVEL)
		 $btoeStatusStr = "manual paired"

	  ElseIf   StringInStr($userName, "BToE_user_not")  Or StringInStr($userName, "BToE_user_version_not") Then
		 myToolTip("BToE not-paired was detected !!", 150, 150, $DEBUG_INFUNC_LEVEL)
		 $btoeStatusStr = "not paired"

	  EndIf
	  $extraDot    = ","
	  $btoeStatus  = '"BToEpairingstatus"' & ":" & " " & '"' & $btoeStatusStr  & '",' & @LF
	  $btoeversion = '"BToEversion"' 	   & ":" & " " & '"' & $EMS_VERSION    & '"' & @LF
	  myToolTip("$btoeStatus - " & $btoeStatus, 150, 150, $DEBUG_INFUNC_LEVEL)
	  myToolTip("$btoeversion - " & $btoeversion, 150, 150, $DEBUG_INFUNC_LEVEL)

   EndIf

   ; Add $emsLocation status (If needed)
   $emsLocation     = getEmsLocation($userName)

   ; Add USBHeadsetType status (If needed)
   $usbHeadsetType  = getUSBHeadsetType($userName, $phoneNumber)

   ; Add HRSSpeakerModel status (If needed)
   $hrsSpeakerModel = getHrsSpeakerModel($userName, $phoneNumber)

   ; Add HRSSpeakerFW status (If needed)
   $hrsSpeakerFW    = getHrsSpeakerFW($userName, $phoneNumber)

   ; Stage 0 -  send packet with primary data to make the system be reasy for the 'create-user' method.
   myToolTip("Stage 0 -  send packet with primary data to make the system be reasy for the 'create-user' method.", 150, 150, $DEBUG_INFUNC_LEVEL)
   $PostData0 = "{" 												   			   	   & @LF & _
	'"sessionId"'         & ":" & " " & '"' & $SESSION_ID 			& '",' 			   & @LF & _
	'"emsUserName"' 	  & ":" & " " & '"' & $EMS_USERNAME 		& '",' 			   & @LF & _
	'"emsUserPassword"'   & ":" & " " & '"' & $EMS_POST_USERS_PSWD 	& '",' 			   & @LF & _
	'"mac"' 			  & ":" & " " & '"' & $macAdress    		& '",' 			   & @LF & _
	'"ip"'	  			  & ":" & " " & '"' & $ipAddress			& '",' 			   & @LF & _
	'"subnet"'		  	  & ":" & " " & '"' & $EMS_SUBNET       	& '",' 			   & @LF & _
	'"region"'		  	  & ":" & " " & '"' & $regionName       	& '",' 			   & @LF & _
	'"model"'		  	  & ":" & " " & '"' & $phoneType    		& '",' 			   & @LF & _
	'"fwVersion"'		  & ":" & " " & '"' & $EMS_VERSION  		& '",' 			   & @LF & _
	'"userAgent"'		  & ":" & " " & '"' & $EMS_USER_AGENT   	& '",' 			   & @LF & _
	'"userName"'		  & ":" & " " & '"' & ""     				& '",' 			   & @LF & _
	'"userId"'		  	  & ":" & " " & '"' & ""       				& '",' 			   & @LF & _
	'"status"'		  	  & ":" & " " & '"' & "started"  	    	& '",' 			   & @LF & _
	'"phoneNumber"' 	  & ":" & " " & '"' & ""  					& '",' 			   & @LF & _
	'"location"' 	  	  & ":" & " " & '"' & $emsLocation  		& '",' 			   & @LF & _
	'"sipProxy"'		  & ":" & " " & '"' & ""     				& '"'  & $extraDot & @LF & _
											  $btoeStatus									 & _
											  $btoeversion									 & _
											  $usbHeadsetType		   			   	   		 & _
											  $hrsSpeakerModel		   			   	   		 & _
											  $hrsSpeakerFW		   			   	   	   		 & _
	"}"
   myToolTip("$PostData0 is:" & @CRLF & $PostData0, 150, 150, $DEBUG_INFUNC_LEVEL)
   myToolTip("$target2 - " & $target2, 150, 150, $DEBUG_INFUNC_LEVEL)
   $Socket = myOpenSocket($IP)
   _HTTPPost($IP, $target2, $Socket, $PostData0)
   $recv = _HTTPRead($Socket,0)
   _HTTPClose($Socket)
   myCountDown(2000)

   ; Stage 1 -  Start create user according to given MAC and region.
   If 	  StringInStr($userName, "sameMac_2_") Then
	  	  myToolTip("sameMac_2_ prefix was detected !", 150, 150, $DEBUG_INFUNC_LEVEL)

   Else
	  myToolTip("Stage 1 -  Start create user according to given MAC and region.", 150, 150, $DEBUG_INFUNC_LEVEL)
	  $target = "http://" & $IP & "/ipp/region/" & $regionName & "/" & $macAdress &  ".cfg"
	  myToolTip("$target - " & $target, 150, 150, $DEBUG_INFUNC_LEVEL)
	  $oHTTP = ObjCreate("winhttp.winhttprequest.5.1")
	  If Not @error Then
		  myToolTip("$oHTTP object was created successfully !!", 150, 150, $DEBUG_INFUNC_LEVEL)

	  Else
		  myCountDown(2000)
	  	  $oHTTP = ObjCreate("winhttp.winhttprequest.5.1")

	  EndIf
	  myCountDown(2000)
	  $oHTTP.Open("POST", $target, False)
	  myCountDown(3000)
	  $oHTTP.SetRequestHeader("User-Agent", $EMS_USER_AGENT)
	  myCountDown(2000)
	  $oHTTP.Send()
	  myCountDown(2000)
	  $oReceived = $oHTTP.ResponseText
	  myCountDown(2000)
	  $oStatusCode = $oHTTP.Status
	  myToolTip("$oStatusCode - " & $oStatusCode, 150, 150, $DEBUG_INFUNC_LEVEL)
	  myCountDown(1000)

   EndIf

   ; Stage 2 -  Create the user + device with all the data
   myToolTip("Stage 2 -  Create the user + device with all the data", 150, 150, $DEBUG_INFUNC_LEVEL)
   $PostData = "{" 															   & 			 @LF & _
	   '"sessionId"'          & ":" & " " & '"' & $SESSION_ID 			& '",' & 			 @LF & _
	   '"emsUserName"' 	      & ":" & " " & '"' & $EMS_USERNAME 		& '",' & 			 @LF & _
	   '"emsUserPassword"'    & ":" & " " & '"' & $EMS_POST_USERS_PSWD 	& '",' & 			 @LF & _
	   '"mac"' 			      & ":" & " " & '"' & $macAdress    		& '",' & 			 @LF & _
	   '"ip"'	  			  & ":" & " " & '"' & $ipAddress			& '",' & 			 @LF & _
	   '"subnet"'		  	  & ":" & " " & '"' & $EMS_SUBNET       	& '",' & 			 @LF & _
	   '"model"'		  	  & ":" & " " & '"' & $phoneType    		& '",' & 			 @LF & _
	   '"fwVersion"'		  & ":" & " " & '"' & $EMS_VERSION  		& '",' & 			 @LF & _
	   '"userAgent"'		  & ":" & " " & '"' & $EMS_USER_AGENT    	& '",' & 			 @LF & _
	   '"userName"'		  	  & ":" & " " & '"' & $userName     		& '",' & 			 @LF & _
	   '"userId"'		  	  & ":" & " " & '"' & $userId       		& '",' &	 		 @LF & _
	   '"status"'		  	  & ":" & " " & '"' & $phoneStatus  		& '",' & 			 @LF & _
	   '"phoneNumber"' 	      & ":" & " " & '"' & $phoneNumber  		& '",' & 			 @LF & _
	   '"location"' 	  	  & ":" & " " & '"' & $emsLocation  		& '",' & 			 @LF & _
	   '"sipProxy"'		  	  & ":" & " " & '"' & $sipProxy     		& '"'  & $extraDot & @LF & _
	   											  $btoeStatus					 			 	 & _
												  $btoeversion					 			 	 & _
												  $usbHeadsetType		   			   		 	 & _
												  $hrsSpeakerModel		   			   		 	 & _
												  $hrsSpeakerFW		   			   		 	 	 & _
	   "}"
   myToolTip("$PostData is:" & @CRLF & $PostData, 150, 150, $DEBUG_INFUNC_LEVEL)
   sendData($target2, $PostData)
   myToolTip("exit createUserViaPost()", 150, 150, $DEBUG_OUTFUNC_LEVEL)

EndFunc
;--------------------------------------------------------------------------------------------------
Func sendData($target2, $PostData, $retryNumber = 3)

	$Socket = myOpenSocket($IP)
	myCountDown(3000)

	For $i = 1 To $retryNumber Step 1

		_HTTPPost($IP, $target2, $Socket, $PostData)
		myCountDown(3000)
		$recv = _HTTPRead($Socket,0)
		myToolTip("$recv - " & $recv, 150, 150, $DEBUG_INFUNC_LEVEL)
		If $recv <> "" Then
			_HTTPClose($Socket)
			myCountDown(3000)
			Return

		EndIf
		_HTTPClose($Socket)
		$Socket = myOpenSocket($IP)
		myCountDown(10000)

	Next

	; After three tries, still no answer is received
	_HTTPClose($Socket)
	myCountDown(3000)
	Exit 1

EndFunc
;--------------------------------------------------------------------------------------------------
Func myOpenSocket($IP, $triesNum = 3)

	For $i = 1 To $triesNum Step 1

		$tempSocket = _HTTPConnect($IP)
		myToolTip("$tempSocket - " & $tempSocket, 150, 150, $DEBUG_INFUNC_LEVEL)
		If $tempSocket <> -1 Then
			Return $tempSocket

		Else
			myCountDown(10000)

		EndIf
	Next
	myToolTip("After three tries, socket is still un-accessable !!", 150, 150, $DEBUG_INFUNC_LEVEL)
	Exit 1

EndFunc
;--------------------------------------------------------------------------------------------------
Func myToolTip($txt, $xPos = 150, $yPos = 150, $level = $DEBUG_TESTER_LEVEL, $delay = 500)

   Local $target = " "
   For $i = 1 To $level Step 1
	  $target = $target & @TAB

   Next
   ConsoleWrite("" & $target &  _NowTime() & "  " & $txt & @TAB & @TAB & @LF)
   ToolTip(@LF & $txt & @LF & "---------------------------------------" , $xPos, $yPos)
   Sleep($delay)

EndFunc
;--------------------------------------------------------------------------------------------------
Func markEnd($sFilePath)

    _FileCreate($sFilePath)
    Local $hFileOpen = FileOpen($sFilePath, $FO_READ + $FO_OVERWRITE)
	Sleep(1000)
    FileWrite($hFileOpen, "--" & @CRLF)
    FileClose($hFileOpen)
    Return True

EndFunc
;--------------------------------------------------------------------------------------------------
Func writeMacToFile($macAddress, $i)

   $sFilePath2 = @WorkingDir & "\" & "mac_" & $i & ".txt"
   myToolTip("$sFilePath2 is " & $sFilePath2, 150, 150, $DEBUG_INFUNC_LEVEL)
   FileDelete($sFilePath2)
   _FileCreate($sFilePath2)
   Local $hFileOpen = FileOpen($sFilePath2, $FO_APPEND)
   FileWrite($hFileOpen, $macAddress)
   FileClose($hFileOpen)

EndFunc
;--------------------------------------------------------------------------------------------------
Func writeIPToFile($ipAddress, $i)

   $sFilePath2 = @WorkingDir & "\" & "ip_" & $i & ".txt"
   myToolTip("$sFilePath2 is " & $sFilePath2, 150, 150, $DEBUG_INFUNC_LEVEL)
   FileDelete($sFilePath2)
   _FileCreate($sFilePath2)
   Local $hFileOpen = FileOpen($sFilePath2, $FO_APPEND)
   FileWrite($hFileOpen, $ipAddress)
   FileClose($hFileOpen)

EndFunc
;--------------------------------------------------------------------------------------------------
Func myRandomString($length = "2", $isDisplay = 0)

   myToolTip("enter myRandomString()", 150, 150, $DEBUG_INFUNC_LEVEL)
	Local $sText = ""
	For $i = 1 To $length
		; Decide if big character, small character or number is needed
		$result = Random(0, 2, 1)
		If $result = 0  Then
			$sText &= Chr(Random(65, 70, 1))

		 ElseIf $result = 1 Then
			$sText &= Chr(Random(97, 102, 1))

		 Else
			$sText &= Chr(Random(48, 57, 1))

		 EndIf

	Next
	myToolTip("The new random string is " & $sText, 150, 150, $DEBUG_INFUNC_LEVEL)
	If $isDisplay = 1 Then
	  MsgBox(0, "", "The random string of text is: " & $sText) ; Display the string of text.
   EndIf
   myToolTip("exit myRandomString()", 150, 150, $DEBUG_OUTFUNC_LEVEL)
   return $sText

EndFunc
;--------------------------------------------------------------------------------------------------
Func getEmsLocation($userName)

   $emsLocation = ""
   If 	  StringInStr($userName, "location_2048") Then
	  myToolTip("Create a location string in 2048 chracters long !!", 150, 150, $DEBUG_INFUNC_LEVEL)
	  $emsLocation = myRandomString(2048)

   ElseIf StringInStr($userName, "location_2049") Then

	  myToolTip("Create a location string in 2049 chracters long !!", 150, 150, $DEBUG_INFUNC_LEVEL)
	  $emsLocation = myRandomString(2049)

   Else
	  myToolTip("Create a default location string - myLocation", 150, 150, $DEBUG_INFUNC_LEVEL)
	  $emsLocation = "myLocation"

   EndIf

   return $emsLocation

EndFunc
;--------------------------------------------------------------------------------------------------
Func getUSBHeadsetType($userName, $phoneNumber)

   $usbHeadsetType = ""
   If 	  StringInStr($userName, "usbHdstTypeuser") Then
	  myToolTip("USB headset type user was detected !! ($userName - " & $userName & ")", 150, 150, $DEBUG_INFUNC_LEVEL)
	  If 	   StringInStr($userName, "usbHdstTypeuserstatus") Then
		 myToolTip("USB headset type status was detected !!", 150, 150, $DEBUG_INFUNC_LEVEL)
		 $usbHeadsetType = $phoneNumber
		 writeIPToFile($usbHeadsetType, "1")

	  ElseIf   StringInStr($userName, "usbHdstTypeuserunknown") Then
		 myToolTip("USB headset type unknown was detected !!", 150, 150, $DEBUG_INFUNC_LEVEL)
		 $usbHeadsetType = "unknown"

	  ElseIf   StringInStr($userName, "usbHdstTypeuserempty") Then
		 myToolTip("USB headset type empty was detected !!", 150, 150, $DEBUG_INFUNC_LEVEL)

	  ElseIf   StringInStr($userName, "usbHdstTypeuserlong") Then
		 myToolTip("USB headset type long status was detected !!", 150, 150, $DEBUG_INFUNC_LEVEL)
		 $usbHeadsetType = myRandomString(130)
		 writeIPToFile($usbHeadsetType, "1")

	  EndIf
	  $extraDot   = ","
	  $usbHeadsetType  = '"USBHeadsetType"' & ":" & " " & '"' & $usbHeadsetType  & '",' & @LF
	  myToolTip("$usbHeadsetType - " & $usbHeadsetType, 150, 150, $DEBUG_INFUNC_LEVEL)

   EndIf

   return $usbHeadsetType

EndFunc
;--------------------------------------------------------------------------------------------------
Func getHrsSpeakerModel($userName, $phoneNumber)

   $hrsSpeakerModel = ""
   If 	  StringInStr($userName, "hrsSpeakerModel") Then
	  myToolTip("HRS Speaker Model user was detected !! ($userName - " & $userName & ")", 150, 150, $DEBUG_INFUNC_LEVEL)
	  If 	   StringInStr($userName, "hrsSpeakerModelStatus") Then
		 myToolTip("HRS Speaker Model status was detected !!", 150, 150, $DEBUG_INFUNC_LEVEL)
		 $hrsSpeakerModel = $phoneNumber
		 writeIPToFile($hrsSpeakerModel, "1")

	  ElseIf   StringInStr($userName, "hrsSpeakerModelLong") Then
		 myToolTip("HRS Speaker Model long status was detected !!", 150, 150, $DEBUG_INFUNC_LEVEL)
		 $hrsSpeakerModel = myRandomString(130)
		 writeIPToFile($hrsSpeakerModel, "1")

	  ElseIf   StringInStr($userName, "hrsSpeakerModelEmpty") Then
		 myToolTip("HRS Speaker Model empty status was detected !!", 150, 150, $DEBUG_INFUNC_LEVEL)
		 $hrsSpeakerModel = ""

	  ElseIf   StringInStr($userName, "hrsSpeakerModelSpec") Then
		 myToolTip("HRS Speaker Model speical characters status was detected !!", 150, 150, $DEBUG_INFUNC_LEVEL)
		 $hrsSpeakerModel = "~!@#$%^&*()+<>,'_"
		 writeIPToFile($hrsSpeakerModel, "1")

	  EndIf
	  $extraDot   = ","
	  $hrsSpeakerModel  = '"HRSSpeakerModel"' & ":" & " " & '"' & $hrsSpeakerModel  & '",' & @LF
	  myToolTip("$hrsSpeakerModel - " & $hrsSpeakerModel, 150, 150, $DEBUG_INFUNC_LEVEL)

   EndIf

   return $hrsSpeakerModel

EndFunc
;--------------------------------------------------------------------------------------------------
Func getHrsSpeakerFW($userName, $phoneNumber)

   $hrsSpeakerFW = ""
   If 	  StringInStr($userName, "hrsSpeakerFw") Then
	  myToolTip("HRS Speaker FW user was detected !! ($userName - " & $userName & ")", 150, 150, $DEBUG_INFUNC_LEVEL)

	  If 	   StringInStr($userName, "hrsSpeakerFwStatus") Then
		 myToolTip("HRS Speaker FW status was detected !!", 150, 150, $DEBUG_INFUNC_LEVEL)
		 $hrsSpeakerFW = $phoneNumber
		 writeIPToFile($hrsSpeakerFW, "1")

	  ElseIf   StringInStr($userName, "hrsSpeakerFwLong") Then
		 myToolTip("HRS Speaker FW long status was detected !!", 150, 150, $DEBUG_INFUNC_LEVEL)
		 $hrsSpeakerFW = myRandomString(130)
		 writeIPToFile($hrsSpeakerFW, "1")

	  ElseIf   StringInStr($userName, "hrsSpeakerFwEmpty") Then
		 myToolTip("HRS Speaker FW empty status was detected !!", 150, 150, $DEBUG_INFUNC_LEVEL)
		 $hrsSpeakerFW = ""

	  ElseIf   StringInStr($userName, "hrsSpeakerFwSpec") Then
		 myToolTip("HRS Speaker FW speical characters status was detected !!", 150, 150, $DEBUG_INFUNC_LEVEL)
		 $hrsSpeakerFW = "~!@#$%^&*()+<>,'_"
		 writeIPToFile($hrsSpeakerFW, "1")

	  EndIf
	  $extraDot   = ","
	  $hrsSpeakerFW  = '"HRSSpeakerFW"' & ":" & " " & '"' & $hrsSpeakerFW  & '",' & @LF
	  myToolTip("$hrsSpeakerFW - " & $hrsSpeakerFW, 150, 150, $DEBUG_INFUNC_LEVEL)

   EndIf

   return $hrsSpeakerFW

EndFunc