@echo off
set JAVA="c:\Program Files\Java\jdk1.8.0_141\bin\java.exe"
set GF_CLI=d:\payara-4.1.2.174\glassfish\lib\client\appserver-cli.jar
set /P NUMBER="Enter Server number: "
set /P PORTAL_TYPE="Enter PortalType [base|admin|calc|cert|drg|insurance|psy]: "

if %PORTAL_TYPE%==base (
%JAVA% -jar %GF_CLI% --user admin --host vdataportal0%NUMBER% deploy --force --keepstate DataPortalBase\target\DataPortal.war
goto end
)
if %PORTAL_TYPE%==admin (
	%JAVA% -jar %GF_CLI% --user admin --host vdataportal0%NUMBER% deploy --force --keepstate DataPortalAdmin\target\DataPortalAdmin.war
	goto end
)	
if %PORTAL_TYPE%==calc (
	%JAVA% -jar %GF_CLI% --user admin --host vdataportal0%NUMBER% deploy --force --keepstate DataPortalCalc\target\DataPortalCalc.war
	goto end
)
if %PORTAL_TYPE%==cert (
	%JAVA% -jar %GF_CLI% --user admin --host vdataportal0%NUMBER% deploy --force --keepstate DataPortalCert\target\DataPortalCert.war
	goto end
)	
if %PORTAL_TYPE%==drg (
	%JAVA% -jar %GF_CLI% --user admin --host vdataportal0%NUMBER% deploy --force --keepstate DataPortalDrg\target\DataPortalDrg.war
	goto end
)
if %PORTAL_TYPE%==insurance (
	%JAVA% -jar %GF_CLI% --user admin --host vdataportal0%NUMBER% deploy --force --keepstate DataPortalInsurance\target\DataPortalInsurance.war
	goto end
)
if %PORTAL_TYPE%==psy (
	%JAVA% -jar %GF_CLI% --user admin --host vdataportal0%NUMBER% deploy --force --keepstate DataPortalPsy\target\DataPortalPsy.war
	goto end
)
echo "valid portal types: base|admin|calc|cert|drg|insurance|psy"
:end
pause