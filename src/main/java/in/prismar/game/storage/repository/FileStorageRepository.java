package in.prismar.game.storage.repository;

import in.prismar.game.storage.model.Storage;
import in.prismar.library.file.gson.GsonRepository;

import java.io.File;

public class FileStorageRepository extends GsonRepository<Storage> implements StorageRepository {
    public FileStorageRepository(String path) {
        super(path.concat("storages").concat(File.separator), Storage.class, "Storage", 10000);
    }
}
