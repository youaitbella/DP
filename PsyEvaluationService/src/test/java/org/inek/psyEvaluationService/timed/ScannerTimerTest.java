package org.inek.psyEvaluationService.timed;

import org.assertj.core.api.Assertions;
import org.assertj.db.type.Request;
import org.assertj.db.type.Source;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.enums.Function;
import org.inek.dataportal.api.enums.PortalType;
import org.inek.dataportal.common.controller.ReportController;
import org.inek.dataportal.common.data.KhComparison.entities.*;
import org.inek.dataportal.common.data.KhComparison.enums.PsyGroup;
import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.data.access.InfoDataFacade;
import org.inek.dataportal.common.data.account.entities.*;
import org.inek.dataportal.common.data.account.iface.Person;
import org.inek.dataportal.common.data.adm.Announcement;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.data.adm.ReportTemplate;
import org.inek.dataportal.common.data.adm.facade.AdminFacade;
import org.inek.dataportal.common.data.common.Conversation;
import org.inek.dataportal.common.data.common.ListFeature;
import org.inek.dataportal.common.data.common.ListFunction;
import org.inek.dataportal.common.data.common.ListWorkflowStatus;
import org.inek.dataportal.common.data.icmt.entities.Customer;
import org.inek.dataportal.common.data.icmt.facade.CustomerFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.CustomerTyp;
import org.inek.dataportal.common.enums.Stage;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.psyEvaluationService.backingBean.MessageProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

class ScannerTimerTest {
    private static EntityManager entityManager;

    @BeforeAll
    public static void obtainEntityManager() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("DataPortalDevLocalPU");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeEntityManager() {
        entityManager.close();
    }

    @Test
    void shouldStartTimedExecutionMethod() {
        MyAEBFacade myAEBFacade = new MyAEBFacade(entityManager);
        System.out.println("after 1");
        MyConfigFacade myConfigFacade = new MyConfigFacade(entityManager);
        System.out.println("after 2");
        MyAdminFacade myAdminFacade = new MyAdminFacade(entityManager);
        MyInfoDataFacade myInfoDataFacade = new MyInfoDataFacade(entityManager);
        MyCustomerFacade myCustomerFacade = new MyCustomerFacade(entityManager);
        MyApplicationTools myApplicationTools = new MyApplicationTools(myConfigFacade, myInfoDataFacade, myCustomerFacade);
        MyReportController myReportController = new MyReportController(myAdminFacade, myApplicationTools);
        System.out.println("after 3");
        MyMailer myMailer = new MyMailer();
        System.out.println("after 4");
        MyMessageProvider myMessageProvider = new MyMessageProvider();
        System.out.println("after 5");
        ScannerTimer scannerTimer = new ScannerTimer(myAEBFacade, myConfigFacade, myReportController, myMailer, myMessageProvider);
        System.out.println("after 6");
        createInekComparisonJob();
        scannerTimer.execute();
        System.out.println("after 7");

        System.out.println("gestartet");
    }

    private void createInekComparisonJob() {
        Account account = entityManager.find(Account.class, 6006);
        Assertions.assertThat(account.getLastName()).isEqualToIgnoringCase("Kunkel");
        InekComparisonJob inekComparisonJob = new InekComparisonJob();
        inekComparisonJob.setAccount(account);
        // make inekComparisonJob managed by persistence manager
        entityManager.persist(inekComparisonJob);
        inekComparisonJob.setDataYear(2018);
        inekComparisonJob.setAebUpTo("2020-01-17");
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.flush();
        transaction.commit();
        Assertions.assertThat(inekComparisonJob.getId()).isNotEqualTo(0);

        AEBFacade aebFacade = new AEBFacade(entityManager);
        transaction.begin();
        aebFacade.generateInekComparisonHospitals(inekComparisonJob);
        transaction.commit();
        Request request = new org.assertj.db.type.Request(
                new Source("jdbc:sqlserver://vdatabase01:1433;databaseName=DataPortalDev;integratedSecurity=true","", ""),
                "select count(1) as anz from psy.InekComparisonHospital where ichInekComparisonJobId = " + inekComparisonJob.getId());
        org.assertj.db.api.Assertions.assertThat(request).row(0).column(0).value().isGreaterThan(100);

    }
}

class MyCustomerFacade extends CustomerFacade {
    public MyCustomerFacade() {
    }

