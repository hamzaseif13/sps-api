import { StyleSheet, Text, View } from 'react-native'
import React from 'react'
import { ActivityIndicator, MD2Colors } from 'react-native-paper'

const LoadingScreen = () => {
  return (
    <View style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
    <ActivityIndicator animating={true} size={"large"} color={MD2Colors.blue600} />
</View>
  )
}

export default LoadingScreen

const styles = StyleSheet.create({})