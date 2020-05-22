/* 
 * UUIDVault-bungee
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * UUIDVault-bungee is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UUIDVault-bungee is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with UUIDVault-bungee. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.uuidvault.plugin.bungee;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import space.arim.uuidvault.api.UUIDResolution;
import space.arim.uuidvault.plugin.SimpleImplementation;

public class UUIDVaultBungee extends SimpleImplementation {

	private final Logger logger;
	
	UUIDVaultBungee(Plugin plugin) {
		super((cmd) -> ProxyServer.getInstance().getScheduler().runAsync(plugin, cmd));
		logger = plugin.getLogger();
	}

	@Override
	protected boolean verifyNativePluginClass(Class<?> pluginClass) {
		return !Plugin.class.equals(pluginClass) && Plugin.class.isAssignableFrom(pluginClass);
	}

	@Override
	protected void notifyException(UUIDResolution resolver, Throwable throwable) {
		logger.log(Level.WARNING, "Another plugin encountered an error while resolving a UUID/name:", throwable);
	}

	@Override
	protected UUID resolveNatively(String name) {
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);
		return (player == null) ? null : player.getUniqueId();
	}

	@Override
	protected String resolveNatively(UUID uuid) {
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
		return (player == null) ? null : player.getName();
	}
	
	// Re-overriding this ensures it is visible to UUIDVaultBungeePlugin
	@Override
	protected void completeNativeStartup() {
		super.completeNativeStartup();
	}

}
