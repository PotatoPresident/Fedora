package us.potatoboy.fedora.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.RavagerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RavagerEntityModel.class)
public interface RavagerHeadAccessor {
    @Accessor("head")
    ModelPart getHead();
}
