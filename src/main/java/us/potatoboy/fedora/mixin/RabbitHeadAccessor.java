package us.potatoboy.fedora.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.RabbitEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RabbitEntityModel.class)
public interface RabbitHeadAccessor {
    @Accessor("head")
    ModelPart getHead();
}
