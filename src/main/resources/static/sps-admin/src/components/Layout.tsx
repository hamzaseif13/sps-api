
import FmdGoodIcon from '@mui/icons-material/FmdGood';
import PersonIcon from '@mui/icons-material/Person';
import React, { ReactNode } from 'react'
import { Link, NavLink } from 'react-router-dom'
import useAuth from '../hooks/useAuth';
import Dropdown from './Dropdown'
import LogoutIcon from '@mui/icons-material/Logout';
import { useDispatch } from 'react-redux';
import { AppDispatch } from '../store/store';
import { onLogout } from '../features/login/loginSlice';
import AdminPanelSettingsIcon from '@mui/icons-material/AdminPanelSettings';
import ReportProblemIcon from '@mui/icons-material/ReportProblem';
function Sidebar({children}:{children:ReactNode}):any {
  const dispatch = useDispatch<AppDispatch>()

  const [isAuth] = useAuth()
  if(!isAuth) return children
  const logOut=()=>{
    dispatch(onLogout({}))
  }
  return (
    <>
      <aside  className="fixed  top-0 left-0 z-40 w-64 h-screen transition-transform  translate-x-0" >
        <div className="h-full px-3 py-4 overflow-y-auto  bg-gray-800">
          <ul className="space-y-2">
            <li>
              <NavLink to='' className={({isActive})=>`${isActive && 'bg-gray-700'} flex items-center p-2 text-base font-normal  rounded-lg  text-white  hover:bg-gray-700`}>
                <svg aria-hidden="true" className="w-6 h-6  transition duration-75 text-gray-400  group-hover:text-white" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path d="M2 10a8 8 0 018-8v8h8a8 8 0 11-16 0z"></path><path d="M12 2.252A8.014 8.014 0 0117.748 8H12V2.252z"></path></svg>
                <span className="flex-1 ml-3 whitespace-nowrap">Dashboard</span>
              </NavLink>
            </li>
            <li>
              <NavLink to='violations' className={({isActive})=>`${isActive && 'bg-gray-700'} flex items-center p-2 text-base font-normal  rounded-lg  text-white  hover:bg-gray-700`}>
              <ReportProblemIcon className='text-gray-400'/>
                <span className="flex-1 ml-3 whitespace-nowrap">Violations</span>
              </NavLink>
            </li>
            <Dropdown title='Admins' icon={<AdminPanelSettingsIcon />}>
               <li>
                <NavLink to='/admins' end  className={({isActive})=> `flex items-center w-full p-2 text-base font-normal transition duration-75 rounded-lg pl-11 group text-white hover:bg-gray-700 ${isActive && 'bg-gray-700'}`}>
                  All Admins
                </NavLink>
              </li>
              <li>
                <NavLink to='/admins/add' end className={({isActive})=> `flex items-center w-full p-2 text-base font-normal transition duration-75 rounded-lg pl-11 group text-white hover:bg-gray-700 ${isActive && 'bg-gray-700'}`}>
                  Add Admin
                </NavLink>
              </li>
            </Dropdown>
            <Dropdown title='Zones' icon={<FmdGoodIcon />}>
               <li>
                <NavLink to='/zones' end  className={({isActive})=> `flex items-center w-full p-2 text-base font-normal transition duration-75 rounded-lg pl-11 group text-white hover:bg-gray-700 ${isActive && 'bg-gray-700'}`}>
                  All zones
                </NavLink>
              </li>
              <li>
                <NavLink to='zones/add' end className={({isActive})=> `flex items-center w-full p-2 text-base font-normal transition duration-75 rounded-lg pl-11 group text-white hover:bg-gray-700 ${isActive && 'bg-gray-700'}`}>
                  Add Zone
                </NavLink>
              </li>
            </Dropdown>
            <Dropdown title='Officers' icon={<PersonIcon />}>
               <li>
                <NavLink to='/officers'end className={({isActive})=> `flex items-center w-full p-2 text-base font-normal transition duration-75 rounded-lg pl-11 group text-white hover:bg-gray-700 ${isActive && 'bg-gray-700'}`}>
                  All Officers
                </NavLink>
              </li>
              <li>
                <NavLink to='/officers/add' end className={({isActive})=> `flex items-center w-full p-2 text-base font-normal transition duration-75 rounded-lg pl-11 group text-white hover:bg-gray-700 ${isActive && 'bg-gray-700'}`}>
                  Add Officer
                </NavLink>
              </li>
            </Dropdown>
            <li>
              <button onClick={logOut} className='w-full flex  p-2 text-base font-normal  rounded-lg  text-white  hover:bg-gray-700
              '>
                <div className='text-gray-400'>
                <LogoutIcon color='inherit'/>
                </div>
                <span className="flex-1 ml-3 whitespace-nowrap text-left">Logout</span>
              </button>
            </li>
          </ul>
        </div>
      </aside>
      <main className='ml-64'>
        {children}
      </main>
    </>
  )
}

export default Sidebar