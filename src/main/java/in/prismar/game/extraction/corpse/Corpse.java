package in.prismar.game.extraction.corpse;

import dev.sergiferry.playernpc.api.NPC;
import dev.sergiferry.playernpc.api.NPCLib;
import in.prismar.library.common.tuple.Tuple;
import in.prismar.library.spigot.nms.NmsUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class Corpse {

    private final Player player;
    private final Inventory inventory;
    private final long timeToLive;

    private final NPC.Global npc;

    public Corpse(Plugin plugin, Player player, long ttl) {
        this.player = player;
        this.timeToLive = ttl;
        this.inventory = Bukkit.createInventory(null, 9 * 5, player.getName());
        for(ItemStack stack : player.getInventory()) {
            if(stack != null) {
                if(stack.getType() != Material.AIR) {
                    inventory.addItem(stack);
                }
            }
        }

        this.npc = NPCLib.getInstance().generateGlobalNPC(plugin, UUID.randomUUID().toString(), player.getLocation());
        Tuple<String, String> profile = NmsUtil.getTextureAndSignature(player);
        this.npc.setSkin(profile.getFirst(), profile.getSecond());
        this.npc.addCustomClickAction(NPC.Interact.ClickType.EITHER, (npc, player1) -> {
            player1.openInventory(inventory);
            player1.playSound(player1.getLocation(), Sound.BLOCK_CHEST_OPEN, 0.5f, 1);
        });
        this.npc.setPose(NPC.Pose.GLIDING);
        this.npc.forceUpdate();

    }
}
