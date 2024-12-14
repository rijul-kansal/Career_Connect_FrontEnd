package com.learning.careerconnect.Model

data class GetAllSavedLaterJobsOM(
    var `data`: Data? = null,
    var length: Int? = null, // 3
    var status: String? = null // success
) {
    data class Data(
        var `data`: List<Data?>? = null
    ) {
        data class Data(
            var __v: Int? = null, // 0
            var _id: String? = null, // 6737279f5105531d156a48f2
            var jobId: JobId? = null,
            var userId: String? = null // 671fd32c424d84937f6f89b6
        ) {
            data class JobId(
                var __v: Int? = null, // 3
                var _id: String? = null, // 672122842f91c619ab063921
                var aboutCompany: String? = null, // Blockchain Innovations is at the forefront of developing blockchain solutions for businesses worldwide. Our focus is on secure, scalable, and transparent platforms to transform industries including finance, supply chain, and healthcare.
                var companyLinks: List<CompanyLink?>? = null,
                var costToCompany: String? = null, // 2200000
                var descriptionAboutRole: String? = null, // As a Blockchain Developer, you will be responsible for designing and implementing blockchain-based applications and ensuring the security and efficiency of these platforms.
                var durationOfInternship: String? = null, // 6 months
                var lastDateToApply: Long? = null, // 1761674284000
                var location: List<String?>? = null,
                var minimumQualification: List<String?>? = null,
                var nameOfCompany: String? = null, // BlockChain Innovations
                var nameOfRole: String? = null, // Blockchain Developer - Full Time
                var noOfOpening: Int? = null, // 2
                var noOfStudentsApplied: Int? = null, // 2
                var perks: List<String?>? = null,
                var postedDate: Long? = null, // 1730224772700
                var responsibilities: List<String?>? = null,
                var roleCategory: String? = null, // Blockchain
                var skillsRequired: List<String?>? = null,
                var startDate: String? = null, // Jan 2025
                var stopResponses: Boolean? = null, // false
                var typeOfJob: String? = null // FullTime
            ) {
                data class CompanyLink(
                    var _id: String? = null, // 672122842f91c619ab063922
                    var link: String? = null, // https://www.linkedin.com/company/blockchain-innovations
                    var name: String? = null // Company's link
                )
            }
        }
    }
}