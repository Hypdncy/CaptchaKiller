/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package com.hypdncy.CaptchaKiller.Intruder;

import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.intruder.GeneratedPayload;
import burp.api.montoya.intruder.IntruderInsertionPoint;
import burp.api.montoya.intruder.PayloadGenerator;
import com.hypdncy.CaptchaKiller.entity.CaptchaEntity;
import com.hypdncy.CaptchaKiller.ui.GUI;


public class MyPayloadGenerator implements PayloadGenerator {
    private final GUI gui;

    public MyPayloadGenerator(GUI gui) {
        this.gui = gui;
    }

    @Override
    public GeneratedPayload generatePayloadFor(IntruderInsertionPoint insertionPoint) {
        HttpRequestResponse requestResponse = this.gui.getHttpResponseFromCaptchaReq();
        CaptchaEntity captchaEntity = this.gui.getCaptchaFromResponse(requestResponse);
        gui.sendCaptchaChk(captchaEntity);
        if(gui.enableUpdateTableJRadioButton.isSelected()){
            gui.tableModel.add(captchaEntity);
        }
        return GeneratedPayload.payload(captchaEntity.getImgRes());
    }

}
