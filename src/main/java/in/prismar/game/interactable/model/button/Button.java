package in.prismar.game.interactable.model.button;

import in.prismar.game.animation.model.Animation;
import in.prismar.game.interactable.InteractableService;
import in.prismar.game.interactable.model.Interactable;
import in.prismar.game.interactable.model.keycode.KeyCodeFrame;
import in.prismar.library.spigot.scheduler.Scheduler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class Button extends Interactable {

    private String door;
    private int doorOpenSeconds;
    private boolean disableWarningSounds;


    @Override
    public void onInteract(InteractableService service, Player interacter) {
        if(door == null) {
            return;
        }
        if(doorOpenSeconds == 0) {
            return;
        }
        Animation animation = service.getGame().getAnimationFacade().getRepository().findById(door);
        if(animation != null) {
            if(animation.isTempDataBoolean("doorOpen")) {
                return;
            }
            getLocation().getWorld().playSound(getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 0.7f, 1f);
            service.getGame().getAnimationFacade().getService().play(animation, false);
            Location location = animation.getCuboid().getCenter();
            if(!disableWarningSounds) {
                location.getWorld().playSound(location, "door.warning", SoundCategory.BLOCKS, 0.6f, 1f);
            }
            Scheduler.runDelayed(((long) animation.getTicks() * animation.getFrames().size())+5, () -> {
                location.getWorld().playSound(location, "door.open", SoundCategory.BLOCKS, 0.75f, 1f);
                Scheduler.runDelayed(20L * doorOpenSeconds, () -> {
                    service.getGame().getAnimationFacade().getService().play(animation, true);
                    Scheduler.runDelayed(((long) animation.getTicks() * animation.getFrames().size())+5, () -> {
                        location.getWorld().playSound(location, "door.open", SoundCategory.BLOCKS, 0.75f, 1f);
                        animation.getTempData().put("doorOpen", false);
                    });
                });
            });

        }
    }
}
