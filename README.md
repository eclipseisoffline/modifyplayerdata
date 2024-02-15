# Modify Player Data

This mod allows Minecraft's data commands (`/data`, `/execute store`, etc.) to modify player data.

Feel free to report any bugs, or suggest new features, at the issue tracker.

## License

This mod is licensed under GNU GPLv3.

## Usage

This mod is currently in development.

This mod is currently available for Minecraft 1.20.4 (Fabric modloader). The Fabric API is not
required.

## Supported tags

NBT tags currently supported:

- `Air`
- `FallDistance`
- `Fire`
- `Glowing`
- `Invulnerable`
- `Motion`
- `NoGravity`
- `PortalCooldown`
- `Tags`
- `TicksFrozen`
- `AbsorptionAmount`
- `Health`
- `abilities`
- `SelectedItemSlot`

NBT tags I plan to support:

- `HasVisualFire`
- `Silent`
- `HurtTime`
- `EnderItems`
- `foodExhaustionLevel`
- `foodLevel`
- `foodSaturationLevel`
- `foodTickTimer`
- `Inventory`
- `LastDeathLocation`
- `playerGameType`
- `previousPlayerGameType`
- `recipeBook`
- `Score`
- `seenCredits`
- `SelectedItem`
- `SleepTimer`
- `warden_spawn_tracker`

NBT tags I won't add support for:

- `CustomName`
- `CustomNameVisible`
- `OnGround`
- `Passengers`[^1]
- `Pos`[^2]
- `Rotation`[^2]
- `UUID`
- `active_effects`[^3]
- `ArmorDropChances`
- `ArmorItems`
- `Attributes`[^4]
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
- `LeftHanded`[^5]
- `NoAI`
- `PersistenceRequired`
- `SleepingX`
- `SleepingY`
- `SleepingZ`
- `DataVersion`
- `Dimension`[^2]
- `enteredNetherPosition`[^8]
- `RootVehicle`[^1]
- `ShoulderEntityLeft`
- `ShoulderEntityRight`
- `SpawnDimension`[^6]
- `SpawnForced`[^8]
- `SpawnX`[^6]
- `SpawnY`[^6]
- `SpawnZ`[^6]
- `XpLevel`[^7]
- `XpP`[^7]
- `XpSeed`[^8]
- `XpTotal`[^7]

[^1]: Use the `/ride` command.
[^2]: Use the `/tp` command.
[^3]: Use the `/effect` command.
[^4]: Use the `/attribute` command.
[^5]: Though Minecraft has a left-handed option, this can't be controlled by the server.
[^6]: Use the `/spawnpoint` command.
[^7]: Use the `/xp` command.
[^8]: I can't think of any use cases for this tag, but will add support on request.
