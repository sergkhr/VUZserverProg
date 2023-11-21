# Use an official Golang runtime as a parent image
FROM golang:1.21 AS build

WORKDIR /app

COPY . .

# Build the Go app
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o prac8 ./cmd/prac8

# Use an official Nginx runtime as a parent image
FROM nginx:latest

# Set the working directory in the Nginx container
WORKDIR /usr/share/nginx/html

# Copy the prac8 binary into the Nginx image
COPY --from=build /app/prac8 .

# Copy the nginx configuration file
COPY nginx.conf /etc/nginx/nginx.conf

# Set the command to start prac8
CMD ["./prac8"]