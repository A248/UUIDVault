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

import space.arim.uuidvault.api.UUIDResolution;
import space.arim.uuidvault.api.UUIDVaultRegistration;

class Registration implements UUIDVaultRegistration {

	private final SimpleImplementation core;
	private final Class<?> pluginClass;
	private final UUIDResolution resolver;
	
	Registration(SimpleImplementation core, Class<?> pluginClass, UUIDResolution resolver) {
		this.core = core;
		this.pluginClass = pluginClass;
		this.resolver = resolver;
	}
	
	@Override
	public boolean unregister() {
		return core.unregister(pluginClass, resolver);
	}

}
