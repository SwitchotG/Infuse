package org.infuse.plugin;



import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.infuse.plugin.Interactions.EmitterCollideInteraction;
import org.infuse.plugin.commands.InfuseCommand;
import org.infuse.plugin.components.MEEmitterComponent;
import org.infuse.plugin.components.METransformableComponent;
import org.infuse.plugin.components.METraversableComponent;

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

        //System registry

        //Interaction registry
        this.getCodecRegistry(Interaction.CODEC).register("infuse_EmitterCollideinteraction", EmitterCollideInteraction.class, EmitterCollideInteraction.CODEC);

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


    public HytaleLogger getLOGGER(){
        return LOGGER;
    }
}