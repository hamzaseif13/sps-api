import React from "react";
import { useQuery } from "react-query";
import { Link, useParams } from "react-router-dom";
import { getZoneById } from "../../features/zone/api";
import { LinearProgress } from "@mui/material";
import QrCode2Icon from "@mui/icons-material/QrCode2";
import Space from "./Space";

const Zone = () => {
  const { zoneId } = useParams();
  const { data, error, isLoading } = useQuery(["zone", zoneId], () =>
    getZoneById(zoneId!)
  );

  if (isLoading) return <LinearProgress />;
  if (!data?.isSuccess) {
    if (data?.statusCode === 404) return <div>Zone Not found</div>;
    return <div>{data?.message}</div>;
  }
  const zoneInfo = data.data!;
  return (
    <div className=" py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="bg-white overflow-hidden shadow-sm sm:rounded-lg">
          <div className="p-6">
            <div className="flex justify-between items-start">
              <h1 className="text-2xl font-bold mb-4">{zoneInfo.title} </h1>
              <Link
                to={`/qr?zoneId=${zoneInfo.id}&spaces=${zoneInfo.numberOfSpaces}`}
                className="submit-btn block my-2 bg-gray-800">
                <QrCode2Icon />
              </Link>
            </div>
            <p className="text-gray-600 mb-2">{zoneInfo.address}</p>
            <div className="flex mb-4">
              <div className="w-1/2">
                <p className="text-gray-600">
                  Number of Spaces: {zoneInfo.numberOfSpaces}
                </p>
              </div>
              <div className="w-1/2">
                <p className="text-gray-600">
                  Available Spaces: {zoneInfo.availableSpaces}
                </p>
              </div>
            </div>
            <p className="text-lg font-bold text-green-600">
              Fee: ${zoneInfo.fee}
            </p>
            <div className="flex gap-5 mt-5">
              {zoneInfo.spaceList
                ?.sort((a, b) => a.number - b.number)
                .map((space) => (
                  <Space key={space.id} space={space}/>
                ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Zone;
