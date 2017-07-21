package com.zane.androidupnpdemo.service.callback;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.zane.androidupnpdemo.Intents;
import com.zane.androidupnpdemo.util.Utils;

import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.state.StateVariableValue;
import org.fourthline.cling.support.lastchange.LastChange;
import org.fourthline.cling.support.renderingcontrol.lastchange.RenderingControlLastChangeParser;
import org.fourthline.cling.support.renderingcontrol.lastchange.RenderingControlVariable;

import java.util.Map;

/**
 * 说明：RenderingControl 事件回传
 * 作者：zhouzhan
 * 日期：17/7/18 18:54
 */

public class RenderingControlSubscriptionCallback extends SubscriptionCallback {

    private static final String TAG = RenderingControlSubscriptionCallback.class.getSimpleName();

    private Context mContext;

    public RenderingControlSubscriptionCallback(Service service, Context context) {
        super(service);
        mContext = context;
    }

    @Override
    protected void failed(GENASubscription subscription, UpnpResponse responseStatus, Exception exception, String defaultMsg) {
    }

    @Override
    protected void established(GENASubscription subscription) {
    }

    @Override
    protected void ended(GENASubscription subscription, CancelReason reason, UpnpResponse responseStatus) {
    }

    @Override
    protected void eventReceived(GENASubscription subscription) {
        Map values = subscription.getCurrentValues();
        if (Utils.isNull(values)) {
            return;
        }
        if (!values.containsKey("LastChange")) {
            return;
        }

        String lastChangeValue = values.get("LastChange").toString();
        Log.i(TAG, "LastChange:" + lastChangeValue);
        LastChange lastChange;
        try {
            lastChange = new LastChange(
                    new RenderingControlLastChangeParser(),
                    lastChangeValue);
            //获取音量 volume
            int volume = 0;
            if (lastChange.getEventedValue(0, RenderingControlVariable.Volume.class) != null) {
                volume = lastChange.getEventedValue(0, RenderingControlVariable.Volume.class).getValue().getVolume();

                Log.e(TAG, "onVolumeChange volume: " + volume);
                Intent intent = new Intent(Intents.ACTION_VOLUME_CALLBACK);
                intent.putExtra(Intents.EXTRA_VOLUME, volume);
                mContext.sendBroadcast(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void eventsMissed(GENASubscription subscription, int numberOfMissedEvents) {
    }
}
