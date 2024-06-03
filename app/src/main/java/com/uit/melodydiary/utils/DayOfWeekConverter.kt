package com.uit.melodydiary.utils

object DayOfWeekConverter {

    fun convertToThu(dayOfWeek: String) : String {
        return when (dayOfWeek.toUpperCase()) {
            "MONDAY" -> "Thứ 2"
            "TUESDAY" -> "Thứ 3"
            "WEDNESDAY" -> "Thứ 4"
            "THURSDAY" -> "Thứ 5"
            "FRIDAY" -> "Thứ 6"
            "SATURDAY" -> "Thứ 7"
            "SUNDAY" -> "Chủ nhật"
            else -> "Không xác định"
        }
    }
}
