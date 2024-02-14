package in.prismar.game.interactable.gui;

import in.prismar.game.Game;
import in.prismar.game.bounty.BountyService;
import in.prismar.game.interactable.InteractableService;
import me.leoko.advancedgui.manager.LayoutManager;
import me.leoko.advancedgui.utils.components.TextComponent;

import java.util.HashMap;
import java.util.Map;

public final class LayoutInitialization {

    protected static final Map<String, TextComponent> TEXT_COMPONENT_MAP = new HashMap<>();

    public static void init(InteractableService service, Game game) {
       LayoutManager.getInstance().registerLayoutExtension(new CodeLayoutExtension(service), game);
    }

    public static void changeCode(String id, String code) {
        if (TEXT_COMPONENT_MAP.containsKey(id)) {
            TextComponent component = TEXT_COMPONENT_MAP.get(id);
            component.setText(code);
        }
    }

    private LayoutInitialization() {}
}