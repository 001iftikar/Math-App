package com.example.mathapp.mappers

import com.example.mathapp.data.remote.model.SharedGoalDto
import com.example.mathapp.domain.model.SharedGoal
import com.example.mathapp.utils.SupabaseTimeCast.formattedTimestampZ

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