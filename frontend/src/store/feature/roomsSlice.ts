import { createAsyncThunk, createSlice, type PayloadAction } from "@reduxjs/toolkit";
import axios from "axios";

export interface RoomsState {
   rooms: string[];
   loading: boolean;
   error: string | null;
}

const initialState: RoomsState = {
   rooms: [],
   loading: false,
   error: null,
};

export const fetchRooms = createAsyncThunk(
   "rooms/fetch",
   async (_arg: void, { rejectWithValue }) => {
      try {
         const response = await axios.get<{
            activeRooms: string[];
         }>("http://localhost:8080/ws/monitor/rooms");

         return response.data.activeRooms;
      } catch (error) {
         if (axios.isAxiosError(error)) {
            const data = error.response?.data as { message?: string } | undefined;
            const message = data?.message ?? error.message ?? "Failed to fetch rooms";
            return rejectWithValue(message);
         }

         return rejectWithValue("Failed to fetch rooms");
      }
   }
);

const roomsSlice = createSlice({
   name: "rooms",
   initialState,
   reducers: {
      clearRooms: (state) => {
         state.rooms = [];
         state.error = null;
      },
   },
   extraReducers: (builder) => {
      builder
         .addCase(fetchRooms.pending, (state) => {
            state.loading = true;
            state.error = null;
         })
         .addCase(fetchRooms.fulfilled, (state, action: PayloadAction<string[]>) => {
            state.loading = false;
            state.rooms = action.payload;
         })
         .addCase(fetchRooms.rejected, (state, action) => {
            state.loading = false;
            state.error = action.payload as string;
         });
   },
});

export const { clearRooms } = roomsSlice.actions;
export default roomsSlice.reducer;
