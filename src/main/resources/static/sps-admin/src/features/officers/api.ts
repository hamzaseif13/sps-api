import axios from "axios";

const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL + "/api/v1/officer",
  headers: {
    Authorization: `Bearer ${localStorage.getItem("token")}`,
  },
});
export interface OfficerRegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phone: number;
  startsAt: string;
  endsAt: string;
  daysOfWeek: string[];
  zoneIds?: number[];
}
export interface Officer {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  zones?: any;
  schedule?: Schedule;
  phoneNumber: number;
}
export interface Schedule {
  daysOfWeek: string[];
  startsAt: string;
  endsAt: string;
}

export type OfficerUpdateRequest = Pick<
  OfficerRegisterRequest,
  "startsAt" | "endsAt" | "daysOfWeek" | "zoneIds"
> & { id: number };

export const getAllOfficers = () => {
  return axiosInstance.get("");
};
export const createOrUpdateOfficer = (
  req: OfficerRegisterRequest | OfficerUpdateRequest
) => {
  if ("id" in req) {
    const id = req.id;
    const temp:any =req
    delete temp.id
    return axiosInstance.put(`${id}`, temp);
  } else {
    return axiosInstance.post("", req);
  }
};
export const deleteOfficerById = (id: number) => {
  return axiosInstance.delete(`/${id}`);
};
export const getOfficerById = (id: number) => {
  return axiosInstance.get(`/${id}`);
};
export const updateOfficer = (req: OfficerUpdateRequest) => {
  return axiosInstance.put(`/${req.id}`, req);
};
