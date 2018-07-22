

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
$usersNumber  	  	= $CmdLine[3]  ; I.e.  3
$usersPrefixName    = $CmdLine[4]  ; I.e.  mySahiUser
$domainName 		= $CmdLine[5]  ; I.e.  onebox3.com
$createStatus 		= $CmdLine[6]  ; I.e.  registered
$phoneType 		 	= $CmdLine[7]  ; I.e.  430HD
$regionName 		= $CmdLine[8]  ; I.e.  Region_1
$location 		 	= $CmdLine[9]  ; I.e.  myLocation

;~ $IP	 			  = "10.21.8.32"
;~ $PORT	 		  = "8081"
;~ $usersNumber  	  = "1"
;~ $usersPrefixName  = "usbHdstType_user_status_1"
;~ $domainName 	  = "cloudbond365b.com"
;~ $createStatus 	  = "registered"
;~ $phoneType 		  = "430HD"
;~ $regionName 	  = "Nir"
;~ $location 		  = "myLocation"


;~ Exit 1
$usersNumber = StringStripWS($usersNumber, $STR_STRIPALL)
;~ MsgBox(0, "Arguments", "$IP - " & $IP & @CRLF & "$PORT - " & $PORT & @CRLF & "$usersNumber - " & $usersNumber & @CRLF & "$usersPrefixName - " & $usersPrefixName & @CRLF & "$domainName - " & $domainName & @CRLF & "$createStatus - " & $createStatus & @CRLF & "$phoneType - " & $phoneType & @CRLF & "$regionName - " & $regionName & @CRLF &"$location - " & $location & @CRLF)

; POST variables
Global $emsSubnet 				 = "255.255.255.0"
Global $emsPostUsersPassword	 = "3f6d0199102b53ca0a37a527f0efc221"										; Password for users which created via POST query
Global $emsPostVersion			 = "UC_2.0.13.121"							 								; Version number for users which created via POST query
Global $emsPostUserAgent		 = "AUDC-IPPhone-" & $phoneType & "_"  & $emsPostVersion & "/1.0.0000.0"	; User-agent for users which created via POST query
Global $emsPostUrl			 	 = "/rest/v1/ipphoneMgrStatus/keep-alive"									; URL for the second POST query that sent when creating a user via POST query
Global $audcMacPrefix			 = "00908f"									 								; General prefix for all Audiocodes given MAC addresses
Global $countDownLevel 		    = 5
Global $searchLevel			    = 4
Global $innerFunctionLevel	    = 3
Global $functionLevel		    = 2
Global $testerLevel			    = 1
Global $testDescLevel		    = 0
$usersNumber 					= Number($usersNumber)
$sFilePath = @WorkingDir & "\" & "success.txt"
FileDelete($sFilePath)
myToolTip("Activate main()", 150, 150, $testDescLevel)
myToolTip("$usersPrefixName - " & $usersPrefixName, 150, 150, $testDescLevel)

; Take BToE version from 'Location' field.
if StringInStr($usersPrefixName, "BToE_user_version") Then
   myToolTip("Take BToE version from 'Location' field", 150, 150, $innerFunctionLevel)
   $emsPostVersion = $location
   $location 	   = "mylocation"

EndIf

; Change the version field.
if StringInStr($usersPrefixName, "dvFilter2") Then
   myToolTip("Change the regular Version field", 150, 150, $innerFunctionLevel)
   $emsPostVersion = "UC_3.1.0.478"

EndIf

createUsersViaPost($usersNumber, $usersPrefixName, $domainName, $createStatus, $phoneType, $regionName, $location)
myCountDown(10000)
markEnd($sFilePath)
myToolTip("Exit main()", 150, 150, $testDescLevel)

;--------------------------------------------------------------------------------------------------
Func myCountDown($target, $txt = "")

   For $i = 1000 To $target Step 1000
	  Sleep(1000)
	  ToolTip($txt & "(" & $i & "/" & $target, 150, 150)

   Next

