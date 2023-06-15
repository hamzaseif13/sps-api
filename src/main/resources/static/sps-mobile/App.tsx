import { StyleSheet, StatusBar } from "react-native";
import React from "react";
import AppContext from "./src/context/AppContext";
import NavigationProvider from "./src/navigation/NavigationProvider";
import { QueryClient, QueryClientProvider } from "react-query";
import { NavigationContainer } from "@react-navigation/native";
import Toast, { BaseToast } from 'react-native-toast-message';
const toastConfig = {
	success: ({ ...rest }) => (
	  <BaseToast
		{...rest}
		text1Style={{
		  fontSize: 15,
		  fontWeight: 'normal', /* FIXED */
		  color:"green"
		}}
		 text2Style={{
			fontSize:13
		 }}
	  />
	),
	error: ({ ...rest }) => (
		<BaseToast
		  {...rest}
		  text1Style={{
			fontSize: 15,
			fontWeight: 'normal', /* FIXED */
			color:"red"
		  }}
		   text2Style={{
			  fontSize:13
		   }}
		/>
	  )
  };
const queryClient = new QueryClient({defaultOptions:{
	queries:{
		 refetchOnWindowFocus:false,
		 retry: false,
		 refetchOnMount: false,
	}
}});
const App = () => {
	return (
		<NavigationContainer>
			<StatusBar backgroundColor={"#4169e1"}/>
			<QueryClientProvider client={queryClient}>
				<AppContext>
					<NavigationProvider />
				</AppContext>
			</QueryClientProvider>
			<Toast config={toastConfig} />
		</NavigationContainer>
	);
};

export default App;

const styles = StyleSheet.create({});
