const BASE_URL =
    window.location.hostname === "localhost" ||
    window.location.hostname === "127.0.0.1"
    ? "http://127.0.0.1:8000"
    : "https://traveling-helper.onrender.com";

export const request = async (endpoint, options = {}) => {
    const response = await fetch(`${BASE_URL}${endpoint}`, options);
    if (!response.ok) throw new Error('fetch failed!!');
    return response.json();
}