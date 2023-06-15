import {
	KeyboardAvoidingView,
	ScrollView,
	StyleSheet,
	Text,
	TouchableOpacity,
	View,
} from "react-native";
import React, { useLayoutEffect, useState } from "react";
import { useNavigation } from "@react-navigation/native";
import { TextInput, Button } from "react-native-paper";
import { useForm, Controller } from "react-hook-form";
import { useMutation, useQuery } from "react-query";
import { getAllZones } from "../../api/customer";
import CustomSafeAreaView from "../../components/CustomSafeAreaView";
import * as ImagePicker from "expo-image-picker";
import { ReportRequest, createReport } from "../../api/officer";
import { FontAwesome } from "@expo/vector-icons";
import LoadingScreen from "../LoadingScreen";
import { Dropdown } from "react-native-element-dropdown";
import { Zone } from "../../interface/Zone";
import { Toast } from "react-native-toast-message/lib/src/Toast";

const Report = ({route}:any) => {
	const [selectedImage, setSelectedImage] = useState<ImagePicker.ImagePickerAsset>();
	const [selectedZone, setSelectedZone] = useState<string>(route.params);
	const {
		control,
		handleSubmit,
		formState: { errors, isValid },
	} = useForm();

	const navigation = useNavigation<any>();

	const { data, isLoading: zonesLoading } = useQuery({
		queryKey: "zones",
		queryFn: getAllZones,
		enabled: true,
	});

	const { mutateAsync: report, isLoading } = useMutation(
		(request: ReportRequest) => createReport(request),
		{ mutationKey: "report" }
	);

	useLayoutEffect(() => {
		navigation.setOptions({
			headerTitleAlign: "center",
			headerBackVisible: false,
		});
	}, []);

	const submitReport = async (data: any) => {
		const request: ReportRequest = {
			...data,
			zoneId: Number(selectedZone!),
			imageBase64: selectedImage?.base64!,
			imageType: selectedImage?.uri.split(".").pop(),
		};

		const resp = await report(request);

		if (resp.isSuccess) {
			Toast.show({
				type: "success",
				text1: "Report submitted successfully",
				position: "top",
			});
			setTimeout(()=>{navigation.navigate("Home2")},1000)
		} else {
			Toast.show({
				type: "error",
				text1: "Something went wrong",
				position: "top",
			});
		}
	};

	const pickImage = async () => {
		let result = await ImagePicker.launchImageLibraryAsync({
			mediaTypes: ImagePicker.MediaTypeOptions.Images,
			allowsMultipleSelection: false,
			base64: true,
			quality: 1,
		});

		if (!result.canceled) {
			setSelectedImage(result.assets[0]);
		}
	};

	const takePhoto = async () => {
		let result = await ImagePicker.launchCameraAsync({
			mediaTypes: ImagePicker.MediaTypeOptions.Images,
			allowsMultipleSelection: false,
			base64: true,
			quality: 1,
		});

		if (!result.canceled) {
			setSelectedImage(result.assets[0]);
		}
	};

	if (zonesLoading) return <LoadingScreen />;
	return (
		<CustomSafeAreaView>
			<ScrollView>
				<KeyboardAvoidingView behavior="padding" style={styles.container}>
					<ScrollView>
						{/* plate number */}
						<Controller
							control={control}
							rules={{
								required: true,
								minLength: 3,
							}}
							render={({ field: { onChange, onBlur, value } }) => (
								<TextInput
									left={<TextInput.Icon icon="numeric" />}
									value={value}
									onChangeText={onChange}
									onBlur={onBlur}
									mode="outlined"
									label="Plate Number"
									style={styles.input}
								/>
							)}
							name="plateNumber"
						/>
						{errors.plateNumber && (
							<Text style={styles.error}>
								this Field is Required and should be at least 3 characters.
							</Text>
						)}

						{/* car brand */}

						<Controller
							control={control}
							rules={{
								required: true,
								minLength: 3,
							}}
							render={({ field: { onChange, onBlur, value } }) => (
								<TextInput
									left={<TextInput.Icon icon="car" />}
									value={value}
									onChangeText={onChange}
									onBlur={onBlur}
									mode="outlined"
									label="Car Brand"
									style={styles.input}
								/>
							)}
							name="carBrand"
						/>
						{errors.carBrand && (
							<Text style={styles.error}>
								this Field is Required and should be at least 3 characters.
							</Text>
						)}

						{/* email  */}

						<Controller
							control={control}
							rules={{
								required: true,
								minLength: 3,
							}}
							render={({ field: { onChange, onBlur, value } }) => (
								<TextInput
									left={<TextInput.Icon icon="palette" />}
									value={value}
									onChangeText={onChange}
									onBlur={onBlur}
									mode="outlined"
									label="Car Color"
									style={styles.input}
								/>
							)}
							name="carColor"
						/>
						{errors.carColor && (
							<Text style={styles.error}>
								this Field is Required and should be at least 3 characters.
							</Text>
						)}
						<Dropdown
							style={styles.dropdown}
							placeholderStyle={styles.placeholderStyle}
							selectedTextStyle={styles.selectedTextStyle}
							inputSearchStyle={styles.inputSearchStyle}
							iconStyle={styles.iconStyle}
							value={String(selectedZone)}
							data={parseZonesToDropdown(data?.data!)}
							search={false}
							maxHeight={300}
							labelField="label"
							valueField="value"
							placeholder="Select zone related to the violation"
							onChange={(item) => {
								setSelectedZone(item.value);
							}}
						/>
						<Controller
							control={control}
							rules={{
								required: true,
								minLength: 20,
							}}
							render={({ field: { onChange, onBlur, value } }) => (
								<TextInput
									value={value}
									onChangeText={onChange}
									onBlur={onBlur}
									mode="outlined"
									multiline
									numberOfLines={10}
									label="Violation Details"
									style={[styles.input]}
								/>
							)}
							name="details"
						/>
						{errors.details && (
							<Text style={styles.error}>
								this Field is Required and should be at least 20 characters.
							</Text>
						)}

						<View style={{ marginTop: 10, flexDirection: "row", gap: 10 }}>
							{!selectedImage ? (
								<>
									<TouchableOpacity
										style={{
											backgroundColor: "#4169e1",
											padding: 15,
											borderRadius: 10,
											justifyContent: "center",
											alignItems: "center",
											flex: 1,
										}}
										onPress={takePhoto}
									>
										<FontAwesome name="camera" size={24} color="white" />
									</TouchableOpacity>
									<TouchableOpacity
										style={{
											backgroundColor: "#4169e1",
											padding: 15,
											borderRadius: 10,
											justifyContent: "center",
											alignItems: "center",
											flex: 1,
										}}
										onPress={pickImage}
									>
										<FontAwesome name="image" size={24} color="white" />
									</TouchableOpacity>
								</>
							) : (
								<TouchableOpacity
									style={{
										backgroundColor: "#4169e1",
										padding: 15,
										borderRadius: 10,
										justifyContent: "center",
										alignItems: "center",
										flex: 1,
										flexDirection: "row",
										gap: 10,
									}}
									onPress={() => setSelectedImage(undefined)}
								>
									<Text style={{ color: "white" }}>Remove selected image</Text>
									<FontAwesome name="remove" size={24} color="white" />
								</TouchableOpacity>
							)}
						</View>
						<Button
							disabled={isLoading || !selectedImage || !selectedZone}
							loading={isLoading}
							style={styles.button}
							buttonColor="#4169e1"
							mode="contained"
							onPress={handleSubmit(submitReport)}
						>
							Submit
						</Button>
					</ScrollView>
				</KeyboardAvoidingView>
			</ScrollView>
		</CustomSafeAreaView>
	);
};

