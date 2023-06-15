import React, { useEffect, useRef, useState } from "react";

import GoogleMapReact from "google-map-react";

import SearchIcon from "@mui/icons-material/Search";
import { ZoneLocation } from "../../features/zone/api";

interface Props {
  zoneLocation?: ZoneLocation;
  setZoneLocation: React.Dispatch<
    React.SetStateAction<ZoneLocation | undefined>
  >;
}

const SelectLocation: React.FC<Props> = ({ zoneLocation, setZoneLocation }) => {
  const mapRef = useRef<HTMLDivElement>(null);
  let geoCoder:google.maps.Geocoder ;

 
  const defaultProps = {
    center: {
      lat: 31.9539,
      lng: 35.9106,
    },
    zoom: 11,
  };
  const handleMapClick = (event: GoogleMapReact.ClickEventValue) => {
    geoCoder = new google.maps.Geocoder();
    geoCoder.geocode({ location: {lat:event.lat,lng:event.lng} }).then((response) => {
      if (response.results[0]) {
        setZoneLocation({
          latLng: {lat: event.lat, lng: event.lng},
          address: response.results[0].formatted_address,
        });
      }
    })
  };
  return (
    <div className="">
      <span className="block h-8">{zoneLocation?.address}</span>
      <div className="relative">
       
        <div className="w-full border h-[500px] rounded shadow-lg hover:cursor-pointer" ref={mapRef}>
          <GoogleMapReact
            onClick={handleMapClick}
            bootstrapURLKeys={{ key: import.meta.env.VITE_MAPS_API_KEY }}
            defaultCenter={defaultProps.center}
            defaultZoom={defaultProps.zoom} > 
            {zoneLocation && (
              <Marker lat={zoneLocation.latLng.lat} lng={zoneLocation.latLng.lng} />
            )}
          </GoogleMapReact>
        </div>
      </div>
    </div>
  );
};
const Marker = ({ lat, lng }: any) => (
  <div
    style={{
      position: "absolute",
      transform: "translate(-50%, -50%)",
      cursor: "pointer",
      width: "20px",
      height: "20px",
      borderRadius: "50%",
      background: "red",
    }}
  />
);
export default SelectLocation;
