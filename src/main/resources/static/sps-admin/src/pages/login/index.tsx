import React, { useEffect } from "react";
import { useForm } from "react-hook-form";
import { login } from "../../features/login/api";
import useAsync from "../../hooks/useAsync";
import Alert from "@mui/material/Alert";
import { AppDispatch } from "../../store/store";
import { useDispatch } from "react-redux";
import { onLogin } from "../../features/login/loginSlice";
import useAuth from "../../hooks/useAuth";
import { Navigate, useNavigate } from "react-router-dom";
const LoginPage = () => {
  const navigate = useNavigate();
  const [isAuth] = useAuth();
  const dispatch = useDispatch<AppDispatch>();
  const { error, execute, status, value } = useAsync(login, false);
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();
  const submit = (data: any) => {
    execute(data);
  };
  if (status === "success") {
    setTimeout(()=>{
      window.location.reload()
      dispatch(
        onLogin({ token: value?.data.jwtToken, email: value?.data.email })
      );
    },1000)
  }
  useEffect(() => {
    if (isAuth) {
      navigate("/");
    }
  }, []);
  return (
    <div className="rounded-lg  shadow-2xl p-4 max-w-[800px] m-auto mt-[10rem]">
      <h1 className=" font-bold title text-center mb-4">Login</h1>
      <form onSubmit={handleSubmit(submit)} action="">
        <div className="my-2">
          <label htmlFor="email" className="input-label">
            Email
          </label>
          <input
            placeholder="person@email.com"
            type="email"
            id="email"
            className="input-feild "
            {...register("email", { required: true })}
          />
          {errors.email && (
            <span className="error-span ">This Field Is Required</span>
          )}
        </div>

        <div className="my-2">
          <label htmlFor="password" className="input-label ">
            Password
          </label>
          <div className=" flex items-center rounded">
            <input
              type="password"
              id="password"
              className="input-feild "
              {...register("password", { required: true })}
            />
          </div>
          {errors.password && (
            <span className="error-span ">{error?.message}</span>
          )}
        </div>

        {status === "error" && <Alert severity="error">Bad Credentials</Alert>}
        {status === "success" && (
          <Alert severity="success">
            Login Successfully, you will be redirected shortly!
          </Alert>
        )}
        <div className=" flex justify-end mt-2">
          <button className="submit-btn " type="submit">
            {status === "pending" ? "Loading ..." : "Save"}
          </button>
        </div>
      </form>
    </div>
  );
};

export default LoginPage;
