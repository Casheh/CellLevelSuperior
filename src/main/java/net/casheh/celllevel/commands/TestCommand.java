package net.casheh.celllevel.commands;

import net.casheh.celllevel.inventory.WithdrawInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player p = (Player) sender;

        WithdrawInventory.inv.open(p);

        return true;

    }

}
