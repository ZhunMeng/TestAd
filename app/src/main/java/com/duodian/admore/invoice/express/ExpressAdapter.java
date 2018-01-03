package com.duodian.admore.invoice.express;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duodian.admore.R;
import com.duodian.admore.invoice.list.InvoiceDetailInfo;
import com.duodian.admore.invoice.list.OnInvoiceListActionListener;
import com.duodian.admore.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by duodian on 2017/11/22.
 * plan list adapter
 */

public class ExpressAdapter extends RecyclerView.Adapter<ExpressAdapter.ExpressItemViewHolder> {

    private List<LogisticsInfo> logisticsInfoList;
    private Context context;


    public ExpressAdapter(Context context, List<LogisticsInfo> logisticsInfoList) {
        this.context = context;
        this.logisticsInfoList = logisticsInfoList;
    }


    @Override
    public ExpressItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressItemViewHolder(LayoutInflater.from(parent
                .getContext()).inflate(R.layout.itemview_express, parent, false));
    }

    @Override
    public void onBindViewHolder(ExpressItemViewHolder holder, int position) {
        holder.textView_statusDesc.setText(logisticsInfoList.get(position).getStatusDescription());
        holder.textView_date.setText(logisticsInfoList.get(position).getDate());
        holder.textView_detail.setText(logisticsInfoList.get(position).getDetails());
        if (position == 0) {
            holder.view_topLine.setVisibility(View.INVISIBLE);
            holder.view_point.setBackground(context.getResources().getDrawable(R.drawable.shape_circle_blue));
            holder.textView_statusDesc.setTextColor(context.getResources().getColor(R.color.blue));
            holder.textView_detail.setTextColor(context.getResources().getColor(R.color.blue));
            holder.textView_date.setTextColor(context.getResources().getColor(R.color.blue));
        } else {
            holder.view_topLine.setVisibility(View.VISIBLE);
            holder.view_point.setBackground(context.getResources().getDrawable(R.drawable.shape_circle_gray));
            holder.textView_statusDesc.setTextColor(context.getResources().getColor(R.color.grayText));
            holder.textView_detail.setTextColor(context.getResources().getColor(R.color.grayText));
            holder.textView_date.setTextColor(context.getResources().getColor(R.color.grayText));
        }
    }

    @Override
    public int getItemCount() {
        return logisticsInfoList.size();
    }


    class ExpressItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.view_topLine)
        View view_topLine;

        @BindView(R.id.view_point)
        View view_point;

        @BindView(R.id.view_bottomLine)
        View view_bottomLine;

        @BindView(R.id.textView_statusDesc)
        TextView textView_statusDesc;

        @BindView(R.id.textView_detail)
        TextView textView_detail;

        @BindView(R.id.textView_date)
        TextView textView_date;

        ExpressItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
