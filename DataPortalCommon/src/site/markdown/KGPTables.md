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
|||Bereiche||spServiceProvisionTypeId|
|||wird nicht erbracht / keine Fremdvergabe / vollständige Fremdvergabe / teilweise Fremdvergabe||spProvidedTypeId|
|||Fremdvergebene Teilbereiche||spPartitionExternalAssignment|
|||Anmerkung||spNote|
|||Kostenvolumen||spAmount|
||||||
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
||||||
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
||||||
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
|Stationärer Bereich|||KGPBaseInformation||
|||Leistungsdokumentation gem. BIM||biBimAll|
|||Ausnahmegenehmigung für das Datenjahr (Kommentar)||biIntensiveExceptionalPermission|
|||Stichwortartig Kriterien Pflegetage||biIntensiveCriteriaBullets|
|||Stichwortartig Verfahren zur Verrechnung der Kosten||biIntensiveMethodBullets|
||||||
|Stationärer Bereich|Alternativverfahren||KGPListStationAlternative||
|||Fachabteilung||saDepartmentKey|
|||FAB Schlüssel 301||saDepartmentName|
|||Alternativverfahren||saAlternative|
||||||
|Stationärer Bereich|Leistungsdaten und Kosten in KstGr 21||KGPListStationServiceCost||
|||Kostenstelle||sscCostCenterID|
|||Nummer der Kostenstelle||sscCostCenterNumber|
|||Station||sscStation|
|||Anzahl Betten||sscBedCnt|
|||Bettenführende Aufnahmestation||sscReceivingStation|
|||Summe Pflegetage Regelbehandlung||sscRegularCareDays|
|||Summe Gewichtungspunkte Regelbehandlung||sscRegularWeight|
|||Summe Pflegetage Intensiv||sscIntensiveCareDays|
|||Summe Gewichtungspunkte Intensiv||sscIntensiveWeight|
|||Anzahl VK ÄD||sscMedicalServiceCnt|
|||Anzahl VK PD||sscNursingServiceCnt|
|||Anzahl VK Psychologen||sscPsychologistCnt|
|||Anzahl VK Sozialarbeiter||sscSocialWorkerCnt|
|||Anzahl VK Spezialtherapeuten||sscSpecialTherapistCnt|
|||Anzahl VK FD||sscFunctionalServiceCnt|
|||Kosten ÄD||sscMedicalServiceAmount|
|||Kosten PD||sscNursingServiceAmount|
|||Kosten Psychologen||sscPsychologistAmount|
|||Kosten Sozialarbeiter||sscSocialWorkerAmount|
|||Kosten Spezialtherapeuten||sscSpecialTherapistAmount|
|||Kosten FD||sscFunctionalServiceAmount|
|||Kosten med. Infra.||sscMedicalInfrastructureAmount|
|||Kosten nicht med. Infra.||sscNonMedicalInfrastructureAmount|
|||Zuordnung Psych-PV - Allgemeinpsychiatrie, Sucht, Kinder und Jugendpsychiatrie, Gerontopsychiatrie, Psychosomatik||sscPsyPvMapping|
||||||
|Med Infra|Gewähltes Verfahren||KGPBaseInformation||
|||Gleichungsverfahren, Stufenleiterverfahren, Anbauverfahren, Sonstige||biIBLVMethodMedInfra|
|||Erläuterung Sonstige Vorgehensweise||biOtherMethodMedInfra|
||||||
|Nicht Med Infra|Gewähltes Verfahren||KGPBaseInformation||
|||Gleichungsverfahren, Stufenleiterverfahren, Anbauverfahren, Sonstige||biIBLVMethodNonMedInfra|
|||Erläuterung Sonstige Vorgehensweise||biOtherMethodNonMedInfra|
||||||
|Med Infra/Nicht Med Infra|Verrechnungsschlüssel und Kostenvolumen||KGPListMedInfra||
|||Kostenartengruppe||miCostTypeID|
|||Nummer der Kostenstelle||miCostCenterNumber|
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