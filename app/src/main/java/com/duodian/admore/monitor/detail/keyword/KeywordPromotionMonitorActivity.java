package com.duodian.admore.monitor.detail.keyword;

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
import com.duodian.admore.monitor.detail.KeywordPromotionInfo;
import com.duodian.admore.monitor.detail.chart.KeywordMarkerView;
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

public class KeywordPromotionMonitorActivity extends BaseActivity {

    private static final String TAG = "KeywordPromotionMonitorActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    @BindView(R.id.textView_keywordRank)
    TextView textView_keywordRank;

    @BindView(R.id.textView_heat)
    TextView textView_heat;

    @BindView(R.id.textView_passNum)
    TextView textView_passNum;

    @BindView(R.id.lineChart)
    LineChart lineChart;


    private List<Entry> entries_passNum;
    private List<Entry> entries_genreRank;
    private List<Long> XAxisLong;
    private List<Integer> genreRankLong;
    private List<Integer> passNumLong;


    private MonitorPlanInfo monitorPlanInfo;
    private KeywordPromotionInfo keywordPromotionInfo;
    private boolean isRequesting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_keyword_promotion_moniotor);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            monitorPlanInfo = (MonitorPlanInfo) getIntent().getSerializableExtra("monitorPlanInfo");
            keywordPromotionInfo = (KeywordPromotionInfo) getIntent().getSerializableExtra("keywordPromotionInfo");
            getPlans();
        }

        // add status bar height
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
        textView_title.setText(getResources().getString(R.string.keywordPromotionDetail));

    }

    private void setRequestStatus(final boolean requesting) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isRequesting = requesting;
