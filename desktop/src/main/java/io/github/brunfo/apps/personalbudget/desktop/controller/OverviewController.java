package io.github.brunfo.apps.personalbudget.desktop.controller;


import io.github.brunfo.apps.personalbudget.desktop.DesktopApp;

public interface OverviewController {

    /**
     * É chamado pela aplicação principal para referenciar a si mesma.
     *
     * @param controller
     */
    void setDesktopApp(DesktopApp controller);
}

