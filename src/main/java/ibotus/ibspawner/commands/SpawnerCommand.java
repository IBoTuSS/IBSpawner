package ibotus.ibspawner.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ibotus.ibspawner.configurations.Config;
import ibotus.ibspawner.configurations.Lang;
import ibotus.ibspawner.configurations.Menu;
import ibotus.ibspawner.utils.ChatUtils;

import java.util.ArrayList;
import java.util.List;
public class SpawnerCommand implements CommandExecutor, TabCompleter {
    private final JavaPlugin plugin;

    public SpawnerCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("ibspawner.reload") && args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                Config.loadYaml(plugin);
                Lang.loadYaml(plugin);
                Menu.loadYaml(plugin);
                String message = Lang.getLang().getString("messages.reload");
                ChatUtils.sendMsg(player, message);
                return true;
            } else {
                String message = Lang.getLang().getString("messages.fail_reload");
                ChatUtils.sendMsg(player, message);
            }
        }
        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("ibspawner.reload") && args.length == 1) {
                List<String> list = new ArrayList<>();
                list.add("reload");
                return list;
            }
        }
        return null;
    }
}
