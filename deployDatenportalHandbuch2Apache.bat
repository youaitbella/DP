@echo off

echo Kopiere InEK-Datenportal.pdf auf den Server
scp InEK-Datenportal.pdf edv@vDmzApacheSvr01:.
scp InEK-DatenportalZert.pdf edv@vDmzApacheSvr01:.
scp InEK-DatenportalIntern.pdf edv@vDmzApacheSvr01:.
echo.

echo Starte deployment auf dem Server
ssh edv@vDmzApacheSvr01 /srv/scripte/deployDatenportalHandbuch.sh
echo.

echo Fertig
echo.

echo Unter folgendem Link kann das Handbuch nun erreicht werden:

echo https://daten.inek.org/InEK-Datenportal.pdf
echo.
pause
