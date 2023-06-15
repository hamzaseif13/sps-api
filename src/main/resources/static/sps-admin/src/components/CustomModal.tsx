import { Modal } from '@mui/material'
import React from 'react'
import CloseIcon from '@mui/icons-material/Close';

interface Props{
    open:boolean
    onClose:()=>void
    children:React.ReactNode
    title:string
 
}
export const CustomModal:React.FC<Props> = ({open,onClose,children,title}) => {
  return (
    <Modal open={open} onClose={onClose}>
    <div className='absolute top-1/2 left-1/2 -translate-x-1/2 rounded shadow-lg -translate-y-1/2 w-full bg-white p-4 max-w-[1200px] '>
    <div className='flex gap-2 justify-between my-4 items-center'>
      <h2 className='text-4xl'>{title}</h2>
        <button onClick={onClose} className='text-4xl text-gray-500 hover:opacity-70 '>
          <CloseIcon fontSize='inherit'/>
        </button>
    </div>
     {
       children
      }
      <div className='flex gap-2 justify-end mt-4'>
        <button onClick={onClose}  className='btn bg-red-500 text-white '>Close</button>
      </div>
    </div>
  </Modal>
  )
}
