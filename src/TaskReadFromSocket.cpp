//
// Created by dor on 04/01/18.
//

#include "../include/connectionHandler.h"
#include "../include/TaskReadFromSocket.h"
#include "../include/GlobalSignout.h"

void TaskReadFromSocket::operator()() {
        std::string answer;
        // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
        // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
       while(!std::cin.eof()){
        if (!(_handler).getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
           std::cout << "<"+ answer ;
           if(answer.find("ACK Signout succeeded") != std::string::npos) {
               signout = 1;
               std::cout << "Ready to exit. Press any key" << std::endl;
               break;
           }
           answer = "";
        }
    }