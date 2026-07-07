package com.facebook.react.modules.sound;

import D2.h;
import android.media.AudioManager;
import com.facebook.fbreact.specs.NativeSoundManagerSpec;
import com.facebook.react.bridge.ReactApplicationContext;
import kotlin.jvm.internal.DefaultConstructorMarker;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = "SoundManager")
public final class SoundManagerModule extends NativeSoundManagerSpec {
    public static final a Companion = new a(null);
    public static final String NAME = "SoundManager";

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public SoundManagerModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @Override // com.facebook.fbreact.specs.NativeSoundManagerSpec
    public void playTouchSound() {
        Object systemService = getReactApplicationContext().getSystemService("audio");
        h.d(systemService, "null cannot be cast to non-null type android.media.AudioManager");
        ((AudioManager) systemService).playSoundEffect(0);
    }
}
