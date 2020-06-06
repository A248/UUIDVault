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

import space.arim.uuidvault.api.UUIDResolver;
import space.arim.uuidvault.api.UUIDVaultRegistration;

class Registration implements UUIDVaultRegistration, Comparable<Registration> {

	private final SimpleImplementation core;
	final Class<?> pluginClass;
	final UUIDResolver resolver;
	private final byte priority;
	final String name;
	
	Registration(SimpleImplementation core, Class<?> pluginClass, UUIDResolver resolver, byte priority, String name) {
		this.core = core;
		this.pluginClass = pluginClass;
		this.resolver = resolver;
		this.priority = priority;
		this.name = name;
	}
	
	@Override
	public boolean unregister() {
		return core.unregister(this);
	}

	@Override
	public int compareTo(Registration o) {
		// Higher priorities first
		return o.priority - priority;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pluginClass.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Registration)) {
			return false;
		}
		Registration other = (Registration) obj;
		return pluginClass.equals(other.pluginClass);
	}

}
