package com.system2override.yoke.OttoMessages;

public class ForegroundMessage {
    // need this and CurrentAppMessage. see the comment in
    // ForegroundAppObserverThread.publishAppChanges
    String app;

    public ForegroundMessage(String foregroundApp) {
        this.app = foregroundApp;
    }

    public String getApp() {
        return this.app;
    }
}
