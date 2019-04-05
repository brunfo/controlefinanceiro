package io.github.brunfo.apps.controlefinanceiro.desktop.controller;

import io.github.brunfo.apps.controlefinanceiro.controller.OverviewController;
import javafx.stage.Stage;

public interface EditDialogController extends OverviewController {

    void setDialogStage(Stage dialogStage);

    Stage getDialogStageStage();
}