EndFunc
;--------------------------------------------------------------------------------------------------
func createUsersViaPost($usersNumber, $usersPrefixName, $domainName, $createStatus, $phoneType, $regionName, $location)

   myToolTip("enter createUsersViaPost()", 150, 150, $testerLevel)
   For $i = 1 To $usersNumber Step 1

	  ; Set user-name
	  $userName = ""
	  If $usersNumber <> 1 Then
		 $userName     = $usersPrefixName & "_" & $i

	  Else
		 $userName     = $usersPrefixName

	  EndIf

	  ; Set MAC address
	  $myMacAddress = ""
	  If StringInStr($usersPrefixName, "sameMac") Or StringInStr($usersPrefixName, "chngStts") Then
		 $myMacAddress = $audcMacPrefix & "123456"
		 myToolTip("Constant MAC address - " & $myMacAddress, 150, 150, $innerFunctionLevel)

	  Else
		 $myMacAddress = myRandomMacAddress()
		 myToolTip("Random MAC address - " & $myMacAddress, 150, 150, $innerFunctionLevel)

	  EndIf
	  writeMacToFile($myMacAddress, $i)
	  $phoneNumber  = myRandomPhoneNumber()
	  $ipAddress    = myRandomIp()
	  writeIPToFile($ipAddress, $i)
	  myToolTip("$userName - " & $userName, 150, 150, $functionLevel)
	  createUserViaPost($emsPostUsersPassword, $myMacAddress, $ipAddress, $phoneType, $emsPostVersion, $emsPostUserAgent, $userName,  $userName & "@" & $domainName, $createStatus, $phoneNumber, $domainName, $regionName, $location)
	  myCountDown(10000,  "Create user " & $userName & " ended !!")

   Next
   myToolTip("exit createUsersViaPost()", 150, 150, $testerLevel)

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
    $sText2 = $audcMacPrefix & $sText
   myToolTip("myRandomMacAddress() mac - " & $sText2, 150, 150, $functionLevel)
   return $sText2

EndFunc
;--------------------------------------------------------------------------------------------------
Func myRandomPhoneNumber($length = "9")

   Local $sText = ""
   For $i = 1 To $length
		 $sText &= Chr(Random(48, 57, 1))

   Next
   myToolTip("myRandomPhoneNumber() $sText - " & $sText, 150, 150, $functionLevel)
   return $sText

EndFunc
;--------------------------------------------------------------------------------------------------
Func myRandomIp()

   Local $sText = ""
   For $i = 1 To 3
	  $sText &= Random(1, 127, 1) & "."

   Next
   $sText &= Random(1, 127, 1)
   myToolTip("myRandomPhoneNumber() $sText - " & $sText, 150, 150, $functionLevel)
   return $sText

