package in.prismar.game.item.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ReloadSubCommand extends HelpSubCommand<Player> {

    private final CustomItemRegistry registry;

    public ReloadSubCommand(CustomItemRegistry registry) {
        super("reload");
        setDescription("Reload all items");
        setAliases("rl");
        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments spigotArguments) throws CommandException {
        registry.load();
        player.sendMessage(PrismarinConstants.PREFIX + "§7Successfully loaded §a" + registry.getItems().size() + " §7items");
        return true;
    }


}
