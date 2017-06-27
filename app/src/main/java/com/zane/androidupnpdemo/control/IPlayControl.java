package com.zane.androidupnpdemo.control;

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
     * @param metadata
     */
    void playNew(String url, String metadata);

    /**
     * 播放
     */
    void play();

    /**
     * 暂停
     */
    void pause();

    /**
     * 停止
     */
    void stop();

    /**
     * 视频 seek
     *
     * @param pos   seek到的位置
     */
    void seek(int pos);


}