EndFunc
;--------------------------------------------------------------------------------------------------
Func createUserViaPost($userPassword, $macAdress, $ipAddress  , $phoneType  , $phoneVersion, $userAgent , _
					   $userName    ,  $userId	, $phoneStatus, $phoneNumber, $sipProxy    , $regionName, _
					   $location)

   myToolTip("enter createUserViaPost()", 150, 150, $functionLevel)
   myToolTip("$userName - " & $userName, 150, 150, $innerFunctionLevel)

   ; Set vars
   $sessionId   = "a033bb54"
   $emsUsername = 'system'
   $emsSubnet   = "255.255.255.0"
   $target2 = "http://" & $IP & ":" & $PORT & $emsPostUrl

   ; Add BtOe status and version
   $btoeStatus  = ""
   $btoeversion = ""
   $extraDot    = ""
   If 	  StringInStr($userName, "BToE_user") Then
	  myToolTip("BToE user was detected !! ($userName - " & $userName & ")", 150, 150, $innerFunctionLevel)
	  $btoeStatusStr = ""
	  If 	   StringInStr($userName, "BToE_user_dis")  Or StringInStr($userName, "BToE_user_version_dis") Then
		 myToolTip("BToE disabled was detected !!", 150, 150, $innerFunctionLevel)
		 $btoeStatusStr = "BToE disabled"

	  ElseIf   StringInStr($userName, "BToE_user_auto") Or StringInStr($userName, "BToE_user_version_auto") Then
		 myToolTip("BToE auto was detected !!", 150, 150, $innerFunctionLevel)
		 $btoeStatusStr = "auto paired"

	  ElseIf   StringInStr($userName, "BToE_user_man")  Or StringInStr($userName, "BToE_user_version_man") Then
		 myToolTip("BToE manual was detected !!", 150, 150, $innerFunctionLevel)
		 $btoeStatusStr = "manual paired"

	  ElseIf   StringInStr($userName, "BToE_user_not")  Or StringInStr($userName, "BToE_user_version_not") Then
		 myToolTip("BToE not-paired was detected !!", 150, 150, $innerFunctionLevel)
		 $btoeStatusStr = "not paired"

	  EndIf
	  $extraDot    = ","
	  $btoeStatus  = '"BToEpairingstatus"' & ":" & " " & '"' & $btoeStatusStr  & '",' & @LF
	  $btoeversion = '"BToEversion"' 	   & ":" & " " & '"' & $emsPostVersion & '"' & @LF
	  myToolTip("$btoeStatus - " & $btoeStatus, 150, 150, $innerFunctionLevel)
	  myToolTip("$btoeversion - " & $btoeversion, 150, 150, $innerFunctionLevel)

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
   myToolTip("Stage 0 -  send packet with primary data to make the system be reasy for the 'create-user' method.", 150, 150, $innerFunctionLevel)
   $PostData0 = "{" 												   			   & @LF & _
	'"sessionId"'         & ":" & " " & '"' & $sessionId 		& '",' 			   & @LF & _
	'"emsUserName"' 	  & ":" & " " & '"' & $emsUsername 		& '",' 			   & @LF & _
	'"emsUserPassword"'   & ":" & " " & '"' & $userPassword 	& '",' 			   & @LF & _
	'"mac"' 			  & ":" & " " & '"' & $macAdress    	& '",' 			   & @LF & _
	'"ip"'	  			  & ":" & " " & '"' & $ipAddress		& '",' 			   & @LF & _
	'"subnet"'		  	  & ":" & " " & '"' & $emsSubnet       	& '",' 			   & @LF & _
	'"region"'		  	  & ":" & " " & '"' & $regionName       & '",' 			   & @LF & _
	'"model"'		  	  & ":" & " " & '"' & $phoneType    	& '",' 			   & @LF & _
	'"fwVersion"'		  & ":" & " " & '"' & $phoneVersion  	& '",' 			   & @LF & _
	'"userAgent"'		  & ":" & " " & '"' & $userAgent    	& '",' 			   & @LF & _
	'"userName"'		  & ":" & " " & '"' & ""     			& '",' 			   & @LF & _
	'"userId"'		  	  & ":" & " " & '"' & ""       			& '",' 			   & @LF & _
	'"status"'		  	  & ":" & " " & '"' & "started"  	    & '",' 			   & @LF & _
	'"phoneNumber"' 	  & ":" & " " & '"' & ""  				& '",' 			   & @LF & _
	'"location"' 	  	  & ":" & " " & '"' & $emsLocation  	& '",' 			   & @LF & _
	'"sipProxy"'		  & ":" & " " & '"' & ""     			& '"'  & $extraDot & @LF & _
											  $btoeStatus					 	   & _
											  $btoeversion					 	   & _
											  $usbHeadsetType		   			   & _
											  $hrsSpeakerModel		   			   & _
											  $hrsSpeakerFW		   			   	   & _
	"}"
   myToolTip("$PostData0 is:" & @CRLF & $PostData0, 150, 150, $innerFunctionLevel)
   myToolTip("$target2 - " & $target2, 150, 150, $innerFunctionLevel)
   $Socket = _HTTPConnect($IP)
   _HTTPPost($IP, $target2, $Socket, $PostData0)
   $recv = _HTTPRead($Socket,0)
   _HTTPClose($Socket)
   myCountDown(10000)

   ; Stage 1 -  Start create user according to given MAC and region.
   If 	  StringInStr($userName, "sameMac_2_") Then
	  	  myToolTip("sameMac_2_ prefix was detected !", 150, 150, $innerFunctionLevel)

   Else
	  myToolTip("Stage 1 -  Start create user according to given MAC and region.", 150, 150, $innerFunctionLevel)
	  $target = "http://" & $IP & "/ipp/region/" & $regionName & "/" & $macAdress &  ".cfg"
	  myToolTip("$target - " & $target, 150, 150, $innerFunctionLevel)
	  $oHTTP = ObjCreate("winhttp.winhttprequest.5.1")
	  $oHTTP.Open("POST", $target, False)
	  $oHTTP.SetRequestHeader("User-Agent", $emsPostUserAgent)
	  $oHTTP.Send()
	  myCountDown(5000)
	  $oReceived = $oHTTP.ResponseText
	  myCountDown(2000)
	  $oStatusCode = $oHTTP.Status
	  myToolTip("$oStatusCode - " & $oStatusCode, 150, 150, $innerFunctionLevel)
	  myCountDown(5000)
	  myToolTip("$oReceived - " & $oReceived, 150, 150, $innerFunctionLevel)
	  myCountDown(10000)

   EndIf

   ; Stage 2 -  Create the user + device with all the data
   myToolTip("Stage 2 -  Create the user + device with all the data", 150, 150, $innerFunctionLevel)
   $PostData = "{" 														   & 			 @LF & _
	   '"sessionId"'          & ":" & " " & '"' & $sessionId 		& '",' & 			 @LF & _
	   '"emsUserName"' 	      & ":" & " " & '"' & $emsUsername 		& '",' & 			 @LF & _
	   '"emsUserPassword"'    & ":" & " " & '"' & $userPassword 	& '",' & 			 @LF & _
	   '"mac"' 			      & ":" & " " & '"' & $macAdress    	& '",' & 			 @LF & _
	   '"ip"'	  			  & ":" & " " & '"' & $ipAddress		& '",' & 			 @LF & _
	   '"subnet"'		  	  & ":" & " " & '"' & $emsSubnet       	& '",' & 			 @LF & _
	   '"model"'		  	  & ":" & " " & '"' & $phoneType    	& '",' & 			 @LF & _
	   '"fwVersion"'		  & ":" & " " & '"' & $phoneVersion  	& '",' & 			 @LF & _
	   '"userAgent"'		  & ":" & " " & '"' & $userAgent    	& '",' & 			 @LF & _
	   '"userName"'		  	  & ":" & " " & '"' & $userName     	& '",' & 			 @LF & _
	   '"userId"'		  	  & ":" & " " & '"' & $userId       	& '",' &	 		 @LF & _
	   '"status"'		  	  & ":" & " " & '"' & $phoneStatus  	& '",' & 			 @LF & _
	   '"phoneNumber"' 	      & ":" & " " & '"' & $phoneNumber  	& '",' & 			 @LF & _
	   '"location"' 	  	  & ":" & " " & '"' & $emsLocation  	& '",' & 			 @LF & _
	   '"sipProxy"'		  	  & ":" & " " & '"' & $sipProxy     	& '"'  & $extraDot & @LF & _
	   											  $btoeStatus					 			 & _
												  $btoeversion					 			 & _
												  $usbHeadsetType		   			   		 & _
												  $hrsSpeakerModel		   			   		 & _
												  $hrsSpeakerFW		   			   		 	 & _
	   "}"
   myToolTip("$PostData is:" & @CRLF & $PostData, 150, 150, $innerFunctionLevel)
   $Socket = _HTTPConnect($IP)
   _HTTPPost($IP, $target2, $Socket, $PostData)
   myCountDown(10000)
   $recv = _HTTPRead($Socket,0)
   myToolTip("$recv - " & $recv, 150, 150, $innerFunctionLevel)
   if $recv == 0 Then
	  myToolTip("$recv = 0 !!", 150, 150, $innerFunctionLevel)
	  _HTTPClose($Socket)
	  myToolTip("device_create_error", 150, 150, $innerFunctionLevel)
	  Exit 1

   EndIf
   _HTTPClose($Socket)
   myCountDown(5000)
   myToolTip("exit createUserViaPost()", 150, 150, $functionLevel)

