package in.prismar.game.ai;

import in.prismar.library.meta.anno.Service;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
public class AIRegistry {

    @Getter
    private static AIRegistry instance;

    private List<AI> ais = new ArrayList<>();

    public AIRegistry() {
        instance = this;
    }

    public void register(AI ai) {
        ais.add(ai);
    }

    public void unregister(AI ai) {
        ais.remove(ai);
    }


}
