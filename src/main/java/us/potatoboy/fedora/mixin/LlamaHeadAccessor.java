package us.potatoboy.fedora.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.LlamaEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LlamaEntityModel.class)
public interface LlamaHeadAccessor {
    @Accessor("head")
    ModelPart getHead();
}
