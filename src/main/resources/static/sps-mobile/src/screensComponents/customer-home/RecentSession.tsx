import { StyleSheet, Text, View } from "react-native";
import React, { useEffect } from "react";
import MapView, { PROVIDER_GOOGLE, Marker } from "react-native-maps";
import { Button } from "react-native-paper";
import Countdown from "react-countdown";
import { useQuery } from "react-query";
import { getLatestSession } from "../../api/customer";
import { useNavigation } from "@react-navigation/native";
import { getPrice } from "../../screens/user/History";
import { useAppContext } from "../../context/AppContext";
const RecentSession = () => {
	const { user } = useAppContext();
	const { data, isLoading, refetch } = useQuery("recent-session", () => getLatestSession(), {});
	const navigation = useNavigation<any>();
	const zone = data?.data?.zone;
	const bookingSession = data?.data?.bookingSession;
	if (isLoading)
		return (
			<View style={styles.container}>
				<Text>Loading...</Text>
			</View>
		);

	if (!data?.data?.bookingSession)
		return (
			<View style={styles.container}>
				<Text>No Session Found</Text>
			</View>
		);
	return (
		<View style={styles.container}>
			{ !!data.data ? 
				<>
					<View style={styles.idk}>
						<Text style={styles.text}>
							{bookingSession.state === "ACTIVE" ? "Current booking" : "Last booking"}{" "}
						</Text>
						<Text>{zone.tag}</Text>
					</View>
					<MapView
						style={styles.map}
						provider={PROVIDER_GOOGLE}
						initialRegion={{
							latitude: zone.lat,
							longitude: zone.lng,
							latitudeDelta: 0.015,
							longitudeDelta: 0.0121,
						}}
					>
						<Marker
							coordinate={{ latitude: zone.lat, longitude: zone.lng }}
							title="Marker Title"
							description="Marker Description"
						/>
					</MapView>
					<View style={styles.idk}>
						<View style={{ display: "flex", gap: 4 }}>
							<Text>{zone.title}</Text>
							{bookingSession && bookingSession.state === "ACTIVE" && (
								<Countdown
									date={
										Date.now() +
										calculateMillisecondsRemaining(
											bookingSession.createdAt,
											bookingSession.duration
										)
									}
									renderer={({ hours, minutes, seconds, completed }) => (
										<Text>
											Time Left : {hours.toString().padStart(2, "0")}:
											{minutes.toString().padStart(2, "0")}:{seconds.toString().padStart(2, "0")}{" "}
										</Text>
									)}
								/>
							)}
							{bookingSession && bookingSession.state !== "ACTIVE" && (
								<Text>
									Price : {getPrice(data?.data)} {formatDuration(bookingSession.duration)}{" "}
								</Text>
							)}
						</View>
						{bookingSession.state === "ACTIVE" && (
							<Button
								buttonColor="#4169e1"
								mode="contained" disabled={bookingSession.extended===true}
								onPress={() => navigation.navigate("Extend", data.data)}
							>
								<Text>Extend</Text>
							</Button>
						)}
					</View>
				</>
			:<Text>No Session Found</Text>}
		</View>
	);
};

export default RecentSession;

const styles = StyleSheet.create({
	container: {
		marginVertical: 10,
		backgroundColor: "#D8D8D8",
		padding: 10,
		borderRadius: 10,
		height: "46%",
	},
	map: {
		width: "100%",
		height: "60%",
	},
	text: {
		fontSize: 20,
		marginBottom: 10,
	},
	idk: {
		display: "flex",
		flexDirection: "row",
		justifyContent: "space-between",
		marginVertical: 5,
		alignItems: "center",
		marginTop: 10,
	},
});
export function calculateMillisecondsRemaining(createdAt: string, duration: number) {
	var currentTime = new Date().getTime(); // Get the current time in milliseconds
	var futureTime = new Date(createdAt).getTime(); // Convert the createdAt string to a Date object and get the time in milliseconds
	return futureTime + duration - currentTime;
}

export function formatDuration(duration: number) {
	var hours = Math.floor(duration / 3600000);
	var minutes = Math.floor((duration % 3600000) / 60000);

	var formattedDuration = "";
	if (hours > 0) {
		formattedDuration += hours + "h ";
	}
	if (minutes > 0) {
		formattedDuration += minutes + " minutes";
	}

	return formattedDuration;
}
