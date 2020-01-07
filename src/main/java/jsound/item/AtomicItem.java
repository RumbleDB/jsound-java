package jsound.item;

import org.api.Item;

public abstract class AtomicItem extends Item {

    @Override
    public boolean isAtomicItem() {
        return true;
    }
}
