import React, { useState, useEffect, useCallback } from "react";
import {AxiosError,AxiosResponse} from'axios'


const useAsync = <RequestBody,>(asyncFunction:any, immediate = true,body?:RequestBody ) => {
    const [status, setStatus] = useState<"idle"|"pending"|"error"|"success">("idle");
    const [value, setValue] = useState<AxiosResponse>();
    const [error, setError] = useState<AxiosError>();
    // The execute function wraps asyncFunction and
    // handles setting state for pending, value, and error.
    // useCallback ensures the below useEffect is not called
    // on every render, but only if asyncFunction changes.
    const execute = useCallback((body?:RequestBody) => {
      setStatus("pending");
      setValue(undefined);
      setError(undefined);
      return asyncFunction(body)
        .then((response:any) => {
          setValue(response);
          setStatus("success");
        })
        .catch((error:any) => {
          setError(error);
          setStatus("error");
        });
    }, [asyncFunction]);
    // Call execute if we want to fire it right away.
    // Otherwise execute can be called later, such as
    // in an onClick handler.
    useEffect(() => {
      if (immediate) {
        execute(body);
      }
    }, [execute, immediate]);
    return { execute, status, value, error };
  };
  
  export default useAsync;