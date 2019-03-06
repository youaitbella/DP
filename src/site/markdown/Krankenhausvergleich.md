# Krankenhausvergleich

## Zielsetzung

Mit dem Gesetz zur Weiterentwicklung der Versorgung und der Vergütung für psychiatrische und psychosomatische Leistungen (PsychVVG) vom 19.12.2016 wurde in § 4 BPflV ein leistungsbezogener Krankenhausvergleich eingeführt.

Dieser Leistungsvergleich dient insbesondere als Basis für Budgetverhandlungen.
Dabei werden dem Krankenhaus (KH), über dessen Budget verhandelt wird, die Daten einer oder mehrerer Vergleichsgruppe(n) gegenüber gestellt.

Jedes Krankenhaus ist für den Vergleich einem Bundesland und einem Fachgebiet zugeordnet.

Die Vergleichgruppe setzt sich zusammen aus

a) allen KH aus dem selben Bundesland mit dem selben Fachgebiet 
b) allen KH aus demselben Bundesland
c) allen KH bundesweit mit dem selben Fachgebiet 

Bei mehr als 15 KH in Gruppe a) ist diese für den Vergleich zu nutzen. Maßgeblich ist die Anzahl bestehender KH, nicht die Anzahl Datenlieferungen (zu vergleichender Daten).

Für den Vergleich kommen Daten aus unterschiedlichen Zeiträumen zum Tragen

1. aktuell 
2. Jahresgleich Vereinbarungsjahr - 1
3. Jahresgleich Vereinbarungsjahr - 2 

Für aktuell werden aus allen Häusern der Vergleichsgruppe die jeweils letzten (bezogen auf Vereinbarungsjahr) vorliegenden Daten, die älter als das Vereinbarungsjahr sind, genutzt.
Bei jahresgleichen ("sortenreinen") Vergleichen stammen alle Vereinbarungen aus dem selben Jahr. 
Ein "sortenreiner" Vergleich ist nur möglich, wenn vom zu vergleichenden Krankenhaus bereits eine Vereinbarung für das betreffende Jahr vorliegt.

Die Vergleichswerte für die Verhandlungen werden aus dem jeweils aktuell vorliegendem Datenbestand berechnet.
Da sich dieser Bestand jederzeit ändern kann, wird die Auswertung unter einer Id "eingefroren" und kann von den Verhandlungspartnern jederzeit wieder abgerufen werden.

Die Vergleichswerte auf Bundes- sowie Landesebene werden darüber hinaus zu bestimmten Stichtagen (Quartalsbeginn) berechnet und in ihrer neusten Form für die Öffentlichkeit zugänglich zur Verfügung gestellt.

## Zuordnung Fachgebiet und Land

Das Fachgebiet entspricht einer der drei PEPP-Strukturkategorien oder "Sonstige". 
Die Zuordnung eines Fachgebiets zu einem Krankenhaus erfolgt initial anhand vorliegender Daten (letzte Vereinbarung, ersatzweise §21). 
Hierbei gilt: Entfallen mehr als 70% der Berechnungstage auf ein Fachgebiet, wird dieses dem KH zugeordnet, andernfalls "Sonstige". 
Die Zuordnung wird für jedes Vereinbarungsjahr erneut festgelegt und in einer Datenbank-Tabelle hinterlegt.

Die Zuordnung zu einem Bundesland erfolgt anhand des Standortes. 
Sollte ein Krankenhaus über mehrere Standorte in unterschiedlichen Bundesländern verfügen, so sendet das KH eine Information über das zu Grunde liegende Bundesland (gilt im Regelfall entsprechend der Adresse des Genehmigungsbescheids) an das InEK. 
Die Zuordnung zu einem Bundesland wird ebenfalls in der Datenbank hinterlegt.

## Datenerfassung

