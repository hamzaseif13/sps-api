import React from "react";
import { ZoneInfo } from "../../features/zone/api";
import useToggle from "../../hooks/useToggle";
import QrCode2Icon from "@mui/icons-material/QrCode2";
import { CustomModal } from "../../components/CustomModal";
import QRGenerator from "./QRGenerator";
import { Link } from "react-router-dom";
import { Tooltip } from "@mui/material";

interface Props {
  zoneInfo: ZoneInfo;
}
const ZoneCard: React.FC<Props> = ({ zoneInfo }) => {
  
const zoneNotEmpty =zoneInfo.availableSpaces!==zoneInfo.numberOfSpaces
  return (
    <div className="p-4 rounded-lg shadow-lg w-[350px] flex flex-col justify-between">
      <div className="flex gap-2 justify-between items-center">
        <h1 className="text-2xl font-medium">{zoneInfo.title.toUpperCase()}</h1>
        <span className="block font-bold border p-2 rounded-lg">
          {zoneInfo.tag}
        </span>
      </div>
      <p className="text-gray-500 my-2">{zoneInfo.address}</p>
      <h2 className="my-2">
        Available Spaces : {zoneInfo.availableSpaces}/{zoneInfo.numberOfSpaces}
      </h2>
      <h2 className="my-2">
        Available From : {zoneInfo.startsAt.substring(0, 5)} to{" "}
        {zoneInfo.endsAt.substring(0, 5)}
      </h2>
      <div className="flex gap-2">
        <Link
          to={`/zones/${zoneInfo.id}`}
          className="submit-btn block my-2 bg-gray-800">
         Details
        </Link>
        <Tooltip title={`${zoneNotEmpty ? "cant edit zone when its not empty": "edit zone"}`} >
        <Link to={`/zones/add`}  onClick= {zoneNotEmpty? e => e.preventDefault() : ()=>{}} className={`submit-btn block my-2 ${zoneNotEmpty && "bg-gray-400 cursor-not-allowed hover:bg-slate-400"}`} state={{edit:true,zoneInfo}}>
          Edit
        </Link>
        </Tooltip>
      </div>
    </div>
  );
};

export default ZoneCard;
