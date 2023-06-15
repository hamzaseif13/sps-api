import React from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { onLogout, selectAdmin } from '../features/login/loginSlice'
import { AppDispatch } from '../store/store';

const useAuth = () => {
    const adminDetails = useSelector(selectAdmin);
    const dispatch = useDispatch<AppDispatch>();
    const logout = () => {
       dispatch(onLogout({}))
    }
    return [Boolean( adminDetails.token),logout] as const
}

export default useAuth