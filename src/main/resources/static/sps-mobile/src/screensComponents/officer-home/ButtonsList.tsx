import { StyleSheet, Text, View } from "react-native";
import React from "react";
import { Entypo, AntDesign, FontAwesome, MaterialIcons } from "@expo/vector-icons";
import HomeButton from "../../components/HomeButton";
import { Button } from "react-native-paper";

const ButtonsList = () => {
	return (
		<View style={styles.buttonsContainer}>
			<HomeButton title="Map" target="Map">
				<Entypo name="location-pin" size={60} color="#4169e1" />
			</HomeButton>

			<HomeButton title="Report" target="Report">
				<MaterialIcons name="report" size={60} color="#4169e1" />
			</HomeButton>
		</View>
	);
};

export default ButtonsList;

const styles = StyleSheet.create({
	buttonsContainer: {
		display: "flex",
		flexDirection: "row",
		flexWrap: "wrap",
		justifyContent: "space-between",
		gap: 7,
		marginTop: 20,
	},
});
