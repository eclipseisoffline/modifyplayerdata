execute if entity @s[nbt={CustomData:{ViewingInventory:1b}}] run data modify entity @s Inventory set from entity @s CustomData.InventoryBackup
execute if entity @s[nbt={CustomData:{ViewingInventory:1b}}] run tellraw @s "Inventory reset"
execute if entity @s[nbt={CustomData:{ViewingInventory:1b}}] run return run data merge entity @s {CustomData:{ViewingInventory:0b}}

execute unless entity @a[sort=nearest,limit=1,distance=0.1..10] run return run tellraw @s {"text":"No nearby player was found","color":"red"}

data merge entity @s {CustomData:{ViewingInventory:1b}}
data modify entity @s CustomData.InventoryBackup set from entity @s Inventory
data modify entity @s Inventory set from entity @a[sort=nearest,limit=1,distance=0.1..10] Inventory
tellraw @s [{"text":"Viewing inventory of "},{"selector":"@a[sort=nearest,limit=1,distance=0.1..10]"}]
