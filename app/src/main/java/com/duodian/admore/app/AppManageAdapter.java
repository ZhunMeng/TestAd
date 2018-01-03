package com.duodian.admore.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duodian.admore.R;
import com.duodian.admore.plan.OnPlanActionListener;
import com.duodian.admore.plan.bean.PlanKeywordInfo;
import com.duodian.admore.utils.GlideRoundTransform;
import com.duodian.admore.utils.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by duodian on 2017/12/04.
 * app manage adapter
 */

public class AppManageAdapter extends RecyclerView.Adapter<AppManageAdapter.AppManageViewHolder> {

    private List<AppInfo> appInfoList;
    private Context context;
    private OnAppActionListener onAppActionListener;

    public AppManageAdapter(Context context, List<AppInfo> appInfoList) {
        this.context = context;
        this.appInfoList = appInfoList;
    }

    public void setOnAppActionListener(OnAppActionListener onAppActionListener) {
        this.onAppActionListener = onAppActionListener;
    }

    @Override
    public AppManageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_app_manage, parent, false);
        return new AppManageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppManageViewHolder holder, final int position) {
        Glide.with(context)
                .load(appInfoList.get(position).getSmallIcon())
                .apply(new RequestOptions().transforms(new GlideRoundTransform(context, 8)))
                .transition(withCrossFade())
                .into(holder.imageView_app);
        holder.textView_trackName.setText(appInfoList.get(position).getTrackName());
        holder.textView_trackId.setText(appInfoList.get(position).getTrackId());
        holder.textView_statusStr.setText(appInfoList.get(position).getStatusStr());
        holder.textView_price.setText(appInfoList.get(position).getFormattedPrice());
        if (appInfoList.get(position).getStatus() == 0) {
            holder.textView_statusStr.setTextColor(context.getResources().getColor(R.color.yellow));
        } else if (appInfoList.get(position).getStatus() == 1) {
            holder.textView_statusStr.setTextColor(context.getResources().getColor(R.color.green));
        }
        if (appInfoList.get(position).getDown() == 1) {//已下架
            holder.textView_statusStr.setTextColor(context.getResources().getColor(R.color.red));
            holder.textView_statusStr.setText("已下架");
        }
    }


    @Override
    public int getItemCount() {
        return appInfoList.size();
    }


    class AppManageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView_app)
        ImageView imageView_app;

        @BindView(R.id.textView_trackName)
        TextView textView_trackName;

        @BindView(R.id.textView_trackId)
        TextView textView_trackId;

        @BindView(R.id.textView_statusStr)
        TextView textView_statusStr;

        @BindView(R.id.textView_price)
        TextView textView_price;

        @BindView(R.id.button_delete)
        Button button_delete;


        AppManageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            button_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position >= 0 && position < appInfoList.size()) {
                        if (onAppActionListener != null) {
                            LogUtil.e("tag", appInfoList.get(position).getUserAppId()+"");
                            onAppActionListener.onDelete(appInfoList.get(position).getUserAppId());
                        }
                    }
                }
            });
        }
    }


}
