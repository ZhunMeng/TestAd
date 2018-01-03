package com.duodian.admore.monitor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duodian.admore.R;
import com.duodian.admore.monitor.bean.MonitorPlanInfo;
import com.duodian.admore.monitor.bean.OnMonitorPlanActionListener;
import com.duodian.admore.utils.GlideRoundTransform;
import com.duodian.admore.utils.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by duodian on 2017/10/30.
 * plan list adapter
 */

public class MonitorPlanListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<MonitorPlanInfo> monitorPlanInfoList;
    private Context context;

    private OnMonitorPlanActionListener onMonitorPlanActionListener;

    public MonitorPlanListAdapter(Context context, List<MonitorPlanInfo> monitorPlanInfoList) {
        this.context = context;
        this.monitorPlanInfoList = monitorPlanInfoList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MonitorPlanInfo.TYPE_TITLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_monitor_plan_date, parent, false);
            return new MonitorPlanListDateViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_monitor_plan, parent, false);
            return new MonitorPlanListItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MonitorPlanListDateViewHolder) {
            MonitorPlanListDateViewHolder planListTitleViewHolder = (MonitorPlanListDateViewHolder) holder;
            planListTitleViewHolder.textView_date.setText(monitorPlanInfoList.get(position).getYmd());
        } else if (holder instanceof MonitorPlanListItemViewHolder) {
            final MonitorPlanListItemViewHolder planListItemViewHolder = (MonitorPlanListItemViewHolder) holder;
            Glide.with(context)
                    .load(monitorPlanInfoList.get(position).getSmallIcon())
                    .apply(new RequestOptions().transforms(new GlideRoundTransform(context, 8)))
                    .into(planListItemViewHolder.imageView_smallIcon);
            planListItemViewHolder.textView_trackName.setText(monitorPlanInfoList.get(position).getTrackName());
            planListItemViewHolder.textView_totalNum.setText(monitorPlanInfoList.get(position).getTotalNum() + "");
            planListItemViewHolder.textView_clickNum.setText(monitorPlanInfoList.get(position).getClickNum() + "");
            planListItemViewHolder.textView_downNum.setText(monitorPlanInfoList.get(position).getDownNum() + "");
            planListItemViewHolder.textView_passNum.setText(monitorPlanInfoList.get(position).getPassNum() + "");
            planListItemViewHolder.textView_completionRate.setText(monitorPlanInfoList.get(position).getCompletionRate());
            planListItemViewHolder.textView_activationRate.setText(monitorPlanInfoList.get(position).getActivationRate());
            planListItemViewHolder.linear_itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onMonitorPlanActionListener != null) {
                        if (position >= 0 && position < monitorPlanInfoList.size()) {
                            onMonitorPlanActionListener.onClick(monitorPlanInfoList.get(position));
                        }
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return monitorPlanInfoList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return monitorPlanInfoList.get(position).getItemType();
    }


    public void setOnMonitorPlanActionListener(OnMonitorPlanActionListener onMonitorPlanActionListener) {
        this.onMonitorPlanActionListener = onMonitorPlanActionListener;
    }

    class MonitorPlanListDateViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.textView_date)
        TextView textView_date;

        MonitorPlanListDateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    class MonitorPlanListItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.linear_itemView)
        LinearLayout linear_itemView;

        @BindView(R.id.imageView_smallIcon)
        ImageView imageView_smallIcon;

        @BindView(R.id.textView_trackName)
        TextView textView_trackName;

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

        @BindView(R.id.textView_activationRate)
        TextView textView_activationRate;

        MonitorPlanListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            linear_itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if (position <= getItemCount() - 1 && position > -1) {
//                        if (onMonitorPlanActionListener != null) {
//                            onMonitorPlanActionListener.onClick(monitorPlanInfoList.get(position));
//                        }
//                    }
//                }
//            });
        }
    }


}
