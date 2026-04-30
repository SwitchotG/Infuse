package org.infuse.plugin;



import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.infuse.plugin.Class.Ray.MEConsumerInputRay;
import org.infuse.plugin.Interactions.EmitterCollideInteraction;
import org.infuse.plugin.Interactions.EmitterCollisionLeaveInteraction;
import org.infuse.plugin.commands.InfuseCommand;
import org.infuse.plugin.components.*;
import org.infuse.plugin.system.EmiteBreakSystem;
import org.infuse.plugin.system.EmiteSystem;
import org.infuse.plugin.system.MEConsumerSystem;
import org.infuse.plugin.system.RayCleanupSystem;

import javax.annotation.Nonnull;

/**
 * This class serves as the entrypoint for your plugin. Use the setup method to register into game registries or add
 * event listeners.
 */
public class InfusePlugin extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static InfusePlugin INSTANCE;
    private ComponentType<ChunkStore, MEEmitterComponent> MEEmitterComponentType;
    private ComponentType<ChunkStore, METraversableComponent> METraversableComponentType;
    private ComponentType<ChunkStore, METransformableComponent> METransformableComponentType;
    private ComponentType<ChunkStore, MEConsumerComponent> MEConsumerComponentType;

    public InfusePlugin(@Nonnull JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from " + this.getName() + " version " + this.getManifest().getVersion().toString());
        InfusePlugin.INSTANCE = this;
    }

    public static InfusePlugin get(){
        return InfusePlugin.INSTANCE;
    }

    @Override
    protected void setup() {
        LOGGER.atInfo().log("Setting up plugin " + this.getName());
        this.getCommandRegistry().registerCommand(new InfuseCommand(this.getName(), this.getManifest().getVersion().toString()));

        //Component registry :
        this.MEEmitterComponentType = this.getChunkStoreRegistry().registerComponent(MEEmitterComponent.class, "MEEmitterComponent", MEEmitterComponent.CODEC);
        this.METraversableComponentType = this.getChunkStoreRegistry().registerComponent(METraversableComponent.class, "METraversableComponent", METraversableComponent.CODEC);
        this.METransformableComponentType = this.getChunkStoreRegistry().registerComponent(METransformableComponent.class, "METransformableComponent", METransformableComponent.CODEC);
        this.MEConsumerComponentType = this.getChunkStoreRegistry().registerComponent(MEConsumerComponent.class, "MEConsumerComponent", MEConsumerComponent.CODEC);

        //System registry
        //this.getChunkStoreRegistry().registerSystem(new EmiteSystem());
        //this.getEntityStoreRegistry().registerSystem(new EmiteBreakSystem());
        //this.getChunkStoreRegistry().registerSystem(new RayCleanupSystem());
        //this.getChunkStoreRegistry().registerSystem(new MEConsumerSystem());

        //Interaction registry
        this.getCodecRegistry(Interaction.CODEC).register("infuse_EmitterCollideInteraction", EmitterCollideInteraction.class, EmitterCollideInteraction.CODEC);
        this.getCodecRegistry(Interaction.CODEC).register("infuse_EmitterCollisionLeaveInteraction", EmitterCollisionLeaveInteraction.class, EmitterCollisionLeaveInteraction.CODEC);
    }

    public ComponentType<ChunkStore, MEEmitterComponent> getMEEmitterComponentType(){
        return this.MEEmitterComponentType;
    }


    public ComponentType<ChunkStore, METraversableComponent> getMETraversableComponentType(){
        return this.METraversableComponentType;
    }

    public ComponentType<ChunkStore, METransformableComponent> getMETransformableComponentType(){
        return this.METransformableComponentType;
    }

    public ComponentType<ChunkStore, MEConsumerComponent> getMEConsumerComponentType(){
        return this.MEConsumerComponentType;
    }


    public HytaleLogger getLOGGER(){
        return LOGGER;
    }
}