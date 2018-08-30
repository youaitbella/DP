@echo off

set /p answer=Alles auf Produktion installieren (J/N)?
if /i "%answer:~,1%" EQU "J" goto install
goto finish
   
:install   
SET /P PASSWORD="Enter AdminPassword: "
echo AS_ADMIN_PASSWORD=%PASSWORD%> password.txt
set JAVA="c:\Program Files\Java\jdk1.8.0_141\bin\java.exe"
set GF_CLI=d:\payara-4.1.2.174\glassfish\lib\client\appserver-cli.jar

rem redeploy to 01
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal01 redeploy --force --keepstate=true --name=DataPortal DataPortalBase\target\DataPortal.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal01 redeploy --force --keepstate=true --name=DataPortalAdmin DataPortalAdmin\target\DataPortalAdmin.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal01 redeploy --force --keepstate=true --name=DataPortalCalc DataPortalCalc\target\DataPortalCalc.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal01 redeploy --force --keepstate=true --name=DataPortalCert DataPortalCert\target\DataPortalCert.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal01 redeploy --force --keepstate=true --name=DataPortalInsurance DataPortalInsurance\target\DataPortalInsurance.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal01 redeploy --force --keepstate=true --name=DataPortalDrg DataPortalDrg\target\DataPortalDrg.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal01 redeploy --force --keepstate=true --name=DataPortalPsy DataPortalPsy\target\DataPortalPsy.war

rem redeploy to 02
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal02 redeploy --force --keepstate=true --name=DataPortal DataPortalBase\target\DataPortal.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal02 redeploy --force --keepstate=true --name=DataPortalAdmin DataPortalAdmin\target\DataPortalAdmin.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal02 redeploy --force --keepstate=true --name=DataPortalCalc DataPortalCalc\target\DataPortalCalc.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal02 redeploy --force --keepstate=true --name=DataPortalCert DataPortalCert\target\DataPortalCert.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal02 redeploy --force --keepstate=true --name=DataPortalInsurance DataPortalInsurance\target\DataPortalInsurance.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal02 redeploy --force --keepstate=true --name=DataPortalDrg DataPortalDrg\target\DataPortalDrg.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal02 redeploy --force --keepstate=true --name=DataPortalPsy DataPortalPsy\target\DataPortalPsy.war

:finish
del password.txt
pause
