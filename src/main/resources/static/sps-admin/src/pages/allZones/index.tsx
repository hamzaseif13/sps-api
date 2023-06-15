import React from "react";
import useToggle from "../../hooks/useToggle";
import useAsync from "../../hooks/useAsync";
import { getAllZones } from "../../features/zone/api";
import LinearProgress from "@mui/material/LinearProgress";
import ZoneCard from "./ZoneCard";
import { Alert } from "@mui/material";
import { useQuery } from "react-query";
const AllZones = () => {
  const { data, isLoading, error } = useQuery("zones", getAllZones);
  const [qrModal, toggleQR] = useToggle(false);

  if (isLoading) return <LinearProgress />;
  if (error)
    return (
      <Alert severity="error">
        Something Went Wrong Please Try Again Later.
      </Alert>
    );
  return (
    <main className="px-2  mt-[2rem]">
      <h1 className=" font-bold title text-center mb-4 ">Zones</h1>
      <section className="flex gap-2 justify-center flex-wrap">
        {data?.data.length > 0? data?.data?.map((zone: any) => (
          <ZoneCard zoneInfo={zone} key={zone.id} />
        )) : <Alert severity="info">No Zones Found</Alert>}
      </section>
    </main>
  );
};

export default AllZones;
