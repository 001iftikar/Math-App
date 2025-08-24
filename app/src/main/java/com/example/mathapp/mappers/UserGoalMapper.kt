package com.example.mathapp.mappers

import com.example.mathapp.data.remote.model.GoalResponseDto
import com.example.mathapp.domain.model.GoalModel
import com.example.mathapp.utils.SupabaseTimeCast.formattedTimestampZ

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