package in.prismar.game.bounty.gui;

import in.prismar.game.Game;
import in.prismar.game.bounty.BountyService;
import me.leoko.advancedgui.manager.LayoutManager;

public final class LayoutInitialization {

    public static void init(BountyService service, Game game) {
        LayoutManager.getInstance().registerLayoutExtension(new BountyLayoutExtension(service), game);
    }

    private LayoutInitialization() {}
}
