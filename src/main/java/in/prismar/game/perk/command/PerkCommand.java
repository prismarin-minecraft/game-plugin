package in.prismar.game.perk.command;

import in.prismar.game.perk.PerkService;
import in.prismar.game.perk.frame.PerkFrame;
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
public class PerkCommand extends PlayerCommand {

    @Inject
    private PerkService service;

    public PerkCommand() {
        super("perk");
        setAliases("perks");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        PerkFrame frame = new PerkFrame(service, player);
        frame.openInventory(player, Sound.BLOCK_CHEST_OPEN, 0.6f);
        return true;
    }
}
