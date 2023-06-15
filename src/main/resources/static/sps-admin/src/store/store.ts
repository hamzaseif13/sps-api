import { configureStore } from "@reduxjs/toolkit";
import login from "../features/login/loginSlice";
export const store = configureStore({
    reducer:{
         login,
    }
})

export type AppDispatch = typeof store.dispatch
export type RootState = ReturnType<typeof store.getState>