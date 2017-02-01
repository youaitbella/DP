Die Basisdateien der nachfolgenden Struktur liegen unter W:\Oekonomie\KGP\Anpassung KGP DJ2016\Übergabe an EDV

|Blatt|Tabelle|Spalte|Tabelle DB|Spalte DB|
|---|---|---|---|---|
|Grundlagen|Grundlagen||KGPBaseInformation||
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
||||||
|Grundlagen|Entlassender Standort||KGPListLocation||
|||Entlassender Standort||lLocation|
|||Nummer||lLocationNo|
||||||
|Grundlagen|Berücksichtigte Abgrenzungstatbestände||KGPListDelimitationFact||
|||Text (Label)||dfContentTextID|
|||bitte markieren||dfUsed|
|||Personalkosten||dfPersonalCost|
|||Sachkosten||dfMaterialcost|
|||Infrastrukturkosten||dfInfraCost|
||||||
|Leistungsdokumentation (ext. Lei)|||||
|||Bereich|KGPListServiceProvisionType|sptText|
|||wird nicht erbracht|KGPListServiceProvision|spNotProvided|
|||keine Fremdvergabe||spNoExternalAssignment|
|||vollständige Fremdvergabe||spFullExternalAssignment|
|||teilweise Fremdvergabe||spPartialExternalAssignment|
|||Fremdvergebene Teilbereiche||spPartitionExternalAssignment|
|||Anmerkung||spNote|
|||Kostenvolumen||spAmount|
|Leistungsdokumentation(Therapeutischer Bereich)|||KGPListTherapy||
|||thxxxx||thxxxx|
|Leistungsdokumentation(Radiologie + Laboratorien)|||KGPListRadiologyLaboratory||
|||Kostenartengruppe||rlCostTypeID|
|||Kostenstelle||rlCostCenterID|
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
|Leistungsdokumentation (KST 11-13)|||KGPListCostCenter||
|||Kostenstelle||ccCostCenterID|
|||Name Kostenstelle||ccCostCenterText|
|||Kostenvolumen||ccAmount|
|||Anzahl zugeordenter Vollkräfte…||ccFullVigorCnt|
|||Leistungsschlüssel||ccServiceKey|
|||Beschreibung Leistungsschlüssel||ccServiceKeyDescription|
|||Summe Leistungsschlüssel||ccServiceSum|
||||||
|Normalstation|||KGPBaseInformation||
|||Ärzte freie Mitarbeit||biNormalFreelancing|
|||Honorarverträge||biFeeContract|
|||Anzahl kalkulierter Fälle mit PKMS||biPKMSCaseCnt|
|||Erfassung PKMS Normalstation und KIS-Integration/Manuell||biPKMSRecording|
|||Sonstiges||biPKMSOther|
|Normalstation|Hochaufwendige Pflegeleistungen (Alternativen)||KGPPKMSAlternative||
|||Fachabteilung||paDepartment|
|||FAB Schlüssel 301||paDepartmentKey|
|||Alternativverfahren||paAlternative|
|Normalstation|Leistungsdokumentation für die Kostenartengruppen 2, 4a und 6a||KGPNormalStationServiceDocumentation||
|||Ja/Nein||nssUsed|
|||Fachabteilung||nssDepartment|
|||FAB Schlüssel 301||nssDepartmentKey|
|||Alternativverfahren||nssAlternative|
|Normalstation|Ärzte in freier Mitarbeit||KGPNormalFreelancer||
|||Bereich||nfDivision|
|||AnzahlVK||nfFullVigorCnt|
|||Kostenvolumen||nfAmount|
|||KoArtGr 1||nfCostType1|
|||KoArtGr 6c||nfCostType6c|
|Normalstation|Honorarverträge||KGPNormalFeeContract||
|||Bereich||nfcDivision|
|||FAB Schlüssel 301||nfcDepartmentKey|
|||Anzahl Fälle||nfcCaseCnt|
|||abgegr. Kostenvolumen||nfcAmount|
|Normalstation|Kosten KstGr 1||KGPListCostCenterCost||
|||Nummer der Kostenstelle||cccCostCenter|
|||Name der Kostenstelle||cccCostCenterText|
|||Zugeordnete Kostenstellengruppe||cccCostTypeID|
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
||||||
|Med Infra|Gewähltes Verfahren||KGPBaseInformation||
|||Gleichungsverfahren||biApproximationMethodMedInfra|
|||Stufenleiterverfahren||biStepladderMethodMedInfra|
|||Anbauverfahren||biExtensionMethodMedInfra|
|||Sonstige Vorgehensweise||biOtherMethodMedInfra|
|Nicht Med Infra|Gewähltes Verfahren||KGPBaseInformation||
|||Gleichungsverfahren||biApproximationMethodNonMedInfra|
|||Stufenleiterverfahren||biStepladderMethodNonMedInfra|
|||Anbauverfahren||biExtensionMethodNonMedInfra|
|||Sonstige Vorgehensweise||biOtherMethodNonMedInfra|
|Med Infra/Nicht Med Infra|Verrechnungsschlüssel und Kostenvolumen||KGPListMedInfra||
|||Kostenartengruppe||miCostTypeID|
|||Nummer der Kostenstelle||miCostCenter|
|||Name der Kostenstelle||miCostCenterText|
|||Verwendeter Schlüssel||miKeyUsed|
|||Kostenvolumen||miAmount|
||||||
|PK-Verrechnung|Gewähltes Verfahren für die Durchführung der Personalkostenverrechnung||KGPPersonalAccounting||
|||Kostenartengruppe||paCostTypeID|
|||Mitarbeiterbezogene Zeiterfassung||paStaffRecording|
|||Stellenplanauswertung||paStaffEvaluation|
|||Dienstplanauswertung||paServiceEvaluation|
|||Leistungsstatistiken||paServiceStatistic|
|||Expertenschätzung||paExpertRating|
|||Sonstige||paOther|
|||Kostenvolumen||paAmount|


[Zurück zum Hauptdokument](DataPortal.md#KGPTables)