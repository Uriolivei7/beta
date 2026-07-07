package com.reactnativecommunity.blurview;

import com.facebook.react.bridge.ReactApplicationContext;
import d1.O;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class b implements O {
    @Override // d1.O
    public List e(ReactApplicationContext reactApplicationContext) {
        return new ArrayList();
    }

    @Override // d1.O
    public List f(ReactApplicationContext reactApplicationContext) {
        return Collections.singletonList(new BlurViewManager(reactApplicationContext));
    }
}
