package com.learning.careerconnect.Model

data class UpdateMeOM(
    var `data`: Data? = null,
    var status: String? = null // success
) {
    data class Data(
        var `data`: Data? = null
    ) {
        data class Data(
            var __v: Int? = null, // 1
            var _id: String? = null, // 66f5633ff2a89ba73327b4ed
            var achievements: List<Achievement?>? = null,
            var careerPreference: CareerPreference? = null,
            var certificateEarned: List<Any?>? = null,
            var codingProfileLink: List<String?>? = null,
            var currentLocation: CurrentLocation? = null,
            var dateOfBirth: Long? = null, // 915148800000
            var education: List<Education?>? = null,
            var email: String? = null, // bb@gmail.com
            var experience: List<Experience?>? = null,
            var fcmToken: String? = null, // dPEe7gSgTl2rB2Bv-spAAk:APA91bFWmRRt_SKQSNq1gw1n3O503wCAX8HvylMnHWBvI15_PzXd8nAxAJUjm0Dg2-POu00j8Rbyv8rKSiL3_XKM0wzlFZy9AiyP_J1-9TurcCMtr8HhlQpjOKn5st-C--4Ubu7KC4Kb
            var gender: String? = null, // Male
            var githubLink: String? = null, // https://github.com/johndoe
            var image: String? = null, // https://ghantee.com/wp-content/uploads/2023/12/lord-shiva-and-parvati-cute-cartoon-image.jpg
            var language: List<String?>? = null,
            var mobileNumber: Long? = null, // 9876543210
            var name: String? = null, // Bhole Baba
            var portfolioLink: String? = null, // https://johndoe.dev
            var project: List<Project?>? = null,
            var resumeLink: String? = null, // https://example.com/resume/johndoe.pdf
            var skills: List<String?>? = null,
            var summary: String? = null, // Highly motivated software developer with 2+ years of experience in full-stack development. Skilled in JavaScript, Node.js, React, and AI technologies.
            var typeOfUser: String? = null // User
        ) {
            data class Achievement(
                var _id: String? = null, // 66f6aa71dfe5936ce7707950
                var description: String? = null, // Won 1st place in the Hackathon organized by XYZ Company
                var link: String? = null // https://marketplace.canva.com/EAFGv9WhSmc/1/0/1600w/canva-blue-and-yellow-minimalist-employee-of-the-month-certificate-jaIc19nYjY4.jpg
            )

            data class CareerPreference(
                var _id: String? = null, // 66f6aa71dfe5936ce7707948
                var preferredJobType: List<String?>? = null,
                var preferredLocation: List<String?>? = null
            )

            data class CurrentLocation(
                var city: String? = null, // Mumbai
                var country: String? = null, // India
                var state: String? = null // Maharashtra
            )

            data class Education(
                var CGPA: String? = null, // 8.5
                var _id: String? = null, // 66f6aa71dfe5936ce7707949
                var collegeName: String? = null, // IIT Bombay
                var course: String? = null, // B.Tech in Computer Science
                var graduationYear: String? = null // 2022
            )

            data class Experience(
                var _id: String? = null, // 66f6aa71dfe5936ce770794b
                var description: String? = null, // Worked on backend services, developed APIs, and optimized database queries for high performance systems.Worked on backend services, developed APIs, and optimized database queries for high performance systems.
                var endDate: Long? = null, // 1672444800000
                var nameOfCompany: String? = null, // Tech Solutions Pvt Ltd
                var role: String? = null, // Software Engineer
                var startDate: Long? = null, // 1609459200000
                var type: String? = null // Remote
            )

            data class Project(
                var _id: String? = null, // 66f6aa71dfe5936ce770794d
                var description: String? = null, // Developed a personal portfolio website to showcase projects and skills using modern web technologies.
                var gitHubLink: String? = null, // https://github.com/johndoe/portfolio
                var name: String? = null, // Personal Portfolio Website
                var projectLink: String? = null, // https://johndoe.dev
                var skills: List<String?>? = null
            )
        }
    }
}