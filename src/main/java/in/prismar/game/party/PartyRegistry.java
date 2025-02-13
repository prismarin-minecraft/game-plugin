package in.prismar.game.party;

import in.prismar.api.PrismarinApi;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.party.PartyProvider;
import in.prismar.api.scoreboard.ScoreboardProvider;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.Game;
import in.prismar.game.missions.MissionWrapper;
import in.prismar.game.party.task.PartyLimitCheckerTask;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.common.registry.LocalMapRegistry;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Service
public class PartyRegistry extends LocalMapRegistry<String, Party> implements PartyProvider {

    public static final String PARTY_CHAT_PREFIX = "§8[§dParty§8] §d";

    @Inject
    private Game game;

    @Inject
    private MissionWrapper missionWrapper;

    private final ConfigStore store;
    private final UserProvider<User> userProvider;
    private final ConfigStore configStore;

    public PartyRegistry(Game game) {
        super(false, false);
        this.store = PrismarinApi.getProvider(ConfigStore.class);
        this.userProvider = PrismarinApi.getProvider(UserProvider.class);
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);

        Bukkit.getScheduler().runTaskTimerAsynchronously(game, new PartyLimitCheckerTask(this), 20, 20);
    }



    public boolean isPartyChatToggled(Player player) {
        User user = userProvider.getUserByUUID(player.getUniqueId());
        return user.containsTag("partychat");
    }

    public boolean togglePartyChat(Player player) {
        User user = userProvider.getUserByUUID(player.getUniqueId());
        if(!user.containsTag("partychat")) {
            user.setTag("partychat", true);
            return true;
        }
        user.removeTag("partychat");
        return false;
    }

    public boolean isPartyOverLimit(Party party) {
        return party.getSize() > getAllowedPartySize();
    }

    public boolean isPartyOverLimitOrEquals(Party party) {
        return party.getSize() >= getAllowedPartySize();
    }

    public int getAllowedPartySize() {
        return Bukkit.getOnlinePlayers().size() > getPartySizeLimitPlayers() ? getMaxPartySize() : getPartySizeLimit();
    }

    public int getPartySizeLimitPlayers() {
        return configStore.getIntPropertyOrDefault("party.size.limit.players", 40);
    }

    public int getPartySizeLimit() {
        return configStore.getIntPropertyOrDefault("party.size.limit", 3);
    }

    public int getMaxPartySize() {
        return configStore.getIntPropertyOrDefault("party.max.size", 4);
    }


    public void glow(Party party, Player target) {
        ScoreboardProvider provider = PrismarinApi.getProvider(ScoreboardProvider.class);
        try {
            game.getGlowingEntities().setGlowing(target, party.getOwner(), ChatColor.GREEN);
            game.getGlowingEntities().setGlowing(party.getOwner(), target, ChatColor.GREEN);
            provider.recreateTablist(party.getOwner());
            for(Player member : party.getMembers()) {
                game.getGlowingEntities().setGlowing(target, member, ChatColor.GREEN);
                game.getGlowingEntities().setGlowing(member, target, ChatColor.GREEN);
                provider.recreateTablist(member);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public void unglow(Party party, Player target) {
        ScoreboardProvider provider = PrismarinApi.getProvider(ScoreboardProvider.class);
        try {
            game.getGlowingEntities().unsetGlowing(target, party.getOwner());
            game.getGlowingEntities().unsetGlowing(party.getOwner(), target);
            provider.recreateTablist(party.getOwner());
            for(Player member : party.getMembers()) {
                game.getGlowingEntities().unsetGlowing(target, member);
                game.getGlowingEntities().unsetGlowing(member, target);
                provider.recreateTablist(member);

            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean kick(Party party, Player target) {
        Iterator<Player> iterator = party.getMembers().iterator();
        while (iterator.hasNext()) {
            Player member = iterator.next();
            if(member.getUniqueId().equals(target.getUniqueId())) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public boolean leave(Party party, Player player) {
        unglow(party, player);
        if(party.getOwner().getUniqueId().equals(player.getUniqueId())) {
            if(party.getMembers().isEmpty()) {
                unregister(party.getOwner().getUniqueId().toString());
                return true;
            }
            Player random = party.getMembers().get(MathUtil.random(party.getMembers().size() - 1));
            unregister(player.getUniqueId().toString());
            party.setOwner(random);
            party.getMembers().remove(random);
            register(random.getUniqueId().toString(), party);
            return false;
        }
        removeMember(party, player);
        return false;
    }

    public Party create(Player owner) {
        Party party = new Party(owner);
        register(party.getOwner().getUniqueId().toString(), party);
        return party;
    }

    public Party getPartyByPlayer(Player player) {
        for(Party party : getAll()) {
            if(party.getOwner().getUniqueId().equals(player.getUniqueId())) {
                return party;
            }
            for(Player member : party.getMembers()) {
                if(member.getUniqueId().equals(player.getUniqueId())) {
                    return party;
                }
            }
        }
        return null;
    }

    public boolean hasInvite(Party party, Player target) {
        for(Player invited : party.getInvites()) {
            if(invited.getUniqueId().equals(target.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    public void sendMessage(Party party, String message) {
        party.getOwner().sendMessage(message);
        for(Player member : party.getMembers()) {
            member.sendMessage(message);
        }
    }

    public void removeMember(Party party, Player player) {
        Iterator<Player> iterator = party.getMembers().iterator();
        while (iterator.hasNext()) {
            Player member = iterator.next();
            if(member.getUniqueId().equals(player.getUniqueId())) {
                iterator.remove();
            }
        }
    }

    public void removeInvite(Party party, Player player) {
        Iterator<Player> iterator = party.getInvites().iterator();
        while (iterator.hasNext()) {
            Player invited = iterator.next();
            if(invited.getUniqueId().equals(player.getUniqueId())) {
                iterator.remove();
            }
        }
    }

    public boolean hasPartyAndIsOwner(Player player) {
        Party party = getPartyByPlayer(player);
        if(party != null) {
            return party.getOwner().getUniqueId().equals(player.getUniqueId());
        }
        return false;
    }

    @Override
    public boolean hasParty(Player player) {
        return getPartyByPlayer(player) != null;
    }

    @Override
    public boolean isOwnerOfParty(Player player) {
        if(hasParty(player)) {
            Party party = getPartyByPlayer(player);
            return party.getOwner().getUniqueId().equals(player.getUniqueId());
        }
        return false;
    }


    @Override
    public List<Player> getPartyMembers(Player player) {
        Party party = getPartyByPlayer(player);
        List<Player> members = new ArrayList<>(party.getMembers());
        members.add(party.getOwner());
        return members;
    }

}
