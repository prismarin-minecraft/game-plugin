package in.prismar.game.map.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.map.GameMapFacade;
import in.prismar.game.map.repository.GameMapRepository;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.item.ItemUtil;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class CreateSubCommand extends HelpSubCommand<Player> {

    private final GameMapFacade facade;
    private final GameMapRepository repository;

    public CreateSubCommand(GameMapFacade facade) {
        super("create");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "map.admin");
        setDescription("Create a map");
        setUsage("<id>");
        this.facade = facade;
        this.repository = facade.getRepository();
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1);
            if(repository.existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis map already exists");
                return true;
            }
            if(!ItemUtil.hasItemInHandAndHasDisplayName(player, true)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cPlease hold an item in your hand");
                return true;
            }
            ItemStack item = player.getInventory().getItemInMainHand();
            repository.create(id, item);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have created the map §b" + id);
            return true;
        }
        return false;
    }
}
