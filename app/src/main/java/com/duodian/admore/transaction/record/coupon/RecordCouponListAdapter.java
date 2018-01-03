package com.duodian.admore.transaction.record.coupon;

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

public class RecordCouponListAdapter extends RecyclerView.Adapter<RecordCouponListAdapter.CouponListItemViewHolder> {

    private List<RecordCouponInfo> recordCouponInfoList;
    private Context context;
    private OnCouponListActionListener onCouponListActionListener;


    public RecordCouponListAdapter(Context context, List<RecordCouponInfo> recordCouponInfoList) {
        this.context = context;
        this.recordCouponInfoList = recordCouponInfoList;
    }

    public void setOnCouponListActionListener(OnCouponListActionListener onCouponListActionListener) {
        this.onCouponListActionListener = onCouponListActionListener;
    }

    @Override
    public CouponListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CouponListItemViewHolder(LayoutInflater.from(parent
                .getContext()).inflate(R.layout.itemview_record_coupon_list, parent, false));
    }

    @Override
    public void onBindViewHolder(CouponListItemViewHolder holder, int position) {
        holder.textView_remark.setText(recordCouponInfoList.get(position).getRemark());
        holder.textView_cdate.setText(recordCouponInfoList.get(position).getCdateStr());
        holder.textView_expendStr.setText(recordCouponInfoList.get(position).getExpendStr());
    }

    @Override
    public int getItemCount() {
        return recordCouponInfoList.size();
    }


    class CouponListItemViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.linear_itemView)
        LinearLayout linear_itemView;

        @BindView(R.id.textView_remark)
        TextView textView_remark;

        @BindView(R.id.textView_cdate)
        TextView textView_cdate;

        @BindView(R.id.textView_expendStr)
        TextView textView_expendStr;


        CouponListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            linear_itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position >= 0 && position < recordCouponInfoList.size()) {
                        if (onCouponListActionListener != null) {
                            if (recordCouponInfoList.get(position) != null)
                                onCouponListActionListener.onItemClick(recordCouponInfoList.get(position));
                        }
                    }
                }
            });
        }
    }


}
