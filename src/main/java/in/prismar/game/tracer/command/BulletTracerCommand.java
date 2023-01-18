package in.prismar.game.tracer.command;

import in.prismar.api.user.User;
import in.prismar.game.tracer.BulletTracerRegistry;
import in.prismar.game.tracer.frame.BulletTraceFrame;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.player.PlayerCommand;
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
public class BulletTracerCommand extends PlayerCommand {

    @Inject
    private BulletTracerRegistry registry;

    public BulletTracerCommand() {
        super("bullettracer");
        setAliases("tracers", "bullettracers");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        User user = registry.getUserProvider().getUserByUUID(player.getUniqueId());
        BulletTraceFrame frame = new BulletTraceFrame(registry, player, user);
        frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.6f);
        return true;
    }
}
