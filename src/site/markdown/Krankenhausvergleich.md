# Krankenhausvergleich

## Zielsetzung

Mit dem Gesetz zur Weiterentwicklung der Versorgung und der Vergütung für psychiatrische und psychosomatische Leistungen (PsychVVG) vom 19.12.2016 wurde in § 4 BPflV ein leis-tungsbezogener Krankenhausvergleich eingeführt.

Dieser Leistungsvergleich dient insbesondere als Basis für Budgetverhandlungen.
Dabei werden dem Krankenhaus, über dessen Budget verhandelt wird, die Daten einer Vergleichsgruppe gegenüber gestellt.

Die Vergleichsgruppe setzt sich zusammen aus allen Psych-Krankenhäusern 

- Deutschlands
- eines Bundeslandes
- die bestimmten Mekrmalen entsprechen

Letzteres entstammt einem Vorschlag des InEK und fand erst breite Zustimmung der Selbstverwaltungspartner, während später die Krankenkassen Bedenken anmeldeten.
Insofern ist dies (Stand September 2018) noch nicht konsentiert. 

Die Vergleichswerte für die Verhandlungen werden aus dem jeweils aktuellen Datenbestand berechnet.
Da sich dieser Bestand jederzeit ändern kann, wird der berechnete Stand unter einer Id "eingefroren" und kann von den Verhandlungspartner jederzeit wieder abgerufen werden.

Die Vergleichswerte auf Bundes- sowie Landesebene werden darüber hinaus zu bestimmten Stichtagen (Quartalsbeginn) berechnet und in ihrer neusten Form für die Öffentlichkeit zugänglich zur Verfügung gestellt.

## Datenerfassung

Für den Vergleich werden diverse Daten benötigt. 
Teilweise werden diese bereits vom InEK im Rahmen der §21-Datenlieferung sowie Psych-Personalnachweis erhoben bzw. gesammelt.
Für weitere Daten wie AEB, Strukturinformation werden neue Erfassungsformulare bereitgestellt.
Ein Großteil der Daten liegt dabei sowohl den Krankenhäusern als auch den Krankenkassen vor.
Auch wenn das Gesetz vorgibt, wer für welche Daten (primär) lieferverpflichtet ist, haben sich die Vertragspartner geeinigt, dass der jeweils andere Partner die Daten als Fallback erfassen darf.

Liegen Daten von beiden Partnern vor, so werden im Vergleich die Daten des primär lieferverpflichteten Partners genutzt.
Insofern sind die Eingaben der beiden Partner getrennt zu erfassen und zu speichern.
Insbesondere dürfen Korrekturen nur im "eigenen" Datenstand erfolgen.

Die Berechtigung der Anwender erfolgt grundsätzlich über einen IK-Admin.  

Krankenhaus lieferverplichtet:
Der IK-Admin des Krankenhauses legt auf Ebene eines IK fest, welcher Anwender mit welchen Rechten die zu dem betreffenden IK gehörenden Daten bearbeiten darf.
Der IK-Admin der Krankenkasse legt fest, welcher "seiner" Anwender für welches Krankenhaus (IK) als Verhandlungspartner zuständig ist.
Sofern diese Krankenkasse für die Verhandlungen auf Kassenseite federführend ist, darf der Kassenmitarbeiter Daten für das KH-IK erfassen, sofern er von seinem IK-Admin Schreibrecht erhalten hat.

Krankenkasse lieferverpflichtet:
Es werden seitens der federführenden Krankenkasse Daten für ein bestimmtes Krankenhaus (IK) erfasst.
Der IK-Admin der Krankenkasse legt fest, welcher "seiner" Anwender für welches Krankenhaus (IK) als Verhandlungspartner zuständig ist und erteilt Rechte.
Der Krankenhausmitarbeiter kann - im Rahmen seiner Rechte - die Daten für sein Krankenhaus an Stelle der federführenden Krankenkasse als Fallback erfassen.

Der GKV stellt dem InEK eine Liste zur Verfügung, welche Krankenkasse (IK) für welches Krankenhaus (IK) die Federführung übernimmt.


## Auswertung

Zur Auswertung des Krankenhausvergleichs stehen dem Anwender nach Eingabe der IK des zu vergleichenden Krankenhauses je drei Auswahlmöglichkeiten für Region/Fachgebiet und Zeitraum zur Verfügung

Das Fachgebiet entspricht einer der drei PEPP-Strukturkategorien oder "Sonstige". Die Zuordnung eines Fachgebiets zu einem Krankenhaus erfolgt initial anhand vorliegender Daten (Vereinbarung, ersatzweise §21). Hierbei gilt: Entfallen mehr als 70% der Berechnungstage auf ein Fachgebiet, wird dieses dem KH zugeordnet, andernfalls "Sonstige". Die Zuordnung wird für jedes Vereinbarungsjahr erneut festgelegt und in einer DB-Tabelle hinterlegt.
Die Zuordnung zu einem Bundesland erfolgt anhand des Standortes. Sollte ein Krankenhaus über mehrere Standorte in unterschiedlichen Bundesländern verfügen, so gilt im Regelfall die Adresse des Genehmigungsbescheids. Die Zuordnung zu einem Bundesland wird ebenfalls in der DB hinterlegt.

Folgende Auswertungen werden angeboten:

Region/Fachgebiet
a. Vergleichsgruppe besteht aus allen KH aus dem selben Bundesland und dem selben Fachgebiet 
b. Vergleichsgruppe besteht aus allen KH aus demselben Bundesland
c. Vergleichsgruppe besteht aus allen KH bundesweit mit dem selben Fachgebiet 
Bei mehr als 15 KH in Gruppe a. ist diese zu nutzen. Maßgeblich ist die Anzahl vorhandener KH, nicht die Anzahl Datenlieferungen.

