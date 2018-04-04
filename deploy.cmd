@echo off
set JAVA="c:\Program Files\Java\jdk1.8.0_141\bin\java.exe"
set GF_CLI=d:\payara-4.1.2.174\glassfish\lib\client\appserver-cli.jar

if %1==base (
%JAVA% -jar %GF_CLI% --user admin --host vdataportal03 deploy --force --keepstate DataPortalBase\target\DataPortal.war
goto end
)
if %1==admin (
	%JAVA% -jar %GF_CLI% --user admin --host vdataportal03 deploy --force --keepstate DataPortalAdmin\target\DataPortalAdmin.war
	goto end
)	
if %1==calc (
	%JAVA% -jar %GF_CLI% --user admin --host vdataportal03 deploy --force --keepstate DataPortalCalc\target\DataPortalCalc.war
	goto end
)
if %1==cert (
	%JAVA% -jar %GF_CLI% --user admin --host vdataportal03 deploy --force --keepstate DataPortalCert\target\DataPortalCert.war
	goto end
)	
if %1==drg (
	%JAVA% -jar %GF_CLI% --user admin --host vdataportal03 deploy --force --keepstate DataPortalDrg\target\DataPortalDrg.war
	goto end
)
if %1==insurance (
	%JAVA% -jar %GF_CLI% --user admin --host vdataportal03 deploy --force --keepstate DataPortalInsurance\target\DataPortalInsurance.war
	goto end
)
if %1==psy (
	%JAVA% -jar %GF_CLI% --user admin --host vdataportal03 deploy --force --keepstate DataPortalPsy\target\DataPortalPsy.war
	goto end
)
echo "usage: deploy base|admin|calc|cert|drg|insurance|psy"
:end