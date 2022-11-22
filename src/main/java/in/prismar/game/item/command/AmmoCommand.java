package in.prismar.game.item.command;

import com.google.common.base.Joiner;
import in.prismar.api.PrismarinConstants;
import in.prismar.game.item.CustomItem;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.gun.AmmoType;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class AmmoCommand extends SpigotCommand<Player> {

    @Inject
    private CustomItemRegistry registry;


    public AmmoCommand() {
        super("ammo");
        setSenders(Player.class);
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "ammo");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            AmmoType type = AmmoType.getAmmoType(arguments.getString(0));
            if(type == null) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cWrong type: " + Joiner.on(", ").join(AmmoType.values()));
                return true;
            }
            int amount = MathUtil.clamp(arguments.getInteger(1), 1, 64);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You received §a" + amount + "x " + type.name());
            ItemStack item = type.getItem().clone();
            item.setAmount(amount);
            player.getInventory().addItem(item);
            return true;
        }
        player.sendMessage(PrismarinConstants.PREFIX + "§cUsage: /ammo <type> <amount>");
        return true;
    }


}
