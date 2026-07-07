package com.facebook.react.bridge;

import com.facebook.yoga.YogaValue;
import com.facebook.yoga.w;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class DimensionPropConverter {
    public static final Companion Companion = new Companion(null);

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final YogaValue getDimension(Object obj) {
            if (obj == null) {
                return null;
            }
            if (obj instanceof Double) {
                return new YogaValue((float) ((Number) obj).doubleValue(), w.POINT);
            }
            if (obj instanceof String) {
                return YogaValue.a((String) obj);
            }
            throw new JSApplicationCausedNativeException("DimensionValue: the value must be a number or string.");
        }

        private Companion() {
        }
    }

    public static final YogaValue getDimension(Object obj) {
        return Companion.getDimension(obj);
    }
}
