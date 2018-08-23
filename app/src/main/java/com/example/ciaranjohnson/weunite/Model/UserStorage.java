package com.example.ciaranjohnson.weunite.Model;

import android.net.Uri;

public class UserStorage {

    private Uri uri;

    public UserStorage(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
