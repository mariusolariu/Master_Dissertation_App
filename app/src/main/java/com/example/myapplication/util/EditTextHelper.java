package com.example.myapplication.util;

import android.text.TextUtils;
import android.widget.EditText;

public class EditTextHelper {

    public static boolean emptyET(EditText editText, String value, String errorMessage) {
        if (TextUtils.isEmpty(value)) {
            editText.setError(errorMessage);
            return true;
        }

        return false;
    }
}
