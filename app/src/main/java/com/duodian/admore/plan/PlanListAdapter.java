package com.duodian.admore.plan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.duodian.admore.R;
import com.duodian.admore.plan.bean.PlanKeywordInfo;
import com.duodian.admore.utils.GlideRoundTransform;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by duodian on 2017/10/30.
 * plan list adapter
 */

public class PlanListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_LIST = 0;
    public static final int TYPE_TODAY = 1;

    private List<PlanKeywordInfo> planKeywordInfoList;
    private Context context;
    private int titleType;


    private OnPlanActionListener onPlanActionListener;

    public PlanListAdapter(Context context, List<PlanKeywordInfo> planKeywordInfoList, int titleType) {
        this.context = context;
        this.planKeywordInfoList = planKeywordInfoList;
        this.titleType = titleType;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PlanKeywordInfo.TYPE_TITLE) {
            View view = null;
            if (titleType == TYPE_TODAY) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_spread_plan_title_today, parent, false);
            } else if (titleType == TYPE_LIST) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_spread_plan_title, parent, false);
            }
            return new PlanListTitleViewHolder(view);
        } else if (viewType == PlanKeywordInfo.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_spread_plan_item, parent, false);
            return new PlanListItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_spread_plan_title_today_date, parent, false);
            return new PlanListDateViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PlanListTitleViewHolder) {
            PlanListTitleViewHolder planListTitleViewHolder = (PlanListTitleViewHolder) holder;
            Glide.with(context)
                    .load(planKeywordInfoList.get(position).getSmallIcon())
                    .apply(new RequestOptions().transforms(new GlideRoundTransform(context, 8)))
                    .transition(withCrossFade())
                    .into(planListTitleViewHolder.imageView_smallIcon);
            planListTitleViewHolder.textView_trackName.setText(planKeywordInfoList.get(position).getTrackName());
        } else if (holder instanceof PlanListItemViewHolder) {
            PlanListItemViewHolder planListItemViewHolder = (PlanListItemViewHolder) holder;
            planListItemViewHolder.textView_keyword.setText(planKeywordInfoList.get(position).getKeyword());
            planListItemViewHolder.textView_number.setText(planKeywordInfoList.get(position).getNumber() + " 次/下载");
            planListItemViewHolder.textView_time.setText(planKeywordInfoList.get(position).getSpreadTime());
            if (planKeywordInfoList.get(position).getStatus() == 1) {//开始
                planListItemViewHolder.imageView_action.setImageResource(R.drawable.ic_play_arrow_green_24dp);
            } else if (planKeywordInfoList.get(position).getStatus() == 9) {//暂停
                planListItemViewHolder.imageView_action.setImageResource(R.drawable.ic_pause_red_24dp);
            }
            if (planKeywordInfoList.get(position).isEnd()) {
                planListItemViewHolder.imageView_action.setImageResource(R.drawable.ic_stop_stop_24dp);
                planListItemViewHolder.imageView_action.setOnClickListener(null);
            } else {
                planListItemViewHolder.imageView_action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onPlanActionListener != null) {
                            onPlanActionListener.OnAction(position);
                        }
                    }
                });

            }
        } else if (holder instanceof PlanListDateViewHolder) {
            PlanListDateViewHolder planListDateViewHolder = (PlanListDateViewHolder) holder;
            planListDateViewHolder.textView_date.setText(planKeywordInfoList.get(position).getYmd());
        }
    }


    @Override
    public int getItemCount() {
        return planKeywordInfoList.size();
    }


    public void setOnPlanActionListener(OnPlanActionListener onPlanActionListener) {
        this.onPlanActionListener = onPlanActionListener;
    }

    @Override
    public int getItemViewType(int position) {
        return planKeywordInfoList.get(position).getItemType();
    }

    class PlanListDateViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.textView_date)
        TextView textView_date;

        PlanListDateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class PlanListTitleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView_smallIcon)
        ImageView imageView_smallIcon;

        @BindView(R.id.textView_trackName)
        TextView textView_trackName;

        PlanListTitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class PlanListItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_keyword)
        TextView textView_keyword;

        @BindView(R.id.textView_number)
        TextView textView_number;

        @BindView(R.id.textView_time)
        TextView textView_time;

        @BindView(R.id.imageView_action)
        ImageView imageView_action;

        PlanListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


}
