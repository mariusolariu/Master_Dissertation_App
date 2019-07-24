package com.sibot.mentorapp.util;

import android.text.TextUtils;
import android.widget.EditText;

public class EditTextHelper {

    public static boolean isET_empty(EditText editText, String value, String errorMessage) {
        if (TextUtils.isEmpty(value)) {
            editText.setError(errorMessage);
            return true;
        }

        return false;
    }
}
