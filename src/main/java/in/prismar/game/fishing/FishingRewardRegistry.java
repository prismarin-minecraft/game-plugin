package in.prismar.game.fishing;

import com.google.gson.reflect.TypeToken;
import in.prismar.game.Game;
import in.prismar.library.file.gson.GsonFileWrapper;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.item.container.ItemContainer;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Service
public class FishingRewardRegistry extends GsonFileWrapper<List<FishingReward>> {
    public FishingRewardRegistry(Game game) {
        super(game.getDefaultDirectory().concat("fishing_rewards.json"), new TypeToken<List<FishingReward>>(){}.getType());
        load();
        if(getEntity() == null) {
            setEntity(new ArrayList<>());
        }
    }

    public FishingReward create(ItemStack item, float chance, String assignedRod) {
        FishingReward reward = new FishingReward();
        reward.setAssignedRod(assignedRod);
        reward.setChance(chance);
        reward.setItem(new ItemContainer(item));
        getEntity().add(reward);
        save();
        return reward;
    }

    public void remove(FishingReward reward) {
        this.getEntity().remove(reward);
        save();
    }

    public FishingReward getRandomByAssignedRod(String rod) {
        float random = (float) Math.random();
        float acumulatedChance = 0f;
        for (int i = 0; i < getEntity().size(); i++) {
            FishingReward reward = getEntity().get(i);
            if(!reward.getAssignedRod().equals(rod)) {
                continue;
            }
            acumulatedChance += reward.getChance();
            if (acumulatedChance >= random) {
                return reward;
            }
        }
        return getEntity().get(getEntity().size()-1);
    }
}