Für den Vergleich werden diverse Daten benötigt. 
Teilweise werden diese bereits vom InEK im Rahmen der §21-Datenlieferung sowie Psych-Personalnachweis erhoben bzw. gesammelt.
Für weitere Daten wie AEB, Strukturinformation werden neue Erfassungsformulare bereitgestellt.
Ein Teil der Daten, wie beispielsweise die Daten der AEB, liegen dabei sowohl dem Krankenhaus als auch der Krankenkasse (KK) vor.
Auch wenn das Gesetz vorgibt, wer für welche Daten (primär) lieferverpflichtet ist, haben sich die Vertragspartner geeinigt, dass der jeweils andere Partner solche Daten als Fallback erfassen darf.

Im Rahmen der Datenerfassung werden Plausibilitätsprüfungen durchgeführt. Unplausible Daten können zwar gespeichert, nicht aber an das InEK gesendet werden. 
Somit hat der Anwender hat die Möglichkeit, die Daten vor dem abschließenden Sendevorgang zu überarbeiten.

Werden Daten von beiden Partnern erfasst, so werden diese getrennt abgelegt. Es bleibt damit nachvollziehbar, welche Daten vom KH bzw. der KK erfasst wurden.
Die beiden Datensätze werden vom System automatisch verglichen. Bei (signifikanten) Abweichungen werden beide Partner informiert und erhalten Gelegenheit zur Korrektur oder Bestätigung (Datenlieferung erhält Status "Korrektur bzw. Konkretisierung angefordert" und kann editiert werden) des eigenen Datensatzes. Erst wenn keine (signifikanten) Abweichungen vorliegen, werden diese Daten im Vergleich genutzt (*andernfalls Krankenhaus mit Hinweis "ausgeschlossen wegen abweichender Daten" in die Liste der beteiligten Häuser aufnehmen?*). 
Insbesondere kann für das KH bei abweichenden Daten kein Vergleich durchgeführt werden. 
Damit liegt es im Interesse der Partner, Abweichungen vor Verhandlungsbeginn zu klären.
Ein besonderes Clearingverfahren ist somit nicht erforderlich. 

Liegen Daten von beiden Partnern vor, so werden im Vergleich die Daten des primär lieferverpflichteten Partners genutzt.

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

Entsprechend dem aktuellen Stand (06.03.2019) besteht der Wunsch, die Anzahl der Anwender und Zuständigkeiten zu beschränken.

### Plausiprüfungen

In ihrer Vereinbarung fordern die Vereinbarungspartner, die Daten auf Plausibilität zu prüfen, bevor sie für den Vergleich zugelassen werden.

- In Schlüsselfeldern wie PEPP, ET, ZE, Entgelt, OPS erfolgt eine Prüfung gegen eine für das betreffende Jahr hinterlegte Liste. Sollten für einzelne Felder im Datenportal keine abschließenden Listen hinterlegt werden können, so erfolgt mindestens eine formale Prüfung und eine Warung, wenn der eingegebene Wert nicht in der Liste enthalten ist.
- Die Eingaben in allen Feldern werden entsprechend ihrem Datgentyp geprüft (Beispiel: Im Feld "Anzahl" sind nur Ziffern zulässig. Die Anzahl muss [> 0] | [darf nicht < 0] sein).
- Beim Upload werden zusätzliche Informationen wie "Bewertungsrelation/Tag" erfasst. Diese werden gegen die für das betreffende Jahr gültigen Werte geprüft. Bei Abweichungen wird das Feld gegen die Werte der anderen Jahre geprüft. Wird daruch für den gesamten Upload ein anderes Jahr erkannt, so erfolgt eine entsprechend Meldung ("Sie versuchen Daten aus xxxx für yyyy hochzuladen. Vorgang fortsetzen?"). Hier kann der Anwender den Upload abbrechen. Bei erfolgtem Upload werden alle fehlerhaften Felder in einem Uploadprotokoll gelistet.

## Auswertung

Zur Auswertung des Krankenhausvergleichs stehen dem Anwender nach Eingabe der IK des zu vergleichenden Krankenhauses je drei Auswahlmöglichkeiten für Region/Fachgebiet und Zeitraum zur Verfügung

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
