/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.facades;

import javafx.util.Pair;
import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.StructuralChanges.StructuralChangesBaseInformation;
import org.inek.dataportal.common.data.AbstractDataAccessWithActionLog;
import org.inek.dataportal.common.enums.WorkflowStatus;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author lautenti
 */
@Stateless
public class StructuralChangesFacade extends AbstractDataAccessWithActionLog {

    @Transactional
    public StructuralChangesBaseInformation save(StructuralChangesBaseInformation baseInfo) {
        if (baseInfo.getId() == -1) {
            persist(baseInfo);
            return baseInfo;
        }
        return merge(baseInfo);
    }
}
