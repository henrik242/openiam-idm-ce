/*
 * Copyright 2009, OpenIAM LLC 
 * This file is part of the OpenIAM Identity and Access Management Suite
 *
 *   OpenIAM Identity and Access Management Suite is free software: 
 *   you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License 
 *   version 3 as published by the Free Software Foundation.
 *
 *   OpenIAM is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OpenIAM.  If not, see <http://www.gnu.org/licenses/>. *
 */

/**
 *
 */
package org.openiam.idm.srvc.pswd.service;

import java.util.Random;

/**
 * Generates a random string that is used to create a password.  Generated password should comply with common AD policy
 * Minimum of 6 characters
 * English uppercase characters (A through Z).
 * English lowercase characters (a through z).
 * Base 10 digits (0 through 9).
 * Non-alphabetic characters (for example, !, $, #, %).
 *
 * @author suneet
 */
public class PasswordGenerator {


    private static final char[] lowerChars = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    private static final char[] upperChars = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    private static final char[] numericChars = {'0','1','2','3','4','5','6','7','8','9'};
    private static final char[] specialChars = {'!','$','@','%','&','{','}','*','#','%','+','-','_','/','?'};

    private static final String charset = "!$@%&{}*#%+-_/?0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final String specialCharset = "!$@%&{}*#%+-_/?";
    private static final String numericCharset = "0123456789";
    private static final String upperCharset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String lowerCharset = "abcdefghijklmnopqrstuvwxyz";
    private String[] args;


    public static String generatePassword(int length) {

        boolean foundRequiredChar = false;

        boolean lcase = false, ucase = false, numchar = false, specialchar = false;


        Random rand = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();

             for (int i = 0; i < length; i++) {

                if (sb.length() >= (length-4) && !foundRequiredChar ) {
                    // we have 4 chars to fill and need to make sure that we meet the password policy

                    if (!specialchar) {
                        getMissingChar( rand, specialCharset, sb );
                        specialchar = true;
                        continue;
                    }
                    if (!numchar) {
                        getMissingChar( rand, numericCharset, sb );
                        numchar = true;
                        continue;
                    }
                    if (!ucase) {
                        getMissingChar( rand, upperCharset, sb );
                        ucase = true;
                        continue;
                    }
                    if (!lcase) {
                        getMissingChar( rand, lowerCharset, sb );
                        lcase = true;
                        continue;
                    }

                }


                int pos = rand.nextInt(charset.length());

                char c = charset.charAt(pos);

                if (contain(lowerChars, c)) {
                    lcase = true;
                }
                if (contain(upperChars, c)) {
                    ucase = true;
                }
                if (contain(numericChars, c)) {
                    numchar = true;
                }
                if (contain(specialChars, c)) {
                    specialchar = true;
                }
                sb.append(c);

                if (lcase && ucase && numchar && specialchar) {
                    foundRequiredChar = true;

                }


            }

        return sb.toString();
    }

    static private boolean contain(char[] referenceString, char c ) {
        for (char ch : referenceString) {
            if (ch == c) {
                return true;

            }

        }
        return false;

    }
    static private void getMissingChar( Random rand, String charset, StringBuilder sb ) {
        int p = rand.nextInt(charset.length());
        sb.append(charset.charAt(p));
    }



}
