import * as SecureStore from "expo-secure-store"

export const getJwtToken = async () => {
    const userjson =  await SecureStore.getItemAsync("user");
    if(!userjson) return null;
    const user = JSON.parse(userjson);
    return user.jwtToken as string
}