package com.system2override.yoke.OttoMessages;

import android.content.pm.ApplicationInfo;

public class BannedAppAdded {
    public ApplicationInfo appInfo;

    public BannedAppAdded(ApplicationInfo appInfo) {
        this.appInfo = appInfo;
    }
}
