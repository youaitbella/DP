package org.inek.dataportal.psy.nub.helper;

import org.inek.dataportal.psy.nub.entities.PsyNubRequest;
import org.inek.dataportal.psy.nub.enums.PsyNubDateFields;
import org.inek.dataportal.psy.nub.enums.PsyNubMoneyFields;
import org.inek.dataportal.psy.nub.enums.PsyNubNumberFields;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PsyNubRequestChecker implements Serializable {

    private static Integer[] VALID_GENDERS = new Integer[]{1, 2};

    private static String FIELD_IK = "Institutskennzeichen";

    private static String FIELD_FIRSTNAME = "Vorname";
    private static String FIELD_LASTNAME = "Nachname";
    private static String FIELD_GENDER = "Gender";
    private static String FIELD_STREET = "Straße";
    private static String FIELD_POSTCODE = "Postleitzahl";
    private static String FIELD_TOWN = "Stadt";
    private static String FIELD_PHONE = "Telefon";
    private static String FIELD_EMAIL = "Email";
    private static String FIELD_HOSPITALNAME = "Krankenhausname";

    private static String FIELD_NUB_NAME = "Name der NUB";
    private static String FIELD_NUB_DESCRIPTION = "Beschreibung der NUB";
    private static String FIELD_NUB_OPS = "OPS der NUB";

    private static String FIELD_NUB_ANY_ADD_COST = "Mehrkosten in den Bereichen Personalkosten, Sachkosten oder sonstige Kosten";

    private static String FIELD_NUB_ADD_COST_PERSONAL = "Kommentar zu den Mehrkosten in dem Bereich Personal";
    private static String FIELD_NUB_ADD_COST_MATERIAL = "Kommentar zu den Mehrkosten in dem Bereich Sachkosten";
    private static String FIELD_NUB_ADD_COST_OTHER = "Kommentar zu den Mehrkosten in dem Bereich Sonstige Kosten";

    private static String FIELD_NUB_LESS_COST_PERSONAL = "Kommentar zu den Kosteneinsparungen in dem Bereich Personal";
    private static String FIELD_NUB_LESS_COST_MATERIAL = "Kommentar zu den Kosteneinsparungen in dem Bereich Sachkosten";
    private static String FIELD_NUB_LESS_COST_OTHER = "Kommentar zu den Kosteneinsparungen in dem Bereich Sonstige Kosten";

    private static String FIELD_NUB_INDICATORS = "Indikation der NUB";
    private static String FIELD_NUB_REPLACEMENT = "Welche Methode wird durch die Anfrage abgelößt oder ergänzt";
    private static String FIELD_NUB_WHATS_NEW = "Warum handelt es sich um eine neue Methode";
    private static String FIELD_NUB_WHY_NOT_IN_PEPP = "Warum ist die NUB nicht im PEPP System abgebildet";

    private static String FIELD_NUB_IN_HOSPITAL = "Einführung im Krankenhaus";

    private static String FIELD_NUB_PATIENTS_TARGETYEAR = "Anzahl Patienten im Zieljahr";
    private static String FIELD_NUB_PATIENTS_PRE_TARGETYEAR = "Anzahl Patienten im aktuellem Jahr";
    private static String FIELD_NUB_PATIENTS_PRE_PRE_TARGETYEAR = "Anzahl Patienten im Vorjahr";

    public static List<String> checkPsyRequestForSend(PsyNubRequest request) {
        List<String> errorMessages = new ArrayList<>();
        checkMasterData(request, errorMessages);
        checkProposalData(request, errorMessages);
        checkDateValues(request, errorMessages);
        checkNumberValues(request, errorMessages);
        checkMoneyValues(request, errorMessages);
        return errorMessages;
    }

    private static void checkMoneyValues(PsyNubRequest request, List<String> errorMessages) {
        if (hasAnyAddMoneyValue(request, errorMessages)) {
            if (request.getMoneyValue(PsyNubMoneyFields.ADD_COSTS_PERSONAL).getMoney() > 0) {
                checkIsEmpty(request.getMoneyValue(PsyNubMoneyFields.ADD_COSTS_PERSONAL).getComment(), FIELD_NUB_ADD_COST_PERSONAL, errorMessages);
            }

            if (request.getMoneyValue(PsyNubMoneyFields.ADD_COSTS_MATERIAL).getMoney() > 0) {
                checkIsEmpty(request.getMoneyValue(PsyNubMoneyFields.ADD_COSTS_MATERIAL).getComment(), FIELD_NUB_ADD_COST_MATERIAL, errorMessages);
            }

            if (request.getMoneyValue(PsyNubMoneyFields.ADD_COSTS_OTHER).getMoney() > 0) {
                checkIsEmpty(request.getMoneyValue(PsyNubMoneyFields.ADD_COSTS_OTHER).getComment(), FIELD_NUB_ADD_COST_OTHER, errorMessages);
            }
        }

        checkIsEmpty(request.getMoneyValue(PsyNubMoneyFields.LESS_COSTS_PERSONAL).getComment(), FIELD_NUB_LESS_COST_PERSONAL, errorMessages);
        checkIsEmpty(request.getMoneyValue(PsyNubMoneyFields.LESS_COSTS_MATERIAL).getComment(), FIELD_NUB_LESS_COST_MATERIAL, errorMessages);
        checkIsEmpty(request.getMoneyValue(PsyNubMoneyFields.LESS_COSTS_OTHER).getComment(), FIELD_NUB_LESS_COST_OTHER, errorMessages);
    }

    private static void checkNumberValues(PsyNubRequest request, List<String> errorMessages) {
        checkIsBetweenValue(request.getNumberValue(PsyNubNumberFields.PATIENTS_TARGETYEAR).getNumber(),
                1, 999999999, FIELD_NUB_PATIENTS_TARGETYEAR, errorMessages);
        checkIsBetweenValue(request.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_TARGETYEAR).getNumber(),
                0, 999999999, FIELD_NUB_PATIENTS_PRE_TARGETYEAR, errorMessages);
        checkIsBetweenValue(request.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_PRE_TARGETYEAR).getNumber(),
                0, 999999999, FIELD_NUB_PATIENTS_PRE_PRE_TARGETYEAR, errorMessages);
    }

    private static void checkDateValues(PsyNubRequest request, List<String> errorMessages) {
        checkIsEmpty(request.getDateValue(PsyNubDateFields.INTRODUCED_HOSPITAL).getDate(), FIELD_NUB_IN_HOSPITAL, errorMessages);
        if (PsyNubRequestValueChecker.isValidStringForDateValue(request.getDateValue(PsyNubDateFields.INTRODUCED_HOSPITAL).getDate())) {
            checkIsEmpty("", FIELD_NUB_IN_HOSPITAL, errorMessages);
        }
    }

    private static void checkMasterData(PsyNubRequest request, List<String> errorMessages) {
        //TODO Check IK
        checkIsEmpty(request.getFirstName(), FIELD_FIRSTNAME, errorMessages);
        checkIsEmpty(request.getLastName(), FIELD_LASTNAME, errorMessages);
        checkIsValidValue(request.getGender(), VALID_GENDERS, FIELD_GENDER, errorMessages);
        checkIsEmpty(request.getStreet(), FIELD_STREET, errorMessages);
        checkIsEmpty(request.getPostalCode(), FIELD_POSTCODE, errorMessages);
        checkIsEmpty(request.getTown(), FIELD_TOWN, errorMessages);
        checkIsEmpty(request.getPhone(), FIELD_PHONE, errorMessages);
        checkIsEmpty(request.getEmail(), FIELD_EMAIL, errorMessages);
        checkIsEmpty(request.getIkName(), FIELD_HOSPITALNAME, errorMessages);
    }

    private static void checkProposalData(PsyNubRequest request, List<String> errorMessages) {
        checkIsEmpty(request.getName(), FIELD_NUB_NAME, errorMessages);
        checkIsEmpty(request.getProposalData().getDescription(), FIELD_NUB_DESCRIPTION, errorMessages);
        if (!request.getProposalData().getHasNoProcs()) {
            checkIsEmpty(request.getProposalData().getProcs(), FIELD_NUB_OPS, errorMessages);
        }
        checkIsEmpty(request.getProposalData().getIndication(), FIELD_NUB_INDICATORS, errorMessages);
        checkIsEmpty(request.getProposalData().getReplacement(), FIELD_NUB_REPLACEMENT, errorMessages);
        checkIsEmpty(request.getProposalData().getWhatsNew(), FIELD_NUB_WHATS_NEW, errorMessages);

        checkIsEmpty(request.getProposalData().getWhyNotRepresented(), FIELD_NUB_WHY_NOT_IN_PEPP, errorMessages);
    }

    private static boolean hasAnyAddMoneyValue(PsyNubRequest request, List<String> errorMessages) {
        boolean hasAnyEntry = request.getMoneyValue(PsyNubMoneyFields.ADD_COSTS_PERSONAL).getMoney() > 0
                || request.getMoneyValue(PsyNubMoneyFields.ADD_COSTS_MATERIAL).getMoney() > 0
                || request.getMoneyValue(PsyNubMoneyFields.LESS_COSTS_OTHER).getMoney() > 0;

        if (!hasAnyEntry) {
            errorMessages.add(FIELD_NUB_ANY_ADD_COST);
        }
        return hasAnyEntry;
    }

    private static void checkIsEmpty(String value, String errorMessage, List<String> errorMessages) {

    }

    private static void checkIsValidValue(int value, Integer[] validValues, String errorMessage, List<String> errorMessages) {

    }

    private static void checkIsBetweenValue(int value, int min, int max, String errorMessage, List<String> errorMessages) {

    }

    /*

        String proxyErr = checkProxyIKs(nubRequest.getProxyIKs());
        if (!proxyErr.isEmpty()) {
            message.setMessage(Utils.getMessage("lblErrorProxyIKs"));
            message.setTopic(EditNubRequest.NubRequestTabs.tabNubAddress.name());
        }
        return message;
     */
}