    public MyCustomerFacade(EntityManager em) {
        super(em);
    }

    @Override
    public Customer getCustomerByIK(int ik) {
        throw new UnsupportedOperationException();
        //return super.getCustomerByIK(ik);
    }

    @Override
    public boolean isValidIK(String ikString) {
        throw new UnsupportedOperationException();
        //return super.isValidIK(ikString);
    }

    @Override
    public boolean isFormalCorrectIk(String ikString) {
        throw new UnsupportedOperationException();
        //return super.isFormalCorrectIk(ikString);
    }

    @Override
    public boolean checkIK(int ik) {
        throw new UnsupportedOperationException();
        //return super.checkIK(ik);
    }

    @Override
    public void isIKValid(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
        throw new UnsupportedOperationException();
        //super.isIKValid(ctx, component, value);
    }
}
class MyInfoDataFacade extends InfoDataFacade {
    public MyInfoDataFacade() {
    }

    public MyInfoDataFacade(EntityManager em) {
        super(em);
    }

    @Override
    public boolean isTrashMailDomain(String domain) {
        throw new UnsupportedOperationException();
        //return super.isTrashMailDomain(domain);
    }

    @Override
    public List<ListFeature> findAllListFeature() {
        throw new UnsupportedOperationException();
        //return super.findAllListFeature();
    }

    @Override
    public void saveListFeature(ListFeature listFeature) {
        throw new UnsupportedOperationException();
        //super.saveListFeature(listFeature);
    }

    @Override
    public List<ListWorkflowStatus> findAllListWorkflowStatus() {
        throw new UnsupportedOperationException();
        //return super.findAllListWorkflowStatus();
    }

    @Override
    public void saveListWorkflowStatus(ListWorkflowStatus listWorkflowStatus) {
        throw new UnsupportedOperationException();
        //super.saveListWorkflowStatus(listWorkflowStatus);
    }

    @Override
    public List<ListFunction> findAllListFunction() {
        throw new UnsupportedOperationException();
        //return super.findAllListFunction();
    }

    @Override
    public void saveListFunction(ListFunction listFunction) {
        throw new UnsupportedOperationException();
        //super.saveListFunction(listFunction);
    }
}

class MyApplicationTools extends ApplicationTools {

    public MyApplicationTools(ConfigFacade _config, InfoDataFacade _info, CustomerFacade _customerFacade) {
        super(_config, _info, _customerFacade);
    }

    @Override
    public String getServerUrl() {
        throw new UnsupportedOperationException();
        //return super.getServerUrl();
    }

    @Override
    public String getServerWithContextUrl() {
        throw new UnsupportedOperationException();
        //return super.getServerWithContextUrl();
    }

    @Override
    public String getVersion() {
        throw new UnsupportedOperationException();
        //return super.getVersion();
    }

    @Override
    public void showInfoDialog(String titel, String message) {
        throw new UnsupportedOperationException();
        //super.showInfoDialog(titel, message);
    }

    @Override
    public boolean isEnabled(ConfigKey key) {
        throw new UnsupportedOperationException();
        //return super.isEnabled(key);
    }

    @Override
    public boolean isEnabled(String name) {
        throw new UnsupportedOperationException();
        //return super.isEnabled(name);
    }

    @Override
    public boolean isFeatureEnabled(Feature feature) {
        throw new UnsupportedOperationException();
        //return super.isFeatureEnabled(feature);
    }

    @Override
    public String readConfig(ConfigKey key) {
        return super.readConfig(key);
    }

    @Override
    public int readConfigInt(ConfigKey key) {
        throw new UnsupportedOperationException();
        //return super.readConfigInt(key);
    }

    @Override
    public boolean readConfigBool(ConfigKey key) {
        throw new UnsupportedOperationException();
        //return super.readConfigBool(key);
    }

    @Override
    public boolean isTestMode() {
        throw new UnsupportedOperationException();
        //return super.isTestMode();
    }

    @Override
    public String readPortalAddress(PortalType portalType, Stage stage) {
        throw new UnsupportedOperationException();
        //return super.readPortalAddress(portalType, stage);
    }

    @Override
    public String retrieveHospitalInfo(int ik) {
        throw new UnsupportedOperationException();
        //return super.retrieveHospitalInfo(ik);
    }

    @Override
    public String retrieveHospitalInfoWithPsyState(int ik) {
        throw new UnsupportedOperationException();
        //return super.retrieveHospitalInfoWithPsyState(ik);
    }

