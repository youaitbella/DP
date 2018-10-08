/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.enums;

/**
 *
 * @author muellermi
 */
public enum StructureInformationCategorie {
    BedCount("Allgemeine Angaben", "Planbetten", 2),
    TherapyPartCount("Allgemeine Angaben", "Anzahl teilstationärer Therapieplätze", 2),
    RegionalCare("Regionale Versorgungsverpflichtung",
            "Stichpunktartige Beschreibung der strukturellen und personellen Besonderheiten duch die regionalen Versorgungsverpflichtung", 1),
    AccommodationText("Besonderheiten bei der gesetzlichen Unterbringung",
            "Beschreibung der strukturellen und personellen Besonderheiten für patienten mit gesetzlicher Unterbringung", 1),
    SPCenterText("Sozialpädiatrisches Zentrum gem. § 119 SGB V am Krankenhaus vorhanden", "Leistungsangebot und Leistungsschwerpunkte", 1),
    AmbulantPerformanceMain("Ambulante Leistungserbringung des Krankenhauses", "Angabe der Leistungsschwerpunkte", 1),
    DismissManagement("Leistungen im Rahmen des Entlassmanagements", "Leistungsschwerpunkte", 1),
    CareProvider("Vorliegen und Struktur eines gemeindepsychiatrischen Verbunds", "Wesentliche eingebundene Leistungserbringer", 2),
    PerformanceAreas("Vorliegen und Struktur eines gemeindepsychiatrischen Verbunds", "Zentrale Leistungsbereiche", 2),
    AmbulantStructure("Ambulante Infrastruktur im Umfeld des Krankenhauses",
            "Beschreibung der örtlichen Versorgungssituation im vertragsärztlichen/therapeutischen Bereich", 1),
    SocialPsychiatryService("Umfang und Struktur der Sozialpsychiatrischen Dienste", "Beschreibung", 1);

    StructureInformationCategorie(String area, String description, int countElements) {
        _area = area;
        _description = description;
        _countElements = countElements;
    }

    private final String _area;

    public String getArea() {
        return _area;
    }

    private final String _description;

    public String getDescription() {
        return _description;
    }

    private final int _countElements;

    public int getCountElements() {
        return _countElements;
    }
}
