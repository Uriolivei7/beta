package U1;

import android.view.View;
import com.facebook.react.bridge.ColorPropConverter;
import com.facebook.react.uimanager.AbstractC0430g;
import com.facebook.react.uimanager.BaseViewManager;
import com.facebook.react.uimanager.U;

/* JADX INFO: loaded from: classes.dex */
public class a extends AbstractC0430g {
    public a(BaseViewManager<Object, ? extends U> baseViewManager) {
        super(baseViewManager);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.facebook.react.uimanager.AbstractC0430g, com.facebook.react.uimanager.Q0
    public void b(View view, String str, Object obj) {
        str.hashCode();
        switch (str) {
            case "blurAmount":
                ((b) this.f7477a).setBlurAmount(view, obj == null ? 10 : ((Double) obj).intValue());
                break;
            case "enabled":
                ((b) this.f7477a).setEnabled(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "blurRadius":
                ((b) this.f7477a).setBlurRadius(view, obj != null ? ((Double) obj).intValue() : 0);
                break;
            case "blurType":
                ((b) this.f7477a).setBlurType(view, (String) obj);
                break;
            case "autoUpdate":
                ((b) this.f7477a).setAutoUpdate(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "overlayColor":
                ((b) this.f7477a).setOverlayColor(view, ColorPropConverter.getColor(obj, view.getContext()));
                break;
            case "downsampleFactor":
                ((b) this.f7477a).setDownsampleFactor(view, obj != null ? ((Double) obj).intValue() : 0);
                break;
            default:
                super.b(view, str, obj);
                break;
        }
    }
}
