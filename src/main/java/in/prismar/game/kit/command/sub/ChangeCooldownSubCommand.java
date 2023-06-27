package in.prismar.game.kit.command.sub;

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
public class ChangeCooldownSubCommand extends HelpSubCommand<Player> {

    private final KitService service;

    public ChangeCooldownSubCommand(KitService service) {
        super("changeCooldown");
        this.service = service;
        setUsage("<id> <cooldownInSeconds>");
        setDescription("Change cooldown of kit");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            final String id = arguments.getString(1);
            if(!service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis kit does not exists");
                return true;
            }
            Kit kit = service.getRepository().findById(id);
            int cooldownInSeconds = arguments.getInteger(2);
            kit.setCooldownInSeconds(cooldownInSeconds);
            service.getRepository().save(kit);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You successfully changed the cooldown of kit §a" + id);
            return true;
        }
        return false;
    }
}