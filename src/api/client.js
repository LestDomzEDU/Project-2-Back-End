import axios from "axios";

// Expo reads env vars that start with EXPO_PUBLIC_ at build time
const baseURL =
  process.env.EXPO_PUBLIC_API_BASE_URL || "http://10.0.2.2:8080";

const api = axios.create({
  baseURL,
  timeout: 8000,
});

export default api;