/* 
 * UUIDVault-plugin
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * UUIDVault-plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UUIDVault-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with UUIDVault-plugin. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU Lesser General Public License.
 */
package space.arim.uuidvault.plugin;

import space.arim.uuidvault.api.UUIDResolver;
import space.arim.uuidvault.api.UUIDVaultRegistration;

class Registration implements UUIDVaultRegistration, Comparable<Registration> {

	final Class<?> pluginClass;
	final UUIDResolver resolver;
	private final byte priority;
	final String name;
	
	Registration(Class<?> pluginClass, UUIDResolver resolver, byte priority, String name) {
		this.pluginClass = pluginClass;
		this.resolver = resolver;
		this.priority = priority;
		this.name = name;
	}

	@Override
	public int compareTo(Registration o) {
		// Higher priorities first
		int priorityDiff = o.priority - priority;
		if (priorityDiff == 0) {
			// Same priority, different registration
			return o.pluginClass.hashCode() - pluginClass.hashCode();
		}
		return priorityDiff;
	}
	
	@Override
	public String toString() {
		return "Registration [pluginClass=" + pluginClass + ", resolver=" + resolver + ", priority=" + priority
				+ ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pluginClass.hashCode();
		result = prime * result + priority;
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Registration)) {
			return false;
		}
		Registration other = (Registration) object;
		return priority == other.priority && pluginClass == other.pluginClass;
	}

}
