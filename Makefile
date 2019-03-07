.PHONY: build
build:
	./gradlew build

.PHONY: run
run:
	java -jar build/libs/books-0.0.1-SNAPSHOT.jar
