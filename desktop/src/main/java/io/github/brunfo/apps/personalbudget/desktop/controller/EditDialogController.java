package io.github.brunfo.apps.personalbudget.desktop.controller;

import io.github.brunfo.apps.personalbudget.controller.OverviewController;
import javafx.stage.Stage;

public interface EditDialogController extends OverviewController {

    void setDialogStage(Stage dialogStage);

    Stage getDialogStageStage();
}

