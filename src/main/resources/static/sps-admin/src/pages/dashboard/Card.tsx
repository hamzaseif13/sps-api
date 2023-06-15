import React from 'react';
import CircularProgress from '@mui/material/CircularProgress';
import { useQuery } from 'react-query';
interface Props {
    title: string;
    dataFetcher:  () => Promise<{
      isSuccess: boolean;
      data?: any;
      statusCode: number;
      message?: string;
      error?: string;
  }>;
    Icon:any
    queryKey:string
}
const Card = ({ title, dataFetcher,Icon,queryKey }:Props) => {
  const { data,isLoading,error} = useQuery(queryKey, dataFetcher)
  return (
    <div className="bg-white p-4 rounded-lg shadow">
      {isLoading ? <div className='flex justify-center items-center mt-10'>
      <CircularProgress />
      </div>
      :   <>
      <div className="p-2 rounded-full w-fit bg-[#EFF2F7]">
        <Icon fontSize="large" htmlColor="#3d51e0" />
      </div>
      <p className="text-2xl font-bold mt-4">{data?.data.length}</p>
      <h2 className=" text-[#3d51e0]">{title}</h2>
        </>}
   
  </div>
  );
};

export default Card;