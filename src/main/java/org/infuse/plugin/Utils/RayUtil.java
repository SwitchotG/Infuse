package org.infuse.plugin.Utils;

import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.modules.interaction.BlockHarvestUtils;
import com.hypixel.hytale.server.core.modules.interaction.BlockPlaceUtils;
import com.hypixel.hytale.server.core.universe.world.ParticleUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.infuse.plugin.InfusePlugin;
import org.infuse.plugin.components.METransformableComponent;
import org.infuse.plugin.components.METraversableComponent;

public final class RayUtil {

    public static boolean castRay(int resistance, int rotation, Vector3i vector3i, Store<EntityStore> store, World world){

        int resistanceLeft = resistance;
        int i = -1;


        while(resistanceLeft > 0){
            i++;
            switch(rotation){
                case 0:
                    resistanceLeft = shootPartRay(world, new Vector3i(vector3i.x + 1 + i, vector3i.y, vector3i.z), resistanceLeft, store);
                    break;
                case 1:
                    resistanceLeft = shootPartRay(world, new Vector3i(vector3i.x, vector3i.y, vector3i.z - 1 - i), resistanceLeft, store);
                    break;
                case 2:
                    resistanceLeft = shootPartRay(world, new Vector3i(vector3i.x - 1 - i, vector3i.y, vector3i.z), resistanceLeft, store);
                    break;
                case 3:
                    resistanceLeft = shootPartRay(world, new Vector3i(vector3i.x, vector3i.y, vector3i.z + 1 + i), resistanceLeft, store);
                    break;
            }
        }

        return true;
    }

    public static int shootPartRay(World world, Vector3i vector3i, int resistanceLeft, Store<EntityStore> store){
        BlockType blockType = world.getBlockType(vector3i.x, vector3i.y, vector3i.z);
        int returnedResistance = resistanceLeft;
        if(blockType != null){
            if(blockType.getId().equals("Empty")){
                returnedResistance--;
            }else{
                Holder<ChunkStore> holder = world.getBlockComponentHolder(vector3i.x, vector3i.y, vector3i.z);

                if(holder == null){
                    returnedResistance = 0;
                    return returnedResistance;
                }

                METransformableComponent transformableComponent = holder.getComponent(InfusePlugin.get().getMETransformableComponentType());

                if(transformableComponent != null){
                    world.setBlock(vector3i.x, vector3i.y, vector3i.z, transformableComponent.getBlockId());
                }

                METraversableComponent traversableComponent = holder.getComponent(InfusePlugin.get().getMETraversableComponentType());

                if(traversableComponent != null){
                   returnedResistance -= traversableComponent.getStoppingPower();
                }else{
                    returnedResistance = 0;
                    return returnedResistance;
                }
            }
            ParticleUtil.spawnParticleEffect("Mana_Small_Explosion", new Vector3d(vector3i.x + 0.5, vector3i.y + 0.3, vector3i.z + 0.5), store);
        }
        return returnedResistance;
    }
}
