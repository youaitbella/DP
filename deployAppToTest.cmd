@echo off
set JAVA="c:\Program Files\Java\jdk1.8.0_141\bin\java.exe"
set GF_CLI=d:\payara-4.1.2.174\glassfish\lib\client\appserver-cli.jar
set /P PORTAL_TYPE="Enter PortalType [base|admin|calc|cert|drg|insurance|psy]: "
SET /P PASSWORD="Enter AdminPassword: "
echo AS_ADMIN_PASSWORD=%PASSWORD%> password.txt

FOR /L %%i IN (3,1,4) DO (call :update %%i)

del password.txt
pause
exit

:update
if %PORTAL_TYPE%==base (
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal0%1 deploy --force --keepstate DataPortalBase\target\DataPortal.war
goto end
)
if %PORTAL_TYPE%==admin (
	%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal0%1 deploy --force --keepstate DataPortalAdmin\target\DataPortalAdmin.war
	goto end
)	
if %PORTAL_TYPE%==calc (
	%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal0%1 deploy --force --keepstate DataPortalCalc\target\DataPortalCalc.war
	goto end
)
if %PORTAL_TYPE%==cert (
	%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal0%1 deploy --force --keepstate DataPortalCert\target\DataPortalCert.war
	goto end
)	
if %PORTAL_TYPE%==drg (
	%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal0%1 deploy --force --keepstate DataPortalDrg\target\DataPortalDrg.war
	goto end
)
if %PORTAL_TYPE%==insurance (
	%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal0%1 deploy --force --keepstate DataPortalInsurance\target\DataPortalInsurance.war
	goto end
)
if %PORTAL_TYPE%==psy (
	%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal0%1 deploy --force --keepstate DataPortalPsy\target\DataPortalPsy.war
	goto end
)
echo "valid portal types: base|admin|calc|cert|drg|insurance|psy"

:end
exit /b
