package com.iftikar.mathapp.utils

import com.iftikar.mathapp.BuildConfig

const val FAB_EXPLODE_BOUNDS_KEY = "FAB_EXPLODE_BOUNDS_KEY"
object SupabaseConstants {
    const val SUPABASE_URL = BuildConfig.SUPABASE_URL // store in local.properties file as the same name
    const val SUPABASE_KEY = BuildConfig.SUPABASE_KEY // store in local.properties file as the same name
    const val GOAL_TABLE = "goals"
    const val GROUP_TABLE = "groups"
    const val GROUP_MEMBER_TABLE = "group_members"
    const val SHARED_GOALS_TABLE = "shared_goals"
    const val PROFILES_TABLE = "profiles"
    const val MESSAGE_TABLE = "chats"
}

object ServiceConstants {
    const val ACTION_SERVICE_START = "ACTION_SERVICE_START"
    const val ACTION_SERVICE_STOP = "ACTION_SERVICE_STOP"
    const val ACTION_SERVICE_CANCEL = "ACTION_SERVICE_CANCEL"
    const val NOTIFICATION_CHANNEL_ID = "TIMER_NOTIFICATION_ID"
    const val NOTIFICATION_CHANNEL_NAME = "Timer notification"
    const val NOTIFICATION_ID = 1
    const val STUDY_SESSION_DEEP_LINK = "myapp://goToStudySessionScreen"
}