package io.github.brunfo.apps.personalbudget.desktop.controller;


import io.github.brunfo.apps.personalbudget.desktop.DesktopApp;

public interface OverviewController {

    /**
     * Called by desktop to references it self.
     *
     * @param controller
     */
    void setDesktopApp(DesktopApp controller);
}

