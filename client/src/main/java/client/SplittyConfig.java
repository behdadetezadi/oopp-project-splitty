package client;

import client.utils.AlertUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SplittyConfig {
    private String splittyServerUrl;
    private String splittyWebsocketUrl;
    public SplittyConfig() {
        loadProperties();
        generateWebsocketUrl();

    }

    private void loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("application.properties")) {
            properties.load(fis);
            splittyServerUrl = properties.getProperty("splitty.server.url").trim();
            splittyWebsocketUrl = properties.getProperty("splitty.websocket.url").trim();
        } catch (IOException e) {
            AlertUtils.showErrorAlert("Server Error", "File not Fonund", "Cannot Find application.properties please check the file in the root of the project");
        }
    }

    private void generateWebsocketUrl() {
        if (splittyWebsocketUrl == null || splittyWebsocketUrl.isEmpty()) {
            String wsScheme = splittyServerUrl.startsWith("https") ? "wss://" : "ws://";
            splittyWebsocketUrl = wsScheme + splittyServerUrl.substring(splittyServerUrl.indexOf("://") + 3);
            if (!splittyWebsocketUrl.endsWith("/websocket")) {
                splittyWebsocketUrl += "websocket";
            }
        }
    }

    public String getSplittyServerUrl() {
        return splittyServerUrl;
    }
    public String getSplittyWebsocketUrl(){
        return splittyWebsocketUrl;
    }
}
