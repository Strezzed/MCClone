package org.strezz.mcclone;


import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.ConsoleWriter;
import org.pmw.tinylog.writers.FileWriter;

public final class MCClone {

    public static void main(String[] args) {
        createLogger(true, true);

        Logger.info("Test <3");
    }

    public static void createLogger(boolean fileLogging, boolean consoleLogging) {
        if (!fileLogging && !consoleLogging) {
            return;
        }

        final Configurator logConfigurator = Configurator.defaultConfig();
        logConfigurator.removeAllWriters();

        if (fileLogging) {
            logConfigurator.addWriter(new FileWriter("game.log"));
        }

        if (consoleLogging) {
            logConfigurator.addWriter(new ConsoleWriter());
        }

        logConfigurator.formatPattern("[GAME] {date:dd-MM-YYYY HH:mm:ss} [{thread}] {level}: {message}");
        logConfigurator.activate();
    }


}
