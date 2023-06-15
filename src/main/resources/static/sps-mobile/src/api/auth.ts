import { LoginResponse } from "../interface/LoginResponse.interface";
import { WrapperApiResponse } from "../interface/WrapperApiResponse.interface";
import { globalAPi } from "./api";

export const loginUser = async (
	email: string,
	password: string
): Promise<WrapperApiResponse<LoginResponse>> => {
	try {
		const resp = await globalAPi.post("auth/login_mobile", {
			email,
			password,
		});
		return { isSuccess: true, data: resp.data, statusCode: 200 };
	} catch (error: any) {
		const statusCode = error.response.status;
		const errorMessage = statusCode >= 500 ? "Something Went Wrong, Please Try Again Later" : "Invalid Credentials";
		return {
			isSuccess: false,
			statusCode: statusCode,
			error: errorMessage,
		};
	}
};
