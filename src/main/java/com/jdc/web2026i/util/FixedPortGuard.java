package com.jdc.web2026i.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FixedPortGuard {

    private static final int PORT = 8086;
    private static final Path PID_FILE = Paths.get(System.getProperty("user.dir"), "target", "web-2026-i.pid");
    private static final Pattern NETSTAT_LINE = Pattern.compile("^\\s*TCP\\s+\\S+:" + PORT + "\\s+\\S+\\s+LISTENING\\s+(\\d+)\\s*$", Pattern.CASE_INSENSITIVE);

    private FixedPortGuard() {
    }

    public static void reclaimPort() {
        long currentPid = ProcessHandle.current().pid();
        stopPidFromFile(currentPid);
        stopAnyListenerOnPort(currentPid);
        writePidFile(currentPid);
        Runtime.getRuntime().addShutdownHook(new Thread(FixedPortGuard::deletePidFile));
    }

    private static void stopPidFromFile(long currentPid) {
        if (!Files.exists(PID_FILE)) {
            return;
        }

        try {
            String value = Files.readString(PID_FILE, StandardCharsets.UTF_8).trim();
            if (value.isEmpty()) {
                return;
            }

            long pid = Long.parseLong(value);
            if (pid != currentPid) {
                killProcess(pid);
            }
        } catch (Exception ignored) {
            // Si no se puede leer el PID anterior, se continúa con el siguiente intento.
        }
    }

    private static void stopAnyListenerOnPort(long currentPid) {
        for (Long pid : findListeningPids()) {
            if (pid != currentPid) {
                killProcess(pid);
            }
        }
    }

    private static Set<Long> findListeningPids() {
        Set<Long> pids = new HashSet<>();
        Process process = null;
        try {
            process = new ProcessBuilder("cmd", "/c", "netstat -ano -p tcp").start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Matcher matcher = NETSTAT_LINE.matcher(line);
                    if (matcher.matches()) {
                        pids.add(Long.parseLong(matcher.group(1)));
                    }
                }
            }
            process.waitFor();
        } catch (Exception ignored) {
            // Si netstat no está disponible o falla, no bloqueamos el arranque.
        } finally {
            if (process != null) {
                process.destroyForcibly();
            }
        }
        return pids;
    }

    private static void killProcess(long pid) {
        try {
            ProcessHandle.of(pid).ifPresent(handle -> {
                handle.destroy();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (handle.isAlive()) {
                    handle.destroyForcibly();
                }
            });
        } catch (Exception ignored) {
            // No detenemos el arranque si el proceso no se pudo matar.
        }
    }

    private static void writePidFile(long pid) {
        try {
            Files.createDirectories(PID_FILE.getParent());
            Files.writeString(PID_FILE, Long.toString(pid), StandardCharsets.UTF_8);
        } catch (IOException ignored) {
            // El archivo PID es solo auxiliar.
        }
    }

    private static void deletePidFile() {
        try {
            Files.deleteIfExists(PID_FILE);
        } catch (IOException ignored) {
            // Nada que hacer.
        }
    }
}

