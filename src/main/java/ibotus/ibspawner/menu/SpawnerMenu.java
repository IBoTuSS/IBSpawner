package ibotus.ibspawner.menu;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ibotus.ibspawner.configurations.Menu;

public class SpawnerMenu implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        boolean menuEnabled = Menu.getMenu().getBoolean("menu_enabled");
        if (!menuEnabled) {
            return;
        }
        Player player = event.getPlayer();
        Action action = event.getAction();
        Block block = event.getClickedBlock();
        ItemStack itemInHand = player.getItemInHand();

        if (block != null && block.getType() == Material.SPAWNER && action == Action.RIGHT_CLICK_BLOCK && player.isSneaking() && itemInHand.getType() == Material.AIR) {
            Inventory inv = InventoryCreator.createInventory(player);
            player.openInventory(inv);
        }
    }
}
