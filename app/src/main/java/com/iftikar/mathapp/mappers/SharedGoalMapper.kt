package com.iftikar.mathapp.mappers

import com.iftikar.mathapp.data.remote.model.SharedGoalDto
import com.iftikar.mathapp.domain.model.SharedGoal
import com.iftikar.mathapp.utils.SupabaseTimeCast.formattedTimestampZ

fun SharedGoalDto.toSharedGoal(): SharedGoal {
    return SharedGoal(
        id = id,
        groupId = group_id,
        title = title,
        description = description,
        isCompleted = is_completed,
        createdAt = created_at.formattedTimestampZ(),
        endBy = end_by.formattedTimestampZ(),
    )
}