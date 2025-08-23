package com.example.mathapp.mappers

import com.example.mathapp.data.remote.model.GoalResponseDto
import com.example.mathapp.domain.model.GoalModel

fun GoalResponseDto.toGoalModel(): GoalModel {
    return GoalModel(
        id = this.id,
        createdAt = this.createdAt,
        title = this.title,
        description = this.description,
        endBy = this.endBy,
        isCompleted = this.isCompleted
    )
}