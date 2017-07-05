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
import org.fourthline.cling.support.avtransport.callback.Pause;
import org.fourthline.cling.support.avtransport.callback.Play;
import org.fourthline.cling.support.avtransport.callback.Seek;
import org.fourthline.cling.support.avtransport.callback.SetAVTransportURI;
import org.fourthline.cling.support.avtransport.callback.Stop;
import org.fourthline.cling.support.model.TransportState;

/**
 * 说明：Cling 实现的控制方法
 * 作者：zhouzhan
 * 日期：17/6/27 17:17
 */

public class ClingPlayControl implements IPlayControl{

    private static final String TAG = ClingPlayControl.class.getSimpleName();
    /**
     * 当前状态
     */
    private TransportState mCurrentState = TransportState.STOPPED;

    @Override
    public void playNew(final String url, final ControlCallback callback) {

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
    public void pause(final ControlCallback callback) {
        final Service avtService = ClingUtils.findServiceFromSelectedDevice(ClingUpnpServiceManager.AV_TRANSPORT_SERVICE);
        if (Utils.isNull(avtService))
            return;

        final ControlPoint controlPointImpl = ClingUtils.getControlPoint();
        if (Utils.isNull(controlPointImpl))
            return;

        controlPointImpl.execute(new Pause(avtService) {

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
    public void seek(int pos, final ControlCallback callback) {
        final Service avtService = ClingUtils.findServiceFromSelectedDevice(ClingUpnpServiceManager.AV_TRANSPORT_SERVICE);
        if (Utils.isNull(avtService))
            return;

        final ControlPoint controlPointImpl = ClingUtils.getControlPoint();
        if (Utils.isNull(controlPointImpl))
            return;

        String time = Utils.stringForTime(pos);
        Log.e(TAG, "seek->pos: " + pos + ", time: " + time);
        controlPointImpl.execute(new Seek(avtService, time) {

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

    /**
     * 设置片源，用于首次播放
     *
     * @param url   片源地址
     * @param callback  回调
     */
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

    public TransportState getCurrentState() {
        return mCurrentState;
    }

    public void setCurrentState(TransportState currentState) {
        if (this.mCurrentState != currentState) {
            this.mCurrentState = currentState;
        }
    }
}
