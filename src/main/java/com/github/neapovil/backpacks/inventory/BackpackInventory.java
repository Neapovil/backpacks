package com.github.neapovil.backpacks.inventory;

import java.util.Arrays;

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
    private Inventory inventory;
    private BackpackInventoryGson backpackInventoryGson;

    public BackpackInventory(Backpacks plugin, ItemStack itemStack, BackpacksResource.Backpack backpack)
    {
        this.plugin = plugin;
        this.itemStack = itemStack;
        this.backpack = backpack;
    }

    @Override
    public @NotNull Inventory getInventory()
    {
        return this.inventory;
    }

    public void deserialize()
    {
        this.backpackInventoryGson = this.itemStack.getItemMeta().getPersistentDataContainer().get(plugin.BACKPACK_KEY,
                plugin.backpackDataType);

        if (this.backpackInventoryGson == null)
        {
            this.backpackInventoryGson = new BackpackInventoryGson(this.backpack.id, this.backpack.update, this.backpack.size);
            this.inventory.getViewers()
                    .forEach(i -> i.sendRichMessage("<red>Something was wrong with this backpack. The backpack has been rebuilded. ALL items lost."));
        }

        this.inventory = Bukkit.createInventory(this, Math.max(this.backpack.size, this.backpackInventoryGson.backpackSize), backpack.displayName());

        this.inventory.setContents(this.backpackInventoryGson.items.toArray(ItemStack[]::new));
    }

    public void serialize()
    {
        this.backpackInventoryGson.items.clear();
        this.backpackInventoryGson.items.addAll(Arrays.asList(this.inventory.getContents()));

        this.itemStack.editMeta(itemmeta -> {
            itemmeta.getPersistentDataContainer().set(plugin.BACKPACK_KEY, plugin.backpackDataType, this.backpackInventoryGson);
        });
    }
}
