import { getJwtToken } from "../helpers";
import { WrapperApiResponse } from "../interface/WrapperApiResponse.interface";
import { Zone } from "../interface/Zone";
import { globalAPi } from "./api";

export const getZoneById = async (id:string): Promise<WrapperApiResponse<Zone>> => {
	try {
		const token = await getJwtToken()
		const resp = await globalAPi.get("/zone/"+id,{headers:{Authorization:`Bearer ${token}`}});
		return { isSuccess: true, data: resp.data, statusCode: resp.status };
	} catch (error: any) {
		const statusCode = error.response.status;
		return {
			isSuccess: false,
			statusCode: statusCode,
			error: "Something went wrong",
		};
	}
};