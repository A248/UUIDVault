/* 
 * UUIDVault-bungee
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * UUIDVault-bungee is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UUIDVault-bungee is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with UUIDVault-bungee. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU Lesser General Public License.
 */
package space.arim.uuidvault.plugin;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class UUIDVaultBungeePlugin extends Plugin implements Listener {
	
	@Override
	public void onLoad() {
		UUIDVaultBungee uvb = new UUIDVaultBungee(this);
		uvb.setInstance1();
	}
	
}
