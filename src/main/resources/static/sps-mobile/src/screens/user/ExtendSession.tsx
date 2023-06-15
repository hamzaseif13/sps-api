import React,{useEffect,useLayoutEffect} from "react";
import { View, Text, StyleSheet, TouchableOpacity, SafeAreaView } from "react-native";
import { FontAwesome } from "@expo/vector-icons";
import Countdown from "react-countdown";
import { calculateMillisecondsRemaining, formatDuration } from "../../screensComponents/customer-home/RecentSession";
import { useMutation, useQueryClient } from "react-query";
import { extendBookingSession } from "../../api/customer";
import LoadingSpinner from "../../components/LoadingSpinner";
import { Toast } from "react-native-toast-message/lib/src/Toast";
import { useNavigation } from "@react-navigation/native";

const ExtendSession = ({ route }: any) => {
	const queryClient = useQueryClient();
	const bookingSession = route.params.bookingSession;
	const zone = route.params?.zone;
	const navigation = useNavigation<any>();
	const { mutateAsync, isLoading, error } = useMutation(
		"extend",
		(request: { zoneId: number; durationInMs: number; sessionId: number }) =>
			extendBookingSession(request)
	);
	const extend = async (durationInMs: number) => {
		const resp = await mutateAsync({ sessionId: bookingSession.id, durationInMs, zoneId: zone.id });
		if(resp.isSuccess){
			Toast.show({
				type: "success",
				text1: "Success",
				text2: "Session extended successfully",
			})
			queryClient.invalidateQueries("recent-session");
			queryClient.invalidateQueries("history");
			queryClient.invalidateQueries("wallet");
			navigation.navigate("Home2")
		}
		else{
			Toast.show({
				type: "error",
				text1: "Error",
				text2: resp.error,
			})
		}
	};
	useEffect(()=>{},[route.params])
	useLayoutEffect(()=>{
		navigation.setOptions({
			headerTitle:"Extend - "+formatDuration(bookingSession.duration)
		})
	},[])
	return (
		<SafeAreaView style={styles.container}>
			<View style={styles.topLeft}>
				<Text style={styles.location}>{zone.title} </Text>
				<View style={styles.timeZone}>
					<Text style={styles.time}>{dateFormatter(bookingSession.createdAt)}</Text>
					<View>
						<Text style={styles.tagHeader}>
							{zone.tag.split("-")[0]}-<Text style={styles.tagBody}>{zone.tag.split("-")[1]}</Text>
						</Text>
					</View>
				</View>
				
				<View style={{marginBottom:20}}>
					<Countdown
						date={
							Date.now() +
							calculateMillisecondsRemaining(bookingSession.createdAt, bookingSession.duration)
						}
						renderer={({ hours, minutes, seconds, completed }) => (
							<Text style={styles.timerText}>
								{hours.toString().padStart(2, "0")}:{minutes.toString().padStart(2, "0")}:
								{seconds.toString().padStart(2, "0")}{" "}
							</Text>
						)}
					/>
				</View>
			</View>

			<View style={styles.bottomLeft}>
				<Text style={styles.extend}>Extend</Text>

				<View style={{ flexDirection: "column" }}>
					{[15, 30, 45, 60].map((item, index) => (
						<TouchableOpacity key={index} style={styles.button} onPress={() => extend(item * 60 * 1000)}>
							<FontAwesome name="plus" size={20} color="#4169e1" />
							<Text style={styles.buttonText}>Add {item} minutes</Text>
						</TouchableOpacity>
					))}
				</View>
			</View>
			<LoadingSpinner  visible={isLoading}/>
		</SafeAreaView>
	);
};

const styles = StyleSheet.create({
	container: {
		flex: 1,
		padding: 20,
	},
	topLeft: {
		marginTop: 20,
	},
	location: {
		fontSize: 30,
		fontWeight: "bold",
	},
	timeZone: {
		flexDirection: "row",
		marginTop: 15,
		justifyContent: "space-between",
		alignItems: "center",
	},
	time: {
		fontSize: 20,
		fontWeight: "bold",
		marginRight: 10,
		padding: 5,
	},
	zone: {
		fontSize: 20,
		fontWeight: "bold",
		backgroundColor: "#e0e0e0",
		padding: 5,
		borderRadius: 10,
	},
	timerText: {
		fontSize: 40,
		fontWeight: "bold",
		alignSelf: "center",
		marginTop: 40,
	},
	bottomLeft: {
		flex: 0.6,
		justifyContent: "center",
		marginBottom: 15,
		marginRight: 90,
		alignItems: "flex-start",
	},
	extend: {
		fontSize: 30,
		fontWeight: "bold",
		marginBottom: 10,
	},
	button: {
		padding: 10,
		borderRadius: 5,
		flexDirection: "row",
		marginBottom: 15,
	},
	buttonText: {
		left: 10,
		fontSize: 16,
		marginRight: 80,
	},
	tagHeader: {
		fontSize: 20,
		fontWeight: "bold",
		color: "black",
		backgroundColor: "#e0e0e0",
		borderRadius: 15,
		padding: 8,
	},
	tagBody: {
		fontWeight: "bold",
		color: "#4169e1",
	},
});

export default ExtendSession;
function dateFormatter(dateString: string) {
	const date = new Date(dateString);

	const options: any = {
		year: "numeric",
		month: "short",
		day: "numeric",
		hour: "numeric",
		minute: "numeric",
		hour12: true,
	};

	const formattedDate = new Intl.DateTimeFormat("en-US", options).format(date);

	return formattedDate;
}
