import React from "react";
import { Link } from "react-router-dom";
import { deleteOfficerById, Officer } from "../../features/officers/api";
import useAsync from "../../hooks/useAsync";
import Snackbar, { SnackbarOrigin } from "@mui/material/Snackbar";
import MuiAlert from "@mui/material/Alert";
import { Admin, deleteAdminById } from "../../features/admin/api";

interface Props {
  admin: Admin;
  refresh: any;
}

const AdminRow: React.FC<Props> = ({ admin, refresh }) => {
  const { error, status, execute } = useAsync(deleteAdminById, false);
  const deleteAdmin = () => {
    execute(admin.id);
  };
  if (status === "success") {
    setTimeout(() => {
      refresh();
    }, 2000);
  }
  const isLoggedIn= admin.email == localStorage.getItem("email");
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
        {admin.id}
      </th>
      <td className="px-6 py-4">
        {admin.firstName + " " + admin.lastName}
      </td>
      <td className="px-6 py-4">{admin.email}</td>
    
   
      <td className="px-6 py-4">
        <button  onClick={deleteAdmin} disabled={isLoggedIn} className="submit-btn bg-red-500 disabled:bg-slate-600">
          Delete
        </button>
      </td>
    </tr>
  );
};

export default AdminRow;
