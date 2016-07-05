package com.example.aishwarya.samplewear;

import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.example.aishwarya.samplewear.adapter.AccountSettingAdapter;

import java.util.ArrayList;

public class DataShowActivity extends BaseActivity {
    private static final String TAG = DataShowActivity.class.getSimpleName();
    private WearableListView mWearableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_show);
        mWearableListView = (WearableListView) findViewById(R.id.sample_list_view);
        getdatafromintent();
    }

    private void getdatafromintent() {
        Log.d(TAG, "Got data from intent");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
          ArrayList<String> accountNames =  bundle.getStringArrayList("account_name");
            initWearableListView(accountNames);
        }
    }

    private void initWearableListView(ArrayList<String> accountNames) {
        AccountSettingAdapter accountSettingAdapter  = new AccountSettingAdapter(accountNames, this);
        mWearableListView.setAdapter(accountSettingAdapter);
    }

}
