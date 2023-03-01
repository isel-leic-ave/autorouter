package pt.isel

import com.fasterxml.jackson.annotation.JsonProperty

data class Student(
    @JsonProperty("nr") val nr: Int,
    @JsonProperty("name") val name: String,
    @JsonProperty("group") val group: Int,
    @JsonProperty("semester") val semester: Int)
