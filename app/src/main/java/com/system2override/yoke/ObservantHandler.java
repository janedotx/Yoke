package com.system2override.yoke;

import android.os.Handler;

import java.util.concurrent.locks.ReentrantLock;

public class ObservantHandler extends Handler {
    private final ReentrantLock lock;

    public ObservantHandler(ReentrantLock lock) {
        this.lock = lock;
    }

}
