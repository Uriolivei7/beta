package org.wonday.orientation;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import d1.O;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class c implements O {
    @Override // d1.O
    public List e(ReactApplicationContext reactApplicationContext) {
        return Arrays.asList(new OrientationModule(reactApplicationContext));
    }

    @Override // d1.O
    public List f(ReactApplicationContext reactApplicationContext) {
        return Arrays.asList(new ViewManager[0]);
    }
}
