; Global source files
#include <Constants.au3>
#include <MsgBoxConstants.au3>
#include <IE.au3>
#include <Array.au3>
#include <Date.au3>
#include <File.au3>
#include <http.au3>

; Set arguments via command line
;~ $IP	 			   = $CmdLine[1]  ; I.e.  10.21.51.12
;~ $PORT	 		   = $CmdLine[2]  ; I.e.  8081
;~ $usersNumber  	   = $CmdLine[3]  ; I.e.  3
;~ $domainName 	   = $CmdLine[4]  ; I.e.  cloudbond365b.com
;~ $createStatus 	   = $CmdLine[5]  ; I.e.  registered
;~ $phoneType 		   = $CmdLine[6]  ; I.e.  430HD
;~ $regionName 	   = $CmdLine[7]  ; I.e.  Nir
;~ $location 		   = $CmdLine[8]  ; I.e.  myLocation
;~ $rateOfCreate 	   = $CmdLine[9]  ; I.e.  10000 (10 secs)

;~ $IP	 			  = "10.21.8.35"
$IP	 			  = "172.17.118.163"
$PORT	 		  = "8081"
$usersNumber  	  = "100"
$domainName 	  = "cloudbond365b.com"
$createStatus 	  = "registered"
$phoneType 		  = "430HD"
$regionName 	  = "Nir"
$location 		  = "myLocation"
$rateOfCreate	  = 1

;~ MsgBox(0, "Arguments", "$IP - " & $IP & @CRLF & "$PORT - " & $PORT & @CRLF & "$usersNumber - " & $usersNumber & @CRLF & "$usersPrefixName - " & $usersPrefixName & @CRLF & "$domainName - " & $domainName & @CRLF & "$createStatus - " & $createStatus & @CRLF & "$phoneType - " & $phoneType & @CRLF & "$regionName - " & $regionName & @CRLF &"$location - " & $location & @CRLF)
; Exit 1


; Gloabal network variables
Global const $EMS_SUBNET 	 = "255.255.255.0"
Global const $EMS_URL		 = "/rest/v1/ipphoneMgrStatus/keep-alive"									; URL for the second POST query that sent when creating a user via POST query
Global Const $HTTP_STATUS_OK = 200

; Gloabal phone variables
Global const $EMS_POST_PASSWORD	     = "3f6d0199102b53ca0a37a527f0efc221"												; Password for users which created via POST query
Global const $EMS_PHONE_FIRM_VERSION = "UC_2.0.13.121"							 										; Version number for users which created via POST query
Global const $EMS_PHONE_USER_AGENT	 = "AUDC-IPPhone-" & $phoneType & "_"  & $EMS_PHONE_FIRM_VERSION & "/1.0.0000.0"	; User-agent for users which created via POST query
Global const $AUDC_MAC_PREFIX		 = "00908f"									 										; General prefix for all Audiocodes given MAC addresses

; Debug variables
Global const $DEBUG_COUNTDOWN_LEVEL	= 5
Global const $DEBUG_SEARCH_LEVEL	= 4
Global const $DEBUG_IN_FUNC_LEVEL	= 3
Global const $DEBUG_FUNCTION_LEVEL	= 2
Global const $DEBUG_TESTER_LEVEL	= 1
Global const $DEBUG_DESC_LEVEL		= 0


