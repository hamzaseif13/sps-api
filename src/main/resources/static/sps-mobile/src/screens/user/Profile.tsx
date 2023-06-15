import React, { useLayoutEffect } from "react";
import { View, Text,  StyleSheet, TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useAppContext } from '../../context/AppContext';
import { useNavigation } from '@react-navigation/native';
import * as SecureStore from "expo-secure-store";

const ProfilePage = () => {
  const {setUser} = useAppContext()
  const navigation = useNavigation<any>();
  useLayoutEffect(() => {
    navigation.setOptions({
     headerTitleAlign:"center",
     headerTitleStyle: {
      fontWeight: 'bold',
      fontSize: 24,
    }
    });
  }, [navigation]);
  const logout = async () => {
		await SecureStore.deleteItemAsync("user");
		setUser(null);
	};



  return (
    <View style={styles.container}>
      <View style={styles.section}>
        <TouchableOpacity onPress={()=>navigation.navigate("History")}style={styles.sectionHeader}>
          <Ionicons name="time" size={24} color="#000" />
          <Text style={styles.sectionTitle}>History</Text>
        </TouchableOpacity>
        {/* Add your history content here */}
      </View>
      <View style={styles.section}>
        <TouchableOpacity onPress={()=>navigation.navigate("Cars")} style={styles.sectionHeader}>
          <Ionicons name="car" size={24} color="#000" />
          <Text style={styles.sectionTitle}>Cars</Text>
        </TouchableOpacity>
        {/* Add your cars content here */}
      </View>
      <View style={styles.section}>
        <TouchableOpacity onPress={logout} style={styles.sectionHeader}>
          <Ionicons name="log-out" size={24} color="#000" />
          <Text style={styles.sectionTitle}>Logout</Text>
        </TouchableOpacity>
        {/* Add your logout content here */}
      </View>
     
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    backgroundColor: '#f0f0f0',
  },
  section: {
    marginBottom: 16,
    padding: 16,
    backgroundColor: '#fff',
    borderRadius: 8,
  },
  sectionHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 8,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginLeft: 8,
  },
});

export default ProfilePage;
