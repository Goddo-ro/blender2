package com.cgvsu.controllers;

import com.cgvsu.log.Log;
import com.cgvsu.log.Statuses;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

import static com.cgvsu.utils.LogsUtils.generateLabelFromLog;

public class LogController {
    private final VBox console;
    private final AnchorPane consolePane;
    private final ScrollPane consoleScroll;

    public LogController(VBox console, AnchorPane consolePane, ScrollPane consoleScroll) {
        this.console = console;
        this.consolePane = consolePane;
        this.consoleScroll = consoleScroll;
    }

    private final List<Log> logs = new ArrayList<>();

    public List<Log> getLogs() {
        return logs;
    }

    public void addLog(String body, Statuses status) {
        logs.add(0, new Log(body, status));
        updateLogs();
    }

    void updateLogs() {
        console.getChildren().clear();
        for (Log log : logs) {
            Label label = generateLabelFromLog(log);
            console.getChildren().add(label);
        }

        console.setMinHeight(console.getChildren().size() * 30 - 12);
        consolePane.setPrefHeight(console.getChildren().size() * 30 - 12);
        consoleScroll.setVmax(console.getChildren().size() * 30 - 12);
        consoleScroll.setVvalue(console.getChildren().size() * 30 - 12);
    }
}
