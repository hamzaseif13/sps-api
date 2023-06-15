import React, { useEffect } from "react";
import FmdGoodOutlinedIcon from "@mui/icons-material/FmdGoodOutlined";
import PersonIcon from "@mui/icons-material/Person";
import LocalParkingIcon from "@mui/icons-material/LocalParking";
import ReportProblemIcon from "@mui/icons-material/ReportProblem";
import Map from "./Map";
import { useQuery } from "react-query";
import LinearProgress from "@mui/material/LinearProgress/LinearProgress";
import {
  getAllCustomers,
  getAllZones,
  getViolations,
} from "../../features/zone/api";
import Card from "./Card";

import BookingSessionCard from "./BookingSessionCard";
import { CircularProgress } from "@mui/material";
const Dashboard = () => {
  const { data, isLoading, error } = useQuery("zones", getAllZones);

  if (error) {
    return (
      <div className="mt-10 text-center text-lg col-span-4">
        Something went wrong please try again later
      </div>
    );
  }
  return (
    <div className="p-10">
      <h1 className="text-center font-bold text-3xl">Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mt-10">
        <div className="bg-white p-4 rounded-lg shadow">
          {isLoading ? (
            <div className="flex justify-center items-center mt-10">
              <CircularProgress />
            </div>
          ) : (
            <>
              <div className="p-2 rounded-full w-fit bg-[#EFF2F7]">
                <FmdGoodOutlinedIcon fontSize="large" htmlColor="#3d51e0" />
              </div>
              <p className="text-2xl font-bold mt-4">{data?.data.length}</p>
              <h2 className=" text-[#3d51e0]">Zones</h2>
            </>
          )}
        </div>

        <Card title="Users" queryKey="users" dataFetcher={getAllCustomers} Icon={PersonIcon} />

        <BookingSessionCard />

        <Card
          title="Violations"
          dataFetcher={getViolations}
          Icon={ReportProblemIcon}
          queryKey="violations"
        />
        {!isLoading && 
          <Map zones={data?.data} />

        }
      </div>
    </div>
  );
};

export default Dashboard;
