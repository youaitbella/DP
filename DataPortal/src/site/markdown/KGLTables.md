|Blatt|Tabelle|Spalte|Tabelle DB|Spalte DB|
|---|---|---|---|---|
|Grundlagen|Grundlagen||KGLBaseInformation||
|||Summe kalkulationsrelevanter Kosten (EUR)||biSumCalcCost|
|||Fälle vollstationär||biCaseInStationCnt|
|||Fälle teilstationär||biCasePartialStationCnt|
|||Berechnungstage teilstationär||biDaysPartialStation|
|||Begleitpersonen||biPatientEscort|
|||rein vorstationär||biPreStation|
|||Betten (DRG-Bereich)||biBeds|
|||Anzahl teilstationäre Plätze (DRG-Bereich)||biPartialCnt|
|||Anzahl Standorte||biLocationCnt|
|||Differenzierter Versorgungsvertrag||biDifLocationSupply|
|||BesondereEinrichtung||biSpecialUnit|
|||Zentren und Schwerpunkte||biCentralFocus|
|Grundlagen|Entlassender Standort||KGLListLocation||
|||Entlassender Standort||lLocation|
|||Nummer||lLocationNo|
|Grundlagen|Besondere Einrichtung||KGLListSpecialUnit||
|||Art der BE||suType|
|||Fallzahl||suCaseCnt|
|||Kostenvolumen||suCost|
|Grundlagen|Zentren und Schwerpunkte||KGLListCentralFocus||
|||Bezeichnung||cfText|
|||Fallzahl||cfCaseCnt|
|||Personalkosten||cfPersonalCost|
|||Sachkosten||cfMaterialcost|
|||Infrastrukturkosten||cfInfraCost|
|||Vereinbartes Entgelt||cfRemunerationAmount|
|||Entgeltschlüssel||cfRemunerationKey|
|Grundlagen|Berücksichtigte Abgrenzungstatbestände||KGLListDelimitationFact||
|||bitte markieren||dfUsed|
|||Personalkosten||dfPersonalCost|
|||Sachkosten||dfMaterialcost|
|||Infrastrukturkosten||dfInfraCost|
|Neonatologie||"Levels der Versorgungsstufe des Perinatalzentrums
"|KGLBaseInformation|biNeonatLvl|
|Neonatologie|Qualifikationen gem. Richtlinie||KGLNeonatQuality||
|||Qualifikation||nqContentTextID|
|||Erfüllt (Datum)||nqFulfilled|
|Neonatologie|Kennzahlen/Kosten||KGLNeonatCntCost||
|||Kennzahl||nccContentTextID|
|||Anzahl/Kosten||nccValue|
|Leistungsdokumentation (ext. Lei)|||||
|||Bereich|KGLListServiceProvisionType|sptText|
|||wird nicht erbracht|KGLListServiceProvision|spNotProvided|
|||keine Fremdvergabe||spNoExternalAssignment|
|||vollständige Fremdvergabe||spFullExternalAssignment|
|||teilweise Fremdvergabe||spPartialExternalAssignment|
|||Fremdvergebene Teilbereiche||spPartitionExternalAssignment|
|||Anmerkung||spNote|
|||Kostenvolumen||spAmount|
|Leistungsdokumentation (OP + AN)|Leistungsdokumentation||KGLOpAn||
|||Das Krankenhaus hat einen/mehrere Zentral-OPs:||oaHasCentralOP|
|||Wie viele Zentral-OPs hat das Krankenhaus?||oaCentralOPCnt|
|||ÄD||oaMedicalService|
|||FD/MTD||oaFunctionalService|
|||Beschreibung||oaDescription|
|||Summe der verwendeten Leistungsminuten der kalkulationsrelevanten Fälle ÄD||oaMedicalServiceAmount|
|||Summe der verwendeten Leistungsminuten der kalkulationsrelevanten Fälle FD/MTD||oaFunctionalServiceAmount|
|||Leistungsdokumentation Bezeichnung||oaContentTextID|
||||||
|Leistungsdokumentation (OP + Kreißsaal)|Top 3 ambulante Leistungen / Top 5 Geburtshilfen||KGLListKstTop||
|||Kostenstelle||ktCostCenterID|
|||Bezeichnung||ktText|
|||Fallzahl||ktCaseCnt|
|||Erlösvolumen||ktAmount|
|||abgegr. Kostenvolumen||ktDelimitationAmount|
|||Rang||ktRank|
|Leistungsdokumentation (Kreißsaal Gynäkologie)|Kreißsaal/Gynäkologie||KGLBaseInformation||
|||Das Krankenhaus erbringt Leistungen im Bereich der Gynäkologie.||biGynecology|
|||Das Krankenhaus erbringt Leistungen im Bereich Geburtshilfe.||biObstetrical|
|||Summe der Aufenthaltszeit der Patientin im Kreißsaal in Stunden||biDeliveryRoomHours|
|||davon für vorgeburtliche Kreißsaalaufenthalte||biDeliveryRoomPreBirthHabitationCnt|
|||Anzahl vollstationär geborener Kinder ||biDeliveryRoomInstationBirthCnt|
|||Aufenthaltszeiten der Patientin im Kreißsaal||biNoDeliveryRoomHabitation|
|||Bitte erläutern Sie (kurz) die Organisationsstrukturen in diesem Bereich||biDeliveryRoomOrganizationalStructure|
|Leistungsdokumentation (Kardiologie)|||KGLBaseInformation||
|||Das Krankenhaus erbringt Leistungen im Bereich der Kardiologie.||biCardiology|
|||Anzahl kardiologischer Eingriffsräume||biCardiologyRoomCnt|
|||Wieviele kalkulationsrelevante Fälle werden dort behandelt?||biCardiologyCaseCnt|
|Leistungsdokumentation (Endoskopie)|||KGLBaseInformation||
|||Das Krankenhaus erbringt Leistungen im Bereich der Endoskopie.||biEndoscopy|
|||Anzahl endoskopischer Eingriffsräume||biEndoscopyRoomCnt|
|||Wieviele kalkulationsrelevante Fälle werden dort behandelt?||biEndoscopyCaseCnt|
|MHI-Richtlinie|||KGLBaseInformation||
|||Das Krankenhaus führt minimalinvasive Herzklappeninterventionen durch||biMhi|
|||Erfüllt uneingeschränkt die Anforderungen||biMhiAbsolute|
|||Erfüllt gemäß der Übergangsregelung||biMhiTransitionalArrangement|
|||Erfüllt zum Stichtag die notwendigen Anforderungen der Richtlinie||biMhiGuideline|
|||Erfüllt im kommenden Datenjahr die notwendigen Anforderungen||biMhiGuidelineAspired|
|Leistungsdokumentation (Kreißsaal Gynäkologie)|||KGLListObstetricsGynecology||
|||Kostenstelle||ogCostCenterText|
|||"Ärztlicher 
Dienst"||ogMedicalServiceCnt|
|||"davon: 
Belegärzte "||ogAttendingDoctorCnt|
|||Pflegedienst||ogNursingServiceCnt|
|||"Funktions-
dienst (ohne Hebammen)"||ogFunctionalServiceCnt|
|||Hebammen||ogMidwifeCnt|
|||"davon: 
Beleg-Hebammen"||ogAttendingMidwifeCnt|
|Leistungsdokumentation(Endoskopie)|"endoskopischen Bereiche die nicht den Leistungsschlüssel "Eingriffszeit" verwenden""ndoskopischen Bereiche d"||KGLListEndoscopyDifferential||
|||Bereich||edDivision|
|||Leistungsschlüssel||edActivityKey|
|Leistungsdokumentation(Radiologie)|Ausgewählte Leistungen||KGLRadiologyService||
|||Bezeichnung||rsContentTextID|
|||OPS||rsOpsCode|
|||Kosten pro Leistung||rsServiceCost|
|||Fallzahl stationär||rsCaseCntStationary|
|||Fallzahl ambulant||rsCaseCntAmbulant|
|||abgegr. Kostenvolumen ambulant||rsAbulantAmount|
|Leistungsdokumentation(Radiologie + Laboratorien)|||KGLListRadiologyLaboratory||
|||Kostenstelle||rlCostCenterID|
|||Name Kostenstelle||rlCostCenterText|
|||Leistungsdokumentation||rlServiceDocumentation|
|||Beschreibung||rlDescription|
|||Leistungsvolumen vor Abgrenzung||rlServiceVolumePre|
|||Kostenvolumen vor Abgrenzung||rlAmountPre|
|||Leistungsvolumen nach Abgrenzung||rlServiceVolumePost|
|||Kostenvolumen nach Abgrenzung||rlAmountPost|
|Leistungsdokumentation (KST 11-13)|||KGLListCostCenter||
|||Kostenstelle||ccCostCenterID|
|||Name Kostenstelle||ccCostCenterText|
|||Kostenvolumen||ccAmount|
|||Anzahl zugeordenter Vollkräfte…||ccFullVigorCnt|
|||Leistungsschlüssel||ccServiceKey|
|||Beschreibung Leistungsschlüssel||ccServiceKeyDescription|
|||Summe Leistungsschlüssel||ccServiceSum|
|PK-Verrechnung|Gewähltes Verfahren für die Durchführung der Personalkostenverrechnung||KGLPersonalAccounting||
|||Kostenartengruppe||paCostTypeID|
|||Mitarbeiterbezogene Zeiterfassung||paStaffRecording|
|||Stellenplanauswertung||paStaffEvaluation|
|||Dienstplanauswertung||paServiceEvaluation|
|||Leistungsstatistiken||paServiceStatistic|
|||Expertenschätzung||paExpertRating|
|||Sonstige||paOther|
|||Kostenvolumen||paAmount|

[Zurück zum Hauptdokument](DataPortal.md#KGLTables)