# Riddlechests Datapacks

Including all the riddles into the main jar file would be way to much.
And you don't WANT to load all the different riddles, since they are stored
as server-side recipe data and will be transferred to connecting clients.
A small subset, say one or two categories from this list are probably enough.

Each of these packs contains between 100 and 600 questions. Maybe you want
to auto-rotate them on a weekly basis or something to avoid repetitions.

They all bring a loottable mapping to the riddlechests:default_loot loottable.
Either change that loot table or change the mapping to one of your own tables,
if you want different loot to be created in the chests.