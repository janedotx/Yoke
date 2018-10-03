package com.system2override.hobbes.AppLimit;

// logic for what user can 'do' goes here
public class AppLimitScreenPresenter {
    private AppLimitScreen view;

    public AppLimitScreenPresenter(AppLimitScreen screen) {
        this.view = screen;
    }
    public void destroy() {
        this.view = null;
    }


}
