package in.prismar.game.storage.model;

import in.prismar.library.common.repository.entity.StringRepositoryEntity;
import in.prismar.library.spigot.item.container.ItemsContainer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Storage extends StringRepositoryEntity {

    private ItemsContainer items;

    public Storage(String id, ItemsContainer items) {
        super(id);
        this.items = items;
    }
}
