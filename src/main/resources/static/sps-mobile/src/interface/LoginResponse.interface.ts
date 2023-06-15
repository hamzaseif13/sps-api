export interface LoginResponse{
    email: string,
    jwtToken: string,
    role: "CUSTOMER" | "OFFICER",
    firstName: string,
    lastName: string,
}