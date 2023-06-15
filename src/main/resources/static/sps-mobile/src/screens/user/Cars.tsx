import { Button, ScrollView, StyleSheet, Text, TouchableOpacity, View,RefreshControl } from "react-native";
import React, { useLayoutEffect } from "react";
import { useAppContext } from "../../context/AppContext";
import CustomSafeAreaView from "../../components/CustomSafeAreaView";
import { useQuery } from "react-query";
import { getCustomerCars } from "../../api/customer";
import { Car } from "../../interface/Car";
import { useNavigation } from "@react-navigation/native";

const Cars = () => {
	const navigation = useNavigation();

	const { data, isLoading, error,isRefetching,refetch } = useQuery("cars", () => getCustomerCars());
	const cars = data?.data;
    useLayoutEffect(() => {
        navigation.setOptions({
            headerTitle: "Select Car"
        })
    },[])
	return (
		<CustomSafeAreaView>
			<ScrollView
				style={styles.container}
				refreshControl={<RefreshControl refreshing={isRefetching} onRefresh={refetch} />}
			>
				<Text style={{ fontSize: 30, fontWeight: "bold" }}>Your Cars</Text>
				{cars?.reverse().map((car) => (
					<View key={car.id} style={styles.car} >
						<Text style={{ fontWeight: "bold", fontSize: 20 }}>
							<Text>{car.brand} -</Text>
							<Text> {car.color}</Text>
						</Text>
						<View>
							<Text>Plate Number: {car.plateNumber}</Text>
						</View>
					</View>
				))}
			</ScrollView>
		</CustomSafeAreaView>
	);
};

export default Cars;

const styles = StyleSheet.create({
	container: {
		paddingHorizontal: 20,
		marginBottom:100
	},
	car: {
		padding: 10,
		backgroundColor: "#e0e0e0",
		borderRadius: 10,
		marginVertical: 10,
	},
});
