# IK-Admin

## Ausgangslage / Motivation

Das InEK Datenportal steht grundsätzlich allen Anwendern offen. 
Insbesondere am Vorschlagsverfahren kann sich jederman beteiligen.
Anwender bestimmen selbst, welche Funktionen sie nutzen möchten und hinterlegen eigenständig für welches Krankenhaus sie im InEK Datenportal tätig sind.

Mit Ausbau des InEK Datenportals ist zunehmend Funktionalität mit direktem Krankenhausbezug enthalten.
Teilweise sind hier nur bestimmte Anwender zulässig, so dass solche Funktionen erst nach Prüfung durch das InEK freigeschaltet werden.
Mit Einführung der Funktionen "Teilnahme Kostenkalkulation" (in 2017) wurde das Anlegen von Daten in diesem Funktionsbereich gar auf die beim InEK hinterlegtn Ansprechpartner eingeschränkt.

Im InEK Datenportal angelegte Datensätze "gehören" dem Benutzer, der sie anlegt.
Mittels Kooperation kann der Anwender bestimmen, wer diese Daten zusätzlich sehen, bearbeiten, an das InEK senden und / oder den "Besitz" übernehmen darf.
Im Falle von Mitarbeiterwechsel müssen Daten mit Krankenhausbezug für das Krankenhaus weiterhin zugreifbar bleiben.
Eine reibungslose Übergabe ist zwar mittels richtig eingestellter Kooperation möglich, wie auch durch Übertragung des kompletten Benutzerkontos, 
jedoch kommt es in der Praxis immer wieder vor, dass es genau hier hapert und das InEK Daten mit Krankenhausbezug von einem Anwenderkonto auf einen anderes "umhängen" muss.
Im Hinblick auf den Datenschutz geschieht dies nur nach schriftlichem Auftrag durch die Krankenhausleitung.

Fragen wie "wer arbeitet im InEK Datenportal für ein Haus?" kann das InEK bei Bedarf durch eine entsprechende Datenbankabfrage beantworten, 
jedoch nicht unbedingt die Frage "ist der betreffende Anwender berechtigt, Daten für dieses Haus zu bearbeiten?". 
Zur Beantwortung dieser Frage ist eine Instanz erforderlich, welche die betreffenden Personen kennt.

Hier greift nun die Installation eines (oder mehrerer) "IK-Admins".
Ein IK-Admin ist eine vom Krankenhaus bestimmte Person, regelt den Zugriff auf Krankenhausdaten und übernimmt die Verwaltungsaufgaben, 
die das InEK derzeit nur nach schriftlichem Auftrag des Hauses ausführt, z.B. Umhängen von NUB-Anfragen.
Ein entscheidender Teil der Benutzeradministration wird somit dahin verlagert, wo dass Wissen über Anwender und benötigte Funktionen vorhanden ist.
Damit wird die Sicherheit erhöht und der Verwaltungsaufwand verteilt und reduziert (schriftlicher Auftrag entfällt).

## Anforderungen

### Anlegen eines IK-Admins

- Das InEK legt IK-Admins im schriftlichen Auftrag der Krankenhausleitung an
- Ein IK-Admin kann für dieses IK weitere IK-Admins anlegen

### Löschen eines IK-Admins

- Ein IK-Admin kann für dieses IK für andere Personen die IK-Admin-Rechte entziehen
- Ein Anwender löscht sein Benutzerkonto - das Löschen ist nicht möglich, sofern es sich um den einzigen IK-Admin handelt
- Das InEK löscht IK-Admins im schriftlichen Auftrag der Krankenhausleitung

### Zuweisung von IK und Funktionen

- Der IK-Admin kann Anwendern ein IK nebst Funktionen zuweisen; in einem solchen Falle ist eine Freischaltung durch das InEK nicht erforderlich
- Trägt ein Anwender für sich ein IK ein, für das ein IK-Admin existiert, so ist dieser Eintrag nicht möglich. Stattdessen wird der IK-Admin informiert und kann die Eintragung vornehmen.

### Sichtbarkeit und Übertragung von Daten

- Die Sichtbarkeit von Daten wird unverändert kooperativ geregelt
- Zusätzlich kann der IK-Admin bestimmen, welche Krankenhausdaten unabhängig von einer Kooperation für wen mit welchen Rechten zugreifbar sind. Solange der IK-Admin Daten nicht mittels Verbot blockiert, gelten dabei die weiter gefassten Rechte (Zu klären: Dies für für IK-Admin sichbar machen?)
- Daten bestimmter Funktionen (derzeit NUB), die nicht krankenhausweit sichtbar sind, kann der IK-Admin von einem Anwenderkonto auf ein anderes übertragen

### weitere Funktionen

- Der IK-Admin verwaltet die IK-Supervisoren
- Der IK-Admin erhält eine Übersicht mit allen der IK zugeordneten Anwenderkonten
- Der IK-Admin kann eineoder mehrere zulässige Mail-Domains (wie "@musterhospital.de") festlegen. Die Mail-Domain schränkt den "Besitzer" von Daten ein (*). Möchte ein Anwender seine Mailadresse auf eine nicht-zulässige Domain umstellen, so wird (nach Hinweis) das betreffende IK aus seiner Zuständigkeit entfernt. Die o.a. Zuweisung von IK und Funktionen erfolgt dagegen unabhängig von der Mail-Domain.
- Der Anwender erhält zu seinen IKs eine Übersicht, welche IK-Admins für die einzelnen IKs zuständig sind

(*) Hintergrund:
Der Besitzer bestimmt im Rahmen der Kooperation, wer seine Daten alles sehen darf. 
Beispielsweise ist es für bestimmte Dokumente unerwünscht, dass ein Berater, der mit vielen Häusern kooperiert diese "besitzt" und somit versehentlich auch für andere sichtbar macht.
Alternativ ist dies via IK abhängiger Kooperation zu regeln.




[Zurück zum Hauptdokument](DataPortal.md#FunctionalRequirements)