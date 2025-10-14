import { useEffect } from "react";
import { SafeAreaView, Text } from "react-native";
import api from "./src/api/client";

export default function App() {
  useEffect(() => {
    (async () => {
      try {
        const res = await api.get("/health");
        console.log("Health:", res.data); // should log { status: "ok" }
      } catch (e) {
        console.log("Health check failed:", e?.message);
      }
    })();
  }, []);

  return (
    <SafeAreaView style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
      <Text>Base URL: {process.env.EXPO_PUBLIC_API_BASE_URL}</Text>
    </SafeAreaView>
  );
}
