package in.prismar.game.interactable.model.keylock;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.animation.model.Animation;
import in.prismar.game.interactable.InteractableService;
import in.prismar.game.interactable.event.InteractableKeyLockEvent;
import in.prismar.game.interactable.model.Interactable;
import in.prismar.library.spigot.item.ItemUtil;
import in.prismar.library.spigot.scheduler.Scheduler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class KeyLock extends Interactable {

    private String door;
    private int doorOpenSeconds;
    private boolean disableWarningSounds;
    private String key;
    private String dungeon;


    @Override
    public void onInteract(InteractableService service, Player interacter) {
        if(door == null) {
            return;
        }
        InteractableKeyLockEvent event = new InteractableKeyLockEvent(interacter, this, true);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()) {
            return;
        }

        Animation animation = service.getGame().getAnimationFacade().getRepository().findById(door);
        if(animation != null) {
            if(animation.isTempDataBoolean("doorOpen")) {
                return;
            }
            if(!ItemUtil.hasItemInHandAndHasDisplayName(interacter, true)) {
                interacter.sendMessage(PrismarinConstants.PREFIX + "§cYou need the right key for this door");
                interacter.playSound(interacter.getLocation(), Sound.ENTITY_CREEPER_DEATH, 0.5f, 1f);
                return;
            }
            final ItemStack stack = interacter.getInventory().getItemInMainHand();
            if(!stack.getItemMeta().getDisplayName().equals(key.replace("&", "§").replace("_", " "))) {
                return;
            }
            event = new InteractableKeyLockEvent(interacter, this, false);
            Bukkit.getPluginManager().callEvent(event);
            ItemUtil.takeItemFromHand(interacter.getPlayer(), true);

            getLocation().getWorld().playSound(getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 0.7f, 1f);
            service.getGame().getAnimationFacade().getService().play(animation, false);
            Location location = animation.getCuboid().getCenter();
            if(!disableWarningSounds) {
                location.getWorld().playSound(location, "door.warning", SoundCategory.BLOCKS, 0.6f, 1f);
            }
            Scheduler.runDelayed(((long) animation.getTicks() * animation.getFrames().size())+5, () -> {
                location.getWorld().playSound(location, "door.open", SoundCategory.BLOCKS, 0.75f, 1f);
            });
            if(doorOpenSeconds != 0) {
                Scheduler.runDelayed(20L * doorOpenSeconds, () -> {
                    service.getGame().getAnimationFacade().getService().play(animation, true);
                    Scheduler.runDelayed(((long) animation.getTicks() * animation.getFrames().size())+5, () -> {
                        location.getWorld().playSound(location, "door.open", SoundCategory.BLOCKS, 0.75f, 1f);
                        animation.getTempData().put("doorOpen", false);
                    });
                });
            }
            animation.getTempData().put("doorOpen", true);
        }
    }
}
