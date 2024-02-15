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
- `enteredNetherPosition`[^9]
- `RootVehicle`[^1]
- `ShoulderEntityLeft`
- `ShoulderEntityRight`
- `SpawnDimension`[^7]
- `SpawnForced`[^9]
- `SpawnX`[^7]
- `SpawnY`[^7]
- `SpawnZ`[^7]
- `XpLevel`[^8]
- `XpP`[^8]
- `XpSeed`[^9]
- `XpTotal`[^8]

[^1]: Use the `/ride` command.
[^2]: Use the `/tp` command.
[^3]: Use the `/effect` command.
[^4]: Use the `/attribute` command.
[^5]: Though Minecraft has a left-handed option, this can't be controlled by the server.
[^6]: Though Minecraft has a left-handed option, this can't be controlled by the server.
[^7]: Use the `/spawnpoint` command.
[^8]: Use the `/xp` command.
[^9]: I can't think of any use cases for this tag, but will add support on request.
