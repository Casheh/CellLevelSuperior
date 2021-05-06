package net.casheh.celllevel.events;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import net.casheh.celllevel.CellLevel;
import net.casheh.celllevel.managers.IslandUtilities;
import net.casheh.celllevel.managers.PlayerUtilities;
import net.casheh.celllevel.managers.Wand;
import net.casheh.celllevel.nbt.NBTEditor;
import net.casheh.celllevel.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ChestClickListener implements Listener {

    @EventHandler
    public void onChestClick(PlayerInteractEvent e) {

        if (e.getClickedBlock() == null)
            return;
        if (e.getPlayer().getInventory().getItemInMainHand() == null || e.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR)
            return;
        if (!(e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.TRAPPED_CHEST))
            return;

        Player player = (Player) e.getPlayer();
        Chest chest = (Chest) e.getClickedBlock().getState();
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();
        IslandUtilities islandUtilities = new IslandUtilities(island);
        PlayerUtilities playerUtilities = new PlayerUtilities(player.getUniqueId());

        if (NBTEditor.contains(e.getPlayer().getInventory().getItemInMainHand(), "beaconWand")) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                e.setCancelled(true);

                Bukkit.getScheduler().runTaskAsynchronously(CellLevel.inst, new Runnable() {
                    ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
                    @Override
                    public void run() {
                        Wand wand = new Wand(item);
                        if (wand.usable()) {
                            if (SuperiorSkyblockAPI.getPlayer(player).getIsland() == null) {
                                player.sendMessage(CellLevel.inst.getCfg().getNotAtIsland());
                                return;
                            }
                            if (!SuperiorSkyblockAPI.getPlayer(player).getIsland().isInside(player.getLocation())) {
                                player.sendMessage(CellLevel.inst.getCfg().getNotAtIsland());
                                return;
                            }

                            int beacons = Util.getMaterialCount(Material.BEACON, chest.getInventory());
                            int sponge = Util.getMaterialCount(Material.SPONGE, chest.getInventory());

                            chest.getInventory().remove(Material.BEACON);
                            chest.getInventory().remove(Material.SPONGE);

                            player.sendMessage(ChatColor.AQUA  + "Beacons: " + beacons);
                            player.sendMessage(ChatColor.GOLD + "Sponge: " + sponge);

                            islandUtilities.setBeacons(islandUtilities.getBeacons() + beacons);
                            islandUtilities.setSponge(islandUtilities.getSponge() + sponge);
                            playerUtilities.setBeacons(playerUtilities.getBeacons() + beacons);
                            playerUtilities.setSponge(playerUtilities.getSponge() + sponge);
                            wand.setUses(wand.getUses() - 1);
                            e.getPlayer().getInventory().setItemInMainHand(wand.getItem());
                        } else {
                            e.getPlayer().sendMessage(CellLevel.inst.getCfg().getPrefix() + Util.color("&cYour wand has ran out of charge!"));
                        }
                    }
                });
            }
        }
    }


}