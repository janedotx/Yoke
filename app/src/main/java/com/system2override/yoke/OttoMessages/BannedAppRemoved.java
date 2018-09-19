package com.system2override.yoke.OttoMessages;

import android.content.pm.ApplicationInfo;

public class BannedAppRemoved {
    private ApplicationInfo appInfo;

    public BannedAppRemoved(ApplicationInfo appInfo) {
        this.appInfo = appInfo;
    }
}
