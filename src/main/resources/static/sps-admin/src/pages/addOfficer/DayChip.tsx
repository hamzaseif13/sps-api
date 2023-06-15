import React from 'react'
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
interface Props{
    setDays:React.Dispatch<React.SetStateAction<string[]>>
    day:string
}
export const DayChip:React.FC<Props> = ({setDays,day}) => {
    const deleteZone=()=>{
        setDays((prevZones:string[])=>{
            return prevZones.filter((z)=>z!==day)
        })
    }
  return (
    <div className='chip rounded-lg flex items-center gap-2'>
        <h2 className='font-bold text-lg'>{day}</h2>
        <button onClick={deleteZone} type="button"className='text-gray-500 hover:text-gray-800'>
            <HighlightOffIcon/>
        </button>
    </div>
  )
}