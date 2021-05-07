package net.casheh.celllevel.managers;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.handlers.PlayersManager;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.PlayerRole;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import net.casheh.celllevel.CellLevel;
import net.casheh.celllevel.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class IslandUtilities {

    private final Island island;

    public IslandUtilities(Island island) {
        this.island = island;
    }

    public List<UUID> getAllMembers() {
        return this.island.getIslandMembers(true).stream().map(SuperiorPlayer::getUniqueId).collect(Collectors.toList());
    }

    public int getBeacons() {
        UUID islandId = this.island.getUniqueId();
        try {

            String query = "SELECT beacons FROM virtual WHERE islandId=?";
            PreparedStatement statement = CellLevel.inst.getDatabase().prepare(query);
            statement.setString(1, islandId.toString());
            ResultSet rs = statement.executeQuery();

            if (rs.next())
                return rs.getInt("beacons");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getSponge() {
        UUID islandId = this.island.getUniqueId();
        try {

            String query = "SELECT sponge FROM virtual WHERE islandId=?";
            PreparedStatement statement = CellLevel.inst.getDatabase().prepare(query);
            statement.setString(1, islandId.toString());
            ResultSet rs = statement.executeQuery();

            if (rs.next())
                return rs.getInt("sponge");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setBeacons(int amount) {
        Bukkit.getScheduler().runTaskAsynchronously(CellLevel.inst, new Runnable() {
            @Override
            public void run() {
                UUID islandId = island.getUniqueId();
                try {

                    String query = "SELECT * FROM virtual WHERE islandId=?";
                    PreparedStatement statement = CellLevel.inst.getDatabase().prepare(query);
                    statement.setString(1, islandId.toString());
                    ResultSet rs = statement.executeQuery();

                    if (rs.next()) {

                        query = "UPDATE virtual SET beacons=? WHERE islandId=?";
                        PreparedStatement update = CellLevel.inst.getDatabase().prepare(query);
                        update.setInt(1, amount);
                        update.setString(2, islandId.toString());
                        update.executeUpdate();

                    } else {

                        query = "INSERT INTO virtual (islandId, beacons, sponge) VALUES (?,?,?)";
                        PreparedStatement insert = CellLevel.inst.getDatabase().prepare(query);
                        insert.setString(1, islandId.toString());
                        insert.setInt(2, amount);
                        insert.setInt(3, 0);
                        insert.executeUpdate();

                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setSponge(int amount) {
       Bukkit.getScheduler().runTaskAsynchronously(CellLevel.inst, new Runnable() {
           @Override
           public void run() {
               UUID islandId = island.getUniqueId();
               try {

                   String query = "SELECT * FROM virtual WHERE islandId=?";
                   PreparedStatement statement = CellLevel.inst.getDatabase().prepare(query);
                   statement.setString(1, islandId.toString());
                   ResultSet rs = statement.executeQuery();

                   if (rs.next()) {

                       query = "UPDATE virtual SET sponge=? WHERE islandId=?";
                       PreparedStatement update = CellLevel.inst.getDatabase().prepare(query);
                       update.setInt(1, amount);
                       update.setString(2, islandId.toString());
                       update.executeUpdate();

                   } else {

                       query = "INSERT INTO virtual (islandId, beacons, sponge) VALUES (?,?,?)";
                       PreparedStatement insert = CellLevel.inst.getDatabase().prepare(query);
                       insert.setString(1, islandId.toString());
                       insert.setInt(2, 0);
                       insert.setInt(3, amount);
                       insert.executeUpdate();

                   }

               } catch (SQLException e) {
                   e.printStackTrace();
               }
           }
       });
    }

    public List<String> getLevelsList() {
        PlayersManager playersManager = SuperiorSkyblockAPI.getPlayers();
        PlayerRole leader = playersManager.getPlayerRoleFromId(3);
        PlayerRole admin = playersManager.getPlayerRoleFromId(2);
        PlayerRole moderator = playersManager.getPlayerRoleFromId(1);
        PlayerRole member = playersManager.getPlayerRoleFromId(0);
        Set<UUID> leaders = this.island.getIslandMembers(leader).stream().map(SuperiorPlayer::getUniqueId).collect(Collectors.toSet());
        Set<UUID> admins = this.island.getIslandMembers(admin).stream().map(SuperiorPlayer::getUniqueId).collect(Collectors.toSet());
        Set<UUID> moderators = this.island.getIslandMembers(moderator).stream().map(SuperiorPlayer::getUniqueId).collect(Collectors.toSet());
        Set<UUID> members = this.island.getIslandMembers(member).stream().map(SuperiorPlayer::getUniqueId).collect(Collectors.toSet());

        int totalBeacons = 0;
        int totalSponge = 0;

        List<String> lore = new ArrayList<>();
        lore.add(" ");

        if (leaders.size() > 0) {
            lore.add(Util.color("&8» &c&lLeader &8«"));
            for (UUID uuid : leaders) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                PlayerUtilities playerUtilities = new PlayerUtilities(uuid);
                int beacons = playerUtilities.getBeacons();
                int sponge = playerUtilities.getSponge();
                totalBeacons += beacons;
                totalSponge += sponge;
                lore.add(Util.color("&c&l" + player.getName() + "&r&8: &a&l" +
                        Util.addCommas((int) (beacons * 0.5) + sponge) + " &r&8(&b&lB: &f" + Util.addCommas(beacons) + " &8/ &e&lS: &r&f" + Util.addCommas(sponge) + "&r&8)"));
            }
        }

        if (admins.size() > 0) {
            lore.add(" ");
            lore.add(Util.color("&8» &e&lAdmins &8«"));
            for (UUID uuid : admins) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                PlayerUtilities playerUtilities = new PlayerUtilities(uuid);
                int beacons = playerUtilities.getBeacons();
                int sponge = playerUtilities.getSponge();
                totalBeacons += beacons;
                totalSponge += sponge;
                lore.add(Util.color("&e&l" + player.getName() + "&r&8: &a&l" +
                        Util.addCommas((int) (beacons * 0.5) + sponge) + " &r&8(&b&lB: &f" + Util.addCommas(beacons) + " &8/ &e&lS: &r&f" + Util.addCommas(sponge) + "&r&8)"));
            }
        }

        if (moderators.size() > 0) {
            lore.add(" ");
            lore.add(Util.color("&8» &e&lModerators &8«"));
            for (UUID uuid : moderators) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                PlayerUtilities playerUtilities = new PlayerUtilities(uuid);
                int beacons = playerUtilities.getBeacons();
                int sponge = playerUtilities.getSponge();
                totalBeacons += beacons;
                totalSponge += sponge;
                lore.add(Util.color("&e&l" + player.getName() + "&r&8: &a&l" +
                        Util.addCommas((int) (beacons * 0.5) + sponge) + " &r&8(&b&lB: &f" + Util.addCommas(beacons) + " &8/ &e&lS: &r&f" + Util.addCommas(sponge) + "&r&8)"));
            }
        }

        if (members.size() > 0) {
            lore.add(" ");
            lore.add(Util.color("&8» &d&lMembers &8«"));
            for (UUID uuid : members) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                PlayerUtilities playerUtilities = new PlayerUtilities(uuid);
                int beacons = playerUtilities.getBeacons();
                int sponge = playerUtilities.getSponge();
                totalBeacons += beacons;
                totalSponge += sponge;
                lore.add(Util.color("&d&l" + player.getName() + "&r&8: &a&l" +
                        Util.addCommas((int) (beacons * 0.5) + sponge) + " &r&8(&b&lB: &f" + Util.addCommas(beacons) + " &8/ &e&lS: &r&f" + Util.addCommas(sponge) + "&r&8)"));
            }
        }

        lore.add(" ");
        lore.add(Util.color("&b&lTotal Beacons&r&8: &b&l" + Util.addCommas(totalBeacons)));
        lore.add(Util.color("&e&lTotal Sponge&r&8: &e&l" + Util.addCommas(totalSponge)));
        lore.add(" ");
        lore.add(Util.color("&b&n&lTotal Cell Value:&r &b&l" + Util.addCommas(((int) (0.5 * totalBeacons) + totalSponge))));

        return lore;
    }
}

