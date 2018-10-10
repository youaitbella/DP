@echo off
SET /P PASSWORD="Enter AdminPassword: "
echo AS_ADMIN_PASSWORD=%PASSWORD%> password.txt
set JAVA="c:\Program Files\Java\jdk1.8.0_141\bin\java.exe"
set GF_CLI=d:\payara-4.1.2.174\glassfish\lib\client\appserver-cli.jar

FOR /L %%i IN (3,1,4) DO (call :update %%i)
del password.txt
pause
exit

:update

%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal0%1 deploy --name=DataPortal DataPortalBase\target\DataPortal.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal0%1 deploy --name=DataPortalAdmin DataPortalAdmin\target\DataPortalAdmin.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal0%1 deploy --name=DataPortalCalc DataPortalCalc\target\DataPortalCalc.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal0%1 deploy --name=DataPortalCare DataPortalCare\target\DataPortalCare.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal0%1 deploy --name=DataPortalCert DataPortalCert\target\DataPortalCert.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal0%1 deploy --name=DataPortalInsurance DataPortalInsurance\target\DataPortalInsurance.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal0%1 deploy --name=DataPortalDrg DataPortalDrg\target\DataPortalDrg.war
%JAVA% -jar %GF_CLI% --user admin --passwordfile password.txt --host vdataportal0%1 deploy --name=DataPortalPsy DataPortalPsy\target\DataPortalPsy.war