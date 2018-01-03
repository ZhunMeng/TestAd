package com.duodian.admore.play;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.duodian.admore.R;
import com.duodian.admore.plan.bean.PlanKeywordInfo;
import com.duodian.admore.play.bean.PlayPlanInfo;
import com.duodian.admore.utils.GlideRoundTransform;
import com.duodian.admore.utils.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by duodian on 2017/10/30.
 * play plan list adapter
 */

public class PlayPlanListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PlayPlanInfo> playPlanInfoList;
    private Context context;
    private int titleType;


    private OnPlayPlanActionListener onPlayPlanActionListener;

    public PlayPlanListAdapter(Context context, List<PlayPlanInfo> playPlanInfoList) {
        this.context = context;
        this.playPlanInfoList = playPlanInfoList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PlanKeywordInfo.TYPE_TITLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_play_plan_title, parent, false);
            return new PlayPlanListTitleViewHolder(view);
        } else if (viewType == PlanKeywordInfo.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_play_plan_item, parent, false);
            return new PlayPlanListItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_play_plan_title_date, parent, false);
            return new PlayPlanListDateViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PlayPlanListTitleViewHolder) {
            PlayPlanListTitleViewHolder planListTitleViewHolder = (PlayPlanListTitleViewHolder) holder;
            Glide.with(context)
                    .load(playPlanInfoList.get(position).getSmallIcon())
                    .apply(new RequestOptions().transforms(new GlideRoundTransform(context, 8)))
                    .transition(withCrossFade())
                    .into(planListTitleViewHolder.imageView_smallIcon);
            Glide.with(context)
                    .load(playPlanInfoList.get(position).getBannerLink())
                    .apply(new RequestOptions().transforms(new GlideRoundTransform(context, 8)))
                    .transition(withCrossFade())
                    .into(planListTitleViewHolder.imageView_banner);
            planListTitleViewHolder.textView_trackName.setText(playPlanInfoList.get(position).getTrackName());
        } else if (holder instanceof PlayPlanListItemViewHolder) {
            PlayPlanListItemViewHolder planListItemViewHolder = (PlayPlanListItemViewHolder) holder;
            planListItemViewHolder.textView_content.setText(playPlanInfoList.get(position).getContent());
            planListItemViewHolder.textView_time_date.setText(playPlanInfoList.get(position).getSpreadDateTime());
            planListItemViewHolder.textView_number.setText(playPlanInfoList.get(position).getNumberStr());
            if (playPlanInfoList.get(position).getStatus() == 1) {//开始
                planListItemViewHolder.imageView_action.setImageResource(R.drawable.ic_play_arrow_green_24dp);
            } else if (playPlanInfoList.get(position).getStatus() == 9) {//暂停
                planListItemViewHolder.imageView_action.setImageResource(R.drawable.ic_pause_red_24dp);
            }
            if (playPlanInfoList.get(position).isEnd()) {
                planListItemViewHolder.imageView_action.setImageResource(R.drawable.ic_stop_stop_24dp);
                planListItemViewHolder.imageView_action.setOnClickListener(null);
            } else {
                planListItemViewHolder.imageView_action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onPlayPlanActionListener != null) {
                            onPlayPlanActionListener.OnAction(position);
                        }
                    }
                });
            }
        } else if (holder instanceof PlayPlanListDateViewHolder) {
            PlayPlanListDateViewHolder planListDateViewHolder = (PlayPlanListDateViewHolder) holder;
            planListDateViewHolder.textView_date.setText(playPlanInfoList.get(position).getCdateStr());
        }
    }


    @Override
    public int getItemCount() {
        return playPlanInfoList.size();
    }


    public void setOnPlayPlanActionListener(OnPlayPlanActionListener onPlayPlanActionListener) {
        this.onPlayPlanActionListener = onPlayPlanActionListener;
    }


    @Override
    public int getItemViewType(int position) {
        return playPlanInfoList.get(position).getItemType();
    }

    class PlayPlanListDateViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.textView_date)
        TextView textView_date;

        PlayPlanListDateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class PlayPlanListTitleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView_smallIcon)
        ImageView imageView_smallIcon;

        @BindView(R.id.textView_trackName)
        TextView textView_trackName;

        @BindView(R.id.imageView_banner)
        ImageView imageView_banner;

        PlayPlanListTitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class PlayPlanListItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_content)
        TextView textView_content;

        @BindView(R.id.textView_time_date)
        TextView textView_time_date;


        @BindView(R.id.textView_number)
        TextView textView_number;

        @BindView(R.id.imageView_action)
        ImageView imageView_action;

        int i;

        PlayPlanListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            textView_content.setOnClickListener(new View.OnClickListener() { // TODO: 2017/11/8 精确到每个位置
                @Override
                public void onClick(View v) {
                    if (i % 2 == 0) {
                        textView_content.setEllipsize(null);
                        textView_content.setSingleLine(false);
                    } else {
                        textView_content.setEllipsize(TextUtils.TruncateAt.END);
                        textView_content.setSingleLine(true);
                    }
                    i++;
                }
            });
        }
    }


}
