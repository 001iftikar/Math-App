package com.example.mathapp.mappers

import com.example.mathapp.data.remote.model.GroupMemberDto
import com.example.mathapp.domain.model.GroupMember
import com.example.mathapp.domain.model.UserProfile
import com.example.mathapp.utils.SupabaseTimeCast.formattedTimestampZ

fun GroupMemberDto.toGroupMember(profile: UserProfile): GroupMember {
    return GroupMember(
        id = id,
        groupId = group_id,
        userId = user_id,
        role = role,
        addedAt = added_at.formattedTimestampZ(),
        profile = profile
    )
}
