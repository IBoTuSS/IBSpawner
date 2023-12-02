package ibotus.ibspawner.breakspawner;

import org.bukkit.Particle;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import ibotus.ibspawner.configurations.Config;

public class Particles {

    private static void spawnParticle(Block block, String particleTypeKey, String amountKey) {
        String particleName = Config.getConfig().getString("Particle." + particleTypeKey + ".Type");
        Particle particle = Particle.valueOf(particleName);
        int particleAmount = Config.getConfig().getInt("Particle." + amountKey + ".Amount");

        Location location = block.getLocation().add(0.5, 0.5, 0.5);
        World world = block.getWorld();
        world.spawnParticle(particle, location, particleAmount);
    }

    public static void successfulBreak(Block block) {
        spawnParticle(block, "Success", "Success");
    }

    public static void unsuccessfulBreak(Block block) {
        spawnParticle(block, "Fail", "Fail");
    }

    public static void successfulPlacement(Block block) {
        spawnParticle(block, "PlacementSuccess", "PlacementSuccess");
    }
}

