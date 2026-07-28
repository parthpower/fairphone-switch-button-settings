/*
 * Copyright (C) 2026 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2026 FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.settings.switchbutton.aidl;

interface INotificationsProxy {
    /**
     * Sets whether an application should bypass Do Not Disturb mode.
     * 
     * This method allows configuring whether notifications from a specific app
     * should be allowed to interrupt the user's Do Not Disturb mode.
     * This method is called by `com.fairphone.spring.launcher` to enable advanced app notification
     * filtering when Fairphone Moments is enabled.
     *
     * @param packageName The package name of the application to configure
     * @param uid The user ID of the application
     * @param bypassDnd If true, the app's notifications will bypass DND; if false, they will be suppressed during DND
     */
    void setAppBypassDnd(String packageName, int uid, boolean bypassDnd);
}
