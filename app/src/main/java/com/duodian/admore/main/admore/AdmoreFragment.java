package com.duodian.admore.main.admore;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duodian.admore.R;
import com.duodian.admore.main.BaseFragment;
import com.duodian.admore.main.admore.resourcevoucher.ResourceVoucherActivity;
import com.duodian.admore.main.admore.spreadplancreate.SpreadPlanCreateActivity;
import com.duodian.admore.main.admore.resourceachieve.ResourcePurchaseActivity;
import com.duodian.admore.main.admore.resourcebalance.ResourceBalanceActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdmoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdmoreFragment extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AdmoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdmoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdmoreFragment newInstance(String param1, String param2) {
        AdmoreFragment fragment = new AdmoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @BindView(R.id.balance)
    TextView textViewBalance;

    @BindView(R.id.resourceNum)
    TextView textViewResourceNum;

    @BindView(R.id.creditNum)
    TextView textViewCreditNum;

    @BindView(R.id.voucherNum)
    TextView textViewVoucherNum;

    @BindView(R.id.linear_resourcePurchase)
    LinearLayout linear_resourcePurchase;

    @BindView(R.id.linear_resourceBalance)
    LinearLayout linear_resourceBalance;

    @BindView(R.id.linear_creditNum)
    LinearLayout linear_creditNum;

    @BindView(R.id.linear_voucherNum)
    LinearLayout linear_voucherNum;

    @BindView(R.id.linearLayout_spreadCreate)
    LinearLayout linearLayout_spreadCreate;
    @BindView(R.id.linearLayout_spreadPlan)
    LinearLayout linearLayout_spreadPlan;
    @BindView(R.id.linearLayout_spreadCurrentDay)
    LinearLayout linearLayout_spreadCurrentDay;
    @BindView(R.id.linearLayout_spreadHistory)
    LinearLayout linearLayout_spreadHistory;

    @BindView(R.id.linearLayout_spreadPlayCreate)
    LinearLayout linearLayout_spreadPlayCreate;
    @BindView(R.id.linearLayout_spreadPlayPlan)
    LinearLayout linearLayout_spreadPlayPlan;
    @BindView(R.id.linearLayout_spreadPlayCurrentDay)
    LinearLayout linearLayout_spreadPlayCurrentDay;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admore, container, false);
        ButterKnife.bind(this, view);
        textViewBalance.setText("￥ 888888888.88");
        textViewResourceNum.setText("888888.88");
        textViewCreditNum.setText("Y 888888.88");
        textViewVoucherNum.setText("888888.88");
        linear_resourcePurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResourcePurchaseActivity.class);
                startActivity(intent);
            }
        });
        linear_resourceBalance.setOnClickListener(this);
        linear_creditNum.setOnClickListener(this);
        linear_voucherNum.setOnClickListener(this);
        linear_resourcePurchase.setOnClickListener(this);

        linearLayout_spreadCreate.setOnClickListener(this);
        linearLayout_spreadPlan.setOnClickListener(this);
        linearLayout_spreadCurrentDay.setOnClickListener(this);
        linearLayout_spreadHistory.setOnClickListener(this);

        linearLayout_spreadPlayCreate.setOnClickListener(this);
        linearLayout_spreadPlayPlan.setOnClickListener(this);
        linearLayout_spreadPlayCurrentDay.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        int id = v.getId();
        switch (id) {
            case R.id.linear_resourceBalance:
                intent = new Intent(getActivity(), ResourceBalanceActivity.class);
                break;
            case R.id.linear_creditNum:
                break;
            case R.id.linear_voucherNum:
                intent = new Intent(getActivity(), ResourceVoucherActivity.class);
                break;
            case R.id.linear_resourcePurchase:
                intent = new Intent(getActivity(), ResourcePurchaseActivity.class);
                break;
            case R.id.linearLayout_spreadCreate:
                intent = new Intent(getActivity(), SpreadPlanCreateActivity.class);
                break;
            case R.id.linearLayout_spreadPlan:
                break;
            case R.id.linearLayout_spreadCurrentDay:
                break;
            case R.id.linearLayout_spreadHistory:
                break;
            case R.id.linearLayout_spreadPlayCreate:
                break;
            case R.id.linearLayout_spreadPlayPlan:
                break;
            case R.id.linearLayout_spreadPlayCurrentDay:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
