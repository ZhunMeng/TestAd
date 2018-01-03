package com.duodian.admore.play.today;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duodian.admore.R;
import com.duodian.admore.plan.bean.PlanKeywordInfo;
import com.duodian.admore.play.OnPlayPlanActionListener;
import com.duodian.admore.play.today.PlayPlanTodayInfo;
import com.duodian.admore.utils.GlideRoundTransform;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by duodian on 2017/11/30.
 * play plan today list adapter
 */

public class PlayPlanTodayListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PlayPlanTodayInfo> playPlanTodayInfoList;
    private Context context;
    private int titleType;


    private OnPlayPlanActionListener onPlayPlanActionListener;

    public PlayPlanTodayListAdapter(Context context, List<PlayPlanTodayInfo> playPlanTodayInfoList) {
        this.context = context;
        this.playPlanTodayInfoList = playPlanTodayInfoList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PlanKeywordInfo.TYPE_TITLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_play_plan_title, parent, false);
            return new PlayPlanTodayListTitleViewHolder(view);
        } else if (viewType == PlanKeywordInfo.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_play_plan_today_item, parent, false);
            return new PlayPlanTodayListItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_play_plan_title_date, parent, false);
            return new PlayPlanTodayListDateViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PlayPlanTodayListTitleViewHolder) {
            PlayPlanTodayListTitleViewHolder planListTitleViewHolder = (PlayPlanTodayListTitleViewHolder) holder;
            Glide.with(context)
                    .load(playPlanTodayInfoList.get(position).getSmallIcon())
                    .apply(new RequestOptions().transforms(new GlideRoundTransform(context, 8)))
                    .transition(withCrossFade())
                    .into(planListTitleViewHolder.imageView_smallIcon);
            Glide.with(context)
                    .load(playPlanTodayInfoList.get(position).getBannerLink())
                    .apply(new RequestOptions().transforms(new GlideRoundTransform(context, 8)))
                    .transition(withCrossFade())
                    .into(planListTitleViewHolder.imageView_banner);
            planListTitleViewHolder.textView_trackName.setText(playPlanTodayInfoList.get(position).getTrackName());
        } else if (holder instanceof PlayPlanTodayListItemViewHolder) {
            PlayPlanTodayListItemViewHolder planListItemViewHolder = (PlayPlanTodayListItemViewHolder) holder;
            planListItemViewHolder.textView_content.setText(playPlanTodayInfoList.get(position).getContent());
            planListItemViewHolder.textView_time_date.setText(playPlanTodayInfoList.get(position).getSpreadDate());
            planListItemViewHolder.textView_totalNum.setText(String.valueOf(playPlanTodayInfoList.get(position).getNumber()));
            planListItemViewHolder.textView_clickNum.setText(String.valueOf(playPlanTodayInfoList.get(position).getClickNum()));
            planListItemViewHolder.textView_passNum.setText(String.valueOf(playPlanTodayInfoList.get(position).getPassNum()));

        } else if (holder instanceof PlayPlanTodayListDateViewHolder) {
            PlayPlanTodayListDateViewHolder planListDateViewHolder = (PlayPlanTodayListDateViewHolder) holder;
            planListDateViewHolder.textView_date.setText(playPlanTodayInfoList.get(position).getYmd());
        }
    }


    @Override
    public int getItemCount() {
        return playPlanTodayInfoList.size();
    }


    public void setOnPlayPlanActionListener(OnPlayPlanActionListener onPlayPlanActionListener) {
        this.onPlayPlanActionListener = onPlayPlanActionListener;
    }


    @Override
    public int getItemViewType(int position) {
        return playPlanTodayInfoList.get(position).getItemType();
    }

    class PlayPlanTodayListDateViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.textView_date)
        TextView textView_date;

        PlayPlanTodayListDateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class PlayPlanTodayListTitleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView_smallIcon)
        ImageView imageView_smallIcon;

        @BindView(R.id.textView_trackName)
        TextView textView_trackName;

        @BindView(R.id.imageView_banner)
        ImageView imageView_banner;

        PlayPlanTodayListTitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class PlayPlanTodayListItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_content)
        TextView textView_content;

        @BindView(R.id.textView_time_date)
        TextView textView_time_date;

        @BindView(R.id.textView_totalNum)
        TextView textView_totalNum;

        @BindView(R.id.textView_clickNum)
        TextView textView_clickNum;

        @BindView(R.id.textView_passNum)
        TextView textView_passNum;


        PlayPlanTodayListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
