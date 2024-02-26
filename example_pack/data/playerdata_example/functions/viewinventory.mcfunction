# Runs cleanup and reset's inventory if already viewing an inventory
execute if entity @s[nbt={CustomData:{ViewingInventory:1b}}] run data modify entity @s Inventory set from entity @s CustomData.InventoryBackup
execute if entity @s[nbt={CustomData:{ViewingInventory:1b}}] run data remove entity @s CustomData.InventoryBackup
execute if entity @s[nbt={CustomData:{ViewingInventory:1b}}] run tellraw @s "Inventory reset"
execute if entity @s[nbt={CustomData:{ViewingInventory:1b}}] run return run data merge entity @s {CustomData:{ViewingInventory:0b}}

# Checks to see if the given entity is valid for viewing inventory of
$execute if entity @s[nbt={UUID:$(UUID)}] run return run tellraw @s {"text":"Can't view own inventory","color":"red"}
$execute unless entity @a[nbt={UUID:$(UUID)}] run return run tellraw @s {"text":"Can only view inventories of players","color":"red"}
$execute if entity @a[nbt={UUID:$(UUID),CustomData:{ViewingInventory:1b}}] run return run tellraw @s {"text":"That player is already viewing someone's inventory","color":"red"}
$execute if entity @a[nbt={CustomData:{ViewingInventory:1b,ViewingInventoryUUID:$(UUID)}}] run return run tellraw @s {"text":"That player's inventory is already being viewed","color":"red"}

# Starts viewing inventory
$data merge entity @s {CustomData:{ViewingInventory:1b,ViewingInventoryUUID:$(UUID)}}
data modify entity @s CustomData.InventoryBackup set from entity @s Inventory
$data modify entity @s Inventory set from entity @a[nbt={UUID:$(UUID)},limit=1] Inventory
$tellraw @s [{"text":"Viewing inventory of "},{"selector":"@a[nbt={UUID:$(UUID)},limit=1]"}]
