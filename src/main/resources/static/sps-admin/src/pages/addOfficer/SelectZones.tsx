import React, { useState, useEffect } from "react";
import useAsync from "../../hooks/useAsync";
import { ZoneInfo, getAllZones } from "../../features/zone/api";
import { LinearProgress } from "@mui/material";
import ClearIcon from "@mui/icons-material/Clear";
interface Props {
  setZones: React.Dispatch<React.SetStateAction<ZoneInfo[]>>;
  zones: ZoneInfo[];
}
const SelectZones: React.FC<Props> = ({ setZones, zones }) => {
  const { value, status, error, execute } = useAsync(getAllZones, true);
  const [filteredZones, setFilteredZones] = useState<ZoneInfo[]>([]);
  const [fetchedZones, setFetchedZones] = useState<ZoneInfo[]>([]);

  useEffect(() => {
    if (status === "success") {
      setFetchedZones(value?.data);
      setFilteredZones(value?.data);
    }
  }, [status]);
  const toggle = (zoneInfo: ZoneInfo) => {
    if (zones.some((zone) => zone.id === zoneInfo.id)) {
      setZones((prevZones) => {
        return prevZones.filter((z) => z.id !== zoneInfo.id);
      });
    } else {
      setZones((prevZones) => {
        return [...prevZones, zoneInfo];
      });
    }
  };

  const isSelected = (id: number) => {
    return zones.some((zone) => zone.id === id);
  };

  const filterZones = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    if (value === "") {
      setFilteredZones(fetchedZones);
    } else {
      setFilteredZones((prevZones) => {
        return prevZones.filter(
          (zone) =>
            zone.address.toLowerCase().includes(value.toLowerCase()) ||
            zone.title.toLowerCase().includes(value.toLowerCase()) ||
            zone.tag.toLowerCase().includes(value.toLowerCase())
        );
      });
    }
  };
  return (
    <div>
      <div className="relative">
        <input
          type="text"
          className="input-feild p-4"
          placeholder="Search for Zone"
          onChange={filterZones}
        />
        <button
          className="absolute top-4 right-2 hover:opacity-80"
          onClick={() => setFilteredZones(fetchedZones)}>
          <ClearIcon color="action" />
        </button>
      </div>
      <div className="flex gap-2 flex-wrap my-2 ">
        {filteredZones.map((zone) => (
          <div
            className={`chip rounded-lg hover:cursor-pointer hover:bg-blue-400 ${
              isSelected(zone.id) ? "bg-blue-500 text-white" : ""
            }`}
            onClick={() => toggle(zone)}
            key={zone.id}>
            <h1 className="font-extrabold">{zone.tag}</h1>
            <p>{zone.title}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default SelectZones;
