package com.learning.careerconnect.Model

/**
{
    "status": "success",
    "data": {
        "currentLocation": {
            "city": "Los Angeles",
            "state": "California",
            "country": "USA"
        },
        "_id": "67894416e6d944d0dda8c26b",
        "typeOfUser": "Recruiter",
        "name": "Jane Smith",
        "mobileNumber": 9123456789,
        "email": "r3@gmail.com",
        "skills": [
            "Python",
            "Data Science",
            "Machine Learning",
            "SQL",
            "TensorFlow"
        ],
        "language": [
            "English",
            "German",
            "French"
        ],
        "VerifiedUser": true,
        "ChangePassword": 1737048971332,
        "education": [
            {
                "course": "Computer Science",
                "collegeName": "University of California, Los Angeles",
                "CGPA": "9.0",
                "graduationYear": "2023",
                "_id": "67894c762a3a1c9619d1ebf9"
            },
            {
                "course": "Data Science",
                "collegeName": "Stanford University",
                "CGPA": "8.8",
                "graduationYear": "2025",
                "_id": "67894c762a3a1c9619d1ebfa"
            }
        ],
        "experience": [
            {
                "nameOfCompany": "Tech Innovations",
                "role": "Software Engineer",
                "description": "Developed scalable APIs for a SaaS product, resulting in a 20% increase in system efficiency. Led a small team to integrate cloud-based solutions into legacy systems.",
                "startDate": 1609459200000,
                "endDate": 1630992000000,
                "type": "Remote",
                "_id": "67894c762a3a1c9619d1ebfb"
            },
            {
                "nameOfCompany": "Data Insights Corp.",
                "role": "Data Analyst",
                "description": "Worked with large datasets to identify key trends and provide actionable insights to the business. Automated reporting pipelines, reducing report generation time by 50%.",
                "startDate": 1633084800000,
                "endDate": 1664611200000,
                "type": "OnSite",
                "_id": "67894c762a3a1c9619d1ebfc"
            }
        ],
        "project": [
            {
                "name": "Machine Learning Model for Predictive Analytics",
                "gitHubLink": "https://github.com/janesmith/ml-predictive-analytics",
                "projectLink": "https://janesmith.com/ml-project",
                "skills": [
                    "Python",
                    "Scikit-learn",
                    "Machine Learning"
                ],
                "description": "A machine learning model to predict customer churn based on historical data. Achieved an accuracy of 92%.",
                "_id": "67894c762a3a1c9619d1ebfd"
            },
            {
                "name": "Personal Finance Tracker App",
                "gitHubLink": "https://github.com/janesmith/finance-tracker",
                "projectLink": "https://janesmith.com/finance-app",
                "skills": [
                    "React",
                    "Node.js",
                    "MongoDB"
                ],
                "description": "A web application that helps users track their expenses and savings. Includes features like budget planning and financial goal setting.",
                "_id": "67894c762a3a1c9619d1ebfe"
            }
        ],
        "achievements": [
            {
                "description": "Winner of AI Innovation Challenge 2023",
                "link": "https://ai-innovation.com/winners-2023",
                "_id": "67894c762a3a1c9619d1ebff"
            },
            {
                "description": "Published paper on Predictive Analytics in International Journal of Data Science",
                "link": "https://ijds.com/articles/2023/predictive-analytics-paper",
                "_id": "67894c762a3a1c9619d1ec00"
            }
        ],
        "lastUpdated": 1737049110583,
        "certificateEarned": [
            {
                "type": "Certified Data Scientist",
                "score": 9,
                "date": 1633027200000,
                "_id": "67894c762a3a1c9619d1ec02"
            },
            {
                "type": "Certified Python Developer",
                "score": 8,
                "date": 1625097600000,
                "_id": "67894c762a3a1c9619d1ec03"
            }
        ],
        "__v": 4,
        "careerPreference": {
            "preferredLocation": [
                "Los Angeles",
                "Seattle"
            ],
            "preferredJobType": [
                "FullTime",
                "Internship"
            ],
            "_id": "67894c762a3a1c9619d1ebf8"
        },
        "dateOfBirth": 1735575475000,
        "gender": "Female",
        "image": "https://career-connect-bkt.s3.ap-south-1.amazonaws.com/shiv.jpg",
        "links": {
            "codeChef": "avc",
            "codeForces": "Asd",
            "leetCode": "Dd",
            "website": "DV",
            "github": "fd",
            "linkedin": "Fd",
            "_id": "67894c762a3a1c9619d1ec01"
        },

        "summary": "A passionate data scientist with experience in machine learning and data analysis. Seeking opportunities to apply my skills in a challenging role that values innovation and creativity.",
        "fcmToken": "f9GGZ_1VTtS8LieGn61-ZP:APA91bGg3hwuU4Ve20NZMyMhGT8pNhYb_Yk8v7rW6G1VuzFUI8YPKl5FJG7OUMkoMDB4K2uYUChL9B4p_wM-0dp9MXqr6is9OjgxsUvQiQZpHjH_sfEulgw"
    }
}
*/
data class AttachmentOM(
    var `data`: Data?,
    var status: String? // success
) {
    data class Data(
        var ChangePassword: Long?, // 1737048971332
        var VerifiedUser: Boolean?, // true
        var __v: Int?, // 4
        var _id: String?, // 67894416e6d944d0dda8c26b
        var achievements: List<Achievement?>?,
        var careerPreference: CareerPreference?,
        var certificateEarned: List<CertificateEarned?>?,
        var currentLocation: CurrentLocation?,
        var dateOfBirth: Long?, // 1735575475000
        var education: List<Education?>?,
        var email: String?, // r3@gmail.com
        var experience: List<Experience?>?,
        var fcmToken: String?, // f9GGZ_1VTtS8LieGn61-ZP:APA91bGg3hwuU4Ve20NZMyMhGT8pNhYb_Yk8v7rW6G1VuzFUI8YPKl5FJG7OUMkoMDB4K2uYUChL9B4p_wM-0dp9MXqr6is9OjgxsUvQiQZpHjH_sfEulgw
        var gender: String?, // Female
        var image: String?, // https://career-connect-bkt.s3.ap-south-1.amazonaws.com/shiv.jpg
        var language: List<String?>?,
        var lastUpdated: Long?, // 1737049110583
        var links: Links?,
        var mobileNumber: Long?, // 9123456789
        var name: String?, // Jane Smith
        var project: List<Project?>?,
        var skills: List<String?>?,
        var resumeLink:String?,
        var summary: String?, // A passionate data scientist with experience in machine learning and data analysis. Seeking opportunities to apply my skills in a challenging role that values innovation and creativity.
        var typeOfUser: String? // Recruiter
    ) {
        data class Achievement(
            var _id: String?, // 67894c762a3a1c9619d1ebff
            var description: String?, // Winner of AI Innovation Challenge 2023
            var link: String? // https://ai-innovation.com/winners-2023
        )

        data class CareerPreference(
            var _id: String?, // 67894c762a3a1c9619d1ebf8
            var preferredJobType: List<String?>?,
            var preferredLocation: List<String?>?
        )

        data class CertificateEarned(
            var _id: String?, // 67894c762a3a1c9619d1ec02
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
            var _id: String?, // 67894c762a3a1c9619d1ebf9
            var collegeName: String?, // University of California, Los Angeles
            var course: String?, // Computer Science
            var graduationYear: String? // 2023
        )

        data class Experience(
            var _id: String?, // 67894c762a3a1c9619d1ebfb
            var description: String?, // Developed scalable APIs for a SaaS product, resulting in a 20% increase in system efficiency. Led a small team to integrate cloud-based solutions into legacy systems.
            var endDate: Long?, // 1630992000000
            var nameOfCompany: String?, // Tech Innovations
            var role: String?, // Software Engineer
            var startDate: Long?, // 1609459200000
            var type: String? // Remote
        )

        data class Links(
            var _id: String?, // 67894c762a3a1c9619d1ec01
            var codeChef: String?, // avc
            var codeForces: String?, // Asd
            var github: String?, // fd
            var leetCode: String?, // Dd
            var linkedin: String?, // Fd
            var website: String? // DV
        )

        data class Project(
            var _id: String?, // 67894c762a3a1c9619d1ebfd
            var description: String?, // A machine learning model to predict customer churn based on historical data. Achieved an accuracy of 92%.
            var gitHubLink: String?, // https://github.com/janesmith/ml-predictive-analytics
            var name: String?, // Machine Learning Model for Predictive Analytics
            var projectLink: String?, // https://janesmith.com/ml-project
            var skills: List<String?>?
        )
    }
}