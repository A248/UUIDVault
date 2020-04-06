/* 
 * UUIDVault-plugin
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * UUIDVault-plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UUIDVault-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with UUIDVault-plugin. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.uuidvault.plugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import space.arim.uuidvault.api.UUIDResolution;
import space.arim.uuidvault.api.UUIDVault;
import space.arim.uuidvault.api.UUIDVaultRegistration;

public abstract class ImplementationHelper extends UUIDVault {

	private final Executor bukkitAsyncExecutor;
	final ConcurrentMap<JavaPlugin, UUIDResolution> resolvers = new ConcurrentHashMap<>();

	ImplementationHelper(JavaPlugin plugin) {
		bukkitAsyncExecutor = (cmd) -> Bukkit.getScheduler().runTaskAsynchronously(plugin, cmd);
	}

	@Override
	public UUIDVaultRegistration register(UUIDResolution resolver, JavaPlugin plugin) {
		UUIDResolution existing = resolvers.putIfAbsent(plugin, resolver);
		return (existing == null) ?  new RegistrationImpl(this, plugin, resolver) : null;
	}

	boolean unregister(JavaPlugin plugin, UUIDResolution resolver) {
		return resolvers.remove(plugin, resolver);
	}

	@Override
	public CompletableFuture<UUID> resolve(String name) {
		UUID immediate = resolveImmediately(name);
		return (immediate != null) ? CompletableFuture.completedFuture(immediate)
				: CompletableFuture.supplyAsync(() -> resolveAsynced(name), bukkitAsyncExecutor);
	}

	@Override
	public UUID resolveImmediately(String name) {
		Player player = Bukkit.getPlayer(name);
		if (player != null) {
			return player.getUniqueId();
		}
		for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
			if (offlinePlayer.getName().equalsIgnoreCase(name)) {
				return offlinePlayer.getUniqueId();
			}
		}
		for (UUIDResolution resolver : resolvers.values()) {
			UUID uuid = resolver.resolveImmediately(name);
			if (uuid != null) {
				return uuid;
			}
		}
		return null;
	}
	
	@Override
	public CompletableFuture<String> resolve(UUID uuid) {
		String immediate = resolveImmediately(uuid);
		return (immediate != null) ? CompletableFuture.completedFuture(immediate)
				: CompletableFuture.supplyAsync(() -> resolveAsynced(uuid), bukkitAsyncExecutor);
	}
	
	@Override
	public String resolveImmediately(UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		if (player != null) {
			return player.getName();
		}
		for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
			if (offlinePlayer.getUniqueId().equals(uuid)) {
				return offlinePlayer.getName();
			}
		}
		for (UUIDResolution resolver : resolvers.values()) {
			String name = resolver.resolveImmediately(uuid);
			if (name != null) {
				return name;
			}
		}
		return null;
	}

	abstract UUID resolveAsynced(String name);

	abstract String resolveAsynced(UUID uuid);

}
