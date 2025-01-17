package com.learning.careerconnect.Model

data class LoginOM(
    var `data`: Data?,
    var refreshToken: String?, // eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoicjNAZ21haWwuY29tIiwiaWF0IjoxNzM3MDQ5NDY3LCJleHAiOjE3NDQ4MjU0Njd9.6HFZjZxP3tUCNrLccsvkfbrBZnIEmMB0D9ZVs_OZP-0
    var status: String?, // success
    var message:String?,
    var token: String? // eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoicjNAZ21haWwuY29tIiwiaWF0IjoxNzM3MDQ5NDY3LCJleHAiOjE3Mzc5MTM0Njd9.l4xS8E3ri3cLL4Mnj7jvx9_NuDxVVFpvpRN8PhHopxs
) {
    data class Data(
        var `data`: Data?
    ) {
        data class Data(
            var __v: Int?, // 3
            var _id: String?, // 67894416e6d944d0dda8c26b
            var achievements: List<Achievement?>?,
            var careerPreference: CareerPreference?,
            var certificateEarned: List<CertificateEarned?>?,
            var currentLocation: CurrentLocation?,
            var dateOfBirth: Long?, // 1735575475000
            var education: List<Education?>?,
            var email: String?, // r3@gmail.com
            var experience: List<Experience?>?,
            var gender: String?, // Female
            var image: String?, // https://career-connect-bkt.s3.ap-south-1.amazonaws.com/shiv.jpg
            var language: List<String?>?,
            var links: Links?,
            var mobileNumber: Long?, // 9123456789
            var name: String?,
            var resumeLink:String?,
            var project: List<Project?>?,
            var skills: List<String?>?,
            var fcmToken:String?,
            var summary: String?, // A passionate data scientist with experience in machine learning and data analysis. Seeking opportunities to apply my skills in a challenging role that values innovation and creativity.
            var typeOfUser: String? // Recruiter
        ) {
            data class Achievement(
                var _id: String?, // 67894471e6d944d0dda8c2aa
                var description: String?, // Winner of AI Innovation Challenge 2023
                var link: String? // https://ai-innovation.com/winners-2023
            )

            data class CareerPreference(
                var _id: String?, // 67894471e6d944d0dda8c2a3
                var preferredJobType: List<String?>?,
                var preferredLocation: List<String?>?
            )

            data class CertificateEarned(
                var _id: String?, // 67894471e6d944d0dda8c2ad
                var date: Long?, // 1633027200000
                var score: Int?, // 9
                var type: String? // Certified Data Scientist
            )

            data class CurrentLocation(
                var city: String?, // Los Angeles
                var country: String?, // USA
                var state: String? // California
            )

            data class Education(
                var CGPA: String?, // 9.0
                var _id: String?, // 67894471e6d944d0dda8c2a4
                var collegeName: String?, // University of California, Los Angeles
                var course: String?, // Computer Science
                var graduationYear: String? // 2023
            )

            data class Experience(
                var _id: String?, // 67894471e6d944d0dda8c2a6
                var description: String?, // Developed scalable APIs for a SaaS product, resulting in a 20% increase in system efficiency. Led a small team to integrate cloud-based solutions into legacy systems.
                var endDate: Long?, // 1630992000000
                var nameOfCompany: String?, // Tech Innovations
                var role: String?, // Software Engineer
                var startDate: Long?, // 1609459200000
                var type: String? // Remote
            )

            data class Links(
                var _id: String?, // 67894471e6d944d0dda8c2ac
                var codeForces: String?, // Asd
                var github: String?, // fd
                var leetCode: String?, // Dd
                var website: String?, // DV
                var linkedin: String?, // DV
                var codeChef: String?, // DV
            )

            data class Project(
                var _id: String?, // 67894471e6d944d0dda8c2a8
                var description: String?, // A machine learning model to predict customer churn based on historical data. Achieved an accuracy of 92%.
                var gitHubLink: String?, // https://github.com/janesmith/ml-predictive-analytics
                var name: String?, // Machine Learning Model for Predictive Analytics
                var projectLink: String?, // https://janesmith.com/ml-project
                var skills: List<String?>?
            )
        }
    }
}