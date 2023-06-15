import { Button, ScrollView, StyleSheet, Text, TouchableOpacity, View, } from "react-native";
import React, { useLayoutEffect } from "react";
import { useAppContext } from "../../context/AppContext";
import CustomSafeAreaView from "../../components/CustomSafeAreaView";
import { useQuery } from "react-query";
import { getCustomerCars } from "../../api/customer";
import { Car } from "../../interface/Car";
import { useNavigation } from "@react-navigation/native";

const SelectCar = () => {
	const { setCar } = useAppContext();
	const navigation = useNavigation();
	const selectCar = (car: Car) => {
		setCar(car);
		navigation.goBack();
	};
	const { data, isLoading, error } = useQuery("cars", () => getCustomerCars());
	const cars = data?.data;
    useLayoutEffect(() => {
        navigation.setOptions({
            headerTitle: "Select Car"
        })
    },[])
	return (
		<CustomSafeAreaView>
			<ScrollView style={styles.container}>
				<Text style={{ fontSize: 30, fontWeight: "bold" }}>Select Car</Text>
				{cars?.length===0?<Text>No Cars</Text> :cars?.reverse().map((car) => (
					<TouchableOpacity key={car.id} style={styles.car} onPress={() => selectCar(car)} >
						<Text style={{ fontWeight: "bold", fontSize: 20 }}>
							<Text>{car.brand} -</Text>
							<Text> {car.color}</Text>
						</Text>
						<View>
							<Text>Plate Number: {car.plateNumber}</Text>
						</View>
					</TouchableOpacity>
				))}
			</ScrollView>
		</CustomSafeAreaView>
	);
};

export default SelectCar;

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
