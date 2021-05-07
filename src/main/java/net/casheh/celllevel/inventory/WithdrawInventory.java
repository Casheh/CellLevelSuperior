package net.casheh.celllevel.inventory;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import net.casheh.celllevel.util.ItemBuilder;
import net.casheh.celllevel.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WithdrawInventory implements InventoryProvider {

    public static final SmartInventory inv = SmartInventory.builder()
            .id("withdrawTest")
            .provider(new WithdrawInventory())
            .size(3, 9)
            .title(Util.color("&bWithdraw"))
            .build();

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        inventoryContents.set(1, 1, ClickableItem.of(new ItemStack(Material.STAINED_GLASS_PANE, 64, (short) 14), e -> {
            int amount = Util.getContainingNumber(Util.strip(inventoryContents.get(SlotPos.of(1, 4)).get().getItem().getItemMeta().getLore().get(1)));
            if (amount > 64)
                amount -= 64;
            else
                amount = 1;
            setBeacon(inventoryContents, amount);
        }));
        inventoryContents.set(1, 2, ClickableItem.of(new ItemStack(Material.STAINED_GLASS_PANE, 10, (short) 14), e -> {
            int amount = Util.getContainingNumber(Util.strip(inventoryContents.get(SlotPos.of(1, 4)).get().getItem().getItemMeta().getLore().get(1)));
            if (amount > 10)
                amount -= 10;
            else
                amount = 1;
            setBeacon(inventoryContents, amount);
        }));
        inventoryContents.set(1, 3, ClickableItem.of(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14), e -> {
            int amount = Util.getContainingNumber(Util.strip(inventoryContents.get(SlotPos.of(1, 4)).get().getItem().getItemMeta().getLore().get(1)));
            if (amount > 1)
                setBeacon(inventoryContents, amount - 1);
        }));
        setBeacon(inventoryContents, 100);
        inventoryContents.set(1, 5, ClickableItem.of(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13), e -> {
            int amount = Util.getContainingNumber(Util.strip(inventoryContents.get(SlotPos.of(1, 4)).get().getItem().getItemMeta().getLore().get(1)));
            if (amount + 1 < Integer.MAX_VALUE)
                setBeacon(inventoryContents, amount + 1);
        }));
        inventoryContents.set(1, 6, ClickableItem.of(new ItemStack(Material.STAINED_GLASS_PANE, 10, (short) 13), e -> {
            int amount = Util.getContainingNumber(Util.strip(inventoryContents.get(SlotPos.of(1, 4)).get().getItem().getItemMeta().getLore().get(1)));
            if (amount + 10 < Integer.MAX_VALUE)
                setBeacon(inventoryContents, amount + 10);
        }));
        inventoryContents.set(1, 7, ClickableItem.of(new ItemStack(Material.STAINED_GLASS_PANE, 64, (short) 13), e -> {
            int amount = Util.getContainingNumber(Util.strip(inventoryContents.get(SlotPos.of(1, 4)).get().getItem().getItemMeta().getLore().get(1)));
            if (amount + 64 < Integer.MAX_VALUE)
                setBeacon(inventoryContents, amount + 64);
        }));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {}

    private void setBeacon(InventoryContents inventoryContents, int amount) {
        String lore;
        if (amount == 1)
            lore = "&oClick here to withdraw &b" + amount + "&f beacon!";
        else
            lore = "&oClick here to withdraw &b" + amount + "&f beacons!";
        inventoryContents.set(1, 4, ClickableItem.of(new ItemBuilder(Material.BEACON).displayname("&b&lWITHDRAW").lore(" ").lore(lore).build(), e -> {
            int quantity = Util.getContainingNumber(Util.strip(e.getCurrentItem().getItemMeta().getLore().get(1)));
            e.getWhoClicked().getInventory().addItem(new ItemStack(Material.BEACON, quantity));
        }));
    }

}
