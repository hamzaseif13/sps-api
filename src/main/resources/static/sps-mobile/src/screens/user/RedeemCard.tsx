import React from "react";
import { StyleSheet, View, Text, TouchableOpacity } from "react-native";
import CustomSafeAreaView from "../../components/CustomSafeAreaView";
import { Button, TextInput } from "react-native-paper";
import { useMutation, useQueryClient } from "react-query";
import { chargeWallet } from "../../api/customer";
import { Toast } from "react-native-toast-message/lib/src/Toast";
import { useNavigation } from "@react-navigation/native";

export default function RedeemCard() {
	const [amount, setAmount] = React.useState<number>();
    const queryClient = useQueryClient();
    const naviagtion = useNavigation<any>();
	const handleChange = (text: any) => {
		const cleanedValue = text.replace(/[^0-9]/g, "");

		if (cleanedValue !== "") {
			const intValue = parseInt(cleanedValue);
			if (intValue >= 1 && intValue <= 10) {
				setAmount(cleanedValue);
			}
		} else {
			setAmount(undefined);
		}
	};
	const redeem = async () => {
		const resp = await mutateAsync();
		if (resp.isSuccess) {
			Toast.show({
				type: "success",
				text1: "Success",
				text2: "Redeem Card Added Successfully",
			});
            queryClient.refetchQueries("wallet");
            setTimeout(()=>{
                naviagtion.goBack()
            },1000)
		} else {
			Toast.show({
				type: "error",
				text1: "Error",
				text2: "Something Went Wrong",
			});
		}
	};
	let disabled = amount && amount > 1 ? false : true;
	const { mutateAsync, isLoading } = useMutation("redeem", () => chargeWallet(amount!));
	return (
		<CustomSafeAreaView>
			<View style={styles.container}>
				<Text style={{ fontSize: 20, fontWeight: "bold" }}>Enter Your Redeem Card</Text>
				<TextInput
					keyboardType="number-pad"
					value={String(amount || "")}
					mode="outlined"
					onChangeText={handleChange}
				/>
				<Text style={{ marginTop: 5 }}>Amount should be between 1-10</Text>
				<Button
					disabled={disabled}
					loading={isLoading}
					mode="contained"
					onPress={redeem}
					style={{ borderRadius: 10, marginTop: 10 }}
					buttonColor="#4169e1"
				>
					Redeem
				</Button>
			</View>
		</CustomSafeAreaView>
	);
}

const styles = StyleSheet.create({
	container: {
		marginHorizontal: 20,
	},
	button: {
		backgroundColor: "#4169e1",
		padding: 10,
		borderRadius: 10,
		marginTop: 10,
		alignItems: "center",
	},
});
