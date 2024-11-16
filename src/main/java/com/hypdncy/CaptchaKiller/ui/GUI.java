package com.hypdncy.CaptchaKiller.ui;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.Marker;
import burp.api.montoya.http.HttpService;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.responses.HttpResponse;
import burp.api.montoya.ui.UserInterface;
import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;
import com.hypdncy.CaptchaKiller.entity.CaptchaEntity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName: GUI
 * Package: com.hypdncy.CaptchaKiller.ui
 * Description:
 *
 * @Author Hypdncy
 * @Create 2024/10/10 11:51
 * @Version 1.0
 */
public class GUI {
    public JRadioButton enableExtJRadioButton;
    public JRadioButton enableUpdateTableJRadioButton;
    public JTextField apiURLJTextField;
    public JTextField captchaURLJTextField;
    public JTextField keyJTextField;
    public JTextField valueTextField;
    public JComboBox<String> keyComboBox;
    public JComboBox<String> valueComboBox;
    public JTextField keyRegJTextField;
    public JTextField valueRegJTextField;
    public HttpRequestEditor requestViewer;
    public HttpResponseEditor responseViewer;
    public JSplitPane globalJPane;
    public HttpClient client = HttpClient.newHttpClient();
    private HttpRequestResponse uiHttpRequestResponse;
    public final MyTableModel tableModel;
    public final MontoyaApi api;
    public final int TableRowHeight ;


    public GUI(MontoyaApi api, MyTableModel tableModel) {
        this.tableModel = tableModel;
        this.api = api;
        TableRowHeight = tableModel.getTable().getRowHeight();
    }

