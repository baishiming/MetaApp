package com.baishiming.meta.metaapp;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import http.HttpCallBack;
import http.HttpUtils;

public class MainActivity extends AppCompatActivity {

    HttpUtils httpUtils = new HttpUtils();
    private int pn = 50;
    private int sn = 1;
    private String q = "北京";
    private RecyclerView recyclerView;
    private PicAdapter picAdapter;
    private List<PicBean> myList;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    picAdapter.setData(myList);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        //设置横向
        GridLayoutManager layoutManager= new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);

       getData();
        picAdapter = new PicAdapter(this);
        recyclerView.setAdapter(picAdapter);
    }

    private void getData(){
        Map<String,Object> map = new HashMap<>();
        map.put("q",q);
        map.put("pn",pn);
        map.put("sn",sn);
        HttpUtils.get(map, new HttpCallBack() {
            @Override
            public void onCallBack(String string) {
                GetPicBean getPicBean = GsonUtil.gsonToBean(string, GetPicBean.class);
                Log.e("reload",getPicBean.toString());
                myList = getPicBean.getList();
                handler.sendEmptyMessage(1);
            }
        });
    }

}
