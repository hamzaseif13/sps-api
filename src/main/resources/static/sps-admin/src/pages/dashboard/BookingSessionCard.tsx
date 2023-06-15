import React from "react";
import CircularProgress from "@mui/material/CircularProgress";
import { useQuery } from "react-query";
import { getBookingCounter } from "../../features/zone/api";
import LocalParkingIcon from "@mui/icons-material/LocalParking";
const BookingSessionCard = () => {
  const { data, isLoading, error } = useQuery(
    "bookingCounter",
    getBookingCounter
  );
  if (error) {
    return <h1>Sometinh went wrong please try again</h1>;
  }
  return (
    <div className="bg-white p-4 rounded-lg shadow">
      {isLoading ? (
        <div className="flex justify-center items-center mt-10">
          <CircularProgress />
        </div>
      ) : (
        <>
          <div className="p-2 rounded-full w-fit bg-[#EFF2F7]">
            <LocalParkingIcon fontSize="large" htmlColor="#3d51e0" />
          </div>
          <p className="text-2xl font-bold mt-4">{data?.data}</p>
          <h2 className=" text-[#3d51e0]">Booking Session</h2>
        </>
      )}
    </div>
  );
};

export default BookingSessionCard;
