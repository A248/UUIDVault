/* 
 * UUIDVault-velocity
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * UUIDVault-velocity is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UUIDVault-velocity is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with UUIDVault-velocity. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU Lesser General Public License.
 */
package space.arim.uuidvault.plugin;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

@Plugin(id = PluginInfo.ANNOTATION_ID, name = PluginInfo.NAME, version = PluginInfo.VERSION, authors = {
		"A248" }, url = PluginInfo.URL, description = PluginInfo.DESCRIPTION)
public class UUIDVaultVelocityPlugin {
	
	@Inject
	public UUIDVaultVelocityPlugin(ProxyServer server, Logger logger) {
		UUIDVaultVelocity uvv = new UUIDVaultVelocity(server, logger);
		uvv.setInstance1();
	}
	
}
