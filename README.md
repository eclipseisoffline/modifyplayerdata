# Modify Player Data

![version badge](https://img.shields.io/badge/dynamic/json?url=https%3A%2F%2Fapi.modrinth.com%2Fv2%2Fproject%2Fe706DYY5%2Fversion&query=%24%5B0%5D.version_number&label=version&color=green)
[![downloads badge](https://img.shields.io/badge/dynamic/json?url=https%3A%2F%2Fapi.modrinth.com%2Fv2%2Fproject%2Fe706DYY5&query=%24.downloads&logo=data%3Aimage%2Fsvg%2Bxml%3Bbase64%2CPHN2ZyB3aWR0aD0iNTEyIiBoZWlnaHQ9IjUxNCIgdmlld0JveD0iMCAwIDUxMiA1MTQiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI%2BCiAgPHBhdGggZmlsbC1ydWxlPSJldmVub2RkIiBjbGlwLXJ1bGU9ImV2ZW5vZGQiIGQ9Ik01MDMuMTYgMzIzLjU2QzUxNC41NSAyODEuNDcgNTE1LjMyIDIzNS45MSA1MDMuMiAxOTAuNzZDNDY2LjU3IDU0LjIyOTkgMzI2LjA0IC0yNi44MDAxIDE4OS4zMyA5Ljc3OTkxQzgzLjgxMDEgMzguMDE5OSAxMS4zODk5IDEyOC4wNyAwLjY4OTk0MSAyMzAuNDdINDMuOTlDNTQuMjkgMTQ3LjMzIDExMy43NCA3NC43Mjk4IDE5OS43NSA1MS43MDk4QzMwNi4wNSAyMy4yNTk4IDQxNS4xMyA4MC42Njk5IDQ1My4xNyAxODEuMzhMNDExLjAzIDE5Mi42NUMzOTEuNjQgMTQ1LjggMzUyLjU3IDExMS40NSAzMDYuMyA5Ni44MTk4TDI5OC41NiAxNDAuNjZDMzM1LjA5IDE1NC4xMyAzNjQuNzIgMTg0LjUgMzc1LjU2IDIyNC45MUMzOTEuMzYgMjgzLjggMzYxLjk0IDM0NC4xNCAzMDguNTYgMzY5LjE3TDMyMC4wOSA0MTIuMTZDMzkwLjI1IDM4My4yMSA0MzIuNCAzMTAuMyA0MjIuNDMgMjM1LjE0TDQ2NC40MSAyMjMuOTFDNDY4LjkxIDI1Mi42MiA0NjcuMzUgMjgxLjE2IDQ2MC41NSAzMDguMDdMNTAzLjE2IDMyMy41NloiIGZpbGw9IiMxYmQ5NmEiLz4KICA8cGF0aCBkPSJNMzIxLjk5IDUwNC4yMkMxODUuMjcgNTQwLjggNDQuNzUwMSA0NTkuNzcgOC4xMTAxMSAzMjMuMjRDMy44NDAxMSAzMDcuMzEgMS4xNyAyOTEuMzMgMCAyNzUuNDZINDMuMjdDNDQuMzYgMjg3LjM3IDQ2LjQ2OTkgMjk5LjM1IDQ5LjY3OTkgMzExLjI5QzUzLjAzOTkgMzIzLjggNTcuNDUgMzM1Ljc1IDYyLjc5IDM0Ny4wN0wxMDEuMzggMzIzLjkyQzk4LjEyOTkgMzE2LjQyIDk1LjM5IDMwOC42IDkzLjIxIDMwMC40N0M2OS4xNyAyMTAuODcgMTIyLjQxIDExOC43NyAyMTIuMTMgOTQuNzYwMUMyMjkuMTMgOTAuMjEwMSAyNDYuMjMgODguNDQwMSAyNjIuOTMgODkuMTUwMUwyNTUuMTkgMTMzQzI0NC43MyAxMzMuMDUgMjM0LjExIDEzNC40MiAyMjMuNTMgMTM3LjI1QzE1Ny4zMSAxNTQuOTggMTE4LjAxIDIyMi45NSAxMzUuNzUgMjg5LjA5QzEzNi44NSAyOTMuMTYgMTM4LjEzIDI5Ny4xMyAxMzkuNTkgMzAwLjk5TDE4OC45NCAyNzEuMzhMMTc0LjA3IDIzMS45NUwyMjAuNjcgMTg0LjA4TDI3OS41NyAxNzEuMzlMMjk2LjYyIDE5Mi4zOEwyNjkuNDcgMjE5Ljg4TDI0NS43OSAyMjcuMzNMMjI4Ljg3IDI0NC43MkwyMzcuMTYgMjY3Ljc5QzIzNy4xNiAyNjcuNzkgMjUzLjk1IDI4NS42MyAyNTMuOTggMjg1LjY0TDI3Ny43IDI3OS4zM0wyOTQuNTggMjYwLjc5TDMzMS40NCAyNDkuMTJMMzQyLjQyIDI3My44MkwzMDQuMzkgMzIwLjQ1TDI0MC42NiAzNDAuNjNMMjEyLjA4IDMwOC44MUwxNjIuMjYgMzM4LjdDMTg3LjggMzY3Ljc4IDIyNi4yIDM4My45MyAyNjYuMDEgMzgwLjU2TDI3Ny41NCA0MjMuNTVDMjE4LjEzIDQzMS40MSAxNjAuMSA0MDYuODIgMTI0LjA1IDM2MS42NEw4NS42Mzk5IDM4NC42OEMxMzYuMjUgNDUxLjE3IDIyMy44NCA0ODQuMTEgMzA5LjYxIDQ2MS4xNkMzNzEuMzUgNDQ0LjY0IDQxOS40IDQwMi41NiA0NDUuNDIgMzQ5LjM4TDQ4OC4wNiAzNjQuODhDNDU3LjE3IDQzMS4xNiAzOTguMjIgNDgzLjgyIDMyMS45OSA1MDQuMjJaIiBmaWxsPSIjMWJkOTZhIi8%2BCjwvc3ZnPg%3D%3D&label=downloads&color=green)](https://modrinth.com/mod/modify-player-data)

This mod allows Minecraft's data commands (`/data`, `/execute store`, etc.) to modify player data.

Alongside supporting most vanilla player NBT tags, this mod also adds the `CustomData` NBT tag,
which can be used to store any type of NBT data on a per-player basis.

Feel free to report any bugs, or suggest new features, at the issue tracker.

## License

This mod is licensed under GNU GPLv3.

## Usage

Mod builds can be found [here](https://github.com/eclipseisoffline/modifyplayerdata/packages/2069488) and on [Modrinth](https://modrinth.com/mod/modify-player-data).

This mod is currently available for Minecraft 1.20.5+6 and 1.20.4 (no longer updated).
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
