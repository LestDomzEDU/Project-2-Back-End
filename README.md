Add EXPO_PUBLIC_API_BASE support depending on your way to access this back-end:
# Web
EXPO_PUBLIC_API_BASE=http://localhost:8080 npx expo start

# Android emulator
EXPO_PUBLIC_API_BASE=http://10.0.2.2:8080 npx expo start

# Physical device
EXPO_PUBLIC_API_BASE=http://192.168.x.x:8080 npx expo start

When you are running the back-end repo, use these commands:
docker compose up -d

mvn spring-boot:run


In the front end run using these commands:
npm install

Set EXPO_PUBLIC_API_BASE env var

npx expo start -c
