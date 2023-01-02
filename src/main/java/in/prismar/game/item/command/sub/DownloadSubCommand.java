package in.prismar.game.item.command.sub;

import com.google.common.base.Joiner;
import in.prismar.api.PrismarinConstants;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.impl.gun.type.AmmoType;
import in.prismar.game.item.reader.CustomItemReader;
import in.prismar.game.item.reader.CustomItemReaderType;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class DownloadSubCommand extends HelpSubCommand<Player> {

    private final CustomItemRegistry registry;

    public DownloadSubCommand(CustomItemRegistry registry) {
        super("download");
        setDescription("Download an item from the internet");
        setAliases("d");
        setUsage("<type> <id> <url>");
        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 4) {
            CustomItemReaderType type = arguments.getEnumType(CustomItemReaderType.values(), 1);
            final String id = arguments.getString(2).toLowerCase();
            final String url = arguments.getString(3);
            final String path = registry.getGame().getDefaultDirectory() + "items" + File.separator + type.getPath() +
                    File.separator;
            File directory = new File(path);
            directory.mkdirs();

            try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(path + id + ".toml")) {
                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            } catch (IOException e) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThere was an error while trying to download this file.");
                return true;
            }
            registry.load();
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have successfully downloaded §b" + id + ".toml");
            return true;
        }
        return false;
    }
}
