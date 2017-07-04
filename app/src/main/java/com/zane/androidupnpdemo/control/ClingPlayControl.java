package com.zane.androidupnpdemo.control;

import android.util.Log;

import com.zane.androidupnpdemo.entity.ClingResponse;
import com.zane.androidupnpdemo.entity.IResponse;
import com.zane.androidupnpdemo.listener.ControlCallback;
import com.zane.androidupnpdemo.service.manager.ClingUpnpServiceManager;
import com.zane.androidupnpdemo.util.ClingUtils;
import com.zane.androidupnpdemo.util.Utils;

import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.avtransport.callback.Play;
import org.fourthline.cling.support.avtransport.callback.SetAVTransportURI;
import org.fourthline.cling.support.avtransport.callback.Stop;

/**
 * 说明：Cling 实现的控制方法
 * 作者：zhouzhan
 * 日期：17/6/27 17:17
 */

public class ClingPlayControl implements IPlayControl{

    private static final String TAG = ClingPlayControl.class.getSimpleName();

    @Override
    public void playNew(final String url, final ControlCallback callback) {
//        final Service avtService = ClingUtils.findServiceFromSelectedDevice(ClingUpnpServiceManager.AV_TRANSPORT_SERVICE);
//
//        if (Utils.isNull(avtService))
//            return;
//
//        final ControlPoint controlPointImpl = ClingUtils.getControlPoint();
//        if (Utils.isNull(controlPointImpl))
//            return;

        stop(new ControlCallback() { // 1、 停止当前播放视频
            @Override
            public void success(IResponse response) {

                setAVTransportURI(url, new ControlCallback() {   // 2、设置 url
                    @Override
                    public void success(IResponse response) {
                        play(callback);                        // 3、播放视频
                    }

                    @Override
                    public void fail(IResponse response) {
                        if (Utils.isNotNull(callback)){
                            callback.fail(response);
                        }
                    }
                });
            }

            @Override
            public void fail(IResponse response) {
                if (Utils.isNotNull(callback)){
                    callback.fail(response);
                }
            }
        });

//        controlPointImpl.execute(new Stop(avtService) {
//
//            @Override
//            public void success(ActionInvocation invocation) {
//                super.success(invocation);
//
//                controlPointImpl.execute(new SetAVTransportURI(avtService, url) {
//
//                    @Override
//                    public void success(ActionInvocation invocation) {
//                        super.success(invocation);
//                        //Second,Set Play command.
//                        controlPoint.execute(new Play(avtService) {
//                            @Override
//                            public void success(ActionInvocation invocation) {
//                                Log.i(TAG, "PlayNewItem success:" + url);
//                            }
//
//                            @Override
//                            public void failure(ActionInvocation arg0, UpnpResponse arg1, String arg2) {
//                                Log.e(TAG, "playNewItem failed");
//                            }
//                        });
//                        Log.i(TAG, "PlayNewItem success:" + url);
//                    }
//
//                    @Override
//                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
//                        Log.e(TAG, "playNewItem failed");
//                    }
//                });
//            }
//
//            @Override
//            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
//            }
//        });
    }

    @Override
    public void play(final ControlCallback callback) {
        final Service avtService = ClingUtils.findServiceFromSelectedDevice(ClingUpnpServiceManager.AV_TRANSPORT_SERVICE);
        if (Utils.isNull(avtService))
            return;

        final ControlPoint controlPointImpl = ClingUtils.getControlPoint();
        if (Utils.isNull(controlPointImpl))
            return;

        controlPointImpl.execute(new Play(avtService) {

            @Override
            public void success(ActionInvocation invocation) {
                super.success(invocation);
                if (Utils.isNotNull(callback)){
                    callback.success(new ClingResponse(invocation));
                }
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                if (Utils.isNotNull(callback)){
                    callback.fail(new ClingResponse(invocation, operation, defaultMsg));
                }
            }
        });
    }

    @Override
    public void pause(ControlCallback callback) {

    }

    @Override
    public void stop(final ControlCallback callback) {
        final Service avtService = ClingUtils.findServiceFromSelectedDevice(ClingUpnpServiceManager.AV_TRANSPORT_SERVICE);
        if (Utils.isNull(avtService))
            return;

        final ControlPoint controlPointImpl = ClingUtils.getControlPoint();
        if (Utils.isNull(controlPointImpl))
            return;

        controlPointImpl.execute(new Stop(avtService) {

            @Override
            public void success(ActionInvocation invocation) {
                super.success(invocation);
                if (Utils.isNotNull(callback)){
                    callback.success(new ClingResponse(invocation));
                }
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                if (Utils.isNotNull(callback)){
                    callback.fail(new ClingResponse(invocation, operation, defaultMsg));
                }
            }
        });
    }

    @Override
    public void seek(int pos, ControlCallback callback) {

    }

    private void setAVTransportURI(String url, final ControlCallback callback){
        if (Utils.isNull(url))
            return;

        final Service avtService = ClingUtils.findServiceFromSelectedDevice(ClingUpnpServiceManager.AV_TRANSPORT_SERVICE);
        if (Utils.isNull(avtService))
            return;

        final ControlPoint controlPointImpl = ClingUtils.getControlPoint();
        if (Utils.isNull(controlPointImpl))
            return;

        controlPointImpl.execute(new SetAVTransportURI(avtService, url) {

            @Override
            public void success(ActionInvocation invocation) {
                super.success(invocation);
                if (Utils.isNotNull(callback)){
                    callback.success(new ClingResponse(invocation));
                }
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                if (Utils.isNotNull(callback)){
                    callback.fail(new ClingResponse(invocation, operation, defaultMsg));
                }
            }
        });
    }
}
