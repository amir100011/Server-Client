//
// Created by dor on 04/01/18.
//

#ifndef CLIENT_TASKREADFROMSOCKET_H
#define CLIENT_TASKREADFROMSOCKET_H

#include "Task.h"

class TaskReadFromSocket : public Task{
public:
    TaskReadFromSocket(ConnectionHandler& handler):Task(handler){}
    void operator()();
};


#endif //CLIENT_TASKREADFROMSOCKET_H
