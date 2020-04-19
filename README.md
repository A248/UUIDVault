# UUIDVault
Many Spigot plugins store UUID-name mappings. It's about time this practice was consolidated.

## Introduction

See [Why UUIDVault](https://github.com/A248/UUIDVault/wiki/Why-UUIDVault)

## Advantages of UUIDVault

* Improved efficiency: you don't need multiple plugins storing UUID/name information.

* Server agnostic! It doesn't matter whether your plugin is for Spigot, BungeeCord, Sponge, or Velocity.
(This means if your project has a "common" or "shared" implementation, you can use UUIDVault directly there.)

* Highly documented. The javadoc has almost as much detail as the JDK. I haven't ever seen a single
Minecraft-related project with more documentation than UUIDVault to date. And even if you don't like the javadoc,
nearly all of the information is also explained in the wiki.

## Server Owners

See [Requirements and Installation](https://github.com/A248/UUIDVault/wiki/Requirements-and-Installation)

## Developers

### API Usage

See [API Usage](https://github.com/A248/UUIDVault/wiki/API-Usage)

### Implementing Plugins

See [Making an Implementation](https://github.com/A248/UUIDVault/wiki/Making-an-Implementation)

Even if your plugin is already implemented, you might want to have control of the implementation
yourself so that you can make API changes and have your implementation up-to-date. If this is the case,
create a Github issue and include in it your implementation.

## License

See the file LICENSE.txt for the full GNU General Public License v3.
The license applies to this entire repository and is copied into binaries as LICENSE.txt.
