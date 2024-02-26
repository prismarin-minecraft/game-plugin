package in.prismar.game.interactable;

import in.prismar.game.Game;
import in.prismar.game.interactable.gui.LayoutInitialization;
import in.prismar.game.interactable.model.Interactable;
import in.prismar.game.interactable.model.keycode.Keycode;
import in.prismar.game.interactable.repository.FileInteractableRepository;
import in.prismar.game.interactable.repository.InteractableRepository;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.Location;

import java.util.HashSet;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class InteractableService {

    private final Game game;
    private final InteractableRepository repository;

    public InteractableService(Game game) {
        this.game = game;
        this.repository = new FileInteractableRepository(this);

        try {
            LayoutInitialization.init(this, game);
        }catch (NoClassDefFoundError exception) {
            System.out.println("Couldn't find AdvancedGUI");
        }

    }

    public <T extends Interactable> T create(String id, Location location, InteractableType type) {
        try {
            T interactable = (T) type.getType().getConstructor().newInstance();
            interactable.setId(id);
            interactable.setLocation(location);
            if(type == InteractableType.KEYCODE) {
                Keycode code = (Keycode) interactable;
                code.setCodes(new HashSet<>());
            }

            repository.create(interactable);
            return interactable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
