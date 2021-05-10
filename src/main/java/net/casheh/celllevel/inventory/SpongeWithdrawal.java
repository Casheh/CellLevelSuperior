package net.casheh.celllevel.inventory;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import net.casheh.celllevel.CellLevel;
import net.casheh.celllevel.managers.IslandUtilities;
import net.casheh.celllevel.managers.PlayerUtilities;
import net.casheh.celllevel.util.ItemBuilder;
import net.casheh.celllevel.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Array;
import java.util.Arrays;

public class SpongeWithdrawal implements InventoryProvider {

    public static final SmartInventory inv = SmartInventory.builder()
            .id("spongeWithdrawal")
            .provider(new SpongeWithdrawal())
            .size(3, 9)
            .title(Util.color("&6Withdraw Sponges"))
            .manager(CellLevel.inst.getInventoryManager())
            .build();

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        inventoryContents.set(1, 1, ClickableItem.of(new ItemBuilder(Material.STAINED_GLASS_PANE).displayname("&c&l-64").damage((short)14).build(), e -> {
            int amount = Util.getContainingNumber(Util.strip(inventoryContents.get(SlotPos.of(1, 4)).get().getItem().getItemMeta().getLore().get(1)));
            if (amount > 64)
                amount -= 64;
            else
                amount = 1;
            setSponge(inventoryContents, amount);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 1);
        }));
        inventoryContents.set(1, 2, ClickableItem.of(new ItemBuilder(Material.STAINED_GLASS_PANE).displayname("&c&l-10").damage((short)14).build(), e -> {
            int amount = Util.getContainingNumber(Util.strip(inventoryContents.get(SlotPos.of(1, 4)).get().getItem().getItemMeta().getLore().get(1)));
            if (amount > 10)
                amount -= 10;
            else
                amount = 1;
            setSponge(inventoryContents, amount);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 1);
        }));
        inventoryContents.set(1, 3, ClickableItem.of(new ItemBuilder(Material.STAINED_GLASS_PANE).displayname("&c&l-1").damage((short)14).build(), e -> {
            int amount = Util.getContainingNumber(Util.strip(inventoryContents.get(SlotPos.of(1, 4)).get().getItem().getItemMeta().getLore().get(1)));
            if (amount > 1)
                setSponge(inventoryContents, amount - 1);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 1);
        }));
        setSponge(inventoryContents, 1);
        inventoryContents.set(2, 4, ClickableItem.of(getMaxItem(), e -> {
            final PlayerUtilities playerUtilities = new PlayerUtilities(player.getUniqueId());
            final IslandUtilities islandUtilities = new IslandUtilities(SuperiorSkyblockAPI.getPlayer(player).getIsland());
            int emptySpace = Util.getEmptySpace(player.getInventory(), Material.SPONGE);

            if (playerUtilities.getSponge() == 0) {
                player.closeInventory();
                player.sendMessage(CellLevel.inst.getCfg().getPrefix() + Util.color("&cYou do not have any sponges to withdraw!"));
                return;
            }

            if (emptySpace == 0) {
                player.closeInventory();
                player.sendMessage(CellLevel.inst.getCfg().getPrefix() + Util.color("&cYour inventory is full!"));
                return;
            }

            if (playerUtilities.getBeacons() > Util.getEmptySpace(player.getInventory(), Material.SPONGE)) {
                player.closeInventory();
                player.getInventory().addItem(new ItemStack(Material.SPONGE, emptySpace));
                player.sendMessage(CellLevel.inst.getCfg().getPrefix() + Util.color("&aYou have withdrawn &b" + emptySpace + "&6 sponges!"));
                playerUtilities.setSponge(playerUtilities.getSponge() - emptySpace);
                islandUtilities.setSponge(islandUtilities.getSponge() - emptySpace);
                return;
            }

            player.closeInventory();
            player.getInventory().addItem(new ItemStack(Material.SPONGE, playerUtilities.getSponge()));
            player.sendMessage(CellLevel.inst.getCfg().getPrefix() + Util.color("&aYou have withdrawn &b" + playerUtilities.getBeacons() + "&6 sponges!"));
            islandUtilities.setSponge(islandUtilities.getSponge() - playerUtilities.getSponge());
            playerUtilities.setSponge(0);

        }));
        inventoryContents.set(1, 5, ClickableItem.of(new ItemBuilder(Material.STAINED_GLASS_PANE).displayname("&a&l+1").damage((short)13).build(), e -> {
            int amount = Util.getContainingNumber(Util.strip(inventoryContents.get(SlotPos.of(1, 4)).get().getItem().getItemMeta().getLore().get(1)));
            if (amount + 1 < Integer.MAX_VALUE)
                setSponge(inventoryContents, amount + 1);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 1);
        }));
        inventoryContents.set(1, 6, ClickableItem.of(new ItemBuilder(Material.STAINED_GLASS_PANE).displayname("&a&l+10").damage((short)13).build(), e -> {
            int amount = Util.getContainingNumber(Util.strip(inventoryContents.get(SlotPos.of(1, 4)).get().getItem().getItemMeta().getLore().get(1)));
            if (amount + 10 < Integer.MAX_VALUE)
                setSponge(inventoryContents, amount + 10);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 1);
        }));
        inventoryContents.set(1, 7, ClickableItem.of(new ItemBuilder(Material.STAINED_GLASS_PANE).displayname("&a&l+64").damage((short)13).build(), e -> {
            int amount = Util.getContainingNumber(Util.strip(inventoryContents.get(SlotPos.of(1, 4)).get().getItem().getItemMeta().getLore().get(1)));
            if (amount + 64 < Integer.MAX_VALUE)
                setSponge(inventoryContents, amount + 64);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 1);
        }));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {}

    private void setSponge(InventoryContents inventoryContents, int amount) {
        String lore;
        if (amount == 1)
            lore = "&o&fClick here to withdraw &6 " + amount + "&f&o sponge!";
        else
            lore = "&o&fClick here to withdraw &6 " + amount + "&f&o sponges!";
        inventoryContents.set(1, 4, ClickableItem.of(new ItemBuilder(Material.SPONGE).displayname("&6&lWITHDRAW").lore(" ").lore(lore).build(), e -> {
            int quantity = Util.getContainingNumber(Util.strip(e.getCurrentItem().getItemMeta().getLore().get(1)));
            withdraw((Player) e.getWhoClicked(), amount);
        }));
    }

    private void withdraw(Player player, int amount) {
        PlayerUtilities playerUtilities = new PlayerUtilities(player.getUniqueId());
        IslandUtilities islandUtilities = new IslandUtilities(SuperiorSkyblockAPI.getPlayer(player).getIsland());
        int emptySpace = Util.getEmptySpace(player.getInventory(), Material.SPONGE);

        if (playerUtilities.getSponge() < amount || playerUtilities.getSponge() == 0) {
            player.closeInventory();
            player.sendMessage(CellLevel.inst.getCfg().getPrefix() + Util.color("&cYou do not have enough sponges!"));
            return;
        }

        if (emptySpace < amount) {
            if (emptySpace == 0) {
                player.closeInventory();
                player.sendMessage(CellLevel.inst.getCfg().getPrefix() + Util.color("&cYour inventory is full!"));
                return;
            }

            player.closeInventory();
            player.sendMessage(CellLevel.inst.getCfg().getPrefix() + Util.color("&aYou have withdrawn &6" + emptySpace + "&a sponges!"));
            player.getInventory().addItem(new ItemStack(Material.SPONGE, emptySpace));

            Bukkit.getScheduler().runTaskAsynchronously(CellLevel.inst, new Runnable() {
                @Override
                public void run() {
                    playerUtilities.setSponge(playerUtilities.getSponge() - emptySpace);
                    islandUtilities.setSponge(islandUtilities.getSponge() - emptySpace);
                }
            });
            return;
        }

        player.closeInventory();
        player.sendMessage(CellLevel.inst.getCfg().getPrefix() + Util.color("&aYou have withdrawn &6" + amount + "&a sponges!"));
        player.getInventory().addItem(new ItemStack(Material.SPONGE, amount));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Bukkit.getScheduler().runTaskAsynchronously(CellLevel.inst, new Runnable() {
            @Override
            public void run() {
                playerUtilities.setSponge(playerUtilities.getSponge() - amount);
                islandUtilities.setSponge(islandUtilities.getSponge() - amount);
            }
        });
    }

    private ItemStack getMaxItem() {
        ItemStack item = new ItemStack(Material.SPONGE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Util.color("&6&lMAX WITHDRAWAL"));
        meta.setLore(Arrays.asList(" ",
                Util.color("&o&fClick here to withdraw"),
                Util.color("&o&fthe maximum amount of sponges"),
                Util.color("&o&fyour inventory can hold!")));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 999);
        return item;
    }



}
