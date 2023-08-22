package com.dou.home.shared;
import org.apache.commons.io.FilenameUtils;

public interface CommonStrings {

    String RESP_MSG_SUCCESS                     = "Success";

    String RESP_MSG_NOT_FOUND                   = "Not found";

    String RESP_MSG_RESOURCE_NOT_FOUND          = "Resource not found";

    String RESP_MSG_DOCUMENT_NOT_FOUND          = "Not found document file for agreement";

    String RESP_MSG_SERVER_ERROR                = "Failure processing";

    String RESP_MSG_SAVE_SUCCESS                = "Save Successfully";

    String RESP_MSG_SAVE_FAIL                   = "Save fail";

    String RESP_MSG_MAXMUN_NEW_UNDERWRITER      = "The value New Underwriter Maximun 10 digits";

    String RESP_MSG_NEW_UNDERWRITER_CANNOT_EMPTY     = "New Underwriter cannot be empty";

    String RESP_MSG_UPLOAD_SUCCESS              = "Upload Files Successfully";

    String RESP_MSG_UPLOAD_FAIL                 = "Upload Files Fail. Please try again!";

    String RESP_MSG_MERGING_FAIL                = "Merging fail";

    String RESP_MSG_MERGING_SUCCESS             = "Merging Successfully";

    String RESP_MSG_CACULATE_FAIL               =  "Failed to calculate ";

    String RESP_MSG_GET_DATA_FAIL              =  "Failed to get data ";

    String EMPTY_STRING                         = "";

    String DATE_FORMAT                          = "MM/dd/yyyy";

    String FINNONE_SEARCH_IDNO                  = "IDNO";

    String FINNONE_SEARCH_AGREEMENT             = "AGREEMENT";

    String REPORT_TEMPLATE_EXTENSION            = ".jrxml";

    String REPORT_VIEW_EXTENSION                = ".jasper";

    String DATA_SOURCE_ORCALE                   = "oracle";

    String DATA_SOURCE_HOME                     = "home";

    String FILE_EXTENSION_PDF                   = FilenameUtils.EXTENSION_SEPARATOR + "pdf";

}
