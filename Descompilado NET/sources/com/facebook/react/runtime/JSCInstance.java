package com.facebook.react.runtime;

import com.facebook.jni.HybridData;
import com.facebook.soloader.SoLoader;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class JSCInstance extends JSRuntimeFactory {
    private static final a Companion = new a(null);

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final HybridData initHybrid() {
            return JSCInstance.initHybrid();
        }

        private a() {
        }
    }

    static {
        SoLoader.t("jscinstance");
    }

    public JSCInstance() {
        super(Companion.initHybrid());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final native HybridData initHybrid();
}
