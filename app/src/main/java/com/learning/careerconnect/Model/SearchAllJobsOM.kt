package com.learning.careerconnect.Model

import java.io.Serializable

data class SearchAllJobsOM(
    var `data`: Data? = null,
    var length: Int? = null, // 2
    var status: String? = null, // success
    var totalJobs: Int? = null // 2
) : Serializable {
    data class Data(
        var `data`: List<Data?>? = null
    ) : Serializable{
        data class Data(
            var __v: Int? = null, // 0
            var _id: String? = null, // 66f9a6f9d84e498104e472a0
            var aboutCompany: String? = null, // AI Labs is a cutting-edge research organization focused on artificial intelligence and its applications in industries such as healthcare, finance, and robotics. Our mission is to push the boundaries of AI research and development.
            var companyLinks: List<CompanyLink?>? = null,
            var costToCompany: String? = null, // 50,000 GBP per annum (pro-rata)
            var descriptionAboutRole: String? = null, // You will work with a team of AI researchers and engineers to develop innovative AI models and algorithms. Your work will involve both theoretical research and practical implementations of AI technologies.
            var durationOfInternship: String? = null, // 6 weeks
            var lastDateToApply: Long? = null, // 1759173100000
            var location: List<String?>? = null,
            var minimumQualification: List<String?>? = null,
            var nameOfCompany: String? = null, // AI Labs
            var nameOfRole: String? = null, // Artificial Intelligence Researcher
            var noOfOpening: Int? = null, // 1
            var noOfStudentsApplied: Int? = null, // 0
            var perks: List<String?>? = null,
            var postedDate: Long? = null, // 1727637241971
            var responsibilities: List<String?>? = null,
            var roleCategory: String? = null, // Artificial Intelligence
            var skillsRequired: List<String?>? = null,
            var startDate: String? = null, // December 2024
            var stopResponses: Boolean? = null, // false
            var typeOfJob: String? = null // PartTime
        ) : Serializable{
            data class CompanyLink(
                var _id: String? = null, // 66f9a6f9d84e498104e472a1
                var link: String? = null, // https://www.ailabs.org
                var name: String? = null // Company Website
            ): Serializable
        }
    }
}