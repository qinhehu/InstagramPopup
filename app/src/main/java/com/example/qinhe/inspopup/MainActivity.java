package com.example.qinhe.inspopup;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.example.qinhe.inspopup.http.HttpRequestManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    RecyclerView list;
    ListAdapter adapter;
    InsDialog dialog;

    private List<String> sources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        list.setItemAnimator(new DefaultItemAnimator());
        adapter = new ListAdapter(this);
        list.setAdapter(adapter);

        dialog = new InsDialog(this);

        adapter.setInsPressListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dialog.show();
                dialog.setImag(sources.get((int) v.getTag()));
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return false;
            }
        });
        getData();

    }

    void getData() {
        String id = "福利";
        Observable<GankIoDataBean> observable = HttpRequestManager.getApiServices().getGankIoData(id, 1, 10);
        Observer observer = new Observer<GankIoDataBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(GankIoDataBean o) {
                List<GankIoDataBean.ResultBean> beanList = o.getResults();
                if (sources == null) {
                    sources = new ArrayList<>();
                }
                sources.clear();
                for (int i = 0; i < beanList.size(); i++) {
                    sources.add(beanList.get(i).getUrl());
                }
                adapter.setSource(sources);
            }
        };
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (dialog.isShowing()) {
                    dialog.dispatchTouchEvent(ev);
                }
                break;
        }
        if (dialog.isShowing()) {
            list.clearFocus();
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
}
