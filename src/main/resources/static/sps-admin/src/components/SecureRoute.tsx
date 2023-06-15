import { Navigate, Outlet } from 'react-router-dom';
import useAuth from '../hooks/useAuth';


export default function SecureRoute() {
    const [isAuth] = useAuth()
    return isAuth ? <Outlet /> : <Navigate to="/login" />;
}
