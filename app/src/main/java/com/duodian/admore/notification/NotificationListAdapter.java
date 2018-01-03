package com.duodian.admore.notification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duodian.admore.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by duodian on 2017/11/22.
 * plan list adapter
 */

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.NotificationListItemViewHolder> {

    private List<NotificationInfo> notificationInfoList;
    private Context context;
    private OnNotificationActionListener onNotificationActionListener;
    private boolean visibleCheckbox;//是否可以进行全选 编辑已读 或删除的操作
    private boolean selectedAll;//是否全部选择

    public void setVisibleCheckbox(boolean visibleCheckbox) {
        this.visibleCheckbox = visibleCheckbox;
    }

    public boolean isVisibleCheckbox() {
        return visibleCheckbox;
    }

    public boolean isSelectedAll() {
        return selectedAll;
    }

    public void setSelectedAll(boolean selectedAll) {
        this.selectedAll = selectedAll;
    }

    public NotificationListAdapter(Context context, List<NotificationInfo> notificationInfoList) {
        this.context = context;
        this.notificationInfoList = notificationInfoList;
    }

    public void setOnNotificationActionListener(OnNotificationActionListener onNotificationActionListener) {
        this.onNotificationActionListener = onNotificationActionListener;
    }

    @Override
    public NotificationListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotificationListItemViewHolder(LayoutInflater.from(parent
                .getContext()).inflate(R.layout.itemview_notification_list, parent, false));
    }

    @Override
    public void onBindViewHolder(NotificationListItemViewHolder holder, int position) {
        holder.textView_title.setText(notificationInfoList.get(position).getTitle());
        holder.textView_cdate.setText(notificationInfoList.get(position).getCdateStr());
        holder.textView_content.setText(notificationInfoList.get(position).getContent());
        if (notificationInfoList.get(position).getStatus() == 1) {
            holder.label.setVisibility(View.GONE);
        } else {
            holder.label.setVisibility(View.VISIBLE);
        }

        if (visibleCheckbox) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }

        if ((notificationInfoList.get(position).isChecked())) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return notificationInfoList.size();
    }


    class NotificationListItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.linear_itemView)
        LinearLayout linear_itemView;

        @BindView(R.id.checkbox)
        CheckBox checkBox;

        @BindView(R.id.label)
        View label;

        @BindView(R.id.textView_title)
        TextView textView_title;

        @BindView(R.id.textView_cdate)
        TextView textView_cdate;

        @BindView(R.id.textView_content)
        TextView textView_content;


        NotificationListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            linear_itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position >= 0 && position < notificationInfoList.size()) {
                        if (onNotificationActionListener != null) {
                            if (notificationInfoList.get(position) != null)
                                onNotificationActionListener.onItemClick(notificationInfoList.get(position), position);
                        }
                    }
                }
            });

            linear_itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (position >= 0 && position < notificationInfoList.size()) {
                        if (isVisibleCheckbox()) {
                            setVisibleCheckbox(false);
                            for (int i = 0; i < notificationInfoList.size(); i++) {
                                notificationInfoList.get(i).setChecked(false);
                                notifyItemChanged(i);
                            }
                            if (onNotificationActionListener != null) {
                                onNotificationActionListener.onItemLongClick(notificationInfoList.get(position), false);
                            }

                        } else {
                            setVisibleCheckbox(true);
                            notificationInfoList.get(position).setChecked(true);
                            for (int i = 0; i < notificationInfoList.size(); i++) {
                                notifyItemChanged(i);
                            }
                            if (onNotificationActionListener != null) {
                                onNotificationActionListener.onItemLongClick(notificationInfoList.get(position), true);
                            }
                        }
                    }

                    return false;
                }
            });


        }
    }


}
