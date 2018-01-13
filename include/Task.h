//
// Created by dor on 04/01/18.
//

#ifndef CLIENT_TASK_H
#define CLIENT_TASK_H

#include "connectionHandler.h"

class Task {
protected:
    ConnectionHandler &_handler;
public:
    Task(ConnectionHandler&handler):_handler(handler){};
   // ~Task();
};


#endif //CLIENT_TASK_H
