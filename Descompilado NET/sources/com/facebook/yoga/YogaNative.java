package com.facebook.yoga;

import com.facebook.soloader.SoLoader;

/* JADX INFO: loaded from: classes.dex */
public class YogaNative {
    static {
        SoLoader.t("yoga");
    }

    static native void jni_YGConfigFreeJNI(long j3);

    static native int jni_YGConfigGetErrataJNI(long j3);

    static native long jni_YGConfigNewJNI();

    static native void jni_YGConfigSetErrataJNI(long j3, int i3);

    static native void jni_YGConfigSetExperimentalFeatureEnabledJNI(long j3, int i3, boolean z3);

    static native void jni_YGConfigSetLoggerJNI(long j3, YogaLogger yogaLogger);

    static native void jni_YGConfigSetPointScaleFactorJNI(long j3, float f3);

    static native void jni_YGConfigSetUseWebDefaultsJNI(long j3, boolean z3);

    static native void jni_YGNodeCalculateLayoutJNI(long j3, float f3, float f4, long[] jArr, YogaNodeJNIBase[] yogaNodeJNIBaseArr);

    static native long jni_YGNodeCloneJNI(long j3);

    static native void jni_YGNodeCopyStyleJNI(long j3, long j4);

    static native void jni_YGNodeFinalizeJNI(long j3);

    static native void jni_YGNodeInsertChildJNI(long j3, long j4, int i3);

    static native boolean jni_YGNodeIsDirtyJNI(long j3);

    static native boolean jni_YGNodeIsReferenceBaselineJNI(long j3);

    static native void jni_YGNodeMarkDirtyJNI(long j3);

    static native long jni_YGNodeNewJNI();

    static native long jni_YGNodeNewWithConfigJNI(long j3);

    static native void jni_YGNodeRemoveAllChildrenJNI(long j3);

    static native void jni_YGNodeRemoveChildJNI(long j3, long j4);

    static native void jni_YGNodeResetJNI(long j3);

    static native void jni_YGNodeSetAlwaysFormsContainingBlockJNI(long j3, boolean z3);

    static native void jni_YGNodeSetHasBaselineFuncJNI(long j3, boolean z3);

    static native void jni_YGNodeSetHasMeasureFuncJNI(long j3, boolean z3);

    static native void jni_YGNodeSetIsReferenceBaselineJNI(long j3, boolean z3);

    static native void jni_YGNodeSetStyleInputsJNI(long j3, float[] fArr, int i3);

    static native int jni_YGNodeStyleGetAlignContentJNI(long j3);

    static native int jni_YGNodeStyleGetAlignItemsJNI(long j3);

    static native int jni_YGNodeStyleGetAlignSelfJNI(long j3);

    static native float jni_YGNodeStyleGetAspectRatioJNI(long j3);

    static native float jni_YGNodeStyleGetBorderJNI(long j3, int i3);

    static native int jni_YGNodeStyleGetBoxSizingJNI(long j3);

    static native int jni_YGNodeStyleGetDirectionJNI(long j3);

    static native int jni_YGNodeStyleGetDisplayJNI(long j3);

    static native long jni_YGNodeStyleGetFlexBasisJNI(long j3);

    static native int jni_YGNodeStyleGetFlexDirectionJNI(long j3);

    static native float jni_YGNodeStyleGetFlexGrowJNI(long j3);

    static native float jni_YGNodeStyleGetFlexJNI(long j3);

    static native float jni_YGNodeStyleGetFlexShrinkJNI(long j3);

    static native int jni_YGNodeStyleGetFlexWrapJNI(long j3);

    static native long jni_YGNodeStyleGetGapJNI(long j3, int i3);

    static native long jni_YGNodeStyleGetHeightJNI(long j3);

    static native int jni_YGNodeStyleGetJustifyContentJNI(long j3);

    static native long jni_YGNodeStyleGetMarginJNI(long j3, int i3);

    static native long jni_YGNodeStyleGetMaxHeightJNI(long j3);

    static native long jni_YGNodeStyleGetMaxWidthJNI(long j3);

    static native long jni_YGNodeStyleGetMinHeightJNI(long j3);

    static native long jni_YGNodeStyleGetMinWidthJNI(long j3);

    static native int jni_YGNodeStyleGetOverflowJNI(long j3);

    static native long jni_YGNodeStyleGetPaddingJNI(long j3, int i3);

    static native long jni_YGNodeStyleGetPositionJNI(long j3, int i3);

    static native int jni_YGNodeStyleGetPositionTypeJNI(long j3);

    static native long jni_YGNodeStyleGetWidthJNI(long j3);

    static native void jni_YGNodeStyleSetAlignContentJNI(long j3, int i3);

    static native void jni_YGNodeStyleSetAlignItemsJNI(long j3, int i3);

    static native void jni_YGNodeStyleSetAlignSelfJNI(long j3, int i3);

    static native void jni_YGNodeStyleSetAspectRatioJNI(long j3, float f3);

    static native void jni_YGNodeStyleSetBorderJNI(long j3, int i3, float f3);

    static native void jni_YGNodeStyleSetBoxSizingJNI(long j3, int i3);

    static native void jni_YGNodeStyleSetDirectionJNI(long j3, int i3);

