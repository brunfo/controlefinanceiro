package io.github.brunfo.apps.personalbudget.desktop.controller;


import io.github.brunfo.apps.personalbudget.controller.MainController;
import io.github.brunfo.apps.personalbudget.controller.OverviewController;
import io.github.brunfo.apps.personalbudget.desktop.DesktopApp;

public class BudgetController implements OverviewController {

    private DesktopApp desktopApp;

    @Override
    public void setMainController(MainController controller) {
        this.desktopApp = (DesktopApp) controller;
    }
}
