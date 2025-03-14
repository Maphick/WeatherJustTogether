package ru.bear.weatherjusttogether.data.mapper

import ru.bear.weatherjusttogether.data.local.entities.HourlyWeatherDto
import ru.bear.weatherjusttogether.data.remote.network.models.HourlyWeatherApi
import ru.bear.weatherjusttogether.domain.models.HourlyWeatherDomain

object HourlyWeathertMapper {

    /** 🔹 API -> Доменная модель */
    fun mapApiToDomain(hour: HourlyWeatherApi): HourlyWeatherDomain {
        return HourlyWeatherDomain(
            time_epoch = hour.time_epoch,
            time = hour.time,
            temp_c = hour.temp_c,
            temp_f = hour.temp_f,
            is_day = hour.is_day,
            conditionText = hour.condition.text,
            conditionIcon = hour.condition.icon,
            conditionCode = hour.condition.code,
            wind_mph = hour.wind_mph,
            wind_kph = hour.wind_kph,
            wind_degree = hour.wind_degree,
            wind_dir = hour.wind_dir,
            pressure_mb = hour.pressure_mb,
            pressure_in = hour.pressure_in,
            precip_mm = hour.precip_mm,
            precip_in = hour.precip_in,
            snow_cm = hour.snow_cm,
            humidity = hour.humidity,
            cloud = hour.cloud,
            feelslike_c = hour.feelslike_c,
            feelslike_f = hour.feelslike_f,
            windchill_c = hour.windchill_c,
            windchill_f = hour.windchill_f,
            heatindex_c = hour.heatindex_c,
            heatindex_f = hour.heatindex_f,
            dewpoint_c = hour.dewpoint_c,
            dewpoint_f = hour.dewpoint_f,
            will_it_rain = hour.will_it_rain,
            chance_of_rain = hour.chance_of_rain,
            will_it_snow = hour.will_it_snow,
            chance_of_snow = hour.chance_of_snow,
            vis_km = hour.vis_km,
            vis_miles = hour.vis_miles,
            gust_mph = hour.gust_mph,
            gust_kph = hour.gust_kph,
            uv = hour.uv
        )
    }

    /** 🔹 Доменная модель -> Room (Сохранение в кэш) */
    fun mapDomainToDto(domain: HourlyWeatherDomain): HourlyWeatherDto {
        return HourlyWeatherDto(
            time_epoch = domain.time_epoch,
            time = domain.time,
            temp_c = domain.temp_c,
            temp_f = domain.temp_f,
            is_day = domain.is_day,
            condition_text = domain.conditionText,
            condition_icon = domain.conditionIcon,
            condition_code = domain.conditionCode,
            wind_mph = domain.wind_mph,
            wind_kph = domain.wind_kph,
            wind_degree = domain.wind_degree,
            wind_dir = domain.wind_dir,
            pressure_mb = domain.pressure_mb,
            pressure_in = domain.pressure_in,
            precip_mm = domain.precip_mm,
            precip_in = domain.precip_in,
            snow_cm = domain.snow_cm,
            humidity = domain.humidity,
            cloud = domain.cloud,
            feelslike_c = domain.feelslike_c,
            feelslike_f = domain.feelslike_f,
            windchill_c = domain.windchill_c,
            windchill_f = domain.windchill_f,
            heatindex_c = domain.heatindex_c,
            heatindex_f = domain.heatindex_f,
            dewpoint_c = domain.dewpoint_c,
            dewpoint_f = domain.dewpoint_f,
            will_it_rain = domain.will_it_rain,
            chance_of_rain = domain.chance_of_rain,
            will_it_snow = domain.will_it_snow,
            chance_of_snow = domain.chance_of_snow,
            vis_km = domain.vis_km,
            vis_miles = domain.vis_miles,
            gust_mph = domain.gust_mph,
            gust_kph = domain.gust_kph,
            uv = domain.uv
        )
    }

    /** 🔹 Room (Кэш) -> Доменная модель */
    fun mapDtoToDomain(dto: HourlyWeatherDto): HourlyWeatherDomain {
        return HourlyWeatherDomain(
            time_epoch = dto.time_epoch,
            time = dto.time,
            temp_c = dto.temp_c,
            temp_f = dto.temp_f,
            is_day = dto.is_day,
            conditionText = dto.condition_text,
            conditionIcon = dto.condition_icon,
            conditionCode = dto.condition_code,
            wind_mph = dto.wind_mph,
            wind_kph = dto.wind_kph,
            wind_degree = dto.wind_degree,
            wind_dir = dto.wind_dir,
            pressure_mb = dto.pressure_mb,
            pressure_in = dto.pressure_in,
            precip_mm = dto.precip_mm,
            precip_in = dto.precip_in,
            snow_cm = dto.snow_cm,
            humidity = dto.humidity,
            cloud = dto.cloud,
            feelslike_c = dto.feelslike_c,
            feelslike_f = dto.feelslike_f,
            windchill_c = dto.windchill_c,
            windchill_f = dto.windchill_f,
            heatindex_c = dto.heatindex_c,
            heatindex_f = dto.heatindex_f,
            dewpoint_c = dto.dewpoint_c,
            dewpoint_f = dto.dewpoint_f,
            will_it_rain = dto.will_it_rain,
            chance_of_rain = dto.chance_of_rain,
            will_it_snow = dto.will_it_snow,
            chance_of_snow = dto.chance_of_snow,
            vis_km = dto.vis_km,
            vis_miles = dto.vis_miles,
            gust_mph = dto.gust_mph,
            gust_kph = dto.gust_kph,
            uv = dto.uv
        )
    }
}
