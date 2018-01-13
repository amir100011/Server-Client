//
// Created by dor on 04/01/18.
//

#ifndef CLIENT_TASKREADFROMKEYBOARD_H
#define CLIENT_TASKREADFROMKEYBOARD_H
#include "Task.h"
class TaskReadFromKeyBoard : public Task{
public:
    TaskReadFromKeyBoard(ConnectionHandler& handler):Task(handler){}
    void operator()();
};



#endif //CLIENT_TASKREADFROMKEYBOARD_H
