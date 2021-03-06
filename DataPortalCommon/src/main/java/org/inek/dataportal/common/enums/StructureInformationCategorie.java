/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.enums;

/**
 * @author muellermi
 */
public enum StructureInformationCategorie {
    BedCount("Allgemeine Angaben", "Anzahl Planbetten (vollstationär)"),
    TherapyPartCount("Allgemeine Angaben", "Anzahl Therapieplätze (teilstationär)"),
    ModelIntention("Allgemeine Angaben", "Modellvorhaben zur Versorgung psychisch kranker Menschen gem. § 64b SGB V"),
    RegionalCare("Regionale Versorgungsverpflichtung",
            "Stichpunktartige Beschreibung der strukturellen und personellen Besonderheiten duch die regionalen Versorgungsverpflichtung"),
    AccommodationText("Besonderheiten bei der gesetzlichen Unterbringung",
            "Beschreibung der strukturellen und personellen Besonderheiten für Patienten mit gesetzlicher Unterbringung"),
    SPCenterText("Sozialpädiatrisches Zentrum gem. § 119 SGB V am Krankenhaus vorhanden", "Leistungsangebot und Leistungsschwerpunkte"),
    AmbulantPerformanceMain("Ambulante Leistungserbringung des Krankenhauses", "Angabe der Leistungsschwerpunkte"),
    DismissManagement("Leistungen im Rahmen des Entlassmanagements", "Leistungsschwerpunkte"),
    CareProvider("Vorliegen und Struktur eines gemeindepsychiatrischen Verbunds", "Wesentliche eingebundene Leistungserbringer"),
    PerformanceAreas("Vorliegen und Struktur eines gemeindepsychiatrischen Verbunds", "Zentrale Leistungsbereiche"),
    AmbulantStructure("Ambulante Infrastruktur im Umfeld des Krankenhauses",
            "Beschreibung der örtlichen Versorgungssituation im vertragsärztlichen/therapeutischen Bereich"),
    SocialPsychiatryService("Umfang und Struktur der Sozialpsychiatrischen Dienste", "Beschreibung"),
    Other("Sonstiges", "Hier können Sie weitere Bereiche im Umfeld Ihres Hauses beschreiben, " +
            "die Ihrer Meinung nach Einfluss auf die Kostenstruktur Ihrer Einrichtung haben"),;

    private final String _area;
    private final String _description;

    StructureInformationCategorie(String area, String description) {
        _area = area;
        _description = description;
    }

    public String getArea() {
        return _area;
    }

    public String getDescription() {
        return _description;
    }

}
