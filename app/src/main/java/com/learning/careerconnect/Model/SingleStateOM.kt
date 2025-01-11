package com.learning.careerconnect.Model

/**
{
    "status": true,
    "data": {
        "name": "Australia",
        "iso3": "AUS",
        "iso2": "AU",
        "states": [
            {
                "name": "Australian Capital Territory",
                "state_code": "ACT"
            },
            {
                "name": "New South Wales",
                "state_code": "NSW"
            },
            {
                "name": "Northern Territory",
                "state_code": "NT"
            },
            {
                "name": "Queensland",
                "state_code": "QLD"
            },
            {
                "name": "South Australia",
                "state_code": "SA"
            },
            {
                "name": "Tasmania",
                "state_code": "TAS"
            },
            {
                "name": "Victoria",
                "state_code": "VIC"
            },
            {
                "name": "Western Australia",
                "state_code": "WA"
            }
        ]
    }
}
*/
data class SingleStateOM(
    var `data`: Data?,
    var status: Boolean? // true
) {
    data class Data(
        var iso2: String?, // AU
        var iso3: String?, // AUS
        var name: String?, // Australia
        var states: List<State?>?
    ) {
        data class State(
            var name: String?, // Australian Capital Territory
            var state_code: String? // ACT
        )
    }
}