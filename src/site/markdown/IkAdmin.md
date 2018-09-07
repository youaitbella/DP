# IK-Admin

## Ausgangslage / Motivation

Das InEK Datenportal steht grundsätzlich allen Anwendern offen. 
Insbesondere am Vorschlagsverfahren kann sich jederman beteiligen.
Anwender bestimmen selbst, welche Funktionen sie nutzen möchten und hinterlegen eigenständig für welches Krankenhaus sie im InEK Datenportal tätig sind.

Mit Ausbau des InEK Datenportals ist zunehmend Funktionalität mit direktem Institus-Bezug enthalten.
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

## Funktionen

### Anlegen eines IK-Admins

- Das InEK legt IK-Admins im schriftlichen Auftrag der Krankenhausleitung an

### Löschen eines IK-Admins

- Das InEK löscht IK-Admins im schriftlichen Auftrag der Krankenhausleitung. Mit Löschung des letzen IK-Admins wird die Berechtigungstabelle für das betreffende Haus entfernt. Damit ist dies wieder "offen". Das InEK sollte vor Löschung des letzen IK-Admins dies mit dem Krankenhaus erläutern und bestätigen lassen.
- Ein Anwender löscht sein Benutzerkonto - das Löschen ist nicht möglich, sofern es sich um den einzigen IK-Admin handelt

### Zuweisung von IK und Funktionen

- Der IK-Admin kann Anwendern die Berechtigung für ein IK nebst Funktionen zuweisen; in einem solchen Falle ist eine Freischaltung durch das InEK nicht erforderlich
- Trägt ein Anwender für sich ein IK ein, für das ein IK-Admin existiert, so ist dieser Eintrag nur ohne Zugriffsrechte möglich. Der IK-Admin wird informiert und kann Berechtigungen zuweisen.

### Sichtbarkeit und Übertragung von Daten

- Die Sichtbarkeit von Daten ohne IK-Bezug respektive ohne IK-Admin für ein IK wird unverändert kooperativ geregelt
- Der Zugriff auf Daten mit IK-Bezug wird durch den IK-Admin festgelegt.
- Daten mit IK-Bezug, kann der IK-Admin von einem Anwenderkonto auf ein anderes übertragen, sofern dieser Anwender über die entsprechenden Rechte verfügt

### weitere Funktionen

- Der IK-Admin verwaltet die IK-Supervisoren
- Der IK-Admin erhält eine Übersicht mit allen der IK zugeordneten Anwenderkonten
- Der IK-Admin kann eine oder mehrere zulässige Mail-Domains (wie "@musterhospital.de") festlegen. Die Mail-Domain schränkt den "Besitzer" von Daten ein (*). Möchte ein Anwender seine Mailadresse auf eine nicht-zulässige Domain umstellen, so wird (nach Hinweis) das betreffende IK aus seiner Zuständigkeit entfernt. Die o.a. Zuweisung von IK und Funktionen erfolgt dagegen unabhängig von der Mail-Domain.
- Der Anwender erhält zu seinen IKs eine Übersicht, welche IK-Admins für die einzelnen IKs zuständig sind

(*) Hintergrund:
Der Besitzer bestimmt im Rahmen der Kooperation, wer seine Daten alles sehen darf. 
Beispielsweise ist es für bestimmte Dokumente unerwünscht, dass ein Berater, der mit vielen Häusern kooperiert diese "besitzt" und somit versehentlich auch für andere sichtbar macht.
Alternativ ist dies via IK abhängiger Kooperation zu regeln.

## Datenstrukturen

Die Daten der IK-Amin-Funktionen werden im Schema "ikadm" gespeichert.

### Definition IK-Admin

Die Definition eins IK-Admins erfolgt durch Zuordnung der zu administrierenden IKs zum Anwenderkonto

(mapAccountIkAdmin)
- AccountId
- IK
- MailDomäne (ggf. Semikolon-getrennte Mehrfachangabe)

### Berechtigungen

Ein Anwender, der potenziell für ein Haus arbeitet, kann dies derzeit in der Stammdatenpflege eintragen.
Diese Info ist in die Berechtigung zu übernehmen. Hier kann der IK-Admin jedoch Rechte für einezelne Funktionalitäten verweigern (Recht: Deny).

- id (technischer Schlüssel)
- AccountId
- IK
- Feature
- Recht (Deny Read Write Seal All)

Deny    - Verbot
Read    - Lesen
Write   - Schreiben. Enthält Lesen, Kann keine neuen Datensätze anlegen!
Create  - Anlegen. Enthält Lesen und Schreiben
Seal    - Abschließen. Enthält Lesen, aber nicht Schreiben (reine Supervisorfunktion).
All     - Alle (Anlegen, Lesen, Schreiben, Abschließen)

### Ablauf

#### Anwender wird zum IK-Admin

Soweit für dieses IK bisher noch kein IK-Admin existierte:

Für alle Anwender, welche das betreffende IK eingetragen haben wird für deren genutzte Featueres geprüft,
ob eine Berechtigung eingetragen ist. Falls nein, so wird eine Berechtigung "All" erzeugt.
Dies stellt sicher, dass Personen, die bisher für ein Krankenhaus gearbeitet haben, dies initial auch weiterhin
können.

#### IK-Admin startet User Management

Der IK-Admin erhält eine editierbare Tabelle mit existierenden Berechtigungen.
Der IK-Admin kann den dort aufgeführten Personen, sowie allen Personen der hinterlegten Mail-Domaine Berchtigungen für weitere Features zufügen. 
Sofern noch nicht vorhanden, wird damit bei diesen Personen das betreffende Feature aktiviert sowie bei Bedarf das IK in den Stammdaten zugefügt.

Entzieht der IK-Admin einem Anwender, der "Besitzer" von Datensätzen ist, die Berechtigung zum Zugriff auf dieselben, so wird der "Besitz" auf den IK-Admin übertragen. Der allgemeine Zugriff auf diese Daten ist von den Rechten, nicht vom Besitzer abhängig, jedoch kann einAnwender ohne Rechte kein Besitzer sein.

#### Anwender fügt IK oder Feature hinzu

Sofern ein IK-Admin vorhanden ist, erzeugt dies Einträge in der Berechtigungstabelle mit Recht "Deny".
Somit wird sichergestellt, dass sich Anwender nicht mehr unmittelbar zur Bearbeitung der Daten eines Hauses eintragen können.
Der IK-Admin erhält eine Nachricht und kann die Rechte anpassen.

Soweit ein Anwender ein Feature zufügt und für alle IKs ein IK-Admin vorhanden ist, benötigt das InEK keine Nachricht zur Freischaltung.

#### Anwender nutzt Feature

Die Liste der möglichen IKs wird wie bisher aus den Anwenderstammdaten gelesen. 
Für jedes IK wird geprüft, ob ein IK-Admin vorhanden ist. Sofern ja, gelten die Berechtigungen entsprechend der Berechtigungstabellen, andernfalls kann der Anwender das IK im Feature wie bisher nutzen.

## Besonderheiten Krankenhausvergleich

Für die Nutzung des Krankenhausvergleichs ist die Nutzer-Verwaltung zwingend über den IK-Admin zu steuren.
Um dies allgemein zu halten, wird der IK-Admin-Zwang mittels Eigenschaft des betreffenden Features gesteuert, so dass dies auch für andere Bereche genutzt werden kann.

[Zurück zum Hauptdokument](DataPortal.md#FunctionalRequirements)