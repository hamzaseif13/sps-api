import {
	ActivityIndicator,
	Linking,
	RefreshControl,
	ScrollView,
	StyleSheet,
	Text,
	View,
} from "react-native";
import React, { useEffect } from "react";
import { useQuery } from "react-query";
import { getOfficerSchedule } from "../../api/officer";
import ZoneCard from "./ZoneCard";
import { useAppContext } from "../../context/AppContext";
const TodaySchedule = () => {
	const { user } = useAppContext();
	const { data, isLoading, error, refetch, isRefetching } = useQuery(
		"officerSchedule",
		() => getOfficerSchedule(),
		{ enabled: true }
	);
	if(isLoading) return <ActivityIndicator style={{marginTop:20}} size="large" color="#4169e1" />
	return (
		<View style={styles.container}>
			{data?.data && (
				<>
					<View
						style={{ flexDirection: "row", alignItems: "center", justifyContent: "space-between" }}
					>
						<Text style={{ fontWeight: "bold", fontSize: 20 }}>TodaySchedule</Text>
						<Text style={{ fontSize: 15 }}>
							{convertTimeFormat(data.data.startsAt)} - {convertTimeFormat(data.data.endsAt)}
						</Text>
					</View>
					<View style={{ flexDirection: "row", columnGap: 10, flexWrap: "wrap" }}>
						{data.data.daysOfWeek.map((day, index) => {
							return (
								<Text
									key={index}
									style={{
										marginTop: 10,
										fontSize: 20,
										backgroundColor: "#e0e0e0",
										padding: 5,
										borderRadius: 10,
									}}
								>
									{day.toLowerCase()}
								</Text>
							);
						})}
					</View>
					<ScrollView
						style={{ gap: 10, marginTop: 10, paddingBottom: 100 }}
						refreshControl={<RefreshControl refreshing={isRefetching} onRefresh={refetch} />}
					>
						{data.data.zones &&
							data.data.zones.length > 0 &&
							data.data.zones.map((zone, index) => {
								return <ZoneCard zone={zone} key={index} />;
							})}
					</ScrollView>
				</>
			)}
		</View>
	);
};

export default TodaySchedule;

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
	container: {
		marginTop: 20,
		flex: 1,
		paddingBottom: 100,
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
export function convertTimeFormat(time: string) {
	const [hours, minutes] = time.split(":");
	let suffix = "AM";
	let formattedHours = parseInt(hours);

	if (formattedHours >= 12) {
		suffix = "PM";
		formattedHours -= 12;
	}

	if (formattedHours === 0) {
		formattedHours = 12;
	}

	const formattedHoursString = formattedHours.toString().padStart(2, "0");

	return `${formattedHoursString}:${minutes} ${suffix}`;
}
