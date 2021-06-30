package com.solexgames.lib.commons.processor;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import com.solexgames.lib.commons.command.annotation.Service;
import com.solexgames.lib.commons.util.BukkitUtil;
import lombok.SneakyThrows;
import org.bukkit.plugin.Plugin;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author GrowlyX
 * @since 6/29/2021
 */

public class AcfCommandProcessor extends PaperCommandManager {

    public AcfCommandProcessor(Plugin plugin) {
        super(plugin);
    }

    public void scanAndRegisterCommands(String pkg) {
        final Collection<Class<?>> classCollection = BukkitUtil.getClassesInPackage(this.plugin, pkg);

        classCollection.forEach(clazz -> {
            if (!clazz.isAnnotationPresent(Service.class)) {
                return;
            }

            final Service service = clazz.getAnnotation(Service.class);

            if (service.active()) {
                this.registerCommand(clazz);
            }
        });
    }

    @SneakyThrows
    private void registerCommand(Class<?> clazz) {
        final Object object = clazz.newInstance();

        if (object instanceof BaseCommand) {
            final BaseCommand baseCommand = (BaseCommand) object;

            this.registerCommand(baseCommand);
        }
    }
}
