# Zugriffsrechte

Das InEK Datenportal wird zur datengetriebenen Kommunikation zwischen dem InEK und externen Teilnehmern genutzt.
Externe Teilnehmer sind Mitarbeiter von Krankenhäusern, Krankenkassen, Berater, Behörder, aber auch Einzelpersonen.
Es wird zwischen Daten mit und ohne Bezug zu einem Institut unterschieden. 
Soweit der IK-Bezug erforderlich ist, können (oder müssen, je nach Funktion) die Zugriffsrechte durch einen IK-Administrator vergeben werden.
Andernfalls kann der Anwender selber bestimmen, wer Zugriff auf "seine" Daten erhalten soll. Dies wird als "Kooperative Rechtevergabe" bezeichnet.

## Kooperative Rechtevergabe

Das InEK Datenportal steht jedem offen. 
Insofern sind – soweit kein IK Admin (siehe unten) eingerichtet – alle externen Anwender "gleich". 
Ein jeder Anwender kann erst einmal nur die Daten sehen, die er selbst erfasst. 
Arbeiten mehrere Personen an einem Thema, kann es sinnvoll sein, die Daten eines anderen Anwenders lesen oder bearbeiten zu können. 
Hierfür sind dem Anwender spezielle Rechte zu erteilen. 

Ist für Daten mit IK-Bezug ein IK-Admin eingerichtet, so erfolgt die Rechtevergabe über diesen. 
Ohne IK-Admin bedeutet Rechtevergabe, dass der Besitzer der Daten dem anderen Anwender die Zugriffsrechte erteilen kann. 
Hier steht also ein kooperierender Gedanke im Vordergrund. 
Entsprechend wird diese Art der Rechteverwaltung im Folgenden als "kooperative Rechtevergabe" bezeichnet.

Die Rechtevergabe setzt voraus, dass der andere Anwender auch existiert. 
Anders als in einem sozialen Netzwerk sind die Anwender im InEK Datenportal nicht öffentlich sichtbar. 
Auch erfolgt bei der Angabe der "fremden" Anwenderkennung / E-Mail-Adresse keine Rückmeldung, ob diese existiert, um ein Test auf Vorhandensein zu unterbinden. 
Stattdessen muss der eingetragene Anwender sich erst als Kooperationspartner bestätigen. 
Bestätigte Kontakte können in der Rechtevergabe wie auch für andere Funktionen, z.B. Nachrichtenversand, genutzt werden.

Optional: Um zu verhindern, dass ein Anwender von ein und demselben anderen Anwender immer wieder mit Kooperationsanfragen bombardiert wird, kann ein anfragender Anwender blockiert werden.

### Erteilbare Rechte

Ein Anwender kann dem Kooperationspartner für seine Daten diese Rechte (in unterschiedlichen Kombinationen) zuweisen:

* Kein Zugriff
* Lesen
* Ändern
* (Als Supervisor) ans InEK senden
* Besitz übernehmen

Die Zuweisung erfolgt getrennt für jedes Feature, das eine kooperative Rechtevergabe unterstützt. 
Je nach Feature kann diese Rechtevergabe auch getrennt nach IK erfolgen.

Soweit ein Anwender anderen das Recht der Supervison erteilt, kann dieser Anwender seine Daten nicht mehr an das InEK senden. 
Der entsprechende Button wird ausgetauscht durch einen "zur Freigabe durch Partner".

Hat ein Anwender von einem Kooperationspartner Rechte erhalten, so werden die "fremden" Daten bei den betreffenden Features in eigenen Listen (je Partner) geführt.

Die Zugriffsrechte innerhalb des jeweiligen Features werden in eine zentrale Abfrage gekapselt.

Kooperative Rechte kommen bei Funktionen mit IK-Bezug nur dann zur Anwendung, wenn für dieses IK kein IK-Admin eingerichtet ist.
Die Rechtevergabe via IK-Admin "schlägt" also die kooperative Rechtevergabe.


## IK-Admin

Soweit ein IK-Admin eingerichtet ist, bestimnmt dieser dir Zugriffsrechte.
Diese Rolle kann für ein IK durchaus mehreren Anwendern zugeteilt werden. 
Der IK-Admin wird weiter unten gesondert beschrieben.

## Freigabe-Buttons

Jeder Datensatz, z.B. eine Nub-Anfrage, ein DRG-Vorschlag etc. kann beliebig editiert und zwischengespeichert werden. 
Ist der Datensatz fertig erfasst, wird er direkt oder über eine Prüfinstanz (Supervisor) an das InEK übermittelt.
Dafür stehen alternativ zwei Buttons zu Verfügung:

1. Zur Freigabe an Kooperationspartner
2. Senden ans InEK


[Zurück zum Hauptdokument](DataPortal.md#AccessRights)