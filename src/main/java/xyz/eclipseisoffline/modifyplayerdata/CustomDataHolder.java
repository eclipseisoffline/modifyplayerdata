package xyz.eclipseisoffline.modifyplayerdata;

import net.minecraft.nbt.NbtCompound;

public interface CustomDataHolder {

    NbtCompound modifyPlayerData$getCustomNbtData();
    void modifyPlayerData$setCustomNbtData(NbtCompound data);
}
