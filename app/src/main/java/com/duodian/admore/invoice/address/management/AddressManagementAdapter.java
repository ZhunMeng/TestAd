package com.duodian.admore.invoice.address.management;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
 * address  list  management adapter
 */

public class AddressManagementAdapter extends RecyclerView.Adapter<AddressManagementAdapter.AddressManagementItemViewHolder> {

    private List<AddressInfo> addressInfoList;
    private Context context;
    private OnAddressListActionListener onAddressListActionListener;

    public void setOnAddressListActionListener(OnAddressListActionListener onAddressListActionListener) {
        this.onAddressListActionListener = onAddressListActionListener;
    }


    public AddressManagementAdapter(Context context, List<AddressInfo> addressInfoList) {
        this.context = context;
        this.addressInfoList = addressInfoList;
    }


    @Override
    public AddressManagementItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddressManagementItemViewHolder(LayoutInflater.from(parent
                .getContext()).inflate(R.layout.itemview_address_management, parent, false));
    }

    @Override
    public void onBindViewHolder(AddressManagementItemViewHolder holder, int position) {
        holder.textView_name.setText(addressInfoList.get(position).getName());
        holder.textView_phone.setText(addressInfoList.get(position).getPhone());
        if (addressInfoList.get(position).isDefSet()) {
            SpannableString spannableString = new SpannableString("[默认地址]");
            spannableString.setSpan(new AbsoluteSizeSpan(14, true), 0, "[默认地址]".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.blue)),
                    0, "[默认地址]".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.textView_realAddress.setText(spannableString);
            holder.textView_realAddress.append(" " + addressInfoList.get(position).getAddress());
            holder.checkbox_select.setChecked(true);
            holder.checkbox_select.setTextColor(context.getResources().getColor(R.color.blue));
        } else {
            holder.textView_realAddress.setText(addressInfoList.get(position).getAddress());
            holder.checkbox_select.setChecked(false);
            holder.checkbox_select.setTextColor(Color.BLACK);
        }

    }

    @Override
    public int getItemCount() {
        return addressInfoList.size();
    }


    class AddressManagementItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.linear_itemView)
        LinearLayout linear_itemView;

        @BindView(R.id.textView_name)
        TextView textView_name;

        @BindView(R.id.textView_phone)
        TextView textView_phone;

        @BindView(R.id.textView_realAddress)
        TextView textView_realAddress;

        @BindView(R.id.checkbox_select)
        CheckBox checkbox_select;

        @BindView(R.id.button_update)
        Button button_update;

        @BindView(R.id.button_delete)
        Button button_delete;

        AddressManagementItemViewHolder(View itemView) {
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

            checkbox_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position >= 0 && position < addressInfoList.size()) {
                        if (onAddressListActionListener != null) {
                            if (addressInfoList.get(position) != null)
                                onAddressListActionListener.onSetupDefault(addressInfoList.get(position));
                        }
                    }
                }
            });

            button_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position >= 0 && position < addressInfoList.size()) {
                        if (onAddressListActionListener != null) {
                            if (addressInfoList.get(position) != null)
                                onAddressListActionListener.onUpdate(addressInfoList.get(position));
                        }
                    }
                }
            });

            button_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position >= 0 && position < addressInfoList.size()) {
                        if (onAddressListActionListener != null) {
                            if (addressInfoList.get(position) != null)
                                onAddressListActionListener.onDelete(addressInfoList.get(position));
                        }
                    }
                }
            });

        }
    }


}
