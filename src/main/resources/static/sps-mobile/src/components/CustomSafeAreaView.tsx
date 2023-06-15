import { Platform, SafeAreaView, StyleSheet, Text, View } from 'react-native'
import React, { ReactNode } from 'react'

interface Props {
    children:ReactNode
}

const CustomSafeAreaView:React.FC<Props> = ({children}) => {
  return (
    <SafeAreaView style={styles.droidSafeArea}>
      {
        children
      }
    </SafeAreaView>
  )
}

export default CustomSafeAreaView

const styles = StyleSheet.create({
    droidSafeArea: {
        flex: 1,
        paddingTop: Platform.OS === 'android' ? 30 : 0,
        backgroundColor:"white",
    }
})
