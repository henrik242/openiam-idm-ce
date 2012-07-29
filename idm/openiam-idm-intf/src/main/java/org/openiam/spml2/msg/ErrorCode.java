
package org.openiam.spml2.msg;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ErrorCode.
 * 
 *
 */
@XmlType(name = "ErrorCode")
@XmlEnum
public enum ErrorCode {

    @XmlEnumValue("malformedRequest")
    MALFORMED_REQUEST("malformedRequest"),

    @XmlEnumValue("unsupportedOperation")
    UNSUPPORTED_OPERATION("unsupportedOperation"),

    @XmlEnumValue("unsupportedIdentifierType")
    UNSUPPORTED_IDENTIFIER_TYPE("unsupportedIdentifierType"),

    @XmlEnumValue("noSuchIdentifier")
    NO_SUCH_IDENTIFIER("noSuchIdentifier"),

    @XmlEnumValue("customError")
    CUSTOM_ERROR("customError"),

    @XmlEnumValue("otherError")
    OTHER_ERROR("otherError"),

    @XmlEnumValue("directoryError")
    DIRECTORY_ERROR("directoryError"),

    @XmlEnumValue("namingException")
    NAMING_EXCEPTION("namingException"),

    @XmlEnumValue("operationNotSupportedException")
    OPERATION_NOT_SUPPORTED_EXCEPTION("operationNotSupportedException"),

    @XmlEnumValue("sqlError")
    SQL_ERROR("sqlError"),

     @XmlEnumValue("connectorError")
    CONNECTOR_ERROR("connectorError"),

    @XmlEnumValue("invalidConfiguration")
    INVALID_CONFIGURATION("invalidConfiguration"),


    @XmlEnumValue("unsupportedExecutionMode")
    UNSUPPORTED_EXECUTION_MODE("unsupportedExecutionMode"),

    @XmlEnumValue("invalidContainment")
    INVALID_CONTAINMENT("invalidContainment"),

    @XmlEnumValue("noSuchRequest")
    NO_SUCH_REQUEST("noSuchRequest"),

    @XmlEnumValue("noSuchObject")
    NO_SUCH_OBJECT("noSuchObject"),

    @XmlEnumValue("unsupportedSelectionType")
    UNSUPPORTED_SELECTION_TYPE("unsupportedSelectionType"),

    @XmlEnumValue("resultSetToLarge")
    RESULT_SET_TO_LARGE("resultSetToLarge"),

    @XmlEnumValue("unsupportedProfile")
    UNSUPPORTED_PROFILE("unsupportedProfile"),

    @XmlEnumValue("invalidIdentifier")
    INVALID_IDENTIFIER("invalidIdentifier"),

    @XmlEnumValue("alreadyExists")
    ALREADY_EXISTS("alreadyExists"),

    @XmlEnumValue("invalidManagedSysId")
    INVALID_MANAGED_SYS_ID("invalidManagedSysId"),

    @XmlEnumValue("userLimitReached")
    USER_LIMIT_REACHED("userLimitReached"),


    @XmlEnumValue("operationError")
    OPERATIONS_ERROR("operationsError"),

    @XmlEnumValue("protocolError")
    PROTOCOL_ERROR("protocolError"),

    @XmlEnumValue("timeLimitExceeded")
    TIMELIMIT_EXCEEDED("timeLimitExceeded"),

    @XmlEnumValue("sizeLimitExceeded")
    SIZELIMIT_EXCEEDED("sizeLimitExceeded"),

    @XmlEnumValue("compareFailed")
    COMPARE_FAILED("compareFailed"),

    @XmlEnumValue("authenticationFailed")
    AUTHENTICATION_FAILED("authenticationFailed"),

    @XmlEnumValue("sessionInvalid")
    SESSION_INVALID("sessionInvalid"),

    @XmlEnumValue("constraintViolation")
    CONSTRAINT_VIOLATION("constraintViolation"),

    @XmlEnumValue("noSuchAttribute")
    NO_SUCH_ATTRIBUTE("noSuchAttribute"),

    @XmlEnumValue("unDefinedType")
    UNDEFINED_TYPE("unDefinedType"),

    @XmlEnumValue("insufficientRights")
    INSUFFICIENT_RIGHTS("insufficientRights"),

    @XmlEnumValue("systemUnavailable")
    SYSTEM_UNAVAILABLE("systemUnavailable"),

    @XmlEnumValue("objectClassViolation")
    OBJECT_CLASS_VIOLATION("objectClassViolation"),

    @XmlEnumValue("noResultsReturned")
    NO_RESULTS_RETURNED("noResultsReturned"),


    @XmlEnumValue("containerNotEmpty")
    CONTAINER_NOT_EMPTY("containerNotEmpty");

    private final String value;

    ErrorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ErrorCode fromValue(String v) {
        for (ErrorCode c: ErrorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }


}