EndFunc
;--------------------------------------------------------------------------------------------------
Func myToolTip($txt, $xPos = 150, $yPos = 150, $level = $testerLevel, $delay = 500)

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
	Sleep(2000)
    Local $hFileOpen = FileOpen($sFilePath, $FO_READ + $FO_OVERWRITE)
	Sleep(2000)
    FileWrite($hFileOpen, "--" & @CRLF)
	Sleep(2000)
    FileClose($hFileOpen)
	Sleep(2000)
    Return True

EndFunc
;--------------------------------------------------------------------------------------------------
Func writeMacToFile($macAddress, $i)

   $sFilePath2 = @WorkingDir & "\" & "mac_" & $i & ".txt"
   myToolTip("$sFilePath2 is " & $sFilePath2, 150, 150, $innerFunctionLevel)
   FileDelete($sFilePath2)
   Sleep(4000)
   _FileCreate($sFilePath2)
   Sleep(2000)
   Local $hFileOpen = FileOpen($sFilePath2, $FO_APPEND)
   FileWrite($hFileOpen, $macAddress)
   FileClose($hFileOpen)

EndFunc
;--------------------------------------------------------------------------------------------------
Func writeIPToFile($ipAddress, $i)

   $sFilePath2 = @WorkingDir & "\" & "ip_" & $i & ".txt"
   myToolTip("$sFilePath2 is " & $sFilePath2, 150, 150, $innerFunctionLevel)
   FileDelete($sFilePath2)
   Sleep(4000)
   _FileCreate($sFilePath2)
   Sleep(2000)
   Local $hFileOpen = FileOpen($sFilePath2, $FO_APPEND)
   FileWrite($hFileOpen, $ipAddress)
   FileClose($hFileOpen)