    @Override
    public String retrieveHospitalName(int ik) {
        throw new UnsupportedOperationException();
        //return super.retrieveHospitalName(ik);
    }

    @Override
    public String retrieveHospitalTown(int ik) {
        throw new UnsupportedOperationException();
        //return super.retrieveHospitalTown(ik);
    }

    @Override
    public Boolean isBwHospital(int ik) {
        throw new UnsupportedOperationException();
        //return super.isBwHospital(ik);
    }

    @Override
    public void cleanHospitalInfoCache() {
        throw new UnsupportedOperationException();
        //super.cleanHospitalInfoCache();
    }
}

class MyAdminFacade extends AdminFacade {
    public MyAdminFacade() {
    }

    public MyAdminFacade(EntityManager em) {
        super(em);
    }

    @Override
    public ReportTemplate findReportTemplate(int id) {
        throw new UnsupportedOperationException();
        //return super.findReportTemplate(id);
    }

    @Override
    public Optional<ReportTemplate> findReportTemplateByName(String name) {
        return super.findReportTemplateByName(name);
    }

    @Override
    public Optional<ReportTemplate> findReportTemplateByName(String group, String name) {
        return super.findReportTemplateByName(group, name);
    }

    @Override
    public List<ReportTemplate> getReportTemplatesByGroup(String group) {
        throw new UnsupportedOperationException();
        //return super.getReportTemplatesByGroup(group);
    }
}

class MyMessageProvider extends MessageProvider {
    @Override
    public void reportMemory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addMessageAndReportMemory(String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void addMessage(String message) {
        System.out.println(message);
    }

    @Override
    public synchronized List<String> getMessages() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getHostInfo() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getJavaVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getServerVersion() {
        throw new UnsupportedOperationException();
    }
}

class MyMailer extends Mailer {
    @Override
    public boolean sendMail(String recipient, String subject, String body) {
        return true;
    }

    @Override
    public boolean sendMail(String recipient, String bcc, String subject, String body) {
        return true;
    }

    @Override
    public boolean sendMailFrom(String from, String recipient, String bcc, String subject, String body) {
        return true;
    }

    @Override
    public boolean sendMailFrom(String from, String recipient, String cc, String bcc, String subject, String body, String... files) {
        return true;
    }

    @Override
    public boolean sendMailTemplate(MailTemplate template, String rec) {
        return true;
    }

    @Override
    public MailTemplate getMailTemplate(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getFormalSalutation(Person person) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSalutation(Person person) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean sendMailWithTemplate(String templateName, Map<String, String> substitutions, Person receiver) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean sendActivationMail(AccountRequest accountRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean sendReRegisterMail(Account account) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean sendMailActivationMail(AccountChangeMail changeMail) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean sendPasswordActivationMail(PasswordRequest pwdRequest, Account account) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean sendFeatureRequestAnswer(String templateName, Account account, AccountFeatureRequest featureRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendWarning(String head, Exception exception) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendError(String head, Exception exception) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendException(Level level, String head, Exception exception) {
        throw new UnsupportedOperationException();
    }
}

class MyReportController extends ReportController {

    public MyReportController(AdminFacade _adminFacade, ApplicationTools _appTools) {
        super(_adminFacade, _appTools);
    }

    @Override
    public boolean reportTemplateExists(String name) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.reportTemplateExists(name);
    }

    @Override
    public byte[] getSingleDocument(String name, int id, String fileName) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getSingleDocument(name, id, fileName);
    }

    @Override
    public byte[] getSingleDocumentByIkAndYear(String name, int ik, int year, String fileName) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getSingleDocumentByIkAndYear(name, ik, year, fileName);
    }

    @Override
    public ReportTemplate getReportTemplateByName(String name) {
        return super.getReportTemplateByName(name);
    }

    @Override
    public void createSingleDocument(String name, int id) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //super.createSingleDocument(name, id);
    }

    @Override
    public void createSingleDocument(String name, int id, String fileName) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //super.createSingleDocument(name, id, fileName);
    }

    @Override
    public byte[] getSingleDocument(String path) {
        //throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        return super.getSingleDocument(path);
    }

    @Override
    public List<ReportTemplate> getReportTemplates(String group) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getReportTemplates(group);
    }
}

