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

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import space.arim.uuidvault.api.UUIDResolution;

public class MutuallyUpdatingImplementation extends ImplementationHelper {

	public MutuallyUpdatingImplementation(JavaPlugin plugin) {
		super(plugin);
	}

	@Override
	UUID resolveAsynced(String name) {
		HashSet<UUIDResolution> updateMe = new HashSet<UUIDResolution>();
		for (UUIDResolution resolver : resolvers.values()) {

			UUID uuid = resolver.resolve(name).join();
			if (uuid != null) {
				updateMe.forEach((didntFind) -> didntFind.update(uuid, name, false));
				return uuid;
			}
			updateMe.add(resolver);
		}
		return null;
	}
	
	@Override
	String resolveAsynced(UUID uuid) {
		HashSet<UUIDResolution> updateMe = new HashSet<UUIDResolution>();
		for (UUIDResolution resolver : resolvers.values()) {

			String name = resolver.resolve(uuid).join();
			if (name != null) {
				updateMe.forEach((didntFind) -> didntFind.update(uuid, name, false));
				return name;
			}
			updateMe.add(resolver);
		}
		return null;
	}
	
}
