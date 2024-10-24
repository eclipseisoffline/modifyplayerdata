# Modify Player Data

![Modrinth Version](https://img.shields.io/modrinth/v/e706DYY5?logo=modrinth&color=008800)
![Modrinth Game Versions](https://img.shields.io/modrinth/game-versions/e706DYY5?logo=modrinth&color=008800)
![Modrinth Downloads](https://img.shields.io/modrinth/dt/e706DYY5?logo=modrinth&color=008800)

This mod allows Minecraft's data commands (`/data`, `/execute store`, etc.) to modify player data.

Alongside supporting most vanilla player NBT tags, this mod also adds the `CustomData` NBT tag,
which can be used to store any type of NBT data on a per-player basis.

Feel free to report any bugs, or suggest new features, at the issue tracker.

## License

This mod is licensed under GNU LGPLv3.

## Usage

Mod builds can be found [here](https://github.com/eclipseisoffline/modifyplayerdata/packages/2069488) and on [Modrinth](https://modrinth.com/mod/modify-player-data).

This mod is currently available for Minecraft 1.21.2+3, 1.21+1, 1.20.5+6, and 1.20.4. Releases for older versions
of Minecraft are generally no longer updated, but should work fine.
The Fabric API is not required. This mod is not required on clients.

## Supported tags

NBT tags currently supported:

- `Air`
- `FallDistance`
- `Fire`
- `Glowing`
- `HasVisualFire`
- `Invulnerable`
- `Motion`
- `NoGravity`
- `PortalCooldown`
- `Pos`
- `Rotation`
- `Tags`
- `Silent`[^9]
- `TicksFrozen`
- `AbsorptionAmount`
- `attributes`
- `FallFlying`
- `Health`
- `HurtTime`[^9]
- `LeftHanded`
- `abilities`
- `EnderItems`
- `foodExhaustionLevel`
- `foodLevel`
- `foodSaturationLevel`
- `foodTickTimer`
- `Inventory`
- `recipeBook`
- `Score`
- `seenCredits`
- `SelectedItem`
- `SelectedItemSlot`
- `SleepTimer`
- `warden_spawn_tracker`
- `CustomData`
  - This tag doesn't exist in vanilla Minecraft, and doesn't do anything. It can be used by datapack creators to store custom NBT data.

NBT tags I won't add support for:

- `CustomName`
- `CustomNameVisible`
- `OnGround`
- `Passengers`[^1]
- `UUID`
- `active_effects`[^3]
- `ArmorDropChances`
- `ArmorItems`
- `Brain`
- `CanPickUpLoot`
- `DeathLootTable`
- `DeathLootTableSeed`
- `DeathTime`
- `FallFlying`
- `HurtByTimestamp`
- `HandDropChances`
- `HandItems`
- `Leash`
- `NoAI`
- `PersistenceRequired`
- `SleepingX`
- `SleepingY`
- `SleepingZ`
- `DataVersion`
- `Dimension`[^2]
- `enteredNetherPosition`[^10]
- `LastDeathLocation`[^8]
- `playerGameType`[^7]
- `previousPlayerGameType`[^7]
- `RootVehicle`[^1]
- `ShoulderEntityLeft`
- `ShoulderEntityRight`
- `SpawnDimension`[^5]
- `SpawnForced`[^10]
- `SpawnX`[^5]
- `SpawnY`[^5]
- `SpawnZ`[^5]
- `XpLevel`[^6]
- `XpP`[^6]
- `XpSeed`[^10]
- `XpTotal`[^6]

[^1]: Use the `/ride` command.
[^2]: Use the `/tp` command.
[^3]: Use the `/effect` command.
[^4]: Use the `/attribute` command.
[^5]: Use the `/spawnpoint` command.
[^6]: Use the `/xp` command.
[^7]: Use the `/gamemode` command.
[^8]: Unable to implement due to limitations within the vanilla client.
[^9]: Is implemented but does not do much due to limitations within the vanilla client
[^10]: I can't think of any use cases for this tag, but will add support on request.
