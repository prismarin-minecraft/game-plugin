package in.prismar.game.item.impl.melee;

import in.prismar.api.user.User;
import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.impl.gun.sound.GunSoundType;
import in.prismar.game.item.model.SkinableItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class MeleeItem extends SkinableItem {

    private int damage;
    private MeleeAttackSpeed attackSpeed = MeleeAttackSpeed.NORMAL;

    private String previewImage;

    public MeleeItem(String id, Material material, String displayName) {
        super(id, material, displayName);
        allFlags();
        setUnbreakable(true);
    }

    @CustomItemEvent
    public void onChangeSlot(Player player, Game game, CustomItemHolder holder, PlayerItemHeldEvent event) {
        final ItemStack stack = player.getInventory().getItem(event.getNewSlot());
        if(stack != null) {
            if(stack.hasItemMeta()) {
                if(stack.getItemMeta().hasDisplayName()) {
                    if(stack.getItemMeta().getDisplayName().equals(getDisplayName())) {
                        player.playSound(player.getLocation(), "equip.melee", 0.6f, 1f);
                    }
                }
            }
        }
    }

    @CustomItemEvent
    public void onUpdate(Player player, Game game, CustomItemHolder holder, CustomItemHolder event) {
        if (event.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        if(attackSpeed.getAmplifier() > 0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10, attackSpeed.getAmplifier()-1, false, false));
        }

    }

    @CustomItemEvent
    public void onDamage(Player player, Game game, CustomItemHolder holder, EntityDamageByEntityEvent event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        if(event.getDamager().getUniqueId().equals(player.getUniqueId())) {
            User user = game.getExtractionFacade().getUserProvider().getUserByUUID(player.getUniqueId());
            if(user.containsTag("meleeCountdown")) {
                long time = user.getTag("meleeCountdown");
                if(System.currentTimeMillis() < time) {
                    event.setCancelled(true);
                    return;
                }
            }
            user.setTag("meleeCountdown",  System.currentTimeMillis() + attackSpeed.getCountdown());
            event.setDamage(damage);
        }

    }


    public void generateDefaultLore() {
        setLore(buildDefaultLore());
    }

    protected List<String> buildDefaultLore() {
        List<String> lore = new ArrayList<>();
        lore.add("§c ");
        lore.add(" §8╔ §7Damage§8: §b" + damage);
        lore.add(" §8╚ §7Attack speed§8: §b" + attackSpeed.getFancyName());
        lore.add("§c ");
        return lore;
    }

    @Override
    public ItemStack build() {
        ItemStack stack = super.build();
        ItemMeta meta = stack.getItemMeta();
        //meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier("generic.attackDamage", damage, AttributeModifier.Operation.ADD_NUMBER));
        stack.setItemMeta(meta);
        return stack;
    }


}
