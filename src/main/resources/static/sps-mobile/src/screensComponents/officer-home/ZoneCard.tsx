import { Linking, ScrollView, StyleSheet, Text, View } from "react-native";
import React from "react";
import { Entypo } from "@expo/vector-icons";
import { Button } from "react-native-paper";
import { ScheduleZone, Zone } from "../../interface/Zone";
import { useNavigation } from "@react-navigation/native";
interface Props {
	zone: Zone | ScheduleZone;
}

const ZoneCard: React.FC<Props> = ({ zone }) => {
	const navigation = useNavigation<any>();
    const openMap = ()=>{
        if("location" in zone){
            return handleOpenMaps(zone.location.lat, zone.location.lng)
        }
        handleOpenMaps(zone.lat, zone.lng)
    }
	return (
		<View style={styles.card}>
			<View style={styles.row}>
				<View>
					<Text style={{ fontWeight: "bold", fontSize: 18 }}>{zone.title}</Text>
					<Text>
						{"location" in zone ? zone.location.address.slice(0, 30) : zone.address.slice(0, 30)}..
					</Text>
				</View>

				<Text style={styles.tagHeader}>
					{zone.tag.split("-")[0]}-<Text style={styles.tagBody}>{zone.tag.split("-")[1]}</Text>
				</Text>
			</View>
			<View style={[styles.row, { marginTop: 10 }]}>
				<Entypo
					onPress={openMap}
					name="location-pin"
					size={40}
					color="#4169e1"
				/>
				<Button
					onPress={() => navigation.navigate("ZoneDetails", zone)}
					mode="contained"
					buttonColor="#4169e1"
				>
					Details
				</Button>
			</View>
		</View>
	);
};

export default ZoneCard;

const styles = StyleSheet.create({
	row: {
		flexDirection: "row",
		justifyContent: "space-between",
		alignItems: "center",
		width: "100%",
	},
	card: {
		backgroundColor: "#e0e0e0",
		padding: 10,
		borderRadius: 10,
		marginVertical: 5,
	},
	tagHeader: {
		fontSize: 20,
		fontWeight: "bold",
		color: "black",
		backgroundColor: "#e0e0e0",
		borderRadius: 15,
		padding: 8,
		width: "30%",
		textAlign: "center",
	},
	tagBody: {
		fontWeight: "bold",
		color: "#4169e1",
	},
});
export const handleOpenMaps = (latitude:number,longitude:number) => {
	const url = `https://www.google.com/maps/search/?api=1&query=${latitude},${longitude}`;
	Linking.openURL(url);
};