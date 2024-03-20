package com.github.neapovil.backpacks.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import com.github.neapovil.backpacks.Backpacks;
import com.github.neapovil.backpacks.gson.BackpackInventoryGson;
import com.github.neapovil.backpacks.inventory.BackpackInventory;

public final class BackpackListener implements Listener
{
    private final Backpacks plugin = Backpacks.instance();
    private final Map<UUID, BackpackInventory> backpacks = new HashMap<>();

    @EventHandler
    private void onPlayerInteractItem(PlayerInteractEvent event)
    {
        if (!event.hasItem())
        {
            return;
        }

        if (!event.getAction().isRightClick())
        {
            return;
        }

        if (!event.getItem().getItemMeta().getPersistentDataContainer().has(plugin.BACKPACK_KEY))
        {
            return;
        }

        final BackpackInventoryGson backpackinventorygson = event.getItem().getItemMeta().getPersistentDataContainer().get(plugin.BACKPACK_KEY,
                plugin.backpackDataType);

        plugin.backpacksResource().find(backpackinventorygson.backpackId).ifPresent(backpack -> {
            final BackpackInventory backpackinventory = new BackpackInventory(plugin, backpack.size, backpack.id);
            backpackinventory.itemStack = event.getItem();
            backpackinventory.deserialize();
            this.backpacks.put(event.getPlayer().getUniqueId(), backpackinventory);
            event.getPlayer().openInventory(backpackinventory.getInventory());
        });
    }

    @EventHandler
    private void onServerTickStart(ServerTickStartEvent event)
    {
        this.backpacks.values().forEach(i -> i.serialize());
    }

    @EventHandler
    private void onServerTickEnd(ServerTickEndEvent event)
    {
        this.backpacks.values().removeIf(i -> i.getInventory().getViewers().isEmpty());
    }

    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent event)
    {
        if (!event.getItemDrop().getItemStack().getItemMeta().getPersistentDataContainer().has(plugin.BACKPACK_KEY))
        {
            return;
        }

        if (this.backpacks.containsKey(event.getPlayer().getUniqueId()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event)
    {
        if (!(event.getInventory().getHolder() instanceof BackpackInventory))
        {
            return;
        }

        if (event.getCurrentItem() == null)
        {
            return;
        }

        if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(plugin.BACKPACK_KEY))
        {
            event.setCancelled(true);
        }
    }
}
