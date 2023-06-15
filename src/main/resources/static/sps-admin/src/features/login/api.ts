import axios from "axios"


export const login = (body:{email:string,password:string})=>{
    return axios.post(`${import.meta.env.VITE_API_URL}/api/v1/auth/login`,body)
}