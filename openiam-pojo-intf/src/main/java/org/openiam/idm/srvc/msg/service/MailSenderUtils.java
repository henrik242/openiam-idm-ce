package org.openiam.idm.srvc.msg.service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailSenderUtils {

    public static String parseBody(final String body, final Map<String, String> mailParams) {
        String bodyCp = body;
        for(Map.Entry<String,String> entry : mailParams.entrySet()) {
            String pattern = "\\["+entry.getKey()+"\\]";
            bodyCp = bodyCp.replaceAll(pattern, Matcher.quoteReplacement(entry.getValue()));
        }
        return bodyCp;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
