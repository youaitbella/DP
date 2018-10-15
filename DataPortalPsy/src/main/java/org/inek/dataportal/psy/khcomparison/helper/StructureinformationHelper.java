/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.khcomparison.helper;

import org.inek.dataportal.common.data.KhComparison.entities.StructureBaseInformation;
import org.inek.dataportal.common.data.KhComparison.entities.StructureInformation;
import org.inek.dataportal.common.enums.StructureInformationCategorie;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lautenti
 */
public class StructureinformationHelper {

    public static String checkForDuplicatedDates(StructureBaseInformation info) {
        Set<String> categories = new HashSet<>();
        String errors = "";

        for (StructureInformation sInfo : info.getStructureInformations()) {
            if (info.getStructureInformations().stream().filter(c -> c.getStructureCategorie() == sInfo.getStructureCategorie()
                    && c.getValidFrom().equals(sInfo.getValidFrom())).count() > 1) {
                categories.add(sInfo.getStructureCategorie().getArea());
            }
        }

        for (String category : categories) {
            errors += category + ", ";
        }

        if (errors.endsWith(", ")) {
            errors = errors.substring(0, errors.length() - 2);
        }

        return errors;
    }

    public static Boolean newDateChangeOrder(Date oldDate, Date newDate, List<StructureInformation> structureInformations) {
        structureInformations.sort(Comparator.comparing(StructureInformation::getValidFrom, Comparator.nullsLast(Comparator.naturalOrder())));

        List<Tupel> compareList = new ArrayList<>();

        for (int i = 0; i < structureInformations.size(); i++) {
            compareList.add(new Tupel(i, structureInformations.get(i).getValidFrom()));
        }

        boolean foundEntry = false;

        for (int i = 0; i < compareList.size(); i++) {
            if (compareList.get(i).getValue().equals(oldDate)) {
                if (!foundEntry) {
                    compareList.get(i).setValue(newDate);
                    foundEntry = true;
                } else {
                    return true;
                }
            }
        }

        compareList.sort(Comparator.comparing(Tupel::getValue, Comparator.nullsLast(Comparator.naturalOrder())));

        for (int i = 0; i < compareList.size(); i++) {
            if (compareList.get(i).getKey() != i) {
                return true;
            }
        }
        return false;
    }

    public static Boolean structureInformationIsReadonly(StructureInformation info, List<StructureInformation> structureInformations, Date currentDate) {
        if (info.getId() == 0) {
            return false;
        }

        if (info.getValidFrom().after(currentDate) || info.getValidFrom().equals(currentDate)) {
            return false;
        }

        if (structureInformations.stream()
                .filter(c -> c.getId() > 0)
                .anyMatch(c -> c.getValidFrom().after(info.getValidFrom()) &&
                        (c.getValidFrom().before(currentDate)) || c.getValidFrom().equals(currentDate))) {
            return true;
        } else {
            return false;
        }
    }

    public static List<StructureInformation> getStructureInformationsByStructureCategorieFiltered(StructureBaseInformation baseInfo, String catName,
                                                                                                  Date filterFrom, Date filterUntil) {

        List<StructureInformation> structureInformations = baseInfo.getStructureInformations().stream()
                .filter(c -> c.getStructureCategorie() == StructureInformationCategorie.valueOf(catName))
                .collect(Collectors.toList());

        structureInformations.removeIf(c -> c.getValidFrom().after(filterUntil));

        if (structureInformations.stream().anyMatch(c -> c.getValidFrom().equals(filterFrom))) {
            structureInformations.removeIf(c -> c.getValidFrom().before(filterFrom));
        } else {
            Optional<StructureInformation> first = structureInformations.stream()
                    .filter(c -> c.getValidFrom().before(filterFrom))
                    .min(Comparator.comparing(StructureInformation::getValidFrom, Comparator.nullsLast(Comparator.reverseOrder())));

            first.ifPresent(structureInformation -> structureInformations.removeIf(c -> c.getValidFrom()
                    .before(structureInformation.getValidFrom())));
        }

        return structureInformations.stream()
                .sorted(Comparator.comparing(StructureInformation::getValidFrom, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

    }

    public static class Tupel {

        private int _key;
        private Date _value;

        public Tupel(int key, Date value) {
            this._key = key;
            this._value = value;
        }

        public int getKey() {
            return _key;
        }

        public void setKey(int key) {
            this._key = key;
        }

        public Date getValue() {
            return _value;
        }

        public void setValue(Date value) {
            this._value = value;
        }
    }

}
