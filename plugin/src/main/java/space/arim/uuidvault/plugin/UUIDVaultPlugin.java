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

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import space.arim.uuidvault.api.UUIDVault;
import space.arim.uuidvault.hooks.PluginHooks;

public class UUIDVaultPlugin extends JavaPlugin {

	@Override
	public void onLoad() {
		/*
		 * Using onLoad so that we're sure to load before other plugins.
		 * Combined with load: STARTUP in plugin.yml, this ensures that
		 * most developers don't need softdepend: [UUIDVaultPlugin]
		 */
		saveDefaultConfig();
		getServer().getServicesManager().register(UUIDVault.class,
				getConfig().getBoolean("mutual-updating", false) ? new MutuallyUpdatingImplementation(this)
						: new SimpleImplementation(this),
				this, ServicePriority.Low);
	}

	@Override
	public void onEnable() {
		// registering hooks, we want to register after the server has finished starting
		getServer().getScheduler().runTaskLater(this, PluginHooks::registerAll, 3L);
	}

}
