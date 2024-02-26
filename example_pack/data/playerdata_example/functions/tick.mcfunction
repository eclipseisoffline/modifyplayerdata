# Run inventory tick function for everyone viewing someone's inventory
execute as @a[nbt={CustomData:{ViewingInventory:1b}}] run function playerdata_example:viewinventoryplayertick with entity @s CustomData
