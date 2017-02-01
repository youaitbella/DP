Die Basisdateien der nachfolgenden Struktur liegen unter W:\Oekonomie\KGP\Anpassung KGP DJ2016\Übergabe an EDV

|Blatt|Tabelle|Spalte|Tabelle DB|Spalte DB|
|---|---|---|---|---|
|Grundlagen|Grundlagen||KGPBaseInformation||
|||Summe kalkulationsrelevanter Kosten (EUR)||biSumCalcCost|
|||Fälle vollstationär||biCaseInStationCnt|
|||Fälle vollstationär Psychosomatik||biCaseInStationCntPsy|
|||Pflegetage vollstationär||biCareDaysInStationCnt|
|||Pflegetage vollstationär Psychosomatik||biCareDaysInStationCntPsy|
|||Fälle teilstationär||biCasePartialStationCnt|
|||Fälle teilstationär Psychosomatik||biCasePartialStationCntPsy|
|||Berechnungstage teilstationär||biDaysPartialStation|
|||Berechnungstage teilstationär Psychosomatik||biDaysPartialStationPsy|
|||Begleitpersonen||biPatientEscort|
|||rein vorstationär||biPreStation|
|||Betten (DRG-Bereich)||biBeds|
|||Anzahl teilstationäre Plätze (DRG-Bereich)||biPartialCnt|
|||Anzahl Standorte||biLocationCnt|
|||Differenzierter Versorgungsvertrag||biDifLocationSupply|
|||Einzelkostenzuordnung für Arzneimittel||biMedicineCostMapping|
|||Wurden Patienten gerichtlich untergebracht?||biCourtPlacement|
|||Daten in ergänzende Datenbereitstellung aufbereiten||biAdditionalDataAllocation|
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
|Leistungsdokumentation|(Externe) Leistungserbringung||KGPListServiceProvision||
|||Bereiche||spServiceProvisionTypeID|
|||wird nicht erbracht / keine Fremdvergabe / vollständige Fremdvergabe / teilweise Fremdvergabe||spProvidedTypeID|
|||Fremdvergebene Teilbereiche||spPartitionExternalAssignment|
|||Anmerkung||spNote|
|||Kostenvolumen||spAmount|
|Leistungsdokumentation|Therapeutischer Bereich||KGPListTherapy||
|||thCostCenterID||Kostenstellen Gruppe|
|||thCostCenterText||Leistungsinhalt der Kostenstelle|
|||thExternalService||externe Leistungserbringung|
|||thKeyUsed||verwendeter Leistungsschlüssel|
|||thServiceUnitsCt1||Summe Leistungseinheiten KoArtGr 1|
|||thPersonalCostCt1||Personalkosten KoArtGr 1|
|||thServiceUnitsCt2||Summe Leistungseinheiten KoArtGr 2|
|||thPersonalCostCt2||Personalkosten KoArtGr 2|
|||thServiceUnitsCt3||Summe Leistungseinheiten KoArtGr 3|
|||thPersonalCostCt3||Personalkosten KoArtGr 3|
|||thServiceUnitsCt3a||Summe Leistungseinheiten KoArtGr 3a|
|||thPersonalCostCt3a||Personalkosten KoArtGr 3a|
|||thServiceUnitsCt3b||Summe Leistungseinheiten KoArtGr 3b|
|||thPersonalCostCt3b||Personalkosten KoArtGr 3b|
|||thServiceUnitsCt3c||Summe Leistungseinheiten KoArtGr 3c|
|||thPersonalCostCt3c||Personalkosten KoArtGr 3c|
|Leistungsdokumentation|Radiologie + Laboratorien||KGPListRadiologyLaboratory||
|||Kostenstelle||rlCostCenterID|
|||Nummer der Kostenstelle||rlCostCenterNumber|
|||Name Kostenstelle||rlCostCenterText|
|||Leistungsdokumentation Hauskatalog*||rlServiceDocHome|
|||Leistungsdokumentation DKG-NT||rlServiceDocDKG|
|||Leistungsdokumentation EBM||rlServiceDocEBM|
|||Leistungsdokumentation GOÄ||rlServiceDocGOA|
|||Leistungsdokumentation sonstige*||rlServiceDocDif|
|||Beschreibung||rlDescription|
|Leistungsdokumentation|KST 11-13||KGPListCostCenter||
|||Kostenstelle||ccCostCenterID|
|||Nummer der Kostenstelle||ccCostCenterNumber|
|||Name der Kostenstelle||ccCostCenterText|
|||Kostenvolumen||ccAmount|
|||Anzahl zugeordenter Vollkräfte…||ccFullVigorCnt|
|||Leistungsschlüssel||ccServiceKey|
|||Beschreibung Leistungsschlüssel||ccServiceKeyDescription|
|||Summe Leistungseinheiten||ccServiceSum|
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