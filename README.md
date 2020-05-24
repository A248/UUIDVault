# UUIDVault
Many Spigot plugins store UUID-name mappings. It's about time this practice was consolidated.

## What is it?

UUIDVault implements an API to bridge the various fragmented uuid-name caches. UUIDVault doesn't store UUIDs itself, because that would defeat the purpose by introducing yet another uuid/name resolver. Instead, UUIDVault hooks into various plugins to utilise their own caches.

## Advantages of UUIDVault

* Improved efficiency: you don't need multiple plugins storing UUID/name information.

* Server agnostic! It doesn't matter whether your plugin is for Spigot, BungeeCord, Sponge, or Velocity.
(This means if your project has a "common" or "shared" module, you can use UUIDVault directly there.)

* Highly documented. The javadoc is well-detailed. I honestly haven't seen a single Minecraft-related project with more documentation than UUIDVault. And even if you don't like the javadoc, nearly all of the information is also explained in the wiki.

More information: [How It Works](https://github.com/A248/UUIDVault/wiki/How-It-Works)

If you're still not convinced: [Why UUIDVault](https://github.com/A248/UUIDVault/wiki/Why-UUIDVault)

## Server Owners

See [Requirements and Installation](https://github.com/A248/UUIDVault/wiki/Requirements-and-Installation)

## Developers

### API Usage

The API is simple and clean, yet powerful. See [Developer Setup](https://github.com/A248/UUIDVault/wiki/Developer-Setup) and [API Usage](https://github.com/A248/UUIDVault/wiki/API-Usage)

### Implementing Plugins

See [Making an Implementation](https://github.com/A248/UUIDVault/wiki/Making-an-Implementation)

Even if your plugin is already implemented, you might want to have control of the implementation
to ensure it is up-to-date. If this is the case, feel free to create a Github issue / PR.

## License

See the file LICENSE.txt for the full GNU General Public License v3.
The license applies to this entire repository and is copied into binaries as LICENSE.txt.
