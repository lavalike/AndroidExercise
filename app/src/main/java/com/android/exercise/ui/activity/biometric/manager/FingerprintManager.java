package com.android.exercise.ui.activity.biometric.manager;

import android.content.Context;
import android.os.Build;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import com.android.exercise.ui.activity.biometric.manager.callback.Fingerprint;
import com.android.exercise.ui.activity.biometric.manager.callback.OnFingerprintCallback;
import com.android.exercise.ui.activity.biometric.manager.impl.FingerprintM;
import com.android.exercise.ui.activity.biometric.manager.impl.FingerprintP;

/**
 * FingerprintManager
 * Created by wangzhen on 2020/6/4.
 */
public class FingerprintManager {
    private Context mContext;
    private OnFingerprintCallback mCallback;

    private FingerprintManager(Context context) {
        this.mContext = context;
    }

    public static FingerprintManager from(Context context) {
        return new FingerprintManager(context);
    }

    public FingerprintManager callback(OnFingerprintCallback callback) {
        this.mCallback = callback;
        return this;
    }

    public void auth() {
        Fingerprint fingerprint = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            fingerprint = new FingerprintP(mContext, mCallback);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprint = new FingerprintM(mContext, mCallback);
        }
        if (fingerprint != null && isSupported()) {
            if (hasEnrolledFingerprints()) {
                fingerprint.authenticate();
            } else {
                if (mCallback != null) {
                    mCallback.noFingerprint();
                }
            }
        } else {
            if (mCallback != null) {
                mCallback.notSupport();
            }
        }
    }

    /**
     * is fingerprint supported
     *
     * @return is supported
     */
    public boolean isSupported() {
        return FingerprintManagerCompat.from(mContext).isHardwareDetected();
    }

    /**
     * whether has enrolled fingerprints
     *
     * @return has fingerprints
     */
    public boolean hasEnrolledFingerprints() {
        return FingerprintManagerCompat.from(mContext).hasEnrolledFingerprints();
    }
}
