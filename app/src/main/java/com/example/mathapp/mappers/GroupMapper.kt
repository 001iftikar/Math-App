package com.example.mathapp.mappers

import com.example.mathapp.data.remote.model.GroupDto
import com.example.mathapp.domain.model.Group
import com.example.mathapp.utils.SupabaseTimeCast.formattedTimestampZ

fun GroupDto.toGroup(): Group {
    return Group(
        id = id,
        admin = admin,
        name = name,
        description = description,
        createdAt = created_at.formattedTimestampZ()
    )
}