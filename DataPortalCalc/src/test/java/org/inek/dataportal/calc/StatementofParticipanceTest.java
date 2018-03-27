/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inek.dataportal.calc;

import org.inek.dataportal.calc.entities.sop.StatementOfParticipance;
import org.inek.dataportal.calc.entities.sop.CalcContact;
import org.inek.dataportal.calc.backingbean.EditStatementOfParticipance;
import org.inek.dataportal.common.helper.structures.MessageContainer;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author schwarzst
 */
public class StatementofParticipanceTest {
        
    @Test
    public void testisStatementComplete1() {
        StatementOfParticipance statement = new StatementOfParticipance();
        EditStatementOfParticipance edit = new EditStatementOfParticipance();
        
        statement.setAccountId(12701);
        statement.setDataYear(2017);
        
        statement.setDrgCalc(false);
        statement.setPsyCalc(false);
        statement.setInvCalc(true);
        statement.setTpgCalc(true);
        statement.setObdCalc(true);
        
        CalcContact c1 = new CalcContact();
        c1.setDrg(false);
        c1.setPsy(false);
        c1.setTpg(true);
        c1.setObd(true);
        c1.setInv(true);
        c1.setGender(1);
        c1.setFirstName("Peter");
        c1.setLastName("Schmitz");
        c1.setPhone("12345");
        c1.setMail("de@dse.de");

        c1.setConsultant(true);
        
        statement.getContacts().add(c1);
        MessageContainer message = edit.composeMissingFieldsMessage(statement);
        System.out.println(message.getMessage());
        assertTrue(message.containsMessage());
    }
    
    @Test
    public void testisStatementComplete2() {
        StatementOfParticipance statement = new StatementOfParticipance();
        EditStatementOfParticipance edit = new EditStatementOfParticipance();
        
        statement.setAccountId(12701);
        statement.setDataYear(2017);
        
        statement.setDrgCalc(false);
        statement.setPsyCalc(false);
        statement.setInvCalc(true);
        statement.setTpgCalc(true);
        statement.setObdCalc(true);
        
        CalcContact c1 = new CalcContact();
        c1.setDrg(false);
        c1.setPsy(false);
        c1.setTpg(true);
        c1.setObd(true);
        c1.setInv(true);
        c1.setGender(1);
        c1.setFirstName("Peter");
        c1.setLastName("Schmitz");
        c1.setPhone("12345");
        c1.setMail("de@dse.de");

        c1.setConsultant(false);
        
        statement.getContacts().add(c1);
        MessageContainer message = edit.composeMissingFieldsMessage(statement);
        System.out.println(message.getMessage());
        assertFalse(message.containsMessage());
    }
    
    @Test
    public void testisStatementComplete3() {
        StatementOfParticipance statement = new StatementOfParticipance();
        EditStatementOfParticipance edit = new EditStatementOfParticipance();
        
        statement.setAccountId(12701);
        statement.setDataYear(2017);
        
        statement.setDrgCalc(true);
        statement.setPsyCalc(false);
        statement.setInvCalc(true);
        statement.setTpgCalc(true);
        statement.setObdCalc(true);
        
        CalcContact c1 = new CalcContact();
        c1.setDrg(true);
        c1.setPsy(false);
        c1.setTpg(true);
        c1.setObd(true);
        c1.setInv(true);
        c1.setGender(1);
        c1.setFirstName("Peter");
        c1.setLastName("Schmitz");
        c1.setPhone("12345");
        c1.setMail("de@dse.de");

        c1.setConsultant(false);
        
        statement.getContacts().add(c1);
        MessageContainer message = edit.composeMissingFieldsMessage(statement);
        System.out.println(message.getMessage());
        assertTrue(message.containsMessage());
    }
    
