package in.prismar.game.missions.impl;

import in.prismar.api.mission.AbstractMission;
import in.prismar.api.mission.MissionType;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ActivateQuarryMission extends AbstractMission {


    public ActivateQuarryMission() {
        super("activate_quarry", MissionType.WEEKLY, 1, Material.COAL, "§bMore resources!");
    }

    @Override
    public String getDescription(int stage) {
        return "§bActivate a quarry";
    }

    @Override
    public String[] getRewards() {
        return new String[]{
                "§01x Diesel Fuel",
        };
    }

    @Override
    public void onReceiveReward(Player player) {
        ItemUtil.giveItem(player, new ItemBuilder(Material.FEATHER).setCustomModelData(10).setName("§cDiesel Fuel").build());


    }

    @Override
    public long getMaxProgress(int stage) {
        return 1;
    }
}
