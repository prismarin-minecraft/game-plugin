package in.prismar.game.bounty.repository;

import com.google.gson.reflect.TypeToken;
import in.prismar.game.bounty.model.Bounty;
import in.prismar.library.file.gson.GsonCompactRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class FileBountyRepository extends GsonCompactRepository<Bounty> implements BountyRepository{
    public FileBountyRepository(String directory) {
        super(directory.concat("bounties.json"), new TypeToken<Map<String, Bounty>>(){});

    }

    @Override
    public List<Bounty> getBountiesSorted() {
        return this.getEntity().values().stream()
                .sorted((o1, o2) -> Double.compare(o2.getMoney(), o1.getMoney()))
                .toList();
    }

    @Override
    public CompletableFuture<Bounty> createAsync(Bounty entity) {
        return CompletableFuture.supplyAsync(() -> create(entity));
    }

    @Override
    public CompletableFuture<Bounty> findByIdAsync(String s) {
        return CompletableFuture.supplyAsync(() -> findById(s));
    }

    @Override
    public CompletableFuture<Optional<Bounty>> findByIdOptionalAsync(String s) {
        return CompletableFuture.supplyAsync(() -> findByIdOptional(s));
    }

    @Override
    public CompletableFuture<Collection<Bounty>> findAllAsync() {
        return CompletableFuture.supplyAsync(this::findAll);
    }

    @Override
    public CompletableFuture<Boolean> existsByIdAsync(String s) {
        return CompletableFuture.supplyAsync(() -> existsById(s));
    }

    @Override
    public CompletableFuture<Bounty> saveAsync(Bounty entity, boolean delayedExecution) {
        return CompletableFuture.supplyAsync(() -> save(entity));
    }

    @Override
    public CompletableFuture<Bounty> deleteAsync(Bounty entity) {
        return CompletableFuture.supplyAsync(() -> delete(entity));
    }

    @Override
    public CompletableFuture<Bounty> deleteByIdAsync(String s) {
        return CompletableFuture.supplyAsync(() -> deleteById(s));
    }
}
