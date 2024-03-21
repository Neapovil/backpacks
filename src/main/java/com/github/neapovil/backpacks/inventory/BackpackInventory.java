package com.github.neapovil.backpacks.inventory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import com.github.neapovil.backpacks.Backpacks;
import com.github.neapovil.backpacks.gson.BackpackInventoryGson;
import com.github.neapovil.backpacks.resources.BackpacksResource;

public final class BackpackInventory implements InventoryHolder
{
    private final Backpacks plugin;
    private final ItemStack itemStack;
    private final BackpacksResource.Backpack backpack;
    private final Inventory inventory;

    public BackpackInventory(Backpacks plugin, ItemStack itemStack, BackpacksResource.Backpack backpack)
    {
        this.plugin = plugin;
        this.itemStack = itemStack;
        this.backpack = backpack;
        this.inventory = Bukkit.createInventory(this, this.backpack.size, backpack.displayName());
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

        for (int i = 0; i < this.backpack.size; i++)
        {
            inventory.setItem(i, backpackinventorygson.items.get(i));
        }
    }

    public void serialize()
    {
        final BackpackInventoryGson backpackinventorygson = new BackpackInventoryGson(this.backpack.id);

        for (int i = 0; i < this.backpack.size; i++)
        {
            backpackinventorygson.items.add(i, inventory.getItem(i));
        }

        this.itemStack.editMeta(itemmeta -> {
            itemmeta.getPersistentDataContainer().set(plugin.BACKPACK_KEY, plugin.backpackDataType, backpackinventorygson);
        });
    }
}
