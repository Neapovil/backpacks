package com.github.neapovil.backpacks;

import org.bukkit.plugin.java.JavaPlugin;

public final class Backpacks extends JavaPlugin
{
    private static Backpacks instance;

    @Override
    public void onEnable()
    {
        instance = this;
    }

    public static Backpacks instance()
    {
        return instance;
    }
}
