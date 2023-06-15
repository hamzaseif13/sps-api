import { StyleSheet, Text, View } from 'react-native'
import React from 'react'

const useTag = (tag:string) => {
    return  [ tag.split("-")[0],tag.split("-")[1]] as const 
}

export default useTag

