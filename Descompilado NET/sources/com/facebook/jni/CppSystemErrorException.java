package com.facebook.jni;

/* JADX INFO: loaded from: classes.dex */
public class CppSystemErrorException extends CppException {
    int errorCode;

    public CppSystemErrorException(String str, int i3) {
        super(str);
        this.errorCode = i3;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
