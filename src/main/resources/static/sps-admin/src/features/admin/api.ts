import axios from 'axios'
import { Officer } from '../officers/api'
export interface CreateAdminRequest{
    firstName:string
    lastName:string
    email:string
    password:string
    phone:number
}
const axiosInstance = axios.create({
    baseURL: import.meta.env.VITE_API_URL + "/api/v1/admin",
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
});
export type Admin = Omit<Officer,"zones"|"schedule">

export const createAdmin=(req:CreateAdminRequest)=>{
    return axiosInstance.post("",req)
}

export const deleteAdminById=(id:number)=>{
    return axiosInstance.delete(`/${id}`)
}

export const getAllAdmins = ()=>{
    return axiosInstance.get("")
}
