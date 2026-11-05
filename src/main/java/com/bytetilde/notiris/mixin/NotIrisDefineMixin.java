package com.bytetilde.notiris.mixin;

import java.util.ArrayList;
import java.util.List;
import net.irisshaders.iris.helpers.StringPair;
import net.irisshaders.iris.shaderpack.preprocessor.JcppProcessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(JcppProcessor.class)
public abstract class NotIrisDefineMixin {
  @ModifyVariable(method = "glslPreprocessSource", name = "environmentDefines", at = @At("HEAD"))
  private static Iterable<StringPair> notIris$addDefine(Iterable<StringPair> environmentDefines) {
    List<StringPair> defines = new ArrayList<>();
    for(StringPair define : environmentDefines) defines.add(define);
    defines.add(new StringPair("NOTIRIS_ENABLED", ""));
    return defines;
  }
}
