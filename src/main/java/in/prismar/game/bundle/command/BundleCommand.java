package in.prismar.game.bundle.command;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.bundle.BundleFacade;
import in.prismar.game.bundle.command.sub.*;
import in.prismar.game.bundle.frame.BundleFrame;
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
public class BundleCommand extends HelpCommand<Player> {

    @Inject
    private BundleFacade facade;

    public BundleCommand() {
        super("bundle", "Bundle");
        setAliases("bundles");
        setSenders(Player.class);
        setBaseColor("§a");
    }

    @SafeInitialize
    private void initialize() {
        addChild(new CreateSubCommand(facade));
        addChild(new DeleteSubCommand(facade));
        addChild(new ListSubCommand(facade));
        addChild(new EditSubCommand(facade));
        addChild(new SetBalanceSubCommand(facade));
        addChild(new ResetSubCommand(facade));
        addChild(new GetSubCommand(facade));
    }

    @Override
    public boolean raw(Player player, SpigotArguments arguments) {
        if(arguments.getLength() == 0) {
            UserProvider<User> provider = PrismarinApi.getProvider(UserProvider.class);
            BundleFrame frame = new BundleFrame(facade, provider.getUserByUUID(player.getUniqueId()), player);
            frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.6f);
            return false;
        }
        if(!player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "bundle.admin")) {
            player.sendMessage(PrismarinConstants.NO_PERMISSION_MESSAGE);
            return false;
        }
        return true;
    }
}
