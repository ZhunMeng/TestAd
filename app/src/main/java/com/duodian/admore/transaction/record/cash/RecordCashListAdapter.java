package com.duodian.admore.transaction.record.cash;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class RecordCashListAdapter extends RecyclerView.Adapter<RecordCashListAdapter.InvoiceListItemViewHolder> {

    private List<RecordCashInfo> recordCashInfoList;
    private Context context;
    private OnCashListActionListener onCashListActionListener;


    public RecordCashListAdapter(Context context, List<RecordCashInfo> recordCashInfoList) {
        this.context = context;
        this.recordCashInfoList = recordCashInfoList;
    }

    public void setOnCashListActionListener(OnCashListActionListener onCashListActionListener) {
        this.onCashListActionListener = onCashListActionListener;
    }

    @Override
    public InvoiceListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InvoiceListItemViewHolder(LayoutInflater.from(parent
                .getContext()).inflate(R.layout.itemview_record_cash_list, parent, false));
    }

    @Override
    public void onBindViewHolder(InvoiceListItemViewHolder holder, int position) {
        holder.textView_typeDesc.setText(recordCashInfoList.get(position).getTypeDesc());
        holder.textView_cdate.setText(recordCashInfoList.get(position).getCdateStr());
        if (recordCashInfoList.get(position).getType() == 1) {
            holder.textView_money.setText("￥ " + recordCashInfoList.get(position).getExpend());
        } else {
            holder.textView_money.setText("￥ " + recordCashInfoList.get(position).getIncome());
        }

    }

    @Override
    public int getItemCount() {
        return recordCashInfoList.size();
    }


    class InvoiceListItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.linear_itemView)
        LinearLayout linear_itemView;

        @BindView(R.id.textView_typeDesc)
        TextView textView_typeDesc;

        @BindView(R.id.textView_cdate)
        TextView textView_cdate;

        @BindView(R.id.textView_money)
        TextView textView_money;

        InvoiceListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            linear_itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position >= 0 && position < recordCashInfoList.size()) {
                        if (onCashListActionListener != null) {
                            if (recordCashInfoList.get(position) != null)
                                onCashListActionListener.onItemClick(recordCashInfoList.get(position));
                        }
                    }
                }
            });

        }
    }


}
