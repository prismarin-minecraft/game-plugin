package in.prismar.game.storage.repository;

import in.prismar.game.storage.model.Storage;
import in.prismar.library.common.repository.AsyncRepository;
import in.prismar.library.common.repository.Repository;

public interface StorageRepository extends Repository<String, Storage>, AsyncRepository<String, Storage> {


}
