package org.infuse.plugin.Interactions;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.BlockEntity;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.component.ActiveAnimationComponent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.stamina.StaminaModule;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.world.PlayerUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.UUIDUtil;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.infuse.plugin.InfusePlugin;
import org.infuse.plugin.Utils.RayUtil;
import org.infuse.plugin.components.MEEmitterComponent;;

public class EmitterCollisionLeaveInteraction extends SimpleBlockInteraction {

    public static final BuilderCodec<EmitterCollisionLeaveInteraction> CODEC = BuilderCodec.builder(EmitterCollisionLeaveInteraction.class, EmitterCollisionLeaveInteraction::new).build();

    @Override
    protected void interactWithBlock(@NonNullDecl World world, @NonNullDecl CommandBuffer<EntityStore> commandBuffer, @NonNullDecl InteractionType interactionType,
                                     @NonNullDecl InteractionContext interactionContext, @NullableDecl ItemStack itemStack, @NonNullDecl Vector3i vector3i, @NonNullDecl CooldownHandler cooldownHandler) {
        var ref = interactionContext.getEntity();
        var store = ref.getStore();

        Holder<ChunkStore> componentHolder = world.getBlockComponentHolder(vector3i.x, vector3i.y, vector3i.z);


        if(componentHolder != null){
            MEEmitterComponent component = componentHolder.getComponent(MEEmitterComponent.getComponentType());
            if(component != null){
                int rotation = world.getBlockRotationIndex(vector3i.x, vector3i.y, vector3i.z);
                RayUtil.destroyRay(vector3i.x, vector3i.y, vector3i.z, component, rotation);
            }
        }




    }

    @Override
    protected void simulateInteractWithBlock(@NonNullDecl InteractionType interactionType, @NonNullDecl InteractionContext interactionContext, @NullableDecl ItemStack itemStack, @NonNullDecl World world, @NonNullDecl Vector3i vector3i) {

    }

}
