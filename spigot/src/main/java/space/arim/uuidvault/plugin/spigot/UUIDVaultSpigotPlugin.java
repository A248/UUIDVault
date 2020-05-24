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

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import space.arim.uuidvault.api.UUIDVault;

public class UUIDVaultSpigotPlugin extends JavaPlugin {

	private UUIDVaultSpigot uvs;
	
	@Override
	public void onLoad() {
		uvs = new UUIDVaultSpigot(this);
		getServer().getServicesManager().register(UUIDVault.class, uvs, this, ServicePriority.Low);
	}
	
	@Override
	public void onEnable() {
		// Trigger startup completion
		getServer().getScheduler().runTaskLater(this, uvs::completeNativeStartup, 1L);
	}
	
}
