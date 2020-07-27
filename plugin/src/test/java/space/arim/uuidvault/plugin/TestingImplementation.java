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

import java.util.UUID;

public class TestingImplementation extends SimpleImplementation {

	TestingImplementation() {

	}

	@Override
	protected boolean verifyNativePluginClass(Class<?> pluginClass) {
		return true;
	}

	@Override
	protected String getDescriptiveName(Class<?> pluginClass) {
		return "Plugin v1.0";
	}

	@Override
	protected void logException(String message, Throwable throwable) {
		System.err.println(message);
		throwable.printStackTrace();
	}

	@Override
	protected UUID resolveNativelyDirectly(String name) {
		return null;
	}

	@Override
	protected String resolveNativelyDirectly(UUID uuid) {
		return null;
	}
	
	// Re-overriding this ensures it is visible
	@Override
	protected void setInstance() {
		super.setInstance();
	}
	
}
