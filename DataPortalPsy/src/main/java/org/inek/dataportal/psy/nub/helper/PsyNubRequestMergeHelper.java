package org.inek.dataportal.psy.nub.helper;

import org.inek.dataportal.common.helper.ObjectComparer;
import org.inek.dataportal.common.helper.ObjectCopier;
import org.inek.dataportal.common.helper.structures.FieldValues;
import org.inek.dataportal.common.utils.DocumentationUtil;
import org.inek.dataportal.psy.nub.entities.PsyNubRequest;
import org.inek.dataportal.psy.nub.entities.PsyNubRequestData;
import org.inek.dataportal.psy.nub.enums.PsyNubDateFields;
import org.inek.dataportal.psy.nub.enums.PsyNubMoneyFields;
import org.inek.dataportal.psy.nub.enums.PsyNubNumberFields;

import java.lang.reflect.Field;
import java.util.*;

public class PsyNubRequestMergeHelper {

    private PsyNubRequest _baseLine;
    private PsyNubRequest _ownProposal;
    private PsyNubRequest _partnerProposal;

    private List<String> _errorMessage = new ArrayList<>();

    private List<Class> _excludedTypes = new ArrayList<>();
    private List<String> _ignoredFields = new ArrayList<>();

    public PsyNubRequestMergeHelper(PsyNubRequest baseLine, PsyNubRequest ownProposal, PsyNubRequest partnerProposal) {
        this._baseLine = baseLine;
        this._ownProposal = ownProposal;
        this._partnerProposal = partnerProposal;
        fillExcludedTypes();
        fillIgnoredFields();
    }

    private void fillIgnoredFields() {
        _ignoredFields.add("_statusId");
        _ignoredFields.add("_lastChangedByAccountId");
        _ignoredFields.add("_version");
    }

    private void fillExcludedTypes() {
        _excludedTypes.add(Date.class);
        _excludedTypes.add(PsyNubRequest.class);
        _excludedTypes.add(PsyNubRequestData.class);
        _excludedTypes.add(List.class);
    }


    public List<String> compareProposals() {
        Map<String, FieldValues> ownMasterDataDifferencesToBaseline = getDifferences(_baseLine, _ownProposal);
        Map<String, FieldValues> ownRequestDataDifferencesToBaseline = getDifferences(_baseLine.getProposalData(), _ownProposal.getProposalData());

        Map<String, FieldValues> partnerMasterDataDifferencesToBaseline = getDifferences(_baseLine, _partnerProposal);
        Map<String, FieldValues> partnerDataDifferencesToBaseline = getDifferences(_baseLine.getProposalData(), _partnerProposal.getProposalData());

        List<String> collisions = updateRequestFields(ownMasterDataDifferencesToBaseline, partnerMasterDataDifferencesToBaseline, false);
        collisions.addAll(updateRequestFields(ownRequestDataDifferencesToBaseline, partnerDataDifferencesToBaseline, true));

        Map<String, String> documentationFields = new HashMap<>();
        documentationFields.putAll(DocumentationUtil.getFieldTranslationMap(_ownProposal));
        documentationFields.putAll(DocumentationUtil.getFieldTranslationMap(_ownProposal.getProposalData()));

        Map<String, FieldValues> combinedPartnerDifferences = new HashMap<>();
        combinedPartnerDifferences.putAll(partnerMasterDataDifferencesToBaseline);
        combinedPartnerDifferences.putAll(partnerDataDifferencesToBaseline);

        for (String value : combinedPartnerDifferences.keySet()) {
            if (collisions.contains(value)) {
                addCollisionMessage(documentationFields.get(value));
            } else {
                addMessage(documentationFields.get(value));
            }
        }

        compareNumberValues();
        compareDateValues();
        compareMoneyValues();

        return _errorMessage;
    }

    private void compareMoneyValues() {
        List<PsyNubMoneyFields> ownDifferentMoneyValues = getMoneyDifferences(_baseLine, _ownProposal);
        List<PsyNubMoneyFields> partnerDifferentMoneyValues = getMoneyDifferences(_baseLine, _partnerProposal);

        List<PsyNubMoneyFields> collisions = new ArrayList<>();

        for (PsyNubMoneyFields moneyField : partnerDifferentMoneyValues) {
            if (ownDifferentMoneyValues.contains(moneyField)) {
                collisions.add(moneyField);
            }
            _ownProposal.getMoneyValue(moneyField).setMoney(_partnerProposal.getMoneyValue(moneyField).getMoney());
            _ownProposal.getMoneyValue(moneyField).setComment(_partnerProposal.getMoneyValue(moneyField).getComment());
        }

        for (PsyNubMoneyFields moneyField : partnerDifferentMoneyValues) {
            if (collisions.contains(moneyField)) {
                addCollisionMessage(moneyField.getDescription());
            } else {
                addMessage(moneyField.getDescription());
            }
        }
    }

