package D2;

/* JADX INFO: loaded from: classes.dex */
public class s {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final t f190a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final I2.b[] f191b;

    static {
        t tVar = null;
        try {
            tVar = (t) Class.forName("kotlin.reflect.jvm.internal.ReflectionFactoryImpl").newInstance();
        } catch (ClassCastException | ClassNotFoundException | IllegalAccessException | InstantiationException unused) {
        }
        if (tVar == null) {
            tVar = new t();
        }
        f190a = tVar;
        f191b = new I2.b[0];
    }

    public static I2.b a(Class cls) {
        return f190a.a(cls);
    }

    public static I2.c b(Class cls) {
        return f190a.b(cls, "");
    }

    public static I2.d c(j jVar) {
        return f190a.c(jVar);
    }

    public static String d(i iVar) {
        return f190a.e(iVar);
    }
}
