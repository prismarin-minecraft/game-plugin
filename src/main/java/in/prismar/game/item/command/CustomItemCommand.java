package in.prismar.game.item.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.command.sub.*;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.spigot.command.spigot.template.help.HelpCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class CustomItemCommand extends HelpCommand<Player> {

    @Inject
    private CustomItemRegistry registry;


    public CustomItemCommand() {
        super("customitem", "CustomItem");
        setBaseColor("§6");
        setSenders(Player.class);
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "customitem");
    }

    @SafeInitialize
    private void initialize() {
        addChild(new ListSubCommand(registry));
        addChild(new GetSubCommand(registry));
        addChild(new AmmoSubCommand(registry));
        addChild(new HolderInfoSubCommand(registry));
        addChild(new ReloadSubCommand(registry));
        addChild(new DownloadSubCommand(registry));

    }

    @Override
    public List<String> tab(Player sender, String alias, String[] args) {
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("get")) {
                return registry.getItems().keySet()
                        .stream()
                        .filter(s -> s.startsWith(args[1]))
                        .toList();
            }
        }
        return super.tab(sender, alias, args);
    }
}
