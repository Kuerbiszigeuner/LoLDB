package fachlogik;

public class Lock 
{
    private int runningThreadsNumber;
    
    public Lock() 
    {
        runningThreadsNumber = 0;
    }
 
    public int getRunningThreadsNumber() 
    {
        return runningThreadsNumber;
    }
 
    public void addRunningThread() 
    {
        runningThreadsNumber++;
    }
 
    public void removeRunningThread() 
    {
        runningThreadsNumber--;
    }
}
