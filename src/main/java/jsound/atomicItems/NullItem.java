package jsound.atomicItems;

import jsound.item.AtomicItem;
import org.api.Item;

public class NullItem extends AtomicItem {

    public NullItem() {
    }

    public boolean isNullItem() {
        return true;
    }

    @Override
    public String getStringValue() {
        return "null";
    }

    @Override
    public String getStringAnnotation() {
        return this.getStringValue();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof NullItem || obj instanceof StringItem)
            && ((Item) obj).getStringValue()
                .equals(this.getStringValue());
    }
}
