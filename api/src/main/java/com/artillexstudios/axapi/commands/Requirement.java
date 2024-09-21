package com.artillexstudios.axapi.commands;

import org.bukkit.command.CommandSender;

public interface Requirement<T> {

    boolean isMet(CommandSender sender, T context);
}
