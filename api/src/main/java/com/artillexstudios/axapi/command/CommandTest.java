package com.artillexstudios.axapi.command;

import com.artillexstudios.axapi.command.annotation.Command;
import com.artillexstudios.axapi.command.annotation.Optional;
import com.artillexstudios.axapi.command.annotation.Permission;
import com.artillexstudios.axapi.command.annotation.Range;
import com.artillexstudios.axapi.command.annotation.Sender;
import com.artillexstudios.axapi.command.annotation.SubCommand;
import com.artillexstudios.axapi.command.annotation.Switch;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command({"axcrate", "axcrates", "crate", "crates"})
@Permission("axcrates.permission")
public class CommandTest {

    // List<Player>
    @SubCommand("")
    public void give(@Sender CommandSender sender, Player[] players, @Optional @Range(min = 1, max = 64) Integer amount, @Switch boolean physical) {

    }
}
