import { createAsyncThunk, createSlice, type PayloadAction } from "@reduxjs/toolkit";
import api from "../../api/axios";
import axios from "axios";
import { tokenService } from "../../services/tokenService";

export interface AuthState {
   id: number | null;
   role: string | null;
   accessToken: string | null;
   refreshToken: string | null;
   loading: boolean;
   error: string | null;
}

const initialState: AuthState = {
   id: null,
   role: null,
   accessToken: null,
   refreshToken: null,
   loading: false,
   error: null,
};

export const login = createAsyncThunk(
   "auth/login",
   async (credentials: { email: string; password: string }, { rejectWithValue }) => {
      try {
         const response = await api.post("/auth/login", credentials);
         return response.data;
      } catch (error) {
         if (axios.isAxiosError(error)) {
            const data = error.response?.data as { message?: string } | undefined;
            const message = data?.message ?? error.message ?? "Login failed";
            return rejectWithValue(message);
         }
         return rejectWithValue("Login failed");
      }
   }
);

const authSlice = createSlice({
   name: "auth",
   initialState,
   reducers: {
      logout: (state) => {
         state.id = null;
         state.role = null;
         state.accessToken = null;
         state.refreshToken = null;
         state.error = null;
         tokenService.clear();
      },
      setTokens: (state, action: PayloadAction<{ accessToken: string; refreshToken: string }>) => {
         state.accessToken = action.payload.accessToken;
         state.refreshToken = action.payload.refreshToken;
         tokenService.setTokens(action.payload);
      },
   },
   extraReducers: (builder) => {
      builder
         .addCase(login.pending, (state) => {
            state.loading = true;
            state.error = null;
         })
         .addCase(login.fulfilled, (state, action: PayloadAction<AuthState>) => {
            state.loading = false;
            state.id = action.payload.id;
            state.role = action.payload.role;
            state.accessToken = action.payload.accessToken;
            state.refreshToken = action.payload.refreshToken;
         })
         .addCase(login.rejected, (state, action) => {
            state.loading = false;
            state.error = action.payload as string;
         });
   },
});

export const { logout, setTokens } = authSlice.actions;
export default authSlice.reducer;
