import { WEATHER_MAP } from "../../../constants/weathers";

export const transformUvIndex = (uv_index) => {
    const value = Number(uv_index);
    if (value <= 2) return `低 (${value})`;
    if (value <= 5) return `中 (${value})`;
    if (value <= 7) return `高 (${value})`;
    if (value <= 10) return `過量 (${value})`;
    return `危險 (${value})`;
}
export const transformAQI = (aqi) => {
    const value = Number(aqi);
    if (value <= 50) return `良好 (${value})`;
    if (value <= 100) return `普通 (${value})`;
    if (value <= 150) return `不適合敏感族群 (${value})`;
    if (value <= 200) return `不健康 (${value})`;
    if (value <= 300) return `非常不健康 (${value})`;
    return `危險 (${value})`;
}
export const getWeatherDescription = (weather_code) => {
    // console.log(weather_code);
    const code = Number(weather_code);
    if (WEATHER_MAP[code]) {
        return WEATHER_MAP[code].label;
    }
    return 'hi'
}
export const getWeatherIconUrl = (weather_code, is_day) => {
    const code = Number(weather_code);
    const wwo_code = WEATHER_MAP[code] ? WEATHER_MAP[code].icon : 113;
    const folder_name = is_day ? 'day' : 'night';
    return `assets/weather/${folder_name}/${wwo_code}.svg`;
}
export const transformTime = (time, timespan) => {
    if (timespan === '24 小時') return time.split('T')[1];
    return time;
}