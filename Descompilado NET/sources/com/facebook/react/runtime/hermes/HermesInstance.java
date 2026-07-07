package com.facebook.react.runtime.hermes;

import com.facebook.jni.HybridData;
import com.facebook.react.runtime.JSRuntimeFactory;
import com.facebook.soloader.SoLoader;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class HermesInstance extends JSRuntimeFactory {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final a f7190a = new a(null);

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        protected final HybridData initHybrid(boolean z3) {
            return HermesInstance.initHybrid(z3);
        }

        private a() {
        }
    }

    static {
        SoLoader.t("hermesinstancejni");
    }

    public HermesInstance(boolean z3) {
        super(initHybrid(z3));
    }

    protected static final native HybridData initHybrid(boolean z3);

    public HermesInstance() {
        this(false);
    }
}
