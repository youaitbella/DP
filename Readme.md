# InEK Datenportal

_DataPortalParent_ ist das Elternprojekt für das InEK Datenportal sowie den Begleitforaschungsbrowser. 
Beide Projekte können unabhängig voneinander ausgerollt und betrieben werden.
Sie nutzen jedoch gemeinsame Ressourcen und Bibliotheken.

_dataPortalParent_ umfasst vier Module:

1. [DataPortal](DataPortal/src/site/markdown/DataPortal.md) ist die Hauptanwendung InEK Datenportal. Dies dient der strukturierten Kommunikation mit Krankenhäusern und externem Sachverstand.
2. [Begleitforschung](Begleitforschung/Readme.md] ist die Internetbasierte Browseranwendung für die Darstellung der öffentlichen Begleitforschungsdaten.
3. [PortalResources](PortalResources/Readme.md) umfasst gemeinsam genutzt Ressourcen wie CSS für ein gemeinsames Look and Feel.
4. [PortalLib](PortalLib/Readme.md) beinhaltet gemeinsam genutzt Bibliotheken.

Eine Kopie der gemeinsam genutzten Teile wird im Build-Vorgang zusammen mit der jeweiligen Hauptanwendung in eine WAR-Datei (Web ARchive) gepackt.



