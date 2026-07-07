package K0;

import D2.h;
import Q0.d;
import com.facebook.imagepipeline.producers.e0;
import com.facebook.imagepipeline.producers.m0;
import h0.InterfaceC0548c;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class c extends K0.a {

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final a f811j = new a(null);

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final InterfaceC0548c a(e0 e0Var, m0 m0Var, d dVar) {
            h.f(e0Var, "producer");
            h.f(m0Var, "settableProducerContext");
            h.f(dVar, "listener");
            return new c(e0Var, m0Var, dVar, null);
        }

        private a() {
        }
    }

    public /* synthetic */ c(e0 e0Var, m0 m0Var, d dVar, DefaultConstructorMarker defaultConstructorMarker) {
        this(e0Var, m0Var, dVar);
    }

    private c(e0 e0Var, m0 m0Var, d dVar) {
        super(e0Var, m0Var, dVar);
    }
}
