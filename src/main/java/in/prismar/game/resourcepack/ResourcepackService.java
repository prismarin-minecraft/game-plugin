package in.prismar.game.resourcepack;

import in.prismar.api.PrismarinApi;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.core.CoreProvider;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;

import java.util.HexFormat;

@Service
@Getter
public class ResourcepackService {

    private String url;
    private byte[] hash;

    public ResourcepackService() {
        load();
    }

    public void load() {
        ConfigStore store = PrismarinApi.getProvider(ConfigStore.class);
        CoreProvider coreProvider = PrismarinApi.getProvider(CoreProvider.class);
        if(coreProvider.isDevMode()) {
            this.url = store.getProperty("dev.resourcepack.url");
            this.hash = HexFormat.of().parseHex(store.getProperty("dev.resourcepack.hash"));
        } else {
            this.url = store.getProperty("resourcepack.url");
            this.hash = HexFormat.of().parseHex(store.getProperty("resourcepack.hash"));
        }
    }
}
