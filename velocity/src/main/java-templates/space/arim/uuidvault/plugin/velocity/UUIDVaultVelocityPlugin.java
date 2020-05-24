/* 
 * UUIDVault-velocity
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * UUIDVault-velocity is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UUIDVault-velocity is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with UUIDVault-velocity. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.uuidvault.plugin.velocity;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

@Plugin(id = "${plugin.annotationId}", name = "${plugin.name}", version = "${plugin.version}", authors = {
		"${plugin.author}" }, url = "${plugin.url}", description = "${plugin.description}")
public class UUIDVaultVelocityPlugin {
	
	private UUIDVaultVelocity uvv;
	
	@Inject
	public UUIDVaultVelocityPlugin(ProxyServer server, Logger logger) {
		uvv = new UUIDVaultVelocity(server, logger);
	}
	
}
