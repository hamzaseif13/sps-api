import { StyleSheet, Text, View } from "react-native";
import React from "react";
import { MaterialIcons} from "@expo/vector-icons";
import useTag from "../../hooks/useTag";

interface Props {
	totalSpaces: number;
	availableSpaces: number;
	tag:string
}
const ParkingMarker: React.FC<Props> = ({availableSpaces,totalSpaces,tag}) => {
	const [tagHeader,tagBody] = useTag(tag)
	return (
		<View style={[styles.container,availableSpaces>0 ? styles.avail : styles.notAvail]}>
          <Text style={styles.tagHeader}>{tag.split("-")[0]}-
		  	<Text style={styles.tagBody}>{tag.split("-")[1]}</Text>
		  </Text>
		</View>
	);
};

export default ParkingMarker;

const styles = StyleSheet.create({
	container: {
		backgroundColor:"white",
		borderWidth:4,
		borderRadius:15,
		padding:5,
		
		
	},icon:{
        backgroundColor:"#4169e1",
    },tagHeader:{
		fontSize:15,
		fontWeight:"bold",
		color:"black"
	}
	,tagBody:{
		fontSize:15,
		fontWeight:"bold",
		color:"#4169e1"
	},
	avail:{
		borderColor:"green"
	},notAvail:{
		borderColor:"red"
	}
});
