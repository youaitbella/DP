# InEK Datenportal

**DataPortal** ist das Elternprojekt für die InEK Datenportale. 
Die InEK Datenportale sind thematisch gegliederte Webanwendungen, die jeweils eine Vielzahl von Funktionen für den jeweiligen Bereich bieten.
Sie nutzen jedoch gemeinsame Ressourcen und Bibliotheken.

Die grundlegende [Projektdokumentation](src/site/markdown/DataPortal.md) beschreibt die Architektur und das allgemeine Verhalten des Datenportals.
Dies wird ergänzt um Modul-spezifische Dokumente.

**DataPortal** umfasst diese Module:

1. [DataPortalCommon](DataPortalCommon/src/site/markdown/DataPortalCommon.md) umfasst die gemeinsam genutzten Ressourcen und Bibliotheken.
2. [Begleitforschung](Begleitforschung/src/site/Begleitforschung.md) ist die Internetbasierte Browseranwendung für die Darstellung der öffentlichen Begleitforschungsdaten.
3. [PortalResources](PortalResources/src/site/PortalResources.md) umfasst gemeinsam genutzt Ressourcen wie CSS für ein gemeinsames Look and Feel.
4. [PortalLib](PortalLib/src/site/PortalLib.md) beinhaltet gemeinsam genutzt Bibliotheken.

Eine Kopie der gemeinsam genutzten Teile wird im Build-Vorgang zusammen mit der jeweiligen Hauptanwendung in eine WAR-Datei (Web ARchive) gepackt.



