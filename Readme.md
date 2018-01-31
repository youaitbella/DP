# InEK Datenportal

**DataPortal** ist das Elternprojekt für die InEK Datenportale. 
Die InEK Datenportale sind thematisch gegliederte Webanwendungen, die jeweils eine Vielzahl von Funktionen für den jeweiligen Bereich bieten.
Sie nutzen jedoch gemeinsame Ressourcen und Bibliotheken.

[Projektdokumentation](DataPortal/src/site/markdown/DataPortal.md)
**DataPortal** umfasst diese Module:

1. [DataPortal](DataPortalAll/src/site/markdown/DataPortal.md) ist die Hauptanwendung InEK Datenportal. Dies dient der strukturierten Kommunikation mit Krankenhäusern und externem Sachverstand.
2. [Begleitforschung](Begleitforschung/src/site/Begleitforschung.md) ist die Internetbasierte Browseranwendung für die Darstellung der öffentlichen Begleitforschungsdaten.
3. [PortalResources](PortalResources/src/site/PortalResources.md) umfasst gemeinsam genutzt Ressourcen wie CSS für ein gemeinsames Look and Feel.
4. [PortalLib](PortalLib/src/site/PortalLib.md) beinhaltet gemeinsam genutzt Bibliotheken.

Eine Kopie der gemeinsam genutzten Teile wird im Build-Vorgang zusammen mit der jeweiligen Hauptanwendung in eine WAR-Datei (Web ARchive) gepackt.



