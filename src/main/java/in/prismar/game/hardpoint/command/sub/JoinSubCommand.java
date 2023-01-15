package in.prismar.game.hardpoint.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.hardpoint.HardpointFacade;
import in.prismar.game.hardpoint.HardpointTeam;
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

    private static final int[] SLOTS = {11, 15};

    private final HardpointFacade facade;

    public JoinSubCommand(HardpointFacade facade) {
        super("join");
        this.facade = facade;
        setDescription("Join");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(facade.isCurrentlyPlaying(player)) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou are already playing");
            return true;
        }
        if(!facade.isOpen() && !player.hasPermission(PrismarinConstants.PERMISSION_PREFIX +"hardpoint.join.bypass")) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cHardpoint is closed!");
            return true;
        }
        Frame frame = new Frame("§6Hardpoint", 3);
        frame.fill();

        int index = 0;
        for (HardpointTeam team : HardpointTeam.values()) {
            frame.addButton(SLOTS[index], new ItemBuilder(team.getIcon()).addLore("§c")
                    .addLore(PrismarinConstants.ARROW_RIGHT+" §7In team§8: §a" + facade.getSession().getPlayers().get(team).size())
                            .addLore("§c")
                    .addLore("§7Click to join this team").build(), (ClickFrameButtonEvent) (player12, event) -> {
                player.closeInventory();
                if(!facade.hasEnoughSpace(team)) {
                    player.sendMessage(PrismarinConstants.PREFIX + "§cThis team is already full.");
                    return;
                }
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.6f, 1);
                facade.join(team, player);
            });
            index++;
        }


        frame.addButton(26 - 8, new ItemBuilder(Material.OAK_DOOR).setName("§cLeave").build(), (ClickFrameButtonEvent) (player1, event) -> {
            player1.performCommand("game");
        });
        frame.build();
        frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.5f);
        return true;
    }
}
