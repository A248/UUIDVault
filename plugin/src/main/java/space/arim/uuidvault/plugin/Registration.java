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

class Registration implements UUIDVaultRegistration, Comparable<Registration> {

	private final SimpleImplementation core;
	final Class<?> pluginClass;
	final UUIDResolution resolver;
	private volatile byte priority;
	
	Registration(SimpleImplementation core, Class<?> pluginClass, UUIDResolution resolver, byte priority) {
		this.core = core;
		this.pluginClass = pluginClass;
		this.resolver = resolver;
		this.priority = priority;
	}
	
	void changePriority(byte priority) {
		this.priority = priority;
	}
	
	@Override
	public boolean unregister() {
		return core.unregister(this);
	}

	@Override
	public int compareTo(Registration o) {
		return priority - o.priority;
	}

}