; Main ()
$usersPrefixName  = "loadShay_" & StringReplace(_NowTime(), ":", "_")
$sFilePath = @WorkingDir & "\" & "success.txt"
FileDelete($sFilePath)
myToolTip("Activate main()", 150, 150, $DEBUG_DESC_LEVEL)
myToolTip("$usersPrefixName - " & $usersPrefixName, 150, 150, $DEBUG_DESC_LEVEL)
$usersNumber = StringStripWS($usersNumber, $STR_STRIPALL)
$usersNumber 					= Number($usersNumber)
createUsersViaPost($usersNumber, $usersPrefixName, $domainName, $createStatus, $phoneType, $regionName, $location)
markEnd($sFilePath)
myToolTip("Exit main()", 150, 150, $DEBUG_DESC_LEVEL)
;--------------------------------------------------------------------------------------------------
func createUsersViaPost($usersNumber, $usersPrefixName, $domainName, $createStatus, $phoneType, $regionName, $location)

   myToolTip("enter createUsersViaPost()", 150, 150, $DEBUG_DESC_LEVEL)
   For $i = 1 To $usersNumber Step 1

	  ; Set user-name, MAC address, Phone number, IP address
	  $userName     = $usersPrefixName & "_" & $i
	  ConsoleWrite(@LF & @LF)
	  ConsoleWrite("+-------------------------------------------------------+" & @LF)
      ConsoleWrite("|                                                       |" & @LF)
	  ConsoleWrite("|       " &  $userName & "                              |" & @LF)
      ConsoleWrite("|                                                       |" & @LF)
	  ConsoleWrite("+-------------------------------------------------------+" & @LF & @LF)

	  $myMacAddress = myRandomMacAddress()
	  writeMacToFile($myMacAddress, $i)
	  $phoneNumber  = myRandomPhoneNumber()
	  $ipAddress    = "http://172.17.121.13" ;myRandomIp()
	  writeIPToFile($ipAddress, $i)

	  ; Print data to console
	  myToolTip("$userName - "     & $userName    , 150, 150, $DEBUG_IN_FUNC_LEVEL)
	  myToolTip("$myMacAddress - " & $myMacAddress, 150, 150, $DEBUG_IN_FUNC_LEVEL)
	  myToolTip("$phoneNumber - "  & $phoneNumber  , 150, 150, $DEBUG_IN_FUNC_LEVEL)
	  myToolTip("$ipAddress - "    & $ipAddress 	  , 150, 150, $DEBUG_IN_FUNC_LEVEL)
	  createUserViaPost($EMS_POST_PASSWORD, $myMacAddress, $ipAddress, $phoneType, $EMS_PHONE_FIRM_VERSION, $EMS_PHONE_USER_AGENT, $userName,  $userName & "@" & $domainName, $createStatus, $phoneNumber, $domainName, $regionName, $location)
 	  Sleep($rateOfCreate)
   Next
   myToolTip("exit createUsersViaPost()", 150, 150, $DEBUG_DESC_LEVEL)

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
    $sText2 = $AUDC_MAC_PREFIX & $sText
   myToolTip("myRandomMacAddress() MAC address - " & $sText2, 150, 150, $DEBUG_FUNCTION_LEVEL)
   return $sText2

EndFunc
;--------------------------------------------------------------------------------------------------
Func myRandomPhoneNumber($length = "9")

   Local $sText = ""
   For $i = 1 To $length

	  $sText &= Chr(Random(48, 57, 1))
   Next
   myToolTip("myRandomPhoneNumber() Phone-number - " & $sText, 150, 150, $DEBUG_FUNCTION_LEVEL)
   return $sText

EndFunc
;--------------------------------------------------------------------------------------------------
Func myRandomIp()

   Local $sText = ""
   For $i = 1 To 3

	  $sText &= Random(1, 127, 1) & "."
   Next
   $sText &= Random(1, 127, 1)
   myToolTip("myRandomIp() IP - " & $sText, 150, 150, $DEBUG_FUNCTION_LEVEL)
   return $sText

