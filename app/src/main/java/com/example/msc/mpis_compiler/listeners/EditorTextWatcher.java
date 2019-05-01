package com.example.msc.mpis_compiler.listeners;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

import com.example.msc.mpis_compiler.containers.CompileState;
import com.example.msc.mpis_compiler.containers.MapsContainer;
import com.example.msc.mpis_compiler.utilities.Utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by eaz on 19/03/28.
 */

public class EditorTextWatcher implements TextWatcher {

    private final Context context;
    private final EditText etMain;
    private final MapsContainer maps;
    private final CompileState state;

    public EditorTextWatcher(Context context, EditText et, MapsContainer hm, CompileState state) {
        this.context = context;
        this.etMain = et;
        this.maps = hm;
        this.state = state;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        state.isScannedForLabels = false;
        if(!Utility.firstScan(maps, state, etMain.getText().toString())) {

        }

        Spannable ss = etMain.getText();
        //remove spans
        for (CharacterStyle span : ss.getSpans(0, ss.length(),CharacterStyle.class))
            ss.removeSpan(span);

        for (String kw : maps.kwColorMap.keySet()) {
            Pattern pattern = Pattern.compile("(?<!\\w)"+kw+"(?!\\w)");
            Matcher matcher = pattern.matcher(ss);
            while (matcher.find()) {
                ss.setSpan(new ForegroundColorSpan(maps.kwColorMap.get(kw)), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //ss.setSpan(new StyleSpan(Typeface.BOLD), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        for (String kw : maps.labelsColorMap.keySet()) {
            Pattern pattern = Pattern.compile("(?<!\\w)"+kw+"(?!\\w)");
            Matcher matcher = pattern.matcher(ss);
            while (matcher.find()) {
                ss.setSpan(new ForegroundColorSpan(maps.labelsColorMap.get(kw)), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //ss.setSpan(new StyleSpan(Typeface.BOLD), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        for (String kw : maps.errorsColorMap.keySet()) {
            Pattern pattern = Pattern.compile("(?<!\\w)"+kw+"(?!\\w)");
            Matcher matcher = pattern.matcher(ss);
            while (matcher.find()) {
                ss.setSpan(new ForegroundColorSpan(Color.RED), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //ss.setSpan(new StyleSpan(Typeface.BOLD), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        {
            Pattern pattern = Pattern.compile("#");
            Matcher matcher = pattern.matcher(ss);
            while (matcher.find()) {
                ss.setSpan(new ForegroundColorSpan(maps.kwColorMap.get("comment")), matcher.start(), ss.toString().indexOf("\n", matcher.start()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //ss.setSpan(new StyleSpan(Typeface.BOLD), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
