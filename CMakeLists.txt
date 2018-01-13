cmake_minimum_required(VERSION 3.8)
project(Client)

set(CMAKE_CXX_STANDARD 11)
link_libraries(boost_system boost_thread)
set(SOURCE_FILES
        src/connectionHandler.cpp

        include/connectionHandler.h
        src/Client.cpp
        src/TaskReadFromKeyBoard.cpp
        include/TaskReadFromKeyBoard.h
        src/TaskReadFromSocket.cpp

        include/TaskReadFromSocket.h include/Task.h include/GlobalSignout.h)

add_executable(Client ${SOURCE_FILES} )