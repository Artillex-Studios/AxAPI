package com.artillexstudios.axapi.commands;

import org.bukkit.command.CommandSender;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER})
public @interface Permission {

    String[] value();

    PermissionMode mode() default PermissionMode.EITHER;

    enum PermissionMode implements Requirement<List<String>> {
        EITHER {
            @Override
            public boolean isMet(CommandSender sender, List<String> permission) {
                for (String s : permission) {
                    if (sender.hasPermission(s)) {
                        return true;
                    }
                }

                return false;
            }
        },
        ALL {
            @Override
            public boolean isMet(CommandSender sender, List<String> permission) {
                for (String s : permission) {
                    if (!sender.hasPermission(s)) {
                        return false;
                    }
                }

                return true;
            }
        };

        public abstract boolean isMet(CommandSender sender, List<String> permission);
    }
}
