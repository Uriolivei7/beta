package h2;

import android.content.Context;
import android.content.SharedPreferences;
import com.learnium.RNDeviceInfo.RNDeviceModule;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

/* JADX INFO: renamed from: h2.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0556a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f9485a;

    public C0556a(Context context) {
        this.f9485a = context;
    }

    String a() throws IllegalAccessException, InvocationTargetException {
        Object objInvoke = Class.forName("com.google.firebase.iid.FirebaseInstanceId").getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
        return (String) objInvoke.getClass().getMethod("getId", new Class[0]).invoke(objInvoke, new Object[0]);
    }

    String b() throws IllegalAccessException, InvocationTargetException {
        Object objInvoke = Class.forName("com.google.android.gms.iid.InstanceID").getDeclaredMethod("getInstance", Context.class).invoke(null, this.f9485a.getApplicationContext());
        return (String) objInvoke.getClass().getMethod("getId", new Class[0]).invoke(objInvoke, new Object[0]);
    }

    String c() {
        return RNDeviceModule.getRNDISharedPreferences(this.f9485a).getString("instanceId", "unknown");
    }

    public String d() {
        String strC = c();
        if (strC != "unknown") {
            return strC;
        }
        try {
            String strA = a();
            f(strA);
            return strA;
        } catch (ClassNotFoundException unused) {
            try {
                String strB = b();
                f(strB);
                return strB;
            } catch (ClassNotFoundException unused2) {
                String strE = e();
                f(strE);
                return strE;
            } catch (IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException unused3) {
                System.err.println("N/A: Unsupported version of com.google.android.gms.iid in your project.");
                String strE2 = e();
                f(strE2);
                return strE2;
            }
        } catch (IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException unused4) {
            System.err.println("N/A: Unsupported version of com.google.firebase:firebase-iid in your project.");
            String strB2 = b();
            f(strB2);
            return strB2;
        }
    }

    String e() {
        return UUID.randomUUID().toString();
    }

    void f(String str) {
        SharedPreferences.Editor editorEdit = RNDeviceModule.getRNDISharedPreferences(this.f9485a).edit();
        editorEdit.putString("instanceId", str);
        editorEdit.apply();
    }
}
