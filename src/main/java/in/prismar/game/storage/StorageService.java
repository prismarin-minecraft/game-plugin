package in.prismar.game.storage;

import in.prismar.game.Game;
import in.prismar.game.storage.model.Storage;
import in.prismar.game.storage.repository.FileStorageRepository;
import in.prismar.game.storage.repository.StorageRepository;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.item.container.ItemsContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class StorageService {

    private final Map<String, Inventory> inventoryCache;
    private final Map<UUID, String> openInventories;

    private StorageRepository repository;

    public StorageService(Game game) {
        this.inventoryCache = new HashMap<>();
        this.openInventories = new HashMap<>();
        this.repository = new FileStorageRepository(game.getDefaultDirectory());
    }

    public Inventory openStorageInventory(Player player, String id, String title, int rows) {
        Inventory inventory = getStorageAsInventory(id, title, rows);
        player.openInventory(inventory);
        this.openInventories.put(player.getUniqueId(), id);
        return inventory;
    }

    @Nullable
    public String closeStorageInventory(Player player, Inventory inventory) {
        if(openInventories.containsKey(player.getUniqueId())) {
            final String id = openInventories.remove(player.getUniqueId());
            saveStorageInventory(id, inventory);
        }
        return null;
    }

    public Inventory getStorageAsInventory(String id, String title, int rows) {
        if(inventoryCache.containsKey(id)) {
            return inventoryCache.get(id);
        }
        Storage storage = getOrCreateStorage(id);
        Inventory inventory = Bukkit.createInventory(null, rows * 9, title);
        inventory.setContents(storage.getItems().getItem());
        inventoryCache.put(id, inventory);
        return inventory;
    }

    public void saveStorageInventory(String id, Inventory inventory) {
        Storage storage = getStorageById(id);
        storage.setItems(new ItemsContainer(inventory.getContents()));
        repository.saveAsync(storage, true);
    }

    /**
     * Expensive call
     *
     * @return {@link Storage}
     */
    public Storage getStorageById(String id) {
        Storage storage = repository.findById(id);
        if(storage.getItems().getItem() == null) {
            storage.getItems().deserialize();
        }
        return repository.findById(id);
    }

    /**
     * Expensive call
     *
     * @param id
     * @return
     */
    public Storage getOrCreateStorage(String id) {
        Optional<Storage> optional = repository.findByIdOptional(id);
        if(optional.isEmpty()) {
            Storage storage = new Storage(id, new ItemsContainer(new ItemStack[0]));
            return repository.create(storage);
        }
        Storage storage = optional.get();
        if(storage.getItems().getItem() == null) {
            storage.getItems().deserialize();
        }
        return storage;
    }
}
