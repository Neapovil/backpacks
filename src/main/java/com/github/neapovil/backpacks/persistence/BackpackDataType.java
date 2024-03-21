package com.github.neapovil.backpacks.persistence;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import com.github.neapovil.backpacks.gson.BackpackInventoryGson;
import com.google.gson.Gson;

public final class BackpackDataType implements PersistentDataType<String, BackpackInventoryGson>
{
    private final Gson gson = new Gson();

    @Override
    public @NotNull Class<String> getPrimitiveType()
    {
        return String.class;
    }

    @Override
    public @NotNull Class<BackpackInventoryGson> getComplexType()
    {
        return BackpackInventoryGson.class;
    }

    @Override
    public @NotNull String toPrimitive(@NotNull BackpackInventoryGson complex, @NotNull PersistentDataAdapterContext context)
    {
        return this.gson.toJson(complex);
    }

    @Override
    public @NotNull BackpackInventoryGson fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context)
    {
        return this.gson.fromJson(primitive, this.getComplexType());
    }

}
