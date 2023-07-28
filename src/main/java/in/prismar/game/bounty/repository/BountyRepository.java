package in.prismar.game.bounty.repository;

import in.prismar.game.bounty.model.Bounty;
import in.prismar.library.common.repository.AsyncRepository;
import in.prismar.library.common.repository.Repository;

import java.util.List;

public interface BountyRepository extends Repository<String, Bounty>, AsyncRepository<String, Bounty> {

    List<Bounty> getBountiesSorted();
}
