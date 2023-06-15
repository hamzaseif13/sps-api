import React from 'react'
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import { ZoneInfo } from '../../features/zone/api';
interface Props{
    setZones:React.Dispatch<React.SetStateAction<ZoneInfo[]>>
    zoneTag:string
}
export const ZoneChip:React.FC<Props> = ({setZones,zoneTag}) => {
    const deleteZone=()=>{
        setZones((prevZones)=>{
            return prevZones.filter((z)=>z.tag!==zoneTag)
        })
    }
  return (
    <div className='chip rounded-lg flex items-center gap-2'>
        <h2 className='font-bold text-lg'>{zoneTag}</h2>
        <button onClick={deleteZone} type="button"className='text-gray-500 hover:text-gray-800'>
            <HighlightOffIcon/>
        </button>
    </div>
  )
}
