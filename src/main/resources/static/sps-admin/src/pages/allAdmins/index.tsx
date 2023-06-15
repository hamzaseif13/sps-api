import React from 'react'
import useAsync from '../../hooks/useAsync';
import { Alert, LinearProgress } from '@mui/material';
import { getAllOfficers } from '../../features/officers/api';
import { getAllAdmins } from '../../features/admin/api';
import AdminRow from './AdminRow';

const AllAdmins = () => {
  const { error, status, value, execute } = useAsync(getAllAdmins, true);

  return (
    <main className="px-2  mt-[2rem]">
      <h1 className=" font-bold title text-center mb-4">Admins</h1>
      <div className="relative overflow-x-auto shadow-md sm:rounded-lg">
        <table className="w-full text-sm  text-gray-500 dark:text-gray-400 text-center">
          {status === "success" && value?.data.length > 0 && (
            <thead className="text-base text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
              <tr>
                <th scope="col" className="px-6 py-3">
                  Id
                </th>
                <th scope="col" className="px-6 py-3">
                  Name
                </th>
                <th scope="col" className="px-6 py-3">
                  Email
                </th>
                <th scope="col" className="px-6 py-3">
                  Delete
                </th>
              </tr>
            </thead>
          )}
         
          <tbody>
            {status === "success" &&
              value?.data.length > 0 &&
              value?.data.map((admin: any) => (
                <AdminRow
                  admin={admin}
                  refresh={execute}
                  key={admin.id}
                />
              ))}
          </tbody>
        </table>
        </div>
        {status === "pending" && (
          
            <LinearProgress />
          )}
          {status === "error" && (
            <Alert severity="error">
              Something Went Wrong Please Try Again Later
            </Alert>
          )}
          {status === "success" && value?.data.length === 0 && (
            <Alert severity="info">
            No Officers Found
          </Alert>
          )}
  
    </main>
  );
}

export default AllAdmins