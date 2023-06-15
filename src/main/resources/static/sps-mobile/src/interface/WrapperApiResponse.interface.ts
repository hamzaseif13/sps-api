export interface WrapperApiResponse<T> {
    isSuccess: boolean,
    data?: T,
    error?: string,
    statusCode:number
}