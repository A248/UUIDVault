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

import java.lang.reflect.Modifier;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import space.arim.uuidvault.api.UUIDVault;
import space.arim.uuidvault.plugin.SimpleImplementation;

/**
 * Implementation of {@link UUIDVault} on the Spigot platform
 * 
 * @author A248
 *
 */
public final class UUIDVaultSpigot extends SimpleImplementation {

	private final Logger logger;
	
	/**
	 * Creates the instance. Use {@link #setInstance()} to set this instance
	 * as the global instance
	 * 
	 * @param plugin the bukkit plugin to use
	 */
	public UUIDVaultSpigot(JavaPlugin plugin) {
		super(true);
		logger = plugin.getLogger();
	}
	
	private JavaPlugin getPluginFor(Class<?> pluginClass) {
		// Not using JavaPlugin#getPlugin because it may throw exceptions for invalid plugin classes
		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			if (plugin instanceof JavaPlugin && plugin.getClass().equals(pluginClass)) {
				return (JavaPlugin) plugin;
			}
		}
		return null;
	}
	
	@Override
	protected boolean verifyNativePluginClass(Class<?> pluginClass) {
		return !JavaPlugin.class.equals(pluginClass) && JavaPlugin.class.isAssignableFrom(pluginClass)
				&& !Modifier.isAbstract(pluginClass.getModifiers()) && getPluginFor(pluginClass) != null;
	}
	
	@Override
	protected String getDescriptiveName(Class<?> pluginClass) {
		JavaPlugin plugin = getPluginFor(pluginClass);
		if (plugin == null) {
			// plugin was unloaded?
			return null;
		}
		return plugin.getDescription().getFullName();
	}
	
	@Override
	protected void logException(String message, Throwable throwable) {
		logger.log(Level.WARNING, message, throwable);
	}
	
	@Override
	protected UUID resolveNativelyDirectly(String name) {

		Player player = Bukkit.getPlayerExact(name);
		return (player == null) ? null : player.getUniqueId();
	}

	@Override
	protected String resolveNativelyDirectly(UUID uuid) {

		Player player = Bukkit.getPlayer(uuid);
		return (player == null) ? null : player.getName();
	}
	
	/**
	 * Sets the global UUIDVault instance, retrievable by {@link UUIDVault#get()}, to this instance. <br>
	 * <br>
	 * If the global instance is already set, an unchecked exception is thrown.
	 * 
	 */
	@Override
	public void setInstance() {
		super.setInstance();
	}

}
