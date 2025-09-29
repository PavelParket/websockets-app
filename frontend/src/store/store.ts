import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./feature/authSlice";
import roomsReducer from "./feature/roomsSlice";

export const store = configureStore({
   reducer: {
      auth: authReducer,
      rooms: roomsReducer,
   },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
