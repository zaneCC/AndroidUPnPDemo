package com.zane.androidupnpdemo.service.callback;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zane.androidupnpdemo.Intents;

import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.state.StateVariableValue;
import org.fourthline.cling.support.avtransport.lastchange.AVTransportLastChangeParser;
import org.fourthline.cling.support.avtransport.lastchange.AVTransportVariable;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.lastchange.EventedValueString;
import org.fourthline.cling.support.lastchange.LastChange;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.TransportState;
import org.fourthline.cling.support.model.item.Item;

import java.util.Map;

/**
 * 说明：
 * 作者：zhouzhan
 * 日期：15/7/17 AM11:33
 */

public class AVTransportSubscriptionCallback  extends SubscriptionCallback {

    private static final String TAG = AVTransportSubscriptionCallback.class.getSimpleName();
    private Context mContext;

    protected AVTransportSubscriptionCallback(org.fourthline.cling.model.meta.Service service,
                                              Context context) {
        super(service);
        mContext = context;
    }

    @Override
    protected void failed(GENASubscription subscription, UpnpResponse responseStatus, Exception exception, String defaultMsg) {
        Log.e(TAG, "AVTransportSubscriptionCallback failed.");
    }

    @Override
    protected void established(GENASubscription subscription) {
    }

    @Override
    protected void ended(GENASubscription subscription, CancelReason reason, UpnpResponse responseStatus) {
        Log.i(TAG, "AVTransportSubscriptionCallback ended.");
    }

    @Override
    protected void eventReceived(GENASubscription subscription) { // 这里进行 事件接收处理

        Map<String, StateVariableValue> values = subscription.getCurrentValues();
        if (values != null && values.containsKey("LastChange")) {
            String lastChangeValue = values.get("LastChange").toString();
            Log.i(TAG, "LastChange:" + lastChangeValue);
            LastChange lastChange;
            try {
                lastChange = new LastChange(new AVTransportLastChangeParser(), lastChangeValue);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            //Parse TransportState value.
            AVTransportVariable.TransportState transportState = lastChange.getEventedValue(0, AVTransportVariable.TransportState.class);
            if (transportState != null) {
                TransportState ts = transportState.getValue();
                if (ts == TransportState.PLAYING) {
                    Log.e(TAG, "PLAYING");
                    Intent intent = new Intent(Intents.ACTION_PLAYING);
                    mContext.sendBroadcast(intent);
                } else if (ts == TransportState.PAUSED_PLAYBACK) {
                    Log.e(TAG, "PAUSED_PLAYBACK");
                    Intent intent = new Intent(Intents.ACTION_PAUSED_PLAYBACK);
                    mContext.sendBroadcast(intent);
                } else if (ts == TransportState.STOPPED) {
                    Log.e(TAG, "STOPPED");
                    Intent intent = new Intent(Intents.ACTION_STOPPED);
                    mContext.sendBroadcast(intent);
                } else if (ts == TransportState.TRANSITIONING){ // 转菊花状态
                    Log.e(TAG, "TRANSITIONING");
                    Intent intent = new Intent(Intents.ACTION_TRANSITIONING);
                    mContext.sendBroadcast(intent);
                }
            }

            //Parse CurrentTrackMetaData value.
            EventedValueString currentTrackMetaData = lastChange.getEventedValue(0, AVTransportVariable.CurrentTrackMetaData.class);
            if (currentTrackMetaData != null && currentTrackMetaData.getValue() != null) {
                DIDLParser didlParser = new DIDLParser();
                Intent lastChangeIntent;
                try {
                    DIDLContent content = didlParser.parse(currentTrackMetaData.getValue());
                    Item item = content.getItems().get(0);
                    String creator = item.getCreator();
                    String title = item.getTitle();

                    lastChangeIntent = new Intent(Intents.ACTION_UPDATE_LAST_CHANGE);
                    lastChangeIntent.putExtra("creator", creator);
                    lastChangeIntent.putExtra("title", title);
                } catch (Exception e) {
                    Log.e(TAG, "Parse CurrentTrackMetaData error.");
                    lastChangeIntent = null;
                }

                if (lastChangeIntent != null)
                    mContext.sendBroadcast(lastChangeIntent);
            }
        }
    }

    @Override
    protected void eventsMissed(GENASubscription subscription, int numberOfMissedEvents) {
    }
}