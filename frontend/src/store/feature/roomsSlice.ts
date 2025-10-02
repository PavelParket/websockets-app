import { createAsyncThunk, createSlice, type PayloadAction } from "@reduxjs/toolkit";
import axios from "axios";

export interface RoomsPayload {
   chatRooms: string[];
   gameRooms: string[];
}

export interface RoomsState {
   rooms: RoomsPayload;
   loading: boolean;
   error: string | null;
}

const initialState: RoomsState = {
   rooms: { chatRooms: [], gameRooms: [] },
   loading: false,
   error: null,
};

export const fetchRooms = createAsyncThunk(
   "rooms/fetch",
   async (_arg: void, { rejectWithValue }) => {
      try {
         const response = await axios.get<RoomsPayload>("http://localhost:8080/ws/monitor/rooms");

         return response.data;
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
         state.rooms = { chatRooms: [], gameRooms: [] };
         state.error = null;
      },
   },
   extraReducers: (builder) => {
      builder
         .addCase(fetchRooms.pending, (state) => {
            state.loading = true;
            state.error = null;
         })
         .addCase(fetchRooms.fulfilled, (state, action: PayloadAction<RoomsPayload>) => {
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
