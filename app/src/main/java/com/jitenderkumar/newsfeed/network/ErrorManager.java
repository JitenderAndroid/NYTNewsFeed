package com.jitenderkumar.newsfeed.network;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

public class ErrorManager {
    private Activity mActivity;
    private View mView;
    private Object mObject;

    public ErrorManager(Activity activity, View view, Object object) {
        mActivity = activity;
        mView = view;
        mObject = object;
    }

    public void handleErrorResponse() {
        if (mObject == null) {
            return;
        }

        if (mObject instanceof String) {
            Toast.makeText(mActivity, mObject.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}