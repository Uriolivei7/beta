package U1;

import android.view.View;
import com.facebook.react.bridge.ColorPropConverter;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.AbstractC0430g;
import com.facebook.react.uimanager.BaseViewManager;
import com.facebook.react.uimanager.U;
import com.facebook.react.views.drawer.ReactDrawerLayoutManager;

/* JADX INFO: loaded from: classes.dex */
public class c extends AbstractC0430g {
    public c(BaseViewManager<Object, ? extends U> baseViewManager) {
        super(baseViewManager);
    }

    @Override // com.facebook.react.uimanager.AbstractC0430g, com.facebook.react.uimanager.Q0
    public void a(View view, String str, ReadableArray readableArray) {
        str.hashCode();
        if (str.equals(ReactDrawerLayoutManager.COMMAND_CLOSE_DRAWER)) {
            ((d) this.f7477a).closeDrawer(view);
        } else if (str.equals(ReactDrawerLayoutManager.COMMAND_OPEN_DRAWER)) {
            ((d) this.f7477a).openDrawer(view);
        }
    }

    @Override // com.facebook.react.uimanager.AbstractC0430g, com.facebook.react.uimanager.Q0
    public void b(View view, String str, Object obj) {
        str.hashCode();
        switch (str) {
            case "statusBarBackgroundColor":
                ((d) this.f7477a).setStatusBarBackgroundColor(view, ColorPropConverter.getColor(obj, view.getContext()));
                break;
            case "drawerBackgroundColor":
                ((d) this.f7477a).setDrawerBackgroundColor(view, ColorPropConverter.getColor(obj, view.getContext()));
                break;
            case "keyboardDismissMode":
                ((d) this.f7477a).setKeyboardDismissMode(view, (String) obj);
                break;
            case "drawerWidth":
                ((d) this.f7477a).setDrawerWidth(view, obj == null ? null : Float.valueOf(((Double) obj).floatValue()));
                break;
            case "drawerPosition":
                ((d) this.f7477a).setDrawerPosition(view, (String) obj);
                break;
            case "drawerLockMode":
                ((d) this.f7477a).setDrawerLockMode(view, (String) obj);
                break;
            default:
                super.b(view, str, obj);
                break;
        }
    }
}