class MyConfigFacade extends ConfigFacade {
    EntityManager entityManager;

    public MyConfigFacade() {
    }

    public MyConfigFacade(EntityManager em) {
        super(em);
        entityManager = em;
    }

    @Override
    public void saveConfig(ConfigKey key, String value) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //super.saveConfig(key, value);
    }

    @Override
    public void saveConfig(Feature feature, boolean value) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //super.saveConfig(feature, value);
    }

    @Override
    public void saveConfig(ConfigKey key, boolean value) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //super.saveConfig(key, value);
    }

    @Override
    public void saveConfig(ConfigKey key, int value) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //super.saveConfig(key, value);
    }

    @Override
    public String readConfig(String key, String defaultValue) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        String value = super.readConfig(key, defaultValue);
        transaction.commit();
        return value;
    }

    @Override
    public String readConfig(ConfigKey key) {
        return super.readConfig(key);
    }

    @Override
    public String readConfig(ConfigKey key, String appendix) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.readConfig(key, appendix);
    }

    @Override
    public boolean readConfigBool(ConfigKey key) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.readConfigBool(key);
    }

    @Override
    public boolean readConfigBool(ConfigKey key, String appendix) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.readConfigBool(key, appendix);
    }

    @Override
    public boolean readConfigBool(Feature feature) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.readConfigBool(feature);
    }

    @Override
    public int readConfigInt(ConfigKey key) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.readConfigInt(key);
    }

    @Override
    public String readPortalAddress(PortalType portalType, Stage stage) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.readPortalAddress(portalType, stage);
    }

    @Override
    public List<Announcement> findActiveWarnings(PortalType portalType) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.findActiveWarnings(portalType);
    }

    @Override
    public List<String> getAllDirs() {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getAllDirs();
    }

    @Override
    public boolean canFirstWriteSynchronizer(String key) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.canFirstWriteSynchronizer(key);
    }

    @Override
    public Conversation saveConversation(Conversation conversation) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.saveConversation(conversation);
    }

    @Override
    public List<Conversation> loadConversations(Function function, Integer id) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.loadConversations(function, id);
    }
}

class MyAEBFacade extends AEBFacade {
    EntityManager entityManager;
    public MyAEBFacade() {
        super();
    }

    public MyAEBFacade(EntityManager em) {
        super(em);
        entityManager = em;
    }

