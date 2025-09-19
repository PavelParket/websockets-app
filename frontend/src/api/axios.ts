import axios, { AxiosError, type InternalAxiosRequestConfig } from "axios";
import { tokenService } from "../services/tokenService";

const api = axios.create({
   baseURL: "http://localhost:8081",
   headers: {
      "Content-Type": "application/json",
   },
});

api.interceptors.request.use((config: InternalAxiosRequestConfig) => {

   const token = tokenService.getAccessToken();

   if (token) {
      config.headers.Authorization = `Bearer ${token}`;
   }

   return config;
});

api.interceptors.response.use(
   (response) => response,
   async (error: AxiosError) => {
      const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean };

      if (error.response?.status === 401 && !originalRequest._retry) {
         originalRequest._retry = true;

         try {
            const refreshToken = tokenService.getRefreshToken();

            if (!refreshToken) {
               tokenService.clear();
               return Promise.reject(error);
            }

            const response = await api.post("/auth/refresh", { token: refreshToken });
            const data = response.data as { accessToken: string; refreshToken: string };

            tokenService.setTokens(data);

            originalRequest.headers.Authorization = `Bearer ${data.accessToken}`;
            return api(originalRequest);
         } catch (refreshError) {
            tokenService.clear();
            return Promise.reject(refreshError);
         }
      }

      return Promise.reject(error);
   }
);

export default api;