EndFunc
;--------------------------------------------------------------------------------------------------
Func createUserViaPost($userPassword, $macAdress, $ipAddress  , $phoneType  , $phoneVersion, $userAgent , _
					   $userName    ,  $userId	, $phoneStatus, $phoneNumber, $sipProxy    , $regionName, _
					   $location)

   myToolTip("enter createUserViaPost()", 150, 150, $DEBUG_FUNCTION_LEVEL)
   myToolTip("$userName - " & $userName,  150, 150, $DEBUG_IN_FUNC_LEVEL)

   ; Set vars
   $sessionId   = "a033bb54"
   $emsUsername = 'system'
   $target2 = "http://" & $IP & ":" & $PORT & $EMS_URL

   ; Set extra data
   $btoeStatus      = ""
   $btoeversion     = ""
   $extraDot        = ""
   $usbHeadsetType  = ""
   $hrsSpeakerModel = ""
   $hrsSpeakerFW    = ""

   ; Stage 0 -  send packet with primary data to make the system be reasy for the 'create-user' method.
   myToolTip("Stage 0 -  send packet with primary data to make the system be reasy for the 'create-user' method.", 150, 150, $DEBUG_IN_FUNC_LEVEL)
   $PostData0 = "{" 												   			   & @LF & _
	'"sessionId"'         & ":" & " " & '"' & $sessionId 		& '",' 			   & @LF & _
	'"emsUserName"' 	  & ":" & " " & '"' & $emsUsername 		& '",' 			   & @LF & _
	'"emsUserPassword"'   & ":" & " " & '"' & $userPassword 	& '",' 			   & @LF & _
	'"mac"' 			  & ":" & " " & '"' & $macAdress    	& '",' 			   & @LF & _
	'"ip"'	  			  & ":" & " " & '"' & $ipAddress		& '",' 			   & @LF & _
	'"subnet"'		  	  & ":" & " " & '"' & $EMS_SUBNET       & '",' 			   & @LF & _
	'"region"'		  	  & ":" & " " & '"' & $regionName       & '",' 			   & @LF & _
	'"model"'		  	  & ":" & " " & '"' & $phoneType    	& '",' 			   & @LF & _
	'"fwVersion"'		  & ":" & " " & '"' & $phoneVersion  	& '",' 			   & @LF & _
	'"userAgent"'		  & ":" & " " & '"' & $userAgent    	& '",' 			   & @LF & _
	'"userName"'		  & ":" & " " & '"' & ""     			& '",' 			   & @LF & _
	'"userId"'		  	  & ":" & " " & '"' & ""       			& '",' 			   & @LF & _
	'"status"'		  	  & ":" & " " & '"' & "started"  	    & '",' 			   & @LF & _
	'"phoneNumber"' 	  & ":" & " " & '"' & ""  				& '",' 			   & @LF & _
	'"location"' 	  	  & ":" & " " & '"' & $location  		& '",' 			   & @LF & _
	'"sipProxy"'		  & ":" & " " & '"' & ""     			& '"'  & $extraDot & @LF & _
											  $btoeStatus					 	   & _
											  $btoeversion					 	   & _
											  $usbHeadsetType		   			   & _
											  $hrsSpeakerModel		   			   & _
											  $hrsSpeakerFW		   			   	   & _
	"}"
   myToolTip("$PostData0 is:" & @CRLF & $PostData0, 150, 150, $DEBUG_IN_FUNC_LEVEL)
   myToolTip("$target2 - " & $target2			  , 150, 150, $DEBUG_IN_FUNC_LEVEL)
   $Socket = _HTTPConnect($IP)
   _HTTPPost($IP, $target2, $Socket, $PostData0)
   $recv = _HTTPRead($Socket,0)
   _HTTPClose($Socket)
   Sleep(1000)

   ; Stage 1 -  Start create user according to given MAC and region.
   myToolTip("Stage 1 -  Start create user according to given MAC and region.", 150, 150, $DEBUG_IN_FUNC_LEVEL)
   $target = "http://" & $IP & "/ipp/tenant/" & $regionName & "/" & $macAdress &  ".cfg"
   myToolTip("$target - " & $target, 150, 150, $DEBUG_IN_FUNC_LEVEL)
   $oHTTP = ObjCreate("winhttp.winhttprequest.5.1")
   $oHTTP.Open("GET", $target, False)
   $oHTTP.SetRequestHeader("User-Agent", $EMS_PHONE_USER_AGENT)
   $oHTTP.Send()
   $oReceived = $oHTTP.ResponseText
   $oStatusCode = $oHTTP.Status
   myToolTip("$oStatusCode - " & $oStatusCode, 150, 150, $DEBUG_IN_FUNC_LEVEL)
