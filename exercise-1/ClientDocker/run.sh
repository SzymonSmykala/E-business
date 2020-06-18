#cp -r ../../client ./client
docker build -t client . && docker run -p 3000:3000 -it client 
