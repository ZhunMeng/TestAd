package com.duodian.admore.monitor.detail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
import com.duodian.admore.monitor.bean.OnMonitorPlanActionListener;
import com.duodian.admore.monitor.detail.chart.KeywordMarkerView;
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
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by duodian on 2017/10/30.
 * plan list adapter
 */

public class MonitorPlanDetailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int STATUS_MONITOR_VIEW = 0;
    private static final int STATUS_LINE_CHART = 1;
    private int currentStatus;

    private List<KeywordPromotionInfo> monitorDetailInfoList;
    private AppCompatActivity context;
    private MonitorPlanInfo monitorPlanInfo;

    private OnKeywordPromotionMonitorDetailActionListener onKeywordPromotionMonitorDetailActionListener;

    public void setOnKeywordPromotionMonitorDetailActionListener(OnKeywordPromotionMonitorDetailActionListener onKeywordPromotionMonitorDetailActionListener) {
        this.onKeywordPromotionMonitorDetailActionListener = onKeywordPromotionMonitorDetailActionListener;
    }

    interface OnKeywordPromotionMonitorDetailActionListener {
        void onClick(MonitorPlanInfo monitorPlanInfo, KeywordPromotionInfo keywordPromotionInfo);
    }

    /**
     * header
     */
    private MonitorPlanDetailHeaderViewHolder monitorPlanDetailHeaderViewHolder;
    private KeywordPromotionContentInfo keywordPromotionContentInfo;


    public MonitorPlanDetailListAdapter(AppCompatActivity context, MonitorPlanInfo monitorPlanInfo, List<KeywordPromotionInfo> monitorDetailInfoList) {
        this.context = context;
        this.monitorPlanInfo = monitorPlanInfo;
        this.monitorDetailInfoList = monitorDetailInfoList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == KeywordPromotionInfo.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_monitor_plan_detail_item, parent, false);
            return new MonitorPlanDetailItemViewHolder(view);
        } else if (viewType == KeywordPromotionInfo.TYPE_TITLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_monitor_plan_detail_title_date, parent, false);
            return new MonitorPlanDetailTitleViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_keyword_promotion_monitor_header, parent, false);
            return new MonitorPlanDetailHeaderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MonitorPlanDetailItemViewHolder) {
            MonitorPlanDetailItemViewHolder monitorPlanDetailItemViewHolder = (MonitorPlanDetailItemViewHolder) holder;
            monitorPlanDetailItemViewHolder.textView_keyword.setText(monitorDetailInfoList.get(position).getKeyword());
            monitorPlanDetailItemViewHolder.textView_totalNum.setText(monitorDetailInfoList.get(position).getNumber() + "");
            monitorPlanDetailItemViewHolder.textView_clickNum.setText(monitorDetailInfoList.get(position).getClickNum() + "");
            monitorPlanDetailItemViewHolder.textView_downNum.setText(monitorDetailInfoList.get(position).getDownloadNum() + "");
            monitorPlanDetailItemViewHolder.textView_passNum.setText(monitorDetailInfoList.get(position).getPassNum() + "");
            monitorPlanDetailItemViewHolder.textView_completionRate.setText(monitorDetailInfoList.get(position).getCompletionRate());

        } else if (holder instanceof MonitorPlanDetailTitleViewHolder) {
            MonitorPlanDetailTitleViewHolder monitorPlanDetailTitleViewHolder = (MonitorPlanDetailTitleViewHolder) holder;
            monitorPlanDetailTitleViewHolder.textView_date.setText(monitorDetailInfoList.get(position).getPutInDate());
        } else if (holder instanceof MonitorPlanDetailHeaderViewHolder) {
            if (monitorPlanDetailHeaderViewHolder == null) {
                monitorPlanDetailHeaderViewHolder = (MonitorPlanDetailHeaderViewHolder) holder;
            }
            monitorPlanDetailHeaderViewHolder.textView_totalNum.setText(String.valueOf(monitorPlanInfo.getTotalNum()));
            monitorPlanDetailHeaderViewHolder.textView_clickNum.setText(String.valueOf(monitorPlanInfo.getClickNum()));
            monitorPlanDetailHeaderViewHolder.textView_downNum.setText(String.valueOf(monitorPlanInfo.getDownNum()));
            monitorPlanDetailHeaderViewHolder.textView_passNum.setText(String.valueOf(monitorPlanInfo.getPassNum()));
            initCharts((MonitorPlanDetailHeaderViewHolder) holder, monitorDetailInfoList.get(position), keywordPromotionContentInfo, monitorPlanInfo);
        }
    }


    @Override
    public int getItemCount() {
        return monitorDetailInfoList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return monitorDetailInfoList.get(position).getItemType();
    }


    class MonitorPlanDetailTitleViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.textView_date)
        TextView textView_date;

        MonitorPlanDetailTitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    class MonitorPlanDetailItemViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.linear_itemView)
        LinearLayout linear_itemView;

        @BindView(R.id.textView_keyword)
        TextView textView_keyword;

        @BindView(R.id.textView_totalNum)
        TextView textView_totalNum;

        @BindView(R.id.textView_clickNum)
        TextView textView_clickNum;

        @BindView(R.id.textView_downNum)
        TextView textView_downNum;

        @BindView(R.id.textView_passNum)
        TextView textView_passNum;

        @BindView(R.id.textView_completionRate)
        TextView textView_completionRate;


        MonitorPlanDetailItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            linear_itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position <= getItemCount() - 1 && position > -1) {
                        if (onKeywordPromotionMonitorDetailActionListener != null) {
                            onKeywordPromotionMonitorDetailActionListener.onClick(monitorPlanInfo, monitorDetailInfoList.get(position));
                        }
                    }
                }
            });
        }
    }


    public void setKeywordPromotionContentInfo(KeywordPromotionContentInfo keywordPromotionContentInfo) {
        this.keywordPromotionContentInfo = keywordPromotionContentInfo;
    }

    /**
     * header
     */
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

    class MonitorPlanDetailHeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_totalNum)
        TextView textView_totalNum;

        @BindView(R.id.textView_clickNum)
        TextView textView_clickNum;

        @BindView(R.id.textView_downNum)
        TextView textView_downNum;

        @BindView(R.id.textView_passNum)
        TextView textView_passNum;


        @BindView(R.id.monitorDetailView)
        MonitorDetailView monitorDetailView;

        @BindView(R.id.lineChart)
        LineChart lineChart;

        @BindView(R.id.imageView_full_screen)
        ImageView imageView_full_screen;

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


        MonitorPlanDetailHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void initCharts(final MonitorPlanDetailHeaderViewHolder holder, KeywordPromotionInfo keywordPromotionInfo, final KeywordPromotionContentInfo keywordPromotionContentInfo,
                            MonitorPlanInfo monitorPlanInfo) {
        Glide.with(context)
                .load(keywordPromotionInfo.getSmallIcon())
                .apply(new RequestOptions().transforms(new GlideRoundTransform(context, 8)))
                .transition(withCrossFade())
                .into(holder.imageView_smallIcon);
        holder.imageView_full_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AppPromotionMonitorActivity.class);
                intent.putExtra("keywordPromotionContentInfo", keywordPromotionContentInfo);
                context.startActivity(intent);
            }
        });
        holder.textView_trackName.setText(monitorPlanInfo.getTrackName());
        holder.monitorDetailView.setNum(monitorPlanInfo.getPassNum(), monitorPlanInfo.getTotalNum());
        holder.monitorDetailView.setNum(890, 1000);
        holder.textView_completionRate.setTextSize(16);
        holder.textView_completionRate.setText(890 + " / " + 1000);
        scaleRate = 16 * 1.0f / 28;
        holder.textView_completionRate.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                holder.textView_completionRate.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                point_trackName_center = new PointF(holder.linear_track.getLeft(), holder.linear_track.getTop());
                point_trackName_left = new PointF(0, holder.linear_track.getTop());

                point_monitorView_center = new PointF(holder.monitorDetailView.getLeft() + holder.monitorDetailView.getWidth() / 2,
                        holder.monitorDetailView.getTop() + holder.monitorDetailView.getHeight() / 2);
                point_monitorView_right = new PointF(holder.frame_completionRate.getLeft() + holder.frame_completionRate.getWidth() / 2,
                        holder.frame_completionRate.getTop() + holder.frame_completionRate.getHeight() / 2);

                pointF_control = new PointF(holder.frame_completionRate.getLeft() + holder.frame_completionRate.getWidth() / 2,
                        holder.monitorDetailView.getTop() + holder.monitorDetailView.getHeight() / 2);
                pointF_control_recover = new PointF(holder.monitorDetailView.getLeft() + holder.monitorDetailView.getWidth() / 2,
                        holder.frame_completionRate.getTop() + holder.frame_completionRate.getHeight() / 2);
                valueAnimator_scale = ValueAnimator.ofObject(new MoveEvaluator(pointF_control), point_monitorView_center, point_monitorView_right);
                valueAnimator_scale.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        PointF pointF = (PointF) animation.getAnimatedValue();
                        holder.monitorDetailView.setTranslationX(pointF.x - point_monitorView_center.x);
                        holder.monitorDetailView.setTranslationY(pointF.y - point_monitorView_center.y);
                        float scale = 1 - animation.getAnimatedFraction();
                        holder.monitorDetailView.setScaleX(scaleRate + (1 - scaleRate) * scale);
                        holder.monitorDetailView.setScaleY(scaleRate + (1 - scaleRate) * scale);
                    }
                });
                valueAnimator_scale.setInterpolator(new AccelerateDecelerateInterpolator());

                valueAnimator_recover = ValueAnimator.ofObject(new MoveEvaluator(pointF_control_recover), point_monitorView_right, point_monitorView_center);
                valueAnimator_recover.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        PointF pointF = (PointF) animation.getAnimatedValue();
                        holder.monitorDetailView.setTranslationX(pointF.x - point_monitorView_center.x);
                        holder.monitorDetailView.setTranslationY(pointF.y - point_monitorView_center.y);
                        float scale = animation.getAnimatedFraction();
                        holder.monitorDetailView.setScaleX(scaleRate + (1 - scaleRate) * scale);
                        holder.monitorDetailView.setScaleY(scaleRate + (1 - scaleRate) * scale);
                    }
                });
                valueAnimator_recover.setInterpolator(new AccelerateDecelerateInterpolator());
            }
        });

        holder.lineChart.post(new Runnable() {
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
                dataSet_genreRank.setColor(context.getResources().getColor(R.color.yellow));
                dataSet_genreRank.setHighLightColor(context.getResources().getColor(R.color.yellow));

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
                dataSet_passNum.setColor(context.getResources().getColor(R.color.green));
                dataSet_passNum.setHighLightColor(context.getResources().getColor(R.color.green));


                XAxis xAxis = holder.lineChart.getXAxis();
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
                xAxis.setLabelCount(8);
                xAxis.setValueFormatter(new IAxisValueFormatter() {

                    private SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);

                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return mFormat.format((long) value);
                    }
                });
                xAxis.setGridColor(Color.argb(30, 255, 255, 255));
                xAxis.setAxisLineColor(Color.argb(30, 255, 255, 255));

                YAxis leftAxis = holder.lineChart.getAxisLeft();
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

                YAxis rightAxis = holder.lineChart.getAxisRight();
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


                holder.lineChart.setData(new LineData(dataSet, dataSet_genreRank, dataSet_passNum));
                holder.lineChart.setPinchZoom(true);
                holder.lineChart.getDescription().setEnabled(false);
                Legend legend = holder.lineChart.getLegend();
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                legend.setDrawInside(false);
                legend.setFormSize(8f);
                legend.setFormToTextSpace(4f);
                legend.setTextColor(Color.argb(200, 255, 255, 255));
                // enable touch gestures
                holder.lineChart.setTouchEnabled(true);

                // enable scaling and dragging
                holder.lineChart.setDragEnabled(true);
                holder.lineChart.setScaleEnabled(true);

                // if disabled, scaling can be done on x- and y-axis separately
                holder.lineChart.setPinchZoom(true);
                holder.lineChart.setAutoScaleMinMaxEnabled(true);
                holder.lineChart.setVisibleXRangeMaximum(16 * 15 * 60 * 1000);
                // set an alternative background color
                // mChart.setBackgroundColor(Color.GRAY);

                dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet_genreRank.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet_passNum.setMode(LineDataSet.Mode.CUBIC_BEZIER);

                // create a custom MarkerView (extend MarkerView) and specify the layout
                // to use for it
                KeywordMarkerView mv = new KeywordMarkerView(context, R.layout.view_keyword_promotion_monitor_chart_pop);
                mv.setChartView(holder.lineChart); // For bounds control
                holder.lineChart.setMarker(mv); // Set the marker to the chart
                holder.lineChart.invalidate();/**/
            }
        });
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
//        translateView(monitorPlanDetailHeaderViewHolder.linear_track, point_trackName_left.x - point_trackName_center.x,
//                point_trackName_left.y - point_trackName_center.y);//移动trackName
        monitorPlanDetailHeaderViewHolder.monitorDetailView.onScale();
        valueAnimator_recover.end();
        valueAnimator_scale.start();
        monitorPlanDetailHeaderViewHolder.lineChart.setAlpha(0f);
        monitorPlanDetailHeaderViewHolder.lineChart.setVisibility(View.VISIBLE);
        monitorPlanDetailHeaderViewHolder.lineChart.clearAnimation();
        monitorPlanDetailHeaderViewHolder.lineChart.animate().alpha(1f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                monitorPlanDetailHeaderViewHolder.lineChart.setVisibility(View.VISIBLE);
                monitorPlanDetailHeaderViewHolder.lineChart.setFocusable(true);
                monitorPlanDetailHeaderViewHolder.lineChart.setTouchEnabled(true);
            }
        }).start();

        monitorPlanDetailHeaderViewHolder.imageView_full_screen.setAlpha(0f);
        monitorPlanDetailHeaderViewHolder.imageView_full_screen.setVisibility(View.VISIBLE);
        monitorPlanDetailHeaderViewHolder.imageView_full_screen.clearAnimation();
        monitorPlanDetailHeaderViewHolder.imageView_full_screen.animate().alpha(1f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                monitorPlanDetailHeaderViewHolder.imageView_full_screen.setVisibility(View.VISIBLE);
            }
        }).start();
    }

    private void hideLineChart() {
//        translateView(monitorPlanDetailHeaderViewHolder.linear_track, 0f, 0f);//移动trackName
        monitorPlanDetailHeaderViewHolder.monitorDetailView.onRecover();
        valueAnimator_scale.end();
        valueAnimator_recover.start();
        monitorPlanDetailHeaderViewHolder.lineChart.clearAnimation();
        monitorPlanDetailHeaderViewHolder.lineChart.animate().alpha(0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                monitorPlanDetailHeaderViewHolder.lineChart.setVisibility(View.INVISIBLE);
            }
        }).start();
        monitorPlanDetailHeaderViewHolder.imageView_full_screen.clearAnimation();
        monitorPlanDetailHeaderViewHolder.imageView_full_screen.animate().alpha(0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                monitorPlanDetailHeaderViewHolder.imageView_full_screen.setVisibility(View.INVISIBLE);
            }
        }).start();

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

    private int getRandomNum(int num) {
        return (int) (new Random().nextFloat() * num);
    }

}