;~    myToolTip("$oReceived - "   & $oReceived  , 150, 150, $DEBUG_IN_FUNC_LEVEL)
   Sleep(1000)


   ; Stage 2 -  Create the user + device with all the data
   myToolTip("Stage 2 -  Create the user + device with all the data", 150, 150, $DEBUG_IN_FUNC_LEVEL)
   $PostData = "{" 														   & 			 @LF & _
	   '"sessionId"'          & ":" & " " & '"' & $sessionId 		& '",' & 			 @LF & _
	   '"emsUserName"' 	      & ":" & " " & '"' & $emsUsername 		& '",' & 			 @LF & _
	   '"emsUserPassword"'    & ":" & " " & '"' & $userPassword 	& '",' & 			 @LF & _
	   '"mac"' 			      & ":" & " " & '"' & $macAdress    	& '",' & 			 @LF & _
	   '"ip"'	  			  & ":" & " " & '"' & $ipAddress		& '",' & 			 @LF & _
	   '"subnet"'		  	  & ":" & " " & '"' & $EMS_SUBNET       & '",' & 			 @LF & _
	   '"model"'		  	  & ":" & " " & '"' & $phoneType    	& '",' & 			 @LF & _
	   '"fwVersion"'		  & ":" & " " & '"' & $phoneVersion  	& '",' & 			 @LF & _
	   '"userAgent"'		  & ":" & " " & '"' & $userAgent    	& '",' & 			 @LF & _
	   '"userName"'		  	  & ":" & " " & '"' & $userName     	& '",' & 			 @LF & _
	   '"userId"'		  	  & ":" & " " & '"' & $userId       	& '",' &	 		 @LF & _
	   '"status"'		  	  & ":" & " " & '"' & $phoneStatus  	& '",' & 			 @LF & _
	   '"phoneNumber"' 	      & ":" & " " & '"' & $phoneNumber  	& '",' & 			 @LF & _
	   '"location"' 	  	  & ":" & " " & '"' & $location  		& '",' & 			 @LF & _
	   '"sipProxy"'		  	  & ":" & " " & '"' & $sipProxy     	& '"'  & $extraDot & @LF & _
	   											  $btoeStatus					 			 & _
												  $btoeversion					 			 & _
												  $usbHeadsetType		   			   		 & _
												  $hrsSpeakerModel		   			   		 & _
												  $hrsSpeakerFW		   			   		 	 & _
	   "}"
   myToolTip("$PostData is:" & @CRLF & $PostData, 150, 150, $DEBUG_IN_FUNC_LEVEL)


;~    HttpPost($target2, $PostData)






   $Socket = _HTTPConnect($IP)
   myToolTip("Value of @error is: "    & @error    , 150, 150, $DEBUG_IN_FUNC_LEVEL)
   myToolTip("Value of @extended is: " & @extended , 150, 150, $DEBUG_IN_FUNC_LEVEL)
   myToolTip("$Socket - " & $Socket, 150, 150, $DEBUG_IN_FUNC_LEVEL)

   $retBytes = _HTTPPost($IP, $target2, $Socket, $PostData)
   myToolTip("Value of @error is: "    & @error    , 150, 150, $DEBUG_IN_FUNC_LEVEL)
   myToolTip("Value of @extended is: " & @extended , 150, 150, $DEBUG_IN_FUNC_LEVEL)
   myToolTip("$retBytes - " & $retBytes, 150, 150, $DEBUG_IN_FUNC_LEVEL)

   $recv = _HTTPRead($Socket,0)
   myToolTip("$recv - " & $recv, 150, 150, $DEBUG_IN_FUNC_LEVEL)
   myToolTip("Value of @error is: "    & @error    , 150, 150, $DEBUG_IN_FUNC_LEVEL)
   myToolTip("Value of @extended is: " & @extended , 150, 150, $DEBUG_IN_FUNC_LEVEL)

;~    if $recv == 0 Then
;~ 	     _HTTPClose($Socket)