    private void compareDateValues() {
        List<PsyNubDateFields> ownDifferentDateValues = getDateDifferences(_baseLine, _ownProposal);
        List<PsyNubDateFields> partnerDifferentDateValues = getDateDifferences(_baseLine, _partnerProposal);

        List<PsyNubDateFields> collisions = new ArrayList<>();

        for (PsyNubDateFields dateField : partnerDifferentDateValues) {
            if (ownDifferentDateValues.contains(dateField)) {
                collisions.add(dateField);
            }
            _ownProposal.getDateValue(dateField).setDate(_partnerProposal.getDateValue(dateField).getDate());
            _ownProposal.getDateValue(dateField).setComment(_partnerProposal.getDateValue(dateField).getComment());
        }

        for (PsyNubDateFields dateField : partnerDifferentDateValues) {
            if (collisions.contains(dateField)) {
                addCollisionMessage(dateField.getDescription());
            } else {
                addMessage(dateField.getDescription());
            }
        }
    }

    private void compareNumberValues() {
        List<PsyNubNumberFields> ownDifferentNumberValues = getNumberDifferences(_baseLine, _ownProposal);
        List<PsyNubNumberFields> partnerDifferentNumberValues = getNumberDifferences(_baseLine, _partnerProposal);

        List<PsyNubNumberFields> collisions = new ArrayList<>();

        for (PsyNubNumberFields numberField : partnerDifferentNumberValues) {
            if (ownDifferentNumberValues.contains(numberField)) {
                collisions.add(numberField);
            }
            _ownProposal.getNumberValue(numberField).setNumber(_partnerProposal.getNumberValue(numberField).getNumber());
            _ownProposal.getNumberValue(numberField).setComment(_partnerProposal.getNumberValue(numberField).getComment());
        }

        for (PsyNubNumberFields numberField : partnerDifferentNumberValues) {
            if (collisions.contains(numberField)) {
                addCollisionMessage(numberField.getDescription());
            } else {
                addMessage(numberField.getDescription());
            }
        }

    }

    private List<PsyNubMoneyFields> getMoneyDifferences(PsyNubRequest baseLine, PsyNubRequest request) {
        List<PsyNubMoneyFields> diffs = new ArrayList<>();
        for (PsyNubMoneyFields moneyField : PsyNubMoneyFields.values()) {
            Map<String, FieldValues> differences = getDifferences(baseLine.getMoneyValue(moneyField), request.getMoneyValue(moneyField));
            if (!differences.isEmpty()) {
                diffs.add(moneyField);
            }
        }
        return diffs;
    }

    private List<PsyNubDateFields> getDateDifferences(PsyNubRequest baseLine, PsyNubRequest request) {
        List<PsyNubDateFields> diffs = new ArrayList<>();
        for (PsyNubDateFields dateField : PsyNubDateFields.values()) {
            Map<String, FieldValues> differences = getDifferences(baseLine.getDateValue(dateField), request.getDateValue(dateField));
            if (!differences.isEmpty()) {
                diffs.add(dateField);
            }
        }
        return diffs;
    }

    private List<PsyNubNumberFields> getNumberDifferences(PsyNubRequest baseLine, PsyNubRequest request) {
        List<PsyNubNumberFields> diffs = new ArrayList<>();
        for (PsyNubNumberFields numberField : PsyNubNumberFields.values()) {
            Map<String, FieldValues> differences = getDifferences(baseLine.getNumberValue(numberField), request.getNumberValue(numberField));
            if (!differences.isEmpty()) {
                diffs.add(numberField);
            }
        }
        return diffs;
    }


    private Map<String, FieldValues> getDifferences(Object baseLine, Object request) {
        Map<String, FieldValues> differences = ObjectComparer.getDifferences(baseLine, request, _excludedTypes);
        removeInvalidFields(differences);
        return differences;
    }

    private void removeInvalidFields(Map<String, FieldValues> list) {
        for (String ignoredField : _ignoredFields) {
            list.remove(ignoredField);
        }
    }

    private void addCollisionMessage(String message) {
        _errorMessage.add("### " + message + " ###");
    }

    private void addMessage(String message) {
        _errorMessage.add(message);
    }

    private List<String> updateRequestFields(Map<String, FieldValues> ownDifferences, Map<String, FieldValues> partnerDifferences, Boolean isData) {
        List<String> collisions = new ArrayList<>();
        for (String fieldName : partnerDifferences.keySet()) {
            if (ownDifferences.containsKey(fieldName)) {
                collisions.add(fieldName);
            }
            FieldValues fieldValues = partnerDifferences.get(fieldName);
            Field field = fieldValues.getField();
            if (isData) {
                ObjectCopier.copyFieldValue(field, _partnerProposal.getProposalData(), _ownProposal.getProposalData());
            } else {
                ObjectCopier.copyFieldValue(field, _partnerProposal, _ownProposal);
            }
        }
        return collisions;
    }
}
