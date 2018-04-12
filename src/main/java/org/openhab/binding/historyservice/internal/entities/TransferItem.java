package org.openhab.binding.historyservice.internal.entities;

import com.google.gson.Gson;

/**
 * Created by phil on 31.07.16.
 */
public class TransferItem extends AbstractItem {
    private ItemState itemState;

    public TransferItem() {
        super();
    }

    public TransferItem(String itemName, String itemType, ItemState itemState) {
        super(itemName, itemType);
        this.itemState = itemState;
    }

    public ItemState getItemState() {
        return itemState;
    }

    public void setItemState(ItemState itemStateList) {
        this.itemState = itemStateList;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}