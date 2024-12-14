package com.learning.careerconnect.Utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.learning.careerconnect.Model.GetAllSavedLaterJobsOM
import java.lang.reflect.Type

class JobIdDeserializer : JsonDeserializer<GetAllSavedLaterJobsOM.Data.Data.JobId> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): GetAllSavedLaterJobsOM.Data.Data.JobId? {
        return if (json.isJsonObject) {
            // Deserialize as an object
            context.deserialize(json, GetAllSavedLaterJobsOM.Data.Data.JobId::class.java)
        } else {
            // Deserialize as a string (you can create a minimal JobId instance with only the `_id` field)
            GetAllSavedLaterJobsOM.Data.Data.JobId(_id = json.asString)
        }
    }
}
