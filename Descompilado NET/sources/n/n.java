package N;

import android.webkit.WebMessagePort;
import android.webkit.WebSettings;
import java.lang.reflect.InvocationHandler;
import org.chromium.support_lib_boundary.WebSettingsBoundaryInterface;
import org.chromium.support_lib_boundary.WebkitToCompatConverterBoundaryInterface;

/* JADX INFO: loaded from: classes.dex */
public class n {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final WebkitToCompatConverterBoundaryInterface f1401a;

    public n(WebkitToCompatConverterBoundaryInterface webkitToCompatConverterBoundaryInterface) {
        this.f1401a = webkitToCompatConverterBoundaryInterface;
    }

    public g a(WebSettings webSettings) {
        return new g((WebSettingsBoundaryInterface) e3.a.a(WebSettingsBoundaryInterface.class, this.f1401a.convertSettings(webSettings)));
    }

    public InvocationHandler b(WebMessagePort webMessagePort) {
        return this.f1401a.convertWebMessagePort(webMessagePort);
    }
}
