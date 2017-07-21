package com.mgtv.ui.play.dlan.control.callback;

import com.mgtv.ui.play.dlan.entity.IResponse;

/**
 * 说明：手机端接收投屏端信息回调
 * 作者：zhouzhan
 * 日期：17/7/19 11:13
 */

public interface ControlReceiveCallback extends ControlCallback{

    void receive(IResponse response);
}
