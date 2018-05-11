/*
 * Copyright (C) 2018  Michael Smith <selabie68@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.selabie68;

import com.google.inject.Inject;
import io.github.selabie68.config.OGUConfig;
import io.github.selabie68.listeners.BlockListener;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;

@Plugin(id = "oregenultimate", name = "OreGenUltimate", version = "0.0.2", description = "Plugin for Minecraft Sponge Servers that generates ore when lava mixes with water and would usually generate cobblestone.")
public class OreGenUltimate {
    @Inject
    public Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    public File configDir;

    private static OreGenUltimate instance;

    public OGUConfig oguConfig;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        instance = this;

        oguConfig = new OGUConfig();

        Sponge.getEventManager().registerListeners(this, new BlockListener());
    }

    public static OreGenUltimate get() {
        return instance;
    }

    @Listener
    public void reload(GameReloadEvent event) {
        oguConfig = new OGUConfig();
    }
}
