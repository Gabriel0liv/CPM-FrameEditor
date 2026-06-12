package com.tom.cpm.timeline.mixin;

import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.tom.cpm.shared.animation.AnimationEngine.AnimationMode;
import com.tom.cpm.shared.animation.AnimationState;
import com.tom.cpm.shared.animation.InterpolatorChannel;
import com.tom.cpm.shared.definition.ModelDefinition;
import com.tom.cpm.shared.editor.anim.EditorAnim;
import com.tom.cpm.shared.editor.elements.ModelElement;
import com.tom.cpm.timeline.CPMAnimatorUtilsCore;

@Mixin(value = EditorAnim.class, remap = false)
public abstract class EditorAnimMixin {
    @Shadow private List<ModelElement> components;
    @Shadow private com.tom.cpm.shared.animation.interpolator.Interpolator[][] psfs;
    @Shadow public abstract float getValue(ModelElement component, InterpolatorChannel attribute, double time);
    @Shadow public abstract int getDuration(AnimationMode mode);
    @Shadow private List<com.tom.cpm.shared.editor.anim.AnimFrame> frames;
    @Shadow public boolean add;
    @Shadow protected abstract void calculateSplines();

    @Inject(
            method = "animate(JLcom/tom/cpm/shared/definition/ModelDefinition;Lcom/tom/cpm/shared/animation/AnimationEngine$AnimationMode;)V",
            at = @At("HEAD"),
            cancellable = true,
            require = 0
    )
    private void cpmTimeline$onAnimateOld(long millis, ModelDefinition def, AnimationMode mode, CallbackInfo ci) {
        cpmTimeline$handleAnimate(ci);
    }

    @Inject(
            method = "animate(Lcom/tom/cpm/shared/animation/AnimationState;JLcom/tom/cpm/shared/definition/ModelDefinition;Lcom/tom/cpm/shared/animation/AnimationEngine$AnimationMode;)V",
            at = @At("HEAD"),
            cancellable = true,
            require = 0
    )
    private void cpmTimeline$onAnimateNew(AnimationState state, long millis, ModelDefinition def, AnimationMode mode, CallbackInfo ci) {
        cpmTimeline$handleAnimate(ci);
    }

    private void cpmTimeline$handleAnimate(CallbackInfo ci) {
        if (CPMAnimatorUtilsCore.scrubTime != -1) {
            System.out.println("[CPM Timeline] Scrubbing Animate: " + CPMAnimatorUtilsCore.scrubTime);
            if (components == null || psfs == null) calculateSplines();
            doCustomAnimate(CPMAnimatorUtilsCore.scrubTime);
            ci.cancel();
        }
    }

    @Inject(method = "apply", at = @At("HEAD"), cancellable = true)
    private void cpmTimeline$onApply(CallbackInfo ci) {
        if (CPMAnimatorUtilsCore.scrubTime != -1) {
            if (components == null || psfs == null) calculateSplines();
            doCustomAnimate(CPMAnimatorUtilsCore.scrubTime);
            ci.cancel();
        }
    }

    private void doCustomAnimate(long time) {
        if (components == null || psfs == null) return;
        if (frames == null || frames.isEmpty()) return;

        int duration = Math.max(1, getDuration(null));
        long localTime = Math.floorMod(time, duration);
        float step = (float) localTime / duration * frames.size();
        int fIdx = (int) Math.floor(step) % frames.size();

        for (int i = 0; i < components.size(); i++) {
            ModelElement component = components.get(i);

            component.rc.setRotation(add,
                    getValue(component, InterpolatorChannel.ROT_X, step),
                    getValue(component, InterpolatorChannel.ROT_Y, step),
                    getValue(component, InterpolatorChannel.ROT_Z, step));

            component.rc.setPosition(add,
                    getValue(component, InterpolatorChannel.POS_X, step),
                    getValue(component, InterpolatorChannel.POS_Y, step),
                    getValue(component, InterpolatorChannel.POS_Z, step));

            component.rc.setColor(
                    getValue(component, InterpolatorChannel.COLOR_R, step),
                    getValue(component, InterpolatorChannel.COLOR_G, step),
                    getValue(component, InterpolatorChannel.COLOR_B, step));

            component.rc.setVisible(frames.get(fIdx).getVisible(component));

            component.rc.setRenderScale(add,
                    getValue(component, InterpolatorChannel.SCALE_X, step),
                    getValue(component, InterpolatorChannel.SCALE_Y, step),
                    getValue(component, InterpolatorChannel.SCALE_Z, step));
        }
    }
}
