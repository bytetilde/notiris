package com.bytetilde.notiris.mixin;

import com.bytetilde.notiris.NotIrisProperties;
import com.google.common.collect.ImmutableList;
import java.nio.file.Path;
import java.util.Map;
import net.irisshaders.iris.helpers.StringPair;
import net.irisshaders.iris.shaderpack.ShaderPack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShaderPack.class)
public abstract class NotIrisPropertiesMixin {
  @Inject(
    method =
      "<init>(Ljava/nio/file/Path;Ljava/util/Map;Lcom/google/common/collect/ImmutableList;Z)V",
    at = @At("HEAD"))
  private static void
  notIris$loadProperties(Path root, Map<String, String> changedConfigs,
                         ImmutableList<StringPair> environmentDefines, boolean isZip,
                         CallbackInfo ci) {
    NotIrisProperties.load(root);
  }
}
