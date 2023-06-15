import { getJwtToken } from "../helpers";
import { CreateSessionRequest } from "../interface/Booking";
import { Car, CreateCarRequest } from "../interface/Car";
import { LoginResponse } from "../interface/LoginResponse.interface";
import { WrapperApiResponse } from "../interface/WrapperApiResponse.interface";
import { Zone } from "../interface/Zone";
import { globalAPi } from "./api";
export const registerCustomer = async (
	request: RegisterCustomerRequest
): Promise<WrapperApiResponse<LoginResponse>> => {
	try {
		
		const resp = await globalAPi.post("/customer", request);
		return { isSuccess: true, data: resp.data, statusCode: 200};
	} catch (error: any) {
		const statusCode = error.response.status;
		const errorMessage = error.response.data.messages[0];
		return {
			isSuccess: false,
			statusCode: statusCode,
			error: errorMessage,
		};
	}
};

export const getAllZones = async (): Promise<WrapperApiResponse<Array<Zone>>> => {
	try {
		const token = await getJwtToken();
		globalAPi.defaults.headers.common["Authorization"] = `Bearer ${token}`;
		const resp = await globalAPi.get("/zone");
		return { isSuccess: true, data: resp.data, statusCode:200};
	} catch (error: any) {
		const statusCode = error.response.status;
		return {
			isSuccess: false,
			statusCode: statusCode,
			error: "Something went wrong",
		};
	}
};

export const getCustomerCars = async (): Promise<WrapperApiResponse<Array<Car>>> => {
	try {
		const token = await getJwtToken();
		if(!token) throw new Error("Token not found")
		const resp = await globalAPi.get("/car",{headers:{Authorization:`Bearer ${token}`}});
		return { isSuccess: true, data: resp.data, statusCode:200 };
	} catch (error: any) {
		const statusCode = error.response.status;
		return {
			isSuccess: false,
			statusCode: statusCode,
			error: "Something went wrong",
		};
	}
}

export const registerNewCarr = async (request: CreateCarRequest): Promise<WrapperApiResponse<Array<Car>>> => {
	try {
		const token = await getJwtToken();
		if(!token) throw new Error("Token not found")
		const resp = await globalAPi.post("/car",request,{headers:{Authorization:`Bearer ${token}`}});
		return { isSuccess: true, data: resp.data, statusCode:200 };
	} catch (error: any) {
		const statusCode = error.response.status;
		const errorMessage  = statusCode <500 ? error.response.data.messages[0] : "Something went wrong"
		return {
			isSuccess: false,
			statusCode: statusCode,
			error: errorMessage,
		};
	}
}
export const createBookingSession = async(request:CreateSessionRequest) :Promise<WrapperApiResponse<number>>=>{
	try {
		const token = await getJwtToken();
		if(!token) throw new Error("Token not found")
		const resp = await globalAPi.post("/booking",request,{headers:{Authorization:`Bearer ${token}`}});
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
}

export const getLatestSession = async():Promise<WrapperApiResponse<any>>=>{
	try {
		const token = await getJwtToken();
		if(!token) throw new Error("Token not found")
		const resp = await globalAPi.get("/booking/current",{headers:{Authorization:`Bearer ${token}`}});
		return { isSuccess: true, data: resp.data, statusCode: 200};
	} catch (error: any) {
	
		return {
			isSuccess: false,
			statusCode: 500,
			error: "something went wrong",
		};
	}
}


export const getWallet = async():Promise<any>=>{
	try {
		const token = await getJwtToken();
		if(!token) throw new Error("Token not found")
		const resp = await globalAPi.get("/wallet",{headers:{Authorization:`Bearer ${token}`}});
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
}

export const chargeWallet = async(amount:number):Promise<WrapperApiResponse<any>>=>{
	try {
		const token = await getJwtToken();
		if(!token) throw new Error("Token not found")
		const resp = await globalAPi.patch("/wallet/charge",{amountToCharge:amount},{headers:{Authorization:`Bearer ${token}`}});
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
}

export const getBookingHistory = async() :Promise<WrapperApiResponse<any>>=>{
	try {
		const token = await getJwtToken();
		if(!token) throw new Error("Token not found")
		const resp = await globalAPi.get("/booking/history",{headers:{Authorization:`Bearer ${token}`}});
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
}

export const validateSpaceNumber = async({zoneId,spaceNumber}:{zoneId:number,spaceNumber:number}) :Promise<WrapperApiResponse<{isAvailable:boolean,message:string}>>=>{
	try {
		const token = await getJwtToken();
		if(!token) throw new Error("Token not found")
		const resp = await globalAPi.post("/space/check",{zoneId,spaceNumber},{headers:{Authorization:`Bearer ${token}`}});
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
}

export const extendBookingSession = async({zoneId,durationInMs,sessionId}:{zoneId:number,durationInMs:number,sessionId:number}) :Promise<WrapperApiResponse<any>>=>{
	try {
		const token = await getJwtToken();
		if(!token) throw new Error("Token not found")
		const resp = await globalAPi.patch(`/booking/${sessionId}/extend`,{zoneId,durationInMs},{headers:{Authorization:`Bearer ${token}`}});
		return { isSuccess: true, data: {}, statusCode: resp.status };
	} catch (error: any) {
		const statusCode = error.response.status;
		const errorMessage  = statusCode <500 ? error.response.data.messages[0] : "Something went wrong"
		return {
			isSuccess: false,
			statusCode: statusCode,
			error: errorMessage,
		};
	}
}