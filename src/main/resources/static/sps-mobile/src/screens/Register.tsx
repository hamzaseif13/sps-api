import {
	Alert,
	KeyboardAvoidingView,
	Platform,
	ScrollView,
	StyleSheet,
	Text,
	TouchableOpacity,
	View,
} from "react-native";
import React, { useLayoutEffect } from "react";
import { useNavigation } from "@react-navigation/native";
import CustomSafeAreaView from "../components/CustomSafeAreaView";
import { TextInput, Button, Snackbar } from "react-native-paper";
import { useForm, Controller } from "react-hook-form";
import { useMutation, useQueryClient } from "react-query";
import { registerCustomer } from "../api/customer";
import { useAppContext } from "../context/AppContext";
import * as SecureStore from "expo-secure-store";
const Register = () => {
	const [visible, setVisible] = React.useState(false);
	const [registerMessage, setRegisterMessage] = React.useState("");
	const onToggleSnackBar = () => setVisible(!visible);
	const onDismissSnackBar = () => setVisible(false);
	const { setUser } = useAppContext();
	const [passwordError, setPasswordError] = React.useState<string>();
	const queryClient = useQueryClient();
	const {
		control,
		handleSubmit,
		formState: { errors },
		setError,
	} = useForm();
	const navigation = useNavigation<any>();
	const { mutateAsync: registerCust, isLoading } = useMutation(
		(registerRequest: RegisterCustomerRequest) => registerCustomer(registerRequest),
		{ mutationKey: "register" }
	);
	useLayoutEffect(() => {
		navigation.setOptions({
			headerTitleAlign: "center",
			headerBackVisible: false,
		});
	}, []);

	const register = async (data: any) => {
		if (!validatePassword(data.password, data.confirmPassword)) {
			setPasswordError("Password does not match");
			return;
		}
		setPasswordError(undefined);
		const res = await registerCust({
			email: data.email,
			firstName: data.firstName,
			lastName: data.lastName,
			password: data.password,
			phoneNumber: data.phoneNumber,
		});
		if (res.isSuccess) {
			onToggleSnackBar();
			setUser(res.data!);
			setRegisterMessage("Register Successful");
			await SecureStore.setItemAsync("user",JSON.stringify(res.data))
			queryClient.invalidateQueries("recent-session");
		} else {
			onToggleSnackBar();
			setRegisterMessage(res.error!);
		}
	};
	return (
		<CustomSafeAreaView>
			<Snackbar visible={visible} onDismiss={onDismissSnackBar}>
				{registerMessage}
			</Snackbar>
			<ScrollView>
				<KeyboardAvoidingView behavior="padding" style={styles.container}>
					{/* first name  */}

					<Controller
						control={control}
						rules={{
							required: true,minLength:3
						}}
						render={({ field: { onChange, onBlur, value } }) => (
							<TextInput
								left={<TextInput.Icon icon="account" />}
								value={value}
								onChangeText={onChange}
								onBlur={onBlur}
								mode="outlined"
								label="First Name"
								style={styles.input}
							/>
						)}
						name="firstName"
					/>
					{errors.firstName && <Text style={styles.error}>this Field is Required and should be at least 3 characters.</Text>}

					{/* last name  */}

					<Controller
						control={control}
						rules={{
							required: true,minLength:3
						}}
						render={({ field: { onChange, onBlur, value } }) => (
							<TextInput
								left={<TextInput.Icon icon="account" />}
								value={value}
								onChangeText={onChange}
								onBlur={onBlur}
								mode="outlined"
								label="Last Name"
								style={styles.input}
							/>
						)}
						name="lastName"
					/>
					{errors.lastName && <Text style={styles.error}>this Field is Required and should be at least 3 characters.</Text>}

					{/* email  */}

					<Controller
						control={control}
						rules={{
							required: true,
							pattern: {
								value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
								message: "Invalid email address",
							},
						}}
						render={({ field: { onChange, onBlur, value } }) => (
							<TextInput
								left={<TextInput.Icon icon="email" />}
								value={value}
								onChangeText={onChange}
								onBlur={onBlur}
								mode="outlined"
								label="Email"
								style={styles.input}
							/>
						)}
						name="email"
					/>
					{errors.email && <Text style={styles.error}>Please Enter a Valid Email</Text>}

					{/* phone  */}

					<Controller
						control={control}
						rules={{
							required: true,minLength:10
						}}
						render={({ field: { onChange, onBlur, value } }) => (
							<TextInput
								left={<TextInput.Icon icon="phone" />}
								value={value}
								onChangeText={onChange}
								onBlur={onBlur}
								mode="outlined"
								label="Phone"
								keyboardType="phone-pad"
								style={styles.input}
							/>
						)}
						name="phoneNumber"
					/>
					{errors.phoneNumber && <Text style={styles.error}>This Field is required and should be 10 digits at least.</Text>}

					{/* password  */}

					<Controller
						control={control}
						rules={{
							required: true,
							minLength: 8,
						}}
						render={({ field: { onChange, onBlur, value } }) => (
							<TextInput
								left={<TextInput.Icon icon="lock" />}
								value={value}
								onChangeText={onChange}
								onBlur={onBlur}
								mode="outlined"
								label="Password"
								keyboardType="visible-password"
								style={styles.input}
							/>
						)}
						name="password"
					/>
					{errors.password && (
						<Text style={styles.error}>
							This Field is required. and should be at Least 8 characters
						</Text>
					)}

					{/* confirm password  */}

					<Controller
						control={control}
						rules={{
							required: true,
							minLength: 8,
						}}
						render={({ field: { onChange, onBlur, value } }) => (
							<TextInput
								left={<TextInput.Icon icon="lock" />}
								value={value}
								onChangeText={onChange}
								onBlur={onBlur}
								mode="outlined"
								label="Confirm Password"
								keyboardType="visible-password"
								style={styles.input}
							/>
						)}
						name="confirmPassword"
					/>
					{errors.confirmPassword && (
						<Text style={styles.error}>
							This Field is required. and should be at Least 8 characters
						</Text>
					)}
					{passwordError && <Text style={styles.error}>{passwordError}</Text>}
					<Text style={{ marginHorizontal: 20, marginTop: 10 }}>
						Already Have an Account?{" "}
						<Text onPress={() => navigation.navigate("Login")} style={{ color: "#4169e1" }}>
							Login
						</Text>
					</Text>
					<Button
						disabled={isLoading}
						loading={isLoading}
						style={styles.button}
						onPress={handleSubmit(register)}
						mode="contained"
					>
						Register
					</Button>
				</KeyboardAvoidingView>
			</ScrollView>
		</CustomSafeAreaView>
	);
};

export default Register;
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
		marginHorizontal: 20,
		borderBottomWidth: 0,
	},
	button: {
		backgroundColor: "#4169e1",
		padding: 10,
		marginHorizontal: 20,
		marginTop: 20,
		borderRadius: 10,
	},
	error: {
		marginLeft: 20,
		color: "red",
	},
	container: { gap: 4, paddingBottom: 100 },
});
function validatePassword(password: string, confirmPassword: string) {
	if (password !== confirmPassword) {
		return false;
	}
	return true;
}
