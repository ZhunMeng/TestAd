package com.duodian.admore.monitor.detail;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duodian.admore.R;
import com.duodian.admore.monitor.bean.MonitorPlanInfo;
import com.duodian.admore.utils.GlideRoundTransform;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * MonitorCircleViewFragment
 */
public class MonitorCircleViewFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MonitorCircleViewFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final int STATUS_MONITOR_VIEW = 0;
    private static final int STATUS_LINE_CHART = 1;
    private int currentStatus;

    private KeywordPromotionInfo keywordPromotionInfo;
    private KeywordPromotionContentInfo keywordPromotionContentInfo;
    private MonitorPlanInfo monitorPlanInfo;

    @BindView(R.id.monitorDetailView)
    MonitorDetailView monitorDetailView;

    @BindView(R.id.lineChart)
    LineChart lineChart;

    @BindView(R.id.imageView_smallIcon)
    ImageView imageView_smallIcon;

    @BindView(R.id.textView_trackName)
    TextView textView_trackName;

    @BindView(R.id.frame_completionRate)
    FrameLayout frame_completionRate;

    @BindView(R.id.textView_completionRate)
    TextView textView_completionRate;

    @BindView(R.id.linear_track)
    LinearLayout linear_track;

    private PointF point_trackName_center;//居中位置
    private PointF point_trackName_left;//居左位置

    private PointF point_monitorView_center;//居中位置
    private PointF point_monitorView_right;//居右位置
    private float scaleRate;

    private PointF pointF_control;
    private PointF pointF_control_recover;

    private ValueAnimator valueAnimator_scale;
    private ValueAnimator valueAnimator_recover;


    private List<Entry> entries_totalRank;
    private List<Entry> entries_genreRank;
    private List<Entry> entries_passNum;
    private List<Long> XAxisLong;
    private List<Integer> totalRankLong;
    private List<Integer> genreRankLong;
    private List<Integer> passNumLong;

    public MonitorCircleViewFragment() {
    }

    public static MonitorCircleViewFragment newInstance(KeywordPromotionInfo keywordPromotionInfo, KeywordPromotionContentInfo keywordPromotionContentInfo,
                                                        MonitorPlanInfo monitorPlanInfo) {
        MonitorCircleViewFragment fragment = new MonitorCircleViewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, keywordPromotionInfo);
        args.putSerializable(ARG_PARAM2, keywordPromotionContentInfo);
        args.putSerializable(ARG_PARAM3, monitorPlanInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            keywordPromotionInfo = (KeywordPromotionInfo) getArguments().getSerializable(ARG_PARAM1);
            keywordPromotionContentInfo = (KeywordPromotionContentInfo) getArguments().getSerializable(ARG_PARAM2);
            monitorPlanInfo = (MonitorPlanInfo) getArguments().getSerializable(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitor_circle_view, container, false);
        ButterKnife.bind(this, view);

        Glide.with(this)
                .load(keywordPromotionInfo.getSmallIcon())
                .apply(new RequestOptions().transforms(new GlideRoundTransform(getActivity(), 8)))
                .transition(withCrossFade())
                .into(imageView_smallIcon);
        textView_trackName.setText(monitorPlanInfo.getTrackName());
        monitorDetailView.setNum(monitorPlanInfo.getPassNum(), monitorPlanInfo.getTotalNum());
        monitorDetailView.setNum(890, 1000);
        monitorDetailView.setOnClickListener(this);
        textView_completionRate.setTextSize(16);
        textView_completionRate.setText(890 + " / " + 1000);
        scaleRate = 16 * 1.0f / 28;
        textView_completionRate.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                textView_completionRate.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                point_trackName_center = new PointF(linear_track.getLeft(), linear_track.getTop());
                point_trackName_left = new PointF(0, linear_track.getTop());

                point_monitorView_center = new PointF(monitorDetailView.getLeft() + monitorDetailView.getWidth() / 2,
                        monitorDetailView.getTop() + monitorDetailView.getHeight() / 2);
                point_monitorView_right = new PointF(frame_completionRate.getLeft() + frame_completionRate.getWidth() / 2,
                        frame_completionRate.getTop() + frame_completionRate.getHeight() / 2);

                pointF_control = new PointF(frame_completionRate.getLeft() + frame_completionRate.getWidth() / 2,
                        monitorDetailView.getTop() + monitorDetailView.getHeight() / 2);
                pointF_control_recover = new PointF(monitorDetailView.getLeft() + monitorDetailView.getWidth() / 2,
                        frame_completionRate.getTop() + frame_completionRate.getHeight() / 2);
                valueAnimator_scale = ValueAnimator.ofObject(new MoveEvaluator(pointF_control), point_monitorView_center, point_monitorView_right);
                valueAnimator_scale.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        PointF pointF = (PointF) animation.getAnimatedValue();
                        monitorDetailView.setTranslationX(pointF.x - point_monitorView_center.x);
                        monitorDetailView.setTranslationY(pointF.y - point_monitorView_center.y);
                        float scale = 1 - animation.getAnimatedFraction();
                        monitorDetailView.setScaleX(scaleRate + (1 - scaleRate) * scale);
                        monitorDetailView.setScaleY(scaleRate + (1 - scaleRate) * scale);
                    }
                });
                valueAnimator_scale.setInterpolator(new AccelerateDecelerateInterpolator());

                valueAnimator_recover = ValueAnimator.ofObject(new MoveEvaluator(pointF_control_recover), point_monitorView_right, point_monitorView_center);
                valueAnimator_recover.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        PointF pointF = (PointF) animation.getAnimatedValue();
                        monitorDetailView.setTranslationX(pointF.x - point_monitorView_center.x);
                        monitorDetailView.setTranslationY(pointF.y - point_monitorView_center.y);
                        float scale = animation.getAnimatedFraction();
                        monitorDetailView.setScaleX(scaleRate + (1 - scaleRate) * scale);
                        monitorDetailView.setScaleY(scaleRate + (1 - scaleRate) * scale);
                    }
                });
                valueAnimator_recover.setInterpolator(new AccelerateDecelerateInterpolator());
            }
        });

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
                dataSet.setCubicIntensity(.1f);
                dataSet.setLineWidth(3f);
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
                dataSet_genreRank.setCubicIntensity(.1f);
                dataSet_genreRank.setLineWidth(3f);
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
                dataSet_passNum.setCubicIntensity(.1f);
                dataSet_passNum.setLineWidth(3f);
                dataSet_passNum.setColor(getResources().getColor(R.color.green));
                dataSet_passNum.setHighLightColor(getResources().getColor(R.color.green));


                XAxis xAxis = lineChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTextSize(10f);
                xAxis.setAxisMaximum(18);
                xAxis.setTextColor(Color.WHITE);
                xAxis.setDrawAxisLine(true);
                xAxis.setDrawGridLines(true);
                xAxis.setTextColor(Color.argb(60, 255, 255, 255));
                xAxis.setCenterAxisLabels(true);
