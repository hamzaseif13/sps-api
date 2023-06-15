import Add from "@mui/icons-material/Add";
import React, { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import { CustomModal } from "../../components/CustomModal";
import {
  createOrUpdateOfficer,
  Officer,
  OfficerRegisterRequest,
  OfficerUpdateRequest,
} from "../../features/officers/api";
import useAsync from "../../hooks/useAsync";
import useToggle from "../../hooks/useToggle";
import SelectDays from "./SelectDays";
import SelectZones from "./SelectZones";
import { ZoneChip } from "./ZoneChip";
import Alert from "@mui/material/Alert";
import { ZoneInfo } from "../../features/zone/api";
import { DayChip } from "./DayChip";
import { useLocation } from "react-router-dom";
import Chip from "@mui/material/Chip";
export interface Schedule {
  startsAt: string;
  endsAt: string;
  daysOfWeek: string[];
}
export const AddOfficer = () => {
  const location = useLocation();
  const [zonesModal, toggleZonesModal] = useToggle(false);
  const [daysModal, toggleDaysModal] = useToggle(false);

  const [zones, setZones] = useState<ZoneInfo[]>([]);
  const [daysOfWeek, setDays] = useState<string[]>([]);
  const [editMode, setEditMode] = useState(false);

  const [error, setError] = useState<string>();
  const {
    error: fetchError,
    status,
    execute,
  } = useAsync<OfficerRegisterRequest | OfficerUpdateRequest>(
    createOrUpdateOfficer,
    false
  );
  const {
    register,
    handleSubmit,
    getValues,
    setValue,
    formState: { errors },
  } = useForm();

  const generatePassword = () => {
    setValue(
      "password",
      getValues("phoneNumber") + "-" + getValues("firstName")
    );
  };
  const submit = (data: any) => {
    if (!isBefore(data.startsAt, data.endsAt)) {
      setError("The start at time must be before the end at time");
    } else if (daysOfWeek.length === 0) {
      setError("Please Select  Days");
    } else {
      const schedule = {
        daysOfWeek: daysOfWeek.map((d) => d.toUpperCase()),
        startsAt: data.startsAt,
        endsAt: data.endsAt,
        zoneIds: zones.map((z: any) => z.id),
      };

      if (editMode) {
        if (schedule.startsAt.length === 5) {
          schedule.startsAt += ":00";
        }
        if (schedule.endsAt.length === 5) {
          schedule.endsAt += ":00";
        }
        execute({ ...schedule, id: location.state.officer.id });
      } else {
        if (schedule.startsAt.length === 5) {
          schedule.startsAt += ":00";
        }
        if (schedule.endsAt.length === 5) {
          schedule.endsAt += ":00";
        }
        execute({ ...data, ...schedule });
      }
    }
  };
  useEffect(() => {
    const edit = location.state?.edit;
    setEditMode(Boolean(edit));
    if (edit) {
      setEditMode(true);
      const officer = location.state.officer as Officer;
      setValue("firstName", officer.firstName);
      setValue("lastName", officer.lastName);
      setValue("email", officer.email);
      setValue("phoneNumber", officer.phoneNumber);
      setValue("password", "");
      setValue("startsAt", officer.schedule?.startsAt);
      setValue("endsAt", officer.schedule?.endsAt);
      setDays(officer.schedule?.daysOfWeek || []);
      setZones(officer.zones || []);
    }
  }, []);
  return (
    <section className="px-2 max-w-[800px] m-auto mt-10 pb-20">
      <h1 className="text-4xl font-bold text-center title">
        {editMode ? "Edit Officer" : "Register New Officer"}
      </h1>
      <form
        onSubmit={handleSubmit(submit)}
        action=""
        className="mt-8 rounded shadow-lg p-4">
          <>
        <div className="flex justify-between items-center">
          <h1 className="text-2xl font-medium">Officers Details</h1>
          {editMode && (
            <Chip label="Edit Mode" color="success" variant="filled" />
          )}
        </div>
        <div className="mb-3 flex gap-2">
          <div className="w-1/2">
            <label htmlFor="first-name" className="input-label">
              First Name
            </label>
            <input
              type="text"
              id="first-name"
              className="input-feild"
              placeholder="Hamza"
              required
              readOnly={editMode}
              {...register("firstName", { required: true, minLength: 3 })}
            />
            {errors.firstName && (
              <span className="error-span ">Min Length is 3 characters</span>
            )}
          </div>
          <div className="w-1/2">
            <label htmlFor="last-name" className="input-label">
              Last Name
            </label>
            <input
              type="text"
              id="last-name"
              className="input-feild"
              placeholder="Mohammad"
              readOnly={editMode}
              required
              {...register("lastName", { required: true, minLength: 3 })}
            />
            {errors.lastName && (
              <span className="error-span ">Min Length is 3 characters</span>
            )}
          </div>
        </div>
        <div className="mb-3 flex gap-2">
          <div className="w-1/2">
            <label htmlFor="email" className="input-label">
              Email
            </label>
            <input
              type="email"
              id="email"
              className="input-feild"
              placeholder="name@flowbite.com"
              readOnly={editMode}
              required
              {...register("email", { required: true })}
            />
          </div>
          <div className="w-1/2">
            <label htmlFor="phone" className="input-label">
              Phone Number
            </label>
            <input
              type="text"
              pattern="(07[7-9])[0-9]{7}$"
              id="phone"
              className="input-feild"
              placeholder="077xxxxxxx"
              required
              readOnly={editMode}
              {...register("phoneNumber", { required: true })}
            />
          </div>
        </div>
        {!editMode && <div className="mb-3">
          <label htmlFor="password" className="input-label">
            Password
          </label>
          <div className="flex gap-1">
            <input
              type="text"
              id="password"
              minLength={9}
              className="input-feild"
              placeholder="password"
              disabled={editMode}
              {...register("password", { required: !editMode })}
            />
            <button
              type="button"
              onClick={generatePassword}
              disabled={editMode}
              className="btn bg-gray-700 text-white">
              Generate
            </button>
          </div>
          <span className="mt-2 block text-gray-600">
            The default password will be the phone number, the officer will
            change it when he logs in
          </span>
        </div>}
        </>
        <h1 className="text-2xl font-medium mb-2">Schedule</h1>
        <div className="mb-3 flex w-full gap-2">
          <div className="w-1/2">
            <label className="input-label">start at</label>
            <input
              type="time"
              className="input-feild"
              {...register("startsAt", { required: true })}
              defaultValue={"08:00"}
            />
          </div>
          <div className="w-1/2">
            <label className="input-label">ends at</label>
            <input
              type="time"
              className="input-feild"
              {...register("endsAt", { required: true })}
              defaultValue={"18:00"}
            />
          </div>
        </div>
        <div className="mb-3">
          <label className="input-label">Days of Week</label>
          <div className="flex gap-2 my-2 flex-wrap">
            <button
              type="button"
              onClick={toggleDaysModal}
              className="input-feild max-w-[7rem]">
              <Add />
            </button>
            {daysOfWeek.map((day) => (
              <DayChip key={day} day={day} setDays={setDays} />
            ))}
          </div>
          <CustomModal
            open={daysModal}
            onClose={toggleDaysModal}
            title="Select Zone">
            <SelectDays days={daysOfWeek} setDays={setDays} />
          </CustomModal>
          {errors.daysOfWeek && (
            <span className="error-span ">This Field Is Required</span>
          )}
        </div>
        <h1 className="text-2xl font-medium mb-2">Zones</h1>
        <div className="mb-3">
          <label className="input-label">Assigned Zones</label>
          <div className="flex gap-2 my-2 flex-wrap">
            <button
              type="button"
              onClick={toggleZonesModal}
              className="input-feild max-w-[7rem]">
              <Add />
            </button>
            {zones.map((zone) => (
              <ZoneChip key={zone.id} zoneTag={zone.tag} setZones={setZones} />
            ))}
          </div>
          <CustomModal
            open={zonesModal}
            onClose={toggleZonesModal}
            title="Select Zone">
            <SelectZones zones={zones} setZones={setZones} />
          </CustomModal>
          {error && <span className="error-span ">{error}</span>}
        </div>
        {status === "error" && (
          <Alert severity="error">
            {fetchError?.response?.status === 409
              ? "Email already exists"
              : fetchError?.response?.status === 500
              ? "Server Error, Please try again Later"
              : "Invalid Input, or Email already exists"}
          </Alert>
        )}
        {status === "success" && (
          <Alert severity="success">
            {editMode ? "Updated" : "Created"} Successfully
          </Alert>
        )}
        <div className=" flex justify-end mt-2">
          <button className="submit-btn " type="submit">
            {status === "pending" ? "Loading ..." : "Save"}
          </button>
        </div>
      </form>
    </section>
  );
};

export default AddOfficer;

export function isBefore(s: string, e: string) {
  const startMinutes =
    parseInt(s.split(":")[0]) * 60 + parseInt(s.split(":")[1]);
  const endsMinutes =
    parseInt(e.split(":")[0]) * 60 + parseInt(e.split(":")[1]);
  return startMinutes < endsMinutes;
}
