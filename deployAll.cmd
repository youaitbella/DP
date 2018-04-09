@echo off
SET /P PASSWORD="Enter AdminPassword: "
echo AS_ADMIN_PASSWORD=%PASSWORD%> password.txt
set JAVA="c:\Program Files\Java\jdk1.8.0_141\bin\java.exe"
set GF_CLI=d:\payara-4.1.2.174\glassfish\lib\client\appserver-cli.jar

rem deploy to 03
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal03 deploy --force --keepstate DataPortalBase\target\DataPortal.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal03 deploy --force --keepstate DataPortalAdmin\target\DataPortalAdmin.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal03 deploy --force --keepstate DataPortalCalc\target\DataPortalCalc.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal03 deploy --force --keepstate DataPortalCert\target\DataPortalCert.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal03 deploy --force --keepstate DataPortalInsurance\target\DataPortalInsurance.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal03 deploy --force --keepstate DataPortalDrg\target\DataPortalDrg.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal03 deploy --force --keepstate DataPortalPsy\target\DataPortalPsy.war
goto finish

rem deploy to 04
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal04 deploy --force --keepstate DataPortalBase\target\DataPortal.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal04 deploy --force --keepstate DataPortalAdmin\target\DataPortalAdmin.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal04 deploy --force --keepstate DataPortalCalc\target\DataPortalCalc.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal04 deploy --force --keepstate DataPortalCert\target\DataPortalCert.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal04 deploy --force --keepstate DataPortalInsurance\target\DataPortalInsurance.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal04 deploy --force --keepstate DataPortalDrg\target\DataPortalDrg.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal04 deploy --force --keepstate DataPortalPsy\target\DataPortalPsy.war

:finish
del password.txt
