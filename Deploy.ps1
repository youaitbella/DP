#Erstellt von lautenti
#
#Vorraussetzung(Lokal)
# - Java JDK
# - Payaraserver ab v4
# - erstellte DP wars
# - Powershell in der neuesten Version (5+)
#
#Anleitung
# Erlauben von Powershellscripten: cmd -> Set-executionpolicy -executionpolicy unrestricted
# Falls gewünscht kann die Ausführung mit Set-ExecutionPolicy -ExecutionPolicy Restricted wieder aktiviert werden
# Script ausführen (Rechtsklick -> Mit Powershell ausführen) und den Aweisungen in der Oberfläche oder in der Konsole folgen!!!
# Viel Spaß
# 

function createPasswordFile($File) {
    if (!(Test-Path $File)) {
        New-Item -path $File -type "file" -value "AS_ADMIN_PASSWORD=Hogatodomain1"
    }
    else {
        Add-Content -path $File -value "AS_ADMIN_PASSWORD=Hogatodomain1"
    }
    Write-Host "Password file created"
}

function deletePasswordFile($File) {
    Remove-Item -Path $File
    Write-Host "Password file deleted"
}

function getJavaInstallPath {
    $JavaDir = [Environment]::GetEnvironmentVariable("DP_JAVA_HOME_DIR", "User")
    return "$JavaDir\bin\java.exe"
}

function getPayaraInstallPath {
    $PayaraDir = [Environment]::GetEnvironmentVariable("DP_PAYARA_HOME_DIR", "User")
    return "$PayaraDir\glassfish\lib\client\appserver-cli.jar"
}

function getDataPortalRoot {
    $DPDir = [Environment]::GetEnvironmentVariable("DP_DP_HOME_DIR", "User")
    return "$DPDir\"
}

function getTargetSystem { 
    [String[]] $AvailibleServers = "Testsystem","Produktion", "vDataportal01","vDataportal02","vDataportal03","vDataportal04"

    return $AvailibleServers | Out-GridView -PassThru -Title "Zielsystem auswählen"
}

function getMode { 
    [String[]] $AvailibleModes = "redeploy","deploy"

    return $AvailibleModes | Out-GridView -PassThru -Title "Modus auswählen"
}

function getApp { 
    [String[]] $AvailibleApps = "Alle", "Base", "Admin", "Calc", "Cert", "Drg", "Insurance", "Psy", "Care"

    return $AvailibleApps | Out-GridView -PassThru -Title "App auswählen"
}

function getAppApplicationLocation($App) {
    if($App -eq "Base") {
        return $DataPortalRoot + "DataPortalBase\target\DataPortal.war"
    } else {
        return $DataPortalRoot + "DataPortal$App\target\DataPortal$App.war"
    }
}

function getAppApplicationName($App) {
    if($App -eq "Base") {
        return "DataPortal"
    } else {
        return "DataPortal$App"
    }
}

function deployAllToServer($Servers) {
    foreach($server in $Servers) {
        foreach($app in $AllApps) {
            $AppName = getAppApplicationName $app
            $AppLocation = getAppApplicationLocation $app

            Write-Host "$Mode $AppName from $AppLocation to $server"
            
            $command = "`"$JavaExe`" -jar `"$PayaraCli`" --user $User --passwordfile `"$PasswordFile`" --host $server $Mode --force --name=$AppName $AppLocation";
            cmd.exe /c $command
        }
    }
}

function deployAppToServer($Servers, $App) {
    foreach($server in $Servers) {
            $AppName = getAppApplicationName $App
            $AppLocation = getAppApplicationLocation $App

            Write-Host "$Mode $AppName from $AppLocation to $server"
            
            $command = "`"$JavaExe`" -jar `"$PayaraCli`" --user $User --passwordfile `"$PasswordFile`" --host $server $Mode --force --name=$AppName $AppLocation";
            cmd.exe /c $command
    }
}

