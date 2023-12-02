package ibotus.ibspawner.breakspawner;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import ibotus.ibspawner.configurations.Config;

import java.util.List;

public class PlaceAndPickup implements Listener {
    private final JavaPlugin plugin;

    public PlaceAndPickup(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack itemInHand = event.getItemInHand();

        if (block.getType() == Material.SPAWNER) {
            ItemMeta itemMeta = itemInHand.getItemMeta();
            List<String> lore = itemMeta.getLore();
            if (lore != null && !lore.isEmpty()) {
                String entityTypeString = ChatColor.stripColor(lore.get(0));
                EntityType entityType = EntityType.fromName(entityTypeString);

                CreatureSpawner spawner = (CreatureSpawner) block.getState();
                spawner.setSpawnedType(entityType);
                spawner.update();
                Particles.successfulPlacement(block);
                String soundPlace = Config.getConfig().getString("sound.place");
                if (soundPlace != null && !soundPlace.isEmpty()) {
                    Sound sound = Sound.valueOf(soundPlace);
                    player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
                }
            }
        }
    }




    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ItemStack item = event.getItem().getItemStack();

            if (item.getType() == Material.SPAWNER) {
                ItemMeta itemMeta = item.getItemMeta();
                List<String> lore = itemMeta.getLore();
                if (lore != null && !lore.isEmpty()) {
                    String entityTypeString = lore.get(0).replace("§7", "");
                    EntityType entityType = EntityType.valueOf(entityTypeString);
                }
            }
        }
    }
}
