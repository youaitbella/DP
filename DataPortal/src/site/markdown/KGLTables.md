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
|||Text (Label)||dfContentTextID|
|||bitte markieren||dfUsed|
|||Personalkosten||dfPersonalCost|
|||Sachkosten||dfMaterialcost|
|||Infrastrukturkosten||dfInfraCost|
|Neonatologie||Levels der Versorgungsstufe des Perinatalzentrums|KGLBaseInformation|biNeonatLvl|
|Neonatologie|Qualifikationen gem. Richtlinie||KGLNeonatData||
|||Qualifikation||ndContentTextID|
|||Erfüllt (Datum)/Anzahl/Kosten||ndData|
|Leistungsdokumentation (ext. Lei)|||||
|||Bereich|KGLListServiceProvision|sptText|
|||wird nicht erbracht, keine Fremdvergabe, vollständige Fremdvergabe, teilweise Fremdvergabe||spProvidedType|
|||Fremdvergebene Teilbereiche||spPartitionExternalAssignment|
|||Anmerkung||spNote|
|||Kostenvolumen||spAmount|
|Leistungsdokumentation (OP + AN)|Leistungsdokumentation||KGLOpAn||
|||Wie viele Zentral-OPs hat das Krankenhaus?||oaCentralOPCnt|
|||Personalbindungszeit ÄD (OP)||oaStaffBindingMsOP|
|||Personalbindungszeit FD (OP)||oaStaffBindingFsOP|
|||SNZ ÄD (OP)||oaMedicalServiceSnzOP|
|||SNZ FD (OP)||oaFunctionalServiceSnzOP|
|||SNZ Alternative (OP)||oaDescriptionSnzOP|
|||Rüstzeit ÄD (OP)||oaMedicalServiceRzOP|
|||Rüstzeit FD (OP)||oaFunctionalServiceRzOP|
|||Rüstzeit Alternative (OP)||oaDescriptionRzOP|
|||Summe der verwendeten Leistungsminuten der kalkulationsrelevanten Fälle ÄD (OP)||oaMedicalServiceAmountOP|
|||Summe der verwendeten Leistungsminuten der kalkulationsrelevanten Fälle FD/MTD (OP)||oaFunctionalServiceAmountOP|
|||Personalbindungszeit ÄD (AN)||oaStaffBindingMsAN|
|||Personalbindungszeit FD (AN)||oaStaffBindingFsAN|
|||SNZ ÄD (AN)||oaMedicalServiceSnzAN|
|||SNZ FD (AN)||oaFunctionalServiceSnzAN|
|||SNZ Alternative (AN)||oaDescriptionSnzAN|
|||Rüstzeit ÄD (AN)||oaMedicalServiceRzAN|
|||Rüstzeit FD (AN)||oaFunctionalServiceRzAN|
|||Rüstzeit Alternative (AN)||oaDescriptionRzAN|
|||Summe der verwendeten Leistungsminuten der kalkulationsrelevanten Fälle ÄD (AN)||oaMedicalServiceAmountAN|
|||Summe der verwendeten Leistungsminuten der kalkulationsrelevanten Fälle FD/MTD (AN)||oaFunctionalServiceAmountAN|
||||||
|Leistungsdokumentation (OP + Kreißsaal)|Top 3 ambulante Leistungen / Top 5 Geburtshilfen||KGLListKstTop||
|||Kostenstelle||ktCostCenterId|
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
|||Bei vorgeburtlichen Fällen keine Aufenthaltszeiten der Patientin im Kreißsaal||biNoDeliveryRoomHabitation|
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
|Leistungsdokumentation (Kreißsaal)|Geburtshilfe/Gynäkologie||KGLListObstetricsGynecology||
|||Kostenstelle||ogCostCenterText|
|||Zuordnung Kostenstellengruppe||ogCostTypeID|
|||Ärztlicher Dienst||ogMedicalServiceCnt|
|||davon: Belegärzte||ogAttendingDoctorCnt|
|||Pflegedienst||ogNursingServiceCnt|
|||Funktionsdienst (ohne Hebammen)||ogFunctionalServiceCnt|
|||Hebammen||ogMidwifeCnt|
|||davon: Beleg-Hebammen||ogAttendingMidwifeCnt|
|Leistungsdokumentation(Endoskopie)|endoskopischen Bereiche die nicht den Leistungsschlüssel "Eingriffszeit" verwenden||KGLListEndoscopyDifferential||
|||Bereich||edDivision|
|||Leistungsschlüssel||edActivityKey|
|Leistungsdokumentation(Radiologie)|Ausgewählte Leistungen||KGLRadiologyService||
|||Bezeichnung||rsContentTextID|
|||OPS||rsOpsCode|
|||Kosten pro Leistung||rsServiceCost|
|||Fallzahl stationär||rsCaseCntStationary|
|||Fallzahl ambulant||rsCaseCntAmbulant|
|||abgegr. Kostenvolumen ambulant||rsAmbulantAmount|
|Leistungsdokumentation(Radiologie + Laboratorien)|||KGLListRadiologyLaboratory||
|||Kostenstelle||rlCostCenterID|
|||Nummer der Kostenstelle||rlCostCenterNumber|
|||Name Kostenstelle||rlCostCenterText|
|||Leistungsdokumentation Hauskatalog*||rlServiceDocHome|
|||Leistungsdokumentation DKG-NT||rlServiceDocDKG|
|||Leistungsdokumentation EBM||rlServiceDocEBM|
|||Leistungsdokumentation GOÄ||rlServiceDocGOA|
|||Leistungsdokumentation sonstige*||rlServiceDocDif|
|||Beschreibung||rlDescription|
|||Leistungsvolumen vor Abgrenzung||rlServiceVolumePre|
|||Kostenvolumen vor Abgrenzung||rlAmountPre|
|||Leistungsvolumen nach Abgrenzung||rlServiceVolumePost|
|||Kostenvolumen nach Abgrenzung||rlAmountPost|
|Leistungsdokumentation (KST 11-13)|||KGLListCostCenter||
|||Kostenstelle||ccCostCenterID|
|||Name der Kostenstelle||ccCostCenterText|
|||Nummer der Kostenstelle||ccCostCenterNumber|
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
|||Anzahl kalkulierter Fälle mit PKMS||biPKMSCaseCnt|
|||Erfassung PKMS Normalstation und KIS-Integration/Manuell||biPKMSRecording|
|||Sonstiges||biPKMSOther|
|||Kalkulierte Fälle mit PKMS||biPKMComplex|
|Normalstation|Hochaufwendige Pflegeleistungen (Alternativen)||KGLPKMSAlternative||
|||Fachabteilung||paDepartment|
|||FAB Schlüssel 301||paDepartmentKey|
|||Alternativverfahren||paAlternative|
|Normalstation|Leistungsdokumentation für die Kostenartengruppen 2, 4a und 6a||KGLNormalStationServiceDocumentation||
|||Wert||nssContentTextID|
|||Ja/Nein||nssUsed|
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
|||Zugeordnete Kostenstellengruppe||cccCostCenterID|
|||Nummer der Kostenstelle||cccCostCenterNumber|
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
|Med Infra|Gewähltes Verfahren IBLV||KGLBaseInformation||
|||Gleichungsverfahren, Stufenleiterverfahren, Anbauverfahren, Sonstige||biIBLVMethodMedInfra|
|||Erläuterung Sonstige Vorgehensweise||biOtherMethodMedInfra|
|Nicht Med Infra|Gewähltes Verfahren||KGLBaseInformation||
|||Gleichungsverfahren, Stufenleiterverfahren, Anbauverfahren, Sonstige||biIBLVMethodMedInfra|
|||Erläuterung Sonstige Vorgehensweise||biOtherMethodMedInfra|
|Med Infra/Nicht Med Infra|Verrechnungsschlüssel und Kostenvolumen||KGLListMedInfra||
|||Kostenartengruppe||miCostTypeID|
|||Nummer der Kostenstelle||miCostCenterNumber|
|||Name der Kostenstelle||miCostCenterText|
|||Verwendeter Schlüssel||miKeyUsed|
|||Kostenvolumen||miAmount|
|Intensiv_Stroke (Intensiv)|||KGLBaseInformation||
|||Intensivbetten||biIntensiveBed|
|Intensiv_Stroke (Stroke Unit)|||KGLBaseInformation||
|||Intensivbetten Schlaganfall||biIntensiveStrokeBed|
|Intensiv_Stroke|||KGLListIntensivStroke||
|||IntensivTyp zur Unterscheidung Intensiv/Stroke||isIntensiveType|
|||Name Kostenstelle||isCostCenterText|
|||Belegung: FAB||isDepartmentAssignment|
|||Anzahl Betten||isBedCnt|
|||Anzahl Fälle||isCaseCnt|
|||OPS 8-980||isOPS8980|
|||OPS 8-98f||isOPS898f|
|||OPS 8-981 (Stroke)||isOPS8981|
|||OPS 8-98b (Stroke)||isOPS898b|
|||Mindestmerkmale Zeitabschnitt||isMinimumCriteriaPeriod|
|||Summe gew. Intensiv Std.||isIntensivHoursWeighted|
|||Summe ungew. Intensiv Std.||isIntensivHoursNotweighted|
|||Gewichtungsfaktor Min.||isWeightMinimum|
|||Gewichtungsfaktor Max.||isWeightMaximum|
|||Gewichtungsfaktor Erläuterung||isWeightDescription|
|||Anzahl VK ÄD||isMedicalServiceCnt|
|||Anzahl VK PD||isNursingServiceCnt|
|||Anzahl VK FD||isFunctionalServiceCnt|
|||Kosten ÄD||isMedicalServiceCost|
|||Kosten PD||isNursingServiceCost|
|||Kosten FD||isFunctionalServiceCost|
|||Gemeinkosten Arzneimittel||isOverheadsMedicine|
|||Gemeinkosten med. Sachbedarf||isOverheadsMedicalGoods|
|||Kosten med. Infra||isMedicalInfrastructureCost|
|||Kosten nicht med. Infra||isNonMedicalInfrastructureCost|


[Zurück zum Hauptdokument](DataPortal.md#KGLTables)