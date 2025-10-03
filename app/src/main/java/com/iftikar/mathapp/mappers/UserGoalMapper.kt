package com.iftikar.mathapp.mappers

import com.iftikar.mathapp.data.remote.model.GoalResponseDto
import com.iftikar.mathapp.domain.model.GoalModel
import com.iftikar.mathapp.utils.SupabaseTimeCast.formattedTimestampZ

fun GoalResponseDto.toGoalModel(): GoalModel {
    return GoalModel(
        id = this.id,
        createdAt = this.createdAt.formattedTimestampZ(),
        title = this.title,
        description = this.description,
        endBy = this.endBy.formattedTimestampZ(),
        isCompleted = this.isCompleted
    )
}