# Copies inventory data over
$data modify entity @s Inventory set from entity @a[nbt={UUID:$(ViewingInventoryUUID)},limit=1] Inventory
