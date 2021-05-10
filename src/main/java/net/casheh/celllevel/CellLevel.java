package net.casheh.celllevel;

import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.SmartInvsPlugin;
import net.casheh.celllevel.commands.Deposit;
import net.casheh.celllevel.commands.Levels;
import net.casheh.celllevel.config.Config;
import net.casheh.celllevel.db.Database;
import net.casheh.celllevel.db.SQLite;
import net.casheh.celllevel.events.ChestClickListener;
import net.casheh.celllevel.events.InventoryListeners;
import net.casheh.celllevel.events.IslandEvents;
import net.casheh.celllevel.inventory.CellTopMenu;
import net.casheh.celllevel.util.DependencyUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class CellLevel extends JavaPlugin {

    public static CellLevel inst;

    private Config config;

    private Database database;

    private BukkitTask updateTask;

    private CellTopMenu cellTop;

    private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);

        inst = this;

        connectDb();

        config = new Config(this);

        updateTask = startMenuUpdateTask();

        new DependencyUtils(this).loadDepends();

        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        if (database != null && database.checkConnection()) {
            getServer().getPluginManager().registerEvents(new ChestClickListener(), this);
            getServer().getPluginManager().registerEvents(new InventoryListeners(), this);
            getServer().getPluginManager().registerEvents(new IslandEvents(this), this);
            getCommand("levels").setExecutor(new Levels(this));
            getCommand("deposit").setExecutor(new Deposit());
        }

    }

    @Override
    public void onDisable() {
        if (database.checkConnection())
            database.close();
        updateTask.cancel();
    }

    private void connectDb() {
        try {
            database = new SQLite("virtual");
            database.open();
            database.createTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BukkitTask startMenuUpdateTask() {
        cellTop = new CellTopMenu(this);
        return Bukkit.getScheduler().runTaskTimerAsynchronously(this, cellTop::updateTopList, 20L, config.getUpdateInterval()* 20L);
    }

    public Database getDatabase() {
        return this.database;
    }

    public Config getCfg() {
        return this.config;
    }

    public CellTopMenu getCellTop() {
        return this.cellTop;
    }

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }
}
