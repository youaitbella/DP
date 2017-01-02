Die Basisdateien der nachfolgenden Struktur liegen unter W:\Oekonomie\KGL\Anpassung KGL DJ2016\Übergabe an EDV

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
|Neonatologie||Levels der Versorgungsstufe des Perinatalzentrums|KGLBaseInformation|biNeonatLvl|
|Neonatologie|Qualifikationen gem. Richtlinie||KGLNeonatData||
|||Qualifikation||ndContentTextID|
|||Erfüllt (Datum)/Anzahl/Kosten||ndData|
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
|||Das Krankenhaus führt minimalinvasive Herzklappeninterventionen durch||biMinimalValvularIntervention|
|||Erfüllungsgrad||biMviFulfilled|
|||Erfüllt im kommenden Datenjahr die notwendigen Anforderungen||biMviGuidelineAspired|
|Leistungsdokumentation (Kreißsaal Gynäkologie)|||KGLListObstetricsGynecology||
|||Kostenstelle||ogCostCenterText|
|||Ärztlicher Dienst||ogMedicalServiceCnt||||davon: Belegärzte||ogAttendingDoctorCnt|
|||Pflegedienst||ogNursingServiceCnt||||Funktionsdienst (ohne Hebammen)||ogFunctionalServiceCnt|
|||Hebammen||ogMidwifeCnt||||davon: Beleg-Hebammen||ogAttendingMidwifeCnt|
|Leistungsdokumentation(Endoskopie)|endoskopischen Bereiche die nicht den Leistungsschlüssel "Eingriffszeit" verwenden||KGLListEndoscopyDifferential||
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
|Normalstation|||KGLBaseInformation||
|||Ärzte freie Mitarbeit||biNormalFreelancing|
|||Honorarverträge||biFeeContract|
|||Erfassung PKMS Normalstation||biPKMSRecording|
|||Anzahl kalkulierter Fälle mit PKMS||biPKMSCaseCnt|
|||KIS-Integration/Manuell||biKISIntegration|
|||Sonstiges||biNormalStationOther|
|Normalstation|Hochaufwendige Pflegeleistungen (Alternativen)||KGLPKMSAlternative||
|||Fachabteilung||paDepartment|
|||FAB Schlüssel 301||paDepartmentKey|
|||Alternativverfahren||paAlternative|
|Normalstation|Leistungsdokumentation für die Kostenartengruppen 2, 4a und 6a||KGLNormalStationServiceDocumentation||
|||Ja/Nein||nssUsed|
|||Fachabteilung||nssDepartment|
|||FAB Schlüssel 301||nssDepartmentKey|
|||Alternativverfahren||nssAlternative|
|Normalstation|Ärzte in freier Mitarbeit||KGLNormalFreelancer||
|||Bereich||nfDivision|
|||AnzahlVK||nfFullVigorCnt|
|||Kostenvolumen||nfAmount|
|||KoArtGr 1||nfCostType1|
|||KoArtGr 6c||nfCostType6c|
|Normalstation|Honorarverträge||KGLNormalFeeContract||
|||Bereich||nfcDivision|
|||FAB Schlüssel 301||nfcDepartmentKey|
|||Anzahl Fälle||nfcCaseCnt|
|||abgegr. Kostenvolumen||nfcAmount|
|Normalstation|Kosten KstGr 1||KGLListCostCenterCost||
|||Nummer der Kostenstelle||cccCostCenter|
|||Name der Kostenstelle||cccCostCenterText|
|||FAB Schlüssel 301||cccDepartmentKey|
|||Belegung: FAB||cccDepartmentAssignment|
|||Bettenzahl||cccBedCnt|
|||Pflegetage||cccCareDays|
|||PPR-Minuten||cccPPRMinutes|
|||zusätzliche Gewichtung PPR||cccPPRWeight|
|||Anz. VK ÄD||cccMedicalServiceCnt|
|||Anz. VK PD||cccNursingServiceCnt|
|||Anz. VK FD||cccFunctionalServiceCnt|
|||Kosten ÄD||cccMedicalServiceAmount|
|||Kosten PD||cccNursingServiceAmount|
|||Kosten FD||cccFunctionalServiceAmount|
|||Gemeinkosten Arzneimittel||cccOverheadsMedicine|
|||Gemeinkosten Sachbedarf||cccOverheadsMedicalGoods|
|||Kosten MedInfra||cccMedicalInfrastructureCost|
|||Kosten nicht MedInfra||cccNonMedicalInfrastructureCost|
|Med Infra|Gewähltes Verfahren||KGLBaseInformation||
|||Gleichungsverfahren||biApproximationMethodMedInfra|
|||Stufenleiterverfahren||biStepladderMethodMedInfra|
|||Anbauverfahren||biExtensionMethodMedInfra|
|||Sonstige Vorgehensweise||biOtherMethodMedInfra|
|Nicht Med Infra|Gewähltes Verfahren||KGLBaseInformation||
|||Gleichungsverfahren||biApproximationMethodNonMedInfra|
|||Stufenleiterverfahren||biStepladderMethodNonMedInfra|
|||Anbauverfahren||biExtensionMethodNonMedInfra|
|||Sonstige Vorgehensweise||biOtherMethodNonMedInfra|
|Med Infra/Nicht Med Infra|Verrechnungsschlüssel und Kostenvolumen||KGLListMedInfra||
|||Kostenartengruppe||miCostTypeID|
|||Nummer der Kostenstelle||miCostCenter|
|||Name der Kostenstelle||miCostCenterText|
|||Verwendeter Schlüssel||miKeyUsed|
|||Kostenvolumen||miAmount|
|Intensiv_Stroke (Intensiv)|||KGLBaseInformation||
|||Intensivbetten||biIntensiveBed|
|Intensiv_Stroke (Stroke Unit)|||KGLBaseInformation||
|||Intensivbetten Schlaganfall||biIntensiveStrokeBed|
|Intensiv_Stroke|||KGLListIntensivStroke||
|||IntensivTyp zur Unterscheidung Intensiv/Stroke||isIntensiveType|
|||Nummer Kostenstelle||isCostCenterID|
|||Name Kostenstelle||isCostCenterText|
|||FAB Schlüssel||isDepartmentKey|
|||Belegung: FAB||isDepartmentAssignment|
|||Anzahl Betten||isBedCnt|
|||Anzahl Fälle||isCaseCnt|
|||OPS 8-980||isOPS8980|
|||OPS 8-98f||isOPS898f|
|||OPS 8-981 (Stroke)||isOPS8981|
|||OPS 8-98b (Stroke)||isOPS898b|
|||Mindestmerkmale Zeitabschnitt||isMinimumPeriod|
|||Summe gew. Intensiv Std.||isIntensivHoursWeighted|
|||Summe ungew. Intensiv Std.||isIntensivHoursNotweighted|
|||Gewichtungsfaktor Min.||isWeightMinimum|
|||Gewichtungsfaktor Max.||isWeightMaximum|
|||Gewichtungsfaktor Erläuterung||isWeightDescription|
|Intensiv_Stroke Kosten|||KGLListIntensiveStrokeCost||
|||IntensivTyp zur Unterscheidung Intensiv/Stroke||iscIntensiveType|
|||Nummer Kostenstelle||iscCostCenterID|
|||Name Kostenstelle||iscCostCenterText|
|||FAB Schlüssel||iscDepartmentKey|
|||Belegung: FAB||iscDepartmentAssignment|
|||Anzahl VK ÄD||iscMedicalServiceCnt|
|||Anzahl VK PD||iscNursingServiceCnt|
|||Anzahl VK FD||iscFunctionalServiceCnt|
|||Kosten ÄD||iscMedicalServiceCost|
|||Kosten PD||iscNursingServiceCost|
|||Kosten FD||iscFunctionalServiceCost|
|||Gemeinkosten Arzneimittel||iscOverheadsMedicine|
|||Gemeinkosten med. Sachbedarf||iscOverheadMedicalGoods|
|||Kosten med. Infra||iscMedicalInfrastructureCost|
|||Kosten nicht med. Infra||iscNonMedicalInfrastructureCost|


[Zurück zum Hauptdokument](DataPortal.md#KGLTables)