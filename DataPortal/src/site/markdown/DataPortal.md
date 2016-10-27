# InEK Datenportal

DataPortal ein das Modul von [DataPortalParent](../../../../Readme.md). 
Es handelt sich um die Hauptanwendung zur strukturierten Kommunikation mit Krankenhäusern und sonstigen externen Instituten oder Personen.

Zum Zeitpunkt des Projektstart (2011) erhält das InEK zahlreiche Datenlieferungen in unterschiedlichen Formaten per Mail. 
Das Verfahren ist fehleranfällig (Nicht-Einhaltung des Formats, verlorene Emails etc.) und soll daher durch ein Web-Portal ersetzt werden. 
Dieses Dokument beschreibt das Projekt „Inek Datenportal“, welches beginnend in 2012 zur Übermittlung der Daten gem. §21 KHEntgG genutzt wird. 
Weitere Lieferfunktionen wie NUB, Vorschlagsverfahren und mehr werden sukzessive in diesem Portal implementiert.

[Kalkulationshaus](CalculationHospital.md)



## 1 Glossar

Beschreibung fachlich relevanter Begriffe. Projektspezifisch und / oder Verweis auf übergreifendes Glossar. 

|Begriff|Erläuterung|
|---|---|
|DRG|Diagnosis Related Group. Eine Krankheitsgruppe, welche durch Diagnosen, aber auch Prozeduren und sonstigen klinischen oder demographischen Merkmalen beschrieben wird. ...
Die Zuordnung eines Falls zu einer DRG erfolgt durch einen Grouper|
|---|---|
|DropBox|Benannter und zeitlich beschränkter privater Upload-Bereich, ... den ein Anwender für sich reservieren und zur Datenübermittlung an das InEK nutzen kann|
|---|---|
|Selbstverwaltungspartner im Gesundheitswesen|Deutsche Krankenhausgesellschaft, Spitzenverbände der Krankenkassen und der Verband der privaten Krankenversicherung|
|---|---|

## 2 Einführung und Zielsetzung

### 2.1 Projektvision / Aufgabenstellung

Wozu soll diese Software eingesetzt werden? 
Kurze Beschreibung funktionaler Anforderungen.

### 2.2 Qualitätsziele

Kurze Beschreibung nicht funktionaler Anforderungen.

### 2.3 Beteiligte

Nennung der wichtigen Rollen (nicht Personen) und deren Interessen.

|Rolle|Interessen|
|---|---|
|Anwender||
|Gesetzgeber||
|Selbstverwaltung||
|InEK||
|...||

## 3 Anforderungen

Die Anforderungen können je nach Umfang und Zielsetzung (z.B. Besprechung mit Anwendern) auch in einem getrennten Dokument gehalten werden.

### 3.1 Use cases

Beschreibung typischer Einsatzszenarien, z.B. als UML Use Case oder in tabellarischer Form

|Use Case|<Anwendungsfallname>|
|---|---|
|System|z.B. xGrouper|
|---|---|
|Ebene|Anwenderziel|
|---|---|
|Primärakteur||
|---|---|
|Stakeholder und Interessen||
|---|---|
|Vorbedingungen||
|---|---|
|Nachbedingungen||
|---|---|
|Standardablauf||
|1.|...|
|2.|...|
|3.|...|
|---|---|
|Erweiterungen / alternative Abläufe||
|*.a|...|
|---|---|
|Spezielle Anforderungen||
|---|---|
|Technik- und Datenvariationen||
|---|---|
|Häufigkeit des Auftretens||
|---|---|
|Offene Fragen||
|---|---|

### 3.2 Funktionale Anforderungen

Ausführliche Beschreibung

### 3.3 Nicht funktionale Anforderungen

Ausführliche Beschreibung

### 3.4 Technische Anforderungen

Vorgegebene technische Rahmenbedingungen...
z.B. Einsatz (nur) unter Windows, Einsatz auf Linux, Mac und Windows, Nutzung SQL Server

### 3.5 Schnittstellen

Schnittstellen zu anderen Systemen, z.B. Pflegetool zu Grouper

## 4 Lösungsstrategie

Kurze Darstellung der Lösungsansätze, deren Motivation, verworfene Alternativen (Architekturentscheidungen).
Z.B. Nutzung der Programmiersprache X, des Frameworks Y und Ausschluss Bibliothek Z, verworfen weil…

## 5 Architekturüberblick

### 5.1 Bausteinsicht

#### 5.1.1 Ebene 1 (Vogelperspektive)

Zusammenspiel der Bausteine,  z.B. zentrale Pakete oder Klassen, vorzugsweise grafisch dargestellt (UML)

##### 5.1.1.1 Bausteinname 1

Beschreibung als Blackbox

##### 5.1.1.2 Bausteinname 2

Beschreibung als Blackbox

#### 5.1.2 Ebene 2

„Zoom“ in wichtige Bereiche, Optional auch dritte Ebene

### 5.2 Laufzeitsicht

Grafische Darstellung signifikanter Abläufe (UML Sequenzdiagramm)

### 5.3 Verteilsicht

## 6 Konzepte

Beschreibung grundlegender Konzepte, z.B. Persistenz: Nutzung SQLServer via ORM (objektrelationales Mapping) oder direkter SQL Zugriffe, …

### 6.1 Fachliche Strukturen und Modelle

### 6.2 Typische Muster und Strukturen

### 6.3 Benutzeroberfläche

### 6.4 Geschäftsregeln

### 6.5 Persistenz

### 6.6 Weitere relevante Konzepte

## 7 Qualitätsszenarien

Wie wird die Software geprüft? Z.B. Unittests, automatisierte Systemtests, Tests entsprechend Checkliste.

## 8 Risiken

Welche Risiken existieren und wie wird ihnen entgegengewirkt?

## 9 Anwenderdokumentation

Verweis auf Anwenderhandbuch.

## 10 Deployment / Konfigurationsmanagement

### 10.1 Entwicklungssystem

Beschreibung der für die Entwicklung benötigten Umgebung,
z.B. Betriebssystem, Entwicklungsumgebung, externe Komponenten, Lizenzen etc.

### 10.2 Zielsystem

Beschreibung der Voraussetzungen auf dem Zielsystem, 
z.B. Betriebssystem, Bibliotheken, extern Programme

## 11 Sonstiges

Projektspezifische Besonderheiten
 
