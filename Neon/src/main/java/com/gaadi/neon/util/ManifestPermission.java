package com.gaadi.neon.util;

/**
 * @author princebatra
 * @version 1.0
 * @since 20/1/17
 */
public class ManifestPermission extends Exception {

    private String msg;

    public ManifestPermission(String message) {
        msg = message;
    }

    @Override
    public String toString() {
        return msg;
    }
}
