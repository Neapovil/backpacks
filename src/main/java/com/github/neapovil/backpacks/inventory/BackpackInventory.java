package com.github.neapovil.backpacks.inventory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import com.github.neapovil.backpacks.Backpacks;
import com.github.neapovil.backpacks.gson.BackpackInventoryGson;

public final class BackpackInventory implements InventoryHolder
{
    private final Backpacks plugin;
    private final int size;
    private final int backpackId;
    private final Inventory inventory;
    public ItemStack itemStack;

    public BackpackInventory(Backpacks plugin, int size, int backpackId)
    {
        this.plugin = plugin;
        this.size = size;
        this.backpackId = backpackId;
        this.inventory = Bukkit.createInventory(this, size);
    }

    @Override
    public @NotNull Inventory getInventory()
    {
        return this.inventory;
    }

    public void deserialize()
    {
        final BackpackInventoryGson backpackinventorygson = this.itemStack.getItemMeta().getPersistentDataContainer().get(plugin.BACKPACK_KEY,
                plugin.backpackDataType);

        if (backpackinventorygson == null)
        {
            return;
        }

        if (backpackinventorygson.items.size() == 0)
        {
            return;
        }

        for (int i = 0; i < this.size; i++)
        {
            inventory.setItem(i, backpackinventorygson.items.get(i));
        }
    }

    public void serialize()
    {
        final BackpackInventoryGson backpackinventorygson = new BackpackInventoryGson(this.backpackId);

        for (int i = 0; i < this.size; i++)
        {
            backpackinventorygson.items.add(i, inventory.getItem(i));
        }

        this.itemStack.editMeta(itemmeta -> {
            itemmeta.getPersistentDataContainer().set(plugin.BACKPACK_KEY, plugin.backpackDataType, backpackinventorygson);
        });
    }
}
