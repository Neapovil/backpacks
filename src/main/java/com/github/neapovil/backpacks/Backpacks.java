package com.github.neapovil.backpacks;

import java.io.IOException;
import java.nio.file.Files;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.neapovil.backpacks.command.GiveCommand;
import com.github.neapovil.backpacks.listener.BackpackListener;
import com.github.neapovil.backpacks.persistence.BackpackDataType;
import com.github.neapovil.backpacks.resources.BackpacksResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class Backpacks extends JavaPlugin
{
    private static Backpacks instance;
    private BackpacksResource backpacksResource;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public final NamespacedKey BACKPACK_KEY = new NamespacedKey(this, "backpack");
    public final BackpackDataType backpackDataType = new BackpackDataType();

    @Override
    public void onEnable()
    {
        instance = this;

        this.saveResource("backpacks.json", false);

        try
        {
            this.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        this.getServer().getPluginManager().registerEvents(new BackpackListener(), this);

        new GiveCommand().register();
    }

    public static Backpacks instance()
    {
        return instance;
    }

    public BackpacksResource backpacksResource()
    {
        return this.backpacksResource;
    }

    public void load() throws IOException
    {
        final String string = Files.readString(this.getDataFolder().toPath().resolve("backpacks.json"));
        this.backpacksResource = this.gson.fromJson(string, BackpacksResource.class);
    }

    public void save() throws IOException
    {
        final String string = this.gson.toJson(this.backpacksResource);
        Files.write(this.getDataFolder().toPath().resolve("backpacks.json"), string.getBytes());
    }
}
