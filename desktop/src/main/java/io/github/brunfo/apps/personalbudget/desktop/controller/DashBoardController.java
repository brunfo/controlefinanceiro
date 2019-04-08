package io.github.brunfo.apps.personalbudget.desktop.controller;


import io.github.brunfo.apps.personalbudget.desktop.DesktopApp;

public class DashBoardController implements OverviewController {


    private DesktopApp desktopApp;

    @Override
    public void setDesktopApp(DesktopApp controller) {
        this.desktopApp = controller;
    }
}
