# Stage 1: Build the React app
FROM node:17-alpine as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
ENV VITE_MAPS_API_KEY=AIzaSyAupYyWahXTKCmrYYoGmnooVQRTkuwCCkI
ENV VITE_API_URL=http://localhost:8080
ENV VITE_IMG_URL=https://sps-violations.s3.eu-west-3.amazonaws.com/
COPY . .
RUN npm run build

# Stage 2: Serve the app with Nginx
FROM nginx:1.21.1-alpine
COPY --from=build-stage /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 3000
CMD ["nginx", "-g", "daemon off;"]