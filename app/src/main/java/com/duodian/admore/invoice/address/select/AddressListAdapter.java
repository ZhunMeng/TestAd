package com.duodian.admore.invoice.address.select;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duodian.admore.R;
import com.duodian.admore.invoice.AddressInfo;
import com.duodian.admore.invoice.address.OnAddressListActionListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by duodian on 2017/11/27.
 * address list adapter
 */

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.AddressListItemViewHolder> {

    private List<AddressInfo> addressInfoList;
    private Context context;
    private OnAddressListActionListener onAddressListActionListener;

    public void setOnAddressListActionListener(OnAddressListActionListener onAddressListActionListener) {
        this.onAddressListActionListener = onAddressListActionListener;
    }


    public AddressListAdapter(Context context, List<AddressInfo> addressInfoList) {
        this.context = context;
        this.addressInfoList = addressInfoList;
    }


    @Override
    public AddressListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddressListItemViewHolder(LayoutInflater.from(parent
                .getContext()).inflate(R.layout.itemview_address_list, parent, false));
    }

    @Override
    public void onBindViewHolder(AddressListItemViewHolder holder, int position) {
        holder.textView_name.setText(addressInfoList.get(position).getName());
        holder.textView_phone.setText(addressInfoList.get(position).getPhone());
        if (addressInfoList.get(position).isDefSet()) {
            SpannableString spannableString = new SpannableString("[默认地址]");
            spannableString.setSpan(new AbsoluteSizeSpan(14, true), 0, "[默认地址]".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.blue)),
                    0, "[默认地址]".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.textView_realAddress.setText(spannableString);
            holder.textView_realAddress.append(" " + addressInfoList.get(position).getAddress());
        } else {
            holder.textView_realAddress.setText(addressInfoList.get(position).getAddress());
        }
    }

    @Override
    public int getItemCount() {
        return addressInfoList.size();
    }


    class AddressListItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.linear_itemView)
        LinearLayout linear_itemView;

        @BindView(R.id.textView_name)
        TextView textView_name;

        @BindView(R.id.textView_phone)
        TextView textView_phone;

        @BindView(R.id.textView_realAddress)
        TextView textView_realAddress;

        AddressListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            linear_itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position >= 0 && position < addressInfoList.size()) {
                        if (onAddressListActionListener != null) {
                            if (addressInfoList.get(position) != null)
                                onAddressListActionListener.onItemClick(addressInfoList.get(position));
                        }
                    }
                }
            });


        }
    }


}
