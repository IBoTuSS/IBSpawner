package ibotus.ibspawner.configurations;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Menu {
    private static FileConfiguration menu;
    private static File file;

    public Menu() {
    }

    public static void loadYaml(Plugin plugin) {
        file = new File(plugin.getDataFolder(), "menu.yml");
        if (!file.exists()) {
            plugin.saveResource("menu.yml", true);
        }

        menu = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration getMenu() {
        return menu;
    }
}
