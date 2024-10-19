/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package com.hypdncy.CaptchaKiller.Intruder;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.Annotations;
import burp.api.montoya.core.ToolType;
import burp.api.montoya.http.handler.*;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import com.hypdncy.CaptchaKiller.entity.CaptchaEntity;
import com.hypdncy.CaptchaKiller.ui.GUI;

import java.util.Objects;

import static burp.api.montoya.http.handler.RequestToBeSentAction.continueWith;

public class MyHttpHandler implements HttpHandler {
    private final MontoyaApi api;
    private final GUI gui;


    public MyHttpHandler(MontoyaApi api, GUI gui) {
        this.api = api;
        this.gui = gui;
    }


    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent requestToBeSent) {
        // Todo


        if ((!gui.enableExtJRadioButton.isSelected())
                || requestToBeSent.toolSource().toolType() != ToolType.INTRUDER) {
            return null;
        }

        Annotations annotations = requestToBeSent.annotations();
        String bodyString = requestToBeSent.bodyToString();

        HttpRequestResponse requestResponse = this.gui.getHttpResponseFromCaptchaReq();
        CaptchaEntity captchaEntity = this.gui.getCaptchaFromResponse(requestResponse);
        gui.sendCaptchaChk(captchaEntity);

        if (gui.enableUpdateTableJRadioButton.isSelected()) {
            this.gui.tableModel.add(captchaEntity);
        }

        String newBodyString = bodyString
                .replace("@CaptchaKey@", captchaEntity.getImgKey())
                .replace("@CaptchaRes@", captchaEntity.getImgRes());
        HttpRequest modifiedRequest = requestToBeSent.withBody(newBodyString);


        return continueWith(modifiedRequest, annotations);
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived responseReceived) {
        return ResponseReceivedAction.continueWith(responseReceived);
    }


    public GUI getGui() {
        return gui;
    }

    public MontoyaApi getApi() {
        return api;
    }
}
