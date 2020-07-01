/* 
 * UUIDVault-sponge
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * UUIDVault-sponge is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UUIDVault-sponge is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with UUIDVault-sponge. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.uuidvault.plugin.sponge;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "${plugin.annotationId}", name = "${plugin.name}", version = "${plugin.version}", authors = {
		"${plugin.author}" }, url = "${plugin.url}", description = "${plugin.description}")
public class UUIDVaultSpongePlugin {
	
	@Listener
	public void onEnable(@SuppressWarnings("unused") GamePostInitializationEvent evt) {
		UUIDVaultSponge uvs = new UUIDVaultSponge(Sponge.getPluginManager().fromInstance(this).get());
		uvs.setInstance();
	}
	
}
