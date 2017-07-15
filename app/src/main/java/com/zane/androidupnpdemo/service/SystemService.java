/*
 * Copyright (C) 2014 Kevin Shen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zane.androidupnpdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


import com.zane.androidupnpdemo.Intents;
import com.zane.androidupnpdemo.entity.ClingDevice;
import com.zane.androidupnpdemo.entity.IDevice;
import com.zane.androidupnpdemo.service.callback.AVTransportSubscriptionCallback;
import com.zane.androidupnpdemo.service.manager.ClingUpnpServiceManager;

import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Application service，process background task.
 */
public class SystemService extends Service {
    private static final String TAG = SystemService.class.getSimpleName();

    private Binder binder = new LocalBinder();
    private ClingDevice mSelectedDevice;
    private int mDeviceVolume;
    private AVTransportSubscriptionCallback mAVTransportSubscriptionCallback;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        //End all subscriptions
        if (mAVTransportSubscriptionCallback != null)
            mAVTransportSubscriptionCallback.end();

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        public SystemService getService() {
            return SystemService.this;
        }
    }

    public IDevice getSelectedDevice() {
        return mSelectedDevice;
    }

    public void setSelectedDevice(IDevice selectedDevice, ControlPoint controlPoint) {
        if (selectedDevice == mSelectedDevice) return;

        Log.i(TAG, "Change selected device.");
        mSelectedDevice = (ClingDevice) selectedDevice;
        //End last device's subscriptions
        if (mAVTransportSubscriptionCallback != null) {
            mAVTransportSubscriptionCallback.end();
        }
        //Init Subscriptions
        mAVTransportSubscriptionCallback = new AVTransportSubscriptionCallback(
                mSelectedDevice.getDevice().findService(ClingUpnpServiceManager.AV_TRANSPORT_SERVICE));
        controlPoint.execute(mAVTransportSubscriptionCallback);

        Intent intent = new Intent(Intents.ACTION_CHANGE_DEVICE);
        sendBroadcast(intent);
    }

    public int getDeviceVolume() {
        return mDeviceVolume;
    }

    public void setDeviceVolume(int currentVolume) {
        mDeviceVolume = currentVolume;
    }

}
