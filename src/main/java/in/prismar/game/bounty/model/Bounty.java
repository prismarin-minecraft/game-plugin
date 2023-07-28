package in.prismar.game.bounty.model;

import in.prismar.library.common.repository.entity.StringRepositoryEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class Bounty extends StringRepositoryEntity {

    private String name;
    private List<BountySupplier> suppliers;

    public boolean isSupplier(String uuid) {
        for(BountySupplier supplier : suppliers) {
            if(supplier.getUuid().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public Optional<BountySupplier> getSupplier(String uuid) {
        return suppliers.stream().filter(supplier -> supplier.getUuid().equals(uuid)).findFirst();
    }

    public double getMoney() {
        double money = 0;
        for(BountySupplier supplier : suppliers) {
            money += supplier.getMoney();
        }
        return money;
    }

    @Override
    public String toString() {
        return getId();
    }
}
