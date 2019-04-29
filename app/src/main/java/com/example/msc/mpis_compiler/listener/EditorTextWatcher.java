package com.example.msc.mpis_compiler.listener;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by eaz on 19/03/28.
 */

public class EditorTextWatcher implements TextWatcher {

    private final Context context;
    private final EditText etMain;
    private final HashMap<String, Integer> kwMap;

    public EditorTextWatcher(Context context, EditText et, HashMap<String, Integer> hm) {
        this.context = context;
        this.etMain = et;
        this.kwMap = hm;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        String text = s.toString();
        Spannable spannable = etMain.getText();

        int max = 0;
        for (String kw : kwMap.keySet()) {
            if (text.endsWith(kw)) {
                if(kw.length() > max)
                    max = kw.length();
            }
        }

        CharacterStyle[] oldSpans = spannable.getSpans(text.length() - max, spannable.length(), CharacterStyle.class);
        for (CharacterStyle oldSpan : oldSpans) {
            int spanStart = spannable.getSpanStart(oldSpan);
            if (spanStart >= text.length() - max) {
                spannable.removeSpan(oldSpan);
            }
        }
        //Toast.makeText(context, spannable.length() + " " + text.length() + " " + (text.length() - max) + " max= " + max,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String text = s.toString();
        Spannable spannable = etMain.getText();
        int checkLength = 20 < count ? count : 20;
        int textStyleStart = checkLength < text.length() ? text.length() - checkLength : 0;

                                                                                   //copied text
        for (int i = textStyleStart; i < text.length(); i++) {
            for (String kw : kwMap.keySet()) {
                if (text.indexOf(kw, i) >= 0) {
                    spannable.setSpan(new ForegroundColorSpan(kwMap.get(kw)), text.indexOf(kw, i), text.indexOf(kw, i) + kw.length(), 0);
                    spannable.setSpan(new StyleSpan(Typeface.BOLD), text.indexOf(kw, i), text.indexOf(kw, i) + kw.length(), 0);
                    i += kw.length() - 1;
                }
            }
        }
//        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
