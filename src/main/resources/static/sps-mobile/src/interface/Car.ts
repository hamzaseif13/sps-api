export interface Car{
    id:string,
    plateNumber:string,
    color:string,
    brand:string,
}

export type CreateCarRequest = Omit<Car, 'id'>;