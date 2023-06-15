import React from "react";
import GoogleMapReact from "google-map-react";
import useAsync from "../../hooks/useAsync";
import { ZoneInfo, getAllZones } from "../../features/zone/api";
import Modal from "@mui/material/Modal";
import Box from "@mui/material/Box";
import { Link } from "react-router-dom";
import { useQuery } from "react-query";
import LinearProgress from "@mui/material/LinearProgress/LinearProgress";
export default function Map({ zones }: { zones: ZoneInfo[] }) {
 
  const defaultProps = {
    center: {
      lat: 31.9539,
      lng: 35.9106,
    },
    zoom: 11,
  };

  return (
    // Important! Always set the container height explicitly
    <div className="col-span-4 w-full h-[30rem] shadow-lg rounded-lg">
      <GoogleMapReact
        bootstrapURLKeys={{ key: import.meta.env.VITE_MAPS_API_KEY }}
        defaultCenter={defaultProps.center}
        defaultZoom={defaultProps.zoom}>
        {zones.map((zone: ZoneInfo) => (
          <ZoneMarker
            lat={zone.lat}
            lng={zone.lng}
            zoneInfo={zone}
            text={zone.tag}
          />
        ))}
      </GoogleMapReact>
    </div>
  );
}
const ZoneMarker = ({
  zoneInfo,
  lat,
  lng,
  text,
}: {
  zoneInfo: ZoneInfo;
  lat: number;
  lng: number;
  text: string;
}) => {
  return (
    <>
      <Link to={`/zones/${zoneInfo.id}`}>
        <div
          className={`custom-marker w-[5rem] bg-white border-2 ${
            zoneInfo.availableSpaces > 0 ? "border-green-500" : "bg-red-500"
          } rounded cursor-pointer`}>
          <h1 className="text-center p-1 font-bold text-base">{text}</h1>
        </div>
      </Link>
    </>
  );
};
