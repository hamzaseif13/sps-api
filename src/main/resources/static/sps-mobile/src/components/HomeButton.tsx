import { StyleSheet, Text, View } from 'react-native'
import React, { ReactNode } from 'react'
import { TouchableOpacity } from 'react-native'
import { useNavigation } from '@react-navigation/native'
interface Props{
    title:string,
    children:ReactNode
    target:string
}

const HomeButton:React.FC<Props>  = ({title,children,target}) => {
    const navigation = useNavigation<any>();
  return (
    <TouchableOpacity style={styles.button} onPress={()=>navigation.navigate(target)}>
        <Text style={{textAlign:"center",fontWeight:"bold"}}>{title}</Text>
        {children}
    </TouchableOpacity>
  )
}

export default HomeButton

const styles = StyleSheet.create({
    button:{
        backgroundColor:"#D8D8D8",
        width:"48%",
        padding:10,
        textAlign:"center",
        borderRadius:10,display:"flex",
        justifyContent:"space-between",
        alignItems:"center",
        paddingVertical:20
    }
})