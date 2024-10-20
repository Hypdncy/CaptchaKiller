package com.hypdncy.CaptchaKiller.entity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class CaptchaEntity {
    public static ImageIcon blackIcon;

    static {
        String b64 = "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAABCAIAAAD5T7YfAAAAFElEQVRIDbXBAQEAAAABIP6PzqV2pFqAAAAAElFTkSuQmCC";
        byte[] data = Base64.getDecoder().decode(b64);
        InputStream buff = new ByteArrayInputStream(data);
        try {
            Image img = ImageIO.read(buff);
            blackIcon = new ImageIcon(img);
        } catch (IOException ignored) {
        }
    }

    private String imgKey;
    private byte[] imgData;
    private String imgRes;

    public CaptchaEntity(String imgKey, byte[] imgData, String imgRes) {
        this.imgKey = imgKey;
        this.imgData = imgData;
        this.imgRes = imgRes;
    }

    public CaptchaEntity() {
        this.imgKey = "";
        this.imgData = null;
        this.imgRes = "";
    }

    public static ImageIcon byte2img(byte[] imgData) {
        ImageIcon icon = blackIcon;

        if (imgData == null) {
            return blackIcon;
        }
        InputStream buff = new ByteArrayInputStream(imgData);
        Image img;
        try {
            img = ImageIO.read(buff);
            icon = new ImageIcon(img);
        } catch (IOException ignored) {
        }
        return icon;
    }

    public byte[] getImgData() {
        return imgData;
    }

    public void setImgData(byte[] imgData) {
        this.imgData = imgData;
    }

    public String getImgRes() {
        return imgRes;
    }

    public void setImgRes(String imgRes) {
        this.imgRes = imgRes;
    }

    public String getImgKey() {
        return imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

}
