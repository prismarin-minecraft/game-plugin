package in.prismar.game.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
import in.prismar.game.ffa.FFAFacade;
import in.prismar.game.hardpoint.HardpointFacade;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.*;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class GameCommand extends SpigotCommand<Player> {

    private static final int[] FFA_SLOTS = {
            0, 1, 2, 3,
            9, 10, 11, 12,
            18, 19, 20, 21,
            27, 28, 29, 30,
            36, 37, 38, 39
    };

    private static final int[] HARDPOINT_SLOTS = {
            5, 6, 7, 8,
            14, 15, 16, 17,
            23, 24, 25, 26,
            32, 33, 34, 35,
            41, 42, 43, 44
    };
    private static final int[] LOADOUT_SLOTS = {
            45, 46, 47, 48
    };
    private static final int[] STATS_SLOTS = {
            50, 51, 52, 53
    };

    @Inject
    private FFAFacade mapFacade;

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
        Frame frame = new Frame("§f七七七七七七七七十", 6);

        for (int slot : FFA_SLOTS) {
            frame.addButton(slot, new ItemBuilder(Material.MAP).setCustomModelData(105).allFlags().setName("§e§lFFA")
                            .addLore("§C")
                            .addLore(PrismarinConstants.ARROW_RIGHT + " §7Current map§8: §b" + mapFacade.getRotator().getCurrentMap().getFancyName())
                            .addLore(PrismarinConstants.ARROW_RIGHT + " §7Currently playing§8: §b" + mapFacade.getRotator().getCurrentMap().getPlayers().size())
                            .addLore("§c")
                            .addLore("§7Click me to play")
                            .build()
                    , (ClickFrameButtonEvent) (player12, event) -> player12.performCommand("ffa join"));
        }

        for(int slot : LOADOUT_SLOTS) {
            frame.addButton(slot, new ItemBuilder(Material.MAP).setCustomModelData(105).allFlags().setName("§cLoadout").build(), (ClickFrameButtonEvent) (player1, event) -> {
                player.performCommand("loadout");
            });
        }



        for(int slot : HARDPOINT_SLOTS) {
            frame.addButton(slot, new ItemBuilder(Material.MAP).setCustomModelData(105).setName("§c ").allFlags().setName("§b§lHARDPOINT")
                            .addLore("§C")
                            .addLore(PrismarinConstants.ARROW_RIGHT + " §7Currently playing§8: §b" + hardpointFacade.getCurrentlyPlayingCount())
                            .addLore("§c")
                            .addLore("§7Click me to play")
                            .build()
                    , (ClickFrameButtonEvent) (player12, event) -> player12.performCommand("hardpoint join"));
        }

        for(int slot : STATS_SLOTS) {
            frame.addButton(slot, new ItemBuilder(Material.MAP).setCustomModelData(105).setName("§dStats").allFlags().build(), (ClickFrameButtonEvent) (player1, event) -> {
                player.performCommand("stats");
            });
        }

        frame.build();
        frame.openInventory(player, Sound.BLOCK_PISTON_CONTRACT, 0.5f);
        return true;
    }


}
