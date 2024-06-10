package org.tuiasi.engine.logic.logger;

import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.ui.DefaultEngineEditorUI;
import org.tuiasi.engine.ui.components.basicComponents.textbox.Textbox;

import java.time.Instant;
import java.time.ZoneId;

public class Log {

    static Textbox logsTextbox;
    static boolean initialized = false;

    public static void init(){
        logsTextbox = (Textbox) DefaultEngineEditorUI.getWindow("Debug logs").getComponentByLabel("Logs");
        initialized = true;

        Log.info("Tutel engine version: " + AppLogic.getAppVersion());
    }

    public static void info(String message) {
        if (initialized) {
            logsTextbox.setText(logsTextbox.getText() + getCurrentTime() + " INFO: " + message + "\n");
        }
    }

    public static void error(String message) {
        if (initialized)
            logsTextbox.setText(logsTextbox.getText() + getCurrentTime() + " ERROR: " + message + "\n");
    }

    public static void warning(String message) {
        if (initialized)
            logsTextbox.setText(logsTextbox.getText() + getCurrentTime() + " WARNING: " + message + "\n");
    }

    private static String getCurrentTime(){
        Instant currentMoment = java.time.Instant.now();
        ZoneId zoneId = ZoneId.systemDefault();
        // create a string with hh:mm:ss format that excludes the milliseconds
        return "[" + currentMoment.atZone(zoneId).toLocalTime().toString().substring(0, 8) + "]";
    }
}
