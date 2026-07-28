/*
 * Copyright (C) 2026 FairPhone B.V.
 *
 * SPDX-FileCopyrightText: 2026 FairPhone B.V.
 *
 * SPDX-License-Identifier: EUPL-1.2
 */

package com.fairphone.settings.switchbutton

import android.app.INotificationManager
import android.app.NotificationChannelGroup
import android.app.Service
import android.content.Intent
import android.content.pm.ParceledListSlice
import android.os.IBinder
import android.os.RemoteException
import android.os.ServiceManager
import android.util.Log
import com.fairphone.settings.switchbutton.aidl.INotificationsProxy

class NotificationProxyService : Service() {

    companion object {
        const val TAG = "NotificationProxy"
    }

    private val sINM = INotificationManager.Stub.asInterface(
        ServiceManager.getService(NOTIFICATION_SERVICE)
    )

    private val binder = object : INotificationsProxy.Stub() {
        override fun setAppBypassDnd(packageName: String?, uid: Int, bypassDnd: Boolean) {
            try {
                val channelGroups: List<NotificationChannelGroup?>? =
                    getNotificationChannelGroupsForPackage(packageName, uid)

                channelGroups?.forEach { group ->
                    group?.channels?.forEach { channel ->
                        channel?.setBypassDnd(bypassDnd)
                        sINM.updateNotificationChannelForPackage(packageName, uid, channel)
                    }
                }
                Log.i(TAG, "Successfully updated bypassDnd for $packageName userId $uid")
            } catch (e: RemoteException) {
                Log.e(TAG, "Error setting bypassDnd for $packageName userId $uid", e)
            }
        }
    }

    private fun getNotificationChannelGroupsForPackage(
        packageName: String?,
        uid: Int
    ): List<NotificationChannelGroup?>? {
        try {
            val slice =
                sINM.getNotificationChannelGroupsForPackage(
                    packageName,
                    uid,
                    false
                ) as ParceledListSlice<*>
            return slice.list as? List<NotificationChannelGroup?>
        } catch (e: Exception) {
            Log.w(TAG, "Error calling NoMan", e)
            return emptyList()
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
}