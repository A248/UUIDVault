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
package space.arim.uuidvault.plugin;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = PluginInfo.ANNOTATION_ID, name = PluginInfo.NAME, version = PluginInfo.VERSION, authors = {
		"A248" }, url = PluginInfo.URL, description = PluginInfo.DESCRIPTION)
public class UUIDVaultSpongePlugin {
	
	@Listener
	public void onEnable(@SuppressWarnings("unused") GamePostInitializationEvent evt) {
		UUIDVaultSponge uvs = new UUIDVaultSponge(Sponge.getPluginManager().fromInstance(this).get());
		uvs.setInstance1();
	}
	
}
