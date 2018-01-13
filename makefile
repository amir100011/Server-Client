
LDFLAGS:=-lboost_system -lboost_thread

#All Targets
all: BBclient

#Tool invocations

BBclient: bin/Client.o bin/connectionHandler.o bin/TaskReadFromKeyBoard.o bin/TaskReadFromSocket.o
		@echo 'Building target: BBclient'
		@echo 'Invoking: C++ Linker'
		g++ -o bin/BBclient bin/Client.o bin/connectionHandler.o  bin/TaskReadFromKeyBoard.o bin/TaskReadFromSocket.o $(LDFLAGS)
	@echo 'Finished building target: BBclient'
	@echo ' '

bin/Client.o: src/Client.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Client.o src/Client.cpp

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/connectionHandler.o src/connectionHandler.cpp

# Depends on the source and header files
bin/TaskReadFromKeyBoard.o: src/TaskReadFromKeyBoard.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/TaskReadFromKeyBoard.o src/TaskReadFromKeyBoard.cpp

# Depends on the source and header files
bin/TaskReadFromSocket.o: src/TaskReadFromSocket.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/TaskReadFromSocket.o src/TaskReadFromSocket.cpp

.PHONY: clean

#Clean the build directory
clean:
	rm -f bin/*