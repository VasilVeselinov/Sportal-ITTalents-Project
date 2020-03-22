package sportal.util;

import java.time.format.DateTimeFormatter;

public class GlobalConstants {

    // authorize
    public static final String HAS_AUTHORITY_EDITOR = "hasAuthority('EDITOR')";
    public static final String HAS_AUTHORITY_ADMIN = "hasAuthority('ADMIN')";

    // package name for upload
    public static final String PACKAGE_FOR_PICTURES = "upload_pictures";
    public static final String PACKAGE_FOR_VIDEOS = "upload_videos";
    public static final String PACKAGE_FOR_LOG_FILES = "log_files";

    // date and tame format
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // log massages
    public static final String SUCCESSFUL_VALIDATION = "Successful validation.";
    public static final String SUCCESSFUL_SEND_EMAIL = "Email successfully sent.";
    public static final String SUCCESSFUL_TRANSFER_OF_FILES = "Successful transfer of files.";
    public static final String SUCCESSFUL_DELETE_OF_FILES = "Successful delete of files.";
    public static final String SUCCESSFUL_SAVE_IN_DB = "Successful save in DB.";
    public static final String SUCCESSFUL_RETRIEVAL = "Successful retrieval of database information.";
    public static final String SUCCESSFUL_UPDATE_OF_DB = "Successful update of DB.";
    public static final String SUCCESSFUL_DELETE_FROM_DB = "Successful delete from DB.";
}
