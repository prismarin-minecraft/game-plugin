package in.prismar.game.bundle.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.bundle.BundleFacade;
import in.prismar.game.bundle.model.Bundle;
import in.prismar.library.common.event.EventSubscriber;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.FrameButton;
import in.prismar.library.spigot.inventory.event.FrameCloseEvent;
import in.prismar.library.spigot.item.container.ItemsContainer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class EditSubCommand extends HelpSubCommand<Player> {

    private final BundleFacade facade;

    public EditSubCommand(BundleFacade facade) {
        super("edit");
        this.facade = facade;
        setDescription("Edit a bundle");
        setUsage("<id>");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "bundle.admin");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1);
            if(!facade.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis bundle does exists");
                return true;
            }
            Bundle bundle = facade.getRepository().findById(id);
            Frame frame = new Frame(bundle.getDisplayName(), 3);
            frame.getProperties().setAllowClick(true);

            if(bundle.getContainer() != null) {
                int slot = 0;
                for(ItemStack stack : bundle.getContainer().getItem()) {
                    if(stack != null) {
                        if(stack.getType() != Material.AIR) {
                            frame.addButton(slot, new FrameButton(stack).enableClick());
                            slot++;
                        }
                    }

                }

            }
            frame.getEventBus().subscribe(FrameCloseEvent.class, event -> {
                bundle.setContainer(new ItemsContainer(frame.getOutput().getContents()));
                facade.getRepository().save(bundle);
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1);
                player.sendMessage(PrismarinConstants.PREFIX + "§7You edited the bundle §b" + bundle.getId());
            });

            frame.build();
            frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.6f);
            return true;
        }
        return false;
    }
}
