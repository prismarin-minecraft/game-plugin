package in.prismar.game.item.command.sub;

import com.google.common.base.Joiner;
import in.prismar.api.PrismarinConstants;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.impl.gun.type.AmmoType;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class AmmoSubCommand extends HelpSubCommand<Player> {

    private final CustomItemRegistry registry;

    public AmmoSubCommand(CustomItemRegistry registry) {
        super("ammo");
        setDescription("Get an ammo type");
        setAliases("a");
        setUsage("<type> <amount>");
        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            AmmoType type = arguments.getEnumType(AmmoType.values(), 1);
            int amount = MathUtil.clamp(arguments.getInteger(2), 1, 64);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You received §a" + amount + "x " + type.name());
            ItemStack item = type.getItem().clone();
            item.setAmount(amount);
            player.getInventory().addItem(item);
            return true;
        }
        player.sendMessage(PrismarinConstants.PREFIX + "§7Ammo types§8: §3" + Joiner.on("§8, §3").join(AmmoType.values()));
        return true;
    }
}
