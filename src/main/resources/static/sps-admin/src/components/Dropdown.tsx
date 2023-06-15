import React, { ReactNode, useState } from 'react'

function Dropdown({children,title,icon}:{children:ReactNode,title:string,icon:JSX.Element}) {
    const [open,setOpen] = useState(false)
    const toggle= ()=>{
        setOpen(pre=>!pre)
    }
    return (
        <li>
            <button onClick={toggle} type="button" className="flex items-center w-full p-2 text-base font-normal transition duration-75 rounded-lg group text-white hover:bg-gray-700" aria-controls="dropdown-example" data-collapse-toggle="dropdown-example">
                <div className='w-6 text-gray-400'>
                    {icon}
                </div>
                <span className="flex-1 ml-3 text-left whitespace-nowrap" sidebar-toggle-item="true">{title}</span>
                <svg sidebar-toggle-item="true" className="w-6 h-6 " fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fillRule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clipRule="evenodd"></path></svg>
            </button>
            <ul id="dropdown-example" className={`${!open && 'hidden'} py-2 space-y-2`}>
              {children}
            </ul>
        </li>
    )
}

export default Dropdown