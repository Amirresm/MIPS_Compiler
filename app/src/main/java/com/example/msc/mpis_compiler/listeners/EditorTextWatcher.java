package com.example.msc.mpis_compiler.listeners;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

import com.example.msc.mpis_compiler.R;
import com.example.msc.mpis_compiler.containers.CompileState;
import com.example.msc.mpis_compiler.containers.MapsContainer;
import com.example.msc.mpis_compiler.utilities.Utility;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Amirreza on 25/04/2019.
 */

public class EditorTextWatcher implements TextWatcher {

    private final Context context;
    private final EditText etMain;
    private final MapsContainer maps;
    private final CompileState state;
    int errorColor;
    int labelColor;

    public EditorTextWatcher(Context context, EditText et, MapsContainer hm, CompileState state) {
        this.context = context;
        this.etMain = et;
        this.maps = hm;
        this.state = state;
        errorColor = ContextCompat.getColor(context, R.color.errorColor);
        labelColor = ContextCompat.getColor(context, R.color.labelColor);
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
                if (matcher.end() > matcher.start())
                    ss.setSpan(new ForegroundColorSpan(maps.kwColorMap.get(kw)), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //ss.setSpan(new StyleSpan(Typeface.BOLD), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        for (String kw : maps.labelsColorMap.keySet()) {
            Pattern pattern = Pattern.compile("(?<!\\w)"+kw+"(?!\\w)");
            Matcher matcher = pattern.matcher(ss);
            while (matcher.find()) {
                if (matcher.end() > matcher.start())
                    ss.setSpan(new ForegroundColorSpan(labelColor), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //ss.setSpan(new StyleSpan(Typeface.BOLD), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        for (String kw : maps.errorsColorMap.keySet()) {
            Pattern pattern = Pattern.compile("(?<!\\w)"+kw+"(?!\\w)");
            Matcher matcher = pattern.matcher(ss);
            while (matcher.find()) {
                if (matcher.end() > matcher.start())
                    ss.setSpan(new ForegroundColorSpan(errorColor), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //ss.setSpan(new StyleSpan(Typeface.BOLD), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        {
            Pattern pattern = Pattern.compile("#.*$", Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(ss);
            while (matcher.find()) {
                if (matcher.end() > matcher.start())
                    ss.setSpan(new ForegroundColorSpan(maps.kwColorMap.get("comment")), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //ss.setSpan(new StyleSpan(Typeface.BOLD), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
