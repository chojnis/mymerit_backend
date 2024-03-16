package com.mymerit.mymerit.domain.programexecution;

public interface ProgramExecutionModule {

    ProgramExecutionId execute(ExecuteProgramRequest request);

    ExecuteProgramResult getResult(ProgramExecutionId programExecutionId);

}