;~ 	  Exit 1
;~ 	  myToolTip("1.1 $recv = 0 !!", 150, 150, $DEBUG_IN_FUNC_LEVEL)
;~ 	  myToolTip("1.2 device_create_error - try again", 150, 150, $DEBUG_IN_FUNC_LEVEL)
;~ 	  _HTTPPost($IP, $target2, $Socket, $PostData)
;~ 	  $recv = _HTTPRead($Socket,0)
;~ 	  myToolTip("2. $recv - " & $recv, 150, 150, $DEBUG_IN_FUNC_LEVEL)
;~ 	  _HTTPClose($Socket)
;~ 	  if $recv == 0 Then

;~ 		 myToolTip("2.1 $recv = 0 !!", 150, 150, $DEBUG_IN_FUNC_LEVEL)
;~ 		 Exit 1
;~ 	  EndIf

;~     EndIf
   _HTTPClose($Socket)
   myToolTip("exit createUserViaPost()", 150, 150, $DEBUG_FUNCTION_LEVEL)

EndFunc
;--------------------------------------------------------------------------------------------------
Func myToolTip($txt, $xPos = 150, $yPos = 150, $level = $DEBUG_TESTER_LEVEL, $delay = 500)

   Local $target = " "
   For $i = 1 To $level Step 1

	  $target = $target & @TAB
   Next
   ConsoleWrite("" & $target &  _NowTime() & "  " & $txt & @TAB & @TAB & @LF)
   ToolTip(@LF & $txt & @LF & "---------------------------------------" , $xPos, $yPos)

EndFunc
;--------------------------------------------------------------------------------------------------
Func markEnd($sFilePath)

    _FileCreate($sFilePath)
    Local $hFileOpen = FileOpen($sFilePath, $FO_READ + $FO_OVERWRITE)
    FileWrite($hFileOpen, "--" & @CRLF)
    FileClose($hFileOpen)
    Return True

EndFunc
;--------------------------------------------------------------------------------------------------
Func writeMacToFile($macAddress, $i)

   $sFilePath2 = @WorkingDir & "\" & "mac_" & $i & ".txt"
   FileDelete($sFilePath2)
   _FileCreate($sFilePath2)
   Local $hFileOpen = FileOpen($sFilePath2, $FO_APPEND)
   FileWrite($hFileOpen, $macAddress)
   FileClose($hFileOpen)

EndFunc
;--------------------------------------------------------------------------------------------------
Func writeIPToFile($ipAddress, $i)

   $sFilePath2 = @WorkingDir & "\" & "ip_" & $i & ".txt"
   myToolTip("$sFilePath2 is " & $sFilePath2, 150, 150, $DEBUG_IN_FUNC_LEVEL)
   FileDelete($sFilePath2)
   _FileCreate($sFilePath2)
   Local $hFileOpen = FileOpen($sFilePath2, $FO_APPEND)
   FileWrite($hFileOpen, $ipAddress)
   FileClose($hFileOpen)

EndFunc
;--------------------------------------------------------------------------------------------------
Func HttpPost($sURL, $sData = "")

   Local $oHTTP = ObjCreate("WinHttp.WinHttpRequest.5.1")

   $oHTTP.Open("POST", $sURL, False)
   If (@error) Then Return SetError(1, 0, 0)
;~    $oHTTP.SetRequestHeader("Content-Type", "application/x-www-form-urlencoded")

   $oHTTP.Send($sData)

   $oReceived = $oHTTP.ResponseText
   $oStatusCode = $oHTTP.Status
   myToolTip("$oStatusCode - " & $oStatusCode, 150, 150, $DEBUG_IN_FUNC_LEVEL)
   myToolTip("$oReceived - "   & $oReceived  , 150, 150, $DEBUG_IN_FUNC_LEVEL)


   If (@error) 							 Then Return SetError(2, 0, 0)
   If ($oHTTP.Status <> $HTTP_STATUS_OK) Then Return SetError(3, 0, 0)

   Return SetError(0, 0, $oHTTP.ResponseText)
EndFunc
