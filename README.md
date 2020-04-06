# UUIDVault
Many Spigot plugins store UUID-name mappings. It's about time this practice was consolidated.

## Introduction

### What are UUIDs?

Every Minecraft player has a UUID, or a unique user identifier. UUIDs ensure that if you change your name,
you don't lose all of your statistics and playerdata. If servers tracked data by player name, changing
your name would reset your data.

A UUID looks like this: `ed5f12cd-6007-45d9-a4b9-940524ddaecf`

### Why UUIDVault

One day, I was making a simple plugin which stored player data by UUID. However, I needed to find the name
corresponding to the UUID to display in messages. It would be quite inconvenient to receive a chat message
with another player's UUID.

I knew I could just use the server's internal UUID/name storage. Every Spigot server has a file called
the `uuidcache.json` which stores UUID-name mappings. Plugin developers can utilise this by calling
`Bukkit.getOfflinePlayers();` and iterating over the array.

However, the server's inbuilt cache entries expire after a month. This is all fine for regular operation
of the server, but what if I want persistent storage? Some plugins track players by UUIDs and need to be able
to display the player's name regardless. For example, a ban plugin with a /banlist command would ideally be able
to display month-old bans using player display names.

Also, plugins which take a player name as a command parameter need to find the player's UUID when storing data.
If the player is online, the UUID can be found easily. But if the player is offline and hasn't joined in other a
month, the plugin can't find the player's UUID so easily.

Furthermore, server owners sometimes delete the usercache.json. The usercache can also become corrupt
if your server didn't shutdown properly. Because of this, many plugins maintain their own UUID-name cache.

## Consolidating

If each plugin had its own uuid-name cache, my server would have over 70 such caches. Obviously, most plugins
don't use such a cache. However, you can see that having each plugin store uuid-name mappings is quite wasteful.

To solve this, UUIDVault implements an API to bridge the various fragmented uuid-name caches. UUIDVault doesn't
store UUIDs itself, because that would defeat the purpose by introducing yet another cache. Instead,
UUIDVault hooks into various plugins to utilise their own caches.

Developers can rely on UUIDVault when they want a uuid-name mapping. UUIDVault will draw on information
from all the plugins it is hooked into to find a mapping. This way, you can easily use uuid-name mappings
without having to create your own uuid-name cache.

## Questions

### Why not query the Mojang API?

If every plugin queried the Mojang API, the server would quickly encounter Mojang's rate limit.
This would be disastrous, because it would mean players wouldn't be authenticated.

Furthermore, some servers run in offline/cracked mode, so their players' UUIDs are not the same UUIDs
as in Mojang's official API.

### Why not directly rely on X plugin?

Relying on a single plugin for uuid-name mappings is highly discouraged. This is because of the concept
of *coupling*. Directly coupling your plugin to another plugin which is not related logically to yours
can have deleterious effects.

If you ever want to remove that plugin, you're going to have to do a lot of refactoring. This is why
when I see plugins which directly rely on Essentials, and whose features are completely unrelated
to that of Essentials, I'm disappointed. (I don't use Essentials myself, either)

Instead, it is best to rely on *abstraction*. This means that your use of uuid-name mappings is not specifically
connected to any single plugin or library. It enables your program to be flexible and work with all sorts of
implementations. UUIDVault does all of this abstraction for you.
