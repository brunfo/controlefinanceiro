package io.github.brunfo.apps.controlefinanceiro.desktop.controller;

import io.github.brunfo.apps.controlefinanceiro.controller.MainController;
import io.github.brunfo.apps.controlefinanceiro.controller.OverviewController;
import io.github.brunfo.apps.controlefinanceiro.desktop.DesktopApp;

public class SetupController implements OverviewController {


    private DesktopApp desktopApp;

    @Override
    public void setMainController(MainController controller) {
        this.desktopApp = (DesktopApp) controller;
    }
}
