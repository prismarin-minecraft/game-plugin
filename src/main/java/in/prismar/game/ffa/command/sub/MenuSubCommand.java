package in.prismar.game.ffa.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.ffa.GameMapFacade;
import in.prismar.game.ffa.repository.GameMapRepository;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.item.ItemBuilder;
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
public class MenuSubCommand extends HelpSubCommand<Player> {

    private final GameMapFacade facade;
    private final GameMapRepository repository;

    public MenuSubCommand(GameMapFacade facade) {
        super("menu");
        setDescription("Main menu of ffa");
        this.facade = facade;
        this.repository = facade.getRepository();
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if (facade.getRotator().getCurrentMap() == null) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cNo map is currently open");
            return true;
        }
        if (facade.getRotator().getCurrentMap().getPlayers().containsKey(player.getUniqueId())) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou are already playing");
            return true;
        }

        Frame frame = new Frame("§bFFA", 3);
        frame.fill();

        frame.addButton(10, new ItemBuilder(Material.WOODEN_PICKAXE).setName("§6Loadout").allFlags()
                .setCustomModelData(2).addLore("§c").addLore("§7Click me to open your loadout").build(), (ClickFrameButtonEvent) (player1, event) -> {
                    player.performCommand("loadout");
                });

        frame.addButton(13, new ItemBuilder(Material.LIME_WOOL).glow().setName("§a§lPLAY")
                        .addLore("§C")
                        .addLore(PrismarinConstants.ARROW_RIGHT + " §7Current map§8: §b" + facade.getRotator().getCurrentMap().getFancyName())
                        .addLore(PrismarinConstants.ARROW_RIGHT + " §7Currently playing§8: §b" + facade.getRotator().getCurrentMap().getPlayers().size())
                        .addLore("§c")
                        .addLore("§7Click me to play")
                        .build()
                , (ClickFrameButtonEvent) (player12, event) -> player12.performCommand("ffa join"));

        frame.addButton(16, new ItemBuilder(Material.PAPER).setName("§cStats")
                .addLore("§C").addLore("§7Click me to view your statistics").build(), (ClickFrameButtonEvent) (player1, event) -> {
            player.performCommand("stats");
        });

        frame.build();

        frame.openInventory(player, Sound.BLOCK_PISTON_CONTRACT, 0.5f);

        return true;
    }
}
