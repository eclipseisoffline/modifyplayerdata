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

| Minecraft Version | Status        |
|-------------------|---------------|
| 1.21.9            | ✅ Current     |
| 1.21.6+7+8        | ✔️ Available  |
| 1.21.5            | ✔️ Available  |
| 1.21.4            | ✔️ Available  |
| 1.21.2+3          | ✔️ Available  |
| 1.21+1            | ✅ Current     |
| 1.20.5+6          | ✔️ Available  |
| 1.20.4            | ✔️ Available  |
| 1.20.1            | ✔️ Available  |

I try to keep support up for the latest major and latest minor release of Minecraft. Updates to newer Minecraft
versions may be delayed from time to time, as I do not always have the time to immediately update my mods.

Unsupported versions are still available to download, but they won't receive new features or bugfixes.

## Usage

Mod builds can be found on the releases page, as well as on [Modrinth](https://modrinth.com/mod/modify-player-data).

The Fabric API is not required. This mod is not required on clients.

## Supported tags

NBT tags currently supported:

- `Pos`
- `Motion`
- `Rotation`
- `fall_distance`
- `Fire`
- `Air`
- `Invulnerable`
- `PortalCooldown`
- `Silent`[^1]
- `NoGravity`
- `Glowing`
- `TicksFrozen`
- `HasVisualFire`
- `Tags`
- `data`
- `Health`
- `HurtTime`[^1]
- `AbsorptionAmount`
- `attributes`
- `FallFlying`
- `last_hurt_by_player`
- `last_hurt_by_player_memory_time`
- `last_hurt_by_mob`
- `ticks_since_last_hurt_by_mob`
- `equipment`
- `LeftHanded`
- `Inventory`
- `SelectedItem`
- `SelectedItemSlot`
- `SleepTimer`
- `Score`
- `foodLevel`
- `foodTickTimer`
- `foodSaturationLevel`
- `foodExhaustionLevel`
- `abilities`
- `EnderItems`
- `current_explosion_impact_pos`
- `ignore_fall_damage_from_current_explosion`
- `current_impulse_context_reset_grace_time`
- `warden_spawn_tracker`
- `entered_nether_pos`
- `seenCredits`
- `respawn`
- `spawn_extra_particles_on_fall`
- `raid_omen_position`

NBT tags I won't add support for:

- `OnGround`
- `UUID`
- `CustomName`
- `CustomNameVisible`
- `Passengers`[^2]
- `DeathTime`
- `active_effects`[^3]
- `sleeping_pos`
- `Brain`
- `locator_bar_icon`[^4]
- `XpP`[^5]
- `XpLevel`[^5]
- `XpTotal`[^5]
- `XpSeed`[^6]
- `ShoulderEntityLeft`
- `ShoulderEntityRight`
- `LastDeathLocation`[^7]
- `playerGameType`
- `previousPlayerGameType`[^7]
- `RootVehicle`[^2]
- `recipeBook`[^8]
- `Dimension`
- `ender_pearls`

[^1]: Is implemented but does not do much due to limitations within the vanilla client
[^2]: Use the `/ride` command.
[^3]: Use the `/effect` command.
[^4]: Use the `/waypoint` command.
[^5]: Use the `/xp` command.
[^6]: I can't think of any use cases for this tag, but will add support on request.
[^7]: Unable to implement due to limitations within the vanilla client.
[^8]: Use the `/recipe` command.
