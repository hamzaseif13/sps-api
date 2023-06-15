import { StyleSheet, Text, View } from "react-native";
import React, { createContext, useContext } from "react";
import { Car } from "../interface/Car";

interface AppContextType {
	user?: {
		role: "OFFICER" | "CUSTOMER";
		jwtToken:string
		email:string
		firstName:string
		lastName:string
	};
	setUser: any;
	car?:Car,
	setCar?:any
}
const appContextDefaultValues: AppContextType = {
	setUser: null,
	
};
const AppContextSPS = createContext<AppContextType>(appContextDefaultValues);

interface Props {
	children: React.ReactNode;
}

export const useAppContext = () => {
	return useContext(AppContextSPS);
};

const AppContext = ({ children }: Props) => {
	const [user, setUser] = React.useState<AppContextType["user"]>();
	const [car,setCar] = React.useState<AppContextType["car"]>();
	return <AppContextSPS.Provider value={{ user, setUser,car,setCar }}>{children}</AppContextSPS.Provider>;
};

export default AppContext;