    @Test
    public void testisStatementComplete4() {
        StatementOfParticipance statement = new StatementOfParticipance();
        EditStatementOfParticipance edit = new EditStatementOfParticipance();
        
        statement.setAccountId(12701);
        statement.setDataYear(2017);
        
        statement.setDrgCalc(true);
        statement.setPsyCalc(false);
        statement.setInvCalc(true);
        statement.setTpgCalc(true);
        statement.setObdCalc(true);
        
        statement.setClinicalDistributionModelDrg(1);
        
        CalcContact c1 = new CalcContact();
        c1.setDrg(true);
        c1.setPsy(false);
        c1.setTpg(true);
        c1.setObd(true);
        c1.setInv(true);
        c1.setGender(1);
        c1.setFirstName("Peter");
        c1.setLastName("Schmitz");
        c1.setPhone("12345");
        c1.setMail("de@dse.de");

        c1.setConsultant(false);
        
        statement.getContacts().add(c1);
        MessageContainer message = edit.composeMissingFieldsMessage(statement);
        System.out.println(message.getMessage());
        assertTrue(message.containsMessage());
    }
    
    @Test
    public void testisStatementComplete5() {
        StatementOfParticipance statement = new StatementOfParticipance();
        EditStatementOfParticipance edit = new EditStatementOfParticipance();
        
        statement.setAccountId(12701);
        statement.setDataYear(2017);
        
        statement.setDrgCalc(true);
        statement.setPsyCalc(false);
        statement.setInvCalc(true);
        statement.setTpgCalc(true);
        statement.setObdCalc(true);
        
        statement.setClinicalDistributionModelDrg(1);
        statement.setMultiyearDrg(4);
        CalcContact c1 = new CalcContact();
        c1.setDrg(true);
        c1.setPsy(false);
        c1.setTpg(true);
        c1.setObd(true);
        c1.setInv(true);
        c1.setGender(1);
        c1.setFirstName("Peter");
        c1.setLastName("Schmitz");
        c1.setPhone("12345");
        c1.setMail("de@dse.de");

        c1.setConsultant(false);
        
        statement.getContacts().add(c1);
        MessageContainer message = edit.composeMissingFieldsMessage(statement);
        System.out.println(message.getMessage());
        assertFalse(message.containsMessage());
    }
    
    @Test
    public void testisStatementComplete6() {
        StatementOfParticipance statement = new StatementOfParticipance();
        EditStatementOfParticipance edit = new EditStatementOfParticipance();
        
        statement.setAccountId(12701);
        statement.setDataYear(2017);
        
        statement.setDrgCalc(true);
        statement.setPsyCalc(false);
        statement.setInvCalc(true);
        statement.setTpgCalc(true);
        statement.setObdCalc(true);
        
        statement.setClinicalDistributionModelDrg(1);
        statement.setMultiyearDrg(15);
        CalcContact c1 = new CalcContact();
        c1.setDrg(true);
        c1.setPsy(false);
        c1.setTpg(true);
        c1.setObd(true);
        c1.setInv(true);
        c1.setGender(1);
        c1.setFirstName("Peter");
        c1.setLastName("Schmitz");
        c1.setPhone("12345");
        c1.setMail("de@dse.de");

        c1.setConsultant(false);
        
        statement.getContacts().add(c1);
        MessageContainer message = edit.composeMissingFieldsMessage(statement);
        System.out.println(message.getMessage());
        assertTrue(message.containsMessage());
    }
    
    @Test
    public void testisStatementComplete7() {
        StatementOfParticipance statement = new StatementOfParticipance();
        EditStatementOfParticipance edit = new EditStatementOfParticipance();
        
        statement.setAccountId(12701);
        statement.setDataYear(2017);
        
        statement.setDrgCalc(true);
        statement.setPsyCalc(false);
        statement.setInvCalc(true);
        statement.setTpgCalc(true);
        statement.setObdCalc(true);
        
        statement.setClinicalDistributionModelDrg(1);
        statement.setMultiyearDrg(15);
        statement.setMultiyearDrgText("test");
        CalcContact c1 = new CalcContact();
        c1.setDrg(true);
        c1.setPsy(false);
        c1.setTpg(true);
        c1.setObd(true);
        c1.setInv(true);
        c1.setGender(1);
        c1.setFirstName("Peter");
        c1.setLastName("Schmitz");
        c1.setPhone("12345");
        c1.setMail("de@dse.de");

        c1.setConsultant(false);
        
        statement.getContacts().add(c1);
        MessageContainer message = edit.composeMissingFieldsMessage(statement);
        System.out.println(message.getMessage());
        assertFalse(message.containsMessage());
    }
    
