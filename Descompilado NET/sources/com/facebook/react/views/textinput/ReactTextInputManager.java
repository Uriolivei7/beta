package com.facebook.react.views.textinput;

import a1.C0210a;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.react.animated.NativeAnimatedModule;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.common.mapbuffer.ReadableMapBuffer;
import com.facebook.react.uimanager.A0;
import com.facebook.react.uimanager.AbstractC0465y;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.BaseViewManager;
import com.facebook.react.uimanager.C0418a;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.C0454s0;
import com.facebook.react.uimanager.H0;
import com.facebook.react.uimanager.U;
import com.facebook.react.uimanager.W;
import com.facebook.react.uimanager.X;
import com.facebook.react.uimanager.events.EventDispatcher;
import e1.C0527d;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import org.chromium.support_lib_boundary.WebSettingsBoundaryInterface;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = ReactTextInputManager.REACT_CLASS)
public class ReactTextInputManager extends BaseViewManager<C0478j, U> {
    private static final int AUTOCAPITALIZE_FLAGS = 28672;
    private static final int BLUR_TEXT_INPUT = 2;
    private static final int FOCUS_TEXT_INPUT = 1;
    private static final int IME_ACTION_ID = 1648;
    private static final int INPUT_TYPE_KEYBOARD_DECIMAL_PAD = 8194;
    private static final int INPUT_TYPE_KEYBOARD_NUMBERED = 12290;
    private static final int INPUT_TYPE_KEYBOARD_NUMBER_PAD = 2;
    private static final String KEYBOARD_TYPE_DECIMAL_PAD = "decimal-pad";
    private static final String KEYBOARD_TYPE_EMAIL_ADDRESS = "email-address";
    private static final String KEYBOARD_TYPE_NUMBER_PAD = "number-pad";
    private static final String KEYBOARD_TYPE_NUMERIC = "numeric";
    private static final String KEYBOARD_TYPE_PHONE_PAD = "phone-pad";
    private static final String KEYBOARD_TYPE_URI = "url";
    private static final String KEYBOARD_TYPE_VISIBLE_PASSWORD = "visible-password";
    private static final int PASSWORD_VISIBILITY_FLAG = 16;
    public static final String REACT_CLASS = "AndroidTextInput";
    private static final int SET_TEXT_AND_SELECTION = 4;
    public static final String TAG = "ReactTextInputManager";
    private static final short TX_STATE_KEY_ATTRIBUTED_STRING = 0;
    private static final short TX_STATE_KEY_HASH = 2;
    private static final short TX_STATE_KEY_MOST_RECENT_EVENT_COUNT = 3;
    private static final short TX_STATE_KEY_PARAGRAPH_ATTRIBUTES = 1;
    private static final int UNSET = -1;
    protected com.facebook.react.views.text.o mReactTextViewManagerCallback;
    private static final int SET_MOST_RECENT_EVENT_COUNT = 3;
    private static final int[] SPACING_TYPES = {8, 0, 2, 1, SET_MOST_RECENT_EVENT_COUNT};
    private static final Map<String, String> REACT_PROPS_AUTOFILL_HINTS_MAP = new a();
    private static final InputFilter[] EMPTY_FILTERS = new InputFilter[0];
    private static final String[] DRAWABLE_HANDLE_RESOURCES = {"mTextSelectHandleLeftRes", "mTextSelectHandleRightRes", "mTextSelectHandleRes"};
    private static final String[] DRAWABLE_HANDLE_FIELDS = {"mSelectHandleLeft", "mSelectHandleRight", "mSelectHandleCenter"};

    class a extends HashMap {
        a() {
            put("birthdate-day", "birthDateDay");
            put("birthdate-full", "birthDateFull");
            put("birthdate-month", "birthDateMonth");
            put("birthdate-year", "birthDateYear");
            put("cc-csc", "creditCardSecurityCode");
            put("cc-exp", "creditCardExpirationDate");
            put("cc-exp-day", "creditCardExpirationDay");
            put("cc-exp-month", "creditCardExpirationMonth");
            put("cc-exp-year", "creditCardExpirationYear");
            put("cc-number", "creditCardNumber");
            put("email", "emailAddress");
            put("gender", "gender");
            put("name", "personName");
            put("name-family", "personFamilyName");
            put("name-given", "personGivenName");
            put("name-middle", "personMiddleName");
            put("name-middle-initial", "personMiddleInitial");
            put("name-prefix", "personNamePrefix");
            put("name-suffix", "personNameSuffix");
            put("password", "password");
            put("password-new", "newPassword");
            put("postal-address", "postalAddress");
            put("postal-address-country", "addressCountry");
            put("postal-address-extended", "extendedAddress");
            put("postal-address-extended-postal-code", "extendedPostalCode");
            put("postal-address-locality", "addressLocality");
            put("postal-address-region", "addressRegion");
            put("postal-code", "postalCode");
            put("street-address", "streetAddress");
            put("sms-otp", "smsOTPCode");
            put("tel", "phoneNumber");
            put("tel-country-code", "phoneCountryCode");
            put("tel-national", "phoneNational");
            put("tel-device", "phoneNumberDevice");
            put("username", "username");
            put("username-new", "newUsername");
        }
    }

    private static class b implements InterfaceC0469a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final C0478j f8074a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final EventDispatcher f8075b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int f8076c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private int f8077d = 0;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private int f8078e = 0;

        public b(C0478j c0478j) {
            this.f8074a = c0478j;
            ReactContext reactContextD = H0.d(c0478j);
            this.f8075b = ReactTextInputManager.getEventDispatcher(reactContextD, c0478j);
            this.f8076c = H0.e(reactContextD);
        }

