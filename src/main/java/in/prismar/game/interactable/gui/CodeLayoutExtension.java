package in.prismar.game.interactable.gui;

import in.prismar.game.Game;
import in.prismar.game.interactable.InteractableService;
import in.prismar.game.interactable.model.Interactable;
import in.prismar.game.interactable.model.keycode.Keycode;
import lombok.AllArgsConstructor;
import me.leoko.advancedgui.utils.Layout;
import me.leoko.advancedgui.utils.LayoutExtension;
import me.leoko.advancedgui.utils.components.TextComponent;
import me.leoko.advancedgui.utils.events.LayoutLoadEvent;
import org.bukkit.event.EventHandler;

@AllArgsConstructor
public class CodeLayoutExtension implements LayoutExtension {

    private final InteractableService service;

    @Override
    @EventHandler
    public void onLayoutLoad(LayoutLoadEvent event) {
        Layout layout = event.getLayout();
        final String lowered = layout.getName().toLowerCase();
        if(service.getRepository().existsById(lowered)) {
            Interactable interactable = service.getRepository().findById(lowered);
            if(interactable instanceof Keycode keycode) {
                if(keycode.getCodes().isEmpty()) {
                    return;
                }
                TextComponent component = layout.getTemplateComponentTree().locate("cJInfGCZ", TextComponent.class);
                component.setText(keycode.getCodes().iterator().next());

                LayoutInitialization.TEXT_COMPONENT_MAP.put(lowered, component);
            }
        }
    }
}