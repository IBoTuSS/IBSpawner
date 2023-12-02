package ibotus.ibspawner.menu;

import ibotus.ibspawner.configurations.Config;
import ibotus.ibspawner.configurations.Lang;
import ibotus.ibspawner.configurations.Menu;
import ibotus.ibspawner.data.PlayerData;
import ibotus.ibspawner.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class InventoryClickListener implements Listener {

    private static final float SOUND_VOLUME = 1.0f;
    private static final float SOUND_PITCH = 1.0f;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String inventoryTitle = Menu.getMenu().getString("inventory_title");
        String changeSpawnerTitle = Menu.getMenu().getString("change_spawner_title");
        if (event.getView().getTitle().equals(inventoryTitle) || event.getView().getTitle().equals(changeSpawnerTitle)) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                String materialName = Menu.getMenu().getString("item_menu");
                Material expectedMaterial = Material.valueOf(materialName);
                if (event.getSlot() == 4 && clickedItem.getType() == expectedMaterial) {
                    event.setCancelled(true);
                    int experienceRequired = Config.getConfig().getInt("Experience_Required");
                    double chanceIncrease = Config.getConfig().getDouble("Chance_Increase");

                    int playerLevel = player.getLevel();
                    if (playerLevel >= experienceRequired) {
                        double dropChance = PlayerData.getChance(player);
                        dropChance += chanceIncrease;
                        PlayerData.setChance(player, dropChance);
                        player.setLevel(playerLevel - experienceRequired);
                        ItemMeta meta = clickedItem.getItemMeta();
                        String displayName = Menu.getMenu().getString("item_menu_name");
                        String coloredDisplayName = ChatUtils.color(displayName.replace("%chance%", String.valueOf(dropChance)));
                        meta.setDisplayName(coloredDisplayName);
                        clickedItem.setItemMeta(meta);
                        String soundIncrease = Config.getConfig().getString("sound.add_chance");
                        Sound sound = Sound.valueOf(soundIncrease);
                        player.playSound(player.getLocation(), sound, SOUND_VOLUME, SOUND_PITCH);
                    } else {
                        String soundError = Config.getConfig().getString("sound.add_error");
                        Sound sound = Sound.valueOf(soundError);
                        player.playSound(player.getLocation(), sound, SOUND_VOLUME, SOUND_PITCH);
                        String message = Lang.getLang().getString("messages.error_experience");
                        message = message.replace("%experience%", String.valueOf(experienceRequired));
                        ChatUtils.sendMsg(player, message);
                    }
                } else {
                    String materialOpenMenuEgg = Menu.getMenu().getString("item_menu_egg");
                    Material menuMaterialEgg = Material.valueOf(materialOpenMenuEgg);
                    if (event.getSlot() == 2 && clickedItem.getType() == menuMaterialEgg) {
                        Inventory secondInv = MobMenu.createMobInventory(player);
                        player.openInventory(secondInv);
                        String soundMob = Config.getConfig().getString("sound.open-menu-mob");
                        Sound sound = Sound.valueOf(soundMob);
                        player.playSound(player.getLocation(), sound, SOUND_VOLUME, SOUND_PITCH);
                    } else {
                        List<Map<?, ?>> eggs = Menu.getMenu().getMapList("eggs");

                        for (Map<?, ?> eggMap : eggs) {
                            String type = (String) eggMap.get("type");
                            int experienceRequired = (Integer) eggMap.get("experienceRequired");

                            Material material = Material.getMaterial(type);

                            if (clickedItem.getType() == material) {
                                Block block = player.getTargetBlock(null, 5);
                                if (block.getType() == Material.SPAWNER) {
                                    if (player.getLevel() >= experienceRequired) {
                                        player.setLevel(player.getLevel() - experienceRequired);
                                        CreatureSpawner spawner = (CreatureSpawner) block.getState();
                                        spawner.setSpawnedType(EntityType.valueOf(type.replace("_SPAWN_EGG", "")));
                                        spawner.update();
                                        player.closeInventory();
                                        String soundEggSuccess = Config.getConfig().getString("sound.success");
                                        Sound sound = Sound.valueOf(soundEggSuccess);
                                        player.playSound(player.getLocation(), sound, SOUND_VOLUME, SOUND_PITCH);
                                    } else {
                                        ChatUtils.sendMsg(player, "&cYou don't have enough experience to change spawner type. Required: " + experienceRequired + " levels.");
                                        String soundEggError = Config.getConfig().getString("sound.error_egg");
                                        Sound sound = Sound.valueOf(soundEggError);
                                        player.playSound(player.getLocation(), sound, SOUND_VOLUME, SOUND_PITCH);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
