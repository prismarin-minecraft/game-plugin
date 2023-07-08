package in.prismar.game.battleroyale.arena;

import in.prismar.game.Game;
import in.prismar.game.battleroyale.arena.droptable.DropTableFile;
import in.prismar.game.battleroyale.arena.model.BattleRoyaleArena;
import in.prismar.game.battleroyale.arena.repository.BattleRoyaleArenaRepository;
import in.prismar.game.battleroyale.arena.repository.FileBattleRoyaleArenaRepository;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;

@Service
@Getter
public class BattleRoyaleArenaService {

    private BattleRoyaleArenaRepository repository;
    private DropTableFile droptable;

    public BattleRoyaleArenaService(Game game) {
        final String directory = game.getDefaultDirectory().concat("battleroyale").concat(File.separator);
        this.repository = new FileBattleRoyaleArenaRepository(directory);
        this.droptable = new DropTableFile(directory);
    }

    public BattleRoyaleArena create(String id, String displayName) {
        BattleRoyaleArena arena = new BattleRoyaleArena();
        arena.setId(id);
        arena.setDisplayName(displayName);
        arena.setDrops(new ArrayList<>());
        return repository.create(arena);
    }
}
