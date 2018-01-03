package com.duodian.admore.notification;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.notification.detail.NotificationDetailActivity;
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.MyLinearLayoutManager;
import com.duodian.admore.utils.ToastUtil;
import com.duodian.admore.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NotificationListActivity extends BaseActivity implements OnNotificationActionListener, View.OnClickListener {

    private static final String TAG = "NotificationListActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.linear_menuBottom)
    LinearLayout linear_menuBottom;

    @BindView(R.id.button_selectAll)
    Button button_selectAll;

    @BindView(R.id.button_markRead)
    Button button_markRead;

    @BindView(R.id.button_delete)
    Button button_delete;

    private List<NotificationInfo> notificationInfoList;
    private NotificationListAdapter notificationListAdapter;
    private MyLinearLayoutManager myLinearLayoutManager;


    private int currentListNum;//当前list size
    private boolean isRequesting;
    private int totalNum;
    private int pageNum;//起始查询行数 /当前行数
    private AlertDialog.Builder dialogBuilder_markRead;
    private AlertDialog.Builder dialogBuilder_delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textView_title.setText(getResources().getString(R.string.notificationCenter));

        myLinearLayoutManager = new MyLinearLayoutManager(this);
        recyclerView.setLayoutManager(myLinearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        notificationInfoList = new ArrayList<>();
        notificationListAdapter = new NotificationListAdapter(this, notificationInfoList);
        recyclerView.setAdapter(notificationListAdapter);
        notificationListAdapter.setOnNotificationActionListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotifications(0);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (pageNum >= totalNum) return;
                int totalCount = myLinearLayoutManager.getItemCount();
                int visibleItemCount = recyclerView.getChildCount();
                int firstVisibleItemPosition = myLinearLayoutManager.findFirstVisibleItemPosition();
                if (totalCount - 1 <= visibleItemCount + firstVisibleItemPosition) {
                    getNotifications(pageNum);
                }
            }
        });
        getNotifications(0);
        linear_menuBottom.post(new Runnable() {
            @Override
            public void run() {
                linear_menuBottom.setTranslationY(linear_menuBottom.getHeight());
            }
        });
        button_selectAll.setOnClickListener(this);
        button_markRead.setOnClickListener(this);
        button_delete.setOnClickListener(this);
    }

    private void setRequestStatus(final boolean requesting) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isRequesting = requesting;
                swipeRefreshLayout.setRefreshing(requesting);
            }
        });
    }

    private void getNotifications(final int rowNum) {
        if (isRequesting) return;
        setRequestStatus(true);
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("page", String.valueOf(rowNum));
//        params.put("trackId", String.valueOf(rowNum));
        Observable<HttpResult<NotificationListInfo>> observable = iServiceApi.notificationData(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<NotificationListInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<NotificationListInfo> notificationListInfoHttpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(notificationListInfoHttpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (notificationListInfoHttpResult.getResult() != null) {
                            totalNum = notificationListInfoHttpResult.getResult().getTotal();
                        }
                        List<NotificationInfo> notificationInfos = notificationListInfoHttpResult.getResult().getRows();
                        if (notificationInfos == null) {
                            return;
                        }
                        if (rowNum == 0) {//第一次加载或者重新加载
                            pageNum = 0;
                            notificationInfoList.clear();
//                            playPlanTodayListAdapter.notifyItemRemoved(0);
                            currentListNum = 0;
                            if (notificationInfos.size() == 0) {
                                ToastUtil.showToast(getApplicationContext(), "没有通知", Toast.LENGTH_LONG);
                                return;
                            }
                        }
                        pageNum += notificationInfos.size();
                        for (int i = 0; i < notificationInfos.size(); i++) {
                            NotificationInfo notificationInfo = notificationInfos.get(i);
                            notificationInfo.setCdateStr(Util.formatDate(notificationInfo.getCdate()));

                        }
                        notificationInfoList.addAll(notificationInfos);
//                        playPlanListAdapter.notifyItemRangeInserted(currentListNum, playPlanInfoList.size() - currentListNum);
                        notificationListAdapter.notifyDataSetChanged();
                        currentListNum = notificationInfoList.size() - 1;
                        setRequestStatus(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, e.toString());
                        setRequestStatus(false);
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "onComplete");
                        setRequestStatus(false);
                    }
                });
    }

    /**
     * 查看信息
     *
     * @param notiId notiId
     */
    private void getNotificationDetail(final String notiId) {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("notiId", notiId);
        Observable<HttpResult> observable = iServiceApi.notificationDetail(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(httpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if ("200".equalsIgnoreCase(httpResult.getCode()) && httpResult.isSuccess()) {
                            for (int i = 0; i < notificationInfoList.size(); i++) {
                                if (notificationInfoList.get(i).getNotiId().equalsIgnoreCase(notiId)) {
                                    notificationInfoList.get(i).setStatus(1);
                                    notificationListAdapter.notifyItemChanged(i);
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "onComplete");
                    }
                });
    }

    /**
     * 全部已读消息
     */
    private void readNotification(final String notiId) {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        if (notiId != null) {
            params.put("notiId", notiId);
        }
        Observable<HttpResult> observable = iServiceApi.notificationAllRead(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showLoadingDialog();
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        LogUtil.e(TAG, "onNext");
                        dismissLoadingDialog();
                        if (shouldReLogin(httpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if ("200".equalsIgnoreCase(httpResult.getCode()) && httpResult.isSuccess()) {
                            for (int i = 0; i < notificationInfoList.size(); i++) {
                                notificationInfoList.get(i).setStatus(1);
                                notificationListAdapter.notifyItemChanged(i);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dismissLoadingDialog();
                        LogUtil.e(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                        LogUtil.e(TAG, "onComplete");
                    }
                });
    }

    /**
     * 删除notification
     *
     * @param notiId notiId
     */
    private void deleteNotification(final String notiId) {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("notiId", notiId);
        Observable<HttpResult> observable = iServiceApi.notificationDelete(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showLoadingDialog();
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        LogUtil.e(TAG, "onNext");
                        dismissLoadingDialog();
                        if (shouldReLogin(httpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if ("200".equalsIgnoreCase(httpResult.getCode()) && httpResult.isSuccess()) {
                            for (int i = 0; i < notificationInfoList.size(); i++) {
                                if (notificationInfoList.get(i).getNotiId().equalsIgnoreCase(notiId)) {
                                    LogUtil.e(TAG, "changed" + i);
                                    notificationInfoList.remove(i);
                                    notificationListAdapter.notifyItemRemoved(i);
                                    notificationListAdapter.notifyItemRangeChanged(i, notificationInfoList.size() - i);
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dismissLoadingDialog();
                        LogUtil.e(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                        LogUtil.e(TAG, "onComplete");
                    }
                });
    }


    @Override
    public void onItemClick(NotificationInfo notificationInfo, int position) {
        if (notificationListAdapter.isVisibleCheckbox()) {
            notificationInfoList.get(position).setChecked(!notificationInfoList.get(position).isChecked());
            notificationListAdapter.notifyItemChanged(position);
            boolean checked = false;
            for (NotificationInfo notification : notificationInfoList) {//确定button 标记已读和删除 是否可用
                if (notification.isChecked()) {
                    checked = true;
                    break;
                }
            }
            button_markRead.setEnabled(checked);
            button_delete.setEnabled(checked);
        } else {
            if (notificationInfo.getStatus() != 1) {//未读信息
                getNotificationDetail(notificationInfo.getNotiId());//查看消息
            }
            Intent intent = new Intent(this, NotificationDetailActivity.class);
            intent.putExtra("notificationInfo", notificationInfo);
            startActivity(intent);
        }
    }

    @Override
    public void onItemLongClick(NotificationInfo notificationInfo, boolean selected) {
        if (selected) {
            linear_menuBottom.animate().translationY(0).setDuration(250).start();
            button_markRead.setEnabled(true);
            button_delete.setEnabled(true);
        } else {
            linear_menuBottom.animate().translationY(linear_menuBottom.getHeight()).setDuration(250).start();
        }

    }

    @Override
    public void onBackPressed() {
        if (notificationListAdapter.isVisibleCheckbox()) {
            notificationListAdapter.setVisibleCheckbox(false);
            for (int i = 0; i < notificationInfoList.size(); i++) {
                notificationInfoList.get(i).setChecked(false);
                notificationListAdapter.notifyItemChanged(i);
            }
            linear_menuBottom.animate().translationY(linear_menuBottom.getHeight()).start();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_selectAll:
                if (notificationListAdapter.isSelectedAll()) {
                    notificationListAdapter.setSelectedAll(false);
                    for (int i = 0; i < notificationInfoList.size(); i++) {
                        notificationInfoList.get(i).setChecked(false);
                        notificationListAdapter.notifyItemChanged(i);
                    }
                    button_markRead.setEnabled(false);
                    button_delete.setEnabled(false);
                } else {
                    notificationListAdapter.setSelectedAll(true);
                    for (int i = 0; i < notificationInfoList.size(); i++) {
                        notificationInfoList.get(i).setChecked(true);
                        notificationListAdapter.notifyItemChanged(i);
                    }
                    button_markRead.setEnabled(true);
                    button_delete.setEnabled(true);
                }
                break;
            case R.id.button_markRead:
                if (dialogBuilder_markRead == null) {
                    dialogBuilder_markRead = new AlertDialog.Builder(this);
                    dialogBuilder_markRead.setTitle(getResources().getString(R.string.hint));
                    dialogBuilder_markRead.setMessage("确定标记所选信息为已读?");
                }
                dialogBuilder_markRead.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        linear_menuBottom.animate().translationY(linear_menuBottom.getHeight()).setDuration(250).start();
                        for (int i = 0; i < notificationInfoList.size(); i++) {
                            if (notificationInfoList.get(i).isChecked()) {
                                readNotification(notificationInfoList.get(i).getNotiId());
                            }
                        }
                        notificationListAdapter.setVisibleCheckbox(false);
                        for (int i = 0; i < notificationInfoList.size(); i++) {
                            notificationInfoList.get(i).setChecked(false);
                            notificationListAdapter.notifyItemChanged(i);
                        }

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialogBuilder_markRead.show();
                break;
            case R.id.button_delete:
                if (dialogBuilder_delete == null) {
                    dialogBuilder_delete = new AlertDialog.Builder(this);
                    dialogBuilder_delete.setTitle(getResources().getString(R.string.hint));
                    dialogBuilder_delete.setMessage("确定删除所选信息?");
                }
                dialogBuilder_delete.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        linear_menuBottom.animate().translationY(linear_menuBottom.getHeight()).setDuration(250).start();
                        for (int i = 0; i < notificationInfoList.size(); i++) {
                            if (notificationInfoList.get(i).isChecked()) {
                                deleteNotification(notificationInfoList.get(i).getNotiId());
                            }
                        }
                        notificationListAdapter.setVisibleCheckbox(false);
                        for (int i = 0; i < notificationInfoList.size(); i++) {
                            notificationInfoList.get(i).setChecked(false);
                            notificationListAdapter.notifyItemChanged(i);
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialogBuilder_delete.show();
                break;
        }
    }
}
