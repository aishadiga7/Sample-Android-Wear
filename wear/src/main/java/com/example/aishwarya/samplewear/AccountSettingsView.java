package com.example.aishwarya.samplewear;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by aishwarya on 22/6/16.
 */
public class AccountSettingsView extends FrameLayout implements WearableListView.OnCenterProximityListener {
    private TextView mName;

    public AccountSettingsView(Context context) {
        super(context);
        View.inflate(context, R.layout.wearablelistview_item, this);
        mName = (TextView) findViewById(R.id.name);
    }

    @Override
    public void onCenterPosition(boolean b) {
        mName.animate().scaleX(1f).scaleY(1f).alpha(1);
    }

    @Override
    public void onNonCenterPosition(boolean b) {
        mName.animate().scaleX(0.8f).scaleY(0.8f).alpha(0.6f);
    }
}
