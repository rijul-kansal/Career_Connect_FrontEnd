package com.learning.careerconnect.Model

data class GetQuizQuestionDisplayOM(
    var `data`: Data?,
    var status: String? // success
) {
    data class Data(
        var `data`: List<Data?>?
    ) {
        data class Data(
            var _id: String?, // 66ec7c0cdd8df35ca35dbf38
            var correct_answer: String?, // Auto Scaling
            var difficulty: String?, // hard
            var options: List<String?>?,
            var question: String?, // Which AWS service is used to automatically scale applications based on demand?
            var type: String? // AWS
        )
    }
}