export default Report;
const styles = StyleSheet.create({
	logoBox: {
		backgroundColor: "white",
		width: "50%",
		alignItems: "center",
		alignSelf: "center",
		marginTop: 40,
		borderRadius: 10,
		paddingVertical: 10,
	},
	logoText: { fontSize: 50, fontWeight: "bold" },
	input: {
		borderBottomWidth: 0,
	},
	button: {
		padding: 10,
		marginTop: 10,
		borderRadius: 10,
	},
	error: {
		color: "red",
		marginVertical: 10,
	},
	container: { gap: 4, paddingBottom: 100, marginHorizontal: 20 },
	dropdown: {
		marginVertical: 16,
		height: 50,
		borderWidth: 1,
		padding: 10,
		borderRadius: 5,
		borderColor: "gray",
	},
	selectedTextStyle: {
		fontSize: 16,
		color: "red",
	},
	placeholderStyle: {
		fontSize: 16,
	},
	inputSearchStyle: {
		height: 40,
		fontSize: 16,
	},
	iconStyle: {
		width: 20,
		height: 20,
	},
});

function parseZonesToDropdown(zones: Zone[]) {
	return zones.map((zone) => ({
		label: zone.title.slice(0, 20) + " - " + zone.tag,
		value: String(zone.id),
	}));
}
