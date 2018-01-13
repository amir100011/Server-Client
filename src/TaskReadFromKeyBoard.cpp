//
// Created by dor on 04/01/18.
//

#include "../include/TaskReadFromKeyBoard.h"
#include "../include/GlobalSignout.h"

void TaskReadFromKeyBoard::operator()() {
        while (!std::cin.eof() & !signout) {
            const short bufsize = 1024;
            char buf[bufsize];
            std::cin.getline(buf, bufsize);
            std::string line(buf);
            //int len=line.length();
            if (!(_handler).sendLine(line)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }

        }
    }
