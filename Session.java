package homework;
import java.util.List;
import java.util.ArrayList;


public class Session implements Comparable<Session>
{
    protected int minCap;
    protected int maxCap;
    public List<Talk> talks;

    protected Session(int minCap, int maxCap)
    {
        this.minCap = minCap;
        this.maxCap = maxCap;
        this.talks = new ArrayList<Talk>();
    }

    public List<Talk> getTalks()
    {
        return this.talks;
    }

    protected boolean canAdd(Talk talk)
    {
        return (this.getCapacityLeft() >= talk.getDuration());
    }

    public boolean tryAdd(Talk talk)
    {
        if(this.canAdd(talk))
        {
            this.addTalk(talk);
            return true;
        }
        return false;
    }

    public void addTalk(Talk talk)
    {
        this.talks.add(talk);
    }

    public int getMaxCapacity()
    {
        return this.maxCap;
    }

    public int getMinCapacity()
    {
        return this.minCap;
    }

    public int getCapacityLeft()
    {
        return getMaxCapacity() - ConferenceUtil.getTotalDuration(this.talks);
    }

    public void reset()
    {
        this.talks.clear();
    }

    public boolean isMorningSession()
    {
        return this.maxCap == 180;
    }

    public boolean isAfternoonSession()
    {
        return this.maxCap == 240;
    }

    @Override
    public int compareTo(Session s)
    {
        int myLeft = this.getCapacityLeft();
        int yourLeft = s.getCapacityLeft();

        if(myLeft > yourLeft)
            return 1;
        else if(myLeft < yourLeft)
            return -1;
        else return 0;
    }

    public static Session create(int minCapacity, int maxCapacity) throws Exception
    {
        if(minCapacity < 0)
            throw new Exception("Session's min capacity must > 0");

        if(maxCapacity < minCapacity)
            throw new Exception("Session's max capacity should large then it's min capacity.");

        return new Session(minCapacity, maxCapacity);
    }
}
