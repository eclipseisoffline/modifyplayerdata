execute unless data entity @s CustomData.Home run return run tellraw @s {"text":"No home set","color":"red"}
data modify entity @s Pos set from entity @s CustomData.Home
data modify entity @s Rotation set from entity @s CustomData.HomeRotation
tellraw @s "Teleported home"
