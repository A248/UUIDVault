/* 
 * UUIDVault-bungee
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * UUIDVault-bungee is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UUIDVault-bungee is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with UUIDVault-bungee. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.uuidvault.plugin.bungee;

import java.util.concurrent.atomic.AtomicBoolean;

import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class UUIDVaultBungeePlugin extends Plugin implements Listener {

	private UUIDVaultBungee uvb;
	
	private final AtomicBoolean hasStarted = new AtomicBoolean(false);
	
	@Override
	public void onLoad() {
		uvb = new UUIDVaultBungee(this);
	}
	
	@Override
	public void onEnable() {
		getProxy().getPluginManager().registerListener(this, this);
	}
	
	// This is how startup completion must be detected on BungeeCord
	@EventHandler(priority = (byte) -128)
	public void triggerStartupCompletion(@SuppressWarnings("unused") PreLoginEvent evt) {
		if (hasStarted.compareAndSet(false, true)) {
			uvb.completeNativeStartup();
		}
	}
	
}
