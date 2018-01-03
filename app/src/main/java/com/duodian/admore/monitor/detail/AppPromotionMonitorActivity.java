package com.duodian.admore.monitor.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.monitor.bean.MonitorPlanInfo;
import com.duodian.admore.monitor.detail.chart.KeywordMarkerView;
import com.duodian.admore.monitor.detail.keyword.KeywordPromotionMonitorInfo;
import com.duodian.admore.utils.Util;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AppPromotionMonitorActivity extends BaseActivity {

    private static final String TAG = "AppPromotionMonitorActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.lineChart)
    LineChart lineChart;

    private List<Entry> entries_totalRank;
    private List<Entry> entries_genreRank;
    private List<Entry> entries_passNum;
    private List<Long> XAxisLong;
    private List<Integer> totalRankLong;
    private List<Integer> genreRankLong;
    private List<Integer> passNumLong;

    private KeywordPromotionContentInfo keywordPromotionContentInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_app_promotion_moniotor);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            keywordPromotionContentInfo = (KeywordPromotionContentInfo) getIntent().getSerializableExtra("keywordPromotionContentInfo");
        }

        int height = Util.getStatusBarHeight(getApplicationContext());
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
        params.topMargin += height;
        toolbar.setLayoutParams(params);
        initViews();
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        setUpChart();
    }


    private void setUpChart() {
        lineChart.post(new Runnable() {
            @Override
            public void run() {
                XAxisLong = new ArrayList<>();//x坐标
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);

                for (String time : keywordPromotionContentInfo.getxAxisdataList()) {
                    try {
                        XAxisLong.add(simpleDateFormat.parse(time).getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                totalRankLong = new ArrayList<>();//总排名
                for (int i = 0; i < keywordPromotionContentInfo.getTotalRankList().size(); i++) {
                    try {
                        totalRankLong.add(Integer.parseInt(keywordPromotionContentInfo.getTotalRankList().get(i)));
                    } catch (Exception e) {
                        if (i == 0) {
                            totalRankLong.add(0);
                        } else {
                            totalRankLong.add(totalRankLong.get(i - 1));
                        }

                    }

                }
                entries_totalRank = new ArrayList<>();
                for (int i = 0; i < keywordPromotionContentInfo.getTotalRankList().size(); i++) {
                    entries_totalRank.add(new Entry(XAxisLong.get(i), totalRankLong.get(i) + getRandomNum(100)));
                }
                LineDataSet dataSet = new LineDataSet(entries_totalRank, "总榜排名");
                dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                dataSet.setDrawValues(false);
                dataSet.setDrawIcons(false);
                dataSet.setDrawCircleHole(false);
                dataSet.setDrawCircles(false);
                dataSet.setCubicIntensity(.05f);
                dataSet.setLineWidth(1.5f);
                dataSet.setColor(Color.argb(255, 255, 255, 255));
                dataSet.setHighLightColor(Color.argb(255, 255, 255, 255));

                genreRankLong = new ArrayList<>();//分榜排名
                for (int i = 0; i < keywordPromotionContentInfo.getGenreRankList().size(); i++) {
                    try {
                        genreRankLong.add(Integer.parseInt(keywordPromotionContentInfo.getGenreRankList().get(i)));
                    } catch (Exception e) {
                        if (i == 0) {
                            genreRankLong.add(0);
                        } else {
                            genreRankLong.add(genreRankLong.get(i - 1));
                        }

                    }

                }
                entries_genreRank = new ArrayList<>();
                for (int i = 0; i < keywordPromotionContentInfo.getGenreRankList().size(); i++) {
                    entries_genreRank.add(new Entry(XAxisLong.get(i), genreRankLong.get(i) + getRandomNum(10)));
                }
                LineDataSet dataSet_genreRank = new LineDataSet(entries_genreRank, "分榜排名");
                dataSet_genreRank.setAxisDependency(YAxis.AxisDependency.LEFT);
                dataSet_genreRank.setDrawValues(false);
                dataSet_genreRank.setDrawIcons(false);
                dataSet_genreRank.setDrawCircleHole(false);
                dataSet_genreRank.setDrawCircles(false);
                dataSet_genreRank.setCubicIntensity(.05f);
                dataSet_genreRank.setLineWidth(1.5f);
                dataSet_genreRank.setColor(getResources().getColor(R.color.yellow));
                dataSet_genreRank.setHighLightColor(getResources().getColor(R.color.yellow));

                passNumLong = new ArrayList<>();//分榜排名
                for (int i = 0; i < keywordPromotionContentInfo.getPassNumList().size(); i++) {
                    try {
                        passNumLong.add(Integer.parseInt(keywordPromotionContentInfo.getPassNumList().get(i)));
                    } catch (Exception e) {
                        if (i == 0) {
                            passNumLong.add(0);
                        } else {
                            passNumLong.add(passNumLong.get(i - 1));
                        }

                    }

                }
                entries_passNum = new ArrayList<>();
                for (int i = 0; i < keywordPromotionContentInfo.getPassNumList().size(); i++) {
                    entries_passNum.add(new Entry(XAxisLong.get(i), passNumLong.get(i) + getRandomNum(1000)));
                }
                LineDataSet dataSet_passNum = new LineDataSet(entries_passNum, "激活量");
                dataSet_passNum.setAxisDependency(YAxis.AxisDependency.RIGHT);
                dataSet_passNum.setDrawValues(false);
                dataSet_passNum.setDrawIcons(false);
                dataSet_passNum.setDrawCircleHole(false);
                dataSet_passNum.setDrawCircles(false);
                dataSet_passNum.setCubicIntensity(.05f);
                dataSet_passNum.setLineWidth(1.5f);
                dataSet_passNum.setColor(getResources().getColor(R.color.green));
                dataSet_passNum.setHighLightColor(getResources().getColor(R.color.green));


                XAxis xAxis = lineChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTextSize(10f);
                xAxis.setTextColor(Color.WHITE);
                xAxis.setDrawAxisLine(true);
                xAxis.setDrawGridLines(true);
                xAxis.setTextColor(Color.argb(60, 255, 255, 255));
                xAxis.setCenterAxisLabels(true);
//                xAxis.setAxisMinimum(0f);
                xAxis.setDrawLabels(true);
                xAxis.setGranularityEnabled(true);
                xAxis.setGranularity(60 * 1000);
                xAxis.setLabelCount(18);
                xAxis.setValueFormatter(new IAxisValueFormatter() {

                    private SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);

                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return mFormat.format((long) value);
                    }
                });
                xAxis.setGridColor(Color.argb(30, 255, 255, 255));
                xAxis.setAxisLineColor(Color.argb(30, 255, 255, 255));

                YAxis leftAxis = lineChart.getAxisLeft();
                leftAxis.setTextColor(Color.argb(60, 255, 255, 255));
//                leftAxis.setAxisMaximum(keywordPromotionContentInfo.getRankMax());
                leftAxis.setAxisMaximum(120);
                leftAxis.setAxisMinimum(-0.9f);
                leftAxis.setDrawGridLines(true);
                leftAxis.setDrawZeroLine(true);
                leftAxis.setGranularityEnabled(true);
                leftAxis.setInverted(true);
                leftAxis.setGridColor(Color.argb(30, 255, 255, 255));
                leftAxis.setZeroLineColor(Color.argb(30, 255, 255, 255));
                leftAxis.setAxisLineColor(Color.argb(30, 255, 255, 255));

                YAxis rightAxis = lineChart.getAxisRight();
                rightAxis.setTextColor(Color.argb(60, 255, 255, 255));
//                rightAxis.setAxisMaximum(keywordPromotionContentInfo.getPassNumMax());
                rightAxis.setAxisMaximum(1200);
                rightAxis.setAxisMinimum(-0.9f);
                rightAxis.setDrawGridLines(true);
                rightAxis.setDrawZeroLine(true);
                rightAxis.setGranularityEnabled(true);
                rightAxis.setGridColor(Color.argb(30, 255, 255, 255));
                rightAxis.setZeroLineColor(Color.argb(30, 255, 255, 255));
                rightAxis.setAxisLineColor(Color.argb(30, 255, 255, 255));


                lineChart.setData(new LineData(dataSet, dataSet_genreRank, dataSet_passNum));
                lineChart.setPinchZoom(true);
                lineChart.getDescription().setEnabled(false);
                Legend legend = lineChart.getLegend();
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                legend.setDrawInside(false);
                legend.setFormSize(8f);
                legend.setFormToTextSpace(4f);
                legend.setTextColor(Color.argb(200, 255, 255, 255));
                // enable touch gestures
                lineChart.setTouchEnabled(true);

                // enable scaling and dragging
                lineChart.setDragEnabled(true);
                lineChart.setScaleEnabled(true);

                // if disabled, scaling can be done on x- and y-axis separately
                lineChart.setPinchZoom(true);
                lineChart.setAutoScaleMinMaxEnabled(true);
                lineChart.setVisibleXRangeMaximum(32 * 15 * 60 * 1000);
                // set an alternative background color
                // mChart.setBackgroundColor(Color.GRAY);

                dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet_genreRank.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet_passNum.setMode(LineDataSet.Mode.CUBIC_BEZIER);

                // create a custom MarkerView (extend MarkerView) and specify the layout
                // to use for it
                KeywordMarkerView mv = new KeywordMarkerView(getApplicationContext(), R.layout.view_keyword_promotion_monitor_chart_pop);
                mv.setChartView(lineChart); // For bounds control
                lineChart.setMarker(mv); // Set the marker to the chart
                lineChart.invalidate();
                lineChart.animateXY(1000, 1000);
            }
        });

    }

    private int getRandomNum(int num) {
        return (int) (new Random().nextFloat() * num);
    }

}
