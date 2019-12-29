package jsound.atomicItems;

import jsound.item.AtomicItem;

public class NullItem extends AtomicItem {

    public NullItem() {
    }

    public boolean isNullItem() {
        return true;
    }

    @Override
    public String getStringValue() {
        return null;
    }

    @Override
    public String getStringAnnotation() {
        return this.getStringValue();
    }
}
