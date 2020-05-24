/* 
 * UUIDVault-sponge
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * UUIDVault-sponge is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UUIDVault-sponge is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with UUIDVault-sponge. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.uuidvault.plugin.sponge;

import java.util.UUID;

import org.slf4j.Logger;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.profile.GameProfile;
import space.arim.uuidvault.plugin.SimpleImplementation;

public class UUIDVaultSponge extends SimpleImplementation {

	private final Logger logger;
	
	UUIDVaultSponge(PluginContainer plugin) {
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
	protected boolean verifyNativePluginClass(Class<?> pluginClass) {
		return pluginClass.getDeclaredAnnotation(Plugin.class) != null && getPluginFor(pluginClass) != null;
	}

	@Override
	protected String getDescriptiveName(Class<?> pluginClass) {
		PluginContainer plugin = getPluginFor(pluginClass);
		if (plugin == null) {
			// plugin was unloaded?
			return null;
		}
		return plugin.getName() + " v" + plugin.getVersion().orElse(null);
	}

	@Override
	protected void logException(String message, Throwable throwable) {
		logger.warn(message, throwable);
	}

	@Override
	protected UUID resolveNatively(String name) {
		if (!Sponge.getServer().isMainThread()) return null;
		
		Player player = Sponge.getServer().getPlayer(name).orElse(null);
		if (player != null) {
			return player.getUniqueId();
		}
		GameProfile profile = Sponge.getServer().getGameProfileManager().getCache().getByName(name).orElse(null);
		return (profile == null) ? null : profile.getUniqueId();
	}

	@Override
	protected String resolveNatively(UUID uuid) {
		if (!Sponge.getServer().isMainThread()) return null;
		
		Player player = Sponge.getServer().getPlayer(uuid).orElse(null);
		if (player != null) {
			return player.getName();
		}
		GameProfile profile = Sponge.getServer().getGameProfileManager().getCache().getById(uuid).orElse(null);
		return (profile == null) ? null : profile.getName().orElse(null);
	}
	
}
