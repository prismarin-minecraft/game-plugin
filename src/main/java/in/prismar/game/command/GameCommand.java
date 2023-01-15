package in.prismar.game.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
import in.prismar.game.ffa.GameMapFacade;
import in.prismar.game.hardpoint.HardpointFacade;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.entity.GlowingEntities;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.location.LocationUtil;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import in.prismar.library.spigot.scheduler.Scheduler;
import in.prismar.library.spigot.scheduler.SchedulerRunnable;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class GameCommand extends SpigotCommand<Player> {

    @Inject
    private GameMapFacade mapFacade;

    @Inject
    private HardpointFacade hardpointFacade;


    public GameCommand(Game game) {
        super("game");
        setSenders(Player.class);
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if (mapFacade.getRotator().getCurrentMap().getPlayers().containsKey(player.getUniqueId())) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou are already playing");
            return true;
        }
        Frame frame = new Frame("§bFFA", 3);
        frame.fill();

        frame.addButton(10, new ItemBuilder(Material.WOODEN_PICKAXE).setName("§6Loadout").allFlags()
                .setCustomModelData(2).addLore("§c").addLore("§7Click me to open your loadout").build(), (ClickFrameButtonEvent) (player1, event) -> {
            player.performCommand("loadout");
        });

        frame.addButton(12, new ItemBuilder(mapFacade.getRotator().getCurrentMap().getIcon().getItem().getType()).glow().setName("§a§lFFA")
                        .addLore("§C")
                        .addLore(PrismarinConstants.ARROW_RIGHT + " §7Current map§8: §b" + mapFacade.getRotator().getCurrentMap().getFancyName())
                        .addLore(PrismarinConstants.ARROW_RIGHT + " §7Currently playing§8: §b" + mapFacade.getRotator().getCurrentMap().getPlayers().size())
                        .addLore("§c")
                        .addLore("§7Click me to play")
                        .build()
                , (ClickFrameButtonEvent) (player12, event) -> player12.performCommand("ffa join"));

        frame.addButton(14, new ItemBuilder(Material.WHITE_WOOL).glow().setName("§6§lHARDPOINT")
                        .addLore("§C")
                        .addLore(PrismarinConstants.ARROW_RIGHT + " §7Currently playing§8: §b" + hardpointFacade.getCurrentlyPlayingCount())
                        .addLore("§c")
                        .addLore("§7Click me to play")
                        .build()
                , (ClickFrameButtonEvent) (player12, event) -> player12.performCommand("hardpoint join"));

        frame.addButton(16, new ItemBuilder(Material.PAPER).setName("§cStats")
                .addLore("§C").addLore("§7Click me to view your statistics").build(), (ClickFrameButtonEvent) (player1, event) -> {
            player.performCommand("stats");
        });
        frame.build();
        frame.openInventory(player, Sound.BLOCK_PISTON_CONTRACT, 0.5f);
        return true;
    }


}
