package io.github.brunfo.apps.controlefinanceiro.controller;

import io.github.brunfo.apps.controlefinanceiro.MainApp;

public class SetupController implements OverviewController {


    private MainApp mainApp;

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp= mainApp;
    }
}