Zeitraum
1. aktuell (es werden die jeweils neusten vorliegenden Daten bis Vereinbarungsjahr -1 genutzt; bereits vorlöiegende Vereinbarungen für das Vereinbarungsjahr werden somit ignoriert)
2. Jahresgleich ("sortenrein") Vereinbarungsjahr -1 (wenn für Jahr x verhandelt wird, liegen im Regelfall die Daten des eigenen Hauses für x-1 vor)
3. Jahresgleich Vereinbarungsjahr -2 

Die jahresgleichen Vergleiche sind nur möglich, wenn für das zu vergleichende Haus selbst Daten für das betreffende Jahr vorliegen.

Aus der Kombination a-c sowie 1-3 ergeben sich 9 unterschiedliche Auswertungen. Noch zu klären: 
- Anwender darf wählen
- Es werden immer alle möglichen Auswertungen (3, 6, oder 9, jenachdem ob jahresgleich möglich) erstellt.

Nach Start der Auswertung wird ein Ecxel-Sheet (vorläufiger, noch nicht konsentierter Stand [Auswertungsergebnis_Krankenhausvergleich](/uploads/465b6b3dc9cb50c2320175c2f098318a/Auswertungsergebnis_Krankenhausvergleich_Entwurf_181102_final.xlsx)) für jede der angefragten Auswertungen befüllt und dem Anwender zum Download angeboten. Hierzu wird ein Excel-Formular als Vorlage genutzt und vom Report-Server via SQL-Statements befüllt. Die Auswertung ist sehr umfangreich, so dass es möglich sein könnte, dass die Befüllung einen dem wartenden Anwender zumutbaren Zeitraum (z.B. 10 sek) überschreitet. Im Rahmen der Implementierung wird (abhängig von der dann bekannten Generierungsdauer) entschieden, ob das System dem Anwender die Datei per Knopfdruck zur Verfügung stellt, oder ob der "Start"-Knopf die Auswertung lediglich antriggert und das System die Datei dem Anwender im Dokumentenbereich zur Verfügung stellt. In diesem Fall sendet das System nach Generierung der Daten eine Nachricht an den Anwender. Sofern der Anwender noch im System arbeitet, kann diese Nachricht im Portal (z.B. Popup) erfolgen, andernfalls per Mail.

- **Um möglichst früh entscheiden zu können, wie die Auswertung zum Anwender kommt, wird erst die Auswertung via ReportServer implementiert. Diese kann im Rahmen der Entwicklung mittels eines Tools wie Curl angestoßen werden. Abhängig von diesem Ergebnis wird der Dialog gestaltet.** Damit kann die Entwicklung der eigentlichen Auswertung trotz noch bestehender Fragestellungen zu den Auswahlmöglichkeiten gestartet werden.

Je nach Größe eines Bundeslandes und der Zuordnung zum Fachgebiet kann es vorkommen, dass weniger als 15 Krankenhäuser in Gruppe a. vorhanden sind. Hier können die Verhandler auf eine der Gruppen b. oder c. ausweichen. Die Berechnung der statistischen Kennzahlen setzt jedoch mindestens 4 Häuser mit Datenlieferung voraus. Möglicherweise wird auch diese Menge nicht erreicht. In einem solchen Fall dürfen die Verhandler einvernehmlich die Region auf ein benachbartes Bundesland ausdehnen. Eine entsprechende Auswertung ist für die Verhandler über das Portal **nicht** möglich; vielmehr wird in einem solchen Fall das InEK mit der Erzeugung der Auswertung betraut. In der Schnittstelle zum Reportserver ist daher die **Angabe mehrerer Bundesländer** vorzusehen. Für den Aufruf des Reports gibt es keine weiteren Vorgaben - dies könnte beispielsweise manuell per Curl erfolgen. Alternativ kann eine nur für das InEK sichtbare Erweiterung des Auswerte-Dialogs erstellt werden, in welcher das InEK eine Liste aller Bundesländer hat. Standardmäßig ist das Land des Krankenhauses markiert (nicht abwählbar). Weitere Bundesländer können markiert werden.

Aufgrund der laufenden Datenerfassung wird die Datengrundlage stetig aktualisiert. Der Abruf des Vergleichs zu unterschiedlichen Zeiten kann daher zu unterschiedlichen Auswertungen führen. Damit die Verhandlungspartner mit derselben Auswertung agieren können, wird diese unter einer ID gespeichert und kann von den Partnern unter dieser ID abgerufen werden. Die ID verteilen die Partner selbständig, wahlweise per Mail oder Kommunikation innerhalb des Datenportals.

Optionen (noch zu klären):
Das System bietet zwei "Startknöpfe" zur

- informativer Vergleich (wird nicht gespeichert)
- Vergleich als Verhandlungsgrundlage (wird unter ID gespeichert)

Wenn nicht jeder Vergleich gespeichert wird, könnte bei einer "Verhandlungsgrundlage" die ID automatisch an Partner übermittelt werden.

Neben "individuellen" Auswertungen (Anwender startet nach Selektion), werden für die Öffentlichkeit Auwertungen zu festgelegten Zeitpunkten (Quartal) erstellt und im System für den Abruf bereitgestellt. Hier wird ein Dialog erstellt, welcher für jedem interessierten Portalnutzer (ohne Freigabe) zugänglich ist. Der Anwender wählt einen Bericht (z.B. Link-Liste) und der Download startet unmittelbar.
[Zurück zum Hauptdokument](DataPortal.md#FunctionalRequirements)
