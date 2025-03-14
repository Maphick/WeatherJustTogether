package ru.bear.weatherjusttogether.data.mapper


import ru.bear.weatherjusttogether.data.local.entities.DailyWeathertDto
import ru.bear.weatherjusttogether.data.remote.network.models.DailyWeatherApi
import ru.bear.weatherjusttogether.domain.models.DailyWeatherDomain


object DailyWeathertMapper {

    /** 🔹 API -> Доменная модель */
    fun mapApiToDailyWeather(forecastDay: DailyWeatherApi): DailyWeatherDomain {
        return DailyWeatherDomain(
            date = forecastDay.date,
            date_epoch = forecastDay.date_epoch,
            maxtemp_c = forecastDay.day.maxtemp_c,
            mintemp_c = forecastDay.day.mintemp_c,
            avgtemp_c = forecastDay.day.avgtemp_c,
            maxwind_kph = forecastDay.day.maxwind_kph,
            totalprecip_mm = forecastDay.day.totalprecip_mm,
            avghumidity = forecastDay.day.avghumidity,
            conditionText = forecastDay.day.condition.text,
            conditionIcon = forecastDay.day.condition.icon,
            conditionCode = forecastDay.day.condition.code,
            sunrise = forecastDay.astro.sunrise,
            sunset = forecastDay.astro.sunset,
            moon_phase = forecastDay.astro.moon_phase,
            moon_illumination = forecastDay.astro.moon_illumination
        )
    }

    /** 🔹 Доменная модель -> Room (Сохранение в кэш) */
    fun mapDomainToDto(domain: DailyWeatherDomain): DailyWeathertDto {
        return DailyWeathertDto(
            date = domain.date,
            date_epoch = domain.date_epoch,
            maxtemp_c = domain.maxtemp_c,
            mintemp_c = domain.mintemp_c,
            avgtemp_c = domain.avgtemp_c,
            maxwind_kph = domain.maxwind_kph,
            totalprecip_mm = domain.totalprecip_mm,
            avghumidity = domain.avghumidity,
            condition_text = domain.conditionText,
            condition_icon = domain.conditionIcon,
            condition_code = domain.conditionCode,
            sunrise = domain.sunrise,
            sunset = domain.sunset,
            moon_phase = domain.moon_phase,
            moon_illumination = domain.moon_illumination
        )
    }

    /** 🔹 Room (Кэш) -> Доменная модель */
    fun mapDtoToDomain(dto: DailyWeathertDto): DailyWeatherDomain {
        return DailyWeatherDomain(
            date = dto.date,
            date_epoch = dto.date_epoch,
            maxtemp_c = dto.maxtemp_c,
            mintemp_c = dto.mintemp_c,
            avgtemp_c = dto.avgtemp_c,
            maxwind_kph = dto.maxwind_kph,
            totalprecip_mm = dto.totalprecip_mm,
            avghumidity = dto.avghumidity,
            conditionText = dto.condition_text,
            conditionIcon = dto.condition_icon,
            conditionCode = dto.condition_code,
            sunrise = dto.sunrise,
            sunset = dto.sunset,
            moon_phase = dto.moon_phase,
            moon_illumination = dto.moon_illumination
        )
    }
}
