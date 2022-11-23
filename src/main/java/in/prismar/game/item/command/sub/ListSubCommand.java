package in.prismar.game.item.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.item.CustomItem;
import in.prismar.game.item.CustomItemRegistry;
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
public class ListSubCommand extends HelpSubCommand<Player> {

    private final CustomItemRegistry registry;

    public ListSubCommand(CustomItemRegistry registry) {
        super("list");
        setDescription("View all custom items");
        setAliases("l");
        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments spigotArguments) throws CommandException {
        player.sendMessage("§8╔═══════════════════════╗");
        player.sendMessage(" ");
        player.sendMessage(PrismarinConstants.ARROW_RIGHT + " §7Items");
        for (CustomItem item : registry.getItems().values()) {
            InteractiveTextBuilder builder = new InteractiveTextBuilder()
                    .addText("  " + PrismarinConstants.DOT + " §a" + item.getId(),
                            "/customitem get " + item.getId(), "§7Click me to get this item");
            player.spigot().sendMessage(builder.build());
        }
        player.sendMessage(" ");
        player.sendMessage("§8╚═══════════════════════╝");
        return true;
    }
}
