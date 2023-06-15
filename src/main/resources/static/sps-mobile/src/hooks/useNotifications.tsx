import React, { useEffect, useRef, useState } from "react";
import * as Device from "expo-device";
import * as Notifications from "expo-notifications";
import { Platform,PushNotification } from "react-native";


Notifications.setNotificationHandler({
    handleNotification: async () => ({
      shouldShowAlert: true,
      shouldPlaySound: true,
      shouldSetBadge: true,
    }),
  });
  
export default function useNotifications(){
	const [expoPushToken, setExpoPushToken] = useState("");
	const [notification, setNotification] = useState<any>(false);
	const notificationListener = useRef<any>();
	const responseListener = useRef<any>();

	const handleNotification = async (ms:number) => {
		await Notifications.scheduleNotificationAsync({
			content: {
				title: "5 minutes left",
				body: "your parking session is about to end, extend it so you wont be reported to police",
				data: { data: "goes here" },
				sound: true,
			},
			trigger: { seconds: ms },
		});
	};
	useEffect(() => {
		registerForPushNotificationsAsync().then((token) => setExpoPushToken(token!));

		notificationListener.current = Notifications.addNotificationReceivedListener((notification) => {
			setNotification(notification);
		});

		responseListener.current = Notifications.addNotificationResponseReceivedListener((response) => {
		});

		return () => {
			Notifications.removeNotificationSubscription(notificationListener.current);
			Notifications.removeNotificationSubscription(responseListener.current);
		};
	}, []);
	return handleNotification;
}
async function registerForPushNotificationsAsync() {
	let token;

	if (Platform.OS === "android") {
		await Notifications.setNotificationChannelAsync("default", {
			name: "default",
			importance: Notifications.AndroidImportance.MAX,
			vibrationPattern: [0, 250, 250, 250],
			lightColor: "#FF231F7C",
		});
	}

	if (Device.isDevice) {
		const { status: existingStatus } = await Notifications.getPermissionsAsync();
		let finalStatus = existingStatus;
		if (existingStatus !== "granted") {
			const { status } = await Notifications.requestPermissionsAsync();
			finalStatus = status;
		}
		if (finalStatus !== "granted") {
			alert("Failed to get push token for push notification!");
			return;
		}
		token = (
			await Notifications.getExpoPushTokenAsync({
				projectId: "f7d54a1e-cd99-4245-9b00-6061eb567f2d",
			})
		).data;
	} else {
		alert("Must use physical device for Push Notifications");
	}

	return token;
}
