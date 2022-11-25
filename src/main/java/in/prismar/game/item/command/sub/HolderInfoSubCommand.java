package in.prismar.game.item.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.item.CustomItem;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.text.InteractiveTextBuilder;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class HolderInfoSubCommand extends HelpSubCommand<Player> {

    private final CustomItemRegistry registry;

    public HolderInfoSubCommand(CustomItemRegistry registry) {
        super("holderinfo");
        setDescription("View info about your current inventory");
        setAliases("hi");
        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments spigotArguments) throws CommandException {
        player.sendMessage("§8╔═══════════════════════╗");
        player.sendMessage(" ");
        player.sendMessage(PrismarinConstants.ARROW_RIGHT + " §7Items");
        for (CustomItemHolder holder : registry.getHolders().get(player.getUniqueId())) {
            player.sendMessage(PrismarinConstants.DOT + " §e" + holder.getItem().getId() + " §8| §7Type§8: §3" + holder.getHoldingType());
        }
        player.sendMessage(" ");
        player.sendMessage("§8╚═══════════════════════╝");
        return true;
    }
}
