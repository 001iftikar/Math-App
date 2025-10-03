package com.iftikar.mathapp.mappers

import com.iftikar.mathapp.data.remote.model.GroupDto
import com.iftikar.mathapp.domain.model.Group
import com.iftikar.mathapp.utils.SupabaseTimeCast.formattedTimestampZ

fun GroupDto.toGroup(adminName: String): Group {
    return Group(
        id = id,
        admin = adminName,
        name = name,
        description = description,
        createdAt = created_at.formattedTimestampZ()
    )
}