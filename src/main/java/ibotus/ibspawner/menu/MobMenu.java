package ibotus.ibspawner.menu;

import ibotus.ibspawner.configurations.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class MobMenu {

    public static Inventory createMobInventory(Player player) {
        String changeSpawnerTitle = Menu.getMenu().getString("change_spawner_title");
        Inventory inv = Bukkit.createInventory(null, 54, changeSpawnerTitle);

        List<Map<?, ?>> eggs = Menu.getMenu().getMapList("eggs");
        for (Map<?, ?> eggMap : eggs) {
            String type = (String) eggMap.get("type");
            String name = (String) eggMap.get("name");
            List<String> lore = (List<String>) eggMap.get("lore");
            int slot = (Integer) eggMap.get("slot");

            Material material = Material.getMaterial(type);
            ItemStack egg = new ItemStack(material);
            ItemMeta meta = egg.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(lore);
            egg.setItemMeta(meta);

            inv.setItem(slot, egg);
        }

        String glassType = Menu.getMenu().getString("glassType");
        List<Integer> slots = Menu.getMenu().getIntegerList("slots");

        Material glassMaterial = Material.getMaterial(glassType);
        ItemStack glassPanel = new ItemStack(glassMaterial);
        ItemMeta glassMeta = glassPanel.getItemMeta();
        glassMeta.setDisplayName(" ");
        glassPanel.setItemMeta(glassMeta);

        for (int slot : slots) {
            inv.setItem(slot, glassPanel);
        }
        return inv;
    }
}
