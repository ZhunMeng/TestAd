package com.duodian.admore.order;

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
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by duodian on 2017/11/22.
 * plan list adapter
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListItemViewHolder> {

    private List<OrderInfo> orderInfoList;
    private Context context;
    private OnOrderListActionListener onOrderListActionListener;

    public OrderListAdapter(Context context, List<OrderInfo> orderInfoList) {
        this.context = context;
        this.orderInfoList = orderInfoList;
    }


    public void setOnOrderListActionListener(OnOrderListActionListener onOrderListActionListener) {
        this.onOrderListActionListener = onOrderListActionListener;
    }


    @Override
    public OrderListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderListItemViewHolder(LayoutInflater.from(parent
                .getContext()).inflate(R.layout.itemview_order_list, parent, false));
    }

    @Override
    public void onBindViewHolder(OrderListItemViewHolder holder, int position) {
        holder.textView_productSubClsDesc.setText(orderInfoList.get(position).getProductSubClsDesc());
        holder.textView_statusDesc.setText(orderInfoList.get(position).getStatusDesc());
        holder.textView_total.setText(String.valueOf(orderInfoList.get(position).getTotal()));
        holder.textView_cdate.setText(orderInfoList.get(position).getCdateStr());
        if (orderInfoList.get(position).getStatus() == 0) {
            holder.textView_payment.setVisibility(View.VISIBLE);
            holder.textView_statusDesc.setTextColor(context.getResources().getColor(R.color.blue));
        } else {
            holder.textView_payment.setVisibility(View.GONE);
            holder.textView_statusDesc.setTextColor(context.getResources().getColor(R.color.green));
        }
    }

    @Override
    public int getItemCount() {
        return orderInfoList.size();
    }


    class OrderListItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.linear_itemView)
        LinearLayout linear_itemView;

        @BindView(R.id.textView_productSubClsDesc)
        TextView textView_productSubClsDesc;

        @BindView(R.id.textView_statusDesc)
        TextView textView_statusDesc;

        @BindView(R.id.textView_total)
        TextView textView_total;

        @BindView(R.id.textView_cdate)
        TextView textView_cdate;

        @BindView(R.id.textView_payment)
        TextView textView_payment;

        OrderListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            linear_itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position >= 0 && position < orderInfoList.size()) {
                        if (onOrderListActionListener != null) {
                            if (orderInfoList.get(position) != null)
                                onOrderListActionListener.onItemClick(orderInfoList.get(position));
                        }
                    }
                }
            });

            textView_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position >= 0 && position < orderInfoList.size()) {
                        if (onOrderListActionListener != null) {
                            if (orderInfoList.get(position) != null)
                                onOrderListActionListener.onPayment(orderInfoList.get(position), position);
                        }
                    }
                }
            });


        }
    }


}
