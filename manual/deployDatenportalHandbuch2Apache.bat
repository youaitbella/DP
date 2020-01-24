@echo off

echo Kopiere InEK-Datenportal.pdf auf den Server
scp output/InEK-Datenportal.pdf edv@vDmzApacheSvr01:.
echo.

echo Starte deployment auf dem Server
ssh edv@vDmzApacheSvr01 /srv/scripte/deployDatenportalHandbuch.sh
echo.

echo Fertig
echo.
pause