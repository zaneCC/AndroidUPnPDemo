package com.zane.androidupnpdemo.control;

import com.zane.androidupnpdemo.listener.ControlCallback;

/**
 * 说明：对视频的控制操作定义
 * 作者：zhouzhan
 * 日期：17/6/27 17:13
 */
public interface IPlayControl {

    /**
     * 播放一个新片源
     *
     * @param url   片源地址
     */
    void playNew(String url, ControlCallback callback);

    /**
     * 播放
     */
    void play(ControlCallback callback);

    /**
     * 暂停
     */
    void pause(ControlCallback callback);

    /**
     * 停止
     */
    void stop(ControlCallback callback);

    /**
     * 视频 seek
     *
     * @param pos   seek到的位置
     */
    void seek(int pos, ControlCallback callback);


}
