package com.system2override.hobbes.OttoMessages;

import android.content.pm.ApplicationInfo;

public class BannedAppRemoved {
    public ApplicationInfo appInfo;

    public BannedAppRemoved(ApplicationInfo appInfo) {
        this.appInfo = appInfo;
    }
}
