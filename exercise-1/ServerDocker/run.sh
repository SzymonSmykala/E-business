cp -r ../../exercise-2 ./exercise-2
docker build -t exercise-1 . && docker run -p 9000:9000 -it exercise-1 
