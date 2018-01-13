//
// Created by dor on 03/01/18.
//

#include "../include/connectionHandler.h"
#include <boost/thread.hpp>
#include "../include/TaskReadFromSocket.h"
#include "../include/TaskReadFromKeyBoard.h"
#include "../include/GlobalSignout.h"

int signout = 0;

int main (int argc, char *argv[]) {
    //Connect to socket//R

    //std::string host = argv[1];
    //short port = atoi(argv[2]);

    std::string host = "127.0.0.1";
    int port = 7777;
        ConnectionHandler Handler(host, port);
        if (!Handler.connect()) {
            std::cerr << "Cannot connect to " <<host << ":" << port << std::endl;
            return 1;
        }
        TaskReadFromSocket SocketRead(Handler);
        TaskReadFromKeyBoard KeyBoardRead(Handler);//here he reads from keyboard then send it to the server
        boost::thread th1(SocketRead);
        boost::thread th2(KeyBoardRead);
        th1.join();
        th2.join();
       /* delete(&SocketRead);
        delete(&KeyBoardRead);
        delete(&th1);
        delete(&th2);*/






    return 0;
}
