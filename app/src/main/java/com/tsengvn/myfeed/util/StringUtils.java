package com.tsengvn.myfeed.util;

import android.text.TextUtils;

/**
 * @author : hienngo
 * @since : Sep 03, 2016.
 */
public class StringUtils {
    public static boolean isEmpty(CharSequence value) {
        if (value == null)
            return true;

        value = trim(value);

        return TextUtils.isEmpty(value);
    }

    public static String trim(CharSequence value) {
        if (value == null)
            return null;
        return value.toString().trim();
    }

}
