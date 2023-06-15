import React from "react";
import { Link } from "react-router-dom";
import { deleteOfficerById, Officer } from "../../features/officers/api";
import useAsync from "../../hooks/useAsync";
import Snackbar, { SnackbarOrigin } from "@mui/material/Snackbar";
import MuiAlert from "@mui/material/Alert";

interface Props {
  officer: Officer;
  refresh: any;
}

const OfficerCard: React.FC<Props> = ({ officer, refresh }) => {
  const { error, status, execute } = useAsync(deleteOfficerById, false);
  const deleteOfficer = () => {
    execute(officer.id);
  };
  if (status === "success") {
    setTimeout(() => {
      refresh();
    }, 2000);
  }
  return (
    <tr className="bg-white border-b dark:bg-gray-900 dark:border-gray-700">
      {
        <Snackbar
          anchorOrigin={{ vertical: "top", horizontal: "right" }}
          open={status === "success"}
          autoHideDuration={2000}>
          <MuiAlert severity="success" sx={{ width: "100%" }}>
            deleted Successfully
          </MuiAlert>
        </Snackbar>
      }
      <th
        scope="row"
        className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
        2
      </th>
      <td className="px-6 py-4">
        {officer.firstName + " " + officer.lastName}
      </td>
      <td className="px-6 py-4">{officer.email}</td>
      <td className="px-6 py-4">
        {officer.schedule?.startsAt.substring(0, 5)}
      </td>
      <td className="px-6 py-4">{officer.schedule?.endsAt.substring(0, 5)}</td>
      <td className="px-6 py-4">
        <Link to={`/officers/add`} className="submit-btn" state={{edit:true,officer}}>
          details
        </Link>
      </td>
      <td className="px-6 py-4">
        <button onClick={deleteOfficer} className="submit-btn bg-red-500">
          Delete
        </button>
      </td>
    </tr>
  );
};

export default OfficerCard;
