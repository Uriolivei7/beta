package D2;

/* JADX INFO: loaded from: classes.dex */
public class t {
    public I2.b a(Class cls) {
        return new e(cls);
    }

    public I2.c b(Class cls, String str) {
        return new m(cls, str);
    }

    public String d(g gVar) {
        String string = gVar.getClass().getGenericInterfaces()[0].toString();
        return string.startsWith("kotlin.jvm.functions.") ? string.substring(21) : string;
    }

    public String e(i iVar) {
        return d(iVar);
    }

    public I2.d c(j jVar) {
        return jVar;
    }
}
