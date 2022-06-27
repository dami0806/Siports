package com.bor2h.siportstest

// The result of crawling
data class WeatherData(
    var day : String,
    var date : String,
    var rainfallAm : String,
    var rainfallPm : String,
    var imageAm: Int = R.drawable.ic_map,
    var imagePm: Int = R.drawable.ic_map,
    var lowTemperature : String,
    var highTemperature : String
) {

    override fun toString(): String {
        return "Day : $day\n" + "Date : $date\n" + "RainfallAm : $rainfallAm\n" + "RainfallPm : $rainfallPm\n" + "Lowest : $lowTemperature\n" + "highest : $highTemperature\n"
    }
}
