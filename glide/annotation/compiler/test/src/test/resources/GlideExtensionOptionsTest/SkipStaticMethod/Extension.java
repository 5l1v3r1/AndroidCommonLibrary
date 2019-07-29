package com.bumptech.glide.test;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.request.BaseRequestOptions;

import androidx.annotation.NonNull;

@GlideExtension
public final class Extension {

    private Extension() {
        // Utility class.
    }

    @NonNull
    @GlideOption(skipStaticMethod = true)
    public static BaseRequestOptions<?> test(BaseRequestOptions<?> requestOptions) {
        return requestOptions.centerCrop();
    }
}
