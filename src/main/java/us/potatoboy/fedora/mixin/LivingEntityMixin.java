package us.potatoboy.fedora.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.potatoboy.fedora.Fedora;
import us.potatoboy.fedora.Hat;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "onKilledBy", at = @At("HEAD"))
    private void onKilledBy(@Nullable LivingEntity adversary, CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object)this;
        if (!livingEntity.world.isClient) {
            if (adversary instanceof PlayerEntity) {
                if (Fedora.config.isBlacklisted(Registry.ENTITY_TYPE.getId(livingEntity.getType()))) return;

                PlayerEntity player = (PlayerEntity) adversary;
                Hat hat = Fedora.ENTITY_HAT_COMPONENT.get(livingEntity).getCurrentHat();
                if (hat != null) {
                    Fedora.PLAYER_HAT_COMPONENT.get(player).unlockHat(hat);
                }
            }
        }
    }
}