EndFunc
;--------------------------------------------------------------------------------------------------
Func myRandomString($length = "2", $isDisplay = 0)

   myToolTip("enter myRandomString()", 150, 150, $innerFunctionLevel)
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
	myToolTip("The new random string is " & $sText, 150, 150, $innerFunctionLevel)
	If $isDisplay = 1 Then
	  MsgBox(0, "", "The random string of text is: " & $sText) ; Display the string of text.
   EndIf
   myToolTip("exit myRandomString()", 150, 150, $functionLevel)
   return $sText

EndFunc
;--------------------------------------------------------------------------------------------------
Func getEmsLocation($userName)

   $emsLocation = ""
   If 	  StringInStr($userName, "location_2048") Then
	  myToolTip("Create a location string in 2048 chracters long !!", 150, 150, $innerFunctionLevel)
	  $emsLocation = myRandomString(2048)

   ElseIf StringInStr($userName, "location_2049") Then

	  myToolTip("Create a location string in 2049 chracters long !!", 150, 150, $innerFunctionLevel)
	  $emsLocation = myRandomString(2049)

   Else
	  myToolTip("Create a default location string - myLocation", 150, 150, $innerFunctionLevel)
	  $emsLocation = "myLocation"

   EndIf

   return $emsLocation

EndFunc
;--------------------------------------------------------------------------------------------------
Func getUSBHeadsetType($userName, $phoneNumber)

   $usbHeadsetType = ""
   If 	  StringInStr($userName, "usbHdstTypeuser") Then
	  myToolTip("USB headset type user was detected !! ($userName - " & $userName & ")", 150, 150, $innerFunctionLevel)
	  If 	   StringInStr($userName, "usbHdstTypeuserstatus") Then
		 myToolTip("USB headset type status was detected !!", 150, 150, $innerFunctionLevel)
		 $usbHeadsetType = $phoneNumber
		 writeIPToFile($usbHeadsetType, "1")

	  ElseIf   StringInStr($userName, "usbHdstTypeuserunknown") Then
		 myToolTip("USB headset type unknown was detected !!", 150, 150, $innerFunctionLevel)
		 $usbHeadsetType = "unknown"

	  ElseIf   StringInStr($userName, "usbHdstTypeuserempty") Then
		 myToolTip("USB headset type empty was detected !!", 150, 150, $innerFunctionLevel)

	  ElseIf   StringInStr($userName, "usbHdstTypeuserlong") Then
		 myToolTip("USB headset type long status was detected !!", 150, 150, $innerFunctionLevel)
		 $usbHeadsetType = myRandomString(130)
		 writeIPToFile($usbHeadsetType, "1")

	  EndIf
	  $extraDot   = ","
	  $usbHeadsetType  = '"USBHeadsetType"' & ":" & " " & '"' & $usbHeadsetType  & '",' & @LF
	  myToolTip("$usbHeadsetType - " & $usbHeadsetType, 150, 150, $innerFunctionLevel)

   EndIf

   return $usbHeadsetType

