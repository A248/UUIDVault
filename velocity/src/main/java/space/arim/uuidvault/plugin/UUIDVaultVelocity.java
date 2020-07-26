/* 
 * UUIDVault-velocity
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * UUIDVault-velocity is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UUIDVault-velocity is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with UUIDVault-velocity. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.uuidvault.plugin;

import java.util.UUID;

import org.slf4j.Logger;

import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import space.arim.uuidvault.api.UUIDVault;
import space.arim.uuidvault.plugin.SimpleImplementation;

/**
 * Implementation of {@link UUIDVault} on the Velocity platform
 * 
 * @author A248
 *
 */
public class UUIDVaultVelocity extends SimpleImplementation {

	private final ProxyServer server;
	private final Logger logger;
	
	/**
	 * Creates the instance
	 * 
	 * @param server the velocity server
	 * @param logger the logger from a velocity plugin
	 */
	public UUIDVaultVelocity(ProxyServer server, Logger logger) {
		super(false);
		this.server = server;
		this.logger = logger;
	}

	private PluginContainer getPluginFor(Class<?> pluginClass) {
		for (PluginContainer plugin : server.getPluginManager().getPlugins()) {
			Object pluginObject = plugin.getInstance().orElse(null);
			if (pluginObject != null && pluginObject.getClass().equals(pluginClass)) {
				return plugin;
			}
		}
		return null;
	}
	
	@Override
	boolean verifyNativePluginClass(Class<?> pluginClass) {
		return pluginClass.getDeclaredAnnotation(Plugin.class) != null && getPluginFor(pluginClass) != null;
	}

	@Override
	String getDescriptiveName(Class<?> pluginClass) {
		PluginContainer plugin = getPluginFor(pluginClass);
		if (plugin == null) {
			// plugin was unloaded?
			return null;
		}
		PluginDescription description = plugin.getDescription();
		return description.getName().orElse(description.getId()) + " v" + description.getVersion().orElse(null);
	}

	@Override
	void logException(String message, Throwable throwable) {
		logger.warn(message, throwable);
	}

	@Override
	UUID resolveNativelyDirectly(String name) {
		Player player = server.getPlayer(name).orElse(null);
		return (player == null) ? null : player.getUniqueId();
	}

	@Override
	String resolveNativelyDirectly(UUID uuid) {
		Player player = server.getPlayer(uuid).orElse(null);
		return (player == null) ? null : player.getUsername();
	}
	
	void setInstance1() {
		setInstance();
	}

}
