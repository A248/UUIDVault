/* 
 * UUIDVault-sponge
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * UUIDVault-sponge is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UUIDVault-sponge is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with UUIDVault-sponge. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU Lesser General Public License.
 */
package space.arim.uuidvault.plugin;

import java.util.UUID;

import org.slf4j.Logger;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import space.arim.uuidvault.api.UUIDVault;

/**
 * Implementation of {@link UUIDVault} on the Sponge platform
 * 
 * @author A248
 *
 */
public class UUIDVaultSponge extends SimpleImplementation {

	private final Logger logger;
	
	/**
	 * Creates the instance
	 * 
	 * @param plugin the sponge plugin to use
	 */
	public UUIDVaultSponge(PluginContainer plugin) {
		logger = plugin.getLogger();
	}
	
	private PluginContainer getPluginFor(Class<?> pluginClass) {
		for (PluginContainer plugin : Sponge.getPluginManager().getPlugins()) {
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
		return plugin.getName() + " v" + plugin.getVersion().orElse(null);
	}

	@Override
	void logException(String message, Throwable throwable) {
		logger.warn(message, throwable);
	}
	
	@Override
	public boolean mustCallNativeResolutionSync() {
		return true;
	}

	@Override
	UUID resolveNativelyDirectly(String name) {

		Player player = Sponge.getServer().getPlayer(name).orElse(null);
		return (player == null) ? null : player.getUniqueId();
	}

	@Override
	String resolveNativelyDirectly(UUID uuid) {

		Player player = Sponge.getServer().getPlayer(uuid).orElse(null);
		return (player == null) ? null : player.getName();
	}
	
	void setInstance1() {
		setInstance();
	}
	
}
