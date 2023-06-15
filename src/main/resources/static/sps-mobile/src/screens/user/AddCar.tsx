import { StyleSheet, Text, View, Keyboard } from "react-native";
import React from "react";
import CustomSafeAreaView from "../../components/CustomSafeAreaView";
import { Controller, useForm } from "react-hook-form";
import { Button, TextInput } from "react-native-paper";
import { CreateCarRequest } from "../../interface/Car";
import { useMutation, useQueryClient } from "react-query";
import { registerNewCarr } from "../../api/customer";
import Toast from "react-native-toast-message";
import { useNavigation } from "@react-navigation/native";
const AddCar = () => {
	const queryClient = useQueryClient();
	const navigation = useNavigation();
	const {
		control,
		formState: { errors },
		handleSubmit,
		getValues,
	} = useForm();
	const { mutateAsync: createCar, isLoading } = useMutation(
		(createCarRequest: CreateCarRequest) => registerNewCarr(createCarRequest),
		{
			mutationKey: "createCar",
			onSuccess: () => {
				queryClient.invalidateQueries("cars");
				queryClient.refetchQueries("cars");
			},
		}
	);
	const addCar = async (data: any) => {
		Keyboard.dismiss();
		const resp = await createCar(data);
		if (resp.isSuccess) {
			Toast.show({
				type: "success",
				text1: "Car Created Successfully",
				visibilityTime: 1000,
			});
			navigation.goBack();
		} else {
			Toast.show({
				type: "error",
				text1: "Something went wrong",
				text2: resp.error,
			});
		}
	};
	return (
		<CustomSafeAreaView>
			<View style={styles.container}>
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
							label="brand"
							style={styles.input}
						/>
					)}
					name="brand"
				/>
				{errors.brand && (
					<Text style={styles.error}>
						this Field is Required and should be at least 3 characters.
					</Text>
				)}

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
							label="Color"
							style={styles.input}
						/>
					)}
					name="color"
				/>
				{errors.color && (
					<Text style={styles.error}>
						this Field is Required and should be at least 3 characters.
					</Text>
				)}
				<Button
					disabled={isLoading}
					loading={isLoading}
					style={styles.button}
					onPress={handleSubmit(addCar)}
					mode="contained"
				>
					Register
				</Button>
			</View>
		</CustomSafeAreaView>
	);
};

export default AddCar;

const styles = StyleSheet.create({
	container: {
		paddingHorizontal: 20,
	},
	input: {
		marginHorizontal: 20,
		borderBottomWidth: 0,
		marginVertical: 5,
	},
	error: {
		marginLeft: 20,
		color: "red",
	},
	button: {
		backgroundColor: "#4169e1",
		padding: 10,
		marginHorizontal: 20,
		marginTop: 20,
		borderRadius: 10,
	},
});
