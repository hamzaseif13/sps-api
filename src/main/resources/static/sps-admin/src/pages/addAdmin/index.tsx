import React from 'react'
import { useForm } from "react-hook-form";
import { createAdmin } from '../../features/admin/api';
import useAsync from '../../hooks/useAsync';
import Alert from "@mui/material/Alert";
const AddAdmin = () => {
    const { execute, status, error } = useAsync(createAdmin, false)
    const {
        register,
        handleSubmit,
        getValues,
        setValue,
        formState: { errors },
    } = useForm();

    const submit = (data: any) => {
        execute({email:data.email,firstName:data.firstName,lastName:data.lastName,password:data.password})
    }
    const generatePassword = () => {
        setValue("password", getValues("phone") + "-" + getValues("firstName"));
    };
    return (
        <section className="px-2 max-w-[800px] m-auto mt-10 pb-20">
            <h1 className="text-4xl font-bold text-center title">
                Register New Admin
            </h1>
            <form
                onSubmit={handleSubmit(submit)}
                action=""
                className="mt-8 rounded shadow-lg p-4">
                <h1 className="text-2xl font-medium">Admin Details</h1>
                <div className="mb-3 flex gap-2">
                    <div className='w-1/2'>
                        <label htmlFor="first-name" className="input-label">
                            First Name
                        </label>
                        <input
                            type="text"
                            id="first-name"
                            className="input-feild"
                            placeholder="Hamza"
                            required
                            {...register("firstName", { required: true, minLength: 3 })}
                        />
                        {errors.firstName && (
                            <span className="error-span ">Min Length is 3 characters</span>
                        )}
                    </div>
                    <div className='w-1/2'>
                        <label htmlFor="last-name" className="input-label">
                            Last Name
                        </label>
                        <input
                            type="text"
                            id="last-name"
                            className="input-feild"
                            placeholder="Mohammad"
                            required
                            {...register("lastName", { required: true, minLength: 3 })}
                        />
                        {errors.lastName && (
                            <span className="error-span ">Min Length is 3 characters</span>
                        )}
                    </div>
                </div>
                <div className="mb-3 flex gap-2">
                    <div className="w-1/2">
                        <label htmlFor="email" className="input-label">
                            Email
                        </label>
                        <input
                            type="email"
                            id="email"
                            className="input-feild"
                            placeholder="name@flowbite.com"
                            required
                            {...register("email", { required: true })}
                        />
                    </div>
                    <div className="w-1/2">
                        <label htmlFor="phone" className="input-label">
                            Phone Number
                        </label>
                        <input
                            type="text"
                            pattern="(07[7-9])[0-9]{7}$"
                            id="phone"
                            className="input-feild"
                            placeholder="077xxxxxxx"
                            required
                            {...register("phone", { required: true })}
                        />
                    </div>
                </div>
                <div className="mb-3">
                    <label htmlFor="password" className="input-label">
                        Password
                    </label>
                    <div className="flex gap-1">
                        <input
                            type="text"
                            id="password"
                            minLength={9}
                            className="input-feild"
                            placeholder="password"
                            required
                            {...register("password", { required: true })}
                        />
                        <button
                            type="button"
                            onClick={generatePassword}
                            className="btn bg-gray-700 text-white">
                            Generate
                        </button>
                    </div>
                    <span className="mt-2 block text-gray-600">
                        The default password will be the phone number, the Admin Can
                        change it when he logs in
                    </span>
                </div>



                {status === "error" && (
                    <Alert severity="error">
                        {error?.response?.status === 409
                            ? "Email already exists"
                            : error?.response?.status === 500
                                ? "Server Error, Please try again Later"
                                : "Invalid Input"}
                    </Alert>
                )}
                {status === "success" && (
                    <Alert severity="success">Created Successfully</Alert>
                )}
                <div className=" flex justify-end mt-2">
                    <button className="submit-btn " type="submit">
                        {status === "pending" ? "Loading ..." : "Save"}
                    </button>
                </div>
            </form>
        </section>
    )
}

export default AddAdmin