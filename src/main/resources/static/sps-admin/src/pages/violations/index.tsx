import React from "react";
import useAsync from "../../hooks/useAsync";
import { Alert, LinearProgress } from "@mui/material";
import { useQuery } from "react-query";
import { getViolations } from "../../features/zone/api";
import { Link } from "react-router-dom";

const Violations = () => {
  const { data, isLoading, error,isRefetching } = useQuery("violations", () =>
    getViolations()
  ,{staleTime:2000});
  const violations = data?.data;
  if(isLoading || isRefetching){
    return <LinearProgress />
  }
  return (
    <main className="px-2  mt-[2rem]">
      <h1 className=" font-bold title text-center mb-4">Violations</h1>
      <div className="relative overflow-x-auto shadow-md sm:rounded-lg">
        {!!violations && violations.length > 0 ? (
          <table className="w-full text-sm  text-gray-500 dark:text-gray-400 text-center">
            <thead className="text-base text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
              <tr>
                <th scope="col" className="px-6 py-3">
                  Id
                </th>
                <th scope="col" className="px-6 py-3">
                  Plate number
                </th>
                <th scope="col" className="px-6 py-3">
                  Car brand
                </th>
                <th scope="col" className="px-6 py-3">
                  Car color
                </th>
                <th scope="col" className="px-6 py-3">
                  Details
                </th>
                <th scope="col" className="px-6 py-3">
                  Officer
                </th>{" "}
                <th scope="col" className="px-6 py-3">
                  Zone
                </th>
                <th scope="col" className="px-6 py-3">
                  Image
                </th>
                <th scope="col" className="px-6 py-3">
                  Created at
                </th>
              </tr>
            </thead>

            <tbody>
              {violations.map((violation) => (
                <tr key={violation.id} className="bg-white dark:bg-gray-700">
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">
                    {violation.id}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">
                    {violation.plateNumber}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">
                    {violation.carBrand}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">
                    {violation.carColor}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">
                    {violation.details}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">
                    {violation.officer.firstName} {violation.officer.lastName}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">
                    <Link to={`/zones/${violation.zone.id}`} className="text-blue-500 underline">
                      {violation.zone.tag}
                    </Link>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">
                    <a href={import.meta.env.VITE_IMG_URL + violation.imageUrl} target="_blank" className="text-blue-500 underline">
                        Image
                    </a>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">
                    {violation.createdAt.toString()}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <h1 className="text-center p-5">No violations</h1>
        )}
      </div>
    </main>
  );
};

export default Violations;
