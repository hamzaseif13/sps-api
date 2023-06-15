import { StyleSheet, Text, View, Vibration } from "react-native";
import React, { useState, useEffect } from "react";
import { BarCodeScanner } from "expo-barcode-scanner";
import { Button } from "react-native-paper";
import CustomSafeAreaView from "../../components/CustomSafeAreaView";
import LoadingScreen from "../LoadingScreen";
import { useNavigation } from "@react-navigation/native";
import { useMutation } from "react-query";
import { validateSpaceNumber } from "../../api/customer";
const ScanQR = () => {
	const [hasPermission, setHasPermission] = useState<boolean>(false);
	const [scanned, setScanned] = useState(false);
	const [data, setData] = useState<string>("Not Scanned Yet");
	const [loading, setLoading] = useState<boolean>(false);
	const navigation = useNavigation<any>();
	const getBarCodeScannerPermissions = async () => {
		const { status } = await BarCodeScanner.requestPermissionsAsync();
		setHasPermission(status === "granted");
	};
	useEffect(() => {
		getBarCodeScannerPermissions();
	}, []);

	const {mutateAsync,isLoading } = useMutation("validate-qr",(req:any)=>validateSpaceNumber(req))
	if (hasPermission === false) {
		return (
			<View style={{ margin: 20 }}>
				<Text style={{ fontSize: 20 }}>
					This Feature Requires Camera Permission in able to Work probably, please go to settings
					and give SPS app camera permeation.{" "}
				</Text>
				<Button
					mode="contained"
					style={{ borderRadius: 10, marginTop: 20 }}
					onPress={getBarCodeScannerPermissions}
					buttonColor="#4169e1"
				>
					Enable Camera
				</Button>
			</View>
		);
	}
	const handleBarCodeScanned = ({ type, data }: any) => {
		setLoading(true);
		Vibration.vibrate(100);
		setTimeout(() => {
			navigation.navigate("Confirm",data);
			setLoading(false);
		}, 1000);
	};

	if (loading) {
		return <LoadingScreen />;
	}
	return (
		<CustomSafeAreaView>
			<View style={{ marginHorizontal: 20, flex: 1, alignItems: "center" }}>
				<BarCodeScanner onBarCodeScanned={handleBarCodeScanned} style={styles.camera} />
				<Text style={{marginTop:20}}>Scan the QR code located in the parking space.</Text>
			</View>
		</CustomSafeAreaView>
	);
};

export default ScanQR;

const styles = StyleSheet.create({
	camera: {
		width: "80%",
		height: "50%",
	},
});
