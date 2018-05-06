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

package io.github.selabie68.config;

import io.github.selabie68.OreGenUltimate;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OGUConfig {

    private final File oguConfigFile = new File(OreGenUltimate.get().configDir, "oregenultimate.conf");
    private ConfigurationLoader<CommentedConfigurationNode> oguConfigManager;
    private CommentedConfigurationNode oguConfig;

    public OGUConfig() {
        try {
            // Create the folder if it does not exist
            if (!OreGenUltimate.get().configDir.isDirectory()) {
                OreGenUltimate.get().configDir.mkdirs();
            }

            // Create the file if it does not exist
            if (!oguConfigFile.isFile()) {
                oguConfigFile.createNewFile();
                oguConfigManager = HoconConfigurationLoader.builder().setFile(oguConfigFile).build();
                oguConfig = oguConfigManager.load();
                //Load the Default Values
                //Cobblestone
                oguConfig.getNode("blocks", "cobble").setComment("This is a dummy value, it will actually be calculated").setValue(84.3);
                //Ores
                oguConfig.getNode("blocks", "coal-ore").setValue(3);
                oguConfig.getNode("blocks", "iron-ore").setValue(2);
                oguConfig.getNode("blocks", "gold-ore").setValue(2);
                oguConfig.getNode("blocks", "redstone-ore").setValue(2);
                oguConfig.getNode("blocks", "lapis-ore").setValue(2);
                oguConfig.getNode("blocks", "emerald-ore").setValue(1);
                oguConfig.getNode("blocks", "diamond-ore").setValue(1);
                //Solid Blocks
                oguConfig.getNode("blocks", "coal-block").setValue(0.5);
                oguConfig.getNode("blocks", "iron-block").setValue(0.5);
                oguConfig.getNode("blocks", "gold-block").setValue(0.5);
                oguConfig.getNode("blocks", "redstone-block").setValue(0.5);
                oguConfig.getNode("blocks", "lapis-block").setValue(0.5);
                oguConfig.getNode("blocks", "emerald-block").setValue(0.1);
                oguConfig.getNode("blocks", "diamond-block").setValue(0.1);
                //Worlds
                oguConfig.getNode("worlds", "world").setComment("The worlds the plugins should work in").setValue(true);
                oguConfigManager.save(oguConfig);
            } else {
                oguConfigManager = HoconConfigurationLoader.builder().setFile(oguConfigFile).build();
                oguConfig = oguConfigManager.load();
            }
        } catch (IOException e) {
            OreGenUltimate.get().logger.error("Unable to load config file!");
            e.printStackTrace();
            // Cancel plugin startup
            return;
        }
    }

    public Map<BlockType, Double> getBlocks() {
        Map<BlockType, Double> blocks = new HashMap<>();
        //Ores
        blocks.put(BlockTypes.COAL_ORE, oguConfig.getNode("blocks", "coal-ore").getDouble());
        blocks.put(BlockTypes.IRON_ORE, oguConfig.getNode("blocks", "iron-ore").getDouble());
        blocks.put(BlockTypes.GOLD_ORE, oguConfig.getNode("blocks", "gold-ore").getDouble());
        blocks.put(BlockTypes.REDSTONE_ORE, oguConfig.getNode("blocks", "redstone-ore").getDouble());
        blocks.put(BlockTypes.LAPIS_ORE, oguConfig.getNode("blocks", "lapis-ore").getDouble());
        blocks.put(BlockTypes.EMERALD_ORE, oguConfig.getNode("blocks", "emerald-ore").getDouble());
        blocks.put(BlockTypes.DIAMOND_ORE, oguConfig.getNode("blocks", "diamond-ore").getDouble());
        //Solid Blocks
        blocks.put(BlockTypes.COAL_BLOCK, oguConfig.getNode("blocks", "coal-block").getDouble());
        blocks.put(BlockTypes.IRON_BLOCK, oguConfig.getNode("blocks", "iron-block").getDouble());
        blocks.put(BlockTypes.GOLD_BLOCK, oguConfig.getNode("blocks", "gold-block").getDouble());
        blocks.put(BlockTypes.REDSTONE_BLOCK, oguConfig.getNode("blocks", "redstone-block").getDouble());
        blocks.put(BlockTypes.LAPIS_BLOCK, oguConfig.getNode("blocks", "lapis-block").getDouble());
        blocks.put(BlockTypes.EMERALD_BLOCK, oguConfig.getNode("blocks", "emerald-block").getDouble());
        blocks.put(BlockTypes.DIAMOND_BLOCK, oguConfig.getNode("blocks", "diamond-block").getDouble());

        Double chanceTotal = 0.0;
        for (Double f : blocks.values()) {
            chanceTotal += f;
        }

        if(chanceTotal > 100.0){
            OreGenUltimate.get().logger.error("OreGenUltimate values cannot equal more than 100%");
            return null;
        }

        blocks.put(BlockTypes.COBBLESTONE, 100.0 - chanceTotal);

        return blocks;
    }
}
