package client;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SplittyConfig {
    private String splittyServerUrl;
    private String splittyWebsocketUrl;
    public SplittyConfig() {
        loadProperties();
    }

    private void loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("client/src/main/resources/application.properties")) {
            properties.load(fis);
            splittyServerUrl = properties.getProperty("splitty.server.url");
            splittyWebsocketUrl = properties.getProperty("splitty.websocket.url");
        } catch (IOException e) {
            //TODO
            e.printStackTrace();
        }
    }

    public String getSplittyServerUrl() {
        return splittyServerUrl;
    }
    public String getSplittyWebsocketUrl(){
        return splittyWebsocketUrl;
    }
}
