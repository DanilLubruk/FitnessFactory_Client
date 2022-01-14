package com.example.fitnessfactory_client.utils

import com.example.fitnessfactory_client.R

object StringUtils {

    fun getMessageUnsubscribeFromSession() =
        String.format(
            ResUtils.getString(R.string.message_session_subscription),
            ResUtils.getString(R.string.message_unsubscribe_from_session)
        )

    fun getMessageSubscribeToSession() =
        String.format(
            ResUtils.getString(R.string.message_session_subscription),
            ResUtils.getString(R.string.message_subscribe_to_session)
        )

    fun getMessageErrorClientsSameEmail() =
        String.format(
            ResUtils.getString(R.string.message_error_same_email),
            ResUtils.getString(R.string.caption_clients)
        )

    fun getMessageErrorCoachesSameEmail() =
        String.format(
            ResUtils.getString(R.string.message_error_same_email),
            ResUtils.getString(R.string.title_coaches_screen)
        )

    fun getMessageErrorUsersSameEmail() =
        String.format(
            ResUtils.getString(R.string.message_error_same_email),
            ResUtils.getString(R.string.caption_users)
        )
}