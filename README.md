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
- `fall_distance`
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
- `Silent`[^7]
- `TicksFrozen`
- `AbsorptionAmount`
- `attributes`
- `equipment`
- `FallFlying`
- `Health`
- `HurtTime`[^7]
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
- `active_effects`[^2]
- `DeathTime`
- `HurtByTimestamp`[^8]
- `Team`[^5]
- `sleeping_pos`
- `Brain`
- `last_hurt_by_player`[^8]
- `last_hurt_by_player_memory_time`[^8]
- `last_hurt_by_mob`[^8]
- `ticks_since_last_hurt_by_mob`[^8]
- `XpLevel`[^4]
- `XpP`[^4]
- `XpSeed`[^8]
- `XpTotal`[^4]
- `ShoulderEntityLeft`
- `ShoulderEntityRight`
- `LastDeathLocation`[^6]
- `current_explosion_impact_pos` [^8]
- `ignore_fall_damage_from_current_explosion` [^8]
- `current_impulse_context_reset_grace_time` [^8]
- `entered_nether_pos`[^8]
- `respawn` [^3]
- `spawn_extra_particles_on_fall` [^8]
- `raid_omen_position` [^8]

[^1]: Use the `/ride` command.
[^2]: Use the `/effect` command.
[^3]: Use the `/spawnpoint` command.
[^4]: Use the `/xp` command.
[^5]: Use the `/team` command.
[^6]: Unable to implement due to limitations within the vanilla client.
[^7]: Is implemented but does not do much due to limitations within the vanilla client
[^8]: I can't think of any use cases for this tag, but will add support on request.
