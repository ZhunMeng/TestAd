package com.duodian.admore.monitor.detail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duodian.admore.R;
import com.duodian.admore.monitor.bean.MonitorPlanInfo;
import com.duodian.admore.utils.GlideRoundTransform;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * MonitorLineChartFragment
 */
public class MonitorLineChartFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private KeywordPromotionContentInfo keywordPromotionContentInfo;
    private MonitorPlanInfo monitorPlanInfo;

    @BindView(R.id.lineChart)
    LineChart lineChart;

    @BindView(R.id.imageView_smallIcon)
    ImageView imageView_smallIcon;

    @BindView(R.id.textView_trackName)
    TextView textView_trackName;

    @BindView(R.id.textView_completionRate)
    TextView textView_completionRate;

    private List<Entry> entries_totalRank;
    private List<Entry> entries_genreRank;
    private List<Entry> entries_passNum;

    private List<Long> XAxisLong;
    private List<Integer> totalRankLong;

    public MonitorLineChartFragment() {
    }

    public static MonitorLineChartFragment newInstance(KeywordPromotionContentInfo keywordPromotionContentInfo, MonitorPlanInfo monitorPlanInfo) {
        MonitorLineChartFragment fragment = new MonitorLineChartFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, keywordPromotionContentInfo);
        args.putSerializable(ARG_PARAM2, monitorPlanInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            keywordPromotionContentInfo = (KeywordPromotionContentInfo) getArguments().getSerializable(ARG_PARAM1);
            monitorPlanInfo = (MonitorPlanInfo) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitor_line_chart, container, false);
        ButterKnife.bind(this, view);
        Glide.with(this)
                .load(monitorPlanInfo.getSmallIcon())
                .apply(new RequestOptions().transforms(new GlideRoundTransform(getActivity(), 8)))
                .transition(withCrossFade())
                .into(imageView_smallIcon);
        textView_trackName.setText(monitorPlanInfo.getTrackName());
        textView_completionRate.setText(monitorPlanInfo.getCompletionRate());
        XAxisLong = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);

        for (String time : keywordPromotionContentInfo.getxAxisdataList()) {
            try {
                XAxisLong.add(simpleDateFormat.parse(time).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        totalRankLong = new ArrayList<>();
        for (String num : keywordPromotionContentInfo.getTotalRankList()) {
            try {
                totalRankLong.add(Integer.parseInt(num));
            } catch (Exception e) {
                totalRankLong.add(0);
            }
        }
        entries_totalRank = new ArrayList<>();
        entries_genreRank = new ArrayList<>();
        entries_passNum = new ArrayList<>();
        for (int i = 0; i < keywordPromotionContentInfo.getTotalRankList().size(); i++) {
            entries_totalRank.add(new Entry(XAxisLong.get(i), totalRankLong.get(i)));
        }
        LineDataSet dataSet = new LineDataSet(entries_totalRank, "Label");
        lineChart.setData(new LineData(dataSet));
        lineChart.invalidate();
        return view;
    }

}
