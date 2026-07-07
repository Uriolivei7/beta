package com.facebook.react.fabric.events;

import com.facebook.jni.HybridData;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.fabric.c;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class EventBeatManager implements P1.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final a f6827a = new a(null);
    private final HybridData mHybridData;

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final HybridData b() {
            return EventBeatManager.initHybrid();
        }

        private a() {
        }
    }

    static {
        c.a();
    }

    public EventBeatManager() {
        this.mHybridData = f6827a.b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final native HybridData initHybrid();

    private final native void tick();

    @Override // P1.a
    public void a() {
        tick();
    }

    public EventBeatManager(ReactApplicationContext reactApplicationContext) {
        this();
    }
}
