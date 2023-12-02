package ibotus.ibspawner.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ibotus.ibspawner.configurations.Config;
import ibotus.ibspawner.configurations.Menu;
import ibotus.ibspawner.data.PlayerData;
import ibotus.ibspawner.utils.ChatUtils;

public class InventoryCreator {

    private static final float SOUND_VOLUME = 1.0f;
    private static final float SOUND_PITCH = 1.0f;

    public static Inventory createInventory(Player player) {
        String inventoryTitle = Menu.getMenu().getString("inventory_title");
        Inventory inv = Bukkit.createInventory(null, 9, inventoryTitle);

        String materialName = Menu.getMenu().getString("item_menu");
        Material material = Material.getMaterial(materialName);

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        String materialEgg = Menu.getMenu().getString("item_menu_egg");
        Material menuMaterial = Material.valueOf(materialEgg);
        ItemStack secondItem = new ItemStack(menuMaterial);

        ItemMeta secondMeta = secondItem.getItemMeta();
        String itemName = Menu.getMenu().getString("item_menu_egg_name");
        secondMeta.setDisplayName(itemName);

        secondItem.setItemMeta(secondMeta);
        inv.setItem(2, secondItem);

        double dropChance = PlayerData.getChance(player);
        String displayName = Menu.getMenu().getString("item_menu_name");
        String coloredDisplayName = ChatUtils.color(displayName.replace("%chance%", String.valueOf(dropChance)));
        meta.setDisplayName(coloredDisplayName);

        item.setItemMeta(meta);

        inv.setItem(4, item);
        String soundOpen = Config.getConfig().getString("sound.open-menu");
        Sound soundMenuOpen = Sound.valueOf(soundOpen);
        player.playSound(player.getLocation(), soundMenuOpen, SOUND_VOLUME, SOUND_PITCH);
        return inv;
    }
}
