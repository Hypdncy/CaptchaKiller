/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package com.hypdncy.CaptchaKiller.Intruder;

import burp.api.montoya.intruder.AttackConfiguration;
import burp.api.montoya.intruder.PayloadGenerator;
import burp.api.montoya.intruder.PayloadGeneratorProvider;
import com.hypdncy.CaptchaKiller.ui.GUI;

public class MyPayloadGeneratorProvider implements PayloadGeneratorProvider {
    private final GUI gui;

    public MyPayloadGeneratorProvider(GUI gui) {
        this.gui = gui;
    }

    @Override
    public String displayName() {
        return "CaptchaKiller-Result";
    }

    @Override
    public PayloadGenerator providePayloadGenerator(AttackConfiguration attackConfiguration) {
        return new MyPayloadGenerator(gui);
    }
}
