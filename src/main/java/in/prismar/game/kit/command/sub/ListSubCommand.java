package in.prismar.game.kit.command.sub;

import com.google.common.base.Joiner;
import in.prismar.api.PrismarinConstants;
import in.prismar.game.kit.KitService;
import in.prismar.game.kit.model.Kit;
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
public class ListSubCommand extends HelpSubCommand<Player> {

    private final KitService service;

    public ListSubCommand(KitService service) {
        super("list");
        this.service = service;
        setUsage("<id>");
        setDescription("List all kits");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.sendMessage(PrismarinConstants.PREFIX + "§7Kits§8: §a" + Joiner.on("§8, §a").join(service.getRepository().findAll() ));
        return true;
    }
}
