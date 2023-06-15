import { StyleSheet, Text, View ,Image} from 'react-native'
import React from 'react'

interface Props{
    firstName:string
    secondName:string
    profilePicture:string
}

const HomeHeader:React.FC<Props> = ({firstName,secondName,profilePicture}) => {
  return (
    <View style={styles.container}>
        <View>
            <Text style={styles.welcome}>Welcome,</Text>
            <Text style={styles.name}>{firstName} {secondName}</Text>
        </View>
        <Image style={styles.avatar} source={{uri:"https://xsgames.co/randomusers/avatar.php?g=male"}}/>
    </View>
  )
}

export default HomeHeader

const styles = StyleSheet.create({
    container:{
        flexDirection:"row",
        justifyContent:"space-between",
        alignItems:"center"
    },
    avatar:{
        width:70,
        height:70,
        borderRadius:50
    },
    welcome:{
        fontSize:30,
    },name:{
        fontSize:30,
        color:"#4169e1"
    }
})