import axios, { type InternalAxiosRequestConfig } from "axios";
import { tokenService } from "../services/tokenService";

const websocketApi = axios.create({
   baseURL: "http://localhost:8080",
   headers: {
      "Content-Type": "application/json"
   },
});

websocketApi.interceptors.request.use((config: InternalAxiosRequestConfig) => {
   const token = tokenService.getAccessToken();

   if (token) {
      config.headers.Authorization = `Bearer ${token}`;
   }

   return config;
});

export default websocketApi;
