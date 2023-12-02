package ibotus.ibspawner;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import ibotus.ibspawner.configurations.Menu;
import ibotus.ibspawner.data.PlayerData;
import ibotus.ibspawner.menu.InventoryClickListener;
import ibotus.ibspawner.utils.ChatUtils;
import ibotus.ibspawner.breakspawner.Break;
import ibotus.ibspawner.configurations.Config;
import ibotus.ibspawner.configurations.Lang;
import ibotus.ibspawner.commands.SpawnerCommand;
import ibotus.ibspawner.breakspawner.PlaceAndPickup;
import ibotus.ibspawner.menu.SpawnerMenu;

import java.io.File;

public final class Main extends JavaPlugin {

    private void msg(String msg) {
        String prefix = ChatUtils.color("&aIBSpawner &7| ");
        Bukkit.getConsoleSender().sendMessage(ChatUtils.color(prefix + msg));
    }

    public void onEnable() {
        File dataFolder = this.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        File playerDataFile = new File(dataFolder, "playerData.json");
        if (!playerDataFile.exists()) {
            saveResource("playerData.json", false);
        }
        PlayerData.load();
        Config.loadYaml(this);
        Lang.loadYaml(this);
        Menu.loadYaml(this);
        Bukkit.getConsoleSender().sendMessage("");
        this.msg("&fDeveloper: &at.me/IBoTuS");
        this.msg("&fVersion: &dv" + this.getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("");
        this.getCommand("IBSpawner").setExecutor(new SpawnerCommand(this));
        this.getCommand("IBSpawner").setTabCompleter(new SpawnerCommand(this));
        Bukkit.getPluginManager().registerEvents(new Break(this), this);
        Bukkit.getPluginManager().registerEvents(new PlaceAndPickup(this), this);
        Bukkit.getPluginManager().registerEvents(new SpawnerMenu(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().addPermission(new Permission("ibspawner.reload"));
    }

    public void onDisable() {
        PlayerData.save();
        Bukkit.getConsoleSender().sendMessage("");
        this.msg("&fDisable plugin.");
        Bukkit.getConsoleSender().sendMessage("");
    }
}