        @Override // com.facebook.react.views.textinput.InterfaceC0469a
        public void a() {
            if (this.f8075b == null) {
                return;
            }
            int width = this.f8074a.getWidth();
            int height = this.f8074a.getHeight();
            if (this.f8074a.getLayout() != null) {
                width = this.f8074a.getCompoundPaddingLeft() + this.f8074a.getLayout().getWidth() + this.f8074a.getCompoundPaddingRight();
                height = this.f8074a.getCompoundPaddingTop() + this.f8074a.getLayout().getHeight() + this.f8074a.getCompoundPaddingBottom();
            }
            if (width == this.f8077d && height == this.f8078e) {
                return;
            }
            this.f8078e = height;
            this.f8077d = width;
            this.f8075b.b(new C0470b(this.f8076c, this.f8074a.getId(), C0429f0.f(width), C0429f0.f(height)));
        }
    }

    private static class c implements J {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final C0478j f8079a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final EventDispatcher f8080b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int f8081c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private int f8082d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private int f8083e;

        public c(C0478j c0478j) {
            this.f8079a = c0478j;
            ReactContext reactContextD = H0.d(c0478j);
            this.f8080b = ReactTextInputManager.getEventDispatcher(reactContextD, c0478j);
            this.f8081c = H0.e(reactContextD);
        }

        @Override // com.facebook.react.views.textinput.J
        public void a(int i3, int i4, int i5, int i6) {
            if (this.f8082d == i3 && this.f8083e == i4) {
                return;
            }
            this.f8080b.b(com.facebook.react.views.scroll.k.x(this.f8081c, this.f8079a.getId(), com.facebook.react.views.scroll.l.f7890e, i3, i4, 0.0f, 0.0f, 0, 0, this.f8079a.getWidth(), this.f8079a.getHeight()));
            this.f8082d = i3;
            this.f8083e = i4;
        }
    }

    private static class d implements K {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final C0478j f8084a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final EventDispatcher f8085b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int f8086c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private int f8087d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private int f8088e;

        public d(C0478j c0478j) {
            this.f8084a = c0478j;
            ReactContext reactContextD = H0.d(c0478j);
            this.f8085b = ReactTextInputManager.getEventDispatcher(reactContextD, c0478j);
            this.f8086c = H0.e(reactContextD);
        }

        @Override // com.facebook.react.views.textinput.K
        public void a(int i3, int i4) {
            int iMin = Math.min(i3, i4);
            int iMax = Math.max(i3, i4);
            if (this.f8087d == iMin && this.f8088e == iMax) {
                return;
            }
            this.f8085b.b(new G(this.f8086c, this.f8084a.getId(), iMin, iMax));
            this.f8087d = iMin;
            this.f8088e = iMax;
        }
    }

    private final class e implements TextWatcher {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final C0478j f8089b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final EventDispatcher f8090c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final int f8091d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private String f8092e = null;

        public e(ReactContext reactContext, C0478j c0478j) {
            this.f8090c = ReactTextInputManager.getEventDispatcher(reactContext, c0478j);
            this.f8089b = c0478j;
            this.f8091d = H0.e(reactContext);
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
            this.f8092e = charSequence.toString();
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
            if (this.f8089b.f8117M) {
                return;
            }
            if (i5 == 0 && i4 == 0) {
                return;
            }
            C0210a.c(this.f8092e);
            String strSubstring = charSequence.toString().substring(i3, i3 + i5);
            String strSubstring2 = this.f8092e.substring(i3, i3 + i4);
            if (i5 == i4 && strSubstring.equals(strSubstring2)) {
                return;
            }
            A0 stateWrapper = this.f8089b.getStateWrapper();
            if (stateWrapper != null) {
                WritableNativeMap writableNativeMap = new WritableNativeMap();
                writableNativeMap.putInt("mostRecentEventCount", this.f8089b.A());
                writableNativeMap.putInt("opaqueCacheId", this.f8089b.getId());
                stateWrapper.b(writableNativeMap);
            }
            this.f8090c.b(new m(this.f8091d, this.f8089b.getId(), charSequence.toString(), this.f8089b.A()));
        }
    }

    private static void checkPasswordType(C0478j c0478j) {
        if ((c0478j.getStagedInputType() & INPUT_TYPE_KEYBOARD_NUMBERED) == 0 || (c0478j.getStagedInputType() & 128) == 0) {
            return;
        }
        updateStagedInputTypeFlag(c0478j, 128, PASSWORD_VISIBILITY_FLAG);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static EventDispatcher getEventDispatcher(ReactContext reactContext, C0478j c0478j) {
        return H0.c(reactContext, c0478j.getId());
    }

    private com.facebook.react.views.text.i getReactTextUpdate(String str, int i3) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append((CharSequence) com.facebook.react.views.text.u.b(str, com.facebook.react.views.text.u.f8058g));
        return new com.facebook.react.views.text.i(spannableStringBuilder, i3, false, 0.0f, 0.0f, 0.0f, 0.0f, 0, 0, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$addEventEmitters$0(B0 b02, C0478j c0478j, View view, boolean z3) {
        int iC = b02.c();
        EventDispatcher eventDispatcher = getEventDispatcher(b02, c0478j);
        if (z3) {
            eventDispatcher.b(new p(iC, c0478j.getId()));
        } else {
            eventDispatcher.b(new n(iC, c0478j.getId()));
            eventDispatcher.b(new o(iC, c0478j.getId(), c0478j.getText().toString()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$addEventEmitters$1(C0478j c0478j, B0 b02, TextView textView, int i3, KeyEvent keyEvent) {
        if ((i3 & 255) == 0 && i3 != 0) {
            return true;
        }
        boolean zB = c0478j.B();
        boolean Z3 = c0478j.Z();
        boolean zY = c0478j.Y();
        if (Z3) {
            getEventDispatcher(b02, c0478j).b(new I(b02.c(), c0478j.getId(), c0478j.getText().toString()));
        }
        if (zY) {
            c0478j.clearFocus();
        }
        return zY || Z3 || !zB || i3 == 5 || i3 == 7;
    }

    private void setAutofillHints(C0478j c0478j, String... strArr) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        c0478j.setAutofillHints(strArr);
    }

    private static boolean shouldHideCursorForEmailTextInput() {
        return Build.VERSION.SDK_INT == 29 && Build.MANUFACTURER.toLowerCase(Locale.ROOT).contains("xiaomi");
    }

    private static void updateStagedInputTypeFlag(C0478j c0478j, int i3, int i4) {
        c0478j.setStagedInputType(((~i3) & c0478j.getStagedInputType()) | i4);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Map<String, Integer> getCommandsMap() {
        return C0527d.e("focusTextInput", 1, "blurTextInput", 2);
    }

    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public Map<String, Object> getExportedCustomBubblingEventTypeConstants() {
        Map<String, Object> exportedCustomBubblingEventTypeConstants = super.getExportedCustomBubblingEventTypeConstants();
        if (exportedCustomBubblingEventTypeConstants == null) {
            exportedCustomBubblingEventTypeConstants = new HashMap<>();
        }
        exportedCustomBubblingEventTypeConstants.putAll(C0527d.a().b("topSubmitEditing", C0527d.d("phasedRegistrationNames", C0527d.e("bubbled", "onSubmitEditing", "captured", "onSubmitEditingCapture"))).b("topEndEditing", C0527d.d("phasedRegistrationNames", C0527d.e("bubbled", "onEndEditing", "captured", "onEndEditingCapture"))).b("topFocus", C0527d.d("phasedRegistrationNames", C0527d.e("bubbled", "onFocus", "captured", "onFocusCapture"))).b("topBlur", C0527d.d("phasedRegistrationNames", C0527d.e("bubbled", "onBlur", "captured", "onBlurCapture"))).b("topKeyPress", C0527d.d("phasedRegistrationNames", C0527d.e("bubbled", "onKeyPress", "captured", "onKeyPressCapture"))).a());
        return exportedCustomBubblingEventTypeConstants;
    }

    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        Map<String, Object> exportedCustomDirectEventTypeConstants = super.getExportedCustomDirectEventTypeConstants();
        if (exportedCustomDirectEventTypeConstants == null) {
            exportedCustomDirectEventTypeConstants = new HashMap<>();
        }
        exportedCustomDirectEventTypeConstants.putAll(C0527d.a().b(com.facebook.react.views.scroll.l.b(com.facebook.react.views.scroll.l.f7890e), C0527d.d("registrationName", "onScroll")).a());
        return exportedCustomDirectEventTypeConstants;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Map<String, Object> getExportedViewConstants() {
        return C0527d.d("AutoCapitalizationType", C0527d.g("none", 0, "characters", 4096, "words", 8192, "sentences", 16384));
    }

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Class<? extends U> getShadowNodeClass() {
        return H.class;
    }

    @L1.a(defaultBoolean = true, name = "allowFontScaling")
    public void setAllowFontScaling(C0478j c0478j, boolean z3) {
        c0478j.setAllowFontScaling(z3);
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:29:0x005a  */
    @L1.a(name = "autoCapitalize")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void setAutoCapitalize(com.facebook.react.views.textinput.C0478j r5, com.facebook.react.bridge.Dynamic r6) {
        /*
            r4 = this;
            r0 = 0
            com.facebook.react.bridge.ReadableType r1 = r6.getType()
            com.facebook.react.bridge.ReadableType r2 = com.facebook.react.bridge.ReadableType.Number
            if (r1 != r2) goto Le
            int r0 = r6.asInt()
            goto L5e
        Le:
            com.facebook.react.bridge.ReadableType r1 = r6.getType()
            com.facebook.react.bridge.ReadableType r2 = com.facebook.react.bridge.ReadableType.String
            r3 = 16384(0x4000, float:2.2959E-41)
            if (r1 != r2) goto L5a
            java.lang.String r6 = r6.asString()
            r6.hashCode()
            r1 = -1
            int r2 = r6.hashCode()
            switch(r2) {
                case 3387192: goto L49;
                case 113318569: goto L3e;
                case 490141296: goto L33;
                case 1245424234: goto L28;
                default: goto L27;
            }
        L27:
            goto L53
        L28:
            java.lang.String r2 = "characters"
            boolean r6 = r6.equals(r2)
            if (r6 != 0) goto L31
            goto L53
        L31:
            r1 = 3
            goto L53
        L33:
            java.lang.String r2 = "sentences"
            boolean r6 = r6.equals(r2)
            if (r6 != 0) goto L3c
            goto L53
        L3c:
            r1 = 2
            goto L53
        L3e:
            java.lang.String r2 = "words"
            boolean r6 = r6.equals(r2)
            if (r6 != 0) goto L47
            goto L53
        L47:
            r1 = 1
            goto L53
        L49:
            java.lang.String r2 = "none"
            boolean r6 = r6.equals(r2)
            if (r6 != 0) goto L52
            goto L53
        L52:
            r1 = r0
        L53:
            switch(r1) {
                case 0: goto L5e;
                case 1: goto L5c;
                case 2: goto L5a;
                case 3: goto L57;
                default: goto L56;
            }
        L56:
            goto L5a
        L57:
            r0 = 4096(0x1000, float:5.74E-42)
            goto L5e
        L5a:
            r0 = r3
            goto L5e
        L5c:
            r0 = 8192(0x2000, float:1.148E-41)
        L5e:
            r6 = 28672(0x7000, float:4.0178E-41)
            updateStagedInputTypeFlag(r5, r6, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.views.textinput.ReactTextInputManager.setAutoCapitalize(com.facebook.react.views.textinput.j, com.facebook.react.bridge.Dynamic):void");
    }

    @L1.a(name = "autoCorrect")
    public void setAutoCorrect(C0478j c0478j, Boolean bool) {
        updateStagedInputTypeFlag(c0478j, 557056, bool != null ? bool.booleanValue() ? 32768 : 524288 : 0);
    }

    @L1.a(defaultBoolean = NativeAnimatedModule.ANIMATED_MODULE_DEBUG, name = "autoFocus")
    public void setAutoFocus(C0478j c0478j, boolean z3) {
        c0478j.setAutoFocus(z3);
    }

    @L1.b(customType = "Color", names = {"borderColor", "borderLeftColor", "borderRightColor", "borderTopColor", "borderBottomColor"})
    public void setBorderColor(C0478j c0478j, int i3, Integer num) {
        C0418a.p(c0478j, R1.n.f2075c, num);
    }

    @L1.b(defaultFloat = Float.NaN, names = {"borderRadius", "borderTopLeftRadius", "borderTopRightRadius", "borderBottomRightRadius", "borderBottomLeftRadius"})
    public void setBorderRadius(C0478j c0478j, int i3, float f3) {
        C0418a.q(c0478j, R1.d.values()[i3], Float.isNaN(f3) ? null : new W(f3, X.f7408b));
    }

    @L1.a(name = "borderStyle")
    public void setBorderStyle(C0478j c0478j, String str) {
        C0418a.r(c0478j, str == null ? null : R1.f.b(str));
    }

    @L1.b(defaultFloat = Float.NaN, names = {"borderWidth", "borderLeftWidth", "borderRightWidth", "borderTopWidth", "borderBottomWidth"})
    public void setBorderWidth(C0478j c0478j, int i3, float f3) {
        C0418a.s(c0478j, R1.n.values()[i3], Float.valueOf(f3));
    }

    @L1.a(defaultBoolean = NativeAnimatedModule.ANIMATED_MODULE_DEBUG, name = "caretHidden")
    public void setCaretHidden(C0478j c0478j, boolean z3) {
        if (c0478j.getStagedInputType() == 32 && shouldHideCursorForEmailTextInput()) {
            return;
        }
        c0478j.setCursorVisible(!z3);
    }

    @L1.a(customType = "Color", name = "color")
    public void setColor(C0478j c0478j, Integer num) {
        if (num != null) {
            c0478j.setTextColor(num.intValue());
            return;
        }
        ColorStateList colorStateListB = com.facebook.react.views.text.a.b(c0478j.getContext());
        if (colorStateListB != null) {
            c0478j.setTextColor(colorStateListB);
            return;
        }
        Context context = c0478j.getContext();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Could not get default text color from View Context: ");
        sb.append(context != null ? context.getClass().getCanonicalName() : "null");
        ReactSoftExceptionLogger.logSoftException(str, new IllegalStateException(sb.toString()));
    }

    @L1.a(defaultBoolean = NativeAnimatedModule.ANIMATED_MODULE_DEBUG, name = "contextMenuHidden")
    public void setContextMenuHidden(C0478j c0478j, boolean z3) {
        c0478j.setContextMenuHidden(z3);
    }

    @L1.a(customType = "Color", name = "cursorColor")
    public void setCursorColor(C0478j c0478j, Integer num) {
        int i3 = Build.VERSION.SDK_INT;
        if (i3 >= 29) {
            Drawable textCursorDrawable = c0478j.getTextCursorDrawable();
            if (textCursorDrawable != null) {
                if (num != null) {
                    com.facebook.react.uimanager.B.a();
                    textCursorDrawable.setColorFilter(AbstractC0465y.a(num.intValue(), BlendMode.SRC_IN));
                } else {
                    textCursorDrawable.clearColorFilter();
                }
                c0478j.setTextCursorDrawable(textCursorDrawable);
                return;
            }
            return;
        }
        if (i3 == 28) {
            return;
        }
        try {
            Field declaredField = c0478j.getClass().getDeclaredField("mCursorDrawableRes");
            declaredField.setAccessible(true);
            int i4 = declaredField.getInt(c0478j);
            if (i4 == 0) {
                return;
            }
            Drawable drawableMutate = androidx.core.content.a.d(c0478j.getContext(), i4).mutate();
            if (num != null) {
                drawableMutate.setColorFilter(num.intValue(), PorterDuff.Mode.SRC_IN);
            } else {
                drawableMutate.clearColorFilter();
            }
            Field declaredField2 = TextView.class.getDeclaredField("mEditor");
            declaredField2.setAccessible(true);
            Object obj = declaredField2.get(c0478j);
            Field declaredField3 = obj.getClass().getDeclaredField("mCursorDrawable");
            declaredField3.setAccessible(true);
            declaredField3.set(obj, new Drawable[]{drawableMutate, drawableMutate});
        } catch (IllegalAccessException | NoSuchFieldException unused) {
        }
    }

    @L1.a(defaultBoolean = NativeAnimatedModule.ANIMATED_MODULE_DEBUG, name = "disableFullscreenUI")
    public void setDisableFullscreenUI(C0478j c0478j, boolean z3) {
        c0478j.setDisableFullscreenUI(z3);
    }

    @L1.a(defaultBoolean = true, name = "editable")
    public void setEditable(C0478j c0478j, boolean z3) {
        c0478j.setEnabled(z3);
    }

    @L1.a(name = "fontFamily")
    public void setFontFamily(C0478j c0478j, String str) {
        c0478j.setFontFamily(str);
    }

    @L1.a(defaultFloat = 14.0f, name = "fontSize")
    public void setFontSize(C0478j c0478j, float f3) {
        c0478j.setFontSize(f3);
    }

    @L1.a(name = "fontStyle")
    public void setFontStyle(C0478j c0478j, String str) {
        c0478j.setFontStyle(str);
    }

    @L1.a(name = "fontVariant")
    public void setFontVariant(C0478j c0478j, ReadableArray readableArray) {
        c0478j.setFontFeatureSettings(com.facebook.react.views.text.p.c(readableArray));
    }

    @L1.a(name = "fontWeight")
    public void setFontWeight(C0478j c0478j, String str) {
        c0478j.setFontWeight(str);
    }

    @L1.a(name = "importantForAutofill")
    public void setImportantForAutofill(C0478j c0478j, String str) {
        setImportantForAutofill(c0478j, "no".equals(str) ? 2 : "noExcludeDescendants".equals(str) ? 8 : "yes".equals(str) ? 1 : "yesExcludeDescendants".equals(str) ? SET_TEXT_AND_SELECTION : 0);
    }

    @L1.a(defaultBoolean = true, name = "includeFontPadding")
    public void setIncludeFontPadding(C0478j c0478j, boolean z3) {
        c0478j.setIncludeFontPadding(z3);
    }

    @L1.a(name = "inlineImageLeft")
    public void setInlineImageLeft(C0478j c0478j, String str) {
        c0478j.setCompoundDrawablesWithIntrinsicBounds(X1.c.d().f(c0478j.getContext(), str), 0, 0, 0);
    }

    @L1.a(name = "inlineImagePadding")
    public void setInlineImagePadding(C0478j c0478j, int i3) {
        c0478j.setCompoundDrawablePadding(i3);
    }

    @L1.a(name = "keyboardType")
    public void setKeyboardType(C0478j c0478j, String str) {
        int i3;
        if (KEYBOARD_TYPE_NUMERIC.equalsIgnoreCase(str)) {
            i3 = INPUT_TYPE_KEYBOARD_NUMBERED;
        } else if (KEYBOARD_TYPE_NUMBER_PAD.equalsIgnoreCase(str)) {
            i3 = 2;
        } else if (KEYBOARD_TYPE_DECIMAL_PAD.equalsIgnoreCase(str)) {
            i3 = INPUT_TYPE_KEYBOARD_DECIMAL_PAD;
        } else if (KEYBOARD_TYPE_EMAIL_ADDRESS.equalsIgnoreCase(str)) {
            if (shouldHideCursorForEmailTextInput()) {
                c0478j.setCursorVisible(false);
            }
            i3 = 33;
        } else {
            i3 = KEYBOARD_TYPE_PHONE_PAD.equalsIgnoreCase(str) ? SET_MOST_RECENT_EVENT_COUNT : KEYBOARD_TYPE_VISIBLE_PASSWORD.equalsIgnoreCase(str) ? 144 : KEYBOARD_TYPE_URI.equalsIgnoreCase(str) ? PASSWORD_VISIBILITY_FLAG : 1;
        }
        updateStagedInputTypeFlag(c0478j, 15, i3);
        checkPasswordType(c0478j);
    }

    @L1.a(defaultFloat = 0.0f, name = "letterSpacing")
    public void setLetterSpacing(C0478j c0478j, float f3) {
        c0478j.setLetterSpacingPt(f3);
    }

    @L1.a(defaultFloat = 0.0f, name = "lineHeight")
    public void setLineHeight(C0478j c0478j, int i3) {
        c0478j.setLineHeight(i3);
    }

    @L1.a(defaultFloat = Float.NaN, name = "maxFontSizeMultiplier")
    public void setMaxFontSizeMultiplier(C0478j c0478j, float f3) {
        c0478j.setMaxFontSizeMultiplier(f3);
    }

    @L1.a(name = "maxLength")
    public void setMaxLength(C0478j c0478j, Integer num) {
        InputFilter[] filters = c0478j.getFilters();
        InputFilter[] inputFilterArr = EMPTY_FILTERS;
        if (num == null) {
            if (filters.length > 0) {
                LinkedList linkedList = new LinkedList();
                for (InputFilter inputFilter : filters) {
                    if (!(inputFilter instanceof InputFilter.LengthFilter)) {
                        linkedList.add(inputFilter);
                    }
                }
                if (!linkedList.isEmpty()) {
                    inputFilterArr = (InputFilter[]) linkedList.toArray(new InputFilter[linkedList.size()]);
                }
            }
        } else if (filters.length > 0) {
            boolean z3 = false;
            for (int i3 = 0; i3 < filters.length; i3++) {
                if (filters[i3] instanceof InputFilter.LengthFilter) {
                    filters[i3] = new InputFilter.LengthFilter(num.intValue());
                    z3 = true;
                }
            }
            if (!z3) {
                InputFilter[] inputFilterArr2 = new InputFilter[filters.length + 1];
                System.arraycopy(filters, 0, inputFilterArr2, 0, filters.length);
                filters[filters.length] = new InputFilter.LengthFilter(num.intValue());
                filters = inputFilterArr2;
            }
            inputFilterArr = filters;
        } else {
            inputFilterArr = new InputFilter[]{new InputFilter.LengthFilter(num.intValue())};
        }
        c0478j.setFilters(inputFilterArr);
    }

    @L1.a(defaultBoolean = NativeAnimatedModule.ANIMATED_MODULE_DEBUG, name = "multiline")
    public void setMultiline(C0478j c0478j, boolean z3) {
        updateStagedInputTypeFlag(c0478j, z3 ? 0 : 131072, z3 ? 131072 : 0);
    }

    @L1.a(defaultInt = 1, name = "numberOfLines")
    public void setNumLines(C0478j c0478j, int i3) {
        c0478j.setLines(i3);
    }

    @L1.a(defaultBoolean = NativeAnimatedModule.ANIMATED_MODULE_DEBUG, name = "onContentSizeChange")
    public void setOnContentSizeChange(C0478j c0478j, boolean z3) {
        if (z3) {
            c0478j.setContentSizeWatcher(new b(c0478j));
        } else {
            c0478j.setContentSizeWatcher(null);
        }
    }

    @L1.a(defaultBoolean = NativeAnimatedModule.ANIMATED_MODULE_DEBUG, name = "onKeyPress")
    public void setOnKeyPress(C0478j c0478j, boolean z3) {
        c0478j.setOnKeyPress(z3);
    }

    @L1.a(defaultBoolean = NativeAnimatedModule.ANIMATED_MODULE_DEBUG, name = "onScroll")
    public void setOnScroll(C0478j c0478j, boolean z3) {
        if (z3) {
            c0478j.setScrollWatcher(new c(c0478j));
        } else {
            c0478j.setScrollWatcher(null);
        }
    }

    @L1.a(defaultBoolean = NativeAnimatedModule.ANIMATED_MODULE_DEBUG, name = "onSelectionChange")
    public void setOnSelectionChange(C0478j c0478j, boolean z3) {
        if (z3) {
            c0478j.setSelectionWatcher(new d(c0478j));
        } else {
            c0478j.setSelectionWatcher(null);
        }
    }

    @L1.a(name = "overflow")
    public void setOverflow(C0478j c0478j, String str) {
        c0478j.setOverflow(str);
    }

    @L1.a(name = "placeholder")
    public void setPlaceholder(C0478j c0478j, String str) {
        c0478j.setPlaceholder(str);
    }

    @L1.a(customType = "Color", name = "placeholderTextColor")
    public void setPlaceholderTextColor(C0478j c0478j, Integer num) {
        if (num == null) {
            c0478j.setHintTextColor(com.facebook.react.views.text.a.d(c0478j.getContext()));
        } else {
            c0478j.setHintTextColor(num.intValue());
        }
    }

    @L1.a(name = "returnKeyLabel")
    public void setReturnKeyLabel(C0478j c0478j, String str) {
        c0478j.setImeActionLabel(str, IME_ACTION_ID);
    }

    @L1.a(name = "returnKeyType")
    public void setReturnKeyType(C0478j c0478j, String str) {
        c0478j.setReturnKeyType(str);
    }

    @L1.a(defaultBoolean = NativeAnimatedModule.ANIMATED_MODULE_DEBUG, name = "secureTextEntry")
    public void setSecureTextEntry(C0478j c0478j, boolean z3) {
        updateStagedInputTypeFlag(c0478j, 144, z3 ? 128 : 0);
        checkPasswordType(c0478j);
    }

    @L1.a(defaultBoolean = NativeAnimatedModule.ANIMATED_MODULE_DEBUG, name = "selectTextOnFocus")
    public void setSelectTextOnFocus(C0478j c0478j, boolean z3) {
        c0478j.setSelectTextOnFocus(z3);
    }

    @L1.a(customType = "Color", name = "selectionColor")
    public void setSelectionColor(C0478j c0478j, Integer num) {
        if (num == null) {
            c0478j.setHighlightColor(com.facebook.react.views.text.a.c(c0478j.getContext()));
        } else {
            c0478j.setHighlightColor(num.intValue());
        }
    }

    @L1.a(customType = "Color", name = "selectionHandleColor")
    public void setSelectionHandleColor(C0478j c0478j, Integer num) {
        int i3;
        int i4 = Build.VERSION.SDK_INT;
        if (i4 >= 29) {
            Drawable drawableMutate = c0478j.getTextSelectHandle().mutate();
            Drawable drawableMutate2 = c0478j.getTextSelectHandleLeft().mutate();
            Drawable drawableMutate3 = c0478j.getTextSelectHandleRight().mutate();
            if (num != null) {
                com.facebook.react.uimanager.B.a();
                BlendModeColorFilter blendModeColorFilterA = AbstractC0465y.a(num.intValue(), BlendMode.SRC_IN);
                drawableMutate.setColorFilter(blendModeColorFilterA);
                drawableMutate2.setColorFilter(blendModeColorFilterA);
                drawableMutate3.setColorFilter(blendModeColorFilterA);
            } else {
                drawableMutate.clearColorFilter();
                drawableMutate2.clearColorFilter();
                drawableMutate3.clearColorFilter();
            }
            c0478j.setTextSelectHandle(drawableMutate);
            c0478j.setTextSelectHandleLeft(drawableMutate2);
            c0478j.setTextSelectHandleRight(drawableMutate3);
            return;
        }
        if (i4 == 28) {
            return;
        }
        int i5 = 0;
        while (true) {
            String[] strArr = DRAWABLE_HANDLE_RESOURCES;
            if (i5 >= strArr.length) {
                return;
            }
            try {
                Field declaredField = c0478j.getClass().getDeclaredField(strArr[i5]);
                declaredField.setAccessible(true);
                i3 = declaredField.getInt(c0478j);
            } catch (IllegalAccessException | NoSuchFieldException unused) {
            }
            if (i3 == 0) {
                return;
            }
            Drawable drawableMutate4 = androidx.core.content.a.d(c0478j.getContext(), i3).mutate();
            if (num != null) {
                drawableMutate4.setColorFilter(num.intValue(), PorterDuff.Mode.SRC_IN);
            } else {
                drawableMutate4.clearColorFilter();
            }
            Field declaredField2 = TextView.class.getDeclaredField("mEditor");
            declaredField2.setAccessible(true);
            Object obj = declaredField2.get(c0478j);
            Field declaredField3 = obj.getClass().getDeclaredField(DRAWABLE_HANDLE_FIELDS[i5]);
            declaredField3.setAccessible(true);
            declaredField3.set(obj, drawableMutate4);
            i5++;
        }
    }

    @L1.a(name = "submitBehavior")
    public void setSubmitBehavior(C0478j c0478j, String str) {
        c0478j.setSubmitBehavior(str);
    }

    @L1.a(name = "textAlign")
    public void setTextAlign(C0478j c0478j, String str) {
        if ("justify".equals(str)) {
            if (Build.VERSION.SDK_INT >= 26) {
                c0478j.setJustificationMode(1);
            }
            c0478j.setGravityHorizontal(SET_MOST_RECENT_EVENT_COUNT);
            return;
        }
        if (Build.VERSION.SDK_INT >= 26) {
            c0478j.setJustificationMode(0);
        }
        if (str == null || "auto".equals(str)) {
            c0478j.setGravityHorizontal(0);
            return;
        }
        if ("left".equals(str)) {
            c0478j.setGravityHorizontal(SET_MOST_RECENT_EVENT_COUNT);
            return;
        }
        if ("right".equals(str)) {
            c0478j.setGravityHorizontal(5);
            return;
        }
        if ("center".equals(str)) {
            c0478j.setGravityHorizontal(1);
            return;
        }
        Y.a.I("ReactNative", "Invalid textAlign: " + str);
        c0478j.setGravityHorizontal(0);
    }

    @L1.a(name = "textAlignVertical")
    public void setTextAlignVertical(C0478j c0478j, String str) {
        if (str == null || "auto".equals(str)) {
            c0478j.setGravityVertical(0);
            return;
        }
        if ("top".equals(str)) {
            c0478j.setGravityVertical(48);
            return;
        }
        if ("bottom".equals(str)) {
            c0478j.setGravityVertical(80);
            return;
        }
        if ("center".equals(str)) {
            c0478j.setGravityVertical(PASSWORD_VISIBILITY_FLAG);
            return;
        }
        Y.a.I("ReactNative", "Invalid textAlignVertical: " + str);
        c0478j.setGravityVertical(0);
    }

    @L1.a(name = "autoComplete")
    public void setTextContentType(C0478j c0478j, String str) {
        if (str == null) {
            setImportantForAutofill(c0478j, 2);
            return;
        }
        if ("off".equals(str)) {
            setImportantForAutofill(c0478j, 2);
            return;
        }
        Map<String, String> map = REACT_PROPS_AUTOFILL_HINTS_MAP;
        if (map.containsKey(str)) {
            setAutofillHints(c0478j, map.get(str));
            return;
        }
        Y.a.I("ReactNative", "Invalid autoComplete: " + str);
        setImportantForAutofill(c0478j, 2);
    }

    @L1.a(name = "textDecorationLine")
    public void setTextDecorationLine(C0478j c0478j, String str) {
        c0478j.setPaintFlags(c0478j.getPaintFlags() & (-25));
        if (str == null) {
            return;
        }
        for (String str2 : str.split(" ")) {
            if (str2.equals("underline")) {
                c0478j.setPaintFlags(c0478j.getPaintFlags() | 8);
            } else if (str2.equals("line-through")) {
                c0478j.setPaintFlags(c0478j.getPaintFlags() | PASSWORD_VISIBILITY_FLAG);
            }
        }
    }

    @L1.a(customType = "Color", name = "underlineColorAndroid")
    public void setUnderlineColor(C0478j c0478j, Integer num) {
        Drawable background = c0478j.getBackground();
        if (background == null) {
            return;
        }
        if (background.getConstantState() != null) {
            try {
                background = background.mutate();
            } catch (NullPointerException e4) {
                Y.a.n(TAG, "NullPointerException when setting underlineColorAndroid for TextInput", e4);
            }
        }
        if (num == null) {
            background.clearColorFilter();
        } else {
            background.setColorFilter(num.intValue(), PorterDuff.Mode.SRC_IN);
        }
    }

    @L1.a(defaultBoolean = true, name = "showSoftInputOnFocus")
    public void showKeyboardOnFocus(C0478j c0478j, boolean z3) {
        c0478j.setShowSoftInputOnFocus(z3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.ViewManager
    public void addEventEmitters(final B0 b02, final C0478j c0478j) {
        c0478j.setEventDispatcher(getEventDispatcher(b02, c0478j));
        c0478j.addTextChangedListener(new e(b02, c0478j));
        c0478j.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.facebook.react.views.textinput.E
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view, boolean z3) {
                ReactTextInputManager.lambda$addEventEmitters$0(b02, c0478j, view, z3);
            }
        });
        c0478j.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.facebook.react.views.textinput.F
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i3, KeyEvent keyEvent) {
                return ReactTextInputManager.lambda$addEventEmitters$1(c0478j, b02, textView, i3, keyEvent);
            }
        });
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public com.facebook.react.views.text.c createShadowNodeInstance() {
        return new H();
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public C0478j createViewInstance(B0 b02) {
        C0478j c0478j = new C0478j(b02);
        c0478j.setInputType(c0478j.getInputType() & (-131073));
        c0478j.setReturnKeyType("done");
        c0478j.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        return c0478j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public void onAfterUpdateTransaction(C0478j c0478j) {
        super.onAfterUpdateTransaction(c0478j);
        c0478j.Q();
        c0478j.y();
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void setPadding(C0478j c0478j, int i3, int i4, int i5, int i6) {
        c0478j.setPadding(i3, i4, i5, i6);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void updateExtraData(C0478j c0478j, Object obj) {
        if (obj instanceof com.facebook.react.views.text.i) {
            com.facebook.react.views.text.i iVar = (com.facebook.react.views.text.i) obj;
            int iF = (int) iVar.f();
            int iH = (int) iVar.h();
            int iG = (int) iVar.g();
            int iE = (int) iVar.e();
            int length = UNSET;
            if (iF != UNSET || iH != UNSET || iG != UNSET || iE != UNSET) {
                if (iF == UNSET) {
                    iF = c0478j.getPaddingLeft();
                }
                if (iH == UNSET) {
                    iH = c0478j.getPaddingTop();
                }
                if (iG == UNSET) {
                    iG = c0478j.getPaddingRight();
                }
                if (iE == UNSET) {
                    iE = c0478j.getPaddingBottom();
                }
                c0478j.setPadding(iF, iH, iG, iE);
            }
            if (iVar.b()) {
                Z1.p.g(iVar.i(), c0478j);
            }
            if (c0478j.getSelectionStart() == c0478j.getSelectionEnd()) {
                length = iVar.i().length() - ((c0478j.getText() != null ? c0478j.getText().length() : 0) - c0478j.getSelectionStart());
            }
            c0478j.P(iVar);
            c0478j.M(iVar.c(), length, length);
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Object updateState(C0478j c0478j, C0454s0 c0454s0, A0 a02) {
        if (C0478j.f8103P) {
            Y.a.m(TAG, "updateState: [" + c0478j.getId() + "]");
        }
        if (c0478j.getStateWrapper() == null) {
            c0478j.setPadding(0, 0, 0, 0);
        }
        c0478j.setStateWrapper(a02);
        ReadableMapBuffer readableMapBufferE = a02.e();
        if (readableMapBufferE != null) {
            return getReactTextUpdate(c0478j, c0454s0, readableMapBufferE);
        }
        return null;
    }

    public com.facebook.react.views.text.c createShadowNodeInstance(com.facebook.react.views.text.o oVar) {
        return new H(oVar);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void receiveCommand(C0478j c0478j, int i3, ReadableArray readableArray) {
        if (i3 == 1) {
            receiveCommand(c0478j, "focus", readableArray);
        } else if (i3 == 2) {
            receiveCommand(c0478j, "blur", readableArray);
        } else {
            if (i3 != SET_TEXT_AND_SELECTION) {
                return;
            }
            receiveCommand(c0478j, "setTextAndSelection", readableArray);
        }
    }

    public Object getReactTextUpdate(C0478j c0478j, C0454s0 c0454s0, com.facebook.react.common.mapbuffer.a aVar) {
        if (aVar.getCount() == 0) {
            return null;
        }
        com.facebook.react.common.mapbuffer.a aVarD = aVar.d(0);
        return com.facebook.react.views.text.i.a(com.facebook.react.views.text.t.g(c0478j.getContext(), aVarD, null), aVar.getInt(SET_MOST_RECENT_EVENT_COUNT), com.facebook.react.views.text.r.l(c0454s0, com.facebook.react.views.text.t.l(aVarD), c0478j.getGravityHorizontal()), com.facebook.react.views.text.r.m(aVar.d(1).getString(2)), com.facebook.react.views.text.r.h(c0454s0, Build.VERSION.SDK_INT >= 26 ? c0478j.getJustificationMode() : 0));
    }

    private void setImportantForAutofill(C0478j c0478j, int i3) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        c0478j.setImportantForAutofill(i3);
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    @Override // com.facebook.react.uimanager.ViewManager
    public void receiveCommand(C0478j c0478j, String str, ReadableArray readableArray) {
        byte b4;
        str.hashCode();
        switch (str.hashCode()) {
            case -1699362314:
                b4 = !str.equals("blurTextInput") ? UNSET : (byte) 0;
                break;
            case 3027047:
                b4 = !str.equals("blur") ? UNSET : (byte) 1;
                break;
            case 97604824:
                b4 = !str.equals("focus") ? UNSET : (byte) 2;
                break;
            case 1427010500:
                b4 = !str.equals("setTextAndSelection") ? UNSET : SET_MOST_RECENT_EVENT_COUNT;
                break;
            case 1690703013:
                b4 = !str.equals("focusTextInput") ? UNSET : (byte) 4;
                break;
            default:
                b4 = UNSET;
                break;
        }
        switch (b4) {
            case WebSettingsBoundaryInterface.ForceDarkBehavior.FORCE_DARK_ONLY /* 0 */:
            case 1:
                c0478j.x();
                break;
            case 2:
            case SET_TEXT_AND_SELECTION /* 4 */:
                c0478j.S();
                break;
            case SET_MOST_RECENT_EVENT_COUNT /* 3 */:
                int i3 = readableArray.getInt(0);
                if (i3 != UNSET) {
                    int i4 = readableArray.getInt(2);
                    int i5 = readableArray.getInt(SET_MOST_RECENT_EVENT_COUNT);
                    if (i5 == UNSET) {
                        i5 = i4;
                    }
                    if (!readableArray.isNull(1)) {
                        c0478j.O(getReactTextUpdate(readableArray.getString(1), i3));
                    }
                    c0478j.M(i3, i4, i5);
                    break;
                }
                break;
        }
    }
}
