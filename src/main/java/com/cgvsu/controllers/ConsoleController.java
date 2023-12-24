package com.cgvsu.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;

public class ConsoleController {
    private SplitPane mainSplit;
    private Button toggleConsoleBtn;

    private boolean isConsoleClosed = true;
    final double CLOSED_CONSOLE_POSITION = 0.96;

    double openedConsolePosition = 0.5;
    boolean isToggleMenuClick = false;
    boolean windowResizing;

    public ConsoleController(SplitPane mainSplit, Button toggleConsoleBtn) {
        // TODO: fix toggle for full screen mode
        this.mainSplit = mainSplit;
        this.toggleConsoleBtn = toggleConsoleBtn;

        mainSplit.getDividers().get(0).positionProperty().addListener(
                (o, oldPos, newPos) -> dividerResized(oldPos, newPos));
    }

    private void dividerResized(Number oldPos,
                                Number newPos) {
        if (mainSplit.getHeight() - mainSplit.getHeight() * (double) newPos < 24) {
            mainSplit.setDividerPositions(openedConsolePosition);
            return;
        }

        if (windowResizing) {
            windowResizing = false;
            return;
        }

        if (!isToggleMenuClick) {
            openedConsolePosition = (double) newPos;
        }

        isToggleMenuClick = false;

        if (isConsoleClosed) {
            changeConsoleBtnState();
            isConsoleClosed = false;
        }
    }

    private double getDividerCoef() {
        return (mainSplit.getHeight() - 24) / mainSplit.getHeight();
    }

    private void changeConsoleBtnState() {
        if (isConsoleClosed) {
            toggleConsoleBtn.setText("-");
        } else {
            toggleConsoleBtn.setText("+");
        }
    }

    void toggleConsole() {
        isToggleMenuClick = true;
        changeConsoleBtnState();
        if (isConsoleClosed) {
            mainSplit.setDividerPositions(openedConsolePosition);
        } else {
            mainSplit.setDividerPositions(getDividerCoef());
        }

        isConsoleClosed = !isConsoleClosed;
    }
}
