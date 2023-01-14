package in.prismar.game.command;

import in.prismar.api.PrismarinApi;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.player.PlayerCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HexFormat;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class ResourcepackCommand extends PlayerCommand {

    private final ConfigStore store;

    private final String url;
    private final byte[] hash;

    public ResourcepackCommand() {
        super("resourcepack");
        setAliases("rp");

        this.store = PrismarinApi.getProvider(ConfigStore.class);
        this.url = store.getProperty("resourcepack.url");
        this.hash = HexFormat.of().parseHex(store.getProperty("resourcepack.hash"));
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.setResourcePack(url, hash, false);
        return true;
    }
}
