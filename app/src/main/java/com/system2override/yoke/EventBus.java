package com.system2override.yoke;

import com.squareup.otto.Bus;

public class EventBus {

    private static final Bus BUS = new Bus();

    // sweet and here i was wondering how to get the service to talk back to the activity
    public static Bus getInstance() {
        return BUS;
    }
}
