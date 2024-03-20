package com.github.neapovil.backpacks.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.neapovil.backpacks.Backpacks;
import com.github.neapovil.backpacks.gson.BackpackInventoryGson;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class BackpacksResource
{
    public final List<Backpack> backpacks = new ArrayList<>();

    public List<String> commandSuggestions()
    {
        return this.backpacks.stream().map(i -> i.name).toList();
    }

    public Optional<Backpack> find(String name)
    {
        return this.backpacks.stream().filter(i -> i.name.equalsIgnoreCase(name)).findFirst();
    }

    public Optional<Backpack> find(int id)
    {
        return this.backpacks.stream().filter(i -> i.id == id).findFirst();
    }

    public static class Backpack
    {
        public int id;
        public String name;
        public String displayName;
        public List<String> lore = new ArrayList<>();
        public int size;
        public int modelData;

        public Component displayName()
        {
            return MiniMessage.miniMessage().deserialize(this.displayName);
        }

        public List<Component> lore()
        {
            return this.lore.stream().map(i -> MiniMessage.miniMessage().deserialize(i)).toList();
        }

        public ItemStack itemStack()
        {
            final Backpacks plugin = Backpacks.instance();
            final ItemStack itemstack = new ItemStack(Material.CLOCK);
            itemstack.editMeta(itemmeta -> {
                itemmeta.displayName(this.displayName());
                itemmeta.lore(this.lore());
                itemmeta.setCustomModelData(this.modelData);
                itemmeta.getPersistentDataContainer().set(plugin.BACKPACK_KEY, plugin.backpackDataType, new BackpackInventoryGson(this.id));
            });
            return itemstack;
        }
    }
}