    static native void jni_YGNodeStyleSetDisplayJNI(long j3, int i3);

    static native void jni_YGNodeStyleSetFlexBasisAutoJNI(long j3);

    static native void jni_YGNodeStyleSetFlexBasisFitContentJNI(long j3);

    static native void jni_YGNodeStyleSetFlexBasisJNI(long j3, float f3);

    static native void jni_YGNodeStyleSetFlexBasisMaxContentJNI(long j3);

    static native void jni_YGNodeStyleSetFlexBasisPercentJNI(long j3, float f3);

    static native void jni_YGNodeStyleSetFlexBasisStretchJNI(long j3);

    static native void jni_YGNodeStyleSetFlexDirectionJNI(long j3, int i3);

    static native void jni_YGNodeStyleSetFlexGrowJNI(long j3, float f3);

    static native void jni_YGNodeStyleSetFlexJNI(long j3, float f3);

    static native void jni_YGNodeStyleSetFlexShrinkJNI(long j3, float f3);

    static native void jni_YGNodeStyleSetFlexWrapJNI(long j3, int i3);

    static native void jni_YGNodeStyleSetGapJNI(long j3, int i3, float f3);

    static native void jni_YGNodeStyleSetGapPercentJNI(long j3, int i3, float f3);

    static native void jni_YGNodeStyleSetHeightAutoJNI(long j3);

    static native void jni_YGNodeStyleSetHeightFitContentJNI(long j3);

    static native void jni_YGNodeStyleSetHeightJNI(long j3, float f3);

    static native void jni_YGNodeStyleSetHeightMaxContentJNI(long j3);

    static native void jni_YGNodeStyleSetHeightPercentJNI(long j3, float f3);

    static native void jni_YGNodeStyleSetHeightStretchJNI(long j3);

    static native void jni_YGNodeStyleSetJustifyContentJNI(long j3, int i3);

    static native void jni_YGNodeStyleSetMarginAutoJNI(long j3, int i3);

    static native void jni_YGNodeStyleSetMarginJNI(long j3, int i3, float f3);

    static native void jni_YGNodeStyleSetMarginPercentJNI(long j3, int i3, float f3);

    static native void jni_YGNodeStyleSetMaxHeightFitContentJNI(long j3);

    static native void jni_YGNodeStyleSetMaxHeightJNI(long j3, float f3);

    static native void jni_YGNodeStyleSetMaxHeightMaxContentJNI(long j3);

    static native void jni_YGNodeStyleSetMaxHeightPercentJNI(long j3, float f3);

    static native void jni_YGNodeStyleSetMaxHeightStretchJNI(long j3);

    static native void jni_YGNodeStyleSetMaxWidthFitContentJNI(long j3);

    static native void jni_YGNodeStyleSetMaxWidthJNI(long j3, float f3);

    static native void jni_YGNodeStyleSetMaxWidthMaxContentJNI(long j3);

    static native void jni_YGNodeStyleSetMaxWidthPercentJNI(long j3, float f3);

    static native void jni_YGNodeStyleSetMaxWidthStretchJNI(long j3);

    static native void jni_YGNodeStyleSetMinHeightFitContentJNI(long j3);

    static native void jni_YGNodeStyleSetMinHeightJNI(long j3, float f3);

    static native void jni_YGNodeStyleSetMinHeightMaxContentJNI(long j3);

    static native void jni_YGNodeStyleSetMinHeightPercentJNI(long j3, float f3);

    static native void jni_YGNodeStyleSetMinHeightStretchJNI(long j3);

    static native void jni_YGNodeStyleSetMinWidthFitContentJNI(long j3);

    static native void jni_YGNodeStyleSetMinWidthJNI(long j3, float f3);

    static native void jni_YGNodeStyleSetMinWidthMaxContentJNI(long j3);

    static native void jni_YGNodeStyleSetMinWidthPercentJNI(long j3, float f3);

    static native void jni_YGNodeStyleSetMinWidthStretchJNI(long j3);

    static native void jni_YGNodeStyleSetOverflowJNI(long j3, int i3);

    static native void jni_YGNodeStyleSetPaddingJNI(long j3, int i3, float f3);

    static native void jni_YGNodeStyleSetPaddingPercentJNI(long j3, int i3, float f3);

    static native void jni_YGNodeStyleSetPositionAutoJNI(long j3, int i3);

    static native void jni_YGNodeStyleSetPositionJNI(long j3, int i3, float f3);

    static native void jni_YGNodeStyleSetPositionPercentJNI(long j3, int i3, float f3);

    static native void jni_YGNodeStyleSetPositionTypeJNI(long j3, int i3);

    static native void jni_YGNodeStyleSetWidthAutoJNI(long j3);

    static native void jni_YGNodeStyleSetWidthFitContentJNI(long j3);

    static native void jni_YGNodeStyleSetWidthJNI(long j3, float f3);

    static native void jni_YGNodeStyleSetWidthMaxContentJNI(long j3);

    static native void jni_YGNodeStyleSetWidthPercentJNI(long j3, float f3);

    static native void jni_YGNodeStyleSetWidthStretchJNI(long j3);

    static native void jni_YGNodeSwapChildJNI(long j3, long j4, int i3);
}
