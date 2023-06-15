import { OfficerSchedule } from "./../interface/Zone";
import { WrapperApiResponse } from "../interface/WrapperApiResponse.interface";
import { getJwtToken } from "../helpers";
import { globalAPi } from "./api";

export const getOfficerSchedule = async (): Promise<WrapperApiResponse<OfficerSchedule>> => {
	try {
		const token = await getJwtToken();
		if (!token) throw new Error("Token not found");
		const resp = await globalAPi.get("schedule/logged_in", {
			headers: { Authorization: `Bearer ${token}` },
		});
		return { isSuccess: true, data: resp.data, statusCode: resp.status };
	} catch (error: any) {
		const statusCode = error.response.status;
		const errorMessage =
			statusCode < 500 ? error.response.data.messages[0] : "Something went wrong";
		return {
			isSuccess: false,
			statusCode: statusCode,
			error: errorMessage,
		};
	}
};
export interface ReportRequest {
	plateNumber: string;
	carColor: string;
	carBrand: string;
	details: string;
	imageBase64: string;
	zoneId:number;
	imageType: string;
}
export const createReport = async (request: ReportRequest): Promise<WrapperApiResponse<any>> => {
	try {
		const token = await getJwtToken();
		if (!token) throw new Error("Token not found");
		const resp = await globalAPi.post("violation", request, {
			headers: { Authorization: `Bearer ${token}` },
		});
		return { isSuccess: true, data: resp.data, statusCode: resp.status };
	} catch (error: any) {
		const statusCode = error.response.status;
		const errorMessage =
			statusCode < 500 ? error.response.data.messages[0] : "Something went wrong";
		return {
			isSuccess: false,
			statusCode: statusCode,
			error: errorMessage,
		};
	}
};

export interface SpaceDetails {
	customerName: string;
	carBrand: string;
	carColor: string;
	spaceCreatedAt: string;
	sessionDuration: number;
	sessionExtended: boolean;
}
export const getSpaceDetails = async (spaceId: string): Promise<WrapperApiResponse<SpaceDetails>> => {
	try {
		const token = await getJwtToken();
		if (!token) throw new Error("Token not found");
		const resp = await globalAPi.get("space/" + spaceId, {
			headers: { Authorization: `Bearer ${token}` },
		});
		return { isSuccess: true, data: resp.data, statusCode: resp.status };
	} catch (error: any) {
		const statusCode = error.response.status;
		const errorMessage =
			statusCode < 500 ? error.response.data.messages[0] : "Something went wrong";
		return {
			isSuccess: false,
			statusCode: statusCode,
			error: errorMessage,
		};
	}
};
