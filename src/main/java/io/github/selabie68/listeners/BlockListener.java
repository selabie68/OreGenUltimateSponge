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

package io.github.selabie68.listeners;

import io.github.selabie68.OreGenUltimate;
import io.github.selabie68.utils.RandomCollection;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Iterator;
import java.util.Map;

public class BlockListener {
    private Map<BlockType, Double> blockList;
    private RandomCollection randomiser;

    public BlockListener() {
        OreGenUltimate.get().logger.info("blocks", "Loaded BlockListener...");

        this.blockList = OreGenUltimate.get().oguConfig.getBlocks();
        this.randomiser = new RandomCollection();

        Iterator it = blockList.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry b = (Map.Entry)it.next();
            OreGenUltimate.get().logger.info("Loaded: " + b.getKey() + " with " + b.getValue() + "% Chance");
            this.randomiser.add((Double) b.getValue(), (BlockType) b.getKey());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    @Listener
    public void onCobbleGen(ChangeBlockEvent.Modify e) {
        BlockSnapshot bto = e.getTransactions().get(0).getOriginal();

        Cause cause = e.getCause();

        if (cause.getContext().containsKey(EventContextKeys.LIQUID_MIX) && bto.getLocation().isPresent()) {
            Location<World> ourBlock = bto.getLocation().get();
            OreGenUltimate.get().logger.info("We generated: " + ourBlock.getBlockType());
            e.setCancelled(true);
            ourBlock.setBlockType(this.randomiser.next());
        }
    }
}
