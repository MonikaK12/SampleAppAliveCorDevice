package com.example.sampleappalivecordevice.omron.model.enumerate;


import androidx.annotation.StringRes;

import com.example.sampleappalivecordevice.R;

public enum ResultType {
    Success(R.string.success),
    Failure(R.string.failure);
    @StringRes
    int id;

    ResultType(@StringRes int id) {
        this.id = id;
    }

    @StringRes
    public int stringResId() {
        return this.id;
    }
}