//                xAxis.setAxisMaximum(0f);
                xAxis.setDrawLabels(true);
                xAxis.setSpaceMin(1);
                xAxis.setValueFormatter(new IAxisValueFormatter() {

                    private SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);

                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {

                        long millis = TimeUnit.HOURS.toMillis((long) value);
                        return mFormat.format(new Date(millis));
                    }
                });


                YAxis leftAxis = lineChart.getAxisLeft();
                leftAxis.setTextColor(Color.argb(60, 255, 255, 255));
//                leftAxis.setAxisMaximum(keywordPromotionContentInfo.getRankMax());
                leftAxis.setAxisMaximum(100);
                leftAxis.setAxisMinimum(0);
                leftAxis.setDrawGridLines(true);
                leftAxis.setDrawZeroLine(true);
                leftAxis.setGranularityEnabled(true);

                YAxis rightAxis = lineChart.getAxisRight();
                rightAxis.setTextColor(Color.argb(60, 255, 255, 255));
//                rightAxis.setAxisMaximum(keywordPromotionContentInfo.getPassNumMax());
                rightAxis.setAxisMaximum(1000);
                rightAxis.setAxisMinimum(0);
                rightAxis.setDrawGridLines(true);
                rightAxis.setDrawZeroLine(true);
                rightAxis.setGranularityEnabled(true);

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

                // set an alternative background color
                // mChart.setBackgroundColor(Color.GRAY);

                dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet_genreRank.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet_passNum.setMode(LineDataSet.Mode.CUBIC_BEZIER);

                // create a custom MarkerView (extend MarkerView) and specify the layout
                // to use for it
//                MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//                mv.setChartView(mChart); // For bounds control
//                mChart.setMarker(mv); // Set the marker to the chart
                lineChart.invalidate();
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.monitorDetailView:
                break;
        }
    }

    public void onActivityMenuClick() {
        if (currentStatus == STATUS_MONITOR_VIEW) {
            currentStatus = STATUS_LINE_CHART;
            showLineChart();
        } else if (currentStatus == STATUS_LINE_CHART) {
            currentStatus = STATUS_MONITOR_VIEW;
            hideLineChart();
        }

    }

    private void translateView(View view, float translationX, float translationY) {
        view.animate()
                .translationX(translationX)
                .translationY(translationY)
                .setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }


    private void showLineChart() {
        translateView(linear_track, point_trackName_left.x - point_trackName_center.x,
                point_trackName_left.y - point_trackName_center.y);//移动trackName
        monitorDetailView.onScale();
        valueAnimator_recover.end();
        valueAnimator_scale.start();
        lineChart.setAlpha(0f);
        lineChart.setVisibility(View.VISIBLE);
        lineChart.clearAnimation();
        lineChart.animate().alpha(1f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                lineChart.setVisibility(View.VISIBLE);
                lineChart.setFocusable(true);
                lineChart.setTouchEnabled(true);
            }
        }).start();
    }

    private void hideLineChart() {
        translateView(linear_track, 0f, 0f);//移动trackName
        monitorDetailView.onRecover();
        valueAnimator_scale.end();
        valueAnimator_recover.start();
        lineChart.clearAnimation();
        lineChart.animate().alpha(0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                lineChart.setVisibility(View.INVISIBLE);
            }
        }).start();

    }

    private int getRandomNum(int num) {
        return (int) (new Random().nextFloat() * num);
    }
}
