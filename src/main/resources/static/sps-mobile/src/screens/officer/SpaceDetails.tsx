import { StyleSheet, Text, View } from 'react-native'
import React from 'react'
import { useQuery } from 'react-query';
import { getSpaceDetails } from '../../api/officer';
import LoadingScreen from '../LoadingScreen';
import CustomSafeAreaView from '../../components/CustomSafeAreaView';
import { formatDuration } from '../../screensComponents/customer-home/RecentSession';

const SpaceDetails = ({route}:any) => {
    const spaceId = route.params;
    const {data,isLoading,error} = useQuery(["spaceDetails",spaceId],()=>getSpaceDetails(spaceId))
    if(isLoading)return <LoadingScreen/>
    if(!data?.isSuccess) return <Text>Something went wrong please try again later</Text>
    const info = data.data!
  return (
    <CustomSafeAreaView>
        <View style={styles.container}>
            <Text>Customer Name : </Text>
            <Text style={{fontSize:25,marginBottom:20}}>{info.customerName} </Text>
            <Text>Car Brand : </Text>
            <Text style={{fontSize:25,marginBottom:20}}>{info.carBrand} </Text>
            <Text>Car Color: </Text>
            <Text style={{fontSize:25,marginBottom:20}}>{info.carColor} </Text>
            <Text>Session duration : </Text>
            <Text style={{fontSize:25,marginBottom:20}}>{formatDuration(info.sessionDuration)} </Text>
            <Text>Extended session : </Text>
            <Text style={{fontSize:25,marginBottom:20}}>{info.sessionExtended ? "Yes" : "No"} </Text>
            <Text>Booking session started at : </Text>
            <Text style={{fontSize:25,marginBottom:20}}>{info.spaceCreatedAt} </Text>
        </View>
    </CustomSafeAreaView>
  )
}

export default SpaceDetails

const styles = StyleSheet.create({container:{
    paddingHorizontal:20,
}})