package ibotus.ibspawner.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;
import ibotus.ibspawner.Main;
import ibotus.ibspawner.configurations.Config;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;

public class PlayerData {
    private static final String FILE_NAME = "playerData.json";
    private static HashMap<UUID, Double> playerChances = new HashMap<>();

    public static void load() {
        Gson gson = new Gson();
        try {
            File dataFolder = Main.getPlugin(Main.class).getDataFolder();
            File playerDataFile = new File(dataFolder, FILE_NAME);
            if (!playerDataFile.exists()) {
                throw new FileNotFoundException("File not found: " + FILE_NAME);
            }
            FileReader reader = new FileReader(playerDataFile);
            Type type = new TypeToken<HashMap<UUID, Double>>(){}.getType();
            playerChances = gson.fromJson(reader, type);
            if (playerChances == null) {
                playerChances = new HashMap<>();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static void save() {
        Gson gson = new Gson();
        try {
            File dataFolder = Main.getPlugin(Main.class).getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }
            File playerDataFile = new File(dataFolder, FILE_NAME);
            FileWriter writer = new FileWriter(playerDataFile);
            gson.toJson(playerChances, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static double getChance(Player player) {
        return playerChances.getOrDefault(player.getUniqueId(), Config.getConfig().getDouble("DropChance"));
    }

    public static void setChance(Player player, double chance) {
        playerChances.put(player.getUniqueId(), chance);
    }
}
