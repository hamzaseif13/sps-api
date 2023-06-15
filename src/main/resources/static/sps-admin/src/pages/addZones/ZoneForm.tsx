import React, { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import AddIcon from "@mui/icons-material/Add";
import { CustomModal } from "../../components/CustomModal";
import SelectLocation from "./SelectLocation";
import useToggle from "../../hooks/useToggle";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import {
  ZoneLocation,
  ZoneRegisterRequest,
  ZoneUpdateRequest,
  createZone,
  updateZone,
} from "../../features/zone/api";
import { isBefore } from "../addOfficer";
import { Alert, Chip, Snackbar } from "@mui/material";
import { useLocation } from "react-router-dom";
import { QueryClient, useMutation, useQueryClient } from "react-query";

function ZoneForm() {
  const navigation = useNavigate();
  const [showModal, toggleShowModal] = useToggle(false);
  const [zoneLocation, setZoneLocation] = useState<ZoneLocation>();
  const [error, setError] = useState<string>();
  const location = useLocation();
  const queryClient = useQueryClient();
  const [editMode, setEditMode] = useState<boolean>(
    Boolean(location.state?.edit) || false
  );
  const {
    mutateAsync: createZoneAsync,
    isLoading: creating,
    error: creatingError,
  } = useMutation("createZone", (req: ZoneRegisterRequest) => createZone(req));
  const {
    mutateAsync: updateZoneAsync,
    isLoading: updating,
    error: updatingError,
  } = useMutation("updateZone", (req: ZoneUpdateRequest) =>
    updateZone(req, location.state.zoneInfo.id!)
  );
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    defaultValues: {
      ...location.state?.zoneInfo,
    },
  });
  const submit = async (data: any) => {
    if (!isBefore(data.startsAt, data.endsAt)) {
      setError("The start at time must be before the end at time");
    } else if (zoneLocation === undefined && !editMode) {
      setError("Please Select Location");
    } else {
      if (editMode) {
       
        const body ={
          startsAt: data.startsAt,
          endsAt: data.endsAt,
          title: data.title,
          numberOfSpaces: data.numberOfSpaces,
          fee: data.fee,
        }
        if (body.startsAt.length === 5) {
          body.startsAt += ":00";
        }
        if (body.endsAt.length === 5) {
          body.endsAt += ":00";
        }
        const resp = await updateZoneAsync(body);
        if (resp.isSuccess) {
          toast.success("Zone Updated Successfully", {
            hideProgressBar: true,
            position: "top-center",
          });
          navigation("/zones");
          queryClient.refetchQueries("zones");
        } else {
          toast.error(resp.error, {
            hideProgressBar: true,
            position: "top-center",
          });
        }
      } else {
        const body ={
          ...data,
          startsAt: data.startsAt + ":00",
          endsAt: data.endsAt + ":00",
          lat: zoneLocation?.latLng.lat!,
          lng: zoneLocation?.latLng.lng!,
          address: zoneLocation?.address,
          tag: data.tag.toUpperCase(),
        }
        if (body.startsAt.length === 5) {
          body.startsAt += ":00";
        }
        if (body.endsAt.length === 5) {
          body.endsAt += ":00";
        }
        const resp = await createZoneAsync( body);
        if (resp.isSuccess) {
          toast.success("Zone created Successfully", {
            hideProgressBar: true,
            position: "top-center",
          });
          navigation("/zones");
          queryClient.refetchQueries("zones");
        } else {
          toast.error(resp.error, {
            hideProgressBar: true,
            position: "top-center",
          });
        }
      }
    }
  };
  const closeModal = () => {
    toggleShowModal();
  };

  return (
    <div className="rounded-lg  shadow-2xl p-4 max-w-[800px] m-auto relative ">
      <h1 className="text-4xl font-bold text-center title mb-10">
        {editMode ? "Edit zone" : "Register New zone"}
      </h1>

      {editMode && (
        <Chip
          label="Edit Mode"
          color="success"
          variant="filled"
          className="float-right absolute top-6 right-5"
        />
      )}
      <form onSubmit={handleSubmit(submit)} action="">
        <div className="flex gap-2">
          <div className="w-1/2">
            <label htmlFor="zoneTitle" className="input-label">
              Zone Title
            </label>
            <input
              placeholder="City Mall South Gate"
              type="text"
              id="zoneTitle"
              className="input-feild "
              {...register("title", { required: true })}
            />
            {errors.zoneTitle && (
              <span className="error-span ">This Field Is Required</span>
            )}
          </div>
          <div className="w-1/2">
            <label htmlFor="zoneTitle" className="input-label">
              Zone Tag
            </label>
            <input
              placeholder="TC-101"
              type="text"
              id="zoneTag"
              className="input-feild "
              disabled={editMode}
              {...register("tag", { required: true })}
            />
            {errors.zoneTitle && (
              <span className="error-span ">This Field Is Required</span>
            )}
          </div>
        </div>
        <div className="flex my-2 gap-2">
          <div className="w-1/2">
            <label htmlFor="spacesCount" className="input-label ">
              Number Of Spaces{" "}
            </label>
            <div className=" flex items-center rounded">
              <input
                defaultValue={5}
                type="number"
                id="spacesCount"
                className="input-feild "
                {...register("numberOfSpaces", {
                  required: true,
                  min: 4,
                  max: 15,
                })}
              />
            </div>
            {errors.spacesCount && (
              <span className="error-span ">
                Enter a Valid Value Between 4 and 15
              </span>
            )}
          </div>
          <div className="w-1/2">
            <label htmlFor="spacesCount" className="input-label ">
              Fee{" "}
            </label>
            <div className=" flex items-center rounded">
              <input
                defaultValue={1}
                type="number"
                id="spacesCount"
                className="input-feild "
                {...register("fee", { required: true, min: 0.5, max: 3 })}
              />
            </div>
            {errors.fee && (
              <span className="error-span ">Fee cant be negative</span>
            )}
          </div>
        </div>
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
        {!editMode && (
          <div className="my-2">
            <label htmlFor="address" className="input-label">
              Zone Address
            </label>
            <button
              onClick={toggleShowModal}
              type="button"
              className="borde-gray-500 border flex gap-2 items-center p-2 rounded-lg hover:bg-white bg-gray-50">
              {zoneLocation?.address
                ? zoneLocation?.address
                : "Select Location"}
              <AddIcon className=" w-5" />
            </button>
            <CustomModal
              title="Select Zone Location"
              open={showModal}
              onClose={closeModal}>
              <SelectLocation
                setZoneLocation={setZoneLocation}
                zoneLocation={zoneLocation}
              />
            </CustomModal>
          </div>
        )}
        {error && <span className="error-span ">{error}</span>}

        <div className=" flex justify-end">
          <button className="submit-btn " type="submit">
            {updating || creating
              ? "Loading..."
              : editMode
              ? "Save"
              : "Create Zone"}
          </button>
        </div>
      </form>
    </div>
  );
}

export default ZoneForm;
