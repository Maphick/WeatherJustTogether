package ru.bear.weatherjusttogether.data.mapper

import ru.bear.weatherjusttogether.data.local.entities.TodayWeatherDto
import ru.bear.weatherjusttogether.data.remote.network.models.TodayWeatherApi
import ru.bear.weatherjusttogether.domain.models.TodayWeatherDomain

object TodayWeathertMapper {

    /** API -> Доменная модель */
    fun mapApiToDomain(weather: TodayWeatherApi): TodayWeatherDomain {
        return TodayWeatherDomain(
            city = weather.location.name,
            region = weather.location.region,
            country = weather.location.country,
            lat = weather.location.lat,
            lon = weather.location.lon,
            localtime = weather.location.localtime,
            temp_c = weather.current.temp_c,
            temp_f = weather.current.temp_f,
            is_day = weather.current.is_day,
            conditionText = weather.current.condition.text,
            conditionIcon = weather.current.condition.icon,
            conditionCode = weather.current.condition.code,
            wind_mph = weather.current.wind_mph,
            wind_kph = weather.current.wind_kph,
            wind_degree = weather.current.wind_degree,
            wind_dir = weather.current.wind_dir,
            pressure_mb = weather.current.pressure_mb,
            pressure_in = weather.current.pressure_in,
            precip_mm = weather.current.precip_mm,
            precip_in = weather.current.precip_in,
            humidity = weather.current.humidity,
            cloud = weather.current.cloud,
            feelslike_c = weather.current.feelslike_c,
            feelslike_f = weather.current.feelslike_f,
            vis_km = weather.current.vis_km,
            vis_miles = weather.current.vis_miles,
            uv = weather.current.uv,
            gust_mph = weather.current.gust_mph,
            gust_kph = weather.current.gust_kph,
            lastUpdated = System.currentTimeMillis().toString()
        )
    }

    /** Доменная модель -> Room */
    fun mapDomainToDto(domain: TodayWeatherDomain): TodayWeatherDto {
        return TodayWeatherDto(
            city = domain.city,
            region = domain.region,
            country = domain.country,
            lat = domain.lat,
            lon = domain.lon,
            localtime = domain.localtime,
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
            humidity = domain.humidity,
            cloud = domain.cloud,
            feelslike_c = domain.feelslike_c,
            feelslike_f = domain.feelslike_f,
            vis_km = domain.vis_km,
            vis_miles = domain.vis_miles,
            uv = domain.uv,
            gust_mph = domain.gust_mph,
            gust_kph = domain.gust_kph,
            last_updated = domain.lastUpdated.toString(),
        )
    }

    /** Room -> Доменная модель */
    fun mapDtoToDomain(dto: TodayWeatherDto): TodayWeatherDomain {
        return TodayWeatherDomain(
            city = dto.city,
            region = dto.region,
            country = dto.country,
            lat = dto.lat,
            lon = dto.lon,
            localtime = dto.localtime,
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
            humidity = dto.humidity,
            cloud = dto.cloud,
            feelslike_c = dto.feelslike_c,
            feelslike_f = dto.feelslike_f,
            vis_km = dto.vis_km,
            vis_miles = dto.vis_miles,
            uv = dto.uv,
            gust_mph = dto.gust_mph,
            gust_kph = dto.gust_kph,
            lastUpdated = dto.last_updated
        )
    }
}