function setJavaHomeDir() {
    if([string]::IsNullOrEmpty([Environment]::GetEnvironmentVariable("DP_JAVA_HOME_DIR", "User"))) {
        Write-Host "Java Home (JDK) Ordner auswählen"
        Add-Type -AssemblyName System.Windows.Forms
        $FolderBrowser = New-Object System.Windows.Forms.FolderBrowserDialog
        [void]$FolderBrowser.ShowDialog()

        [Environment]::SetEnvironmentVariable("DP_JAVA_HOME_DIR", $FolderBrowser.SelectedPath, "User")
    }
}

function setPayaraHomeDir() {
    if([string]::IsNullOrEmpty([Environment]::GetEnvironmentVariable("DP_PAYARA_HOME_DIR", "User"))) {
        Write-Host "Payara Home Ordner auswählen"
        Add-Type -AssemblyName System.Windows.Forms
        $FolderBrowser = New-Object System.Windows.Forms.FolderBrowserDialog
        [void]$FolderBrowser.ShowDialog()

        [Environment]::SetEnvironmentVariable("DP_PAYARA_HOME_DIR", $FolderBrowser.SelectedPath, "User")
    }
}

function setDPHomeDir() {
    if([string]::IsNullOrEmpty([Environment]::GetEnvironmentVariable("DP_DP_HOME_DIR", "User"))) {
        Write-Host "Datenportal Home Ordner auswählen"
        Add-Type -AssemblyName System.Windows.Forms
        $FolderBrowser = New-Object System.Windows.Forms.FolderBrowserDialog
        [void]$FolderBrowser.ShowDialog()

        [Environment]::SetEnvironmentVariable("DP_DP_HOME_DIR", $FolderBrowser.SelectedPath, "User")
        
    }
}

[String[]] $TestServer = "vdataportal03","vdataportal04"
[String[]] $ProdServer = "vDataportal01","vDataportal02"
[String[]] $AllApps = "Base", "Admin", "Calc", "Cert", "Drg", "Insurance", "Psy", "Care"

# Start Programm

setJavaHomeDir
setPayaraHomeDir
setDPHomeDir

$tmpJava = getJavaInstallPath
$tmpPayara = getPayaraInstallPath
$tmpDP = getDataPortalRoot

Write-Host "Using $tmpJava"
Write-Host "Using $tmpPayara"
Write-Host "Using $tmpDP"


$JavaExe = getJavaInstallPath
$PayaraCli = getPayaraInstallPath
$DataPortalRoot = getDataPortalRoot
$Target = getTargetSystem
if([string]::IsNullOrEmpty($Target)) {
    exit
}
$Mode = getMode
if([string]::IsNullOrEmpty($Mode)) {
    exit
}
$App = getApp
if([string]::IsNullOrEmpty($App)) {
    exit
}
$ScriptDir = Split-Path $script:MyInvocation.MyCommand.Path
$PasswordFile = "$ScriptDir\password.txt"
$User = "admin"

$msgBoxInput =  [System.Windows.MessageBox]::Show("$Mode $App auf $Target. Klick nur auf JA wenn du weißt was du machst!!!",'Ganz sicher?','YesNo','Info')

  switch  ($msgBoxInput) {
  'No' {
    exit
    }
  }

createPasswordFile $PasswordFile

if($Target -eq "Testsystem") {
    if($App -eq "Alle") {
        deployAllToServer $TestServer
    } else {
        deployAppToServer $TestServer $App
    }
} elseif($Target -eq "Produktion") {
    if($App -eq "Alle") {
        deployAllToServer $ProdServer
    } else {
        deployAppToServer $ProdServer $App
    }
} else {
    if($App -eq "Alle") {
        deployAllToServer $Target
    } else {
        deployAppToServer $Target $App
    }
}
deletePasswordFile $PasswordFile

[System.Windows.MessageBox]::Show("Das Werk ist vollendet. Du kannst nun wieder arbeiten gehen!!!",'Fertig')

# End Programm