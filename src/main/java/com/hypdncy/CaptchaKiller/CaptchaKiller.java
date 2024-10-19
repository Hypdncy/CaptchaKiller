/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package com.hypdncy.CaptchaKiller;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import com.hypdncy.CaptchaKiller.Intruder.MyHttpHandler;
import com.hypdncy.CaptchaKiller.Intruder.MyPayloadGeneratorProvider;
import com.hypdncy.CaptchaKiller.ui.GUI;
import com.hypdncy.CaptchaKiller.ui.MyTableModel;

import javax.swing.*;
import java.awt.*;

public class CaptchaKiller implements BurpExtension {
    private MontoyaApi api;

    @Override
    public void initialize(MontoyaApi api) {
        this.api = api;
        api.extension().setName("CaptchaKiller Modify By Hypdncy v1.0");
        MyTableModel tableModel = new MyTableModel(new JTable());
        GUI gui = new GUI(api, tableModel);
        gui.initGUI();
        api.userInterface().registerSuiteTab("CaptchaKiller", gui.globalJPane);
        api.http().registerHttpHandler(new MyHttpHandler(api, gui));
        api.intruder().registerPayloadGeneratorProvider(new MyPayloadGeneratorProvider(gui));
        api.logging().logToOutput("""
                [*]
                [*] ###################################################
                [*] Author: Hypdncy
                [*] Email:  Hypdncy@outlook.com
                [*] Github: https://github.com/Hypdncy/CaptchaKiller
                [*] 感谢: https://github.com/f0ng/captcha-killer-modified
                [*] 感谢: https://github.com/sml2h3/ddddocr
                [*] ###################################################
                [*]
                """
        );
    }


    private Component constructLoggerTab(MyTableModel tableModel) {
        return new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    }
}
