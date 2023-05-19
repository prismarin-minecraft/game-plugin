package in.prismar.game.item.impl.deployable;

import in.prismar.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class SandbagItem extends DeployableItem{
    public SandbagItem() {
        super("Sandbag", Material.LEAD, "§6Sandbag", 1);
    }

    @Override
    public void onDeploy(Game game, Player player, DeployedItem item) {
        Bukkit.getScheduler().runTaskLater(game, () -> {
            item.getFrame().remove();
            for(Block block : item.getBarrierBlocks()) {
                block.setType(Material.AIR);
            }
            item.getFrame().getWorld().playSound(item.getFrame().getLocation(), Sound.BLOCK_SAND_BREAK, 1, 0);
            item.getFrame().getWorld().spawnParticle(Particle.SMOKE_LARGE, item.getFrame().getLocation().clone().add(0, 0.5, 0), 1);
            }, 20 * 30);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_SAND_STEP, 1f, 0);
    }

    @Override
    public List<Block> iterateBlocks(Block block, BlockFace face) {
        List<Block> barrierBlocks = new ArrayList<>();

        barrierBlocks.add(block.getLocation().clone().add(face == BlockFace.NORTH || face == BlockFace.SOUTH ? 2 : 0, 0,
                face == BlockFace.EAST || face == BlockFace.WEST ? 2 : 0).getBlock());
        barrierBlocks.add(block.getLocation().clone().add(face == BlockFace.NORTH || face == BlockFace.SOUTH ? -2 : 0, 0,
                face == BlockFace.EAST || face == BlockFace.WEST ? -2 : 0).getBlock());

        for (int i = -1; i <= 1; i++) {
            Block barrierBlock = block.getLocation().clone().add(face == BlockFace.NORTH || face == BlockFace.SOUTH ? i : 0, 0,
                    face == BlockFace.EAST || face == BlockFace.WEST ? i : 0).getBlock();
            Block upperBarrierBlock = barrierBlock.getLocation().clone().add(0, 1, 0).getBlock();

            barrierBlocks.add(barrierBlock);
            barrierBlocks.add(upperBarrierBlock);
        }
        return barrierBlocks;
    }
}
