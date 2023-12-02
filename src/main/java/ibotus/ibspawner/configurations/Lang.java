package ibotus.ibspawner.configurations;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Lang {
    private static FileConfiguration lang;

    public static void loadYaml(Plugin plugin) {
        String langName = loadConfigLang(plugin);
        loadLanguageFile(plugin, langName);
        loadLanguageFile(plugin, "EN");
        loadLanguageFile(plugin, "UA");
        loadLanguageFile(plugin, "RU");
    }

    public static FileConfiguration getLang() {
        return lang;
    }

    private static String loadConfigLang(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        return config.getString("Lang", "EN");
    }

    private static void loadLanguageFile(Plugin plugin, String langName) {
        File langFile = new File(plugin.getDataFolder(), "lang_" + langName + ".yml");
        if (!langFile.exists()) {
            plugin.saveResource("lang_" + langName + ".yml", true);
        }
        if (langName.equals(loadConfigLang(plugin))) {
            lang = YamlConfiguration.loadConfiguration(langFile);
        }
    }
}