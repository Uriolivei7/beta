package Q0;

import D2.h;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class a implements e {
    @Override // Q0.e
    public void a(U0.b bVar, Object obj, String str, boolean z3) {
        h.f(bVar, "request");
        h.f(obj, "callerContext");
        h.f(str, "requestId");
    }

    @Override // Q0.e
    public void b(U0.b bVar, String str, boolean z3) {
        h.f(bVar, "request");
        h.f(str, "requestId");
    }

    @Override // com.facebook.imagepipeline.producers.i0
    public boolean c(String str) {
        h.f(str, "requestId");
        return false;
    }

    @Override // com.facebook.imagepipeline.producers.i0
    public void d(String str, String str2, String str3) {
        h.f(str, "requestId");
        h.f(str2, "producerName");
        h.f(str3, "eventName");
    }

    @Override // com.facebook.imagepipeline.producers.i0
    public void e(String str, String str2, Map map) {
        h.f(str, "requestId");
        h.f(str2, "producerName");
    }

    @Override // com.facebook.imagepipeline.producers.i0
    public void f(String str, String str2) {
        h.f(str, "requestId");
        h.f(str2, "producerName");
    }

    @Override // com.facebook.imagepipeline.producers.i0
    public void g(String str, String str2, Throwable th, Map map) {
        h.f(str, "requestId");
        h.f(str2, "producerName");
        h.f(th, "t");
    }

    @Override // com.facebook.imagepipeline.producers.i0
    public void h(String str, String str2, Map map) {
        h.f(str, "requestId");
        h.f(str2, "producerName");
    }

    @Override // Q0.e
    public void i(String str) {
        h.f(str, "requestId");
    }

    @Override // com.facebook.imagepipeline.producers.i0
    public void j(String str, String str2, boolean z3) {
        h.f(str, "requestId");
        h.f(str2, "producerName");
    }

    @Override // Q0.e
    public void k(U0.b bVar, String str, Throwable th, boolean z3) {
        h.f(bVar, "request");
        h.f(str, "requestId");
        h.f(th, "throwable");
    }
}
