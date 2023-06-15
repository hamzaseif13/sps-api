import { createSlice } from "@reduxjs/toolkit";
import { RootState } from "../../store/store";

const initialState: ILoginState = {
    token: localStorage.getItem("token") || undefined,
    email: (localStorage.getItem("email")) || undefined
};
interface ILoginState {
  token?: string;
  email?: string;
}

const loginSlice = createSlice({
  name: "login",
  initialState,
  reducers: {
    onLogin: (state, action) => {
      state.token = action.payload.token;
      state.email = action.payload.email;
      localStorage.setItem("token", action.payload.token);
      localStorage.setItem("email", action.payload.email);
    },
    onLogout:(state,action)=>{
        state.token = undefined;
        state.email = undefined;
        localStorage.removeItem("token");
        localStorage.removeItem("email");
    }
  },
});

export default loginSlice.reducer;
export const { onLogin, onLogout } = loginSlice.actions;
export const selectAdmin = (state: RootState) => state.login;