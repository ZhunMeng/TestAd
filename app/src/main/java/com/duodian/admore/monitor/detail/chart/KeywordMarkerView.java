package com.duodian.admore.monitor.detail.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.duodian.admore.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * Created by duodian on 2017/12/27.
 * keyword marker view
 */

@SuppressLint("ViewConstructor")
public class KeywordMarkerView extends MarkerView {

    private TextView textViewNum;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context        context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public KeywordMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        textViewNum = findViewById(R.id.textViewNum);
    }


    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        textViewNum.setText(String.valueOf((int) e.getY()));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight() * 1.5f);
    }
}
