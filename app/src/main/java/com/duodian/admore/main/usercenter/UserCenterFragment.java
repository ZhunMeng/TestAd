package com.duodian.admore.main.usercenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.duodian.admore.R;
import com.duodian.admore.main.BaseFragment;
import com.duodian.admore.main.usercenter.settings.SettingsActivity;
import com.duodian.admore.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserCenterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserCenterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserCenterFragment extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UserCenterFragment() {
    }

    public static UserCenterFragment newInstance(String param1, String param2) {
        UserCenterFragment fragment = new UserCenterFragment();
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

    @BindView(R.id.frameLayoutEmail)
    FrameLayout frameLayoutEmail;

    @BindView(R.id.portrait)
    ImageView imageViewPortrait;

    @BindView(R.id.userName)
    TextView textView_userName;

    @BindView(R.id.email)
    TextView textView_email;

    @BindView(R.id.imageViewBlur)
    ImageView imageViewBlur;

    @BindView(R.id.linear_myOrder)
    LinearLayout linear_myOrder;

    @BindView(R.id.linear_orderRecord)
    LinearLayout linear_orderRecord;

    @BindView(R.id.linear_resourceManagement)
    LinearLayout linear_resourceManagement;

    @BindView(R.id.linear_invoiceManagement)
    LinearLayout linear_invoiceManagement;

    @BindView(R.id.linear_appManagement)
    LinearLayout linear_appManagement;

    @BindView(R.id.linear_phone)
    LinearLayout linear_phone;

    @BindView(R.id.linear_certification)
    LinearLayout linear_certification;

    @BindView(R.id.linear_accountManagement)
    LinearLayout linear_accountManagement;

    @BindView(R.id.linear_settings)
    LinearLayout linear_settings;

    private RenderScript rs;
    private ScriptIntrinsicBlur blurScript;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_center, container, false);
        ButterKnife.bind(this, view);
        rs = RenderScript.create(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        }
        initViews();
        return view;
    }

    private void initViews() {
        int height = Util.getStatusBarHeight(getContext().getApplicationContext());
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) frameLayoutEmail.getLayoutParams();
        params.topMargin += height;
        frameLayoutEmail.setLayoutParams(params);
        frameLayoutEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Glide.with(getActivity())
                .load(R.drawable.husky)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(final Drawable resource, Transition<? super Drawable> transition) {
                        imageViewPortrait.setImageDrawable(resource);
                        imageViewPortrait.animate().alpha(1.0f).setDuration(500).start();
                        new Thread() {
                            @Override
                            public void run() {
                                long time = System.currentTimeMillis();
                                Bitmap bitmapScaled = Util.getScaledBitmap(((BitmapDrawable) resource).getBitmap(), 64, 64);
                                final Bitmap bitmap = Util.blurBitmap(getActivity(), bitmapScaled, rs, blurScript, 25);
                                Log.e("time", System.currentTimeMillis() - time + "");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        imageViewBlur.setImageBitmap(bitmap);
                                        imageViewBlur.animate().alpha(1.0f).setDuration(500).start();
                                    }
                                });

                            }
                        }.start();

                    }
                });
        textView_userName.setText("Goldberg");
        textView_email.setText("20178462354@qq.com");
        linear_myOrder.setOnClickListener(this);
        linear_orderRecord.setOnClickListener(this);
        linear_resourceManagement.setOnClickListener(this);
        linear_invoiceManagement.setOnClickListener(this);
        linear_appManagement.setOnClickListener(this);
        linear_phone.setOnClickListener(this);
        linear_certification.setOnClickListener(this);
        linear_accountManagement.setOnClickListener(this);
        linear_settings.setOnClickListener(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        int id = v.getId();
        switch (id) {
            case R.id.linear_myOrder:
                break;
            case R.id.linear_orderRecord:
                break;
            case R.id.linear_resourceManagement:
                break;
            case R.id.linear_invoiceManagement:
                break;
            case R.id.linear_appManagement:
                break;
            case R.id.linear_phone:
                break;
            case R.id.linear_certification:
                break;
            case R.id.linear_accountManagement:
                break;
            case R.id.linear_settings:
                intent = new Intent(getActivity(), SettingsActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