EndFunc
;--------------------------------------------------------------------------------------------------
Func getHrsSpeakerModel($userName, $phoneNumber)

   $hrsSpeakerModel = ""
   If 	  StringInStr($userName, "hrsSpeakerModel") Then
	  myToolTip("HRS Speaker Model user was detected !! ($userName - " & $userName & ")", 150, 150, $innerFunctionLevel)
	  If 	   StringInStr($userName, "hrsSpeakerModelStatus") Then
		 myToolTip("HRS Speaker Model status was detected !!", 150, 150, $innerFunctionLevel)
		 $hrsSpeakerModel = $phoneNumber
		 writeIPToFile($hrsSpeakerModel, "1")

	  ElseIf   StringInStr($userName, "hrsSpeakerModelLong") Then
		 myToolTip("HRS Speaker Model long status was detected !!", 150, 150, $innerFunctionLevel)
		 $hrsSpeakerModel = myRandomString(130)
		 writeIPToFile($hrsSpeakerModel, "1")

	  ElseIf   StringInStr($userName, "hrsSpeakerModelEmpty") Then
		 myToolTip("HRS Speaker Model empty status was detected !!", 150, 150, $innerFunctionLevel)
		 $hrsSpeakerModel = ""

	  ElseIf   StringInStr($userName, "hrsSpeakerModelSpec") Then
		 myToolTip("HRS Speaker Model speical characters status was detected !!", 150, 150, $innerFunctionLevel)
		 $hrsSpeakerModel = "~!@#$%^&*()+<>,'_"
		 writeIPToFile($hrsSpeakerModel, "1")

	  EndIf
	  $extraDot   = ","
	  $hrsSpeakerModel  = '"HRSSpeakerModel"' & ":" & " " & '"' & $hrsSpeakerModel  & '",' & @LF
	  myToolTip("$hrsSpeakerModel - " & $hrsSpeakerModel, 150, 150, $innerFunctionLevel)

   EndIf

   return $hrsSpeakerModel

EndFunc
;--------------------------------------------------------------------------------------------------
Func getHrsSpeakerFW($userName, $phoneNumber)

   $hrsSpeakerFW = ""
   If 	  StringInStr($userName, "hrsSpeakerFw") Then
	  myToolTip("HRS Speaker FW user was detected !! ($userName - " & $userName & ")", 150, 150, $innerFunctionLevel)

	  If 	   StringInStr($userName, "hrsSpeakerFwStatus") Then
		 myToolTip("HRS Speaker FW status was detected !!", 150, 150, $innerFunctionLevel)
		 $hrsSpeakerFW = $phoneNumber
		 writeIPToFile($hrsSpeakerFW, "1")

	  ElseIf   StringInStr($userName, "hrsSpeakerFwLong") Then
		 myToolTip("HRS Speaker FW long status was detected !!", 150, 150, $innerFunctionLevel)
		 $hrsSpeakerFW = myRandomString(130)
		 writeIPToFile($hrsSpeakerFW, "1")

	  ElseIf   StringInStr($userName, "hrsSpeakerFwEmpty") Then
		 myToolTip("HRS Speaker FW empty status was detected !!", 150, 150, $innerFunctionLevel)
		 $hrsSpeakerFW = ""

	  ElseIf   StringInStr($userName, "hrsSpeakerFwSpec") Then
		 myToolTip("HRS Speaker FW speical characters status was detected !!", 150, 150, $innerFunctionLevel)
		 $hrsSpeakerFW = "~!@#$%^&*()+<>,'_"
		 writeIPToFile($hrsSpeakerFW, "1")

	  EndIf
	  $extraDot   = ","
	  $hrsSpeakerFW  = '"HRSSpeakerFW"' & ":" & " " & '"' & $hrsSpeakerFW  & '",' & @LF
	  myToolTip("$hrsSpeakerFW - " & $hrsSpeakerFW, 150, 150, $innerFunctionLevel)

   EndIf

   return $hrsSpeakerFW

EndFunc