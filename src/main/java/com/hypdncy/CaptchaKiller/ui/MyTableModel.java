/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package com.hypdncy.CaptchaKiller.ui;


import com.hypdncy.CaptchaKiller.entity.CaptchaEntity;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MyTableModel extends AbstractTableModel {
    private final List<CaptchaEntity> captchaList;
    private final String[] title = {"验证码", "识别结果", "识别Key"};
    private final JTable table;


    public MyTableModel(JTable table) {
        this.table = table;
        this.captchaList = new ArrayList<>();
    }

    @Override
    public synchronized int getRowCount() {
        return captchaList.size();
    }

    @Override
    public int getColumnCount() {
        return title.length;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return switch (column) {
            case 0 -> Icon.class;
            case 1 -> String.class;
            case 2 -> String.class;
            default -> String.class;
        };
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> title[0];
            case 1 -> title[1];
            case 2 -> title[2];
            default -> "";
        };
    }

    @Override
    public synchronized Object getValueAt(int rowIndex, int columnIndex) {
        CaptchaEntity captchaEntity = captchaList.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> captchaEntity.getImgIcon();
            case 1 -> captchaEntity.getImgRes();
            case 2 -> captchaEntity.getImgKey();
            default -> "";
        };
    }

    public synchronized void add(CaptchaEntity captchaEntity) {
        int index = captchaList.size();
        captchaList.add(captchaEntity);
        fireTableRowsInserted(index, index);
    }

    public synchronized CaptchaEntity get(int rowIndex) {
        return captchaList.get(rowIndex);
    }

    public synchronized void clearList() {
        this.captchaList.clear();
        fireTableDataChanged();
    }

    public JTable getTable() {
        return table;
    }

}
