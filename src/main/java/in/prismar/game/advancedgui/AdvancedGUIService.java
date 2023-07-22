package in.prismar.game.advancedgui;

import in.prismar.game.Game;
import in.prismar.library.meta.anno.Service;
import me.leoko.advancedgui.manager.LayoutManager;

@Service
public class AdvancedGUIService {

    public AdvancedGUIService(Game game) {
        LayoutManager.getInstance().registerLayoutExtension(new BountyLayoutExtension(), game);

    }
}
