# Use an official Golang runtime as a parent image
FROM golang:1.21 AS build

WORKDIR /app

COPY . .

# Build the Go app
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o prac9 ./cmd/prac9

# Use an official Nginx runtime as a parent image
FROM nginx:latest

# Set the working directory in the Nginx container
WORKDIR /usr/share/nginx/html

# Copy the prac9 binary into the Nginx image
COPY --from=build /app/prac9 .

# Copy the nginx configuration file
COPY nginx.conf /etc/nginx/nginx.conf

# Copy wait-for-it.sh
COPY wait-for-it.sh /app/wait-for-it.sh

# Устанавливаем права на выполнение для wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh

# Устанавливаем команду запуска контейнера
CMD ["/app/wait-for-it.sh", "mongodb:27017", "--", "./prac9"]
