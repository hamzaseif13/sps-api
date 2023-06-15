import React from "react";
import { useAppContext } from "../context/AppContext";
import OfficerNavigation from "./OfficerNavigation";
import CustomerNavigation from "./CustomerNavigation";
import NotAuthNavigation from "./NotAuthNavigation";
import { useQuery } from "react-query";
import { ActivityIndicator, MD2Colors } from "react-native-paper";
import { View } from "react-native";
import * as SecureStore from "expo-secure-store";
const NavigationProvider = () => {
	const appContext = useAppContext();
	const { isLoading } = useQuery(
		"user",
		async () => {
			return await SecureStore.getItemAsync("user");
		},
		{
			onSuccess: (data) => {
				if (data) {
					appContext.setUser(JSON.parse(data));
				}
			},
		}
	);

	if (isLoading)
		return (
			<View style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
				<ActivityIndicator animating={true} size={"large"} color={MD2Colors.blue600} />
			</View>
		);
	if (!appContext.user) return <NotAuthNavigation />;
	if (appContext.user.role === "OFFICER") {
		return <OfficerNavigation />;
	} else {
		return <CustomerNavigation />;
	}
};

export default NavigationProvider;