    @Override
    public AEBBaseInformation findAEBBaseInformation(int id) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.findAEBBaseInformation(id);
    }

    @Override
    public AEBBaseInformation findAEBBaseInformation(int ik, int year, int typ, WorkflowStatus status) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.findAEBBaseInformation(ik, year, typ, status);
    }

    @Override
    public StructureBaseInformation findStructureBaseInformation(int id) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.findStructureBaseInformation(id);
    }

    @Override
    public List<AEBBaseInformation> getAllByStatus(WorkflowStatus status) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getAllByStatus(status);
    }

    @Override
    public List<StructureBaseInformation> getAllStructureBaseInformations() {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getAllStructureBaseInformations();
    }

    @Override
    public List<AEBBaseInformation> getAllByStatusAndIk(WorkflowStatus status, int ik, CustomerTyp typ) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getAllByStatusAndIk(status, ik, typ);
    }

    @Override
    public List<AEBBaseInformation> getAllByStatusAndIk(List<WorkflowStatus> status, Set<Integer> iks, CustomerTyp typ) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getAllByStatusAndIk(status, iks, typ);
    }

    @Override
    public boolean structureBaseInformaionAvailable(int ik) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.structureBaseInformaionAvailable(ik);
    }

    @Override
    public boolean ikHasModelIntention(int ik) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.ikHasModelIntention(ik);
    }

    @Override
    public StructureBaseInformation getStructureBaseInformationByIk(int ik) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getStructureBaseInformationByIk(ik);
    }

    @Override
    public Set<Integer> retrievePossibleIks(Set<Integer> allowedIks, CustomerTyp typ) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.retrievePossibleIks(allowedIks, typ);
    }

    @Override
    public List<Integer> getPossibleDataYears() {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getPossibleDataYears();
    }

    @Override
    public List<Integer> getUsedDataYears(int ik, CustomerTyp typ) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getUsedDataYears(ik, typ);
    }

    @Override
    public List<Integer> getUsedDataYears(int ik) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getUsedDataYears(ik);
    }

    @Override
    public AEBBaseInformation save(AEBBaseInformation aebBaseInformation) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.save(aebBaseInformation);
    }

    @Override
    public StructureBaseInformation save(StructureBaseInformation info) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.save(info);
    }

    @Override
    public HospitalComparisonInfo save(HospitalComparisonInfo info) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.save(info);
    }

    @Override
    public InekComparisonJob saveInekComparison(InekComparisonJob info) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        InekComparisonJob inekComparisonJob = super.saveInekComparison(info);
        transaction.commit();
        return inekComparisonJob;
    }

    @Override
    public HospitalComparisonJob save(HospitalComparisonJob job) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.save(job);
    }

    @Override
    public void deleteBaseInformation(AEBBaseInformation info) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //super.deleteBaseInformation(info);
    }

    @Override
    public List<OccupationalCategory> getOccupationalCategories() {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getOccupationalCategories();
    }

    @Override
    public void insertOrUpdatePsyGroup(int ik, int year, PsyGroup psyGroup) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //super.insertOrUpdatePsyGroup(ik, year, psyGroup);
    }

    @Override
    public Optional<HospitalComparisonInfo> getHospitalComparisonInfoByHcId(String hcId) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getHospitalComparisonInfoByHcId(hcId);
    }

    @Override
    public List<HospitalComparisonInfo> getHosptalComparisonInfoByAccount(Account acc) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getHosptalComparisonInfoByAccount(acc);
    }

    @Override
    public PsyGroup getPsyGroupByIkAndYear(int ik, int year) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getPsyGroupByIkAndYear(ik, year);
    }

    @Override
    public List<Integer> getAebIdsForEvaluationGroup2_3_8_9(String stateIds, int year, PsyGroup psyGroup) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getAebIdsForEvaluationGroup2_3_8_9(stateIds, year, psyGroup);
    }

    @Override
    public List<Integer> getAebIdsForEvaluationGroup1_7(String stateIds, int year, PsyGroup psyGroup) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getAebIdsForEvaluationGroup1_7(stateIds, year, psyGroup);
    }

    @Override
    public List<Integer> getAebIdsForEvaluationGroup5_6(String stateIds, int year) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getAebIdsForEvaluationGroup5_6(stateIds, year);
    }

    @Override
    public List<Integer> getAebIdsForEvaluationGroup4(String stateIds, int year) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getAebIdsForEvaluationGroup4(stateIds, year);
    }

    @Override
    public int getAebIdForEvaluationHospital2_3_5_6_8_9(int ik, int year) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getAebIdForEvaluationHospital2_3_5_6_8_9(ik, year);
    }

    @Override
    public int getAebIdForEvaluationHospital1_4_7(int ik, int year) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getAebIdForEvaluationHospital1_4_7(ik, year);
    }

    @Override
    public Optional<HospitalComparisonJob> getOldestNewJob() {
        return Optional.empty();
        //return super.getOldestNewJob();
    }

    @Override
    public Optional<InekComparisonJob> getOldestNewInekJob() {
        return super.getOldestNewInekJob();
    }

    @Override
    public List<HospitalComparisonInfo> getHosptalComparisonInfoByIks(Set<Integer> iks) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.getHosptalComparisonInfoByIks(iks);
    }

    @Override
    public boolean emailIsLkaForStateId(String emailDomain, int stateId) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.emailIsLkaForStateId(emailDomain, stateId);
    }

    @Override
    public List excludedIks() {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.excludedIks();
    }

    @Override
    public boolean ikHasBedsOrPlacesForYear(int ik, int year) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.ikHasBedsOrPlacesForYear(ik, year);
    }

    @Override
    public boolean aebIdIsInAnyEvaluation(int id) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.aebIdIsInAnyEvaluation(id);
    }

    @Override
    public void storeCollision(int aebId1, int aebId2) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //super.storeCollision(aebId1, aebId2);
    }

    @Override
    public InekComparisonJob newInekComparisonJob(Account account, int inekDataYear, String inekAebSendDateUpToConsider) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //return super.newInekComparisonJob(account, inekDataYear, inekAebSendDateUpToConsider);
    }

    @Override
    public void generateInekComparisonHospitals(InekComparisonJob inekComparisonJob) {
        throw new UnsupportedOperationException("findAEBBaseInformation not implemented yet");
        //super.generateInekComparisonHospitals(inekComparisonJob);
    }
}