    public void initGUI() {
        JPanel leftNorthPanel = new JPanel();
        leftNorthPanel.setLayout(new GridBagLayout());
        enableExtJRadioButton = new JRadioButton("启用Replace模式");
        enableUpdateTableJRadioButton = new JRadioButton("是否更新验证码表");
        enableExtJRadioButton.setSelected(false);
        enableUpdateTableJRadioButton.setSelected(false);
        JLabel apiURLJLabel = new JLabel("接口URL:");
        apiURLJTextField = new JTextField("http://127.0.0.1:60000/reg_digit_case_lower");
        JLabel captchaURLJLabel = new JLabel("验证码URL:");
        captchaURLJTextField = new JTextField("http://127.0.0.1:60000/getCap");
        // String[] keyItems = {"ResBodyRegex", "ResBodyNone", "ResBodyRegex", "ResBodyJson", "ResBodyXml"};
        // String[] valueItems = {"ResBodyRegexBase64", "ResBodyAllByte", "ResBodyAllBase64", "ResBodyRegexBase64", "ResBodyJsonBase64", "ResBodyXmlBase64"};
        String[] keyItems = {"KeyReg", "KeyNone"};
        String[] valueItems = {"ImgRegBase64", "ImgAllBytes", "ImgAllBase64"};

        keyComboBox = new JComboBox<>(keyItems);
        keyJTextField = new JTextField("\"key\": *?\"(.*?)\"");
        valueComboBox = new JComboBox<>(valueItems);
        valueTextField = new JTextField("data:image/[jpegn]+;base64,(.*?)[\"<']");
        JLabel keyRegJLabel = new JLabel("响应CaptchaKey:");
        JLabel valueRegJLabel = new JLabel("响应CaptchaValue:");

        keyRegJTextField = new JTextField("");
        valueRegJTextField = new JTextField("");
        keyRegJTextField.setEditable(false);
        valueRegJTextField.setEditable(false);


        JButton getCaptchaJButton = new JButton("获取");
        JButton chkCaptchaJButton = new JButton("识别");


        List<? extends JComponent> jComponents = Arrays.asList(
                enableExtJRadioButton,
                enableUpdateTableJRadioButton,
                apiURLJLabel,
                apiURLJTextField,
                captchaURLJLabel,
                captchaURLJTextField,
                keyComboBox,
                keyJTextField,
                valueComboBox,
                valueTextField,
                keyRegJLabel,
                keyRegJTextField,
                valueRegJLabel,
                valueRegJTextField,
                getCaptchaJButton,
                chkCaptchaJButton
        );
        for (int i = 0; i < jComponents.size() / 2; i++) {
            leftNorthPanel.add(jComponents.get(i * 2), new GBC(0, i, 1, 1).setFill(GBC.HORIZONTAL).setWeight(0, 0));
            leftNorthPanel.add(jComponents.get(i * 2 + 1), new GBC(1, i, 1, 1).setFill(GBC.HORIZONTAL).setWeight(1, 0));
        }
        if (jComponents.size() % 2 > 0) {
            leftNorthPanel.add(jComponents.getLast(), new GBC(0, jComponents.size() / 2, 1, 1).setFill(GBC.HORIZONTAL).setWeight(0, 0));
        }
        /*
        // GBC GBC0_0 = new GBC(0, 0, 1, 1).setFill(GBC.HORIZONTAL).setWeight(0, 0);
        // GBC GBC0_1 = new GBC(1, 0, 1, 1).setFill(GBC.HORIZONTAL).setWeight(1, 0);
        // GBC GBC1_0 = new GBC(0, 1, 1, 1).setFill(GBC.HORIZONTAL).setWeight(0, 0);
        // GBC GBC1_1 = new GBC(1, 1, 1, 1).setFill(GBC.HORIZONTAL).setWeight(1, 0);
        // GBC GBC2_0 = new GBC(0, 2, 1, 1).setFill(GBC.HORIZONTAL).setWeight(0, 0);
        // GBC GBC2_1 = new GBC(1, 2, 1, 1).setFill(GBC.HORIZONTAL).setWeight(1, 0);
        // GBC GBC3_0 = new GBC(0, 3, 1, 1).setFill(GBC.HORIZONTAL).setWeight(0, 0);
        // GBC GBC3_1 = new GBC(1, 3, 1, 1).setFill(GBC.HORIZONTAL).setWeight(1, 0);
        // GBC GBC4_0 = new GBC(0, 4, 1, 1).setFill(GBC.HORIZONTAL).setWeight(0, 0);
        // GBC GBC4_1 = new GBC(1, 4, 1, 1).setFill(GBC.HORIZONTAL).setWeight(0, 0);
        // GBC GBC5_0 = new GBC(0, 5, 1, 1).setFill(GBC.HORIZONTAL).setWeight(0, 0);
        // leftNorthPanel.add(enableExtJRadioButton,GBC0_0);
        // leftNorthPanel.add(enableUpdateTableJRadioButton,GBC0_1);
        // leftNorthPanel.add(apiURLJLabel, GBC1_0);
        // leftNorthPanel.add(apiURLJTextField,GBC1_1);
        // leftNorthPanel.add(captchaURLJLabel, GBC2_0);
        // leftNorthPanel.add(captchaURLJTextField,GBC2_1);
        // leftNorthPanel.add(keyRegexJLabel,GBC3_0);
        // leftNorthPanel.add(keyRegexJTextField,GBC3_1);
        // leftNorthPanel.add(valueRegexJLabel,GBC4_0);
        // leftNorthPanel.add(valueRegexJTextField,GBC4_1);
        // leftNorthPanel.add(getCaptchaJButton,GBC5_0);

        // // 网格，行列式布局
        // GridLayout gridLayout = new GridLayout(4, 2);
        // leftNorthPanel.setLayout(gridLayout);
        // leftNorthPanel.add(enableExtJRadioButton);
        // leftNorthPanel.add(enableUpdateTableJRadioButton);
        // leftNorthPanel.add(apiURLJLabel);
        // leftNorthPanel.add(apiURLJTextField);
        // leftNorthPanel.add(captchaURLJLabel);
        // leftNorthPanel.add(captchaURLJTextField);
        // leftNorthPanel.add(keyRegexJLabel);
        // leftNorthPanel.add(keyRegexJTextField);
        // leftNorthPanel.add(valueRegexJLabel);
        // leftNorthPanel.add(valueRegexJTextField);
        // leftNorthPanel.add(getCaptchaJButton);
        */

        // 左下
        JSplitPane leftSouthPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        UserInterface userInterface = api.userInterface();
        requestViewer = userInterface.createHttpRequestEditor();
        responseViewer = userInterface.createHttpResponseEditor();
        JTabbedPane requestJTabbedPane = new JTabbedPane();
        JTabbedPane responseJTabbedPane = new JTabbedPane();
        requestJTabbedPane.addTab("验证码Request", requestViewer.uiComponent());
        responseJTabbedPane.addTab("验证码Response", responseViewer.uiComponent());
        leftSouthPanel.setLeftComponent(requestJTabbedPane);
        leftSouthPanel.setRightComponent(responseJTabbedPane);
        // 整合左面
        JSplitPane leftPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        leftPane.setLeftComponent(leftNorthPanel);
        leftPane.setRightComponent(leftSouthPanel);

        // 右面
        JTable table = new JTable(tableModel) {
        };
        JScrollPane rightPane0 = new JScrollPane(table);
        JButton clearCaptchaJButton = new JButton("清空列表");
        JSplitPane rightPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        rightPane.setLeftComponent(clearCaptchaJButton);
        rightPane.setRightComponent(rightPane0);
        // 全部整合
        globalJPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        globalJPane.setLeftComponent(leftPane);
        globalJPane.setRightComponent(rightPane);

        globalJPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                globalJPane.setDividerLocation(0.7);
            }
        });
        leftSouthPanel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                leftSouthPanel.setDividerLocation(0.5);
            }
        });


        getCaptchaJButton.addActionListener(e -> {
            SwingWorker<Void, HttpRequestResponse> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    try {
                        HttpRequestResponse requestResponse = getHttpResponseFromCaptchaReq();
                        publish(requestResponse); // 发布结果
                    } catch (Exception ex) {
                        publish((HttpRequestResponse) null);
                    }
                    return null;
                }

                @Override
                protected void process(List<HttpRequestResponse> requestResponseList) {
                    for (HttpRequestResponse requestResponse : requestResponseList) {
                        if (requestResponse != null) {
                            List<Marker> markers = getMarkerListFromHttpResponse(requestResponse);
                            uiHttpRequestResponse = requestResponse;
                            // todo
                            responseViewer.setResponse(requestResponse.response().withMarkers(markers));

                        }

                    }
                }
            };
            worker.execute();
        });
        chkCaptchaJButton.addActionListener(e -> {
            SwingWorker<Void, CaptchaEntity> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    try {
                        CaptchaEntity captchaEntity = getCaptchaFromResponse(uiHttpRequestResponse);
                        sendCaptchaChk(captchaEntity);

                        publish(captchaEntity); // 发布结果
                    } catch (Exception ex) {
                        publish(new CaptchaEntity());
                    }
                    return null;
                }

                @Override
                protected void process(List<CaptchaEntity> captchaEntities) {
                    for (CaptchaEntity captchaEntity : captchaEntities) {
                        tableModel.add(captchaEntity);
                        int newRow = tableModel.getRowCount() - 1;
                        ImageIcon imageIcon = captchaEntity.getImgIcon();
                        if (imageIcon.getIconHeight() > tableModel.getTable().getRowHeight()) {
                            table.setRowHeight(newRow, imageIcon.getIconHeight());
                        }
                    }
                }
            };
            worker.execute();
        });
        clearCaptchaJButton.addActionListener(e -> {
            this.tableModel.clearList();
        });

    }

    public HttpRequestResponse getHttpResponseFromCaptchaReq() {
        HttpRequestResponse requestResponse = null;
        try {
            String uri = captchaURLJTextField.getText().trim();
            java.net.URI url = new java.net.URI(uri);
            String scheme = url.getScheme();
            String host = url.getHost();
            int port = url.getPort();
            if (port == -1) {
                port = (scheme.equals("https")) ? 443 : (scheme.equals("http")) ? 80 : -1;
            }
            boolean secure = Objects.equals(scheme, "https");

            HttpService httpService = HttpService.httpService(host, port, secure);
            HttpRequest httpRequest = requestViewer.getRequest().withService(httpService);

            requestResponse = this.api.http().sendRequest(httpRequest);


        } catch (URISyntaxException e) {
            // throw new RuntimeException(e);

        }
        return requestResponse;
    }


    public List<Marker> getMarkerListFromHttpResponse(HttpRequestResponse requestResponse) {
        HttpResponse response = requestResponse.response();
        String keyItem = (String) keyComboBox.getSelectedItem();
        String valueItem = (String) valueComboBox.getSelectedItem();
        // String[] keyItems = {"ResBodyRegex", "ResBodyNone", "ResBodyRegex", "ResBodyJson", "ResBodyXml"};
        // String[] valueItems = {"ResBodyRegexBase64", "ResBodyAllByte", "ResBodyAllBase64", "ResBodyRegexBase64", "ResBodyJsonBase64", "ResBodyXmlBase64"};
        // String[] keyItems = {"KeyReg", "KeyNone"};
        // String[] valueItems = {"ImgRegBase64", "ImgAllBytes", "ImgAllBase64"};


        Marker keyMarker = switch (keyItem) {
            case "KeyReg" -> {
                Matcher matcher = Pattern.compile(keyJTextField.getText()).matcher(response.bodyToString());
                if (matcher.find()) {
                    keyRegJTextField.setText(matcher.group(1));
                    yield Marker.marker(response.bodyOffset() + matcher.start(1), response.bodyOffset() + matcher.end(1));
                } else yield null;
            }
            default -> null;
        };

        Marker dataMarker = switch (valueItem) {
            case "ImgAllBytes" ->
                    Marker.marker(response.bodyOffset(), response.bodyOffset() + response.body().length());
            case "ImgAllBase64" -> {
                valueRegJTextField.setText(response.bodyToString());
                yield Marker.marker(response.bodyOffset(), response.bodyOffset() + response.body().length());
            }
            case "ImgRegBase64", "ImgJsonBase64", "ImgXmlBase64" -> {
                Matcher matcher = Pattern.compile(valueTextField.getText()).matcher(response.bodyToString());
                if (matcher.find()) {
                    valueRegJTextField.setText(matcher.group(1));
                    yield Marker.marker(response.bodyOffset() + matcher.start(1), response.bodyOffset() + matcher.end(1));
                } else yield null;
            }
            default -> null;
        };
        return Arrays.asList(keyMarker, dataMarker);
    }


    public CaptchaEntity getCaptchaFromResponse(HttpRequestResponse requestResponse) {
        HttpResponse response = requestResponse.response();
        // CaptchaEntity captchaEntity = new CaptchaEntity("", null, "");
        CaptchaEntity captchaEntity = new CaptchaEntity();
        // String[] keyItems = {"KeyReg", "KeyNone"};
        // String[] valueItems = {"ImgRegBase64", "ImgAllBytes", "ImgAllBase64"};

        String keyItem = (String) keyComboBox.getSelectedItem();
        String valueItem = (String) valueComboBox.getSelectedItem();
        String imageKey = switch (keyItem) {
            case "KeyReg" -> {
                Matcher matcher = Pattern.compile(keyJTextField.getText()).matcher(response.bodyToString());
                if (matcher.find()) {
                    yield matcher.group(1);
                } else yield "";
            }
            default -> "";
        };
        byte[] imageData = switch (valueItem) {
            case "ImgAllBytes" -> response.body().getBytes();
            case "ImgAllBase64" -> Base64.getDecoder().decode(response.body().getBytes());
            case "ImgRegBase64", "ImgJsonBase64", "ImgXmlBase64" -> {
                Matcher matcher = Pattern.compile(valueTextField.getText()).matcher(response.bodyToString());
                if (matcher.find()) {
                    yield Base64.getDecoder().decode(matcher.group(1));
                } else yield null;
            }
            default -> null;
        };

        try {
            if (imageData != null) {
                InputStream buff = new ByteArrayInputStream(imageData);
                Image img = ImageIO.read(buff);
                ImageIcon icon = new ImageIcon(img);
                captchaEntity.setImgIcon(icon);
                // int columnWidth = this.tableModel.getTable().getColumn(0).getWidth();

                // 宽度缩放
                // if (icon.getIconWidth() > columnWidth) {
                //     Image scaledImage = icon.getImage().getScaledInstance(
                //             columnWidth, -1, Image.SCALE_SMOOTH);
                //     captchaEntity.setImgIcon(new ImageIcon(scaledImage));
                // } else {
                //     captchaEntity.setImgIcon(icon);
                // }
            }
        } catch (IOException e) {
            // throw new RuntimeException(e);
        }
        captchaEntity.setImgData(imageData);
        captchaEntity.setImgKey(imageKey);
        return captchaEntity;
    }

    public void sendCaptchaChk(CaptchaEntity captchaEntity) {
        if (captchaEntity.getImgData() == null) {
            captchaEntity.setImgRes("");
        } else {
            try {
                java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder().uri(
                                java.net.URI.create(apiURLJTextField.getText().trim()))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .POST(java.net.http.HttpRequest.BodyPublishers.ofByteArray(captchaEntity.getImgData()))
                        .build();
                java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
                captchaEntity.setImgRes(response.body());
            } catch (Exception ignored) {
            }

        }
    }

    public MontoyaApi getApi() {
        return api;
    }
}
