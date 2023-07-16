package in.prismar.game.hardpoint.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.hardpoint.HardpointFacade;
import in.prismar.game.hardpoint.HardpointTeam;
import in.prismar.game.hardpoint.config.Hardpoint;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class JoinSubCommand extends HelpSubCommand<Player> {

    private static final int[] RED_SLOTS = {2, 3, 11, 12, 20, 21};
    private static final int[] BLUE_SLOTS = {6, 7, 15, 16, 24, 25};

    private final HardpointFacade facade;

    public JoinSubCommand(HardpointFacade facade) {
        super("join");
        this.facade = facade;
        setDescription("Join");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if (facade.isCurrentlyPlaying(player)) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou are already playing");
            return true;
        }
        if (!facade.isOpen() && !player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "hardpoint.join.bypass")) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cHardpoint is closed!");
            return true;
        }
        if(facade.getGame().getBattleRoyaleService().getRegistry().isInGame(player)) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou can't join while being in the battleroyale game or queue");
            return true;
        }
        Frame frame = new Frame("§f七七七七七七七七以", 3);


        for (int slot : RED_SLOTS) {
            addTeamButton(frame, slot, HardpointTeam.RED);
        }

        for (int slot : BLUE_SLOTS) {
            addTeamButton(frame, slot, HardpointTeam.BLUE);
        }


        frame.addButton(0, new ItemBuilder(Material.MAP).setCustomModelData(105).allFlags().setName("§cBack").build(), (ClickFrameButtonEvent) (player1, event) -> {
            player1.performCommand("game");
        });
        frame.build();
        frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.5f);
        return true;
    }

    private void addTeamButton(Frame frame, int slot, HardpointTeam team) {
        frame.addButton(slot, new ItemBuilder(Material.MAP).setCustomModelData(105).allFlags().setName(team.getFancyName()).addLore("§c")
                .addLore(PrismarinConstants.ARROW_RIGHT + " §7In team§8: §a" + facade.getSession().getPlayers().get(team).size())
                .addLore("§c")
                .addLore("§7Click to join this team").build(), (ClickFrameButtonEvent) (player, event) -> {
            player.closeInventory();
            if (!facade.hasEnoughSpace(team)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis team is already full.");
                return;
            }
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.6f, 1);
            facade.join(team, player);
        });
    }
}
