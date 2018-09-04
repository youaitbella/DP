# InEK Datenportal

**DataPortal** ist das Elternprojekt für die InEK Datenportale. 
Die InEK Datenportale sind thematisch gegliederte Webanwendungen, die jeweils eine Vielzahl von Funktionen für den jeweiligen Bereich bieten.
Sie nutzen jedoch gemeinsame Ressourcen und Bibliotheken.

Die allgemeine [Projektdokumentation](src/site/markdown/DataPortal.md) beschreibt Ziele, Architektur und das allgemeine Verhalten des Datenportals.
Dies wird ergänzt um Modul-spezifische Dokumente.

**DataPortal** umfasst diese Module:

1. [AccountService](AccountService/src/site/markdown/AccountService.md) 
3. [DataPortalAPI](DataPortalAPI/src/site/markdown/DataPortalAPI.md) 
2. [DataPortalCommon](DataPortalCommon/src/site/markdown/DataPortalCommon.md) umfasst die gemeinsam genutzten Ressourcen und Bibliotheken.
2. [DataPortalAdmin](DataPortalAdmin/src/site/DataPortalAdmin.md) beinhaltet administrative Funktionen für den System-Administrator und kann nur InEK-intern genutzt werden. Funktionen für den IK-Admin sind hier nicht enthalten.
3. [DataPortalBase](DataPortalBase/src/site/markdown/DataPortalBase.md) 
4. [DataPortalCalc](DataPortalCalt/src/site/markdown/DataPortalCalc.md) 
5. [DataPortalCert](DataPortalCert/src/site/markdown/DataPortalCert.md) 
6. [DataPortalDrg](DataPortalDrg/src/site/markdown/DataPortalDrg.md) 
7. [DataPortalInsurance](DataPortalInsurance/src/site/markdown/DataPortalInsurance.md) 
8. [DataPortalPsy](DataPortalPsy/src/site/markdown/DataPortalPsy.md) 

Eine Kopie der gemeinsam genutzten Teile (DataPortalCommon) wird im Build-Vorgang zusammen mit der jeweiligen Hauptanwendung in eine WAR-Datei (Web ARchive) gepackt.
Hiervon ausgenommen sind die Services, die als eigenständige Einheiten entwickelt und genutzt werden. 
Neben den hier als Modul aufgeführten Services nutzt das Datenportal weitere eigenständige Services, die jedoch nicht ausschließlich für das Datenportal bestimmt sind.
Solche Services werden daher außerhalb der Datanportal-Struktur als eigenständige Projekte gepflegt und verwalten. 
Ein Beispiel für einen solchen Service ist der InekReportServer.






