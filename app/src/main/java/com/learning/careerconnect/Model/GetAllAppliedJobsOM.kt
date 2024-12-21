package com.learning.careerconnect.Model

import java.io.Serializable

data class GetAllAppliedJobsOM(
    var `data`: Data,
    var length: Int ,
    var status: String,
    var message: String?
) : Serializable {
    data class Data(
        var `data`: List<Data>
    ) : Serializable{
        data class Data(
            var __v: Int? = null, // 0
            var _id: String? = null, // 67348301407a80d855962abe
            var jobAppliedId: JobAppliedId? = null,
            var postedDate: Long? = null, // 1731494657801
            var type: String? = null, // Applied
            var userId: String? = null // 66f9a6c5d84e498104e47290
        ) : Serializable{
            data class JobAppliedId(
                var __v: Int? = null, // 2
                var _id: String? = null, // 672122972f91c619ab06392c
                var aboutCompany: String? = null, // DecentralizeX is focused on creating decentralized applications (dApps) and blockchain-based products to promote open financial systems and transparent governance structures.
                var companyLinks: List<CompanyLink?>? = null,
                var costToCompany: String? = null, // Not Disclosed
                var descriptionAboutRole: String? = null, // As a Blockchain Research Intern, you will explore the latest blockchain trends, research blockchain protocols, and support development efforts with innovative solutions.
                var durationOfInternship: String? = null, // 6 months
                var lastDateToApply: Long? = null, // 1761674284000
                var location: List<String?>? = null,
                var minimumQualification: List<String?>? = null,
                var nameOfCompany: String? = null, // DecentralizeX
                var nameOfRole: String? = null, // Blockchain Research Intern
                var noOfOpening: Int? = null, // 1
                var noOfStudentsApplied: Int? = null, // 1
                var perks: List<String?>? = null,
                var postedDate: Long? = null, // 1730224791937
                var responsibilities: List<String?>? = null,
                var roleCategory: String? = null, // Blockchain
                var skillsRequired: List<String?>? = null,
                var startDate: String? = null, // Immediate
                var stopResponses: Boolean? = null, // false
                var typeOfJob: String? = null // Internship
            ) : Serializable{
                data class CompanyLink(
                    var _id: String? = null, // 672122972f91c619ab06392d
                    var link: String? = null, // https://www.linkedin.com/company/decentralizex
                    var name: String? = null // Company's link
                ): Serializable
            }
        }
    }
}