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

Soweit ein IK-Admin eingerichtet ist, bestimnmt dieser die Zugriffsrechte.
Diese Rolle kann für ein IK durchaus mehreren Anwendern zugeteilt werden. 
Da die Rechteverwaltung des IK-Admin Vorang hat, können zu diesem IK gehörende Dokumente nicht anderen Anwendern via Kooperation bereitgestellt werden.

Die Funktionalität des IK-Admin ist im Anforderungsdokument [IK-Admin](IkAdmin.md) ausführlich beschrieben.

### Erteilbare Rechte

Ein IK-Admin kann dem Anwender für eien Kombination aus IK und Funktion diese Rechte zuweisen:

* Kein Zugriff
* Lesen
* Ändern
* Erstellen
* Ans InEK senden

Für diese Rechte sind unterschiedliche Kombinationen, bis hin zu "Alles" möglich.

## Freigabe-Buttons

Jeder Datensatz, z.B. eine Nub-Anfrage, ein DRG-Vorschlag etc. kann beliebig editiert und zwischengespeichert werden. 
Ist der Datensatz fertig erfasst, wird er direkt oder über eine Prüfinstanz (Supervisor) an das InEK übermittelt.
Dafür stehen alternativ zwei Buttons zu Verfügung:

1. Zur Freigabe (an Kooperationspartner)
2. Senden ans InEK

## AccessManager, Berechtigungsfreigabe

Der Zugriff auf die Berechtigung für die unterschiedlichen Daten wird zentral über eine Manager-Klasse (Accessmanager) gesteuert.

Im Datenporal existieren diverse Funktionen, für welche eine Freischaltung durch das InEK erforderlich ist.
Dabei erfolgt die Freischaltung pauschal auf Ebene der Funktion. 
Vergibt ein IK-Admin Rechte für eine Funktion, welche der Anwender bisher nicht genuzt hat, so wird die betreffende Funktion ebenfalls freigeschaltet.
Es ist sinnvoll, eine Funktion nicht pauschal, sondern für einzelne IKs freizuschalten. 

Um den AccessManager einfach zu gestalten, werden die maximalen Zugriffsrechte je Funktion und IK unabhängig vom Vorhandensein eines IK-Admin gleichlautend genutzt.
Damit verschiebt sich der Fokus vom Freischalten einer Funktion hin zur Rechteerteilung für eine Funktion und IK.
Während der IK-Admin die Rechte seinen Anwendern detailliert in unterschiedlichen Stufen zuweist, vergibt das InEK im Regelfall lediglich die Rechte "Kein Zugriff" oder "Alles", was dem bisherigen Sperren oder Freischalten einer Funktion entspricht.
Für einen einfachen Ablauf sendet das DatenPortal bei einem entpsrechenden Bedarf eine E-Mail an das InEK, welche einen speziellen Link zu Sprerrung oder Freigabe enthält.
Damit entspricht der Ablauf in etwa dem bisherigen Freigabe-Verfahren. 
Bei Freigabe werden im Regelfall "Alle" Rechte eingeräumt.
Eine Ausnahme bilden Funktionen, die einen IK-Admin voraussetzen. Hier werden auch bei Freigabe die Rechte auf "verboten" gesetzt (bzw. bleiben unverändert auf "verboten" stehen).
Darüber hinaus kann das InEK die Rechte bei speziellem Bedarf fein granular steuern.
Hierfür werden die gleichen Funktionen wie beim IK-Admin genutzt.
Das InEK wird somit zum IK-Admin für alle IKs, für welche kein IK-Admin eingerichtet ist.
Existiert ein IK-Admin, so kann das InEK über das Datenportal keine Rechte für das betreffende IK ändern; die Verantwortlichkeit liegt beim IK-Admin.

### Sichtbarbeit von IK 

#### IK-Admin vorhanden

Der Anwender hat Zugriff entsprechend der vom IK-Admin vergebenen Rechte.
Ein IK wird für eine Funktion sichtbar, wenn der Anwender mindestens Leserecht, also ein Recht ungleich "Zugriff verboten" hat.
Welche Aufgaben er für dieses Funktion durchführen kann, hängt von den genauen Rechten ab.

#### Kein IK-Admin vorhanden

Der Anwender sieht alle IK für die er in der betreffenden Funktion mindestens über das Leserecht verfügt.
Hier braucht der AccessManager also nicht zu unterscheiden.
In der Regel vergib das InEK bei Freischaltung das Recht "Alles".

Der Zugriff auf einzelne Datensätze innerhalb eines IK hängt jedoch von anderen Faktoren ab:
* Der Anwender hat den Datensatz im "Besitz: Der Anwender darf darauf zugreifen.
* Der Datensatz befindet sich im "Besitz" eines Kooperationspartners: Der Anwender darf entsprechend der vom Kooperationspartner gewährten Rechte darauf zugreifen.
* Der Datensatz befindet sich im "Besitz" eines Anwenders, mit dem keine Kooperartion besteht: Der Anwender darf nicht darauf zugreifen.

Der Anwender "besitzt" initial Datensätze, die er selber angelegt hat.
Der Besitz kann übernommen werden, sofern ein entsprechendes Recht eingeräumt wurde.

### Anzeige von Daten

In der entsprechenden Funktion werden dem Anwender offene wie bereits an das InEK gesendete Daten gelistet.
Zur Struktuierung der Listen werden die Daten in Gruppen zusammengefasst. 
Existiert für ein IK ein IK-Admin, so ist das IK das Gruppierungsmerkmal, andernfalls der Besitzer (im Rahmen der kooperativen Rechte).
Unterhalb des Besitzers werden keine Daten gelistet, die zu einer vom IK-Admin verwalteten IK gehören.

Ohne IK-Admin kann der AccessManager über die "IK-Amin-Rechte" nur den maximalen Zugriff bestimmen.
Die tatsächlichen Zugriffsrechte ergeben sich auf Ebene IK + Besitzer aus den kooperativen Rechten.
Damit der AccessManager dies bestimmen kann, benötigt er eine Information, welche der betroffenen IK verwaltet sind (IK-Admin existiert).

[Zurück zum Hauptdokument](DataPortal.md#AccessRights)