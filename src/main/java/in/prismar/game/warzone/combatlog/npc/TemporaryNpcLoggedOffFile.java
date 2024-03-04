package in.prismar.game.warzone.combatlog.npc;

import com.google.common.reflect.TypeToken;
import in.prismar.library.file.gson.GsonFileWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TemporaryNpcLoggedOffFile extends GsonFileWrapper<List<UUID>> {

    public TemporaryNpcLoggedOffFile(String directory) {
        super(directory.concat("tmp_npc_loggs.json"), new TypeToken<List<UUID>>(){}.getType());
        load();
        if(getEntity() == null) {
            setEntity(new ArrayList<>());
        }
    }
}
