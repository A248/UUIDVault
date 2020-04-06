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

import org.bukkit.plugin.java.JavaPlugin;

import space.arim.uuidvault.api.UUIDResolution;
import space.arim.uuidvault.api.UUIDVaultRegistration;

class RegistrationImpl implements UUIDVaultRegistration {

	private final ImplementationHelper core;
	private final JavaPlugin plugin;
	private final UUIDResolution resolver;
	
	RegistrationImpl(ImplementationHelper core, JavaPlugin plugin, UUIDResolution resolver) {
		this.core = core;
		this.plugin = plugin;
		this.resolver = resolver;
	}
	
	@Override
	public boolean unregister() {
		return core.unregister(plugin, resolver);
	}

}
