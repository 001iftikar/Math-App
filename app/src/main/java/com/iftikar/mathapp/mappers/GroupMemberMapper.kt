package com.iftikar.mathapp.mappers

import com.iftikar.mathapp.data.remote.model.GroupMemberDto
import com.iftikar.mathapp.domain.model.GroupMember
import com.iftikar.mathapp.domain.model.UserProfile
import com.iftikar.mathapp.utils.SupabaseTimeCast.formattedTimestampZ

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