//                swipeRefreshLayout.setRefreshing(requesting);
//                if (!requesting) {
//                    swipeRefreshLayout.setEnabled(false);
//                }
            }
        });
    }


    private void getPlans() {
        if (isRequesting) return;
        setRequestStatus(true);
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("trackId", monitorPlanInfo.getTrackId());
        params.put("ymd", monitorPlanInfo.getYmd());
        params.put("keyword", keywordPromotionInfo.getKeyword());
        params.put("keywordId", keywordPromotionInfo.getKeywordId());
        Observable<HttpResult<KeywordPromotionMonitorInfo>> observable = iServiceApi.monitorAppRankById(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<KeywordPromotionMonitorInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull HttpResult<KeywordPromotionMonitorInfo> keywordPromotionMonitorInfoHttpResult) {
                        if (shouldReLogin(keywordPromotionMonitorInfoHttpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        KeywordPromotionMonitorInfo keywordPromotionMonitorInfo = keywordPromotionMonitorInfoHttpResult.getResult();
                        setUpChart(keywordPromotionMonitorInfo);
                        setRequestStatus(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        setRequestStatus(false);
                    }

                    @Override
                    public void onComplete() {
                        setRequestStatus(false);
                    }
                });
    }

    private void setUpChart(final KeywordPromotionMonitorInfo keywordPromotionMonitorInfo) {
        lineChart.post(new Runnable() {
            @Override
            public void run() {
                textView_keywordRank.setText(String.valueOf(keywordPromotionMonitorInfo.getKeywordRank()));
                textView_heat.setText(String.valueOf(keywordPromotionMonitorInfo.getHeat()));
                textView_passNum.setText(String.valueOf(keywordPromotionMonitorInfo.getPassNum()));

                XAxisLong = new ArrayList<>();//x坐标
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

                for (String time : keywordPromotionMonitorInfo.getxAxisdatalist()) {
                    try {
//                        time = time.substring(time.length() - 5, time.length());
                        XAxisLong.add(simpleDateFormat.parse(time).getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                genreRankLong = new ArrayList<>();//分榜排名
                for (int i = 0; i < keywordPromotionMonitorInfo.getGenreRankList().size(); i++) {
                    try {
                        genreRankLong.add(Integer.parseInt(keywordPromotionMonitorInfo.getGenreRankList().get(i)));
                    } catch (Exception e) {
                        if (i == 0) {
                            genreRankLong.add(0);
                        } else {
                            genreRankLong.add(genreRankLong.get(i - 1));
                        }

                    }

                }
                entries_genreRank = new ArrayList<>();
                for (int i = 0; i < keywordPromotionMonitorInfo.getGenreRankList().size(); i++) {
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
                for (int i = 0; i < keywordPromotionMonitorInfo.getPassNumList().size(); i++) {
                    try {
                        passNumLong.add(Integer.parseInt(keywordPromotionMonitorInfo.getPassNumList().get(i)));
                    } catch (Exception e) {
                        if (i == 0) {
                            passNumLong.add(0);
                        } else {
                            passNumLong.add(passNumLong.get(i - 1));
                        }

                    }

                }
                entries_passNum = new ArrayList<>();
                for (int i = 0; i < keywordPromotionMonitorInfo.getPassNumList().size(); i++) {
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
                xAxis.setGridColor(Color.argb(30, 255, 255, 255));
                xAxis.setAxisLineColor(Color.argb(30, 255, 255, 255));
                xAxis.setCenterAxisLabels(true);
//                xAxis.setAxisMinimum(0f);
                xAxis.setDrawLabels(true);
                xAxis.setGranularityEnabled(true);
//                xAxis.setGranularity(60 * 1000);
//                xAxis.setLabelCount(8);
                xAxis.setValueFormatter(new IAxisValueFormatter() {

                    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        String time = mFormat.format((long) value);
                        return time.substring(time.length() - 5, time.length());
                    }
                });


                YAxis leftAxis = lineChart.getAxisLeft();
                leftAxis.setTextColor(Color.argb(60, 255, 255, 255));
//                leftAxis.setAxisMaximum(keywordPromotionContentInfo.getRankMax());
                leftAxis.setAxisMaximum(100);
                leftAxis.setAxisMinimum(-0.9f);
                leftAxis.setDrawGridLines(true);
                leftAxis.setGranularityEnabled(true);
                leftAxis.setInverted(true);
                leftAxis.setDrawZeroLine(false);
                leftAxis.setGridColor(Color.argb(30, 255, 255, 255));
                leftAxis.setZeroLineColor(Color.argb(30, 255, 255, 255));
                leftAxis.setAxisLineColor(Color.argb(30, 255, 255, 255));

                YAxis rightAxis = lineChart.getAxisRight();
                rightAxis.setTextColor(Color.argb(60, 255, 255, 255));
//                rightAxis.setAxisMaximum(keywordPromotionContentInfo.getPassNumMax());
                rightAxis.setAxisMaximum(1200);
                rightAxis.setAxisMinimum(-0.9f);
                rightAxis.setDrawGridLines(true);
                rightAxis.setDrawZeroLine(false);
                rightAxis.setGranularityEnabled(true);
                rightAxis.setGridColor(Color.argb(30, 255, 255, 255));
                rightAxis.setZeroLineColor(Color.argb(30, 255, 255, 255));
                rightAxis.setAxisLineColor(Color.argb(30, 255, 255, 255));

                Legend legend = lineChart.getLegend();
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                legend.setDrawInside(false);
                legend.setFormSize(8f);
                legend.setFormToTextSpace(4f);
                legend.setTextColor(Color.argb(200, 255, 255, 255));

                lineChart.setData(new LineData(dataSet_genreRank, dataSet_passNum));
                lineChart.getDescription().setEnabled(false);
                lineChart.setTouchEnabled(true);
                lineChart.setDragEnabled(true);// enable scaling and dragging
                lineChart.setScaleEnabled(true);
                lineChart.setPinchZoom(true);
                lineChart.setAutoScaleMinMaxEnabled(true);
                lineChart.setVisibleXRangeMaximum(16 * 15 * 60 * 1000);

                dataSet_genreRank.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet_passNum.setMode(LineDataSet.Mode.CUBIC_BEZIER);

                KeywordMarkerView mv = new KeywordMarkerView(getApplicationContext(), R.layout.view_keyword_promotion_monitor_chart_pop);
                mv.setChartView(lineChart); // For bounds control
                lineChart.setMarker(mv); // Set the marker to the chart
                lineChart.invalidate();/**/
                lineChart.animateXY(1000, 1000);
            }
        });

    }

    private int getRandomNum(int num) {
        return (int) (new Random().nextFloat() * num);
    }

}
