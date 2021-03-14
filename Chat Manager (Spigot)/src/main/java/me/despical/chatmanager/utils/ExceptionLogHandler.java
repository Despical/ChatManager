package me.despical.chatmanager.utils;

import me.despical.chatmanager.Main;
import org.bukkit.Bukkit;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author Despical
 * <p>
 * Created at 27.02.2021
 */
public class ExceptionLogHandler extends Handler {

    private final Main plugin;

    public ExceptionLogHandler(Main plugin) {
        this.plugin = plugin;

        Bukkit.getLogger().addHandler(this);
    }

    @Override
    public void publish(LogRecord record) {
        Throwable throwable = record.getThrown();

        if (!(throwable instanceof Exception) || throwable.getClass().getSimpleName().contains("Exception")) {
            return;
        }

        if (throwable.getStackTrace().length <= 0) {
            return;
        }

        if (!throwable.getStackTrace()[0].getClassName().contains("me.despical.chatmanager")) {
            return;
        }

        record.setThrown(null);

        Exception exception = throwable.getCause() != null ? (Exception) throwable.getCause() : (Exception) throwable;
        StringBuilder stacktrace = new StringBuilder(exception.getClass().getSimpleName());

        if (exception.getMessage() != null) {
            stacktrace.append(" (").append(exception.getMessage()).append(")");
        }

        stacktrace.append("\n");

        for (StackTraceElement str : exception.getStackTrace()) {
            stacktrace.append(str.toString()).append("\n");
        }

        plugin.getLogger().log(Level.WARNING, "[Reporter service] <<-----------------------------[START]----------------------------->>");
        plugin.getLogger().log(Level.WARNING, stacktrace.toString());
        plugin.getLogger().log(Level.WARNING, "[Reporter service] <<------------------------------[END]------------------------------>>");

        record.setMessage("[Chat Manager] We have found a bug in the code. Contact developer at his official Discord (ID: Despical#4316) with the following error given above!");
    }

    @Override
    public void flush() {}

    @Override
    public void close() throws SecurityException { }
}