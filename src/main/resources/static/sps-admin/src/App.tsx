import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import Layout from "./components/Layout";
import AddZone from "./pages/addZones";
import AddOfficer from "./pages/addOfficer";
import AllOfficers from "./pages/allOfficers";
import AllZones from "./pages/allZones";
import LoginPage from "./pages/login";
import useAuth from "./hooks/useAuth";
import SecureRoute from "./components/SecureRoute";
import AddAdmin from "./pages/addAdmin";
import AllAdmins from "./pages/allAdmins";
import { QueryClient, QueryClientProvider } from "react-query";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Violations from "./pages/violations";
import QRGenerator from "./pages/QR";
import Dashboard from "./pages/dashboard";
import Zone from "./pages/zone";
import { Wrapper } from "@googlemaps/react-wrapper";
function App() {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        refetchOnWindowFocus: false,
        retry: false,
        refetchOnMount: false,
      },
    },
  });
  return (
    <BrowserRouter>
     
        <QueryClientProvider client={queryClient}>
          <Layout>
            <Routes>
              <Route element={<SecureRoute />}>
                <Route path="" element={<Dashboard />} />
                <Route path="zones">
                  <Route index element={<AllZones />} />
                  <Route path="add" element={<AddZone />} />
                  <Route path=":zoneId" element={<Zone />} />
                </Route>
                <Route path="admins">
                  <Route index element={<AllAdmins />} />
                  <Route path="add" element={<AddAdmin />} />
                </Route>
                <Route path="officers">
                  <Route index element={<AllOfficers />} />
                  <Route path="add" element={<AddOfficer />} />
                </Route>
                <Route path="violations" element={<Violations />} />
                <Route path="qr" element={<QRGenerator />} />
              </Route>
              <Route path="login" element={<LoginPage />} />
              <Route path="*" element={<h1>Wrong path</h1>} />
            </Routes>
          </Layout>
        </QueryClientProvider>
        <ToastContainer />
    </BrowserRouter>
  );
}

export default App;
