import { StyleSheet, Text, Keyboard, View } from "react-native";
import React, { useLayoutEffect } from "react";
import { useNavigation } from "@react-navigation/native";
import { FontAwesome5 } from "@expo/vector-icons";
import { TextInput, Button, Snackbar } from "react-native-paper";
import { useForm, Controller } from "react-hook-form";
import CustomSafeAreaView from "../components/CustomSafeAreaView";
import { useMutation, useQueryClient } from "react-query";
import { loginUser } from "../api/auth";
import { useAppContext } from "../context/AppContext";
import * as SecureStore from "expo-secure-store";

const Login = () => {
	const [visible, setVisible] = React.useState(false);
	const [loginMessage, setLoginMessage] = React.useState("");
	const onToggleSnackBar = () => setVisible(!visible);
	const onDismissSnackBar = () => setVisible(false);
	const { setUser } = useAppContext();
	const queryClient= useQueryClient()
	const {
		control,
		formState: { errors },
		handleSubmit,
		getValues,setValue
	} = useForm();
	const navigation = useNavigation<any>();

	const signIn = async () => {
		Keyboard.dismiss();
		const res = await login();
		if (res.isSuccess) {
			onToggleSnackBar();
			setLoginMessage("Login Successful");
			setUser(res.data);
			await SecureStore.setItemAsync("user",JSON.stringify(res.data));
			queryClient.invalidateQueries("recent-session");
			queryClient.invalidateQueries("officerSchedule");
			
		} else {
			onToggleSnackBar();
			setLoginMessage(res.error!);
		}
	};
	const { mutateAsync: login, isLoading } = useMutation(
		() => loginUser(getValues().email, getValues().password),
		{
			mutationKey: "login",
		}
	);

	useLayoutEffect(() => {
		navigation.setOptions({
			headerShown: false,
		});
	}, []);
	const fastLogin = ()=>{
		setValue("email","hamza@hamza.com")
		setValue("password","12345678")
		handleSubmit(signIn)()
	}
	return (
		<CustomSafeAreaView>
			<Snackbar visible={visible} onDismiss={onDismissSnackBar}>
				{loginMessage}
			</Snackbar>
			<View style={styles.logoBox}>
				<FontAwesome5 name="parking" size={100} color="#4169e1" />
				<Text style={styles.logoText}>SPS</Text>
			</View>
			<View>
				<Controller
					control={control}
					rules={{
						required: true,
					}}
					render={({ field: { onChange, onBlur, value } }) => (
						<TextInput
							left={<TextInput.Icon icon="email" />}
							value={value}
							onChangeText={onChange}
							onBlur={onBlur}
							mode="outlined"
							keyboardType="email-address"
							label="Email"
							style={styles.input}
						/>
					)}
					name="email"
				/>
				{errors.email && <Text style={styles.error}>This Field is required.</Text>}
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
				<Text style={{ marginHorizontal: 20, marginTop: 10 }}>
					Don't Have an Account?{" "}
					<Text onPress={() => navigation.navigate("Register")} style={{ color: "#4169e1" }}>
						Register
					</Text>
				</Text>
			</View>
			<Button
				disabled={isLoading}
				loading={isLoading}
				style={styles.button}
				onPress={handleSubmit(signIn)}
				mode="contained"
			>
				Log in
			</Button>
		</CustomSafeAreaView>
	);
};

export default Login;

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
		borderRadius: 10,
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
});
