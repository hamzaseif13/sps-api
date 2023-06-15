import { StyleSheet, Text, View, TouchableOpacity, ScrollView } from "react-native";
import React, { useLayoutEffect } from "react";
import { useNavigation } from "@react-navigation/native";
import { ScheduleZone, Zone } from "../../interface/Zone";
import CustomSafeAreaView from "../../components/CustomSafeAreaView";
import { convertTimeFormat } from "../../screensComponents/officer-home/TodaySchedule";
import { Button } from "react-native-paper";

const ZoneDetails = ({ route }: any) => {
	const navigation = useNavigation<any>();
	const zone: Zone | ScheduleZone = route.params!;
	useLayoutEffect(() => {
		navigation.setOptions({
			headerTitle: zone.title,
		});
	}, []);
	const getAddress = () => {
		if ("location" in zone) {
			return zone.location.address;
		}
		return zone.address;
	};
	const getSpaces = () => {
		if ("spaces" in zone) {
			return zone.spaces.sort((a, b) => a.number - b.number);
		}
		return zone.spaceList.sort((a, b) => a.number - b.number);
	};

	return (
		<CustomSafeAreaView>
			<View style={styles.container}>
				<Text style={{ fontWeight: "bold", fontSize: 27, textAlign: "left" }}>{getAddress()}</Text>
				<View
					style={{
						flexDirection: "row",
						justifyContent: "space-between",
						alignItems: "center",
						marginTop: 10,
					}}
				>
					<Text style={styles.tagHeader}>
						{zone.tag.split("-")[0]}-<Text style={styles.tagBody}>{zone.tag.split("-")[1]}</Text>
					</Text>
					<Text style={{ fontSize: 20 }}>
						{convertTimeFormat(zone.startsAt)} - {convertTimeFormat(zone.endsAt)}
					</Text>
				</View>
				<View
					style={{
						flexDirection: "row",
						justifyContent: "space-between",
						alignItems: "center",
						marginBottom: 20,
					}}
				>
					<Text style={{ fontSize: 20, marginVertical: 10 }}>
						Total spaces : {zone.numberOfSpaces}
					</Text>
					<Text style={{ fontSize: 20, marginVertical: 10 }}>Fees : {zone.fee}/hour JD</Text>
				</View>
				<Button
					mode="contained"
					style={{ borderRadius: 10, marginBottom: 20 }}
					buttonColor="#4169e1"
					onPress={() => navigation.navigate("Report", zone.id)}
				>
					Report violation
				</Button>
				<ScrollView>
					<View style={styles.container2}>
						{getSpaces().map((space, index) => (
							<TouchableOpacity
								disabled={space.state === "AVAILABLE"}
								onPress={() => navigation.navigate("SpaceDetails", space.id)}
								key={index}
								style={[
									styles.card,
									{ backgroundColor: space.state === "AVAILABLE" ? "#77DD77" : "red" },
								]}
							>
								<Text style={styles.cardText}>{space.number}</Text>
							</TouchableOpacity>
						))}
					</View>
				</ScrollView>
			</View>
		</CustomSafeAreaView>
	);
};

const SpaceCard = () => {
	return (
		<View style={styles.spaceCard}>
			<Text>1</Text>
		</View>
	);
};
export default ZoneDetails;

const styles = StyleSheet.create({
	container: {
		paddingHorizontal: 20,
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
	spaceCard: {
		backgroundColor: "green",
		padding: 5,
		width: "45%",
	},
	spaces: {
		flexDirection: "row",
		flexWrap: "wrap",
		justifyContent: "space-between",
		gap: 10,
	},
	container2: {
		flex: 1,
		backgroundColor: "#fff",
		flexDirection: "row",
		flexWrap: "wrap",
		justifyContent: "space-between",paddingBottom:30
	},
	card: {
		width: "48%",
		aspectRatio: 1,
		justifyContent: "center",
		alignItems: "center",
		marginBottom: 20,
		borderRadius: 10,
	},
	cardText: {
		fontSize: 24,
	},
});
