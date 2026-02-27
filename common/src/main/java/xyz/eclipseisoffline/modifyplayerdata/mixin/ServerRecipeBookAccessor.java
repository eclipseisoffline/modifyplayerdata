package xyz.eclipseisoffline.modifyplayerdata.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ServerRecipeBook.class)
public interface ServerRecipeBookAccessor {

    @Accessor
    Set<ResourceKey<Recipe<?>>> getKnown();
}
