package in.prismar.game.interactable.model.keycode;

import in.prismar.game.animation.model.Animation;
import in.prismar.game.interactable.InteractableService;
import in.prismar.game.interactable.gui.LayoutInitialization;
import in.prismar.game.interactable.model.Interactable;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.spigot.scheduler.Scheduler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
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
public class Keycode extends Interactable {

    private String door;
    private int doorOpenSeconds;
    private Set<String> codes;
    private boolean reset;


    @Override
    public void onInteract(InteractableService service, Player interacter) {
        if(door == null) {
            return;
        }
        KeyCodeFrame frame = new KeyCodeFrame((player, code) -> {

            Animation animation = service.getGame().getAnimationFacade().getRepository().findById(door);
            if (animation != null) {
                if (codes.contains(code) && !animation.isTempDataBoolean("doorOpen")) {
                    if(isReset()) {
                        codes.remove(code);
                        String newCode = String.valueOf(MathUtil.random(10000, 99999));
                        codes.add(newCode);
                        LayoutInitialization.changeCode(getId(), newCode);
                        service.getRepository().save(this);
                    }
                    player.closeInventory();
                    service.getGame().getAnimationFacade().getService().play(animation, false);
                    Location location = animation.getCuboid().getCenter();
                    location.getWorld().playSound(location, "door.warning", SoundCategory.BLOCKS, 0.6f, 1f);
                    Scheduler.runDelayed((long) animation.getTicks() * animation.getFrames().size(), () -> {
                        location.getWorld().playSound(location, "door.open", SoundCategory.BLOCKS, 0.75f, 1f);
                    });
                    if (doorOpenSeconds != 0) {
                        Scheduler.runDelayed(20L * doorOpenSeconds, () -> {
                            service.getGame().getAnimationFacade().getService().play(animation, true);
                            Scheduler.runDelayed((long) animation.getTicks() * animation.getFrames().size(), () -> {
                                location.getWorld().playSound(location, "door.open", SoundCategory.BLOCKS, 0.75f, 1f);
                                animation.getTempData().put("doorOpen", false);
                            });
                        });
                    }
                    animation.getTempData().put("doorOpen", true);
                    return true;

                }
            }
            return false;
        });
        frame.openInventory(interacter);
    }
}
