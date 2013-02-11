package org.openiam.idm.srvc.msg.service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MailSenderUtils provides methods to help replace template place holders with actual values.
 * <p/>
 * Example: If we have have the following string:
 * String tmplBody = "Dear [firstName] [lastName]: \n\n";
 * <p/>
 * ParseBody will replace [firstName] with a person firstName.
 */
public class MailSenderUtils {

    public static String parseBody(final String body, final Map<String, String> mailParams) {
        String bodyCp = body;
        for (Map.Entry<String, String> entry : mailParams.entrySet()) {
            String pattern = "\\[" + entry.getKey() + "\\]";
            // null value will cause the replacement method fail.
            if (entry.getValue() != null) {
                bodyCp = bodyCp.replaceAll(pattern, Matcher.quoteReplacement(entry.getValue()));
            } else {
                // dummy value to highlight that there is a problem
                bodyCp = bodyCp.replaceAll(pattern, Matcher.quoteReplacement("#NO VALUE#"));
            }
        }
        return bodyCp;
    }

    public static boolean isEmailArrayValid(String emails) {
        if (emails.contains(",")) {
            boolean valid = true;
            for (String em : emails.split(",")) {
                if (!isEmailValid(em.trim())) {
                    return false;
                }
            }
            return valid;
        } else {
            return isEmailValid(emails);
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
