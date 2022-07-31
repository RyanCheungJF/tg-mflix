package com.tigergraph.tg.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Utility class to decode arguments passed in endpoints which are encoded to contain reserved characters
 */
public class ParameterUtil {

    /**
     * Decodes the encoded parameter and returns it as a readable string
     *
     * @param arg encoded parameter
     * @return decoded parameter
     */
    public static String decodeParameter(String arg) {
        try {
            return URLDecoder.decode(arg, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
