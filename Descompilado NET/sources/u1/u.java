package U1;

import android.view.View;
import com.facebook.react.uimanager.AbstractC0430g;
import com.facebook.react.uimanager.BaseViewManager;
import com.facebook.react.uimanager.U;

/* JADX INFO: loaded from: classes.dex */
public class u extends AbstractC0430g {
    public u(BaseViewManager<Object, ? extends U> baseViewManager) {
        super(baseViewManager);
    }

    @Override // com.facebook.react.uimanager.AbstractC0430g, com.facebook.react.uimanager.Q0
    public void b(View view, String str, Object obj) {
        str.hashCode();
        if (str.equals("name")) {
            ((v) this.f7477a).setName(view, obj == null ? "" : (String) obj);
        } else {
            super.b(view, str, obj);
        }
    }
}
