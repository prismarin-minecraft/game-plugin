package in.prismar.game.quarry.repository;

import in.prismar.game.quarry.model.Quarry;
import in.prismar.library.common.repository.AsyncRepository;
import in.prismar.library.common.repository.Repository;

public interface QuarryRepository extends Repository<String, Quarry>, AsyncRepository<String, Quarry> {
}
