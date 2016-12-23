package com.example.admin.memorym1.data;

public class DbUtilities {

    private static final String VALUE_FALSE = "0";
    private static final String VALUE_TRUE = "1";

    public static boolean getBooleanValue(String _value) {

        boolean booleanReturned = false;

        if (null != _value && _value.equalsIgnoreCase(VALUE_TRUE)) {
            booleanReturned = true;
        }

        return booleanReturned;
    }
}
