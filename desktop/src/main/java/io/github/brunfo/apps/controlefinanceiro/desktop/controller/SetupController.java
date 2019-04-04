package io.github.brunfo.apps.controlefinanceiro.desktop.controller;

import io.github.brunfo.apps.controlefinanceiro.desktop.MainApp;

public class SetupController implements OverviewController {


    private MainApp mainApp;

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp= mainApp;
    }
}
