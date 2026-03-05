package com.example.obdapp.vindecoder

import android.os.Build
import androidx.annotation.RequiresApi


object VinYearResolver {

//    private val yearCodeMap = mapOf(
//        'A' to 1980, 'B' to 1981, 'C' to 1982, 'D' to 1983,
//        'E' to 1984, 'F' to 1985, 'G' to 1986, 'H' to 1987,
//        'J' to 1988, 'K' to 1989, 'L' to 1990, 'M' to 1991,
//        'N' to 1992, 'P' to 1993, 'R' to 1994, 'S' to 1995,
//        'T' to 1996, 'V' to 1997, 'W' to 1998, 'X' to 1999,
//        'Y' to 2000,
//        '1' to 2001, '2' to 2002, '3' to 2003, '4' to 2004,
//        '5' to 2005, '6' to 2006, '7' to 2007, '8' to 2008,
//        '9' to 2009
//    )
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun resolve(vin: String, currentYear: Int = java.time.Year.now().value): Int? {
//        if (vin.length != 17) return null
//        val yearCode = vin[9].uppercaseChar()
//        val baseYear = yearCodeMap[yearCode] ?: return null
//
//        var resolvedYear = baseYear
//        while (resolvedYear + 30 <= currentYear + 1) {
//            resolvedYear += 30
//        }
//
//        return if (resolvedYear < 1996) resolvedYear + 30 else resolvedYear
//    }

    private val yearMap = mapOf(
        'J' to 2018,
        'K' to 2019,
        'L' to 2020,
        'M' to 2021,
        'N' to 2022,
        'P' to 2023,
        'R' to 2024,
        'S' to 2025,
        'T' to 2026
    )

    fun resolve(vin: String): Int? {
        if (vin.length != 17) return null
        return yearMap[vin[9]]
    }
}
