package net.casheh.celllevel.events;

import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandKickEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandQuitEvent;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.google.common.collect.Table;
import net.casheh.celllevel.CellLevel;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.stream.Collectors;

public class IslandEvents implements Listener {

    private CellLevel plugin;

    public IslandEvents(CellLevel plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeave(IslandQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    String query = "DELETE FROM players WHERE uuid=?";
                    PreparedStatement statement = plugin.getDatabase().prepare(query);
                    statement.setString(1, uuid.toString());
                    statement.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @EventHandler
    public void onDisband(IslandDisbandEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    String query = "DELETE FROM players WHERE uuid=?";
                    PreparedStatement statement = null;
                    for (SuperiorPlayer player : e.getIsland().getIslandMembers(true)) {
                        statement = plugin.getDatabase().prepare(query);
                        statement.setString(1, player.getUniqueId().toString());
                        statement.executeUpdate();
                    }

                    query = "DELETE FROM virtual WHERE islandId=?";
                    statement = plugin.getDatabase().prepare(query);
                    statement.setString(1, e.getIsland().getUniqueId().toString());
                    statement.executeUpdate();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @EventHandler
    public void onKick(IslandKickEvent e) {
        UUID uuid = e.getTarget().getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    String query = "DELETE FROM players WHERE uuid=?";
                    PreparedStatement statement = plugin.getDatabase().prepare(query);
                    statement.setString(1, uuid.toString());
                    statement.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

}
