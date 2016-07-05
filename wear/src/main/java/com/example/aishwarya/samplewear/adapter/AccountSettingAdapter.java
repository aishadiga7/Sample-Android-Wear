package com.example.aishwarya.samplewear.adapter;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aishwarya.samplewear.AccountSettingsView;
import com.example.aishwarya.samplewear.R;

import java.util.ArrayList;

/**
 * Created by aishwarya on 22/6/16.
 */
public class AccountSettingAdapter extends WearableListView.Adapter {
    private ArrayList<String> mAccountNames;
    private Context mContext;


    public AccountSettingAdapter(ArrayList<String> accountNames, Context context) {
        mAccountNames = accountNames;
        mContext = context;
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WearableListView.ViewHolder(new AccountSettingsView(mContext));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        AccountSettingsView accountSettingsView  = (AccountSettingsView) holder.itemView;
        final String item = mAccountNames.get(position);
        TextView textView = (TextView) accountSettingsView.findViewById(R.id.name);
        textView.setText(item);
    }

    @Override
    public int getItemCount() {
        return mAccountNames.size();
    }
}
