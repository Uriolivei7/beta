package H0;

/* JADX INFO: loaded from: classes.dex */
public class s {

    class a implements z {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ t f313a;

        a(t tVar) {
            this.f313a = tVar;
        }

        @Override // H0.z
        /* JADX INFO: renamed from: d, reason: merged with bridge method [inline-methods] */
        public void b(R.d dVar) {
            this.f313a.i(dVar);
        }

        @Override // H0.z
        /* JADX INFO: renamed from: e, reason: merged with bridge method [inline-methods] */
        public void c(R.d dVar) {
            this.f313a.c(dVar);
        }

        @Override // H0.z
        /* JADX INFO: renamed from: f, reason: merged with bridge method [inline-methods] */
        public void a(R.d dVar) {
            this.f313a.h(dVar);
        }
    }

    public static u a(x xVar, t tVar) {
        tVar.b(xVar);
        return new u(xVar, new a(tVar));
    }
}
