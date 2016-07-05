package com.example.aishwarya.samplewear;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.sharemodule.Constants;
import com.example.sharemodule.SendMessageTask;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener, Utility.Keys {

    private static  final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout);
        findViewById(R.id.btn_launch_home).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_launch_home) {
            new SendMessageTask(this).execute(Constants.Keys.CALL_LOGIN, "");
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Toast.makeText(this, messageEvent.getPath(), Toast.LENGTH_SHORT).show();
        Utility.Log("wear", "message:" + messageEvent.getPath() + " data:" + new String(messageEvent.getData()));
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        super.onDataChanged(dataEventBuffer);
        Toast.makeText(this, "onDataChanged", Toast.LENGTH_SHORT).show();
        Utility.Log("data changed", "Trigger");
        for (DataEvent event : dataEventBuffer) {

            if (event.getType() == DataEvent.TYPE_CHANGED && event.getDataItem() != null) {
                DataMap dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                Log.d(TAG, "Received Size:" +dataMap.size());
                Log.d(TAG,dataMap.size()+" size");
                Log.d(TAG, event.getDataItem().getUri().getPath());

                switch (event.getDataItem().getUri().getPath())
                {
                    case Constants.Keys.DATA_ITEM:
                        List<DataMap> data = dataMap.getDataMapArrayList(Constants.Keys.ACCOUNT_LIST_ITEM);
                        Log.d(TAG, "Data received at the receiver end:" +data.size());
                        Toast.makeText(this, data.size()+"  size", Toast.LENGTH_SHORT).show();
                        startDataShowActivity(data);
                        break;
                }

            }
        }
    }

    private void startDataShowActivity(List<DataMap> data) {
        Log.d(TAG, "Inside startDataShowActivity()");
        ArrayList<String> accounts = new ArrayList<>();
        for (DataMap datas: data) {
            String account = datas.getString("name");
            accounts.add(account);
        }
        Intent intent = new Intent(this, DataShowActivity.class);
        intent.putStringArrayListExtra("account_name", accounts);
        startActivity(intent);
    }
}
