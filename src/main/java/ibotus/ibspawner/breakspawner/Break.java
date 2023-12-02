package ibotus.ibspawner.breakspawner;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.GameMode;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Sound;
import ibotus.ibspawner.utils.ChatUtils;
import ibotus.ibspawner.configurations.Lang;
import ibotus.ibspawner.configurations.Config;
import ibotus.ibspawner.utils.HashMapUtils;
import ibotus.ibspawner.data.PlayerData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Break implements Listener {
    private final JavaPlugin plugin;

    private static final int DROP_CHANCE_LIMIT = 100;

    private static final float SOUND_VOLUME = 1.0f;

    private static final float SOUND_PITCH = 1.0f;

    public Break(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getItemInHand();
        List<String> Pickaxe = Config.getConfig().getStringList("Pickaxe");
        List<Material> materials = Pickaxe.stream()
                .map(Material::getMaterial)
                .collect(Collectors.toList());
        if (block.getType() == Material.SPAWNER && Pickaxe.contains(itemInHand.getType().name()) &&
                player.getGameMode() == GameMode.SURVIVAL) {
            if (!itemInHand.containsEnchantment(Enchantment.SILK_TOUCH)) {
                String message = Lang.getLang().getString("messages.fail_silk_touch");
                ChatUtils.sendActionBar(player, message);
                String soundBreak = Config.getConfig().getString("sound.fail_silk");
                Sound sound = Sound.valueOf(soundBreak);
                player.playSound(player.getLocation(), sound, SOUND_VOLUME, SOUND_PITCH);
                event.setDropItems(false);
                return;
            }
            if (block.getState() instanceof CreatureSpawner) {
                CreatureSpawner spawner = (CreatureSpawner) block.getState();
                ItemStack drop = new ItemStack(Material.SPAWNER);
                double dropChance = Config.getConfig().getDouble("DropChance");
                double random = Math.random() * DROP_CHANCE_LIMIT;
                UUID uuid = UUID.randomUUID();
                HashMapUtils.spawner.put(uuid, spawner.getSpawnedType().ordinal());
                ItemMeta itemMeta = drop.getItemMeta();
                List<String> lore = new ArrayList<>();
                lore.add(ChatUtils.color("&7" + spawner.getSpawnedType().name()));
                itemMeta.setLore(lore);
                drop.setItemMeta(itemMeta);
                if (random <= dropChance) {
                    block.getWorld().dropItemNaturally(block.getLocation(), drop);
                    String soundBreak = Config.getConfig().getString("sound.break");
                    Sound sound = Sound.valueOf(soundBreak);
                    player.playSound(player.getLocation(), sound, SOUND_VOLUME, SOUND_PITCH);
                    Particles.successfulBreak(block);
                    double baseDropChance = Config.getConfig().getDouble("DropChance");
                    PlayerData.setChance(player, baseDropChance);
                } else {
                    String message = Lang.getLang().getString("messages.spawner_not_dropped");
                    ChatUtils.sendActionBar(player, message);
                    String soundFail = Config.getConfig().getString("sound.fail_break");
                    Sound sound = Sound.valueOf(soundFail);
                    player.playSound(player.getLocation(), sound, SOUND_VOLUME, SOUND_PITCH);
                    Particles.unsuccessfulBreak(block);
                }
            }
            if (Config.getConfig().getBoolean("DisableSpawnerExp") && itemInHand.containsEnchantment(Enchantment.SILK_TOUCH)) {
                event.setExpToDrop(0);
            }
        }
    }
}