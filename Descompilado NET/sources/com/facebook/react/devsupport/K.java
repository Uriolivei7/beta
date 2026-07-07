package com.facebook.react.devsupport;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class K {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final a f6641b = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private boolean f6642a;

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void c(K k3) {
        k3.f6642a = false;
    }

    public final boolean b(int i3, View view) {
        if (i3 == 46 && !(view instanceof EditText)) {
            if (this.f6642a) {
                this.f6642a = false;
                return true;
            }
            this.f6642a = true;
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.facebook.react.devsupport.J
                @Override // java.lang.Runnable
                public final void run() {
                    K.c(this.f6640b);
                }
            }, 200L);
        }
        return false;
    }
}
