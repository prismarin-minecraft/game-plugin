package in.prismar.game.kit.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.kit.KitService;
import in.prismar.game.kit.command.sub.*;
import in.prismar.game.kit.frame.KitFrame;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class KitCommand extends HelpCommand<Player> {

    @Inject
    private KitService service;

    public KitCommand() {
        super("kit", "Kit");
        setSenders(Player.class);

    }

    @SafeInitialize
    private void initialize() {
        addChild(new CreateSubCommand(service));
        addChild(new DeleteSubCommand(service));
        addChild(new ListSubCommand(service));
        addChild(new EditSubCommand(service));
        addChild(new ChangeCooldownSubCommand(service));
    }

    @Override
    public boolean raw(Player player, SpigotArguments arguments) {
        if(!player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "kit.admin")) {
            return false;
        }
        if(arguments.getLength() == 0) {
            KitFrame frame = new KitFrame(service, player);
            frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.5f);
            return false;
        }
        return true;
    }
}
