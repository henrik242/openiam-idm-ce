package org.openiam.util;

import java.io.UnsupportedEncodingException;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;

public class StringUtil {

    private static final String UTF8 = "UTF-8";

    /**
     * @param encoded Base64 encoded String to be decoded
     * @return Decoded string, or null if input was null or decoding failed
     */
    public static String fromBase64(String encoded) {
        if (encoded == null) {
            return null;
        }
        try {
            return new String(decodeBase64(encoded), UTF8);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * @param plain String to be Base64 encoded
     * @return Base64 encoded String, or null if input was null or encoding failed
     */
    public static String toBase64(String plain) {
        if (plain == null) {
            return null;
        }
        try {
            return encodeBase64String(plain.getBytes(UTF8));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
