# Modify Player Data

[![Modrinth Version](https://img.shields.io/modrinth/v/e706DYY5?logo=modrinth&color=008800)](https://modrinth.com/mod/modify-player-data)
[![Modrinth Game Versions](https://img.shields.io/modrinth/game-versions/e706DYY5?logo=modrinth&color=008800)](https://modrinth.com/mod/modify-player-data)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/e706DYY5?logo=modrinth&color=008800)](https://modrinth.com/mod/modify-player-data)
[![Discord Badge](https://img.shields.io/badge/chat-discord-%235865f2)](https://discord.gg/CNNkyWRkqm)
[![Github Badge](https://img.shields.io/badge/github-modifyplayerdata-white?logo=github)](https://github.com/eclipseisoffline/modifyplayerdata)
![GitHub License](https://img.shields.io/github/license/eclipseisoffline/modifyplayerdata)

This mod allows Minecraft's data commands (`/data`, `/execute store`, etc.) to modify player data.

Feel free to report any bugs, or suggest new features, at the issue tracker.

## License

This mod is licensed under GNU LGPLv3.

## Donating

If you like this mod, consider [donating](https://buymeacoffee.com/eclipseisoffline).

## Discord

For support and/or any questions you may have, feel free to join [my discord](https://discord.gg/CNNkyWRkqm).

## Version support

| Minecraft Version | Status       |
|-------------------|--------------|
| 1.21.5            | ✅ Current    |
| 1.21.4            | ✔️ Available |
| 1.21.2+3          | ✔️ Available |
| 1.21+1            | ✅ Current    |
| 1.20.5+6          | ✔️ Available |
| 1.20.4            | ✔️ Available |
| 1.20.1            | ✔️ Available |

I try to keep support up for the latest major and latest minor release of Minecraft. Updates to newer Minecraft
versions may be delayed from time to time, as I do not always have the time to immediately update my mods.

Unsupported versions are still available to download, but they won't receive new features or bugfixes.

## Usage

Mod builds can be found on the releases page, as well as on [Modrinth](https://modrinth.com/mod/modify-player-data).

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
- `data`

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
