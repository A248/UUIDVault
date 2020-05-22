/* 
 * UUIDVault-spigot
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * UUIDVault-spigot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UUIDVault-spigot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with UUIDVault-spigot. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.uuidvault.plugin.spigot;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import space.arim.uuidvault.api.UUIDResolution;
import space.arim.uuidvault.plugin.SimpleImplementation;

public class UUIDVaultSpigot extends SimpleImplementation {

	private final Logger logger;
	
	UUIDVaultSpigot(JavaPlugin plugin) {
		super((cmd) -> Bukkit.getScheduler().runTaskAsynchronously(plugin, cmd));
		logger = plugin.getLogger();
	}
	
	@Override
	protected boolean verifyNativePluginClass(Class<?> pluginClass) {
		return !JavaPlugin.class.equals(pluginClass) && JavaPlugin.class.isAssignableFrom(pluginClass);
	}
	
	@Override
	protected void notifyException(UUIDResolution resolver, Throwable throwable) {
		logger.log(Level.WARNING, "Another plugin encountered an error while resolving a UUID/name:", throwable);
	}
	
	@Override
	protected UUID resolveNatively(String name) {
		Player player = Bukkit.getPlayer(name);
		if (player != null) {
			return player.getUniqueId();
		}
		for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
			if (offlinePlayer.getName().equalsIgnoreCase(name)) {
				return offlinePlayer.getUniqueId();
			}
		}
		return null;
	}

	@Override
	protected String resolveNatively(UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		if (player != null) {
			return player.getName();
		}
		for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
			if (offlinePlayer.getUniqueId().equals(uuid)) {
				return offlinePlayer.getName();
			}
		}
		return null;
	}
	
	// Re-overriding this ensures it is visible to UUIDVaultSpigotPlugin
	@Override
	protected void completeNativeStartup() {
		super.completeNativeStartup();
	}

}
