package com.solexgames.lib.commons.util;

import com.google.common.collect.ImmutableSet;
import lombok.experimental.UtilityClass;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author StackOverflow / modified by GrowlyX
 */

@UtilityClass
public final class BukkitUtil {

    public static void registerListenersIn(JavaPlugin plugin, String packageName) {
        final Collection<Class<?>> collection = BukkitUtil.getClassesInPackage(plugin, packageName);

        collection.forEach(aClass -> {
            try {
                plugin.getServer().getPluginManager().registerEvents((Listener) aClass.newInstance(), plugin);
            } catch (Exception ignored) {
            }
        });

        plugin.getLogger().info("Loaded listeners.");
    }

    public static Collection<Class<?>> getClassesInPackage(Plugin plugin, String packageName) {
        final Collection<Class<?>> classes = new ArrayList<>();

        final CodeSource codeSource = plugin.getClass().getProtectionDomain().getCodeSource();
        final URL resource = codeSource.getLocation();
        final String relPath = packageName.replace('.', '/');
        final String resPath = resource.getPath().replace("%20", " ");
        final String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");

        JarFile jarFile;

        try {
            jarFile = new JarFile(jarPath);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IOException reading JAR File '" + jarPath + "'", e);
        }

        final Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            String className = null;

            if (entryName.endsWith(".class") && entryName.startsWith(relPath) && entryName.length() > (relPath.length() + "/".length())) {
                className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
            }

            if (className != null) {
                Class<?> clazz = null;

                try {
                    clazz = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (clazz != null) {
                    classes.add(clazz);
                }
            }
        }

        try {
            jarFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (ImmutableSet.copyOf(classes));
    }
}