    @Test
    public void testisStatementComplete8() {
        StatementOfParticipance statement = new StatementOfParticipance();
        EditStatementOfParticipance edit = new EditStatementOfParticipance();
        
        statement.setAccountId(12701);
        statement.setDataYear(2017);
        
        statement.setDrgCalc(true);
        statement.setPsyCalc(false);
        statement.setInvCalc(true);
        statement.setTpgCalc(true);
        statement.setObdCalc(true);
        
        statement.setClinicalDistributionModelDrg(1);
        statement.setMultiyearDrg(15);
        statement.setMultiyearDrgText("test");
        CalcContact c1 = new CalcContact();
        c1.setDrg(true);
        c1.setPsy(false);
        c1.setTpg(true);
        c1.setObd(true);
        c1.setInv(true);
        c1.setGender(1);
        c1.setFirstName("Peter");
        c1.setLastName("Schmitz");
        c1.setPhone("12345");
        c1.setMail("de@dse.de");

        c1.setConsultant(false);
        
        CalcContact c2 = new CalcContact();
        c2.setDrg(false);
        c2.setPsy(false);
        c2.setTpg(false);
        c2.setObd(false);
        c2.setInv(false);
        c2.setGender(1);
        c2.setFirstName("Peter");
        c2.setLastName("Schmitz");
        c2.setPhone("12345");
        c2.setMail("dsse@dse.de");

        c2.setConsultant(true);
        
        statement.getContacts().add(c1);
        statement.getContacts().add(c2);
        MessageContainer message = edit.composeMissingFieldsMessage(statement);
        System.out.println(message.getMessage());
        assertTrue(message.containsMessage());
    }
    
    @Test
    public void testisStatementComplete9() {
        StatementOfParticipance statement = new StatementOfParticipance();
        EditStatementOfParticipance edit = new EditStatementOfParticipance();
        
        statement.setAccountId(12701);
        statement.setDataYear(2017);
        
        statement.setDrgCalc(true);
        statement.setPsyCalc(false);
        statement.setInvCalc(true);
        statement.setTpgCalc(true);
        statement.setObdCalc(true);
        
        statement.setClinicalDistributionModelDrg(1);
        statement.setMultiyearDrg(15);
        statement.setMultiyearDrgText("test");
        CalcContact c1 = new CalcContact();
        c1.setDrg(true);
        c1.setPsy(false);
        c1.setTpg(true);
        c1.setObd(true);
        c1.setInv(true);
        c1.setGender(1);
        c1.setFirstName("Peter");
        c1.setLastName("Schmitz");
        c1.setPhone("12345");
        c1.setMail("de@dse.de");

        c1.setConsultant(false);
        
        CalcContact c2 = new CalcContact();
        c2.setDrg(false);
        c2.setPsy(false);
        c2.setTpg(false);
        c2.setObd(true);
        c2.setInv(false);
        c2.setGender(1);
        c2.setFirstName("Peter");
        c2.setLastName("Schmitz");
        c2.setPhone("12345");
        c2.setMail("dsse@dse.de");

        c2.setConsultant(true);
        
        statement.getContacts().add(c1);
        statement.getContacts().add(c2);
        MessageContainer message = edit.composeMissingFieldsMessage(statement);
        System.out.println(message.getMessage());
        assertFalse(message.containsMessage());
    }
    
    @Test
    public void testisStatementComplete10() {
        StatementOfParticipance statement = new StatementOfParticipance();
        EditStatementOfParticipance edit = new EditStatementOfParticipance();
        
        statement.setAccountId(12701);
        statement.setDataYear(2017);
        
        statement.setDrgCalc(true);
        statement.setPsyCalc(false);
        statement.setInvCalc(true);
        statement.setTpgCalc(true);
        statement.setObdCalc(true);
        
        statement.setClinicalDistributionModelDrg(1);
        statement.setMultiyearDrg(15);
        statement.setMultiyearDrgText("test");
        
        statement.setWithConsultant(true);
        
        CalcContact c1 = new CalcContact();
        c1.setDrg(true);
        c1.setPsy(false);
        c1.setTpg(true);
        c1.setObd(true);
        c1.setInv(true);
        c1.setGender(1);
        c1.setFirstName("Peter");
        c1.setLastName("Schmitz");
        c1.setPhone("12345");
        c1.setMail("de@dse.de");

        c1.setConsultant(false);
        
        CalcContact c2 = new CalcContact();
        c2.setDrg(false);
        c2.setPsy(false);
        c2.setTpg(false);
        c2.setObd(true);
        c2.setInv(false);
        c2.setGender(1);
        c2.setFirstName("Peter");
        c2.setLastName("Schmitz");
        c2.setPhone("12345");
        c2.setMail("dsse@dse.de");

        c2.setConsultant(true);
        
        statement.getContacts().add(c1);
        statement.getContacts().add(c2);
        MessageContainer message = edit.composeMissingFieldsMessage(statement);
        System.out.println(message.getMessage());
        assertTrue(message.containsMessage());
    }
    
