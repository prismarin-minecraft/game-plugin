package in.prismar.game.item.impl.deployable;

import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.model.CustomItem;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.item.ItemUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public abstract class DeployableItem extends CustomItem {
    private final ItemStack frameItem;

    public DeployableItem(String id, Material material, String displayName, int customModelData) {
        super(id, material, displayName);
        setCustomModelData(customModelData);
        allFlags();
        this.frameItem = new ItemBuilder(material).setCustomModelData(customModelData).build();
    }

    @CustomItemEvent
    public void onCall(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if(event.getBlockFace() != BlockFace.UP) {
            return;
        }
        if(!event.getClickedBlock().getType().isSolid()) {
            return;
        }
        //TODO: Check if player is participating in clan war

        event.setCancelled(true);
        Block block = event.getClickedBlock().getLocation().clone().add(0, 1, 0).getBlock();

        for(Block barrier : iterateBlocks(block, player.getFacing())) {
            if(barrier.getType() != Material.AIR) {
                return;
            }
        }
        ItemUtil.takeItemFromHand(player, true);

        ItemFrame frame = block.getWorld().spawn(block.getLocation(), ItemFrame.class);
        frame.setVisible(false);
        frame.setItem(frameItem.clone());
        BlockFace facing = player.getFacing();
        if(facing == BlockFace.NORTH) {
            frame.setRotation(Rotation.FLIPPED);
        } else if(facing == BlockFace.EAST) {
            frame.setRotation(Rotation.COUNTER_CLOCKWISE);
        } else if(facing == BlockFace.WEST) {
            frame.setRotation(Rotation.CLOCKWISE);
        }
        frame.setFacingDirection(BlockFace.UP);
        frame.setFixed(true);

        List<Block> barriers = iterateBlocks(block, player.getFacing());
        for(Block barrier : barriers) {
            barrier.setType(Material.BARRIER);
        }
        DeployedItem deployedItem = new DeployedItem(frame, barriers);
        onDeploy(game, player, deployedItem);
    }

    public List<Block> iterateBlocks(Block block, BlockFace face) {
        List<Block> barrierBlocks = new ArrayList<>();
        return barrierBlocks;
    }

    public abstract void onDeploy(Game game, Player player, DeployedItem item);

    @AllArgsConstructor
    @Getter
    public class DeployedItem {

        public ItemFrame frame;
        public List<Block> barrierBlocks;

    }
}
