/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.khcomparison.helper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.inek.dataportal.common.data.KhComparison.entities.StructureBaseInformation;
import org.inek.dataportal.common.data.KhComparison.entities.StructureInformation;

/**
 *
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
