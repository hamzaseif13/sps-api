import React, { useState, useEffect, useRef } from "react";
import {
	View,
	StyleSheet,
	StatusBar,
	Alert,
	TouchableOpacity,
} from "react-native";
import MapView, { PROVIDER_GOOGLE, Marker } from "react-native-maps";
import * as Location from "expo-location";
import { useNavigation } from "@react-navigation/native";
import { useQuery } from "react-query";
import { getAllZones } from "../../api/customer";
import { Zone } from "../../interface/Zone";

import ParkingMarker from "../../screensComponents/customer-map/ParkingMarker";
import { MaterialIcons } from "@expo/vector-icons";
const LAT_DELTA = 0.015;
const LONG_DELTA = 0.0121;
const Map = () => {

	const {
		data,
		isLoading: zonesLoading,
		refetch,
	} = useQuery({
		queryKey: "zones",
		queryFn: getAllZones,
		enabled: true,
		onSuccess: (resp) => {
			if (resp.isSuccess) {
				setZones(resp.data!);
			} else {
				Alert.alert("Error", resp.error);
			}
		},
	});
	const [zones, setZones] = useState<Array<Zone>>([]);
	const navigation = useNavigation<any>();
	const [location, setLocation] = useState<{ longitude: number; latitude: number }>({
		longitude: 35.9106,
		latitude: 31.9539,
	});
	const mapRef = useRef<any>(null);
	const [errorMsg, setErrorMsg] = useState("");
	const getUserLocation = async () => {
		let { status } = await Location.requestForegroundPermissionsAsync();
		if (status !== "granted") {
			Alert.alert("Location Permission", "Please allow location permission to use this feature", [
				{
					text: "Allow",
					onPress: () => getUserLocation(),
				},
				{
					text: "Cancel",
					onPress: navigation.goBack,
				},
			]);
			return;
		}
		Location.getCurrentPositionAsync({}).then((location) => {
			setLocation(location.coords);
			mapRef.current.animateToRegion({
				...location,
				latitudeDelta: LAT_DELTA,
				longitudeDelta: LONG_DELTA,
			});
		});
	};
	useEffect(() => {
		getUserLocation();
		refetch();
	}, []);
	function animateToCurrent() {
		mapRef.current.animateToRegion({
			...location,
			latitudeDelta: LAT_DELTA,
			longitudeDelta: LONG_DELTA,
		});
	}
	return (
		<View style={styles.container}>
			<StatusBar backgroundColor="#003f5c" barStyle="light-content" />
			<MapView
				ref={mapRef}
				style={styles.map}
				provider={PROVIDER_GOOGLE}
				initialRegion={{
					...location,
					latitudeDelta: LAT_DELTA,
					longitudeDelta: LONG_DELTA,
				}}
			>
				<Marker
					anchor={{ x: 0.5, y: 0.5 }}
					zIndex={1000}
					coordinate={location} // replace with your user location coordinates
				>
					<View style={styles.marker}>
						<View style={styles.circle} />
					</View>
				</Marker>
				{zones.map((zone) => (
					<Marker
						key={zone.id}
						coordinate={{
							latitude: zone.lat,
							longitude: zone.lng,
						}}
						onPress={() => {
							navigation.navigate("ZoneDetails",  zone );
						}}
					>
						<ParkingMarker
							tag={zone.tag}
							totalSpaces={zone.numberOfSpaces}
							availableSpaces={zone.availableSpaces}
						/>
					</Marker>
				))}
			</MapView>
		
			<TouchableOpacity onPress={animateToCurrent} style={[styles.floating,{top:20}]}>
				<MaterialIcons name="location-searching" size={30} color="black" />
			</TouchableOpacity>
		</View>
	);
};

export default Map;

const styles = StyleSheet.create({
	container: {
		height: "100%",
	},
	map: {
		width: "100%",
		height: "100%",
	},
	floatingSection: {
		backgroundColor: "white",
		position: "absolute",
		bottom: 40,
		width: "100%",
		height: 200,
		borderTopLeftRadius: 30,
		borderTopRightRadius: 30,
		padding: 20,
		shadowColor: "#000",
		shadowOffset: { width: 0, height: 3 },
		shadowOpacity: 0.2,
		shadowRadius: 3,
		elevation: 3,
	},
	hide: {
		display: "none",
	},
	show: {
		display: "flex",
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
	header: {
		flexDirection: "row",
		justifyContent: "space-between",
		alignItems: "center",
		fontSize: 25,
		color: "#4169e1",
	},
	avlSpaces: {
		fontSize: 20,
	},
	marker: {
		alignItems: "center",
		justifyContent: "center",
	},
	circle: {
		width: 25,
		height: 25,
		borderRadius: 100,
		backgroundColor: "#4169e1",
	},
	floating: {
		position: "absolute",
		backgroundColor: "white",
		width: 60,
		height: 60,
		borderRadius: 100,
		justifyContent: "center",
		alignItems: "center",
		borderWidth: 1,
		borderColor: "lightgrey",
		right: 10,
	},
});
