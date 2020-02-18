package org.inek.dataportal.api.helper;

public class PortalConstants {

    public static final int HTTP_PORT = 80;
    public static final int HTTPS_PORT = 443;
    public static final int BUFFER_SIZE = 8192;
    public static final int SERVICE_PORT = 4445;
    public static final String REQUEST_TOKEN = "T:";
    public static final String REQUEST_ID = "I:";   
    public static final String STOP_COMMAND = "STOP";   
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int SECONDS_PER_HOUR = 60 * 60;
    public static final int MILLISECONDS_PER_HOUR = 60 * 60 * 1000;
    public static final int SECONDS_PER_DAY = 24 * 60 * 60;
    public static final long MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000;
    public static final int BINARY_KILO = 1024;
    public static final int BINARY_MEGA = 1024 * 1024;
    public static final int BINARY_GIGA = 1024 * 1024 * 1024;

    public static final String EXCEL_EXTENSION = ".xlsx";
    public static final String ACCOUNT_ID = "accountId";
    public static final String IK = "ik";
    public static final String IKS = "iks";
    public static final String FEATURE = "feature";
    public static final String STATUS = "status";
    public static final String YEAR = "year";
    public static final String YEARS = "years";

    public static final String VAR_LINK = "{link}";
    public static final String VAR_FEATURE = "{feature}";
    public static final String VAR_FEATURES = "{features}";
    public static final String VAR_NAME = "{name}";
    public static final String VAR_EMAIL = "{email}";
    public static final String VAR_ROLE = "{role}";
    public static final String VAR_PHONE = "{phone}";
    public static final String VAR_COMPANY = "{company}";
    public static final String VAR_IK = "{ik}";
    public static final String VAR_SALUTATION = "{formalSalutation}";
    public static final String VAR_ACTIVATIONKEY = "{activationkey}";
    public static final String VAR_USERNAME = "{username}";
    public static final String VAR_LASTNAME = "{lastname}";
    public static final String VAR_TITLE = "{title}";
    public static final String VAR_YEAR = "{year}";
    public static final String VAR_STATE_IDS = "{stateIds}";
    public static final String VAR_PSY_GROUP_ID = "{psyGroupId}";

    public static final String MESSAGE_SEPERATOR = "\r\n\r\n--------------------------------\r\n\r\n";
    public static final String END_PARAGRAPH = "\r\n\r\n";


}
