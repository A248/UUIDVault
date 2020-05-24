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

import java.lang.reflect.Modifier;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;

import space.arim.uuidvault.plugin.SimpleImplementation;

public class UUIDVaultBungee extends SimpleImplementation {

	private final Logger logger;
	
	UUIDVaultBungee(Plugin plugin) {
		super(false);
		logger = plugin.getLogger();
	}

	private Plugin getPluginFor(Class<?> pluginClass) {
		for (Plugin plugin : ProxyServer.getInstance().getPluginManager().getPlugins()) {
			if (plugin.getClass().equals(pluginClass)) {
				return plugin;
			}
		}
		return null;
	}
	
	@Override
	protected boolean verifyNativePluginClass(Class<?> pluginClass) {
		return !Plugin.class.equals(pluginClass) && Plugin.class.isAssignableFrom(pluginClass)
				&& !Modifier.isAbstract(pluginClass.getModifiers()) && getPluginFor(pluginClass) != null;
	}

	@Override
	protected String getDescriptiveName(Class<?> pluginClass) {
		Plugin plugin = getPluginFor(pluginClass);
		if (plugin == null) {
			// plugin was unloaded?
			return null;
		}
		PluginDescription description = plugin.getDescription();
		return description.getName() + " v" + description.getVersion();
	}
	
	@Override
	protected void logException(String message, Throwable throwable) {
		logger.log(Level.WARNING, message, throwable);
	}

	@Override
	protected UUID resolveNativelyDirectly(String name) {
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);
		return (player == null) ? null : player.getUniqueId();
	}

	@Override
	protected String resolveNativelyDirectly(UUID uuid) {
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
		return (player == null) ? null : player.getName();
	}
	
	// Re-overriding this ensures it is visible
	@Override
	protected void completeNativeStartup() {
		super.completeNativeStartup();
	}

}