    @Test
    public void testisStatementComplete11() {
        StatementOfParticipance statement = new StatementOfParticipance();
        EditStatementOfParticipance edit = new EditStatementOfParticipance();
        
        statement.setAccountId(12701);
        statement.setDataYear(2017);
        
        statement.setDrgCalc(true);
        statement.setPsyCalc(false);
        statement.setInvCalc(true);
        statement.setTpgCalc(true);
        statement.setObdCalc(true);
        
        statement.setClinicalDistributionModelDrg(1);
        statement.setMultiyearDrg(15);
        statement.setMultiyearDrgText("test");
        
        statement.setWithConsultant(true);
        statement.setConsultantCompany("con");
        
        CalcContact c1 = new CalcContact();
        c1.setDrg(true);
        c1.setPsy(false);
        c1.setTpg(true);
        c1.setObd(true);
        c1.setInv(true);
        c1.setGender(1);
        c1.setFirstName("Peter");
        c1.setLastName("Schmitz");
        c1.setPhone("12345");
        c1.setMail("de@dse.de");

        c1.setConsultant(false);
        
        CalcContact c2 = new CalcContact();
        c2.setDrg(false);
        c2.setPsy(false);
        c2.setTpg(false);
        c2.setObd(true);
        c2.setInv(false);
        c2.setGender(1);
        c2.setFirstName("Peter");
        c2.setLastName("Schmitz");
        c2.setPhone("12345");
        c2.setMail("dsse@dse.de");

        c2.setConsultant(true);
        
        statement.getContacts().add(c1);
        statement.getContacts().add(c2);
        MessageContainer message = edit.composeMissingFieldsMessage(statement);
        System.out.println(message.getMessage());
        assertFalse(message.containsMessage());
    }

    @Test
    public void testisStatementComplete12() {
        StatementOfParticipance statement = new StatementOfParticipance();
        EditStatementOfParticipance edit = new EditStatementOfParticipance();
        
        statement.setAccountId(12701);
        statement.setDataYear(2017);
        
        statement.setDrgCalc(true);
        statement.setPsyCalc(false);
        statement.setInvCalc(false);
        statement.setTpgCalc(false);
        statement.setObdCalc(false);
        
        statement.setClinicalDistributionModelDrg(1);
        statement.setObligatory(true);
        statement.setObligatoryFollowingYears(true);
        statement.setMultiyearDrg(0);
        
        statement.setWithConsultant(true);
        statement.setConsultantCompany("con");
        
        CalcContact c1 = new CalcContact();
        c1.setDrg(true);
        c1.setPsy(false);
        c1.setTpg(true);
        c1.setObd(true);
        c1.setInv(true);
        c1.setGender(1);
        c1.setFirstName("Peter");
        c1.setLastName("Schmitz");
        c1.setPhone("12345");
        c1.setMail("de@dse.de");

        c1.setConsultant(false);
        
        CalcContact c2 = new CalcContact();
        c2.setDrg(false);
        c2.setPsy(false);
        c2.setTpg(false);
        c2.setObd(true);
        c2.setInv(false);
        c2.setGender(1);
        c2.setFirstName("Peter");
        c2.setLastName("Schmitz");
        c2.setPhone("12345");
        c2.setMail("dsse@dse.de");

        c2.setConsultant(true);
        
        statement.getContacts().add(c1);
        statement.getContacts().add(c2);
        MessageContainer message = edit.composeMissingFieldsMessage(statement);
        System.out.println(message.getMessage());
        assertTrue(message.containsMessage());
    }
}
