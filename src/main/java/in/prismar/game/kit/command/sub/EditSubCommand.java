package in.prismar.game.kit.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.kit.KitService;
import in.prismar.game.kit.model.Kit;
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

    private final KitService service;

    public EditSubCommand(KitService service) {
        super("edit");
        this.service = service;
        setUsage("<id>");
        setDescription("Edit a kit items");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1);
            if(!service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis kit does not exists");
                return true;
            }
            Kit kit = service.getRepository().findById(id);
            Frame frame = new Frame("§cEdit kit", 4);
            if(kit.getItems() != null) {
                int slot = 0;
                for(ItemStack stack : kit.getItems().getItem()) {
                    if(stack != null && stack.getType() != Material.AIR) {
                        frame.addButton(slot, new FrameButton(stack).enableClick());
                        slot++;
                    }
                }
            }
            frame.getProperties().setAllowClick(true);
            frame.getEventBus().subscribe(FrameCloseEvent.class, event -> {
                kit.setItems(new ItemsContainer(frame.getOutput().getContents()));
                service.getRepository().save(kit);
                player.sendMessage(PrismarinConstants.PREFIX + "§7You have updated the kit §a" + id);
            });
            frame.build();
            frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.5f);
            return true;
        }
        return false;
    }
}
