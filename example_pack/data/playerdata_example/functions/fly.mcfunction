execute store result score @s playerdata_example.fly run data get entity @s abilities.mayfly
execute if score @s playerdata_example.fly matches 1 run data merge entity @s {abilities:{mayfly:0b,flying:0b}}
execute if score @s playerdata_example.fly matches 0 run data merge entity @s {abilities:{mayfly:1b}}
tellraw @s "Toggled flight"
