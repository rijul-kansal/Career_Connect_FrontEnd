package com.learning.careerconnect.Model

data class AllSavedJobsJobIdOnlyOM(
    var `data`: Data? = null,
    var length: Int? = null, // 2
    var status: String? = null // success
) {
    data class Data(
        var `data`: List<Data?>? = null
    ) {
        data class Data(
            var _id: String? = null, // 673398b3f47318e801639467
            var jobId: String? = null // 672122bf2f91c619ab0639e2
        )
    }
}