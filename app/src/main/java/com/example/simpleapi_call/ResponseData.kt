package com.example.simpleapi_call

data class ResponseData(
    val message: String,
    val user_id: Int,
    val name: String,
    val email: String,
    val profile_details: ProfileDetails,
    val data_list: List<DataListDetail>
)

// Json objects are represented as separate data classes in the model that GSON will use
data class ProfileDetails(
    val is_profile_completed: Boolean,
    val rating: Double
)

data class DataListDetail(
    val id: Int,
    val value:String
    )
