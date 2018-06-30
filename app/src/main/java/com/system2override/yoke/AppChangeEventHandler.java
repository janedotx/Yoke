package com.system2override.yoke;

public class AppChangeEventHandler {
        private String lastEvent;

        public AppChangeEventHandler(String event) {
            MyApplication.getBus().register(this);
            this.lastEvent = event;
        }

        public void subscribeToAppChangeEvents(String event) {
            if (event != lastEvent) {
//                    MyApplication.getBus().post();
            }
        }
}
