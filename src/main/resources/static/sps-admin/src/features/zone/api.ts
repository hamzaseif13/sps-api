import axios from "axios";
import { Officer } from "../officers/api";

export interface ZoneInfo {
  fee: number;
  id: number;
  title: string;
  tag: string;
  address: string;
  lng: number;
  lat: number;
  numberOfSpaces: number;
  availableSpaces: number;
  startsAt: string;
  endsAt: string;
  spaceList?:{number:number,id:number,state:"TAKEN"|"AVAILABLE"}[]
}
export type ZoneRegisterRequest = Omit<ZoneInfo, "id" | "availableSpaces">;
export type ZoneUpdateRequest = Omit<ZoneInfo, "availableSpaces" | "lng" | "lat"| "address"|"id" | "tag"> 
export interface ZoneLocation {
  latLng: { lat: number; lng: number };
  address: string;
}
const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL + "/api/v1/",
  headers: {
    Authorization: `Bearer ${localStorage.getItem("token")}`,
  },
});
export const getAllZones = () => {
  return axiosInstance.get("zone");
};
export const createZone = async (zoneRegisterRequest: ZoneRegisterRequest) => {
    try {
		const resp = await axiosInstance.post("zone",zoneRegisterRequest);
		return { isSuccess: true, data: resp.data, statusCode: resp.status };
	} catch (error: any) {
		const statusCode = error.response.status;
		const errorMessage  = statusCode <500 ? error.response.data.messages[0] : "Something went wrong"
		return {
			isSuccess: false,
			statusCode: statusCode,
			error: errorMessage,
		};
	}
};

export const updateZone = async(zoneUpdateRequest: ZoneUpdateRequest,id:number) => {
    console.log("🚀 ~ file: api.ts:47 ~ updateZone ~ zoneUpdateRequest:", zoneUpdateRequest)
    try {
		
		const resp = await axiosInstance.put("zone/"+id,zoneUpdateRequest);
		return { isSuccess: true, data: resp.data, statusCode: resp.status };
	} catch (error: any) {
		const statusCode = error.response.status;
		const errorMessage  = statusCode <500 ? error.response.data.messages[0] : "Something went wrong"
		return {
			isSuccess: false,
			statusCode: statusCode,
			error: errorMessage,
		};
	}
};

interface Violation{
	id:number
	zone:ZoneInfo
	officer:Officer
	plateNumber:string
	createdAt:Date
	imageUrl:string
	details:string
	carBrand:string
	carColor:string
}
export const getViolations = async () => {
    try {
		const resp = await axiosInstance.get("violation");
		return { isSuccess: true, data: resp.data as Violation[], statusCode: resp.status };
	} catch (error: any) {
		const statusCode = error.response.status;
		const errorMessage  = statusCode <500 ? error.response.data.messages[0] : "Something went wrong"
		return {
			isSuccess: false,
			statusCode: statusCode,
			error: errorMessage,
			data:[] as Violation[]
		};
	}
};

export const getZoneById = async(id:string)=>{
	try {
		const resp = await axiosInstance.get("zone/"+id);
		return { isSuccess: true, data: resp.data as ZoneInfo, statusCode: resp.status,message:"" };
	} catch (error: any) {
		const statusCode = error.response.status;
		const errorMessage  = statusCode <500 ? error.response.data.messages[0] : "Something went wrong"
		return {
			isSuccess: false,
			statusCode: statusCode,
			error: errorMessage,
			data:null
		};
	}
}

export const getAllCustomers = async(): Promise<{ isSuccess: boolean, data?: any , statusCode:number,message?:string,error?:string}>=>{
	try {
		const resp = await axiosInstance.get("customer");
		return { isSuccess: true, data: resp.data , statusCode: resp.status,message:"" };
	} catch (error: any) {
		const statusCode = error.response.status as number;
		const errorMessage  = statusCode <500 ? error.response.data.messages[0] : "Something went wrong"
		return {
			isSuccess: false,
			statusCode: statusCode,
			error: errorMessage,
			data:null,
		};
	}
}


export const getBookingCounter = async(): Promise<{ isSuccess: boolean, data?: any , statusCode:number,message?:string,error?:string}>=>{
	try {
		const resp = await axiosInstance.get("booking/counter");
		return { isSuccess: true, data: resp.data , statusCode: resp.status,message:"" };
	} catch (error: any) {
		const statusCode = error.response.status as number;
		const errorMessage  = statusCode <500 ? error.response.data.messages[0] : "Something went wrong"
		return {
			isSuccess: false,
			statusCode: statusCode,
			error: errorMessage,
			data:null,
		};
	}
}

