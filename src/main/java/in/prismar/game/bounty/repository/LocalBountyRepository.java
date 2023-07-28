package in.prismar.game.bounty.repository;

import in.prismar.game.bounty.model.Bounty;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class LocalBountyRepository implements BountyRepository {

    private Map<String, Bounty> bounties;

    public LocalBountyRepository() {
        this.bounties = new HashMap<>();
    }

    @Override
    public List<Bounty> getBountiesSorted() {
        return this.bounties.values().stream()
                .sorted((o1, o2) -> Double.compare(o2.getMoney(), o1.getMoney()))
                .toList();
    }

    @Override
    public Bounty create(Bounty entity) {
        this.bounties.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Bounty findById(String id) {
        return this.bounties.get(id);
    }

    @Override
    public Optional<Bounty> findByIdOptional(String id) {
        if(existsById(id)) {
            return Optional.of(findById(id));
        }
        return Optional.empty();
    }

    @Override
    public Collection<Bounty> findAll() {
        return bounties.values();
    }

    @Override
    public boolean existsById(String id) {
        return bounties.containsKey(id);
    }

    @Override
    public Bounty save(Bounty entity) {
        return entity;
    }

    @Override
    public Bounty delete(Bounty entity) {
        return this.bounties.remove(entity.getId());
    }

    @Override
    public Bounty deleteById(String id) {
        return this.bounties.remove(id);
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
