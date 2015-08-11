package com.imagination.technologies.ora.app.events;

import android.os.Bundle;

public class SendData {
    private Bundle bundle;

    public SendData() {
    }

    public SendData(Bundle bundle) {
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
