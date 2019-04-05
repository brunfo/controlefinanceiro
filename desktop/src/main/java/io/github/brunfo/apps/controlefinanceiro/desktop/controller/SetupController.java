package io.github.brunfo.apps.controlefinanceiro.desktop.controller;

import io.github.brunfo.apps.controlefinanceiro.desktop.DesktopApp;

public class SetupController implements OverviewController {


    private DesktopApp desktopApp;

    @Override
    public void setDesktopApp(DesktopApp desktopApp) {
        this.desktopApp = desktopApp;
    }
}
