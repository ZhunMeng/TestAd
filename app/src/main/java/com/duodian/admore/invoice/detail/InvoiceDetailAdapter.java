package com.duodian.admore.invoice.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duodian.admore.R;
import com.duodian.admore.invoice.list.InvoiceBasisInfo;
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

public class InvoiceDetailAdapter extends RecyclerView.Adapter<InvoiceDetailAdapter.InvoiceDetailItemViewHolder> {

    private Context context;
    private List<InvoiceBasisInfo> invoiceBasisInfoList;

    public InvoiceDetailAdapter(Context context, List<InvoiceBasisInfo> invoiceBasisInfoList) {
        this.context = context;
        this.invoiceBasisInfoList = invoiceBasisInfoList;
    }

    @Override
    public InvoiceDetailItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InvoiceDetailItemViewHolder(LayoutInflater.from(parent
                .getContext()).inflate(R.layout.itemview_invoice_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(InvoiceDetailItemViewHolder holder, int position) {
        holder.textView_code.setText(invoiceBasisInfoList.get(position).getCode());
        holder.textView_money.setText("￥" + invoiceBasisInfoList.get(position).getMoney());
        holder.textView_type.setText(invoiceBasisInfoList.get(position).getTypeDesc());
        holder.textView_cdate.setText(Util.formatDate(invoiceBasisInfoList.get(position).getCdate()));
    }

    @Override
    public int getItemCount() {
        return invoiceBasisInfoList.size();
    }


    class InvoiceDetailItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_code)
        TextView textView_code;

        @BindView(R.id.textView_money)
        TextView textView_money;

        @BindView(R.id.textView_type)
        TextView textView_type;

        @BindView(R.id.textView_cdate)
        TextView textView_cdate;


        InvoiceDetailItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


}
