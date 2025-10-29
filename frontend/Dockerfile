#  Build the applica
FROM node:18-alpine AS build
WORKDIR /app

#  Copying packge files
COPY package*.json ./

# Install ALL dependencies  for building
RUN npm ci

# Copy source code
COPY . .

# Build  application
RUN npm run build

#  Serve the built application with Nginx 
FROM nginx:alpine

# Copy the build output from the previous stage
COPY --from=build /app/dist /usr/share/nginx/html

# Copy custom Nginx config //optional
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
