package com.system2override.yoke.OttoMessages;

public class CurrentAppMessage {
    // need this and ForegroundMessage. see the comment in
    // ForegroundAppObserverThread.publishAppChanges
    String currentApp;

    public CurrentAppMessage(String currentApp) {
        this.currentApp = currentApp;
    }
    public String getCurrentApp() { return currentApp; }
}
