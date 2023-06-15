import { StyleSheet, Text, TouchableOpacity, View } from 'react-native'
import React from 'react'
import { Entypo } from '@expo/vector-icons';
import { useNavigation } from '@react-navigation/native';
const MapSearchBar = () => {
  const navigation = useNavigation<any>();
  return (
    <View style={styles.bar}>
      <Text>MapSearchBar</Text>
      <TouchableOpacity onPress={()=>navigation.navigate("ScanQR")}>
        <Entypo name="camera" size={30} color="#4169e1" />
      </TouchableOpacity>
    </View>
  )
}

export default MapSearchBar

const styles = StyleSheet.create({
    bar:{
        paddingHorizontal:10,
        paddingVertical:20,
        backgroundColor:"white",
        flexDirection:"row",
        justifyContent:"space-between",
        alignItems:"center"
    }
})