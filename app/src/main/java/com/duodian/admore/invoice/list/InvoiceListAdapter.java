package com.duodian.admore.invoice.list;

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
import com.duodian.admore.plan.OnPlanActionListener;
import com.duodian.admore.plan.bean.PlanKeywordInfo;
import com.duodian.admore.utils.GlideRoundTransform;
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by duodian on 2017/11/22.
 * plan list adapter
 */

public class InvoiceListAdapter extends RecyclerView.Adapter<InvoiceListAdapter.InvoiceListItemViewHolder> {

    private List<InvoiceDetailInfo> invoiceDetailInfoList;
    private Context context;
    private OnInvoiceListActionListener onInvoiceListActionListener;


    public InvoiceListAdapter(Context context, List<InvoiceDetailInfo> invoiceDetailInfoList) {
        this.context = context;
        this.invoiceDetailInfoList = invoiceDetailInfoList;
    }

    public void setOnInvoiceListActionListener(OnInvoiceListActionListener onInvoiceListActionListener) {
        this.onInvoiceListActionListener = onInvoiceListActionListener;
    }

    @Override
    public InvoiceListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InvoiceListItemViewHolder(LayoutInflater.from(parent
                .getContext()).inflate(R.layout.itemview_invoice_list, parent, false));
    }

    @Override
    public void onBindViewHolder(InvoiceListItemViewHolder holder, int position) {
        holder.textView_invoiceTypeDesc.setText(invoiceDetailInfoList.get(position).getInvoiceTypeDesc() == null
                ? "" : invoiceDetailInfoList.get(position).getInvoiceTypeDesc());
        holder.textView_statusDesc.setText(invoiceDetailInfoList.get(position).getStatusDesc() == null
                ? "" : invoiceDetailInfoList.get(position).getStatusDesc());
        if (invoiceDetailInfoList.get(position).getStatus() == InvoiceDetailInfo.STATUS_NEW) {
            holder.textView_statusDesc.setTextColor(context.getResources().getColor(R.color.blue));
            holder.textView_cancellation.setVisibility(View.VISIBLE);
        } else if (invoiceDetailInfoList.get(position).getStatus() == InvoiceDetailInfo.STATUS_VALID) {
            holder.textView_statusDesc.setTextColor(context.getResources().getColor(R.color.green));
            holder.textView_cancellation.setVisibility(View.VISIBLE);
        } else {
            holder.textView_statusDesc.setTextColor(context.getResources().getColor(R.color.red));
            holder.textView_cancellation.setVisibility(View.GONE);
        }
        if (invoiceDetailInfoList.get(position).getExpressStatus() == InvoiceDetailInfo.STATUS_POST) {
            holder.textView_logistics.setVisibility(View.VISIBLE);
        } else {
            holder.textView_logistics.setVisibility(View.GONE);
        }
        holder.textView_code.setText("发票号: " + invoiceDetailInfoList.get(position).getCode());
        holder.textView_amount.setText("￥ " + invoiceDetailInfoList.get(position).getAmount());
        holder.textView_cdate.setText("申请日期: " + Util.format(invoiceDetailInfoList.get(position).getCdate()));
    }

    @Override
    public int getItemCount() {
        return invoiceDetailInfoList.size();
    }


    class InvoiceListItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.linear_itemView)
        LinearLayout linear_itemView;

        @BindView(R.id.textView_invoiceTypeDesc)
        TextView textView_invoiceTypeDesc;

        @BindView(R.id.textView_statusDesc)
        TextView textView_statusDesc;

        @BindView(R.id.textView_code)
        TextView textView_code;

        @BindView(R.id.textView_amount)
        TextView textView_amount;

        @BindView(R.id.textView_cdate)
        TextView textView_cdate;

        @BindView(R.id.textView_cancellation)
        TextView textView_cancellation;

        @BindView(R.id.textView_logistics)
        TextView textView_logistics;

        InvoiceListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            linear_itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position >= 0 && position < invoiceDetailInfoList.size()) {
                        if (onInvoiceListActionListener != null) {
                            if (invoiceDetailInfoList.get(position) != null)
                                onInvoiceListActionListener.onItemClick(invoiceDetailInfoList.get(position));
                        }
                    }
                }
            });

            textView_cancellation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position >= 0 && position < invoiceDetailInfoList.size()) {
                        if (onInvoiceListActionListener != null) {
                            if (invoiceDetailInfoList.get(position).getStatus() == InvoiceDetailInfo.STATUS_NEW ||
                                    invoiceDetailInfoList.get(position).getStatus() == InvoiceDetailInfo.STATUS_VALID)
                                onInvoiceListActionListener.onApplyForCancellation(invoiceDetailInfoList.get(position).getInvoiceId(), position);
                        }
                    }
                }
            });

            textView_logistics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position >= 0 && position < invoiceDetailInfoList.size()) {
                        if (onInvoiceListActionListener != null) {
                            if (invoiceDetailInfoList.get(position).getExpressStatus() == InvoiceDetailInfo.STATUS_POST) {
                                onInvoiceListActionListener.onLogistics(invoiceDetailInfoList.get(position).getExpressNo());
                            }
                        }
                    }
                }
            });
        }
    }


}
