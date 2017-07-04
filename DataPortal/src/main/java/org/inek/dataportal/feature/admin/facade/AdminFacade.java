/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.admin.facade;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.facades.AbstractDataAccess;

/**
 * Hides the database accesses fro the admin tasks behind a facade
 * @author muellermi
 */
@RequestScoped
@Transactional
public class AdminFacade extends AbstractDataAccess {
}
