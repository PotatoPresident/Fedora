package us.potatoboy.fedora.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.SnowGolemEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SnowGolemEntityModel.class)
public interface SnowHeadAccessor {
    @Accessor("topSnowball")
    ModelPart getHead();
}
