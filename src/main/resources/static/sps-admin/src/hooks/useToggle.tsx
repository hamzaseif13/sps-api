import React from 'react'



const useToggle= (defaultValue?:boolean) => {
    const [active,setState] = React.useState<boolean>(defaultValue || false)
    const toggle = ()=>{
        setState((prevState:boolean)=>!prevState)
    }
    return [active,toggle] as const
}

export default useToggle